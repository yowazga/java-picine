#!/usr/bin/env python3
"""
LinkedIn Profile Scraper
------------------------
A tool to scrape LinkedIn profiles based on names from Excel files
and save the combined data to Google Sheets for use with Google Looker Studio.
"""

from src.utils.config import Config
from src.utils.logger import Logger
from src.utils.cache import ProfileCache
from src.utils.file_tracker import FileTracker
from src.utils.browser import Browser
from src.scrapers.linkedin_scraper import LinkedInScraper
from src.data.data_processor import DataProcessor

def main():
    """Main entry point for the LinkedIn scraper."""
    # Initialize configuration
    config = Config()
    
    # Initialize logger
    logger = Logger(config.LOG_FILE)
    logger.log("🚀 Starting LinkedIn Profile Scraper")
    
    try:
        # Initialize cache and file tracker
        cache = ProfileCache(config.CACHE_FILE)
        file_tracker = FileTracker(config.PROCESSED_FILES_RECORD)
        
        # Initialize browser
        browser = Browser(logger, config.PAGE_LOAD_TIMEOUT)
        browser.setup()
        
        # Initialize LinkedIn scraper
        linkedin_scraper = LinkedInScraper(browser, logger, cache, config)
        
        # Initialize data processor
        data_processor = DataProcessor(linkedin_scraper, logger, file_tracker, config)
        
        # Login to LinkedIn
        if not linkedin_scraper.login():
            logger.log("❌ Login failed. Exiting.")
            return
        
        # Process all data (load files, process, merge with existing data)
        combined_data = data_processor.process_data()
        
        # Save combined data to Google Sheets
        if combined_data is not None:
            data_processor.save_to_google_sheets(combined_data)
        
        logger.log("✅ Process completed successfully.")
        
    except Exception as e:
        logger.log(f"❌ Error during execution: {str(e)}")
        
    finally:
        # Clean up
        if 'browser' in locals():
            browser.close()
        logger.close()

if __name__ == "__main__":
    main() 