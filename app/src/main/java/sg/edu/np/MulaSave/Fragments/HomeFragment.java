package sg.edu.np.MulaSave.Fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import sg.edu.np.MulaSave.HomePage.FriendsActivity;
import sg.edu.np.MulaSave.HomePage.AddPostActivity;
import sg.edu.np.MulaSave.HomePage.LikedPostActivity;
import sg.edu.np.MulaSave.HomePage.Post;
import sg.edu.np.MulaSave.HomePage.PostAdapter;
import sg.edu.np.MulaSave.R;

public class HomeFragment extends Fragment {

    ImageView addFriend, addPost, viewLikes;
    RecyclerView postRecycler;
    ArrayList<Post> postList;
    PostAdapter postAdapter;
    TextView postNoDisplay;

    FirebaseDatabase databaseRef = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference databaseRefUser = databaseRef.getReference("user");
    DatabaseReference databaseRefPost = databaseRef.getReference("post");
    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();

    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inflate the layout for this fragment
        return view;
    }


    //create this method because getView() only works after onCreateView()
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        addFriend = view.findViewById(R.id.hAddFirend);//get the add friend and post imageviews
        addPost = view.findViewById(R.id.hAddPost);
        viewLikes = view.findViewById(R.id.hLikes);
        postNoDisplay = view.findViewById(R.id.postNoDisplay);

        addFriend.setOnClickListener(new View.OnClickListener() {//set on click listener
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), FriendsActivity.class);//go to add friends class
                startActivity(i);
            }
        });

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//go to add post activity
                Intent i = new Intent(getActivity(), AddPostActivity.class);
                startActivity(i);
            }
        });

        viewLikes.setOnClickListener(new View.OnClickListener() {//start activity to see the liked posts
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), LikedPostActivity.class);
                startActivity(i);
            }
        });

        postNoDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPost.performClick();
            }
        });
        postRecycler = view.findViewById(R.id.postRecycler);//set recycler
        postList = new ArrayList<>();//create new arraylist
        postAdapter = new PostAdapter(postList);//create new adapter

        databaseRefPost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ss : snapshot.getChildren()){
                    Post post = ss.getValue(Post.class);
                    if (post.getCreatorUid().equals(usr.getUid())){//add the post into the post list if the current user created it
                        postList.add(post);
                    }
                    databaseRefUser.child(usr.getUid()).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()){
                                if(post.getCreatorUid().equals(ds.getKey().toString())){//if the creator is friends with the current user
                                    postList.add(post);//add if they are friends
                                    postNoDisplay.setVisibility(View.INVISIBLE);
                                }
                            }
                            Collections.sort(postList,postComparator);
                            if(postList.size()==0){
                                postNoDisplay.setVisibility(View.VISIBLE);
                            }
                            postAdapter.notifyDataSetChanged();
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);//set layout, 1 item per row

        postRecycler.setLayoutManager(linearLayoutManager);
        postRecycler.setItemAnimator(new DefaultItemAnimator());
        postRecycler.setAdapter(postAdapter);//set adapter


    }//end of onview created method

    /*@Override
    public void onResume() {
        super.onResume();
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }*/

    /**
     * desc
     * Param
     * return
     */
    public Comparator<Post> postComparator = new Comparator<Post>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public int compare(Post p1, Post p2) {
            int l1 = Instant.parse(p2.getPostDateTime()).compareTo(Instant.parse(p1.getPostDateTime()));
            return l1;
        }
    };
}