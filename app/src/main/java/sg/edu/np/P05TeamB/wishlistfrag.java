package sg.edu.np.P05TeamB;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class wishlistfrag extends Fragment {
    RecyclerView recyclerViewFilter;
    RecyclerView recyclerViewWishlist;
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
        return inflater.inflate(R.layout.fragment_wishlist, container, false);

    }
}