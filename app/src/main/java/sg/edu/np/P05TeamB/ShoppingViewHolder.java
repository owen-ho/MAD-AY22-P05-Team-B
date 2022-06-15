package sg.edu.np.P05TeamB;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ShoppingViewHolder extends RecyclerView.ViewHolder {

    ConstraintLayout productListing;
    ImageView productImage;
    TextView productTitle;
    TextView productPrice;

    public ShoppingViewHolder(@NonNull View itemView) {
        super(itemView);
        productListing = itemView.findViewById(R.id.productlisting);//
        productImage = itemView.findViewById(R.id.sProdImage);
        productTitle = itemView.findViewById(R.id.sProdTitle);
        productPrice = itemView.findViewById(R.id.sProdPrice);

    }
}