package sg.edu.np.MulaSave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UploadPayment extends AppCompatActivity {

    Product product;
    int SELECT_PICTURE = 200;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    ImageView previewPayment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_payment);

        ImageView BackbuttonPayment = findViewById(R.id.backButtonPayment);
        ImageView NoSubmitPaymentbtn = findViewById(R.id.NoSubmitPaymentbtn);
        ImageView ConfirmPaymentbtn = findViewById(R.id.confirmPaymentBtn);
        ImageView RefreshPayment = findViewById(R.id.refreshPayment);
        ImageView AddPaymentbtn = findViewById(R.id.addPaymentBtn);
        previewPayment = findViewById(R.id.previewPayment);

        Intent i = getIntent();
        product = (Product) i.getSerializableExtra("product");

        BackbuttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadPayment.this, ChildReserveFragment.class);
                finish();
            }
        });

        ConfirmPaymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadPayment.this, ChildReserveFragment.class);
                finish();
            }
        });

        NoSubmitPaymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageReference paymentPic = storageRef.child("paymentpics/" + product.getAsin() +".png");
                paymentPic.delete().addOnSuccessListener(new OnSuccessListener<Void>() { // to remove the image url from firebase storage
                    @Override
                    public void onSuccess(Void unused) {
                    }
                });
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

        RefreshPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storageRef.child("paymentpics/" + product.getAsin() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {//user has set a profile picture before
                        Picasso.get().load(uri).into(previewPayment);
                    }
                }).addOnFailureListener(new OnFailureListener() {//file does not exist (user did not upload before)
                    @Override
                    public void onFailure(@NonNull Exception e) {//set default picture

                    }
                });
            }
        });

    }
    private void chooseImg(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Product Picture"),SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri paymentUri = data.getData();
                if (null != paymentUri) {
                    previewPayment.setImageURI(paymentUri);
                    StorageReference paymentPic = storageRef.child("paymentpics/" + product.getAsin() +".png");
                    paymentPic.putFile(paymentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(UploadPayment.this,"Upload Success! Refresh to see changes",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadPayment.this,"Upload failed, try again later",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }//end of onActivityResult
}