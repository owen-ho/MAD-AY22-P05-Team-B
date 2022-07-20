package sg.edu.np.MulaSave;

import android.content.Context;
import android.net.Uri;
import android.widget.Adapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import sg.edu.np.MulaSave.FriendsFragments.ViewFriendAdapter;

public class User {
    public String uid;
    public String email;
    public String username;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id, String email,String username){
        this.uid = id;
        this.email = email;
        this.username = username;
    }

    public String getUid() {
        return this.uid;
    }
    public void setUid(String _uid){
        this.uid = _uid;
    }

    public String getEmail() {
        return this.email;
    }
    public void setEmail(String _email){
        this.email = _email;
    }

    public String getUsername() {
        return this.username;
    }
    public void setUsername(String _username){
        this.username = _username;
    }

    public void setImg(ViewFriendAdapter adapter,ImageView pic, Context context, int position){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child("profilepics/" + this.uid+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageView view = (ImageView) pic;
                if (view == null){
                    view = new ImageView(context);
                }
                Picasso.get().load(uri).fit().into(view);
                //adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ImageView view = (ImageView) pic;
                if (view == null){
                    view = new ImageView(context);
                }
                Picasso.get().load("https://www.business2community.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640.png").fit().into(view);
                //adapter.notifyDataSetChanged();
            }
        });
    }
}
