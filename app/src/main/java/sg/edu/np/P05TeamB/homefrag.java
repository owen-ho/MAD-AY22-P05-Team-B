package sg.edu.np.P05TeamB;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
                /*
                View view1 = getActivity().getCurrentFocus();//to hide keyboard in the home fragment
                if (view1 != null){
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }*/
                search.setIconified(false); // cannot be placed under setOnSearchListener because it will cause listener to be informed
            }
        });
        search.setSubmitButtonEnabled(true);

        //loading of image using picasso
        ImageView imageView = getView().findViewById(R.id.largeImage);
        Picasso.get().load("https://i.imgur.com/DvpvklR.png").into(imageView);
    }//end of onview created method
}