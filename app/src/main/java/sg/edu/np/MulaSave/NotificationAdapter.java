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

    private Context mContext;
    private List<Notification> mNotification;

    public NotificationAdapter(Context mContext, List<Notification> mNotification) {
        this.mContext = mContext;
        this.mNotification = mNotification;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item, viewGroup, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Notification notification = mNotification.get(i);

        viewHolder.text.setText(notification.getText());

        getUserInfo(viewHolder.image_profile, viewHolder.username, notification.getUserid());

        if(notification.isIsproduct()){
            viewHolder.post_image.setVisibility(View.VISIBLE);
            getProductImage(viewHolder.post_image, viewHolder.productTitle, notification.getProductid());
        }else{
            getPostImage(viewHolder.post_image, viewHolder.productTitle, notification.getProductid());
        }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("product");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            Product p = ds.getValue(Product.class);
                            if(p.getAsin().equals(notification.getProductid())){
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
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_profile, post_image;
        public TextView username, text, productTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            post_image = itemView.findViewById(R.id.post_image);
            username = itemView.findViewById(R.id.username);
            text = itemView.findViewById(R.id.comment);
            productTitle = itemView.findViewById(R.id.notif_product);
        }
    }

    private void getUserInfo(ImageView imageView, TextView username, String publisherid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String current_username = ds.child("username").getValue().toString();
                    String current_uid = ds.child("uid").getValue().toString();
                    if(current_uid.equals(publisherid)){
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                        storageRef.child("profilepics/" + current_uid + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {//user has set a profile picture before
                                Picasso.get().load(uri).into(imageView);
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

    private void getProductImage(ImageView imageView, TextView textView, String productid){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("product");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Product product = ds.getValue(Product.class);
                    if(product.getAsin().equals(productid)){
                        Picasso.get().load(product.getImageUrl()).into(imageView);
                        textView.setText(product.getTitle());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
    private void getPostImage(ImageView imageView, TextView textView, String productid){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("post");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Post post = ds.getValue(Post.class);
                    if(post.getPostUuid().equals(productid)){
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                        storageRef.child("postpics/" + post.getPostUuid() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).into(imageView);
                                imageView.setVisibility(View.VISIBLE);//make it visible
                            }
                        });
                        textView.setText(post.getPostDesc());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}
