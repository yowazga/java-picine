import os
import time
import random
import json
import re
import pandas as pd
from datetime import datetime
from pathlib import Path
from typing import List, Dict, Set, Optional

from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException, NoSuchElementException, StaleElementReferenceException
from webdriver_manager.chrome import ChromeDriverManager
from bs4 import BeautifulSoup
import gspread
from google.oauth2.service_account import Credentials
from dotenv import load_dotenv

# Load environment variables from .env file
load_dotenv()

# Configuration
class Config:
    """Configuration settings for the script."""
    # LinkedIn credentials - loaded from environment variables
    LINKEDIN_USERNAME = os.getenv("LINKEDIN_USERNAME")
    LINKEDIN_PASSWORD = os.getenv("LINKEDIN_PASSWORD")
    
    # Google Sheets configuration
    GOOGLE_SHEETS_CREDENTIALS_FILE = os.getenv("GOOGLE_SHEETS_CREDENTIALS_FILE")
    SPREADSHEET_ID = os.getenv("SPREADSHEET_ID")
    WORKSHEET_NAME = os.getenv("WORKSHEET_NAME", "Combined Data")
    
    # Excel file path
    EXCEL_FILE_PATH = os.getenv("EXCEL_FILE_PATH", "input_data.xlsx")
    FULL_NAME_COLUMN = os.getenv("FULL_NAME_COLUMN", "Full Name")
    
    # Scraping parameters
    MAX_PROFILES_TO_SCRAPE = int(os.getenv("MAX_PROFILES_TO_SCRAPE", "50"))
    MAX_SCROLL_ATTEMPTS = int(os.getenv("MAX_SCROLL_ATTEMPTS", "5"))
    
    # Time delays (in seconds)
    MIN_DELAY = float(os.getenv("MIN_DELAY", "2.0"))
    MAX_DELAY = float(os.getenv("MAX_DELAY", "5.0"))
    PAGE_LOAD_TIMEOUT = int(os.getenv("PAGE_LOAD_TIMEOUT", "30"))
    
    # File paths
    LOG_FILE = Path("linkedin_scraper.log")
    CACHE_FILE = Path("profile_cache.json")


