package sg.edu.np.MulaSave.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sg.edu.np.MulaSave.R;

public class chatadapter extends RecyclerView.Adapter<chatadapter.MyViewHolder> {
    private final List<chatlistner> chatlistnerList;


    private final Context context;

    public chatadapter(List<chatlistner> chatlistnerList, Context context) {
        this.chatlistnerList = chatlistnerList;
        this.context = context;
    }

    @NonNull
    @Override
    public chatadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout,null));

    }

    @Override
    public void onBindViewHolder(@NonNull chatadapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return chatlistnerList.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
