from datetime import datetime
from pathlib import Path

class Logger:
    """Simple logging utility for the LinkedIn scraper."""
    
    def __init__(self, log_file_path: Path):
        """Initialize the logger with a log file path."""
        self.log_file = open(log_file_path, 'a', encoding='utf-8')
        self.log(f"Session started at {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
        
    def log(self, message):
        """Log a message to both console and file."""
        timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        formatted_message = f"[{timestamp}] {message}"
        print(formatted_message)
        self.log_file.write(formatted_message + "\n")
        self.log_file.flush()
        
    def close(self):
        """Close the log file."""
        if self.log_file:
            self.log("🔄 Closing session.")
            self.log_file.close() 