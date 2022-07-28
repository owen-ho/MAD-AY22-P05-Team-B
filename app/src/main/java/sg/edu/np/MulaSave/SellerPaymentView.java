package sg.edu.np.MulaSave;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SellerPaymentView extends AppCompatActivity {

    DatabaseReference databaseRefUser = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("user");
    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();

    Product product;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    /**
     * This activity would
     * 1. Display the proof of payment the buyer has uploaded
     * 2. Accept or decline the proof of payment
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_payment_view);

        //Initialising the variables and views from the xml
        ImageView sellerpaymentback = findViewById(R.id.SellerPaymentBack);
        ImageView declinepaymentbtn = findViewById(R.id.DeclinePaymentBtn);
        ImageView acceptpaymentbtn = findViewById(R.id.AcceptPaymentBtn);
        ImageView paymentproof = findViewById(R.id.proofofpayment);
        ImageView paymentRefresh = findViewById(R.id.refreshPaymentbtn);

        Intent i = getIntent();
        product = (Product) i.getSerializableExtra("product");

        //To get and display the proof of payment from the firebase
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

        //Let the user click back to the previous page using the back button located in this activity
        sellerpaymentback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // The seller can decline the payment uploaded from the buyer
        declinepaymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageReference paymentPic = storageRef.child("paymentpics/" + product.getAsin() +".png"); //To get the id of the image uploaded to the firebase
                paymentPic.delete().addOnSuccessListener(new OnSuccessListener<Void>() { // to remove the image url from firebase storage
                    @Override
                    public void onSuccess(Void unused) {
                        databaseRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot ds: snapshot.getChildren()){//Cycle through users in the firebase
                                    for (DataSnapshot ds1: ds.child("Reserve").getChildren()){//Cycle through the firebase to look for the products inside the users reserve
                                        Product prod = ds1.getValue(Product.class);//To convert the object in the firebase into a product
                                        if (product.getImageUrl().equals(prod.getImageUrl())){// If the current product image url is equals to the product image in any users reserve
                                            addPaymentDeclinedNotifications(usr.getUid(), ds.getKey(), product.getAsin());// To inform the buyer that the seller has declined their proof of payment
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                Intent intent = new Intent(SellerPaymentView.this, ChildReserveFragment.class);
                finish();
            }
        });

        // The seller can accept the payment uploaded from the buyer and mark the product as sold
        acceptpaymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ReserveUnique = ((product.getImageUrl()).replaceAll("[^a-zA-Z0-9]", ""));//To get the id of the current product
                databaseRefUser.child(usr.getUid().toString()).child("Sold").child(ReserveUnique).setValue(product);//add product if the product does not exist in the database
                databaseRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){//Cycle through users in the firebase
                            for (DataSnapshot ds1: ds.child("Reserve").getChildren()){//Cycle through the firebase to look for the products inside the users reserve
                                Product prod = ds1.getValue(Product.class);//To convert the object in the firebase into a product
                                if (product.getImageUrl().equals(prod.getImageUrl())){//If the current product image url is equals to the product image in any users reserve
                                    ds1.getRef().removeValue();//Remove the product from any reserve in users
                                    addPaymentAcceptedNotifications(usr.getUid(), ds.getKey(), product.getAsin());// To inform the buyer that the seller has accepted their proof of payment
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Intent intent = new Intent(SellerPaymentView.this, ChildReserveFragment.class);
                finish();
            }
        });
    }

    /**
     * This is to save the notification to the firebase:
     * 1. To notify buyer that the payment has been accepted by the seller
     * @param sellerid
     * @param buyerid
     * @param productid
     */
    private void addPaymentAcceptedNotifications(String sellerid, String buyerid, String productid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("notifications").child(buyerid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", sellerid);
        hashMap.put("text", "Seller has accepted your payment!");
        hashMap.put("productid", productid);
        hashMap.put("isproduct",true);

        reference.push().setValue(hashMap);
    }

    /**
     * This is to save the notification to the firebase:
     * 1. To notify the buyer that the payment has been declined by the seller
     * @param sellerid
     * @param buyerid
     * @param productid
     */
    private void addPaymentDeclinedNotifications(String sellerid, String buyerid, String productid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("notifications").child(buyerid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", sellerid);
        hashMap.put("text", "Seller has declined your payment!");
        hashMap.put("productid", productid);
        hashMap.put("isproduct",true);

        reference.push().setValue(hashMap);
    }
}