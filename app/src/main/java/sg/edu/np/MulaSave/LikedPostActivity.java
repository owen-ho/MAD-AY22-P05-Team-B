package sg.edu.np.MulaSave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

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

public class LikedPostActivity extends AppCompatActivity {

    RecyclerView likedPostRecycler;
    ArrayList<Post> likedList;
    PostAdapter likedAdapter;
    TextView likedCount;

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

        databaseRefUser.child(usr.getUid()).child("likedposts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likedList.clear();
                for (DataSnapshot ss : snapshot.getChildren()){
                    likedList.add(ss.getValue(Post.class));
                }
                likedCount.setText(String.valueOf(likedList.size()));
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
}