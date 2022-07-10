package sg.edu.np.MulaSave;

import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

import java.util.ArrayList;

public class ExploreFriendAdapter extends RecyclerView.Adapter<ExploreFriendAdapter.ExploreFriendViewHolder>{

    ArrayList<User> exploreList;
    DatabaseReference databaseRefUser = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("user");
    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    public ExploreFriendAdapter(ArrayList<User> _exploreList){
        this.exploreList = _exploreList;
    }
    @NonNull
    @Override
    public ExploreFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;//find view and return the viewholder
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_view_row,parent,false);
        return new ExploreFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreFriendViewHolder holder, int position) {
        User u = exploreList.get(position);
        holder.userName.setText(u.getUsername());//get texts

        holder.positiveText.setText("Add Friend");
        holder.negativeText.setText("Cancel");
        holder.negativeCard.setVisibility(View.GONE);//set the visibility of cancel request to be gone first

        databaseRefUser.child(u.getUid().toString()).child("requests").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().hasChild(usr.getUid().toString())){
                    //change ui to show requested
                    holder.positiveText.setText("Requested");
                    holder.positiveCard.setCardBackgroundColor(Color.parseColor("#FF0288D1"));
                    holder.negativeCard.setVisibility(View.VISIBLE);
                }
            }
        });//end of checking if the user has requested


        //set profile picture
        storageRef.child("profilepics/" + u.getUid().toString() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {//user has set a profile picture before
                Picasso.get().load(uri).fit().into(holder.userPic);
            }
        }).addOnFailureListener(new OnFailureListener() {//file does not exist (user did not upload before)
            @Override
            public void onFailure(@NonNull Exception e) {//set default picture
                //Picasso.get().load("https://www.business2community.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640.png").fit().into(holder.userPic);
            }
        });//end of get profile pic

        holder.positiveCard.setOnClickListener(new View.OnClickListener() {//request to add friend
            @Override
            public void onClick(View view) {
                //u refers to the user in the explore list
                //usr refers to the user currently logged in
                databaseRefUser.child(u.getUid().toString()).child("requests").child(usr.getUid().toString()).setValue("requested");//add the current user under the requests of the user in explore list

                //do not need to change the ui and visibility here because since the code onbind will run again after setting the value
            }
        });

        holder.negativeCard.setOnClickListener(new View.OnClickListener() {//cancel friend request
            @Override
            public void onClick(View view) {

                databaseRefUser.child(u.getUid().toString()).child("requests").child(usr.getUid().toString()).removeValue();

                holder.positiveText.setText("Add Friend");
                holder.positiveCard.setCardBackgroundColor(Color.parseColor("#8BC34A"));
                holder.negativeCard.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exploreList.size();
    }

    //viewholder
    public class ExploreFriendViewHolder extends RecyclerView.ViewHolder{
        ImageView userPic;
        TextView userName, negativeText, positiveText;
        CardView negativeCard, positiveCard;
        public ExploreFriendViewHolder(View itemView){
            super(itemView);
            userPic = itemView.findViewById(R.id.userPic);
            userName = itemView.findViewById(R.id.userName);
            negativeText = itemView.findViewById(R.id.negativeText);
            positiveText = itemView.findViewById(R.id.positiveText);
            negativeCard = itemView.findViewById(R.id.negativeCard);
            positiveCard = itemView.findViewById(R.id.positiveCard);
        }
    }
}
