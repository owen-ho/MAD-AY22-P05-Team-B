package sg.edu.np.MulaSave.HomePage;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import sg.edu.np.MulaSave.Product;
import sg.edu.np.MulaSave.R;
import sg.edu.np.MulaSave.ShoppingRecyclerAdapter;
import sg.edu.np.MulaSave.User;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    ArrayList<Post> postList;
    DatabaseReference databaseRefUser = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("user");
    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    public PostAdapter(ArrayList<Post> _postList) {
        //Collections.sort(_postList,postComparator);
        this.postList = _postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;//find view and return the viewholder
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row,parent,false);
        return new PostViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        holder.postDateTime.setText(post.getPostDateTime());
        holder.postCaption.setText(post.getPostDesc());

        //check if the post is liked, if liked, display as liked (red heart icon)
        databaseRefUser.child(usr.getUid().toString()).child("likedposts").addListenerForSingleValueEvent(new ValueEventListener() {//access users wishlist
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(post.getPostUuid())){
                    holder.postLike.setColorFilter(ContextCompat.getColor(holder.postLike.getContext(), R.color.custom_red));//use custom red color if product is in wishlist
                }
                else{
                    holder.postLike.setColorFilter(ContextCompat.getColor(holder.postLike.getContext(), R.color.custom_gray));//use custom gray color if product is not in wishlist
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("DatabaseError", String.valueOf(error));
            }
        });

        //on click listener for the like button
        holder.postLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add to liked list
                databaseRefUser.child(usr.getUid().toString()).child("likedposts").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.getResult().hasChild(post.getPostUuid())){
                            databaseRefUser.child(usr.getUid().toString()).child("likedposts").child(post.getPostUuid().toString()).removeValue();
                            holder.postLike.setColorFilter(ContextCompat.getColor(holder.postLike.getContext(), R.color.custom_gray));//use custom gray color
                        }
                        else{
                            databaseRefUser.child(usr.getUid().toString()).child("likedposts").child(post.getPostUuid()).setValue(post);
                            holder.postLike.setColorFilter(ContextCompat.getColor(holder.postLike.getContext(), R.color.custom_red));//use custom red color
                        }
                        PostAdapter.this.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView creatorImage, postImage, postLike;
        TextView creatorUsername, postCaption, postDateTime;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            creatorImage = itemView.findViewById(R.id.creatorImage);
            postImage = itemView.findViewById(R.id.postImage);
            creatorUsername = itemView.findViewById(R.id.creatorUsername);
            postCaption = itemView.findViewById(R.id.postCaption);
            postDateTime = itemView.findViewById(R.id.postDateTime);
            postLike = itemView.findViewById(R.id.postLike);
        }
    }

    //custom comparator for sorting the posts
    /*public Comparator<Post> postComparator = new Comparator<Post>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public int compare(Post p1, Post p2) {
            return p1.getPostDateTime().compareTo(p2.getPostDateTime());
        }
    };*/
}
