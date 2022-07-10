package sg.edu.np.MulaSave.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sg.edu.np.MulaSave.APIHandler;
import sg.edu.np.MulaSave.AddFriends;
import sg.edu.np.MulaSave.AddPostActivity;
import sg.edu.np.MulaSave.MainActivity;
import sg.edu.np.MulaSave.Post;
import sg.edu.np.MulaSave.PostAdapter;
import sg.edu.np.MulaSave.Product;
import sg.edu.np.MulaSave.R;

public class HomeFragment extends Fragment {

    ImageView addFriend, addPost;
    RecyclerView postRecycler;
    ArrayList<Post> postList;
    PostAdapter postAdapter;

    DatabaseReference databaseRefUser = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("user");
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

        addFriend.setOnClickListener(new View.OnClickListener() {//set on click listener
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddFriends.class);//go to add friends class
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

        postRecycler = view.findViewById(R.id.postRecycler);//set recycler
        postList = new ArrayList<>();//create new arraylist
        postAdapter = new PostAdapter(postList);//create new adapter
        databaseRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();//so that the posts does not duplicate when user enters again
                for (DataSnapshot ss : snapshot.getChildren()){
                    for (DataSnapshot ds : ss.getChildren()){
                        if (ds.getKey().toString().equals("posts")){
                            for (DataSnapshot postSnapshot : ds.getChildren()){
                                postList.add(postSnapshot.getValue(Post.class));
                            }postAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);//set layout, 1 item per row
        linearLayoutManager.setReverseLayout(true);
        postRecycler.setLayoutManager(linearLayoutManager);
        postRecycler.setItemAnimator(new DefaultItemAnimator());
        postRecycler.setAdapter(postAdapter);//set adapter


    }//end of onview created method

}

