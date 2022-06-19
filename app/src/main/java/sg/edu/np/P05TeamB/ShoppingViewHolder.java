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
    TextView prodNoRating;
    RatingBar prodRating;
    ImageView prodFavourite;

    public ShoppingViewHolder(@NonNull View itemView, int viewType) {
        super(itemView);
        if (viewType == 1){//shopping search view
            productListing = itemView.findViewById(R.id.sProdListing);
            productImage = itemView.findViewById(R.id.sProdImage);
            productTitle = itemView.findViewById(R.id.sProdTitle);
            productPrice = itemView.findViewById(R.id.sProdPrice);
            productWebsite = itemView.findViewById(R.id.sProdWebsite);
            prodRating = itemView.findViewById(R.id.sProdRating);
            prodFavourite = itemView.findViewById(R.id.sProdFavourite);
        }
        else{//wishlistview
            productListing = itemView.findViewById(R.id.wishlistListing);
            productImage = itemView.findViewById(R.id.wImage);
            productTitle = itemView.findViewById(R.id.wTitle);
            productPrice = itemView.findViewById(R.id.wPrice);
            productWebsite = itemView.findViewById(R.id.wWebsite);
            prodRating = itemView.findViewById(R.id.wRatingBar);
            prodFavourite = itemView.findViewById(R.id.wFavourite);
        }

    }
}