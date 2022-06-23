package sg.edu.np.MulaSave;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class wishlistfrag extends Fragment {
    RecyclerView recyclerViewFilter;
    RecyclerView recyclerViewWishlist;
    ArrayList<String> filterList = initFilterList();
    DatabaseReference databaseRefUser = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("user");
    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();

    public wishlistfrag() {
        // Required empty public constructor
    }

    public static wishlistfrag newInstance(String param1, String param2) {
        wishlistfrag fragment = new wishlistfrag();
        Bundle args = new Bundle();
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
        View view  = inflater.inflate(R.layout.fragment_wishlist, container, false);
        if(savedInstanceState != null){

        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //WishList List
        recyclerViewWishlist = view.findViewById(R.id.recyclerWishlist);
        ArrayList<Product> wProdList = new ArrayList<>();
        ShoppingRecyclerAdapter wishlistAdapter = new ShoppingRecyclerAdapter(wProdList, getContext(),2);//wishlist layout
        databaseRefUser.child(usr.getUid().toString()).child("wishlist").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ss : snapshot.getChildren()){
                    Product product = ss.getValue(Product.class);
                    wProdList.add(product);
                }
                wishlistAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("error", "loadPost:onCancelled", error.toException());
            }
        });
        LinearLayoutManager vLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerViewWishlist.setLayoutManager(vLayoutManager);
        recyclerViewWishlist.setItemAnimator(new DefaultItemAnimator());
        recyclerViewWishlist.setAdapter(wishlistAdapter);

        SearchView search = view.findViewById(R.id.wishSearch);//wishlist searchbar
        int id = search.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEdit = search.findViewById(id);
        searchEdit.setTextColor(Color.BLACK);

        search.setSubmitButtonEnabled(true);//enable submit button
        search.setOnClickListener(new View.OnClickListener() {//make the whole searchview avaialble for input
            @Override
            public void onClick(View view) {
                search.setIconified(false);
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {//searchview listener
            @Override
            public boolean onQueryTextSubmit(String s) {//user submitted
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                wProdList.clear();//clear list
                databaseRefUser.child(usr.getUid().toString()).child("wishlist").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ss : snapshot.getChildren()){
                            Product product = ss.getValue(Product.class);
                            if (product.getTitle().toString().toLowerCase().contains(s)){//see if product title contains seach
                                wProdList.add(product);
                            }
                        }
                        wishlistAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("error", "loadPost:onCancelled", error.toException());
                    }
                });
                return false;
            }
        });

        //WishList Filters
        recyclerViewFilter = view.findViewById(R.id.recyclerFilter);
        wishlistFilterAdapter wFilterAdapter = new wishlistFilterAdapter(filterList,wishlistAdapter,wProdList);

        //Layout manager
        LinearLayoutManager hLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);//set horizontal layout
        recyclerViewFilter.setLayoutManager(hLayoutManager);
        recyclerViewFilter.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFilter.setAdapter(wFilterAdapter);//set adapter for wishlist filters
    }



    private ArrayList<String> initFilterList(){
        ArrayList<String> filterList = new ArrayList<>(Arrays.asList("Default" ,"Price [Low - High]","Price [High - Low]","Name [a - z]","Name [z - a]"));
        return filterList;
    }
}