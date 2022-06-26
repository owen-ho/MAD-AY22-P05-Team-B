package sg.edu.np.MulaSave;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class UserInputPrice extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference productRef = database.getReference("product");
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://mad-ay22-p05-team-b.appspot.com/");
    StorageReference storageRef = storage.getReference();
    int SELECT_PICTURE = 200;
    String key = productRef.push().getKey();//To get a unique key to identify products uploaded

    String imagelink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input_price);
        EditText productTitle = findViewById(R.id.titleProduct);
        EditText productPrice = findViewById(R.id.priceProduct);
        EditText productWebsite = findViewById(R.id.websiteProduct);
        EditText productRating = findViewById(R.id.productRating);
        ImageView productPic = findViewById(R.id.addproductbutton);
        Button submitProductbtn = findViewById(R.id.submitProductButton2);
        ImageView back = findViewById(R.id.backButtonInputPrice);
        ImageView refreshBtn = findViewById(R.id.refresh);

        //ImageView upload = findViewById(R.id.addproductbutton);
        productPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImg();
            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() { //Click to check if image is uploaded by loading it from firebase storage
            @Override
            public void onClick(View view) {
                storageRef.child("productpics/" + key + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(productPic);
                    }
                }).addOnFailureListener(new OnFailureListener() {//Happens when file does not exist
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserInputPrice.this, "You have not uploaded a photo yet!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submitProductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check for invalid input
                if (productTitle.getText().toString().equals("") || productPrice.getText().toString().equals("")
                        || productWebsite.getText().toString().equals("") || productRating.getText().toString().equals("")){
                    Toast.makeText(UserInputPrice.this,"Please fill in all boxes",Toast.LENGTH_SHORT).show();
                }
                //check for valid numeric\decimal inputs
                else if((!productRating.getText().toString().matches("\\d*\\.?\\d+")) || (!productPrice.getText().toString().matches("\\d*\\.?\\d+"))){
                    Toast.makeText(UserInputPrice.this,"Please enter valid fields",Toast.LENGTH_SHORT).show();
                }
                else{//primary validation completed
                    try{//use try to catch all other invalid inputs
                        String pt = productTitle.getText().toString();
                        Double pp = Double.parseDouble(productPrice.getText().toString());
                        String pw = productWebsite.getText().toString();
                        Double ratingD = Double.parseDouble(productRating.getText().toString());
                        Float rating = ratingD.floatValue();
                        if (rating > 5){//throw to exception if rating is less than 5, this is to ensure that app does not crash if unable to convert to float due to invalid inputs
                            throw new Exception();
                        }
                        storageRef.child("productpics/" + key + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                submitProductbtn.setEnabled(false);//Prevent uploading of repeated products
                                submitProductbtn.setClickable(false);
                                String productLink = "https://www.google.com/search?q=" + pw + "+" + pt;
                                Product p = new Product(UUID.randomUUID().toString(), pt,"category", pp, uri.toString(),productLink, rating, pw);
                                productRef.child(key).setValue(p);//add product obj to the realtime database

                                finish();//finish the upload activity
                                Toast.makeText(UserInputPrice.this,"Product uploaded",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {//file does not exist
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UserInputPrice.this,"Please ensure picture is uploaded",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(UserInputPrice.this,"Please enter valid inputs",Toast.LENGTH_SHORT).show();
                        Log.i("UserInputPrice", String.valueOf(e));
                    }
                }//end of if else
            }
        });
    }

    @Override
    public void onBackPressed() {
        storageRef.child("productpics/" + key + ".png").delete().addOnSuccessListener(new OnSuccessListener<Void>() { //Delete file if user uploaded but did not complete other inputs
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
        finish();//close activity
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
                Uri pfpUri = data.getData();
                if (null != pfpUri) {
                    //upload to firebase
                    Intent i = new Intent(this,SelectProfilePic.class);
                    i.putExtra("path",pfpUri.toString());
                    i.putExtra("type","product");
                    i.putExtra("key",key);//To identify the image when uploading
                    startActivity(i);
                }
            }
        }
    }//end of onActivityResult
}