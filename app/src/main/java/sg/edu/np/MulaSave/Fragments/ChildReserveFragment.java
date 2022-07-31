package sg.edu.np.MulaSave.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.MulaSave.FriendsFragments.RequestsFragment;
import sg.edu.np.MulaSave.Product;
import sg.edu.np.MulaSave.R;
import sg.edu.np.MulaSave.ReserveAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChildReserveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChildReserveFragment extends Fragment {
    static RecyclerView recyclerViewReserve;
    static LinearLayoutManager crfLayoutManager;
    DatabaseReference databaseRefUser = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("user");
    FirebaseUser usrReserve = FirebaseAuth.getInstance().getCurrentUser();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChildReserveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChildReserveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChildReserveFragment newInstance(String param1, String param2) {
        ChildReserveFragment fragment = new ChildReserveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_child_reserve, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Reserved List
        recyclerViewReserve = view.findViewById(R.id.recyclerReserve);
        ArrayList<Product> ReserveProdList = new ArrayList<>();
        ReserveAdapter ReserveAdapter = new ReserveAdapter(ReserveProdList);//Reserve layout
        databaseRefUser.child(usrReserve.getUid().toString()).child("Reserve").addValueEventListener(new ValueEventListener() {  //access user Reservelist and add to list
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ss : snapshot.getChildren()) {
                    Product product = ss.getValue(Product.class);
                    ReserveProdList.add(product);//add product to list
                }
                ReserveAdapter.notifyDataSetChanged(); // to update the adapter
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("error","LoadPost:onCancelled", error.toException());

            }
        });
        crfLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);//set layout, 1 item per row
        recyclerViewReserve.setLayoutManager(crfLayoutManager);
        recyclerViewReserve.setItemAnimator(new DefaultItemAnimator());
        recyclerViewReserve.setAdapter(ReserveAdapter);//set adapter
    }
    public static void crfScrollTop(){
        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(ChildReserveFragment.recyclerViewReserve.getContext()) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        smoothScroller.setTargetPosition(0);
        crfLayoutManager.startSmoothScroll(smoothScroller);
    }
}