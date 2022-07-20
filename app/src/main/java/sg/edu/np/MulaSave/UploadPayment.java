package sg.edu.np.MulaSave;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UploadPayment extends AppCompatActivity {


    int SELECT_PICTURE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_payment);

        ImageView BackbuttonPayment = findViewById(R.id.backButtonPayment);
        ImageView NoSubmitPaymentbtn = findViewById(R.id.NoSubmitPaymentbtn);
        ImageView ConfirmPaymentbtn = findViewById(R.id.confirmPaymentBtn);
        ImageView RefreshPayment = findViewById(R.id.refreshPayment);
        ImageView AddPaymentbtn = findViewById(R.id.addPaymentBtn);

        BackbuttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadPayment.this, ChildReserveFragment.class);
                finish();
            }
        });

        NoSubmitPaymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadPayment.this, ChildReserveFragment.class);
                finish();
            }
        });

        AddPaymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImg();
            }
        });

    }
    private void chooseImg(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Product Picture"),SELECT_PICTURE);
    }

}