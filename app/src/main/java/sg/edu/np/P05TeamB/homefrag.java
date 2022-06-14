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

import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
            }
        });

        //navigate to new activity after entering
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                getActivity().overridePendingTransition(0, 0);
                Intent i = new Intent(getActivity(), SearchProduct.class);
                startActivity(i);
                getActivity().overridePendingTransition(0, 0);
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