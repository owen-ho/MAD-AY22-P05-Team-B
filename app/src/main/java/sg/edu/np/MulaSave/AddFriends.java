package sg.edu.np.MulaSave;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddFriends extends AppCompatActivity {

    RecyclerView requestsRecyclerView, exploreRecyclerView;
    DatabaseReference databaseRefUser = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("user");
    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<User> exploreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        requestsRecyclerView = findViewById(R.id.requestsRecycler);
        exploreRecyclerView = findViewById(R.id.exploreRecycler);

        ExploreFriendAdapter Eadapter = new ExploreFriendAdapter(exploreList);
        LinearLayoutManager vLayoutManager = new LinearLayoutManager(AddFriends.this,LinearLayoutManager.VERTICAL,false);//set layout, 1 item per row
        exploreRecyclerView.setLayoutManager(vLayoutManager);
        exploreRecyclerView.setItemAnimator(new DefaultItemAnimator());
        exploreRecyclerView.setAdapter(Eadapter);//set adapter
    }
}