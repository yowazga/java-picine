# LinkedIn Profile Scraper

A tool to scrape LinkedIn profiles based on names from Excel files and save the combined data to Google Sheets for use with Google Looker Studio.

## Features

- Scrapes LinkedIn profiles based on names from multiple Excel files
- Processes new files incrementally, tracking which files have been processed
- Remembers previous LinkedIn data to avoid re-scraping known profiles
- Extracts profile information including:
  - Name, headline, and location
  - Current company and role
  - Education history
  - Skills
- Saves the combined data to Google Sheets
- Includes Google Apps Script integration for Google Looker Studio
- Implements human-like behavior to avoid detection
- Caches scraped profiles to avoid re-scraping

## Requirements

- Python 3.7+
- Chrome browser
- ChromeDriver (automatically installed by webdriver-manager)
- Google account with access to Google Sheets and Google Looker Studio

## Installation

1. Clone this repository:
   ```
   git clone <repository-url>
   cd linkedin-profile-scraper
   ```

2. Install the required packages:
   ```
   pip install -r requirements.txt
   ```

3. Create a `.env` file with your LinkedIn credentials and Google Sheets configuration:
   ```
   LINKEDIN_USERNAME=your_linkedin_email
   LINKEDIN_PASSWORD=your_linkedin_password
   GOOGLE_SHEETS_CREDENTIALS_FILE=path/to/credentials.json
   SPREADSHEET_ID=your_spreadsheet_id
   WORKSHEET_NAME=Combined Data
   EXCEL_FILES_DIRECTORY=input_data
   FULL_NAME_COLUMN=Full Name
   MAX_PROFILES_TO_SCRAPE=50
   MAX_SCROLL_ATTEMPTS=5
   MIN_DELAY=2.0
   MAX_DELAY=5.0
   PAGE_LOAD_TIMEOUT=30
   APPS_SCRIPT_DEPLOYMENT_ID=your_apps_script_deployment_id
   ```

4. Create a directory for Excel input files:
   ```
   mkdir input_data
   ```

5. Place your Excel files in the `input_data` directory. Each file should have a column with full names (defined by `FULL_NAME_COLUMN` in the .env file).

## Usage

Run the script:
```
python -m src.main
```

The script will:
1. Load any new Excel files from the input directory
2. Extract names from these files
3. Search for and scrape LinkedIn profiles for each name
4. Combine the new data with existing data in Google Sheets
5. Trigger a Google Apps Script to prepare the data for Looker Studio

## Google Apps Script Setup

1. In your Google Sheet, go to Extensions → Apps Script
2. Create a new script and paste the contents of `google_apps_script.js`
3. Deploy the script as a web app:
   - Click Deploy → New deployment
   - Select "Web app" as the deployment type
   - Set "Who has access" to appropriate level (e.g., "Anyone" or "Anyone with Google account")
   - Click "Deploy"
   - Copy the deployment ID and paste it into your .env file as `APPS_SCRIPT_DEPLOYMENT_ID`

## Google Looker Studio Integration

1. Go to [Google Looker Studio](https://lookerstudio.google.com/)
2. Create a new data source, selecting your Google Sheet
3. Choose the "Dashboard Data" sheet as your data source
4. Create visualizations based on the prepared data, such as:
   - Profile completeness by person
   - Number of people with/without LinkedIn profiles
   - Distribution of skills or education
   - Current companies and roles

## Project Structure

```
linkedin-profile-scraper/
├── src/
│   ├── __init__.py
│   ├── main.py
│   ├── utils/
│   │   ├── __init__.py
│   │   ├── config.py
│   │   ├── logger.py
│   │   ├── cache.py
│   │   ├── browser.py
│   │   └── file_tracker.py
│   ├── scrapers/
│   │   ├── __init__.py
│   │   └── linkedin_scraper.py
│   └── data/
│       ├── __init__.py
│       └── data_processor.py
├── input_data/
│   └── (your Excel files)
├── .env
├── google_apps_script.js
├── requirements.txt
└── README.md
```

## How It Works

1. **File Tracking**: The script keeps track of processed Excel files to avoid duplicating work.
2. **Incremental Processing**: New Excel files are processed and merged with existing data.
3. **Profile Caching**: LinkedIn profiles are cached to avoid re-scraping the same profiles.
4. **Google Apps Script Integration**: After updating Google Sheets, an Apps Script prepares the data for Looker Studio.
5. **Data Continuity**: Each run builds on the previous data, maintaining a growing dataset.

## Disclaimer

This tool is for educational purposes only. Scraping LinkedIn may violate their Terms of Service. Use at your own risk.

## License

MIT 