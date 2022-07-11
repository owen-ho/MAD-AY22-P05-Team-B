package sg.edu.np.MulaSave.HomePage;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import sg.edu.np.MulaSave.R;
import sg.edu.np.MulaSave.User;

public class AddPostActivity extends AppCompatActivity {

    ImageView previewImage, closeButton;
    TextView chooseImage, postButton, postDesc;
    int code = 200;
    Post post;
    DatabaseReference databaseRefUser = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("user");
    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        //create user object
        User user = new User();
        databaseRefUser.child(usr.getUid().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot ds:task.getResult().getChildren()){
                        if (ds.getKey().equals("uid")){
                            user.setUid(ds.getValue().toString());
                        }
                        if(ds.getKey().equals("email")){
                            user.setEmail(ds.getValue().toString());
                        }
                        if(ds.getKey().equals("username")){
                            user.setUsername(ds.getValue().toString());

                        }
                    }
                }
            }
        });//end of setting users

        post = new Post();//create new post object with no fields

        previewImage = findViewById(R.id.previewImage);
        chooseImage = findViewById(R.id.chooseImage);
        closeButton = findViewById(R.id.closeButton);
        postButton = findViewById(R.id.postButton);
        postDesc = findViewById(R.id.postDesc);

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgChooser();
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                post.setCreator(user);//set the creator (user object)
                String randomId = UUID.randomUUID().toString();//create uuid to be used as the post uuid and also the name of the imagefile
                post.setPostUuid(randomId);
                post.setPostDesc(postDesc.getText().toString());

                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

                String formattedDateTime = LocalDateTime.now().format(myFormatObj);
                post.setPostDateTime(formattedDateTime);
                //store the image to storage
                storageRef.child("postpics/" + randomId + ".png").putFile(Uri.parse(post.getPostImageUrl()))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageRef.child("postpics/" + randomId + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        post.setPostImageUrl(uri.toString());//set the image url in post object
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                databaseRefUser.child(usr.getUid().toString()).child("posts").child(randomId).setValue(post);//set the post into the user under posts
                //show success and exit
                Toast.makeText(AddPostActivity.this,"Upload Success!",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {//close the activity and discard
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }//end of oncreate

    private void imgChooser() {//choose image method
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Image"), code);
    }

    //get image and load to preview
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//if result is ok
            if (requestCode == code) {//ensure request code is same
                //get url of image
                Uri selectedImgUri = data.getData();
                if (null != selectedImgUri) {
                    post.setPostImageUrl(selectedImgUri.toString());//set the post object image url
                    // update the preview image in the layout
                    previewImage.setImageURI(selectedImgUri);
                }
            }
        }
    }//end of on activity result
}