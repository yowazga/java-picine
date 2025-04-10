import json
import os
from pathlib import Path
from typing import List, Set, Dict

class FileTracker:
    """Tracks which Excel files have been processed to avoid duplication."""
    
    def __init__(self, record_file_path: Path):
        """Initialize the file tracker with a record file path."""
        self.record_file_path = record_file_path
        self.processed_files = self._load_record()
        
    def _load_record(self) -> Dict:
        """Load previously processed files from record file."""
        if self.record_file_path.exists():
            try:
                with open(self.record_file_path, 'r', encoding='utf-8') as f:
                    return json.load(f)
            except json.JSONDecodeError:
                return {"processed_files": []}
        return {"processed_files": []}
    
    def save_record(self):
        """Save processed files record to file."""
        with open(self.record_file_path, 'w', encoding='utf-8') as f:
            json.dump(self.processed_files, f, ensure_ascii=False, indent=2)
            
    def is_file_processed(self, file_path: str) -> bool:
        """Check if a file has already been processed."""
        return file_path in self.processed_files["processed_files"]
        
    def mark_as_processed(self, file_path: str):
        """Mark a file as processed."""
        if not self.is_file_processed(file_path):
            self.processed_files["processed_files"].append(file_path)
            self.save_record()
            
    def get_new_excel_files(self, directory: str) -> List[str]:
        """Get list of new Excel files that have not been processed yet."""
        excel_extensions = ['.xlsx', '.xls', '.xlsm']
        new_files = []
        
        # Ensure directory exists
        if not os.path.exists(directory):
            os.makedirs(directory)
            return new_files
            
        # Find all Excel files in the directory
        for file in os.listdir(directory):
            file_path = os.path.join(directory, file)
            if (os.path.isfile(file_path) and 
                any(file.lower().endswith(ext) for ext in excel_extensions) and 
                not self.is_file_processed(file_path)):
                new_files.append(file_path)
                
        return new_files 