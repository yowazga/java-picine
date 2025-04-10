import time
import random
from datetime import datetime
from typing import Dict, Set, Optional, List

from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.common.exceptions import TimeoutException
from bs4 import BeautifulSoup

from src.utils.browser import Browser
from src.utils.logger import Logger
from src.utils.cache import ProfileCache

class LinkedInScraper:
    """LinkedIn profile scraper."""
    
    def __init__(self, browser: Browser, logger: Logger, cache: ProfileCache, config):
        """Initialize the scraper with browser, logger, cache, and config."""
        self.browser = browser
        self.logger = logger
        self.cache = cache
        self.config = config
        
    def login(self) -> bool:
        """Log in to LinkedIn with error handling."""
        if not self.config.LINKEDIN_USERNAME or not self.config.LINKEDIN_PASSWORD:
            self.logger.log("❌ LinkedIn credentials not found in environment variables.")
            return False
            
        try:
            self.logger.log("🔄 Navigating to LinkedIn login page...")
            self.browser.driver.get("https://www.linkedin.com/login")
            
            # Wait for login page and enter credentials
            username_field = self.browser.safe_find_element(By.ID, "username")
            password_field = self.browser.safe_find_element(By.ID, "password")
            
            if not username_field or not password_field:
                self.logger.log("❌ Login form not found")
                return False
                
            # Type like a human - with random delays between keystrokes
            self.browser.type_like_human(username_field, self.config.LINKEDIN_USERNAME)
            self.browser.random_delay(0.5, 1.5)
            self.browser.type_like_human(password_field, self.config.LINKEDIN_PASSWORD)
            self.browser.random_delay(0.5, 1.5)
            
            # Click the sign-in button
            password_field.send_keys(Keys.RETURN)
            
            # Wait for successful login (feed page or security verification)
            try:
                WebDriverWait(self.browser.driver, 15).until(
                    lambda driver: "feed" in driver.current_url or "checkpoint" in driver.current_url
                )
                
                # Check if security verification is required
                if "checkpoint" in self.browser.driver.current_url:
                    self.logger.log("⚠️ Security verification required. Please complete it manually.")
                    # Wait for manual verification (up to 2 minutes)
                    WebDriverWait(self.browser.driver, 120).until(lambda driver: "feed" in driver.current_url)
                
                self.logger.log("✅ Successfully logged in to LinkedIn.")
                return True
                
            except TimeoutException:
                self.logger.log("❌ Login failed or timeout occurred.")
                return False
                
        except Exception as e:
            self.logger.log(f"❌ Login error: {str(e)}")
            return False
    
    def search_by_name(self, full_name: str) -> Set[str]:
        """Search for profiles by full name."""
        profile_links = set()
        self.logger.log(f"🔍 Searching for: '{full_name}'")
        
        try:
            # Format the query for LinkedIn search
            search_url = f"https://www.linkedin.com/search/results/people/?keywords={full_name.replace(' ', '%20')}&origin=GLOBAL_SEARCH_HEADER"
            self.browser.driver.get(search_url)
            self.browser.random_delay(2, 4)
            
            # Get initial set of profiles
            profile_links = self._extract_profile_links()
            
            # Scroll for more results if needed
            scroll_count = 0
            prev_count = len(profile_links)
            
            while scroll_count < self.config.MAX_SCROLL_ATTEMPTS and len(profile_links) < 5:  # Limit to top 5 results
                self._scroll_page()
                self.browser.random_delay(2, 3)
                
                new_links = self._extract_profile_links()
                profile_links.update(new_links)
                
                # If no new profiles found after scrolling, break
                if len(profile_links) == prev_count:
                    scroll_count += 1
                else:
                    prev_count = len(profile_links)
                    scroll_count = 0
            
            self.logger.log(f"  Found {len(profile_links)} profiles for '{full_name}'")
            
        except Exception as e:
            self.logger.log(f"❌ Error during search for '{full_name}': {str(e)}")
            self.browser.random_delay(5, 10)  # Longer delay after error
            
        return profile_links
    
    def _extract_profile_links(self) -> Set[str]:
        """Extract profile links from the current search results page."""
        links = set()
        try:
            # Find all profile links with standard selector
            elements = self.browser.driver.find_elements(By.CSS_SELECTOR, "a[href*='/in/']")
            for element in elements:
                href = element.get_attribute("href")
                if href and "/in/" in href:
                    # Clean the URL to remove tracking parameters
                    clean_url = href.split("?")[0]
                    links.add(clean_url)
        except Exception as e:
            self.logger.log(f"Error extracting links: {str(e)}")
        return links
    
    def _scroll_page(self):
        """Scroll down the page with human-like behavior."""
        try:
            # Get current scroll height
            current_height = self.browser.driver.execute_script("return document.body.scrollHeight")
            
            # Scroll down in smaller increments (more human-like)
            scroll_increments = random.randint(3, 5)
            for i in range(scroll_increments):
                scroll_amount = (i + 1) * (current_height / scroll_increments)
                self.browser.driver.execute_script(f"window.scrollTo(0, {scroll_amount});")
                time.sleep(random.uniform(0.3, 0.7))
                
            # Check for "Show more results" button and click if present
            try:
                show_more_button = self.browser.driver.find_element(By.XPATH, "//button[contains(.,'Show more results')]")
                if show_more_button.is_displayed():
                    show_more_button.click()
                    self.browser.random_delay(2, 3)
            except Exception:
                pass
                
        except Exception as e:
            self.logger.log(f"Error during scroll: {str(e)}")
    
    def scrape_profile(self, url: str) -> Optional[Dict]:
        """Scrape data from a LinkedIn profile."""
        # Check if profile is already in cache
        cached_profile = self.cache.get_profile(url)
        if cached_profile:
            self.logger.log(f"📋 Using cached data for {url}")
            return cached_profile
            
        self.logger.log(f"🔍 Scraping profile: {url}")
        
        try:
            self.browser.driver.get(url)
            self.browser.random_delay(3, 6)
            
            # Add some natural browsing behavior
            self.browser.scroll_page_naturally()
            
            # Parse the page with BeautifulSoup
            soup = BeautifulSoup(self.browser.driver.page_source, "html.parser")
            
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
            self.cache.add_profile(url, profile_data)
            
            self.logger.log(f"✅ Successfully scraped: {profile_data['name']}")
            return profile_data
            
        except Exception as e:
            self.logger.log(f"❌ Error scraping profile {url}: {str(e)}")
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