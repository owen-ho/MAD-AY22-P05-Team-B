package sg.edu.np.MulaSave;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SelectProfilePic extends AppCompatActivity {

    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_profile_pic);
        ImageView preview = findViewById(R.id.previewPic);
        Button confirm = findViewById(R.id.cfmBtn);
        Button back = findViewById(R.id.bckBtn);

        Intent intent = getIntent();
        String uri = intent.getStringExtra("path");
        String type = intent.getStringExtra("type");
        String key = intent.getStringExtra("key");
        Uri pfpUri = Uri.parse(uri);
        preview.setImageURI(pfpUri);

        confirm.setOnClickListener(new View.OnClickListener() {//save image to firebase and go back to previous page
            @Override
            public void onClick(View view) {
                //upload to firebase
                if(type != null){
                    if(type.equals("product")) {
                        uploadProductPic(pfpUri, key);//Upload product images to a different path in firebase storage
                    }
                }
                else{
                    uploadPic(pfpUri);
                }
                //go back to page
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

    private void uploadPic(Uri uri){
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            String uid = user.getUid();
            StorageReference pfpRef = storageReference.child("profilepics/" + uid+".png");//use unique uid to link to image
            //no need to delete previous image as the new image will replace the old
            pfpRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(SelectProfilePic.this,"Upload Success! Refresh to see changes",Toast.LENGTH_SHORT).show();//to show at the bottom of the screen when pic uploaded
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SelectProfilePic.this,"Upload failed, try again later",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            //tell user to sign in
        }
    }//end of upload pic

    private void uploadProductPic(Uri uri, String key){
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        StorageReference pfpRef = storageReference.child("productpics/" + key+".png");//use unique uid to link to image
        //no need to delete previous image as the new image will replace the old
        pfpRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(SelectProfilePic.this,"Upload Success! Refresh to see changes",Toast.LENGTH_SHORT).show();//to show at the bottom of the screen when pic uploaded
//                        Intent i = new Intent(SelectProfilePic.this, UserInputPrice.class);
//                        i.putExtra("image",uri);
//                        startActivity(i);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SelectProfilePic.this,"Upload failed, try again later",Toast.LENGTH_SHORT).show();
                    }
                });
    }//end of upload pic
}