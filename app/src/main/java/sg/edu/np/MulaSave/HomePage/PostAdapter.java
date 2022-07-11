package sg.edu.np.MulaSave.HomePage;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import sg.edu.np.MulaSave.R;
import sg.edu.np.MulaSave.User;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    ArrayList<Post> postList;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    public PostAdapter(ArrayList<Post> _postList) {
        Collections.reverse(_postList);
        this.postList = _postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;//find view and return the viewholder
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row,parent,false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        User creator = post.getCreator();
        holder.creatorUsername.setText(creator.getUsername());
        //set profile picture
        storageRef.child("profilepics/" + creator.getUid().toString() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {//user has set a profile picture before
                Picasso.get().load(uri).fit().into(holder.creatorImage);
            }
        }).addOnFailureListener(new OnFailureListener() {//file does not exist (user did not upload before)
            @Override
            public void onFailure(@NonNull Exception e) {//set default picture
                Picasso.get().load("https://www.business2community.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640.png").fit().into(holder.creatorImage);
            }
        });//end of get profile pic
        storageRef.child("postpics/" + post.getPostUuid() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.postImage);
            }
        });

        holder.postCaption.setText(post.getPostDesc());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView creatorImage, postImage;
        TextView creatorUsername, postCaption;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            creatorImage = itemView.findViewById(R.id.creatorImage);
            postImage = itemView.findViewById(R.id.postImage);
            creatorUsername = itemView.findViewById(R.id.creatorUsername);
            postCaption = itemView.findViewById(R.id.postCaption);
        }
    }
}
