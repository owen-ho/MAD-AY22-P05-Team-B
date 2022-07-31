package sg.edu.np.MulaSave.HomePage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

import sg.edu.np.MulaSave.FriendsFragments.ExploreFragment;
import sg.edu.np.MulaSave.FriendsFragments.FriendsFragment;
import sg.edu.np.MulaSave.Notification;
import sg.edu.np.MulaSave.R;
import sg.edu.np.MulaSave.User;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    ArrayList<Post> postList;
    FirebaseDatabase databaseRef = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference databaseRefUser = databaseRef.getReference("user");
    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    AnimatedVectorDrawableCompat avd;//aminations for the doubletap to like
    AnimatedVectorDrawable avd2;

    public PostAdapter(ArrayList<Post> _postList) {
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
        Post post = postList.get(holder.getAdapterPosition());
        holder.postImage.setVisibility(View.INVISIBLE);//set invisible
        holder.creatorImage.setVisibility(View.INVISIBLE);

        //onclick for the creator image in the post
        holder.creatorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!post.getCreatorUid().equals(usr.getUid())){//dont go to friend if the post is created by the user himself
                    goToFriend(holder.creatorImage.getContext(), post);//call function to go to user post
                }
            }
        });

        String creatorUid = post.getCreatorUid();
        databaseRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
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
                Log.i("PostAdapter", error.toString());
            }
        });
        storageRef.child("postpics/" + post.getPostUuid() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.postImage);
                holder.postImage.setVisibility(View.VISIBLE);//make it visible
            }
        });


        //get the difference in post created time and current timing
        Instant timeNow = Instant.now();
        Instant postDt = Instant.parse(post.getPostDateTime());
        String unit = "second";
        long show = postDt.until(timeNow, ChronoUnit.SECONDS);
        if(show >= 60){
            show = postDt.until(timeNow, ChronoUnit.MINUTES);
            unit = "minute";
            if (show >= 60){
                show = postDt.until(timeNow, ChronoUnit.HOURS);
                unit = "hour";
                if(show >= 24){
                    show = postDt.until(timeNow, ChronoUnit.DAYS);
                    unit = "day";
                    if(show >= 30){
                        show = postDt.until(timeNow, ChronoUnit.MONTHS);
                        unit = "month";
                        if(show >= 12){
                            show = postDt.until(timeNow, ChronoUnit.YEARS);
                            unit = "year";
                        }
                    }
                }
            }
        }
        String plural = "";
        if (show != 1){//if the displayed number is not 1, (e.g. 2 second, 5 min)
            plural = "s";//set a letter "s" at the back of the postDateTime
        }
        if(show<0){
            holder.postDateTime.setText("Just Now");
        }
        else{
            holder.postDateTime.setText(String.valueOf(show) + " " + unit + plural + " ago");
        }

        //set caption
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
                addNotifications(usr.getUid(),post.getCreatorUid(),post.getPostUuid());
                return true;
            }
        });

        //on touch listener for the double tap of the post image
        holder.postImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gDetector.onTouchEvent(motionEvent);
            }
        });


        //check if the post is liked, if liked, display as liked (red heart icon)
        databaseRefUser.child(usr.getUid().toString()).child("likedposts").addValueEventListener(new ValueEventListener() {//access users wishlist
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
                            addNotifications(usr.getUid(),post.getCreatorUid(),post.getPostUuid());
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    /**
     * The PostViewHolder class for the PostAdapter adapter object
     */
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
     * Add notifications to the database. If the the same person has liked the same post, do not add to database
     * The method is created for the notification of product posting, so the variable names may not be best suited
     * for post notifications.
     * @param currentUserId the current user, which is the user that liked the post
     * @param creatorId the user to send the notification to, which is the creator of the post
     * @param postId the unique id of the post to identify the post
     */
    private void addNotifications(String currentUserId, String creatorId, String postId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("notifications").child(creatorId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean duplicate = false;//set default for duplication to be false
                for (DataSnapshot ss : snapshot.getChildren()){
                    Notification notif = ss.getValue(Notification.class);
                    if (postId.equals(notif.getProductid()) && notif.getUserid().equals(usr.getUid())){//check for duplicate product ids as the post id that is passed in
                        duplicate = true;
                    }
                }
                if(!duplicate){//if there are duplicates on the same object
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("userid", currentUserId);
                    hashMap.put("text", "liked post");
                    hashMap.put("productid", postId);
                    hashMap.put("isproduct",false);

                    reference.push().setValue(hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("DatabaseError", String.valueOf(error));
            }
        });
    }

    /**
     * This function is used to go from a post object to the user in the friends activity, passing the name of the user into the searchview and filtering to show the target user
     * this function will start the FriendsActivity with a bundle
     * @param context the context of the place where the user called this method
     * @param post the post object that the user clicked on, so that the target tab and target user can be identified for the searchview
     */
    private void goToFriend(Context context, Post post){
        TextView dTitle,dNegativeText, dPositiveText;
        ImageView pic;
        CardView negativeCard, positiveCard, picCard;

        //build alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //get the layout object to be used as the alert dialog
        View view = LayoutInflater.from(context).inflate(R.layout.remove_wislist,null,false);//
        builder.setView(view);
        dTitle = view.findViewById(R.id.dTitle);
        dNegativeText = view.findViewById(R.id.dNegativeText);
        dPositiveText = view.findViewById(R.id.dPositiveText);
        pic = view.findViewById(R.id.wishlistPic);
        negativeCard = view.findViewById(R.id.dNegativeCard);
        positiveCard = view.findViewById(R.id.dPositiveCard);
        picCard = view.findViewById(R.id.picCard);

        dTitle.setText("View User");
        dNegativeText.setText("Back");
        dPositiveText.setText("Go");

        final AlertDialog alertDialog = builder.create();

        //back (dismiss dialog)
        negativeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        picCard.setRadius(200f);//set the card to be circular

        //go to friend
        positiveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();//create a new bundle
                bundle.putString("targetUserUid",post.getCreatorUid());//set the target user with the uid
                databaseRefUser.child(usr.getUid()).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(post.getCreatorUid())){//if the user is a firend
                            bundle.putInt("targetTab",0);//set to first tab in FriendsActivity, which is the friends view
                        }
                        else{
                            bundle.putInt("targetTab",2);//set to third tab in FriendsActivity, which is the explore view since the post creator is not friend
                        }
                        Intent i = new Intent(context, FriendsActivity.class);
                        i.putExtras(bundle);
                        context.startActivity(i);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i("LikedPostActivity", error.toString());
                    }
                });
                alertDialog.dismiss();
            }
        });
        //get the user profile picture to show on the dialog
        storageRef.child("profilepics/" + post.getCreatorUid() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {//user has set a profile picture before
                Picasso.get().load(uri).resize(200,200).into(pic);
            }
        }).addOnFailureListener(new OnFailureListener() {//file does not exist (user did not upload before)
            @Override
            public void onFailure(@NonNull Exception e) {//set default picture
                Picasso.get().load("https://www.business2community.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640.png").resize(200,200).into(pic);
            }
        });//end of get profile pic

        //remove the layout backgound view
        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        }
        alertDialog.show();

    }
}
