package sg.edu.np.MulaSave.HomePage;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import sg.edu.np.MulaSave.Fragments.HomeFragment;
import sg.edu.np.MulaSave.R;


public class HomeExplorePosts extends Fragment {
    static RecyclerView explorePostRecycler;
    ArrayList<Post> postList;
    PostAdapter postAdapter;
    TextView ePostNoDisplay;
    static LinearLayoutManager epLinearLayoutManager;
    FirebaseDatabase databaseRef = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference databaseRefPost = databaseRef.getReference("post");

    public HomeExplorePosts() {
        // Required empty public constructor
    }
    public static HomeExplorePosts newInstance() {
        HomeExplorePosts fragment = new HomeExplorePosts();
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
        return inflater.inflate(R.layout.fragment_home_explore_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        explorePostRecycler = view.findViewById(R.id.homeExploreRecycler);//set recycler
        postList = new ArrayList<>();//create new arraylist
        postAdapter = new PostAdapter(postList);//create new adapter
        ePostNoDisplay = view.findViewById(R.id.ePostNoDisplay);
        initPostMain();

        epLinearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);//set layout, 1 item per row
        explorePostRecycler.setLayoutManager(epLinearLayoutManager);
        explorePostRecycler.setItemAnimator(new DefaultItemAnimator());
        explorePostRecycler.setAdapter(postAdapter);//set adapter
    }
    private void initPostMain(){
        databaseRefPost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ss : snapshot.getChildren()){
                    Post post = ss.getValue(Post.class);
                    postList.add(post);
                }
                Collections.sort(postList,postComparator);
                if(postList.size()==0){
                    ePostNoDisplay.setVisibility(View.VISIBLE);
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("HomeExplorePosts",error.toString());
            }
        });
    }

    /**
     * Custom comparator to sort the timing of the posts to the latest first
     */
    public Comparator<Post> postComparator = new Comparator<Post>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public int compare(Post p1, Post p2) {
            int l1 = Instant.parse(p2.getPostDateTime()).compareTo(Instant.parse(p1.getPostDateTime()));
            return l1;
        }
    };

    /**
     * function to scroll the explorePosts recyclerview to top
     */
    public static void epScrollTop(){
        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(HomeExplorePosts.explorePostRecycler.getContext()) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        smoothScroller.setTargetPosition(0);
        epLinearLayoutManager.startSmoothScroll(smoothScroller);
    }
}