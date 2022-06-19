package sg.edu.np.P05TeamB;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserInputPrice extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference productRef = database.getReference("product");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input_price);

        EditText productTitle = findViewById(R.id.titleProduct);
        EditText productPrice = findViewById(R.id.priceProduct);
        EditText productWebsite = findViewById(R.id.websiteProduct);
        Button submitProductbtn = findViewById(R.id.submitProductButton);

        submitProductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pt = productTitle.getText().toString();
                Double pp = Double.parseDouble(productPrice.getText().toString());
                String pw = productWebsite.getText().toString();

                productRef.child(pt).child("price").setValue(pp);
                productRef.child(pt).child("website").setValue(pw);
                Intent i = new Intent(UserInputPrice.this,MainActivity.class);
                i.putExtra("frgToLoad",0); //Profile frag is the 4th fragment
                startActivity(i);
            }
        });

    }
}