package sg.edu.np.P05TeamB;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SelectProfilePic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_profile_pic);
        ImageView preview = findViewById(R.id.previewPic);
        Button confirm = findViewById(R.id.cfmBtn);
        Button back = findViewById(R.id.bckBtn);

        Intent intent = getIntent();
        String uri = intent.getStringExtra("path");
        Uri pfpUri = Uri.parse(uri);
        preview.setImageURI(pfpUri);

        confirm.setOnClickListener(new View.OnClickListener() {//save image to firebase and go back to profile page
            @Override
            public void onClick(View view) {
                //upload to firebase

                //go back to profile page
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {//back button to go back without saving the new profile picture
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}