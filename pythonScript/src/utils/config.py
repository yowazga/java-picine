import os
from pathlib import Path
from dotenv import load_dotenv

# Load environment variables from .env file
load_dotenv()

class Config:
    """Configuration settings for the script."""
    # LinkedIn credentials - loaded from environment variables
    LINKEDIN_USERNAME = os.getenv("LINKEDIN_USERNAME")
    LINKEDIN_PASSWORD = os.getenv("LINKEDIN_PASSWORD")
    
    # Google Sheets configuration
    GOOGLE_SHEETS_CREDENTIALS_FILE = os.getenv("GOOGLE_SHEETS_CREDENTIALS_FILE")
    SPREADSHEET_ID = os.getenv("SPREADSHEET_ID")
    WORKSHEET_NAME = os.getenv("WORKSHEET_NAME", "Combined Data")
    APPS_SCRIPT_DEPLOYMENT_ID = os.getenv("APPS_SCRIPT_DEPLOYMENT_ID", "")
    
    # Excel files configuration
    EXCEL_FILES_DIRECTORY = os.getenv("EXCEL_FILES_DIRECTORY", "input_data")
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
    PROCESSED_FILES_RECORD = Path("processed_files.json") 