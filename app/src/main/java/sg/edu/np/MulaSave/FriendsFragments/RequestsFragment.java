package sg.edu.np.MulaSave.FriendsFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import sg.edu.np.MulaSave.R;
import sg.edu.np.MulaSave.User;

public class RequestsFragment extends Fragment {

    RecyclerView requestRecycler;
    DatabaseReference databaseRefUser = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("user");
    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<User> requestList;

    public RequestsFragment() {
        // Required empty public constructor
    }

    public static RequestsFragment newInstance() {
        RequestsFragment fragment = new RequestsFragment();
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
        View view = inflater.inflate(R.layout.fragment_requests, container, false);
        requestRecycler = view.findViewById(R.id.requestRecycler);//set recycler
        requestList = new ArrayList<>();//init list

        ViewFriendAdapter rAdapter = new ViewFriendAdapter(requestList,1);
        databaseRefUser.child(usr.getUid()).child("requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ss : snapshot.getChildren()){//get requester uid
                        databaseRefUser.child(ss.getKey().toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = new User();
                                for (DataSnapshot ds : snapshot.getChildren()){
                                    if (ds.getKey().equals("uid")){
                                        user.setUid(ds.getValue().toString());
                                    }
                                    if(ds.getKey().equals("email")){
                                        user.setEmail(ds.getValue().toString());
                                    }
                                    if(ds.getKey().equals("username")){
                                        user.setUsername(ds.getValue().toString());
                                    }
                                }
                                requestList.add(user);
                                rAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Log.i("knn", ss.getKey().toString());
                    }
                }
            }//end of onDataChange

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LinearLayoutManager vLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);//set layout, 1 item per row
        requestRecycler.setLayoutManager(vLayoutManager);
        requestRecycler.setItemAnimator(new DefaultItemAnimator());
        requestRecycler.setAdapter(rAdapter);//set adapter

        return view;
    }
}