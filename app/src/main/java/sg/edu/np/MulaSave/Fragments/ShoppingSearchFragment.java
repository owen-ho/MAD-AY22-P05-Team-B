package sg.edu.np.MulaSave.Fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import sg.edu.np.MulaSave.MainActivity;
import sg.edu.np.MulaSave.ProductSuggestionProvider;
import sg.edu.np.MulaSave.R;

public class ShoppingSearchFragment extends Fragment {
    private ListView searchList;
    private TextView clearBtn;
    private SearchView searchView;

    public ShoppingSearchFragment() {
        // Required empty public constructor
    }

    public static ShoppingSearchFragment newInstance(String param1, String param2) {
        ShoppingSearchFragment fragment = new ShoppingSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Get the SearchView and set the searchable configuration
        showResults(view);
        clearBtn = view.findViewById(R.id.clearSuggestions);
        searchView = view.findViewById(R.id.searchFragQuery);
        searchView.setIconified(false);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        //Associate the searchable configuration with the SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        //Lets users click on the submit button rather than "Enter" or "Return" on their keyboards
        searchView.setSubmitButtonEnabled(true);
        //Enables query refinement from search suggestions
        searchView.setQueryRefinementEnabled(true);

        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);

        EditText searchEdit = searchView.findViewById(id);
        searchEdit.setTextColor(Color.BLACK);

        /**
         * Clears suggestions list
         */
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getActivity(),
                        ProductSuggestionProvider.AUTHORITY, ProductSuggestionProvider.MODE);//Initiates recent sugestions content provider
                suggestions.clearHistory();
            }
        });

        /**
         * Saves query and sends to ShoppingFragment, where 'getProducts' class can be called to retrieve products list from API
         */
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { //Grab products from API whenever a query is submitted
            @Override
            public boolean onQueryTextSubmit(String s) {
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getContext(),ProductSuggestionProvider.AUTHORITY,ProductSuggestionProvider.MODE);
                suggestions.saveRecentQuery(s, null); //Save query for search history suggestions in the future

                MainActivity.query=s;
                ShoppingFragment nextFrag= new ShoppingFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        /**
         * Upon closing searchview, user is sent back to shopping fragment
         */
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                ShoppingFragment nextFrag= new ShoppingFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
                return false;
            }
        });

        /**
         * To show the keyboard automatically
         */
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showInputMethod(view.findFocus());
                }
            }
        });
    }

    private void showInputMethod(View view) {
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mgr != null) {
            mgr.showSoftInput(view, 0);
        }
    }

    /**
     * Retrieves saved suggestions from SearchRecentSuggestionsProvider and loads into ListView adapter
     * @param view To retrieve ListView within view
     */
    private void showResults(View view) {
        searchList = view.findViewById(R.id.searchList);
        String string_uri = "content://sg.edu.np.MulaSave.ProductSuggestionProvider/suggestions";
        Uri uri=Uri.parse(string_uri);

        //Taking suggestions data from SearchRecentSuggestionsProvider
        Cursor cursor=getActivity().getContentResolver().query(uri,null,null,null,null);//All null will return everything from database
        SimpleCursorAdapter simpleCursorAdapter=new SimpleCursorAdapter(view.getContext(),R.layout.suggestion_row,cursor,
                new String[] { "query", "date"},
                new int[]{R.id.suggestion, R.id.suggestionDate},
                SearchManager.FLAG_QUERY_REFINEMENT);

        //Converts date from epoch milli to standard date time format
        simpleCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if (i==3){//Date is found in the 4th column
                    String createDate = cursor.getString(i);
                    TextView textView = view.findViewById(R.id.suggestionDate);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    LocalDateTime ldt = Instant.ofEpochMilli(Long.parseLong(createDate)).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    textView.setText(ldt.format(formatter));
                    return true;
                }
                return false;
            }
        });

        //Calls ListView adapter to display suggestions and date/time where suggestion was saved
        searchList.setAdapter(simpleCursorAdapter);
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CursorWrapper suggestion = (CursorWrapper) adapterView.getAdapter().getItem(i);
                String suggestionQuery = suggestion.getString(1);//Column index 1 refers to the second column, where the query names are kept
                Intent intent = new Intent(getActivity(),MainActivity.class);
                intent.putExtra("shopping",1);//Indicates that the user should be sent to shopping fragment
                intent.putExtra(SearchManager.QUERY,suggestionQuery);//Intents the query to ShoppingFragment to load with either API or previously loaded product list
                getActivity().startActivity(intent);
            }
        });
    }
}