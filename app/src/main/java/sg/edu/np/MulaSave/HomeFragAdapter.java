package sg.edu.np.MulaSave;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeFragAdapter extends RecyclerView.Adapter<HomeFragAdapter.HomeFragViewHolder> {

    public HomeFragAdapter(ArrayList<Product> products){

    }

    @NonNull
    @Override
    public HomeFragViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFragViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
