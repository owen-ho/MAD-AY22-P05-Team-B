package sg.edu.np.MulaSave.messages;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sg.edu.np.MulaSave.R;
import sg.edu.np.MulaSave.chat.chat;

public class messageadapter extends RecyclerView.Adapter<messageadapter.MyViewHolder> {
    private final List<messagelistiner> messagelistiners;
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
        if(list2.getProfilepic().isEmpty()){
            Picasso.get().load(list2.getProfilepic()).into(holder.Profilepic);

        }
        holder.name.setText(list2.getUsername());
        holder.lastmessage.setText(list2.getLastmessage());
        if(list2.getUnseenMessages()==0){
            holder.unseenmessage.setVisibility(View.GONE);
        }
        else{
            holder.unseenmessage.setVisibility(View.VISIBLE);
        }
        holder.rootlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, chat.class);
                intent.putExtra("name",list2.getUsername());
                intent.putExtra("Profilepic",list2.getProfilepic());
                context.startActivity(intent);
            }
        });
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
