package sg.edu.np.MulaSave;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReserveAdapter extends RecyclerView.Adapter<ReserveAdapter.reserveViewHolder> {
    //adapter shared by shopping, wishlist and uploads
    private ArrayList<Product> data;

    public ReserveAdapter(ArrayList<Product> input){
        this.data = input;
    }

    @NonNull
    @Override
    public reserveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reserve_row,parent,false);
        return new reserveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull reserveViewHolder holder, int position) {
        Product product = data.get(position);
        holder.rTitle.setText(product.getTitle());
        String price = "0.0";
        if (product.getPrice()!=null){
            price = String.format("$%.2f",product.getPrice());
        }
        holder.rPrice.setText(price);
        holder.rWebsite.setText(product.getWebsite());

        Picasso.get()
                .load(product.getImageUrl())
                .into(holder.rImage);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class reserveViewHolder extends RecyclerView.ViewHolder {
        TextView rTitle,rPrice, rTime,rWebsite;
        ImageView rImage;
        public reserveViewHolder(@NonNull View itemView) {
            super(itemView);
            rTitle = itemView.findViewById(R.id.rTitle);
            rPrice = itemView.findViewById(R.id.rPrice);
            rTime = itemView.findViewById(R.id.rTime);
            rWebsite = itemView.findViewById(R.id.rWebsite);
            rImage = itemView.findViewById(R.id.rImage);
        }
    }
}
