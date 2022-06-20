package sg.edu.np.MulaSave;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class wishlistFilterAdapter extends RecyclerView.Adapter<wishlistFilterAdapter.wishlistFilterViewHolder> {
    ArrayList<String> filters;
    Context context;

    public wishlistFilterAdapter(ArrayList<String> input){
        this.context = context;
        this.filters = input;
    }

    @NonNull
    @Override
    public wishlistFilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_filter,parent,false);
        return new wishlistFilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull wishlistFilterViewHolder holder, int position) {
        String s = filters.get(position);
        holder.filterText.setText(s);
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    public class wishlistFilterViewHolder extends RecyclerView.ViewHolder {
        TextView filterText;
        public wishlistFilterViewHolder(View itemView){
            super(itemView);
            filterText = itemView.findViewById(R.id.filterText);
        }
    }
}
