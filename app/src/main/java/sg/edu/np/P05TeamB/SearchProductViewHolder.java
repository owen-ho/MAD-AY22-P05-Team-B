package sg.edu.np.P05TeamB;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SearchProductViewHolder extends RecyclerView.ViewHolder {
    TextView prodName;
    TextView prodPrice;
    RatingBar prodRating;
    TextView websiteName;
    public SearchProductViewHolder(View itemView){
        super(itemView);
        prodName = itemView.findViewById(R.id.sProdTitle1);
        prodPrice = itemView.findViewById(R.id.sProdPrice1);
        prodRating = itemView.findViewById(R.id.sProdRating1);
        websiteName = itemView.findViewById(R.id.sProdWebsite1);
    }
}
