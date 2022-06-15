package sg.edu.np.P05TeamB;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import sg.edu.np.P05TeamB.databinding.ActivityMainBinding;

public class homefrag extends Fragment {

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

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    //create this method because getView() only works after onCreateView()
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        SearchView search = getView().findViewById(R.id.goSearch);

        //make the whole search bar clickable
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setIconified(false);
                Fragment fragment = new shoppingfrag();
                Bundle bundle = new Bundle();
                bundle.putBoolean("condition", true);
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, fragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                //set the shop menu item active
                BottomNavigationView navigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavigationView);
                navigationView.setSelectedItemId(R.id.Shop);
                transaction.commit();
            }
        });
        search.setSubmitButtonEnabled(true);

        //navigate to new activity after entering
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Bundle bundle = new Bundle();
                bundle.putString("searchInput", s);

                Fragment fragment = new shoppingfrag();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, fragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                //set the shop menu item active
                BottomNavigationView navigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavigationView);
                navigationView.setSelectedItemId(R.id.Shop);
                transaction.commit();

                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        //loading of image using picasso
        ImageView imageView = getView().findViewById(R.id.largeImage);
        Picasso.get().load("https://i.imgur.com/DvpvklR.png").into(imageView);
    }//end of onview created method
}