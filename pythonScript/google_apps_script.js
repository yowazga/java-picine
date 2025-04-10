/**
 * LinkedIn Profile Data Preparation for Looker Studio
 * 
 * This script prepares the LinkedIn profile data in Google Sheets for use
 * with Google Looker Studio. It formats the data, creates calculated fields,
 * and ensures the data is in the correct format for visualization.
 */

// Configuration
const CONFIG = {
  WORKSHEET_NAME: "Combined Data",
  DASHBOARD_WORKSHEET_NAME: "Dashboard Data"
};

/**
 * Main function that gets triggered via the web app URL
 */
function doGet() {
  try {
    prepareDataForLookerStudio();
    return ContentService.createTextOutput("Data prepared successfully for Looker Studio");
  } catch (error) {
    Logger.log("Error: " + error.message);
    return ContentService.createTextOutput("Error: " + error.message)
      .setMimeType(ContentService.MimeType.TEXT);
  }
}

/**
 * Prepare data for Looker Studio by creating a dashboard-ready sheet
 */
function prepareDataForLookerStudio() {
  // Get the active spreadsheet
  const ss = SpreadsheetApp.getActiveSpreadsheet();
  
  // Get the source data sheet
  const sourceSheet = ss.getSheetByName(CONFIG.WORKSHEET_NAME);
  if (!sourceSheet) {
    throw new Error(`Source sheet '${CONFIG.WORKSHEET_NAME}' not found.`);
  }
  
  // Get all data including headers
  const sourceData = sourceSheet.getDataRange().getValues();
  if (sourceData.length <= 1) {
    throw new Error("No data found in the source sheet.");
  }
  
  // Get headers
  const headers = sourceData[0];
  
  // Create or get the dashboard data sheet
  let dashboardSheet = ss.getSheetByName(CONFIG.DASHBOARD_WORKSHEET_NAME);
  if (!dashboardSheet) {
    dashboardSheet = ss.insertSheet(CONFIG.DASHBOARD_WORKSHEET_NAME);
  } else {
    dashboardSheet.clear();
  }
  
  // Create new headers with calculated fields
  const newHeaders = [...headers];
  if (!newHeaders.includes('Has LinkedIn Profile')) {
    newHeaders.push('Has LinkedIn Profile');
  }
  if (!newHeaders.includes('Education Count')) {
    newHeaders.push('Education Count');
  }
  if (!newHeaders.includes('Skills Count')) {
    newHeaders.push('Skills Count');
  }
  if (!newHeaders.includes('Profile Completeness')) {
    newHeaders.push('Profile Completeness');
  }
  
  // Create new data array with calculated fields
  const newData = [newHeaders];
  
  // Get column indexes
  const linkedInUrlIndex = headers.indexOf('LinkedIn URL');
  const educationIndex = headers.indexOf('Education');
  const skillsIndex = headers.indexOf('Skills');
  
  // Add calculated fields to each row
  for (let i = 1; i < sourceData.length; i++) {
    const row = sourceData[i];
    const newRow = [...row];
    
    // Has LinkedIn Profile (boolean)
    const hasLinkedInProfile = row[linkedInUrlIndex] && row[linkedInUrlIndex].toString().trim() !== '';
    newRow.push(hasLinkedInProfile);
    
    // Education Count
    let educationCount = 0;
    if (educationIndex !== -1 && row[educationIndex]) {
      educationCount = row[educationIndex].toString().split(';').filter(item => item.trim() !== '').length;
    }
    newRow.push(educationCount);
    
    // Skills Count
    let skillsCount = 0;
    if (skillsIndex !== -1 && row[skillsIndex]) {
      skillsCount = row[skillsIndex].toString().split(',').filter(item => item.trim() !== '').length;
    }
    newRow.push(skillsCount);
    
    // Profile Completeness (percentage)
    const fieldsToCheck = ['LinkedIn URL', 'LinkedIn Headline', 'LinkedIn Location', 
                          'Current Company', 'Current Role', 'Education', 'Skills'];
    let filledFields = 0;
    let totalFields = 0;
    
    fieldsToCheck.forEach(field => {
      const fieldIndex = headers.indexOf(field);
      if (fieldIndex !== -1) {
        totalFields++;
        if (row[fieldIndex] && row[fieldIndex].toString().trim() !== '') {
          filledFields++;
        }
      }
    });
    
    const profileCompleteness = totalFields > 0 ? Math.round((filledFields / totalFields) * 100) : 0;
    newRow.push(profileCompleteness);
    
    newData.push(newRow);
  }
  
  // Write the new data to the dashboard sheet
  dashboardSheet.getRange(1, 1, newData.length, newData[0].length).setValues(newData);
  
  // Format the dashboard sheet
  formatDashboardSheet(dashboardSheet, newData);
  
  // Create named ranges for Looker Studio
  createNamedRanges(ss, dashboardSheet, newHeaders);
  
  Logger.log("Data prepared successfully for Looker Studio");
}

/**
 * Format the dashboard sheet for better readability
 */
function formatDashboardSheet(sheet, data) {
  // Freeze the header row
  sheet.setFrozenRows(1);
  
  // Format header row
  const headerRange = sheet.getRange(1, 1, 1, data[0].length);
  headerRange.setFontWeight('bold');
  headerRange.setBackground('#f3f3f3');
  
  // Auto-resize columns
  for (let i = 1; i <= data[0].length; i++) {
    sheet.autoResizeColumn(i);
  }
  
  // Format the Has LinkedIn Profile column as checkbox
  const hasProfileIndex = data[0].indexOf('Has LinkedIn Profile');
  if (hasProfileIndex !== -1) {
    const hasProfileRange = sheet.getRange(2, hasProfileIndex + 1, data.length - 1, 1);
    hasProfileRange.setDataValidation(SpreadsheetApp.newDataValidation().requireCheckbox().build());
  }
  
  // Format the Profile Completeness column
  const completenessIndex = data[0].indexOf('Profile Completeness');
  if (completenessIndex !== -1) {
    const completenessRange = sheet.getRange(2, completenessIndex + 1, data.length - 1, 1);
    completenessRange.setNumberFormat('0"%"');
  }
}

/**
 * Create named ranges for use in Looker Studio
 */
function createNamedRanges(spreadsheet, sheet, headers) {
  const sheetName = sheet.getName();
  
  // Create named range for the entire data set
  const dataRange = sheet.getDataRange();
  spreadsheet.setNamedRange('LinkedInData', dataRange);
  
  // Create named ranges for specific columns
  const keyColumns = [
    'Full Name', 'LinkedIn URL', 'Current Company', 'Current Role',
    'Education', 'Skills', 'Has LinkedIn Profile', 'Profile Completeness'
  ];
  
  keyColumns.forEach(columnName => {
    const columnIndex = headers.indexOf(columnName);
    if (columnIndex !== -1) {
      const columnRange = sheet.getRange(2, columnIndex + 1, sheet.getLastRow() - 1, 1);
      spreadsheet.setNamedRange(`${columnName.replace(/\s+/g, '')}Column`, columnRange);
    }
  });
}

/**
 * Add a menu to the spreadsheet to manually run the preparation
 */
function onOpen() {
  const ui = SpreadsheetApp.getUi();
  ui.createMenu('Looker Studio')
    .addItem('Prepare Data', 'prepareDataForLookerStudio')
    .addToUi();
} 