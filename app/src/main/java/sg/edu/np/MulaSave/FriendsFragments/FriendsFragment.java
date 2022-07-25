package sg.edu.np.MulaSave.FriendsFragments;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.MulaSave.HomePage.AddFriends;
import sg.edu.np.MulaSave.R;
import sg.edu.np.MulaSave.User;

public class FriendsFragment extends Fragment {

    RecyclerView friendRecycler;
    DatabaseReference databaseRefUser = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("user");
    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<User> friendList;
    SearchView searchFriendList;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        friendRecycler = view.findViewById(R.id.friendRecycler);
        friendList = new ArrayList<>();

        ViewFriendAdapter fAdapter = new ViewFriendAdapter(friendList,1);//1 means friend list
        databaseRefUser.child(usr.getUid()).child("friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendList.clear();
                for (DataSnapshot ss : snapshot.getChildren()){//ss.getKey() is the uid of each friend
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

                            if(AddFriends.addNewUser(user,friendList)){
                                friendList.add(user);
                                fAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LinearLayoutManager vLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);//set layout, 1 item per row
        friendRecycler.setLayoutManager(vLayoutManager);
        friendRecycler.setItemAnimator(new DefaultItemAnimator());
        friendRecycler.setAdapter(fAdapter);//set adapter

        searchFriendList = view.findViewById(R.id.searchFriendList);


        searchFriendList.setSubmitButtonEnabled(true);//enable submit button
        searchFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFriendList.setIconified(false);//make the whole searchview available for input
            }
        });
        //layout setting for the searchview
        searchFriendList.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout layout = (ConstraintLayout) getActivity().findViewById(R.id.friendListConstraint);//get constraintlayout
                ConstraintSet set = new ConstraintSet();
                set.clone(layout);

                //set constraints for start and end
                set.connect(R.id.searchFriendsCard,ConstraintSet.START, R.id.friendListConstraint,ConstraintSet.START,0);
                set.connect(R.id.searchFriendsCard,ConstraintSet.END, R.id.friendListConstraint,ConstraintSet.END,0);
                set.applyTo(layout);
                searchFriendList.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        friendList.clear();
                        databaseRefUser.child(usr.getUid()).child("friends").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ss : snapshot.getChildren()){//ss.getKey() is the uid of each friend
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
                                            //add the user if the username is under the
                                            if (user.getUsername().toLowerCase().contains(s.toLowerCase())){//dont add if the input is none && (!s.equals(""))
                                                friendList.add(user);
                                                fAdapter.notifyDataSetChanged();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        return false;
                    }
                });
            }
        });//end of onsearchclick listener

        searchFriendList.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //to convert margin to dp
                Resources r = getActivity().getResources();
                int px = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        24,
                        r.getDisplayMetrics()
                );

                //set layout
                ConstraintLayout layout = (ConstraintLayout) getActivity().findViewById(R.id.friendListConstraint);
                ConstraintSet set = new ConstraintSet();
                set.clone(layout);
                //clear constraints
                set.clear(R.id.searchFriendsCard, ConstraintSet.START);
                set.connect(R.id.searchFriendsCard, ConstraintSet.END,R.id.friendListConstraint,ConstraintSet.END,px);
                set.applyTo(layout);
                return false;//return false so that icon closes back on close
            }
        });
    }
}