import random
import time
from typing import Optional

from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException, NoSuchElementException, StaleElementReferenceException
from webdriver_manager.chrome import ChromeDriverManager

from src.utils.logger import Logger

class Browser:
    """Browser management for LinkedIn scraping."""
    
    def __init__(self, logger: Logger, page_load_timeout: int = 30):
        """Initialize the browser with a logger and timeout."""
        self.driver = None
        self.logger = logger
        self.page_load_timeout = page_load_timeout
        
    def setup(self):
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
        self.driver.set_page_load_timeout(self.page_load_timeout)
        
        # Add additional stealth settings
        self.driver.execute_cdp_cmd("Page.addScriptToEvaluateOnNewDocument", {
            "source": """
                Object.defineProperty(navigator, 'webdriver', {
                    get: () => undefined
                });
            """
        })
        
        self.logger.log("✅ Browser setup complete")
        
    def safe_find_element(self, by, selector, timeout=10, retries=3):
        """Safely find an element with retries and error handling."""
        for attempt in range(retries):
            try:
                element = WebDriverWait(self.driver, timeout).until(
                    EC.presence_of_element_located((by, selector))
                )
                return element
            except (TimeoutException, NoSuchElementException, StaleElementReferenceException) as e:
                if attempt == retries - 1:
                    self.logger.log(f"Error finding element {selector}: {str(e)}")
                    return None
                time.sleep(random.uniform(1, 2))
        return None
        
    def random_delay(self, min_seconds=2.0, max_seconds=5.0):
        """Add a random delay to avoid detection."""
        delay = random.uniform(min_seconds, max_seconds)
        time.sleep(delay)
        
    def type_like_human(self, element, text):
        """Type text like a human with random delays between keystrokes."""
        for char in text:
            element.send_keys(char)
            time.sleep(random.uniform(0.05, 0.2))
            
    def scroll_page_naturally(self):
        """Scroll the page in a natural human-like way."""
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
            self.logger.log(f"Error during natural scrolling: {str(e)}")
            
    def close(self):
        """Close the browser."""
        if self.driver:
            self.driver.quit() 