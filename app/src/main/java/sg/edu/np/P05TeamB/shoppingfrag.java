package sg.edu.np.P05TeamB;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class shoppingfrag extends Fragment {

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private ProgressDialog pd;
    private ArrayList<Product> productList;

    public shoppingfrag() {
    }

    public static shoppingfrag newInstance(String param1, String param2) {
        shoppingfrag fragment = new shoppingfrag();
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
        productList = new ArrayList<Product>();
        recyclerView = view.findViewById(R.id.shoppingrecyclerview);
        //recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        FloatingActionButton uploadbutton = view.findViewById(R.id.uploadproductbutton);

        uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UserInputPrice.class);
                startActivity(i);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SearchView query = view.findViewById(R.id.searchQuery);
        query.setSubmitButtonEnabled(true);
        Bundle bundle = this.getArguments();
        if(bundle!= null){
            Boolean search = bundle.getBoolean("condition",false);
            if(search){
                query.setIconifiedByDefault(false);//set the searchbar to focus
                query.requestFocusFromTouch();//set the searchbar to focus
                query.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        if (hasFocus) {
                            showInputMethod(view.findFocus());
                        }
                    }
                });
            }
        }

        //Button searchBtn = view.findViewById(R.id.searchBtn);
        //make the whole search bar clickable
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.setIconified(false);
            }
        });

        query.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                productList.clear();
                new getProducts(s,productList,view).execute();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
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
            //recyclerView.setAdapter(new ShoppingRecyclerAdapter(productList));

            Collections.sort(productList, new Comparator<Product>() {
                @Override
                public int compare(Product prod1, Product prod2) {
                    return Double.valueOf(prod1.getPrice()).compareTo(Double.valueOf(prod2.getPrice()));
                }
            });

            ShoppingRecyclerAdapter pAdapter = new ShoppingRecyclerAdapter(productList, getContext(),1);
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
                String url = getAPIlink(query,i); //Find URL link by processing through template based on online shopping site used, this is so the correct API & API keys are used
                loadJsonfromUrl(url,i); //Loads json using URL query and extracts Product details from Json
            }
            return null;
        }

        private String getAPIlink(String query, String website){
            String url = null;
            String apikey;
            query = query.replace(" ","+");

            if(website.toLowerCase().equals("amazon")){
                //apikey = "4487B79AE90342968E9E30B71F25913D";
                //url = "https://api.rainforestapi.com/request?api_key="+apikey+"&type=search&amazon_domain=amazon.sg&search_term="+query;

                //TEMPORARY DEMO API FOR TESTING
                url = "https://api.rainforestapi.com/request?api_key=demo&amazon_domain=amazon.com&type=search&search_term=memory+cards";
            }
            else if(website.toLowerCase().equals("walmart")){
                apikey = "83B616CC6FAD4A6FBE7A739483C2C741";
                //url = "https://api.bluecartapi.com/request?api_key="+apikey+"&type=search&search_term="+query+"&sort_by=best_seller";

                //TEMPORARY DEMO API FOR TESTING
                url = "https://api.bluecartapi.com/request?api_key=demo&type=search&search_term=highlighter+pens&sort_by=best_seller";
            }
            else if(website.toLowerCase().equals("ebay")){
                apikey = "A00A8C31BBF84303A82C2EE40B02A6FF";
                //url = "https://api.countdownapi.com/request?api_key="+apikey+"&type=search&ebay_domain=ebay.com&search_term="+query+"&sort_by=price_high_to_low";

                //TEMPORARY DEMO API FOR TESTING
                url = "https://api.countdownapi.com/request?api_key=demo&type=search&ebay_domain=ebay.com&search_term=memory+cards&sort_by=price_high_to_low";
            }
            else{
                Toast.makeText(getContext(),"We do not have APIs to that website yet!",Toast.LENGTH_SHORT).show();
            }
            return url;
        }

        private void loadJsonfromUrl(String url, String website){
            APIHandler handler = new APIHandler();
            String jsonString = handler.httpServiceCall(url);
            Log.d("JSONInput",jsonString);
            if (jsonString!=null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray products = jsonObject.getJSONArray("search_results");

                    for(int i=0;i<products.length();i++) {
                        JSONObject jsonObject1 = products.getJSONObject(i);

                        String title = "No title";
                        String image = "no image";
                        String link = "no link";
                        Double price = 0.0;

                        float ratingF = 0;
                        if (website.toLowerCase().equals("walmart")) {
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
                            Log.i("knn", String.valueOf(ratingF));

                            //JSONObject categoryObject = jsonObject1.getJSONArray("categories").getJSONObject(0); //deleted because ebay API does not have product categories
                            //String category = categoryObject.getString("name");

                            JSONObject priceObject = jsonObject1.getJSONObject("price");
                            price = priceObject.getDouble("value");
                        }

                        productList.add(new Product("asin", title, "category", price, image, link, ratingF, website));
                    }
                } catch (JSONException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getContext(),"Json Parsing Error",Toast.LENGTH_LONG).show();
                            Log.i("error","Json Parsing Error");
                        }
                    });
                }
            }
            else{
                Toast.makeText(getContext(),"Server error",Toast.LENGTH_LONG).show();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"Server Error",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

    }
    private void showInputMethod(View view) {
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mgr != null) {
            mgr.showSoftInput(view, 0);
        }
    }
}