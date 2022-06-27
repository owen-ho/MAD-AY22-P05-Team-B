package sg.edu.np.MulaSave.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sg.edu.np.MulaSave.APIHandler;
import sg.edu.np.MulaSave.MainActivity;
import sg.edu.np.MulaSave.Product;
import sg.edu.np.MulaSave.R;

public class HomeFragment extends Fragment {
    private ArrayList<Product> homeproductList = MainActivity.homeproductList;
    private ImageView[] imArray;
    private CardView[] cardArray;
    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        // Inflate the layout for this fragment
        return v;
    }


    //create this method because getView() only works after onCreateView()
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ImageView large = getView().findViewById(R.id.largeImage);
        ImageView small1 = getView().findViewById(R.id.smallImage1);
        ImageView small2 = getView().findViewById(R.id.smallImage2);
        ImageView small3 = getView().findViewById(R.id.smallImage3);
        ImageView small4 = getView().findViewById(R.id.smallImage4);
        ImageView small5 = getView().findViewById(R.id.smallImage5);
        ImageView small6 = getView().findViewById(R.id.smallImage6);
        imArray = new ImageView[] {large,small1,small2,small3,small4,small5,small6};
        //SearchView search = getView().findViewById(R.id.goSearch);
        CardView cardShop = getView().findViewById(R.id.cardViewGoShop);
        CardView cardCom = getView().findViewById(R.id.cardViewGoCom);

        CardView csmall1 = getView().findViewById(R.id.cardView6);
        CardView csmall2 = getView().findViewById(R.id.cardView5);
        CardView csmall3 = getView().findViewById(R.id.cardView7);
        CardView csmall4 = getView().findViewById(R.id.cardView9);
        CardView csmall5 = getView().findViewById(R.id.cardView8);
        CardView csmall6 = getView().findViewById(R.id.cardView10);
        cardArray = new CardView[]{csmall1,csmall2,csmall3,csmall4,csmall5,csmall6};

        if (homeproductList!=null) {
            if(homeproductList.size()!=0){
                displayAdapter(homeproductList);
            }
        }else{
            homeproductList = new ArrayList<Product>();
            new getProducts().execute();
        }

        //transition to the shopping fragment
        cardShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ShoppingFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("condition", true);//inform shopping fragment that this method is passed
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, fragment );
                transaction.addToBackStack(null);  //this transaction will be added to backstack

                //set the shop menu item active
                BottomNavigationView navigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavigationView);
                navigationView.setSelectedItemId(R.id.Shop);
                transaction.commit();
            }
        });

        cardCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CommunityFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("condition", true);//inform community fragment that this method is passed
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, fragment );
                transaction.addToBackStack(null);  //this transaction will be added to backstack

                //set the shop menu item active
                BottomNavigationView navigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavigationView);
                navigationView.setSelectedItemId(R.id.Community);
                transaction.commit();
            }
        });

    }//end of onview created method

    private void displayAdapter(ArrayList<Product> homeproductList){
        Integer count=0;
        for(ImageView iv:imArray){
            Product p = homeproductList.get(count);
            Picasso
                    .get()
                    .load(p.getImageUrl())
                    .fit()
                    .centerCrop()
                    .into(iv);
            iv.setVisibility(View.VISIBLE);//set to visible -- default is invisible before picasso loads

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //custom alert dialog for product information
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    View v = LayoutInflater.from(getContext()).inflate(R.layout.homefrag_product_dialog,null,false);
                    builder.setView(v);
                    ImageView prodImg = v.findViewById(R.id.hProductPic);
                    Picasso.get().load(Uri.parse(p.getImageUrl())).into(prodImg);
                    ((TextView) v.findViewById(R.id.hProductName)).setText(p.getTitle());
                    ((TextView) v.findViewById(R.id.hProductPrice)).setText(String.format("$ %.2f",p.getPrice()));
                    RatingBar bar = v.findViewById(R.id.hProductRating);
                    ((TextView) v.findViewById(R.id.hProductWebsite)).setText(p.getWebsite());
                    ImageView pWish = v.findViewById(R.id.hProductWish);
                    if(Math.signum(p.getRating()) == 0){//check if rating is 0, if rating is 0, set the visibility to GONE
                        bar.setVisibility(View.GONE);
                    }
                    else{
                        bar.setVisibility(View.VISIBLE);//else set visible and set rating
                        bar.setRating(p.getRating());
                    }

                    String wishlistUnique = (p.getTitle() + (p.getImageUrl().substring(p.getImageUrl().length()-15))+ p.getWebsite()).replaceAll("[^a-zA-Z0-9]", "");
                    DatabaseReference databaseRefUser = FirebaseDatabase
                            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
                            .getReference("user");//get users path
                    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();//identify current user

                    //check if user has the item added to wishlist, if added, turn the button red and gray if otherwise
                    databaseRefUser.child(usr.getUid().toString()).child("wishlist").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(wishlistUnique)){
                                pWish.setColorFilter(ContextCompat.getColor(getContext(), R.color.custom_red));//use custom red color if item is in wishlist
                            }
                            else{
                                pWish.setColorFilter(ContextCompat.getColor(getContext(), R.color.custom_gray));//use custom gray color if item is not in wishlist
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.w("databaseError", "onCancelled: " + String.valueOf(error));
                        }
                    });

                    pWish.setOnClickListener(new View.OnClickListener() {//on click listener for favourite button in searching of product
                        @Override
                        public void onClick(View view) {
                            databaseRefUser.child(usr.getUid().toString()).child("wishlist").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if(task.getResult().hasChild(wishlistUnique)){

                                        //custom dialog for removing of wishlist item
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        View vw = LayoutInflater.from(getContext()).inflate(R.layout.remove_wislist,null, false);//inflate view
                                        builder.setView(vw);
                                        ImageView pic = vw.findViewById(R.id.wishlistPic);
                                        Uri newUri = Uri.parse(p.getImageUrl());
                                        Picasso.get().load(newUri).into(pic);
                                        final AlertDialog alertDialog = builder.create();

                                        //positive button (remove item)
                                        vw.findViewById(R.id.wishlistRemove).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                databaseRefUser.child(usr.getUid().toString()).child("wishlist").child(wishlistUnique).removeValue();
                                                pWish.setColorFilter(ContextCompat.getColor(getContext(), R.color.custom_gray));//use custom gray color
                                                alertDialog.dismiss();//dismiss dialog
                                            }
                                        });
                                        //negative button (cancel removal)
                                        vw.findViewById(R.id.wishlistCancel).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                alertDialog.dismiss();//dismiss dialog
                                            }
                                        });

                                        //remove the extra parts outside of the cardview
                                        if (alertDialog.getWindow() != null){
                                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
                                        }
                                        alertDialog.show();
                                    }
                                    else{//the item does not exist in wishlist, add the item
                                        databaseRefUser.child(usr.getUid().toString()).child("wishlist").child(wishlistUnique).setValue(p);//add product if the product does not exist in the database
                                        pWish.setColorFilter(ContextCompat.getColor(getContext(), R.color.custom_red));//use custom red color
                                    }
                                }
                            });
                        }
                    });

                    final AlertDialog alertDialog = builder.create();

                    //open the product
                    v.findViewById(R.id.hProductOpen).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Opens store page for item
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(p.getLink()));
                            getContext().startActivity(browserIntent);
                            alertDialog.dismiss();
                        }
                    });
                    //close the dialog
                    v.findViewById(R.id.hProductBack).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });

                    if (alertDialog.getWindow() != null){
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
                    }
                    alertDialog.show();
                }
            });
            count+=1;
        }
        for(CardView card:cardArray){
            card.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }


    class getProducts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            //recyclerView.setAdapter(new ShoppingRecyclerAdapter(productList));
            MainActivity.homeproductList = homeproductList;
            displayAdapter(homeproductList);
        }//end of onPostExecute in AsyncTask

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //URL is static for now as users visit home page often which would use up API requests rapidly
            String url = "https://api.rainforestapi.com/request?api_key=demo&type=bestsellers&category_id=bestsellers_appliances&amazon_domain=amazon.com";
            APIHandler handler = new APIHandler();
            String jsonString = handler.httpServiceCall(url);//Loads API Json into a string
            Log.d("JSONInput",jsonString);//Check for success of pulling products from API and also number of requests left
            if (jsonString!=null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray products = jsonObject.getJSONArray("bestsellers");//Not search_results as search parameter is different

                    for(int i=0;i<products.length();i++){
                        JSONObject jsonObject1 = products.getJSONObject(i);

                        String title = "No title";
                        String image = "no image";
                        String link = "no link";
                        Double price = 0.0;
                        Double rating = 0.0;

                        //String asin = jsonObject1.getString("asin");
                        title = jsonObject1.getString("title");
                        image = jsonObject1.getString("image");
                        link = jsonObject1.getString("link");
                        rating = jsonObject1.getDouble("rating");

                        JSONObject priceObject = jsonObject1.getJSONObject("price");
                        price = priceObject.getDouble("value");

                        Product p = new Product("asin",title,"category",price,image,link, rating.floatValue(), "Amazon");//Website is hardcoded as API URL is static
                        homeproductList.add(p);
                    }
                } catch (JSONException e) {
                    if(isAdded()){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(getContext(),"Json Parsing Error",Toast.LENGTH_LONG).show();
                                e.printStackTrace();
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
            return null;
        }
    }
}

