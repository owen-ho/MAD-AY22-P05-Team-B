# MAD-AY22-P05-Team-B MulaSave

## Features Developed by Owen for Stage 2

### Custom WebView for In-App Browser
<img src="https://user-images.githubusercontent.com/93632887/182045409-c498afe3-e9f6-430f-84e3-3c4f297bcc51.png" alt="drawing" width="300"/>

- Bottom toolbar disappears upon user swipe up
- Appears again when user swipes up
- Left 2 buttons go to previous and next website respectively, 3rd button is to refresh page
- Right-most button redirects user to the website in their default browser
- Done with Custom WebView(override some WebView methods) and GestureDetector

### Suggestions based on Recent Searches
<img src="https://user-images.githubusercontent.com/93632887/182045412-374c80cf-f75e-4815-83e8-1add98e593c3.png" alt="drawing" width="300"/>

- Clicking on search button in Shopping fragment sends user to new fragment which stores search history
- Saves user's recent queries and the products data as a result of that query
- Allows for quick searches on previously queried products without long wait times or new API calls
- Done with SharedPreferences to store product list and SearchRecentSuggestionsProvider which uses SQLite to store suggestions.
