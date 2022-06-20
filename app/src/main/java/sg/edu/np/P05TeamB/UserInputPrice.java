package sg.edu.np.P05TeamB;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class UserInputPrice extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference productRef = database.getReference("product");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    int SELECT_PICTURE = 200;
    String key = productRef.push().getKey();
    String imagelink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input_price);

        EditText productTitle = findViewById(R.id.titleProduct);
        EditText productPrice = findViewById(R.id.priceProduct);
        EditText productWebsite = findViewById(R.id.websiteProduct);
        ImageView productPic = findViewById(R.id.addproductbutton);
        Button submitProductbtn = findViewById(R.id.submitProductButton);

        //ImageView upload = findViewById(R.id.addproductbutton);
        productPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImg();
            }
        });


        submitProductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!productTitle.getText().toString().equals("") && !productPrice.getText().toString().equals("") && !productWebsite.getText().toString().equals("")){
                    String pt = productTitle.getText().toString();
                    Double pp = Double.parseDouble(productPrice.getText().toString());
                    String pw = productWebsite.getText().toString();

                    if(pt.length()>=5){
                        productRef.child(key).child("title").setValue(pt);
                        productRef.child(key).child("price").setValue(pp);
                        productRef.child(key).child("website").setValue(pw);
                        storageRef.child("productpics/" + key + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                productRef.child(key).child("image").setValue(uri.toString());
                            }
                        }).addOnFailureListener(new OnFailureListener() {//file does not exist
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });

                        Intent i = new Intent(UserInputPrice.this,MainActivity.class);
                        i.putExtra("frgToLoad",0);
                        startActivity(i);
                    }else{
                        Toast.makeText(UserInputPrice.this,"Please be more descriptive in your title",Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(UserInputPrice.this,"Please fill in all boxes",Toast.LENGTH_SHORT).show();
                }
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
                Uri pfpUri = data.getData();
                if (null != pfpUri) {
                    //upload to firebase
                    Intent i = new Intent(this,SelectProfilePic.class);
                    i.putExtra("path",pfpUri.toString());
                    i.putExtra("type","product");
                    i.putExtra("key",key);
                    startActivity(i);
                }
            }
        }
    }//end of onActivityResult

}