package sg.edu.np.MulaSave;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import sg.edu.np.MulaSave.HomePage.Post;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    private Context mContext; //creation of context variable
    private List<Notification> mNotification; //creation of the list of notification variable to add notifications into the list

    public NotificationAdapter(Context mContext, List<Notification> mNotification) { // creation of constructor
        this.mContext = mContext;
        this.mNotification = mNotification;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item, viewGroup, false); //creation of view, so that notification can be viewed
        return new NotificationAdapter.ViewHolder(view); //return the NotificationAdapter ViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Notification notification = mNotification.get(i);

        viewHolder.text.setText(notification.getText());

        getUserInfo(viewHolder.image_profile, viewHolder.username, notification.getUserid());

        if(notification.isIsproduct()){ // if the notification is on a product that exist, the codes below will run
            viewHolder.post_image.setVisibility(View.VISIBLE); // in the notification, the image of the post is visible
            getProductImage(viewHolder.post_image, viewHolder.productTitle, notification.getProductid()); // get the product ID of the post
        }else{
            getPostImage(viewHolder.post_image, viewHolder.productTitle, notification.getProductid()); // if not get the product id
        }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("product"); // referenced to the product in our database so that the notification knows which product it is
                reference.addValueEventListener(new ValueEventListener() { // read from database
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            Product p = ds.getValue(Product.class);
                            if(p.getAsin().equals(notification.getProductid())){ // if p (stands for product) id has the same product
                                if (p.getLink().equals("link")){//products from community uploads have string link as the link var
                                    Intent i = new Intent(mContext, descriptionpage.class);
                                    i.putExtra("product",p);//pass product into desc
                                    mContext.startActivity(i);//start the product desc activity
                                }
//                                else{//start browser intent
//                                    Intent browserIntent = new Intent(mContext, WebActivity.class);
//                                    browserIntent.putExtra("url",p.getLink());
//                                    mContext.startActivity(browserIntent);
//                                }
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return mNotification.size();
    } // function for get item count

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_profile, post_image; // taking profile image and post image
        public TextView username, text, productTitle; // taking username, text and product title to add to the notifications

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile); // retrieving id from profile
            post_image = itemView.findViewById(R.id.post_image); // retrieving image post
            username = itemView.findViewById(R.id.username); // retrieving username
            text = itemView.findViewById(R.id.comment);
            productTitle = itemView.findViewById(R.id.notif_product); // retrieving product title
        }
    }

    private void getUserInfo(ImageView imageView, TextView username, String publisherid){ // function to get product information
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user"); // back to database to retrieve user information
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String current_username = ds.child("username").getValue().toString(); // getting username of current user
                    String current_uid = ds.child("uid").getValue().toString(); // getting id of current user
                    if(current_uid.equals(publisherid)){
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                        storageRef.child("profilepics/" + current_uid + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() { //reference to database
                            @Override
                            public void onSuccess(Uri uri) {//user has set a profile picture before
                                Picasso.get().load(uri).into(imageView); //finding profile picture of user
                            }
                        }).addOnFailureListener(new OnFailureListener() {//file does not exist (user did not upload before)
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                        username.setText(current_username);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getProductImage(ImageView imageView, TextView textView, String productid){ // retrieving image of the product
        DatabaseReference reference = FirebaseDatabase.getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("product"); //referencing from database
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Product product = ds.getValue(Product.class); // retrieving product from database then retrieving information of specific product object
                    if(product.getAsin().equals(productid)){ // happens if product id is the same id as the product from the class object
                        Picasso.get().load(product.getImageUrl()).into(imageView); // getting image of product
                        textView.setText(product.getTitle());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
    private void getPostImage(ImageView imageView, TextView textView, String productid){ // function to get image of the post
        DatabaseReference reference = FirebaseDatabase.getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("post"); // retrieving post information from database
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Post post = ds.getValue(Post.class); // getting post from the object class
                    if(post.getPostUuid().equals(productid)){
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference(); // database referencing
                        storageRef.child("postpics/" + post.getPostUuid() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() { // get id of the post
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).into(imageView); // get image and put into image view
                                imageView.setVisibility(View.VISIBLE);//make it visible
                            }
                        });
                        textView.setText(post.getPostDesc()); // set the text to the post description so that it can be posted with the notifications
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}
