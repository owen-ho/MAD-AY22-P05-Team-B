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

import sg.edu.np.MulaSave.Memorydata;
import sg.edu.np.MulaSave.R;

public class chatadapter extends RecyclerView.Adapter<chatadapter.MyViewHolder> {
    private List<chatlistener> chatlistnerList;
    private String getuid;


    private final Context context;

    public chatadapter(List<chatlistener> chatlistnerList, Context context) {
        this.chatlistnerList = chatlistnerList;
        this.context = context;
        this.getuid = Memorydata.getdata(context);
    }

    @NonNull
    @Override
    public chatadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout,null));

    }

    @Override
    public void onBindViewHolder(@NonNull chatadapter.MyViewHolder holder, int position) {
        chatlistener list2 = chatlistnerList.get(position);
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        getuid = mAuth.getCurrentUser().getUid();

        if(list2.getUid().equals(getuid)){
            Log.v("idk","idkkkk");
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.oppoLayout.setVisibility(View.GONE);
            holder.myMessage.setText(list2.getMessage());
            holder.myTime.setText(list2.getDate()+" "+list2.getTime());
            Log.v("data",String.valueOf(list2.getDate()));


        }
        else{
            Log.v("oppo", "ljlj");
            holder.myLayout.setVisibility(View.GONE);
            holder.oppoLayout.setVisibility(View.VISIBLE);
            holder.oppoMessage.setText(list2.getMessage());
            holder.oppoTime.setText(list2.getDate()+" "+list2.getTime());

        }


    }

    @Override
    public int getItemCount() {
        return chatlistnerList.size();
    }

    public void updatechatlist(List<chatlistener> chatlistnerList){
        this.chatlistnerList = chatlistnerList;
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout oppoLayout,myLayout;
        private TextView oppoMessage, myMessage;
        private TextView oppoTime,myTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            oppoLayout = itemView.findViewById(R.id.oppolayout);
            myLayout = itemView.findViewById(R.id.mylayout);
            oppoMessage=itemView.findViewById(R.id.oppomessage);
            myMessage = itemView.findViewById(R.id.mymessage);
            oppoTime = itemView.findViewById(R.id.oppomessagetime);
            myTime = itemView.findViewById(R.id.mymessagetime) ;



        }
    }

}
