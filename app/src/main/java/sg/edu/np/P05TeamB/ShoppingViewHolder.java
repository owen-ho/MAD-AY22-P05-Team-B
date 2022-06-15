package sg.edu.np.P05TeamB;


import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ShoppingViewHolder extends RecyclerView.ViewHolder {

    ConstraintLayout productListing;
    ImageView productImage;
    TextView productTitle;
    TextView productPrice;
    TextView productWebsite;
    RatingBar prodRating;

    public ShoppingViewHolder(@NonNull View itemView) {
        super(itemView);
        productListing = itemView.findViewById(R.id.sProdListing);
        productImage = itemView.findViewById(R.id.sProdImage);
        productTitle = itemView.findViewById(R.id.sProdTitle);
        productPrice = itemView.findViewById(R.id.sProdPrice);
        productWebsite = itemView.findViewById(R.id.sProdWebsite);
        prodRating = itemView.findViewById(R.id.sProdRating);
    }
}