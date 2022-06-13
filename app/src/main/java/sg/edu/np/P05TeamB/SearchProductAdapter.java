package sg.edu.np.P05TeamB;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductViewHolder> {
    ArrayList<String> pNames;
    LayoutInflater inflater;

    public SearchProductAdapter(ArrayList<String> pNames, Context context){
        this.pNames = pNames;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SearchProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = inflater.inflate(R.layout.product_row,parent,false);
        return new SearchProductViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchProductViewHolder holder, int position) {
        holder.prodName.setText(pNames.get(position));

        //rating (need change)
        holder.prodRating.setRating(4.5f);
    }

    @Override
    public int getItemCount() {
        return pNames.size();
    }
}
