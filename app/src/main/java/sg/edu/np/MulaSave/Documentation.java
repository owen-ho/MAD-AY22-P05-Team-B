package sg.edu.np.MulaSave;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Documentation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentation);
        ((FloatingActionButton)findViewById(R.id.floatingBack)).setOnClickListener(new View.OnClickListener() {//finish activity on click of the back button
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}