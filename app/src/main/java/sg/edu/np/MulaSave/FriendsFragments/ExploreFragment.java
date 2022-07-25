package sg.edu.np.MulaSave.FriendsFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

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

public class ExploreFragment extends Fragment {
    RecyclerView exploreRecyclerView;
    DatabaseReference databaseRefUser = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("user");
    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<User> exploreList;
    SearchView searchFriendExplore;
    TextView exploreNoDisplay;

    public ExploreFragment() {
        // Required empty public constructor
    }


    public static ExploreFragment newInstance() {
        ExploreFragment fragment = new ExploreFragment();
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
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        exploreRecyclerView = view.findViewById(R.id.exploreRecycler);
        searchFriendExplore = view.findViewById(R.id.searchFriendExplore);
        exploreNoDisplay = view.findViewById(R.id.exploreNoDisplay);

        exploreList = new ArrayList<>();
        ViewFriendAdapter Eadapter = new ViewFriendAdapter(exploreList,3);
        databaseRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {//get data on success
                exploreList.clear();
                for (DataSnapshot ss : snapshot.getChildren()){
                    //User extractUser = ss.getValue(User.class);
                    User user = new User();
                    for (DataSnapshot ds : ss.getChildren()){//because the users may have wishlists and other fields, cannot extract directly to user class
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
                    if (user.getUid().equals(usr.getUid())){
                        //do nothing
                    }
                    else{
                        exploreList.add(user);//add user to the list
                    }
                }
                setVisible();
                Eadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchFriendExplore.setSubmitButtonEnabled(true);//enable submit button
        searchFriendExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFriendExplore.setIconified(false);//make the whole searchview available for input
            }
        });



        LinearLayoutManager vLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);//set layout, 1 item per row
        exploreRecyclerView.setLayoutManager(vLayoutManager);
        exploreRecyclerView.setItemAnimator(new DefaultItemAnimator());
        exploreRecyclerView.setAdapter(Eadapter);//set adapter
    }
    private void setVisible(){
        if(exploreList.size() != 0){
            exploreNoDisplay.setVisibility(View.INVISIBLE);
        }
    }
}