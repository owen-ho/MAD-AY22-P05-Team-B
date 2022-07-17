package sg.edu.np.MulaSave.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import sg.edu.np.MulaSave.R;

public class chat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
        final ImageView backbtn = findViewById(R.id.backbtn);
        final TextView nameTTv = findViewById(R.id.name);
        final EditText messageedittxt = findViewById(R.id.messageedittxt);
        final ImageView profilepic = findViewById(R.id.profilePic);
        final ImageView sendbtn = findViewById(R.id.sendbtn);

        // Retrieving data from message adapater class

        final String getName = getIntent().getStringExtra("name");
        final String getprofilepic = getIntent().getStringExtra("Profilepic");

        nameTTv.setText(getName);
        Picasso.get().load(getprofilepic).into(profilepic);

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}