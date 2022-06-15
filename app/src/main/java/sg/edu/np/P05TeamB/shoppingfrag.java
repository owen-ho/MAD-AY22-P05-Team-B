package sg.edu.np.P05TeamB;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchView query = view.findViewById(R.id.searchQuery);
        //Button searchBtn = view.findViewById(R.id.searchBtn);
        query.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                new getProducts(s,productList,view).execute();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
//        searchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                new getProducts(query.toString(),productList,view).execute();
//            }
//        });
    }

    class getProducts extends AsyncTask<Void, Void, Void> {
        String query;
        ArrayList<Product> productList;
        View view;
        public getProducts(String queries, ArrayList<Product> pList,View v){
            this.query=queries;
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
            SearchProductAdapter pAdapter = new SearchProductAdapter(productList, getContext());

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

        private Float convertToFloat(Double doubleValue) {
            return doubleValue == null ? null : doubleValue.floatValue();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String APIkey = "CFF558057AA04DCB817FDCA3F5FE9546";//Hard coded but can be changed later on if we swap rainforestapi accounts
            query = query.replace(" ","+");
            String url ="https://api.rainforestapi.com/request?api_key="+APIkey+"&type=search&amazon_domain=amazon.sg&search_term="+query;
            APIHandler handler = new APIHandler();
            String jsonString = handler.httpServiceCall(url);
            Log.d("TAGGGGGG",jsonString);
            if (jsonString!=null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray products = jsonObject.getJSONArray("search_results");

                    for(int i=0;i<products.length();i++){
                        JSONObject jsonObject1 = products.getJSONObject(i);
                        JSONObject categoryObject = jsonObject1.getJSONArray("categories").getJSONObject(0);
                        JSONObject priceObject = jsonObject1.getJSONObject("price");
                        String asin = jsonObject1.getString("asin");
                        String title = jsonObject1.getString("title");
                        String image = jsonObject1.getString("image");
                        String category = categoryObject.getString("name");
                        Double price = priceObject.getDouble("value");
                        String link = jsonObject1.getString("link");
                        //Double rating = jsonObject1.getDouble("rating");

                        productList.add(new Product(asin,title,category,price,image,link,4.5f));
                    }
                } catch (JSONException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Json Parsing Error",Toast.LENGTH_LONG).show();
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
            return null;
        }
    }
}