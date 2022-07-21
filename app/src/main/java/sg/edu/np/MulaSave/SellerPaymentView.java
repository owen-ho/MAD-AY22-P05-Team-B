package sg.edu.np.MulaSave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class SellerPaymentView extends AppCompatActivity {

    Product product;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_payment_view);

        ImageView sellerpaymentback = findViewById(R.id.SellerPaymentBack);
        ImageView declinepaymentbtn = findViewById(R.id.DeclinePaymentBtn);
        ImageView acceptpaymentbtn = findViewById(R.id.AcceptPaymentBtn);
        ImageView paymentproof = findViewById(R.id.proofofpayment);
        ImageView paymentRefresh = findViewById(R.id.refreshPaymentbtn);

        Intent i = getIntent();
        product = (Product) i.getSerializableExtra("product");

        if (product != null){
            paymentRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    storageRef.child("paymentpics/" + product.getAsin() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {//user has sent a payment
                            Picasso.get().load(uri).into(paymentproof);
                        }
                    }).addOnFailureListener(new OnFailureListener() {//file does not exist (user did not upload before)
                        @Override
                        public void onFailure(@NonNull Exception e) {//set default picture
                            Toast.makeText(SellerPaymentView.this,"Buyer has not uploaded their payment!",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        sellerpaymentback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        declinepaymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageReference paymentPic = storageRef.child("paymentpics/" + product.getAsin() +".png");
                paymentPic.delete().addOnSuccessListener(new OnSuccessListener<Void>() { // to remove the image url from firebase storage
                    @Override
                    public void onSuccess(Void unused) {
                    }
                });
                Intent intent = new Intent(SellerPaymentView.this, ChildReserveFragment.class);
                finish();
            }
        });

        acceptpaymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}