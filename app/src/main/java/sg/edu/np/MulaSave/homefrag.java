package sg.edu.np.MulaSave;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.Rating;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class homefrag extends Fragment {
    private ArrayList<Product> homeproductList = new ArrayList<Product>();
    private ImageView[] imArray;
    public homefrag() {
        // Required empty public constructor
    }
    public static homefrag newInstance(String param1, String param2) {
        homefrag fragment = new homefrag();
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
        SearchView search = getView().findViewById(R.id.goSearch);
        //transition to the shopping fragment
        search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new shoppingfrag();
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

        //to make the whole searchbar clickable
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setIconified(false); // cannot be placed under setOnSearchListener because it will cause listener to be informed
            }
        });
        search.setSubmitButtonEnabled(true);
        new getProducts().execute();
    }//end of onview created method


    class getProducts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            //recyclerView.setAdapter(new ShoppingRecyclerAdapter(productList));



            Integer count=0;
            for(ImageView iv:imArray){
                Product p = homeproductList.get(count);
                Picasso
                        .get()
                        .load(p.getImageUrl())
                        .fit()
                        .into(iv);
                iv.setVisibility(View.VISIBLE);//set to visible -- default is invisible before picasso loads

                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                        if(Math.signum(p.getRating()) == 0){
                            bar.setVisibility(View.GONE);
                        }
                        else{
                            bar.setVisibility(View.VISIBLE);
                            bar.setRating(p.getRating());
                        }

                        String wishlistUnique = (p.getTitle() + (p.getImageUrl().substring(p.getImageUrl().length()-15))+ p.getWebsite()).replaceAll("[^a-zA-Z0-9]", "");
                        DatabaseReference databaseRefUser = FirebaseDatabase
                                .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                .getReference("user");
                        FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();

                        //check if user has the item added to wishlist, if added, turn the button red and gray if otherwise
                        databaseRefUser.child(usr.getUid().toString()).child("wishlist").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChild(wishlistUnique)){
                                    pWish.setColorFilter(ContextCompat.getColor(getContext(), R.color.custom_red));//use custom red color
                                }
                                else{
                                    pWish.setColorFilter(ContextCompat.getColor(getContext(), R.color.custom_gray));//use custom gray color
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

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
                                            View vw = LayoutInflater.from(getContext()).inflate(R.layout.remove_wislist,null, false);
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
                                                    alertDialog.dismiss();
                                                }
                                            });
                                            //negative button (cancel removal)
                                            vw.findViewById(R.id.wishlistCancel).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    alertDialog.dismiss();
                                                }
                                            });

                                            //remove the extra parts outside of the cardview
                                            if (alertDialog.getWindow() != null){
                                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
                                            }
                                            alertDialog.show();
                                        }
                                        else{
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



                        /*
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Product Details");
                        builder.setMessage("Title: "+p.getTitle()+'\n'+'\n'+String.format("Price: $%.2f",p.getPrice()));
                        builder.setCancelable(false);
                        builder.setPositiveButton("Open", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                //Opens store page for item
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(p.getLink()));
                                getContext().startActivity(browserIntent);
                            }
                        });
                        builder.setNegativeButton("Close", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                            }
                        });
                        builder.show();*/
                    }
                });
                count+=1;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "https://api.rainforestapi.com/request?api_key=demo&type=bestsellers&category_id=bestsellers_appliances&amazon_domain=amazon.com";
            APIHandler handler = new APIHandler();
            String jsonString = handler.httpServiceCall(url);
            Log.d("JSONInput",jsonString);
            if (jsonString!=null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray products = jsonObject.getJSONArray("bestsellers");

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

                        Product p = new Product("asin",title,"category",price,image,link, rating.floatValue(), "Amazon");
                        homeproductList.add(p);
                    }
                } catch (JSONException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getContext(),"Json Parsing Error",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
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

