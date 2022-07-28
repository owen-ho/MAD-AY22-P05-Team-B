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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UploadPayment extends AppCompatActivity {

    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();

    Product product;
    int SELECT_PICTURE = 200;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    ImageView previewPayment;

    @Override
    /**
     * This activity would:
     * 1. Allow the buyer to send a screenshot of their proof of payment to the seller
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_payment);

        //Initialising the variables and views from the xml
        ImageView BackbuttonPayment = findViewById(R.id.backButtonPayment);
        ImageView NoSubmitPaymentbtn = findViewById(R.id.NoSubmitPaymentbtn);
        ImageView ConfirmPaymentbtn = findViewById(R.id.confirmPaymentBtn);
        ImageView AddPaymentbtn = findViewById(R.id.addPaymentBtn);
        previewPayment = findViewById(R.id.previewPayment);

        Intent i = getIntent();
        product = (Product) i.getSerializableExtra("product");

        // Let the user click back to the previous page using the back button located in this activity
        BackbuttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadPayment.this, ChildReserveFragment.class);
                finish();
            }
        });

        // When the buyer clicks on the confirm button after uploading their proof of payment
        ConfirmPaymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadPayment.this, ChildReserveFragment.class);// intent the buyer back to the previous page
                addPaymentMadeNotifications(usr.getUid(), product.getSellerUid(), product.getAsin());// To notify the seller that the buyer has provided their proof of payment
                finish();
            }
        });

        // When the buyer click on the no button after uploading their proof of payment
        NoSubmitPaymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageReference paymentPic = storageRef.child("paymentpics/" + product.getAsin() +".png"); //To get the id of the image uploaded to the firebase
                paymentPic.delete().addOnSuccessListener(new OnSuccessListener<Void>() { // to remove the image url from firebase storage
                    @Override
                    public void onSuccess(Void unused) {
                    }
                });
                Intent intent = new Intent(UploadPayment.this, ChildReserveFragment.class);
                finish();
            }
        });

        //To add proof of payment from the users phone
        AddPaymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImg();// calling of function
            }
        });


    }
    //function to add the proof of payment from the buyers phone
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
                            Toast.makeText(UploadPayment.this,"Upload Success!",Toast.LENGTH_SHORT).show();
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

    /**
     * This is to save the notification to the firebase:
     * 1. To notify the seller that payment has been made by the buyer
     * @param buyerid
     * @param sellerid
     * @param productid
     */
    private void addPaymentMadeNotifications(String buyerid, String sellerid, String productid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("notifications").child(sellerid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", buyerid);
        hashMap.put("text", "Payment has been made to you!");
        hashMap.put("productid", productid);
        hashMap.put("isproduct",true);

        reference.push().setValue(hashMap);
    }
}