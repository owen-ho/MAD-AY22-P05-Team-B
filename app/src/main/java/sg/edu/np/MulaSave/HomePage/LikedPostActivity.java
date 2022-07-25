package sg.edu.np.MulaSave.HomePage;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

import sg.edu.np.MulaSave.R;

public class LikedPostActivity extends AppCompatActivity {

    RecyclerView likedPostRecycler;
    ArrayList<Post> likedList;
    PostAdapter likedAdapter;
    TextView likedCount, likedPostNoDisplay;
    ImageView likedBackTrack;

    FirebaseDatabase databaseRef = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference databaseRefUser = databaseRef.getReference("user");
    DatabaseReference databaseRefPost = databaseRef.getReference("post");
    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_post);

        likedPostRecycler = findViewById(R.id.likedPostRecycler);
        likedList = new ArrayList<>();
        likedAdapter = new PostAdapter(likedList);
        likedCount = findViewById(R.id.likedCount);
        likedBackTrack = findViewById(R.id.likedBackTrack);
        likedPostNoDisplay = findViewById(R.id.likedPostNoDisplay);

        //on click listeners for backtrack
        likedBackTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //single event so that the post does not get removed from the liked page upon unlike
        databaseRefUser.child(usr.getUid()).child("likedposts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likedList.clear();
                for (DataSnapshot ss : snapshot.getChildren()){
                    likedList.add(ss.getValue(Post.class));
                }
                likedCount.setText(String.valueOf(likedList.size()));
                Collections.sort(likedList,postComparator);
                if(likedList.size()==0){
                    likedPostNoDisplay.setVisibility(View.VISIBLE);
                }
                likedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LikedPostActivity.this,LinearLayoutManager.VERTICAL,false);//set layout, 1 item per row

        likedPostRecycler.setLayoutManager(linearLayoutManager);
        likedPostRecycler.setItemAnimator(new DefaultItemAnimator());
        likedPostRecycler.setAdapter(likedAdapter);//set adapter
    }
    public Comparator<Post> postComparator = new Comparator<Post>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public int compare(Post p1, Post p2) {
            int l1 = Instant.parse(p2.getPostDateTime()).compareTo(Instant.parse(p1.getPostDateTime()));
            return l1;
        }
    };
}