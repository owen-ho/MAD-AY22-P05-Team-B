package sg.edu.np.MulaSave;


import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ShoppingViewHolder extends RecyclerView.ViewHolder {
//view holder shared by shopping, wishlist and uploads
    ConstraintLayout productListing;
    ImageView productImage;
    TextView productTitle;
    TextView productPrice;
    TextView productWebsite;
    RatingBar prodRating;
    ImageView prodFavourite;
    Boolean remove;//condition to tell adapter if remove item on unlike, to ensure adapter only removes the item when the wishlist fragment is using

    public ShoppingViewHolder(@NonNull View itemView, int viewType) {
        super(itemView);
        if (viewType == 1){//shopping search view, uploads view
            productListing = itemView.findViewById(R.id.sProdListing);
            productImage = itemView.findViewById(R.id.sProdImage);
            productTitle = itemView.findViewById(R.id.sProdTitle);
            productPrice = itemView.findViewById(R.id.sProdPrice);
            productWebsite = itemView.findViewById(R.id.sProdWebsite);
            prodRating = itemView.findViewById(R.id.sProdRating);
            prodFavourite = itemView.findViewById(R.id.sProdFavourite);
            remove = false;//do not remove item from recyclerview when user unlikes the item
        }
        else{//wishlistview
            productListing = itemView.findViewById(R.id.wishlistListing);
            productImage = itemView.findViewById(R.id.rImage);
            productTitle = itemView.findViewById(R.id.rTitle);
            productPrice = itemView.findViewById(R.id.rPrice);
            productWebsite = itemView.findViewById(R.id.rWebsite);
            prodRating = itemView.findViewById(R.id.wRatingBar);
            prodFavourite = itemView.findViewById(R.id.wFavourite);
            remove = true;//remove item from recyclerview when user unlikes the item
        }

    }
}