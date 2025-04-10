import os
import pandas as pd
import requests
from typing import Optional, Dict, Set, List
import gspread
from google.oauth2.service_account import Credentials

from src.utils.logger import Logger
from src.utils.file_tracker import FileTracker
from src.scrapers.linkedin_scraper import LinkedInScraper

class DataProcessor:
    """Data processor for Excel and Google Sheets integration."""
    
    def __init__(self, linkedin_scraper: LinkedInScraper, logger: Logger, file_tracker: FileTracker, config):
        """Initialize the data processor with a LinkedIn scraper, logger, file tracker, and config."""
        self.linkedin_scraper = linkedin_scraper
        self.logger = logger
        self.file_tracker = file_tracker
        self.config = config
        self.combined_df = None
        
    def load_excel_files(self) -> List[pd.DataFrame]:
        """Load data from all new Excel files in the directory."""
        dataframes = []
        
        # Get list of new Excel files
        new_files = self.file_tracker.get_new_excel_files(self.config.EXCEL_FILES_DIRECTORY)
        
        if not new_files:
            self.logger.log("ℹ️ No new Excel files to process.")
            return dataframes
            
        # Process each new file
        for file_path in new_files:
            try:
                self.logger.log(f"📊 Loading Excel data from {file_path}")
                df = pd.read_excel(file_path)
                
                # Check if full name column exists
                if self.config.FULL_NAME_COLUMN not in df.columns:
                    self.logger.log(f"⚠️ Full name column '{self.config.FULL_NAME_COLUMN}' not found in {file_path}.")
                    self.logger.log(f"Available columns: {', '.join(df.columns)}")
                    continue
                    
                # Add source file column for tracking
                df['Source File'] = os.path.basename(file_path)
                
                self.logger.log(f"✅ Successfully loaded {len(df)} rows from {file_path}.")
                dataframes.append(df)
                
                # Mark file as processed
                self.file_tracker.mark_as_processed(file_path)
                
            except Exception as e:
                self.logger.log(f"❌ Error loading Excel data from {file_path}: {str(e)}")
        
        return dataframes
        
    def get_existing_data(self) -> Optional[pd.DataFrame]:
        """Get existing data from Google Sheets."""
        try:
            self.logger.log("🔄 Retrieving existing data from Google Sheets...")
            
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
            
            try:
                worksheet = spreadsheet.worksheet(self.config.WORKSHEET_NAME)
                
                # Get all data including headers
                data = worksheet.get_all_values()
                
                if not data:
                    self.logger.log("ℹ️ No existing data in Google Sheets.")
                    return None
                    
                # Convert to DataFrame
                headers = data[0]
                rows = data[1:] if len(data) > 1 else []
                
                if not rows:
                    self.logger.log("ℹ️ No existing data rows in Google Sheets (only headers).")
                    return pd.DataFrame(columns=headers)
                
                df = pd.DataFrame(rows, columns=headers)
                self.logger.log(f"✅ Successfully retrieved {len(df)} existing rows from Google Sheets.")
                return df
                
            except gspread.exceptions.WorksheetNotFound:
                self.logger.log(f"ℹ️ Worksheet '{self.config.WORKSHEET_NAME}' not found, will create it.")
                return None
                
        except Exception as e:
            self.logger.log(f"❌ Error retrieving existing data: {str(e)}")
            return None
    
    def process_excel_data(self, dataframes: List[pd.DataFrame]) -> Optional[pd.DataFrame]:
        """Process Excel data by searching and scraping each person's LinkedIn profile."""
        if not dataframes:
            self.logger.log("ℹ️ No new data to process.")
            return None
            
        # Combine all dataframes
        combined_df = pd.concat(dataframes, ignore_index=True)
        
        self.logger.log(f"🔄 Combined {len(combined_df)} rows from {len(dataframes)} files.")
            
        # Add LinkedIn data columns
        linkedin_columns = [
            "LinkedIn URL", "LinkedIn Headline", "LinkedIn Location",
            "Current Company", "Current Role", "Education", "Skills"
        ]
        
        for col in linkedin_columns:
            if col not in combined_df.columns:
                combined_df[col] = ""
            
        # Process each row
        for idx, row in combined_df.iterrows():
            full_name = row[self.config.FULL_NAME_COLUMN]
            
            if not full_name or pd.isna(full_name):
                self.logger.log(f"⚠️ Skipping row {idx+1}: No name found.")
                continue
                
            self.logger.log(f"🔄 Processing {idx+1}/{len(combined_df)}: {full_name}")
            
            # Check if we already have this data in the combined dataframe
            if self.combined_df is not None:
                existing_profiles = self.combined_df[self.combined_df[self.config.FULL_NAME_COLUMN] == full_name]
                if not existing_profiles.empty:
                    # Use existing data if available
                    existing_data = existing_profiles.iloc[0]
                    if existing_data["LinkedIn URL"]:
                        self.logger.log(f"📋 Using existing data for {full_name}")
                        for col in linkedin_columns:
                            combined_df.at[idx, col] = existing_data[col]
                        continue
            
            # Search for the profile
            profile_links = self.linkedin_scraper.search_by_name(full_name)
            
            if not profile_links:
                self.logger.log(f"⚠️ No LinkedIn profile found for: {full_name}")
                continue
                
            # Get the first profile (most relevant match)
            profile_url = next(iter(profile_links))
            
            # Scrape the profile
            profile_data = self.linkedin_scraper.scrape_profile(profile_url)
            
            if profile_data:
                # Update DataFrame with LinkedIn data
                combined_df.at[idx, "LinkedIn URL"] = profile_data["url"]
                combined_df.at[idx, "LinkedIn Headline"] = profile_data.get("headline", "")
                combined_df.at[idx, "LinkedIn Location"] = profile_data.get("location", "")
                
                # Current position
                if profile_data.get("experience") and len(profile_data["experience"]) > 0:
                    combined_df.at[idx, "Current Company"] = profile_data["experience"][0].get("company", "")
                    combined_df.at[idx, "Current Role"] = profile_data["experience"][0].get("role", "")
                
                # Education and skills as formatted strings
                education_str = "; ".join([f"{e.get('school', '')} - {e.get('degree', '')}" 
                                         for e in profile_data.get("education", [])])
                combined_df.at[idx, "Education"] = education_str
                
                skills_str = ", ".join(profile_data.get("skills", []))
                combined_df.at[idx, "Skills"] = skills_str
                
        # Add timestamp column
        if "Last Updated" not in combined_df.columns:
            combined_df["Last Updated"] = pd.Timestamp.now().strftime("%Y-%m-%d %H:%M:%S")
            
        return combined_df
    
    def merge_with_existing_data(self, new_data: pd.DataFrame) -> pd.DataFrame:
        """Merge new data with existing data from Google Sheets."""
        existing_data = self.get_existing_data()
        
        if existing_data is None or len(existing_data) == 0:
            self.logger.log("ℹ️ No existing data to merge, using only new data.")
            return new_data
            
        # Ensure all columns exist in both dataframes
        all_columns = list(set(existing_data.columns) | set(new_data.columns))
        
        for col in all_columns:
            if col not in existing_data.columns:
                existing_data[col] = ""
            if col not in new_data.columns:
                new_data[col] = ""
                
        # Concatenate and remove duplicates, keeping most recent data
        combined = pd.concat([existing_data, new_data], ignore_index=True)
        
        # For duplicates based on full name, keep the row with the latest "Last Updated" value
        if "Last Updated" in combined.columns:
            # Convert "Last Updated" to datetime
            combined["Last Updated"] = pd.to_datetime(combined["Last Updated"], errors='coerce')
            
            # Sort by "Last Updated" in descending order
            combined = combined.sort_values("Last Updated", ascending=False)
            
            # Drop duplicates keeping the first (most recent) occurrence
            combined = combined.drop_duplicates(subset=[self.config.FULL_NAME_COLUMN], keep='first')
            
            # Convert "Last Updated" back to string
            combined["Last Updated"] = combined["Last Updated"].dt.strftime("%Y-%m-%d %H:%M:%S")
        else:
            # If no "Last Updated" column, just drop duplicates keeping the first occurrence
            combined = combined.drop_duplicates(subset=[self.config.FULL_NAME_COLUMN], keep='first')
            
        self.logger.log(f"✅ Successfully merged data. Total unique profiles: {len(combined)}")
        
        # Store the combined dataframe for reference in future runs
        self.combined_df = combined
        
        return combined
    
    def save_to_google_sheets(self, df: pd.DataFrame) -> bool:
        """Save the combined data to Google Sheets."""
        if df is None or len(df) == 0:
            self.logger.log("❌ No data to save to Google Sheets.")
            return False
            
        try:
            self.logger.log("🔄 Connecting to Google Sheets...")
            
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
            
            # If Apps Script deployment ID is provided, trigger it to prepare data for Looker Studio
            if self.config.APPS_SCRIPT_DEPLOYMENT_ID:
                self.trigger_apps_script()
            
            self.logger.log(f"✅ Successfully saved {len(df)} rows to Google Sheets.")
            return True
            
        except Exception as e:
            self.logger.log(f"❌ Error saving to Google Sheets: {str(e)}")
            return False
            
    def trigger_apps_script(self):
        """Trigger Google Apps Script to prepare data for Looker Studio."""
        try:
            if not self.config.APPS_SCRIPT_DEPLOYMENT_ID:
                self.logger.log("ℹ️ No Apps Script deployment ID provided, skipping trigger.")
                return
                
            self.logger.log("🔄 Triggering Google Apps Script to prepare data for Looker Studio...")
            
            # Construct the URL to execute the Apps Script web app
            url = f"https://script.google.com/macros/s/{self.config.APPS_SCRIPT_DEPLOYMENT_ID}/exec"
            
            # Execute the web app
            response = requests.get(url)
            
            if response.status_code == 200:
                self.logger.log("✅ Successfully triggered Google Apps Script.")
            else:
                self.logger.log(f"⚠️ Apps Script trigger returned status code: {response.status_code}")
                
        except Exception as e:
            self.logger.log(f"❌ Error triggering Apps Script: {str(e)}")
            
    def process_data(self):
        """Process all data and save to Google Sheets."""
        # Load new Excel files
        new_dataframes = self.load_excel_files()
        
        if not new_dataframes:
            # If no new files, load existing data from Google Sheets
            existing_data = self.get_existing_data()
            if existing_data is not None:
                self.combined_df = existing_data
                self.logger.log("ℹ️ No new files to process. Using existing data only.")
                return existing_data
            else:
                self.logger.log("ℹ️ No data to process.")
                return None
        
        # Process new Excel data
        processed_data = self.process_excel_data(new_dataframes)
        
        if processed_data is None:
            self.logger.log("ℹ️ No data was processed.")
            return None
        
        # Merge with existing data
        combined_data = self.merge_with_existing_data(processed_data)
        
        return combined_data 