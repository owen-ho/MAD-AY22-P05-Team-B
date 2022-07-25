package sg.edu.np.MulaSave;

import android.content.SearchRecentSuggestionsProvider;

public class ProductSuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "sg.edu.np.MulaSave.ProductSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public ProductSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}