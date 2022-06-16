package sg.edu.np.P05TeamB;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ShoppingRecyclerAdapter extends RecyclerView.Adapter<ShoppingViewHolder> {
    private ArrayList<Product> data;
    LayoutInflater inflater;

    public ShoppingRecyclerAdapter(ArrayList<Product> input, Context  context) {
        this.data = input;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.shopping_view_holder;
    }//

    @Override
    public ShoppingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        View view = inflater.inflate(R.layout.shopping_view_holder,parent,false);
        return new ShoppingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShoppingViewHolder holder, int position) {
        Product p = data.get(position);

        holder.productTitle.setText(p.getTitle());
        String price = String.format("$%.2f",p.getPrice());
        holder.productPrice.setText(price);
        holder.prodRating.setRating(p.getRating());

        //HARD CODED
        holder.productWebsite.setText("Amazon.sg");

        Picasso.get()
                .load(p.getImageUrl())
                .into(holder.productImage);

        holder.productListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.productListing.getContext());
                builder.setTitle("Product Details");
                builder.setMessage("Title: "+p.getTitle()+'\n'+'\n'+"Category: "+p.getCategory()+'\n'+'\n'+String.format("Price: $%.2f",p.getPrice()));
                builder.setCancelable(false);
                builder.setPositiveButton("Open", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        //Opens store page for item
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(p.getLink()));
                        holder.productListing.getContext().startActivity(browserIntent);
                    }
                });
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){

                    }
                });
                builder.show();
            }
        });

        holder.prodFavourite.setOnClickListener(new View.OnClickListener() {//on click listener for favourite button in searching of product
            @Override
            public void onClick(View view) {

                // showing favourite
                Drawable unwrappedDrawable = AppCompatResources.getDrawable(holder.prodFavourite.getContext(), R.drawable.favourate_button);
                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                DrawableCompat.setTint(wrappedDrawable, Color.RED);
            }
        });
    }//end of onBindViewHolder

    @Override
    public int getItemCount() {
        return data.size();
    }
}
