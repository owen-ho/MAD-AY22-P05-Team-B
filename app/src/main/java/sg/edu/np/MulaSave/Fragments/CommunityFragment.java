package sg.edu.np.MulaSave.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.MulaSave.Product;
import sg.edu.np.MulaSave.R;
import sg.edu.np.MulaSave.ShoppingRecyclerAdapter;
import sg.edu.np.MulaSave.UserInputPrice;
import sg.edu.np.MulaSave.chatfeaturetesting;
import sg.edu.np.MulaSave.descriptionpage;
import sg.edu.np.MulaSave.wishlistFilterAdapter;

public class CommunityFragment extends Fragment {
    RecyclerView recyclerViewUploads;
    RecyclerView recyclerViewFilterUploads;
    Product product;
    DatabaseReference databaseRefProduct = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("product");//get firebase instance to all uploaded products
    ArrayList<Product> productList;
    ArrayList<Product> filterList;

    public CommunityFragment() {
        // Required empty public constructor
    }

    public static CommunityFragment newInstance() {
        CommunityFragment fragment = new CommunityFragment();
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
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewUploads = view.findViewById(R.id.uploadsRecycler);
        ImageView messagebutton = view.findViewById(R.id.imageView18);
        productList = new ArrayList<>();
        ShoppingRecyclerAdapter prodAdapter = new ShoppingRecyclerAdapter(productList,getContext(),1);//set adapter with  search layout (layout 1)
        databaseRefProduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
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

        messagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), chatfeaturetesting.class);

                i.putExtra("product",product);

                startActivity(i);
            }
        });
        //set the layout
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1,GridLayoutManager.VERTICAL,false);//layout with 2 items per row
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

        //set on searchview open listener for searchview
        search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView)getView().findViewById(R.id.communityTitle)).setVisibility(View.GONE);//set the title to be gone
                ConstraintLayout layout = (ConstraintLayout) getView().findViewById(R.id.communityConstraintLayout);//get constraintlayout
                ConstraintSet set = new ConstraintSet();
                set.clone(layout);
                //set constraints
                set.connect(R.id.communitySearchCard, ConstraintSet.START,R.id.communityConstraintLayout,ConstraintSet.START,0);
                set.connect(R.id.communitySearchCard, ConstraintSet.END,R.id.communityConstraintLayout,ConstraintSet.END,0);
                set.applyTo(layout);
            }
        });
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                ((TextView)getView().findViewById(R.id.communityTitle)).setVisibility(View.VISIBLE);

                //to convert margin to dp
                Resources r = getActivity().getResources();
                int px = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        24,
                        r.getDisplayMetrics()
                );

                //set layout
                ConstraintLayout layout = (ConstraintLayout) getView().findViewById(R.id.communityConstraintLayout);
                ConstraintSet set = new ConstraintSet();
                set.clone(layout);
                //clear constraints
                set.clear(R.id.communitySearchCard, ConstraintSet.START);
                set.connect(R.id.communitySearchCard, ConstraintSet.END,R.id.communityConstraintLayout,ConstraintSet.END,px);
                set.applyTo(layout);
                return false;//return false so that icon closes back on close
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
                            if(p.getTitle().toString().toLowerCase().contains(s.toLowerCase())){
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

        //to navigate user from homefrag to community frag
        Bundle bundle = this.getArguments();
        if(bundle!= null){
            Boolean srch = bundle.getBoolean("condition",false);
            if(srch){//condition passed from the homefrag
                //set the click
                search.performClick();
                search.requestFocus();
                //focus and show keyboard for inputs from user
                search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        if (hasFocus) {
                            showInputMethod(view.findFocus());
                        }
                    }
                });
            }
        }

        FloatingActionButton uploadbutton = view.findViewById(R.id.uploadproductbutton);
        uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UserInputPrice.class);
                startActivity(i);
            }
        });
        //WishList Filters
        recyclerViewFilterUploads = view.findViewById(R.id.communityFilter);
        wishlistFilterAdapter wFilterAdapter = new wishlistFilterAdapter(getView(),prodAdapter,productList,2);

        //Layout manager for filters recyclerview
        LinearLayoutManager hLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);//set horizontal layout
        recyclerViewFilterUploads.setLayoutManager(hLayoutManager);
        recyclerViewFilterUploads.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFilterUploads.setAdapter(wFilterAdapter);//set adapter for wishlist filters
    }
    private void showInputMethod(View view) {
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mgr != null) {
            mgr.showSoftInput(view, 0);
        }
    }
}