class LinkedInScraper:
    """LinkedIn profile scraper with Excel integration."""
    
    def __init__(self):
        """Initialize the scraper with browser settings and session data."""
        self.driver = None
        self.profile_cache = self._load_cache()
        self.config = Config()
        self._setup_logging()
        
    def _setup_logging(self):
        """Set up a simple logging mechanism."""
        self.log_file = open(self.config.LOG_FILE, 'a', encoding='utf-8')
        self.log(f"Session started at {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
        
    def log(self, message):
        """Log a message to both console and file."""
        timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        formatted_message = f"[{timestamp}] {message}"
        print(formatted_message)
        self.log_file.write(formatted_message + "\n")
        self.log_file.flush()
        
    def _load_cache(self) -> Dict:
        """Load previously scraped profiles from cache file."""
        if Config.CACHE_FILE.exists():
            try:
                with open(Config.CACHE_FILE, 'r', encoding='utf-8') as f:
                    return json.load(f)
            except json.JSONDecodeError:
                return {"profiles": {}}
        return {"profiles": {}}
    
    def _save_cache(self):
        """Save scraped profiles to cache file."""
        with open(Config.CACHE_FILE, 'w', encoding='utf-8') as f:
            json.dump(self.profile_cache, f, ensure_ascii=False, indent=2)
        
    def _setup_browser(self):
        """Set up the browser with optimized settings to avoid detection."""
        options = Options()
        
        # Standard options
        options.add_argument("--start-maximized")
        options.add_argument("--disable-blink-features=AutomationControlled")
        
        # Additional options to avoid detection
        options.add_argument("--disable-extensions")
        options.add_argument("--disable-gpu")
        options.add_argument("--no-sandbox")
        options.add_experimental_option("excludeSwitches", ["enable-automation"])
        options.add_experimental_option("useAutomationExtension", False)
        
        # Random user agent
        user_agents = [
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Safari/605.1.15",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36 Edg/92.0.902.55",
        ]
        options.add_argument(f"user-agent={random.choice(user_agents)}")
        
        # Initialize Chrome with WebDriver Manager (auto-updates ChromeDriver)
        service = Service(ChromeDriverManager().install())
        self.driver = webdriver.Chrome(service=service, options=options)
        self.driver.set_page_load_timeout(self.config.PAGE_LOAD_TIMEOUT)
        
        # Add additional stealth settings
        self.driver.execute_cdp_cmd("Page.addScriptToEvaluateOnNewDocument", {
            "source": """
                Object.defineProperty(navigator, 'webdriver', {
                    get: () => undefined
                });
            """
        })
    
    def _random_delay(self, min_seconds=None, max_seconds=None):
        """Add a random delay to avoid detection."""
        min_seconds = min_seconds or self.config.MIN_DELAY
        max_seconds = max_seconds or self.config.MAX_DELAY
        delay = random.uniform(min_seconds, max_seconds)
        time.sleep(delay)
        
    def _safe_find_element(self, by, selector, timeout=10, retries=3):
        """Safely find an element with retries and error handling."""
        for attempt in range(retries):
            try:
                element = WebDriverWait(self.driver, timeout).until(
                    EC.presence_of_element_located((by, selector))
                )
                return element
            except (TimeoutException, NoSuchElementException, StaleElementReferenceException) as e:
                if attempt == retries - 1:
                    self.log(f"Error finding element {selector}: {str(e)}")
                    return None
                self._random_delay(1, 2)
        return None
    
    def login_to_linkedin(self) -> bool:
        """Log in to LinkedIn with error handling."""
        if not self.config.LINKEDIN_USERNAME or not self.config.LINKEDIN_PASSWORD:
            self.log("❌ LinkedIn credentials not found in environment variables.")
            return False
            
        try:
            self.log("🔄 Navigating to LinkedIn login page...")
            self.driver.get("https://www.linkedin.com/login")
            
            # Wait for login page and enter credentials
            username_field = self._safe_find_element(By.ID, "username")
            password_field = self._safe_find_element(By.ID, "password")
            
            if not username_field or not password_field:
                self.log("❌ Login form not found")
                return False
                
            # Type like a human - with random delays between keystrokes
            self._type_like_human(username_field, self.config.LINKEDIN_USERNAME)
            self._random_delay(0.5, 1.5)
            self._type_like_human(password_field, self.config.LINKEDIN_PASSWORD)
            self._random_delay(0.5, 1.5)
            
            # Click the sign-in button
            password_field.send_keys(Keys.RETURN)
            
            # Wait for successful login (feed page or security verification)
            try:
                WebDriverWait(self.driver, 15).until(
                    lambda driver: "feed" in driver.current_url or "checkpoint" in driver.current_url
                )
                
                # Check if security verification is required
                if "checkpoint" in self.driver.current_url:
                    self.log("⚠️ Security verification required. Please complete it manually.")
                    # Wait for manual verification (up to 2 minutes)
                    WebDriverWait(self.driver, 120).until(lambda driver: "feed" in driver.current_url)
                
                self.log("✅ Successfully logged in to LinkedIn.")
                return True
                
            except TimeoutException:
                self.log("❌ Login failed or timeout occurred.")
                return False
                
        except Exception as e:
            self.log(f"❌ Login error: {str(e)}")
            return False
    
    def _type_like_human(self, element, text):
        """Type text like a human with random delays between keystrokes."""
        for char in text:
            element.send_keys(char)
            time.sleep(random.uniform(0.05, 0.2))
    
    def search_by_name(self, full_name: str) -> Set[str]:
        """Search for profiles by full name."""
        profile_links = set()
        self.log(f"🔍 Searching for: '{full_name}'")
        
        try:
            # Format the query for LinkedIn search
            search_url = f"https://www.linkedin.com/search/results/people/?keywords={full_name.replace(' ', '%20')}&origin=GLOBAL_SEARCH_HEADER"
            self.driver.get(search_url)
            self._random_delay(2, 4)
            
            # Get initial set of profiles
            profile_links = self._extract_profile_links()
            
            # Scroll for more results if needed
            scroll_count = 0
            prev_count = len(profile_links)
            
            while scroll_count < self.config.MAX_SCROLL_ATTEMPTS and len(profile_links) < 5:  # Limit to top 5 results
                self._scroll_page()
                self._random_delay(2, 3)
                
                new_links = self._extract_profile_links()
                profile_links.update(new_links)
                
                # If no new profiles found after scrolling, break
                if len(profile_links) == prev_count:
                    scroll_count += 1
                else:
                    prev_count = len(profile_links)
                    scroll_count = 0
            
            self.log(f"  Found {len(profile_links)} profiles for '{full_name}'")
            
        except Exception as e:
            self.log(f"❌ Error during search for '{full_name}': {str(e)}")
            self._random_delay(5, 10)  # Longer delay after error
            
        return profile_links
    
    def _extract_profile_links(self) -> Set[str]:
        """Extract profile links from the current search results page."""
        links = set()
        try:
            # Find all profile links with standard selector
            elements = self.driver.find_elements(By.CSS_SELECTOR, "a[href*='/in/']")
            for element in elements:
                href = element.get_attribute("href")
                if href and "/in/" in href:
                    # Clean the URL to remove tracking parameters
                    clean_url = href.split("?")[0]
                    links.add(clean_url)
        except Exception as e:
            self.log(f"Error extracting links: {str(e)}")
        return links
    
    def _scroll_page(self):
        """Scroll down the page with human-like behavior."""
        try:
            # Get current scroll height
            current_height = self.driver.execute_script("return document.body.scrollHeight")
            
            # Scroll down in smaller increments (more human-like)
            scroll_increments = random.randint(3, 5)
            for i in range(scroll_increments):
                scroll_amount = (i + 1) * (current_height / scroll_increments)
                self.driver.execute_script(f"window.scrollTo(0, {scroll_amount});")
                time.sleep(random.uniform(0.3, 0.7))
                
            # Check for "Show more results" button and click if present
            try:
                show_more_button = self.driver.find_element(By.XPATH, "//button[contains(.,'Show more results')]")
                if show_more_button.is_displayed():
                    show_more_button.click()
                    self._random_delay(2, 3)
            except (NoSuchElementException, StaleElementReferenceException):
                pass
                
        except Exception as e:
            self.log(f"Error during scroll: {str(e)}")
    
    def scrape_profile(self, url: str) -> Optional[Dict]:
        """Scrape data from a LinkedIn profile."""
        # Check if profile is already in cache
        if url in self.profile_cache["profiles"]:
            self.log(f"📋 Using cached data for {url}")
            return self.profile_cache["profiles"][url]
            
        self.log(f"🔍 Scraping profile: {url}")
        
        try:
            self.driver.get(url)
            self._random_delay(3, 6)
            
            # Add some natural browsing behavior
            self._scroll_page_naturally()
            
            # Parse the page with BeautifulSoup
            soup = BeautifulSoup(self.driver.page_source, "html.parser")
            
            # Extract profile data with multiple fallback selectors
            profile_data = {
                "url": url,
                "scraped_at": datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
            }
            
            # Basic info extraction with fallbacks
            profile_data["name"] = self._extract_text(soup, [
                "h1.text-heading-xlarge",
                "div.ph5.pb5 h1",
                "h1.inline.t-24.t-black.t-normal"
            ])
            
            profile_data["headline"] = self._extract_text(soup, [
                "div.text-body-medium.break-words",
                ".pv-text-details__left-panel.mt2 .text-body-medium",
                ".ph5.pb5 .text-body-medium"
            ])
            
            profile_data["location"] = self._extract_text(soup, [
                "span.text-body-small.inline.t-black--light.break-words",
                ".pv-text-details__left-panel.mt2 span.text-body-small",
                ".ph5.pb5 span.text-body-small"
            ])
            
            # Extract education information
            education_elements = soup.select("section.education-section li") or soup.select("#education-section li")
            profile_data["education"] = []
            
            for edu in education_elements:
                school = self._extract_text(edu, ["h3", ".pv-entity__school-name"])
                degree = self._extract_text(edu, [".pv-entity__degree-name", ".pv-entity__secondary-title"])
                date_range = self._extract_text(edu, [".pv-entity__dates span:not(.visually-hidden)", "time"])
                
                if school and school != "Non trouvé":
                    profile_data["education"].append({
                        "school": school,
                        "degree": degree,
                        "date_range": date_range
                    })
            
            # Extract experience information
            experience_elements = soup.select("section.experience-section li") or soup.select("#experience-section li")
            profile_data["experience"] = []
            
            for exp in experience_elements:
                company = self._extract_text(exp, ["h3", ".pv-entity__company-summary-info h3", "p.pv-entity__secondary-title"])
                role = self._extract_text(exp, [".pv-entity__summary-info-v2 h3", ".pv-entity__summary-info h3", "p.pv-entity__primary-title"])
                date_range = self._extract_text(exp, [".pv-entity__date-range span:not(.visually-hidden)", "time"])
                
                if company and company != "Non trouvé":
                    profile_data["experience"].append({
                        "company": company,
                        "role": role,
                        "date_range": date_range
                    })
            
            # Extract skills information
            skills_elements = soup.select("section.skills-section li") or soup.select("#skills-section li")
            profile_data["skills"] = []
            
            for skill in skills_elements:
                skill_name = self._extract_text(skill, [".pv-skill-category-entity__name", "span"])
                if skill_name and skill_name != "Non trouvé":
                    profile_data["skills"].append(skill_name)
            
            # Add to cache
            self.profile_cache["profiles"][url] = profile_data
            self._save_cache()
            
            self.log(f"✅ Successfully scraped: {profile_data['name']}")
            return profile_data
            
        except Exception as e:
            self.log(f"❌ Error scraping profile {url}: {str(e)}")
            return None
    
    def _extract_text(self, soup, selectors):
        """Extract text using multiple fallback selectors."""
        for selector in selectors:
            try:
                elements = soup.select(selector)
                if elements:
                    return elements[0].get_text(strip=True)
            except Exception:
                pass
        return "Non trouvé"
    
    def _scroll_page_naturally(self):
        """Scroll the profile page in a natural human-like way."""
        try:
            # Get page height
            total_height = self.driver.execute_script("return document.body.scrollHeight")
            viewport_height = self.driver.execute_script("return window.innerHeight")
            
            # Scroll in smaller increments with pauses
            current_position = 0
            while current_position < total_height:
                # Random scroll amount
                scroll_amount = random.randint(200, 400)
                current_position += scroll_amount
                
                # Scroll to new position
                self.driver.execute_script(f"window.scrollTo(0, {current_position});")
                
                # Random pause to simulate reading
                read_pause = random.uniform(0.5, 2.0)
                time.sleep(read_pause)
                
                # Sometimes pause longer at interesting sections (random)
                if random.random() < 0.2:
                    time.sleep(random.uniform(1.0, 3.0))
                    
        except Exception as e:
            self.log(f"Error during natural scrolling: {str(e)}")
    
    def load_excel_data(self) -> pd.DataFrame:
        """Load data from Excel file."""
        try:
            self.log(f"📊 Loading Excel data from {self.config.EXCEL_FILE_PATH}")
            df = pd.read_excel(self.config.EXCEL_FILE_PATH)
            
            # Check if full name column exists
            if self.config.FULL_NAME_COLUMN not in df.columns:
                self.log(f"❌ Full name column '{self.config.FULL_NAME_COLUMN}' not found in Excel file.")
                self.log(f"Available columns: {', '.join(df.columns)}")
                return None
                
            self.log(f"✅ Successfully loaded {len(df)} rows from Excel file.")
            return df
            
        except Exception as e:
            self.log(f"❌ Error loading Excel data: {str(e)}")
            return None
    
    def process_excel_data(self, df: pd.DataFrame) -> pd.DataFrame:
        """Process Excel data by searching and scraping each person's LinkedIn profile."""
        if df is None or len(df) == 0:
            self.log("❌ No Excel data to process.")
            return None
            
        # Add LinkedIn data columns
        linkedin_columns = [
            "LinkedIn URL", "LinkedIn Headline", "LinkedIn Location",
            "Current Company", "Current Role", "Education", "Skills"
        ]
        
        for col in linkedin_columns:
            df[col] = ""
            
        # Process each row
        for idx, row in df.iterrows():
            full_name = row[self.config.FULL_NAME_COLUMN]
            
            if not full_name or pd.isna(full_name):
                self.log(f"⚠️ Skipping row {idx+1}: No name found.")
                continue
                
            self.log(f"🔄 Processing {idx+1}/{len(df)}: {full_name}")
            
            # Search for the profile
            profile_links = self.search_by_name(full_name)
            
            if not profile_links:
                self.log(f"⚠️ No LinkedIn profile found for: {full_name}")
                continue
                
            # Get the first profile (most relevant match)
            profile_url = next(iter(profile_links))
            
            # Scrape the profile
            profile_data = self.scrape_profile(profile_url)
            
            if profile_data:
                # Update DataFrame with LinkedIn data
                df.at[idx, "LinkedIn URL"] = profile_data["url"]
                df.at[idx, "LinkedIn Headline"] = profile_data.get("headline", "")
                df.at[idx, "LinkedIn Location"] = profile_data.get("location", "")
                
                # Current position
                if profile_data.get("experience") and len(profile_data["experience"]) > 0:
                    df.at[idx, "Current Company"] = profile_data["experience"][0].get("company", "")
                    df.at[idx, "Current Role"] = profile_data["experience"][0].get("role", "")
                
                # Education and skills as formatted strings
                education_str = "; ".join([f"{e.get('school', '')} - {e.get('degree', '')}" 
                                         for e in profile_data.get("education", [])])
                df.at[idx, "Education"] = education_str
                
                skills_str = ", ".join(profile_data.get("skills", []))
                df.at[idx, "Skills"] = skills_str
            
            # Add a delay between profiles
            self._random_delay(3, 8)
            
        return df
    
    def save_to_google_sheets(self, df: pd.DataFrame) -> bool:
        """Save the combined data to Google Sheets."""
        if df is None or len(df) == 0:
            self.log("❌ No data to save to Google Sheets.")
            return False
            
        try:
            self.log("🔄 Connecting to Google Sheets...")
            
            # Set up credentials
            scope = [
                "https://spreadsheets.google.com/feeds",
                "https://www.googleapis.com/auth/spreadsheets",
                "https://www.googleapis.com/auth/drive"
            ]
            
            creds = Credentials.from_service_account_file(
                self.config.GOOGLE_SHEETS_CREDENTIALS_FILE, 
                scopes=scope
            )
            
            client = gspread.authorize(creds)
            
            # Open spreadsheet and worksheet
            spreadsheet = client.open_by_key(self.config.SPREADSHEET_ID)
            
            # Check if worksheet exists, create if not
            try:
                worksheet = spreadsheet.worksheet(self.config.WORKSHEET_NAME)
                # Clear existing data
                worksheet.clear()
            except gspread.exceptions.WorksheetNotFound:
                worksheet = spreadsheet.add_worksheet(
                    title=self.config.WORKSHEET_NAME,
                    rows=len(df) + 1,  # Headers + data
                    cols=len(df.columns)
                )
            
            df = df.fillna("")

            # Convert DataFrame to list for gspread
            data = [df.columns.tolist()] + df.values.tolist()
            
            # Update the worksheet
            worksheet.update(data)
            
            self.log(f"✅ Successfully saved {len(df)} rows to Google Sheets.")
            return True
            
        except Exception as e:
            self.log(f"❌ Error saving to Google Sheets: {str(e)}")
            return False
    
    def run(self):
        """Run the full process from Excel to Google Sheets."""
        try:
            # Load Excel data
            excel_data = self.load_excel_data()
            if excel_data is None:
                return
            
            # Setup browser
            self.log("🔄 Setting up browser...")
            self._setup_browser()
            
            # Login to LinkedIn
            if not self.login_to_linkedin():
                self.log("❌ Login failed. Exiting.")
                return
            
            # Process the Excel data (search and scrape LinkedIn)
            combined_data = self.process_excel_data(excel_data)
            
            # Save the combined data to Google Sheets
            if combined_data is not None:
                self.save_to_google_sheets(combined_data)
            
            self.log("✅ Process completed successfully.")
            
        except Exception as e:
            self.log(f"❌ Error during execution: {str(e)}")
            
        finally:
            # Clean up
            if self.driver:
                self.driver.quit()
            if self.log_file:
                self.log("🔄 Closing session.")
                self.log_file.close()


if __name__ == "__main__":
    scraper = LinkedInScraper()
    scraper.run()
