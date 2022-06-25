package sg.edu.np.MulaSave;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeFragAdapter extends RecyclerView.Adapter<HomeFragAdapter.HomeFragViewHolder> {

    ArrayList<Product> homeProdList;
    public HomeFragAdapter(ArrayList<Product> products){
        this.homeProdList = products;
    }

    @NonNull
    @Override
    public HomeFragViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeFragViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.homefrag_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFragViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return homeProdList.size();
    }

    public class HomeFragViewHolder extends RecyclerView.ViewHolder{
        ImageView prodImg;
        TextView prodTitle, prodPrice;
        public HomeFragViewHolder(@NonNull View itemView) {
            super(itemView);
            prodImg = itemView.findViewById(R.id.homePic);
            prodTitle = itemView.findViewById(R.id.homeTitle);
            prodPrice = itemView.findViewById(R.id.homePrice);
        }
    }
}
