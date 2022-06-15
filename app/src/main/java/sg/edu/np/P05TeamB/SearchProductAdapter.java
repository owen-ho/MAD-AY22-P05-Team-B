package sg.edu.np.P05TeamB;

import static java.lang.Math.round;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductViewHolder> {
    ArrayList<Product> prodList;
    LayoutInflater inflater;

    public SearchProductAdapter(ArrayList<Product> prodList, Context context){
        this.prodList = prodList;
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
        Product p = prodList.get(position);
        /*
        holder.prodName.setText(p.getProductName());
        DecimalFormat df = new DecimalFormat("0.00");
        holder.prodPrice.setText("$" + df.format(p.getPrice()));
        holder.prodRating.setRating(p.getRating());
        holder.websiteName.setText(p.getWebsiteName());*/
    }

    @Override
    public int getItemCount() {
        return prodList.size();
    }
}
