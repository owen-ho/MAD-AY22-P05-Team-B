package sg.edu.np.MulaSave.chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import sg.edu.np.MulaSave.MemoryData;
import sg.edu.np.MulaSave.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private List<ChatListener> chatlistnerList;
    private String getuid;
    private final Context context;

    /**
     * To create the chat adapter as well as passing in the list and context.
     * @param chatlistnerList
     * @param context
     */
    public ChatAdapter(List<ChatListener> chatlistnerList, Context context) {
        this.chatlistnerList = chatlistnerList;
        this.context = context;
        this.getuid = MemoryData.getdata(context);
    }

    // Set the messages to have stable ids
    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    //Get accurate position
    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override

    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout,null));
    }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {
        //Setting up the messages and setting visibility based on the id
        ChatListener list2 = chatlistnerList.get(position);
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        getuid = mAuth.getCurrentUser().getUid();

        if(list2.getUid().equals(getuid)){

            holder.myLayout.setVisibility(View.VISIBLE);
            holder.oppoLayout.setVisibility(View.GONE);
            holder.myMessage.setText(list2.getMessage());
            holder.myTime.setText(list2.getDate()+" "+list2.getTime());



        }
        else{
            holder.myLayout.setVisibility(View.GONE);
            holder.oppoLayout.setVisibility(View.VISIBLE);
            holder.oppoMessage.setText(list2.getMessage());
            holder.oppoTime.setText(list2.getDate()+" "+list2.getTime());

        }


    }

    /**
     * Returning list size to generate the recycleview
     * @return
     */
    @Override
    public int getItemCount() {
        return chatlistnerList.size();
    }

    /**
     * Calling out chat
     * @param chatlistnerList full list of conversation to pass to the recycleview for later purposes
     */
    public void updatechatlist(List<ChatListener> chatlistnerList){
        this.chatlistnerList = chatlistnerList;
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {


        // Initialising the variables based on the XML ID.
        private LinearLayout oppoLayout,myLayout;
        private TextView oppoMessage, myMessage;
        private TextView oppoTime,myTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            oppoLayout = itemView.findViewById(R.id.oppolayout);
            myLayout = itemView.findViewById(R.id.mylayout);
            oppoMessage=itemView.findViewById(R.id.sendermessage);
            myMessage = itemView.findViewById(R.id.mymessage);
            oppoTime = itemView.findViewById(R.id.sendermessagetime);
            myTime = itemView.findViewById(R.id.mymessagetime) ;



        }
    }

}
