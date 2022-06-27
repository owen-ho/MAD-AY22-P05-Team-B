package sg.edu.np.MulaSave.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sg.edu.np.MulaSave.APIHandler;
import sg.edu.np.MulaSave.MainActivity;
import sg.edu.np.MulaSave.Product;
import sg.edu.np.MulaSave.R;
import sg.edu.np.MulaSave.ShoppingRecyclerAdapter;
import sg.edu.np.MulaSave.wishlistFilterAdapter;

public class ShoppingFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    RecyclerView recyclerViewFilter;
    private ArrayList<Product> productList = MainActivity.productList;//Take previously loaded productList

    public ShoppingFragment() {
    }

    public static ShoppingFragment newInstance() {
        ShoppingFragment fragment = new ShoppingFragment();
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
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);

        recyclerView = view.findViewById(R.id.shoppingrecyclerview);
        //recyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);

        //productList = new ArrayList<Product>();
        SearchView query = view.findViewById(R.id.searchQuery);
        if (productList!=null) {//Checks for previously loaded productList to display
            if(productList.size()!=0){
                ShoppingRecyclerAdapter pAdapter = new ShoppingRecyclerAdapter(productList, getContext(),1);
                //WishList Filters
                recyclerViewFilter = view.findViewById(R.id.shoppingFilter);
                wishlistFilterAdapter wFilterAdapter = new wishlistFilterAdapter(view,pAdapter,productList,3);

                //Layout manager for filters recyclerview
                LinearLayoutManager hLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);//set horizontal layout
                recyclerViewFilter.setLayoutManager(hLayoutManager);
                recyclerViewFilter.setItemAnimator(new DefaultItemAnimator());
                recyclerViewFilter.setAdapter(wFilterAdapter);//set adapter for wishlist filters
                recyclerView.setAdapter(pAdapter);
            }
        }
        query.setOnQueryTextListener(new SearchView.OnQueryTextListener() { //Grab products from API whenever a query is submitted
            @Override
            public boolean onQueryTextSubmit(String s) {
                productList = new ArrayList<Product>();//Create new list to clear previously loaded products for new query
                new getProducts(s,productList,view).execute();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SearchView query = view.findViewById(R.id.searchQuery);
        query.setSubmitButtonEnabled(true);

        int id = query.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEdit = query.findViewById(id);
        searchEdit.setTextColor(Color.BLACK);

        //make the whole search bar clickable
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.setIconified(false);
            }
        });

        //set on searchview open listener for searchview
        query.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((TextView)getView().findViewById(R.id.placeholderText)).setVisibility(View.GONE);//hide the placeholder text

                ((TextView)getView().findViewById(R.id.shoppingTitle)).setVisibility(View.GONE);//set the title to be gone
                ConstraintLayout layout = (ConstraintLayout) getView().findViewById(R.id.shoppingConstraintLayout);//get constraintlayout
                ConstraintSet set = new ConstraintSet();
                set.clone(layout);
                //set constraints for the title and searchview
                set.connect(R.id.shoppingSearchCard, ConstraintSet.START,R.id.shoppingConstraintLayout,ConstraintSet.START,0);
                set.connect(R.id.shoppingSearchCard, ConstraintSet.END,R.id.shoppingConstraintLayout,ConstraintSet.END,0);
                set.applyTo(layout);
            }
        });
        query.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                ((TextView)getView().findViewById(R.id.shoppingTitle)).setVisibility(View.VISIBLE);

                //to convert margin to dp
                Resources r = getView().getResources();
                int px = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        24,
                        r.getDisplayMetrics()
                );

                //set layout
                ConstraintLayout layout = (ConstraintLayout) getView().findViewById(R.id.shoppingConstraintLayout);
                ConstraintSet set = new ConstraintSet();
                set.clone(layout);
                //clear constraints
                set.clear(R.id.shoppingSearchCard, ConstraintSet.START);
                set.connect(R.id.shoppingSearchCard, ConstraintSet.END,R.id.shoppingConstraintLayout,ConstraintSet.END,px);
                set.applyTo(layout);
                return false;//return false so that icon closes back on close
            }
        });

        //to navigate user from homefrag to shoppingfrag
        Bundle bundle = this.getArguments();
        if(bundle!= null){
            Boolean search = bundle.getBoolean("condition",false);
            if(search){//if search == true, which means set searchview to active
                query.performClick();
                query.requestFocus();
                //to show the keyboard
                query.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        if (hasFocus) {
                            showInputMethod(view.findFocus());
                        }
                    }
                });
            }
        }//end of bundle
    }

    @Override
    public void onResume() {
        super.onResume();
        try{//try catch because the product list may not be initialised
            if (MainActivity.productList.isEmpty() == false){//if there are items in the list
                ((SearchView)getView().findViewById(R.id.searchQuery)).performClick();//click the searchview to show the previous state
                ((SearchView)getView().findViewById(R.id.searchQuery)).setIconified(true);//onresume, hide the keyboard
            }
        }
        catch (Exception e){
            Log.e("error", "onResume: " + String.valueOf(e));
        }
    }

    private void showInputMethod(View view) {
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mgr != null) {
            mgr.showSoftInput(view, 0);
        }
    }

    class getProducts extends AsyncTask<Void, Void, Void> {
        String query;
        ArrayList<Product> productList;
        View view;

        public getProducts(String _query, ArrayList<Product> pList,View v){
            this.query=_query;
            this.productList=pList;
            this.view=v;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            //Updates MainActivity's productList so that the list will not be destroyed alongside fragment and stays persistent
            MainActivity.productList = productList;

            //Display products with productList generated based on user's query
            ShoppingRecyclerAdapter pAdapter = new ShoppingRecyclerAdapter(productList, getContext(),1);
            //WishList Filters
            recyclerViewFilter = view.findViewById(R.id.shoppingFilter);
            wishlistFilterAdapter wFilterAdapter = new wishlistFilterAdapter(getView(),pAdapter,productList,3);

            //Layout manager for filters recyclerview
            LinearLayoutManager hLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);//set horizontal layout
            recyclerViewFilter.setLayoutManager(hLayoutManager);
            recyclerViewFilter.setItemAnimator(new DefaultItemAnimator());
            recyclerViewFilter.setAdapter(wFilterAdapter);//set adapter for wishlist filters
            recyclerView.setAdapter(pAdapter);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String[] apiList = new String[] {"walmart","amazon","ebay"};//Amazon API uses demo as our API has expired, it will only query for memory cards
            for (String i:apiList){
                String url = getAPIlink(query,i); //Finds URL link by processing through template based on online shopping site used, this is so the correct API & API keys are used
                loadJsonfromUrl(url,i); //Loads json using URL query and extracts Product details from Json
            }
            return null;
        }

        private String getAPIlink(String query, String website){
            String url = null;
            String apikey;
            query = query.replace(" ","+");

            if(query.toLowerCase().contains("walmart")){  //Allow use of demo APIs for demo purposes so that API requests are not wasted in testing
                url = "https://api.bluecartapi.com/request?api_key=demo&type=search&search_term=highlighter+pens&sort_by=best_seller";
            }
            else if(query.toLowerCase().contains("amazon")){
                url = "https://api.rainforestapi.com/request?api_key=demo&amazon_domain=amazon.com&type=search&search_term=memory+cards";
            }
            else if(query.toLowerCase().contains("ebay")){
                url = "https://api.countdownapi.com/request?api_key=demo&type=search&ebay_domain=ebay.com&search_term=memory+cards&sort_by=price_high_to_low";
            }
            else{
                if(website.toLowerCase().equals("amazon")){
                    //apikey = "4487B79AE90342968E9E30B71F25913D";
                    //url = "https://api.rainforestapi.com/request?api_key="+apikey+"&type=search&amazon_domain=amazon.sg&search_term="+query;

                    //TEMPORARY DEMO API FOR TESTING
                    url = "https://api.rainforestapi.com/request?api_key=demo&amazon_domain=amazon.com&type=search&search_term=memory+cards";
                }
                else if(website.toLowerCase().equals("walmart")){
                    apikey = "83B616CC6FAD4A6FBE7A739483C2C741";
                    url = "https://api.bluecartapi.com/request?api_key="+apikey+"&type=search&search_term="+query+"&sort_by=best_seller";

                    //TEMPORARY DEMO API FOR TESTING
                    //url = "https://api.bluecartapi.com/request?api_key=demo&type=search&search_term=highlighter+pens&sort_by=best_seller";
                }
                else if(website.toLowerCase().equals("ebay")){
                    apikey = "A00A8C31BBF84303A82C2EE40B02A6FF";
                    url = "https://api.countdownapi.com/request?api_key="+apikey+"&type=search&ebay_domain=ebay.com&search_term="+query+"&sort_by=price_high_to_low";

                    //TEMPORARY DEMO API FOR TESTING
                    //url = "https://api.countdownapi.com/request?api_key=demo&type=search&ebay_domain=ebay.com&search_term=memory+cards&sort_by=price_high_to_low";
                }
                else{
                    Toast.makeText(getContext(),"We do not have APIs to that website yet!",Toast.LENGTH_SHORT).show();
                }
            }
            return url;
        }

        private void loadJsonfromUrl(String url, String website){
            APIHandler handler = new APIHandler();
            String jsonString = handler.httpServiceCall(url);//Loads API Json into a string
            Log.d("JSONInput",jsonString);//Check for success of pulling products from API and also number of requests left
            if (jsonString!=null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);//Convert string to JSONObject to extract specific data
                    JSONArray products = jsonObject.getJSONArray("search_results");
                    for(int i=0;i<products.length();i++) {
                        JSONObject jsonObject1 = products.getJSONObject(i);

                        String title = "No title";
                        String image = "no image";
                        String link = "no link";
                        Double price = 0.0;

                        float ratingF = 0;
                        if (website.toLowerCase().equals("walmart")) {//Product Json format of Walmart API is different from Amazon & Ebay, so it is done separately
                            JSONObject productObject = jsonObject1.getJSONObject("product");
                            title = productObject.getString("title");
                            image = productObject.getString("main_image");
                            link = productObject.getString("link");

                            Double ratingD = productObject.getDouble("rating");
                            ratingF = ratingD.floatValue();

                            JSONObject offersObject = jsonObject1.getJSONObject("offers").getJSONObject("primary");
                            price = offersObject.getDouble("price");
                        } else {
                            //String asin = jsonObject1.getString("asin"); //deleted because ebay API has no asin
                            title = jsonObject1.getString("title");
                            image = jsonObject1.getString("image");
                            link = jsonObject1.getString("link");
                            Double ratingD = jsonObject1.getDouble("rating");
                            ratingF = ratingD.floatValue();

                            //JSONObject categoryObject = jsonObject1.getJSONArray("categories").getJSONObject(0); //deleted because ebay API does not have product categories
                            //String category = categoryObject.getString("name");

                            JSONObject priceObject = jsonObject1.getJSONObject("price");
                            price = priceObject.getDouble("value");
                        }

                        productList.add(new Product("asin", title, "category", price, image, link, ratingF, website));
                    }
                } catch (JSONException e) {
                    if(isAdded()){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(getContext(),"Json Parsing Error",Toast.LENGTH_LONG).show();
                                Log.i("error","Json Parsing Error");
                            }
                        });
                    }
                }
            }
            else{
                Toast.makeText(getContext(),"Server error",Toast.LENGTH_LONG).show();
                if(isAdded()){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Server Error",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }

    }


}