package sg.edu.np.MulaSave.messages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import sg.edu.np.MulaSave.R;
import sg.edu.np.MulaSave.chat.Chat;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    // Getting firebase reference
    private List<MessageListener> messageListeners;
    private final Context context;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference userRef = database.getReference("user");
    DatabaseReference chatRef = database.getReference("Chat");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();


    public MessageAdapter(List<MessageListener> messageListeners, Context context) {
        this.messageListeners = messageListeners;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_adapter_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {

        // Setting up the variables and
        String currentUser;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        MessageListener list2 = messageListeners.get(position);
        Picasso.get().load(Uri.parse(list2.getProfilepic())).into(holder.Profilepic);
        holder.Profilepic.setVisibility(View.VISIBLE);


        // Set text of last message as well as name
        chatRef.child(list2.getChatkey()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().child("user_1").getValue().equals(currentUser)){
                    String test;
                    test = String.valueOf(task.getResult().child("user_2").getValue());

                    userRef.child(test).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            holder.name.setText((String.valueOf(snapshot.child("username").getValue())));
                            holder.lastmessage.setText(list2.getLastmessage());
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    String test;
                    test = String.valueOf(task.getResult().child("user_1").getValue());
                    userRef.child(test).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            holder.name.setText((String.valueOf(task.getResult().child("username").getValue())));
                            holder.lastmessage.setText(list2.getLastmessage());

                        }
                    });
                };
                }
            });

        // Setting the unseenmessages
        if(list2.getUnseenMessages()==0){
            holder.unseenmessage.setVisibility(View.GONE);
            holder.lastmessage.setTextColor(Color.parseColor("#959595"));
        }
        else{
            holder.unseenmessage.setVisibility(View.VISIBLE);
            holder.unseenmessage.setText(list2.getUnseenMessages()+"");
            holder.lastmessage.setTextColor(context.getResources().getColor(R.color.theme_color_9));
        }

        holder.rootlayout.setOnClickListener(new View.OnClickListener() {

            // Passing data into chat class
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("sellerid",list2.getSellerid());
                intent.putExtra("uid",list2.getUid());
                intent.putExtra("name",list2.getUsername());
                intent.putExtra("Profilepic",list2.getProfilepic());
                intent.putExtra("chatkey",list2.getChatkey());
                intent.putExtra("messageListener",list2);
                context.startActivity(intent);
            }
        });
    }
    public void updatedata(List<MessageListener> messageListeners){
        this.messageListeners = messageListeners;

    }

    @Override
    public int getItemCount() {
        return messageListeners.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        // Identifing the variables from the XML
        private ImageView Profilepic;
        private TextView name;
        private TextView lastmessage;
        private TextView unseenmessage;
        private LinearLayout rootlayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Profilepic = itemView.findViewById(R.id.profilepic);
            name = itemView.findViewById(R.id.name);
            lastmessage = itemView.findViewById(R.id.lastmessage);
            unseenmessage = itemView.findViewById(R.id.unseenmessage);
            rootlayout = itemView.findViewById(R.id.rootlayout);

        }
    }
}
