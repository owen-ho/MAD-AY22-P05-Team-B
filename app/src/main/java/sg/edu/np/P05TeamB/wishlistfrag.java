package sg.edu.np.P05TeamB;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class wishlistfrag extends Fragment {
    RecyclerView recyclerViewFilter;
    RecyclerView recyclerViewWishlist;
    ArrayList<String> filterList = initFilterList();

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
        View view  = inflater.inflate(R.layout.fragment_wishlist, container, false);
        recyclerViewFilter = view.findViewById(R.id.recyclerFilter);
        wishlistFilterAdapter wFilterAdapter = new wishlistFilterAdapter(filterList);

        //Layout manager
        LinearLayoutManager hLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewFilter.setLayoutManager(hLayoutManager);
        recyclerViewFilter.setItemAnimator(new DefaultItemAnimator());

        recyclerViewFilter.setAdapter(wFilterAdapter);

        // Inflate the layout for this fragment
        return view;
    }

    private ArrayList<String> initFilterList(){
        ArrayList<String> filterList = new ArrayList<>(Arrays.asList("Date", "Price Low - High","Price High - Low","Name"));
        return filterList;
    }
}