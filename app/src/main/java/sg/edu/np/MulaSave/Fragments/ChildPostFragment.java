package sg.edu.np.MulaSave.Fragments;

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
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.MulaSave.HomePage.Post;
import sg.edu.np.MulaSave.HomePage.PostAdapter;
import sg.edu.np.MulaSave.R;


public class ChildPostFragment extends Fragment {

    RecyclerView userPostRecycler;
    ArrayList<Post> userPostList;
    PostAdapter userPostAdapter;

    FirebaseDatabase databaseRef = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference databaseRefUser = databaseRef.getReference("user");
    DatabaseReference databaseRefPost = databaseRef.getReference("post");
    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();

    public ChildPostFragment() {
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
        return inflater.inflate(R.layout.fragment_child_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userPostRecycler = view.findViewById(R.id.userPostRecycler);//ref to the recyclerview
        userPostList = new ArrayList<>();//init arraylist of post
        userPostAdapter = new PostAdapter(userPostList);//set adapter with the arraylist

        databaseRefPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userPostList.clear();
                for (DataSnapshot ss : snapshot.getChildren()){
                    Post post = ss.getValue(Post.class);
                    if (usr.getUid().equals(post.getCreatorUid())){//check if the creator uid is same as current uid
                        userPostList.add(post);//add post
                    }
                    userPostAdapter.notifyDataSetChanged();//notifydataset changed
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);//set layout, 1 item per row

        userPostRecycler.setLayoutManager(linearLayoutManager);
        userPostRecycler.setItemAnimator(new DefaultItemAnimator());
        userPostRecycler.setAdapter(userPostAdapter);//set adapter
    }
}