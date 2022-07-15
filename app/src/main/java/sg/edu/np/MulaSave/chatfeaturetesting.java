package sg.edu.np.MulaSave;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class chatfeaturetesting extends AppCompatActivity {
    private String uid;
    private RecyclerView messagerecycleview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatfeaturetesting);
        messagerecycleview=findViewById(R.id.messgaerecycleview);
        uid = getIntent().getStringExtra("uid");
        messagerecycleview.setHasFixedSize(true);
        messagerecycleview.setLayoutManager(new LinearLayoutManager(this));
    }
}