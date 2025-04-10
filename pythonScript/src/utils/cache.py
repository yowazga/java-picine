import json
from pathlib import Path
from typing import Dict

class ProfileCache:
    """Cache for LinkedIn profile data to avoid re-scraping."""
    
    def __init__(self, cache_file_path: Path):
        """Initialize the cache with a file path."""
        self.cache_file_path = cache_file_path
        self.cache_data = self._load_cache()
        
    def _load_cache(self) -> Dict:
        """Load previously scraped profiles from cache file."""
        if self.cache_file_path.exists():
            try:
                with open(self.cache_file_path, 'r', encoding='utf-8') as f:
                    return json.load(f)
            except json.JSONDecodeError:
                return {"profiles": {}}
        return {"profiles": {}}
    
    def save_cache(self):
        """Save scraped profiles to cache file."""
        with open(self.cache_file_path, 'w', encoding='utf-8') as f:
            json.dump(self.cache_data, f, ensure_ascii=False, indent=2)
            
    def get_profile(self, url: str) -> Dict:
        """Get a profile from the cache if it exists."""
        return self.cache_data["profiles"].get(url)
        
    def add_profile(self, url: str, profile_data: Dict):
        """Add a profile to the cache."""
        self.cache_data["profiles"][url] = profile_data
        self.save_cache() 