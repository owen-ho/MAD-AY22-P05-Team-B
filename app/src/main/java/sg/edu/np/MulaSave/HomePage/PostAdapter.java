package sg.edu.np.MulaSave.HomePage;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

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

import java.time.LocalDate;
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
    FirebaseDatabase databaseRef = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference databaseRefUser = databaseRef.getReference("user");
    DatabaseReference databaseRefPost = databaseRef.getReference("post");
    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    AnimatedVectorDrawableCompat avd;
    AnimatedVectorDrawable avd2;

    public PostAdapter(ArrayList<Post> _postList) {
        Collections.sort(_postList,postComparator);
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
        holder.postImage.setVisibility(View.INVISIBLE);//set invisible
        holder.creatorImage.setVisibility(View.INVISIBLE);

        String creatorUid = post.getCreatorUid();
        databaseRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ss : snapshot.getChildren()){
                    User creator = new User();
                    for (DataSnapshot ds : ss.getChildren()){//because the users may have wishlists and other additional fields, cannot extract directly to user class
                        if (ds.getKey().equals("uid")){
                            creator.setUid(ds.getValue().toString());
                        }
                        if(ds.getKey().equals("email")){
                            creator.setEmail(ds.getValue().toString());
                        }
                        if(ds.getKey().equals("username")){
                            creator.setUsername(ds.getValue().toString());
                        }
                    }
                    if (creator.getUid().equals(creatorUid)){//if it is the creator as taken from the post object
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
                        holder.creatorImage.setVisibility(View.VISIBLE);
                        break;//break the loop since the creator is found, no point looping
                    }
                }
            }//end of on data change

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        storageRef.child("postpics/" + post.getPostUuid() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.postImage);
                holder.postImage.setVisibility(View.VISIBLE);//make it visible
            }
        });

        holder.postDateTime.setText(post.getPostDateTime());
        holder.postCaption.setText(post.getPostDesc());

        final GestureDetector gDetector = new GestureDetector(holder.postImage.getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {//on double tap, like the post
                //animation for the heart
                final Drawable drawable = holder.postHeartAni.getDrawable();
                holder.postHeartAni.setAlpha(0.70f);
                if (drawable instanceof AnimatedVectorDrawableCompat){
                    avd = (AnimatedVectorDrawableCompat) drawable;
                    avd.start();
                }
                else if (drawable instanceof  AnimatedVectorDrawable){
                    avd2 = (AnimatedVectorDrawable) drawable;
                    avd2.start();
                }
                databaseRefUser.child(usr.getUid()).child("likedposts").child(post.getPostUuid()).setValue(post);
                holder.postLike.setColorFilter(ContextCompat.getColor(holder.postLike.getContext(), R.color.custom_red));//use custom red color
                return true;
            }
        });

        //on touch listener for the double tap
        holder.postImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gDetector.onTouchEvent(motionEvent);
            }
        });


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
                        if(task.getResult().hasChild(post.getPostUuid())){//if post is liked before
                            databaseRefUser.child(usr.getUid()).child("likedposts").child(post.getPostUuid().toString()).removeValue();
                            holder.postLike.setColorFilter(ContextCompat.getColor(holder.postLike.getContext(), R.color.custom_gray));//use custom gray color
                        }
                        else{//post not liked before: so like the post
                            databaseRefUser.child(usr.getUid()).child("likedposts").child(post.getPostUuid()).setValue(post);
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
        ImageView creatorImage, postImage, postLike,postHeartAni;
        TextView creatorUsername, postCaption, postDateTime;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            creatorImage = itemView.findViewById(R.id.creatorImage);
            postImage = itemView.findViewById(R.id.postImage);
            creatorUsername = itemView.findViewById(R.id.creatorUsername);
            postCaption = itemView.findViewById(R.id.postCaption);
            postDateTime = itemView.findViewById(R.id.postDateTime);
            postLike = itemView.findViewById(R.id.postLike);
            postHeartAni = itemView.findViewById(R.id.postHeartAni);
        }
    }

    /**
     * desc
     * Param
     * return
     */
    //custom comparator for sorting the posts
    public Comparator<Post> postComparator = new Comparator<Post>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public int compare(Post p1, Post p2) {
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            int l1 = LocalDateTime.parse(p1.getPostDateTime(),myFormatObj).compareTo(LocalDateTime.parse(p2.getPostDateTime(),myFormatObj));
            return l1;
        }
    };
}
