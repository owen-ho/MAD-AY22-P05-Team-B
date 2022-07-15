package sg.edu.np.MulaSave.messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sg.edu.np.MulaSave.R;

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

    }

    @Override
    public int getItemCount() {
        return messagelistiners.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
