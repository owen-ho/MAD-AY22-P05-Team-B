package sg.edu.np.MulaSave.FriendsFragments;

import android.graphics.Color;
import android.net.Uri;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import sg.edu.np.MulaSave.R;
import sg.edu.np.MulaSave.User;

public class ViewFriendAdapter extends RecyclerView.Adapter<ViewFriendAdapter.ExploreFriendViewHolder>{

    ArrayList<User> userList;
    DatabaseReference databaseRefUser = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("user");
    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    int reqOrExplore;//choose the layout for requests or explore
    //1 means requests
    //2 means explore layout

    public ViewFriendAdapter(ArrayList<User> _userList, int _reqOrExplore){
        this.userList = _userList;
        this.reqOrExplore = _reqOrExplore;
    }

    @Override
    public int getItemViewType(int position)
    {
        if(this.reqOrExplore == 1){
            return 1;//requests page
        }
        else if (this.reqOrExplore == 2){
            return  2;//explore page
        }
        else{
            return 3;//friends page - view user's friends
        }
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
        User u = userList.get(position);
        holder.userName.setText(u.getUsername());//get texts

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

        if(holder.getItemViewType()==1){//requests page
            holder.positiveText.setText("Accept");
            holder.negativeText.setText("Cancel");

            //accept button
            holder.positiveCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseRefUser.child(usr.getUid().toString()).child("friends").child(u.getUid()).setValue(u.getUid());//add the new friend under the current users friend list
                    databaseRefUser.child(u.getUid().toString()).child("friends").child(usr.getUid()).setValue(usr.getUid());
                    databaseRefUser.child(usr.getUid()).child("requests").child(u.getUid()).removeValue();//remove the user from requests like
                    /*userList.remove(u);
                    ViewFriendAdapter.this.notifyItemRemoved(holder.getAdapterPosition());*/
                    userList.clear();
                    ViewFriendAdapter.this.notifyDataSetChanged();
                }
            });

            //decline friend request
            holder.negativeCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseRefUser.child(usr.getUid()).child("requests").child(u.getUid()).removeValue();
                    userList.remove(u);
                    ViewFriendAdapter.this.notifyItemRemoved(holder.getAdapterPosition());
                }
            });

        }//end of requests on bind methods

        else if (holder.getItemViewType()==2){//2 which is explore page

            //remove all friends from explore
            databaseRefUser.child(usr.getUid()).child("friends").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.getResult().exists()){
                        if(task.getResult().hasChild(u.getUid())){
                            userList.remove(u);
                            ViewFriendAdapter.this.notifyDataSetChanged();
                        }
                    }
                }
            });

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

            holder.positiveCard.setOnClickListener(new View.OnClickListener() {//request to add friend
                @Override
                public void onClick(View view) {
                    //u refers to the user in the explore list
                    //usr refers to the user currently logged in
                    databaseRefUser.child(u.getUid().toString()).child("requests").child(usr.getUid().toString()).setValue("wants to add");//add the current user under the requests of the user in explore list
                    //ViewFriendAdapter.this.notifyItemChanged(holder.getAdapterPosition(),User.class);
                    //userList.clear();
                    notifyDataSetChanged();
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
                    //ViewFriendAdapter.this.notifyItemChanged(holder.getAdapterPosition(),User.class);
                    //userList.clear();
                    notifyDataSetChanged();
                }
            });
        }

        else{//friends view (3)

        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
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
