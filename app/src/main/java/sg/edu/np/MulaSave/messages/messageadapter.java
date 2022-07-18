package sg.edu.np.MulaSave.messages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.collection.LLRBNode;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sg.edu.np.MulaSave.R;
import sg.edu.np.MulaSave.chat.chat;

public class messageadapter extends RecyclerView.Adapter<messageadapter.MyViewHolder> {
    private List<messagelistiner> messagelistiners;
    private final Context context;

    public messageadapter(List<messagelistiner> messagelistiners, Context context) {
        this.messagelistiners = messagelistiners;
        this.context = context;
    }

    @NonNull
    @Override
    public messageadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_adapter_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull messageadapter.MyViewHolder holder, int position) {
        messagelistiner list2 = messagelistiners.get(position);
        Log.v("trying",list2.getSellerid());
        if(list2.getProfilepic().isEmpty()){
            //Picasso.get().load(list2.getProfilepic()).into(holder.Profilepic);

        }

        holder.name.setText("hi");
        holder.lastmessage.setText(list2.getLastmessage());
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
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, chat.class);
                intent.putExtra("sellerid",list2.getSellerid());
                intent.putExtra("uid",list2.getUid());
                intent.putExtra("name",list2.getUsername());
                intent.putExtra("Profilepic",list2.getProfilepic());
                intent.putExtra("chatkey",list2.getChatkey());
                context.startActivity(intent);
            }
        });
    }
    public void updatedata(List<messagelistiner> messagelistiners){
        this.messagelistiners = messagelistiners;
        notifyDataSetChanged();


    }

    @Override
    public int getItemCount() {
        return messagelistiners.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView Profilepic;
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
