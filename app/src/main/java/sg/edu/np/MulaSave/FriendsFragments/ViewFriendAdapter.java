package sg.edu.np.MulaSave.FriendsFragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import sg.edu.np.MulaSave.HomePage.FriendsActivity;
import sg.edu.np.MulaSave.R;
import sg.edu.np.MulaSave.User;

public class ViewFriendAdapter extends RecyclerView.Adapter<ViewFriendAdapter.FriendViewHolder>{

    ArrayList<User> userList;
    DatabaseReference databaseRefUser = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("user");
    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    int viewType;//choose the layout for requests or explore
    //1 means requests
    //2 means explore layout

    public ViewFriendAdapter(ArrayList<User> _userList, int _viewType){
        this.userList = _userList;
        this.viewType = _viewType;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public int getItemViewType(int position)
    {
        if(this.viewType == 1){
            return 1;//friends page - view user's friends
        }
        else if (this.viewType == 2){
            return  2;//requests page
        }
        else{
            return 3;//explore page
        }
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;//find view and return the viewholder
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_view_row,parent,false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, @SuppressLint("RecyclerView") int position) {
        User u = userList.get(position);

        u.setImg(holder.userPic,holder.userPic.getContext());//set image in sync

        holder.userName.setText(u.getUsername());//get texts
        //holder.setIsRecyclable(false);
        holder.position = position;

        if(holder.getItemViewType()==1){//friends page
            holder.negativeCard.setVisibility(View.GONE);
            holder.positiveText.setText("Friends");
            holder.positiveCard.setOnClickListener(new View.OnClickListener() {//alertdialog to remove friend
                @Override
                public void onClick(View view) {
                    removeFriendDialog(holder.itemView.getContext(),u,holder.getAdapterPosition());//set context and the friend (User object)
                }
            });
        }//end of friends on bind methods

        else if (holder.getItemViewType()==2){//2 which is requests page

            //AddFriends.refreshPage();
            holder.positiveText.setText("Accept");
            holder.negativeText.setText("Cancel");

            //accept button
            holder.positiveCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseRefUser.child(usr.getUid()).child("requests").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.getResult().exists()){
                                Log.i("knnn",task.getResult().toString() + "cnnnn");
                                if(task.getResult().hasChild(u.getUid())){
                                    databaseRefUser.child(usr.getUid().toString()).child("friends").child(u.getUid()).setValue(u.getUid());//add the new friend under the current users friend list
                                    databaseRefUser.child(u.getUid().toString()).child("friends").child(usr.getUid()).setValue(usr.getUid());
                                    databaseRefUser.child(usr.getUid()).child("requests").child(u.getUid()).removeValue();//remove the user from requests like
                                    userList.remove(u);
                                    ViewFriendAdapter.this.notifyItemRemoved(holder.getAdapterPosition());
                                }
                                else{
                                    Toast.makeText(holder.itemView.getContext(), u.getUsername() + " withdrew the request!",Toast.LENGTH_SHORT).show();
                                    FriendsActivity.refreshPage();
                                }
                            }
                            else{
                                Toast.makeText(holder.itemView.getContext(), u.getUsername() + " withdrew the request!",Toast.LENGTH_SHORT).show();
                                FriendsActivity.refreshPage();
                            }
                        }
                    });
                }
            });

            //decline friend request
            holder.negativeCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseRefUser.child(usr.getUid()).child("requests").child(u.getUid()).removeValue();
                    userList.remove(u);
                    ViewFriendAdapter.this.notifyDataSetChanged();
                }
            });
        }//end of onbind for requests view

        else{//explore view (3)
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
            holder.positiveCard.setCardBackgroundColor(Color.parseColor("#8BC34A"));//set to green
            holder.negativeCard.setVisibility(View.GONE);//set the visibility of cancel request to be gone first
            databaseRefUser.child(u.getUid().toString()).child("requests").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.getResult().hasChild(usr.getUid().toString())){
                        //change ui to show requested
                        holder.positiveText.setText("Requested");
                        holder.positiveCard.setCardBackgroundColor(Color.parseColor("#FF0288D1"));//set to blue
                        holder.negativeCard.setVisibility(View.VISIBLE);
                        ViewFriendAdapter.this.notifyItemChanged(position);
                    }
                }
            });//end of checking if the user has requested

            holder.positiveCard.setOnClickListener(new View.OnClickListener() {//request to add friend
                @Override
                public void onClick(View view) {
                    //u refers to the user in the explore list
                    //usr refers to the user currently logged in

                    databaseRefUser.child(usr.getUid()).child("requests").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(task.getResult().hasChild(u.getUid())){//add friend button is clicked when the other user has requested, so can straight away accept
                                databaseRefUser.child(usr.getUid()).child("friends").child(u.getUid()).setValue(u.getUid());//add the new friend under the current users friend list
                                databaseRefUser.child(u.getUid()).child("friends").child(usr.getUid()).setValue(usr.getUid());
                                databaseRefUser.child(usr.getUid()).child("requests").child(u.getUid()).removeValue();//remove the user from requests like
                                userList.remove(u);
                            }
                            else{
                                databaseRefUser.child(u.getUid().toString()).child("requests").child(usr.getUid().toString()).setValue("wants to add");//add the current user under the requests of the user in explore list
                            }
                        }
                    });
                    //u.setImg(holder.userPic,holder.userPic.getContext());
                    holder.positiveText.setText("Requested");
                    holder.positiveCard.setCardBackgroundColor(Color.parseColor("#FF0288D1"));//set to blue
                    holder.negativeCard.setVisibility(View.VISIBLE);
                    //ViewFriendAdapter.this.notifyItemChanged(position);
                }
            });

            holder.negativeCard.setOnClickListener(new View.OnClickListener() {//cancel friend request
                @Override
                public void onClick(View view) {

                    databaseRefUser.child(u.getUid().toString()).child("requests").child(usr.getUid().toString()).removeValue();

                    holder.positiveText.setText("Add Friend");
                    holder.positiveCard.setCardBackgroundColor(Color.parseColor("#8BC34A"));
                    holder.negativeCard.setVisibility(View.GONE);
                    //ViewFriendAdapter.this.notifyItemChanged(position);
                    ViewFriendAdapter.this.notifyDataSetChanged();
                    //userList.clear();
                    //notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    //viewholder
    public class FriendViewHolder extends RecyclerView.ViewHolder{
        int position;
        ImageView userPic;
        TextView userName, negativeText, positiveText;
        CardView negativeCard, positiveCard;
        public FriendViewHolder(View itemView){
            super(itemView);
            userPic = itemView.findViewById(R.id.userPic);
            userName = itemView.findViewById(R.id.userName);
            negativeText = itemView.findViewById(R.id.negativeText);
            positiveText = itemView.findViewById(R.id.positiveText);
            negativeCard = itemView.findViewById(R.id.negativeCard);
            positiveCard = itemView.findViewById(R.id.positiveCard);
        }
    }

    private void removeFriendDialog(Context context, User friend, int position){
        TextView dTitle,dNegativeText, dPositiveText;
        ImageView pic;
        CardView negativeCard, positiveCard;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.remove_wislist,null,false);
        builder.setView(view);

        dTitle = view.findViewById(R.id.dTitle);
        dNegativeText = view.findViewById(R.id.dNegativeText);
        dPositiveText = view.findViewById(R.id.dPositiveText);
        pic = view.findViewById(R.id.wishlistPic);
        negativeCard = view.findViewById(R.id.dNegativeCard);
        positiveCard = view.findViewById(R.id.dPositiveCard);

        dTitle.setText("Remove Friend");


        storageRef.child("profilepics/" + friend.getUid().toString() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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

        final AlertDialog alertDialog = builder.create();

        //confirm remove friend
        positiveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseRefUser.child(usr.getUid()).child("friends").child(friend.getUid()).removeValue();//remove from current user friend list
                databaseRefUser.child(friend.getUid()).child("friends").child(usr.getUid()).removeValue();//remove current user from the friend's friend list
                userList.remove(position);
                ViewFriendAdapter.this.notifyItemRemoved(position);
                //FriendsActivity.refreshPage();
                alertDialog.dismiss();
            }
        });

        //cancel removal
        negativeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        //remove the extra parts outside of the cardview
        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        }
        alertDialog.show();
    }

}
