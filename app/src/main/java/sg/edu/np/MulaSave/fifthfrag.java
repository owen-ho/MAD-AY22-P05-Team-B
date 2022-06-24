package sg.edu.np.MulaSave;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class fifthfrag extends Fragment {
    RecyclerView recyclerViewUploads;
    DatabaseReference databaseRefProduct = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("product");//get firebase instance to all uploaded products
    ArrayList<Product> productList;

    public fifthfrag() {
        // Required empty public constructor
    }

    public static fifthfrag newInstance() {
        fifthfrag fragment = new fifthfrag();
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
        View view = inflater.inflate(R.layout.fragment_fifthfrag, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewUploads = view.findViewById(R.id.uploadsRecycler);
        productList = new ArrayList<>();
        ShoppingRecyclerAdapter prodAdapter = new ShoppingRecyclerAdapter(productList,getContext(),1);//set adapter with  search layout (layout 1)
        databaseRefProduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ss : snapshot.getChildren()){
                    Product p = ss.getValue(Product.class);//get all uploaded products and convert to product objects
                    productList.add(p);//add products
                }
                prodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("error", "loadPost:onCancelled", error.toException());
            }
        });
        //set the layout
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2,GridLayoutManager.VERTICAL,false);//layout with 2 items per row
        recyclerViewUploads.setLayoutManager(gridLayoutManager);
        recyclerViewUploads.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUploads.setAdapter(prodAdapter);

        SearchView search = view.findViewById(R.id.uploadSearch);
        int id = search.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);//make the input text black
        EditText searchEdit = search.findViewById(id);
        searchEdit.setTextColor(Color.BLACK);

        search.setSubmitButtonEnabled(true);//enable submit button on search bar
        search.setOnClickListener(new View.OnClickListener() {//make the whole searchview avaialble for input
            @Override
            public void onClick(View view) {
                search.setIconified(false);
            }
        });

        //look for items in searchview
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {//so that the search can be updated everytime user enters words, not just onSubmit
                productList.clear();//clear list of products
                databaseRefProduct.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ss : snapshot.getChildren()){
                            Product p = ss.getValue(Product.class);
                            if(p.getTitle().toString().toLowerCase().contains(s)){
                                productList.add(p);//add all the products from search
                            }
                        }
                        prodAdapter.notifyDataSetChanged();//update adapter after all required products are added
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("DatabaseError", String.valueOf(error));
                    }
                });
                return true;//view is handled by us
            }
        });//end of search on query text listener

        FloatingActionButton uploadbutton = view.findViewById(R.id.uploadproductbutton);
        uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UserInputPrice.class);
                startActivity(i);
            }
        });
    }
}