package sg.edu.np.P05TeamB;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SearchProductViewHolder extends RecyclerView.ViewHolder {
    TextView prodName;
    TextView prodPrice;
    RatingBar prodRating;
    public SearchProductViewHolder(View itemView){
        super(itemView);
        prodName = itemView.findViewById(R.id.sProdName);
        prodPrice = itemView.findViewById(R.id.sProdPrice);
        prodRating = itemView.findViewById(R.id.sProdRating);
    }
}