package sg.edu.np.P05TeamB;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ShoppingRecyclerAdapter extends RecyclerView.Adapter<ShoppingViewHolder> {
    private ArrayList<Product> data;

    LayoutInflater inflater;
    int layoutType; //toggle between search product view and shopping list view
    // 1 = shopping search product view
    // 2 = wishlist view

    public ShoppingRecyclerAdapter(ArrayList<Product> input, Context  context, int _layoutType) {
        this.data = input;
        this.inflater = LayoutInflater.from(context);
        this.layoutType = _layoutType;
    }

    @Override
    public int getItemViewType(final int position) {
        if (this.layoutType == 1){
            return 1;//shopping search product view
        }
        else{
            return 2;//wishlist view
        }
    }//

    @Override
    public ShoppingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == 1){//search product view
            view = inflater.inflate(R.layout.shopping_view_holder,parent,false);
        }
        else{//wishlist view
            view = inflater.inflate(R.layout.wishlist_row,parent,false);
        }
        return new ShoppingViewHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(ShoppingViewHolder holder, int position) {
        Product p = data.get(position);

        holder.productTitle.setText(p.getTitle());
        String price = String.format("$%.2f",p.getPrice());
        holder.productPrice.setText(price);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(holder.productListing);//close the constraint layout

        if(Math.signum(p.getRating()) == 0){//check if rating is 0 (rating is not available)
            holder.prodRating.setVisibility(View.GONE);//rating bar gone
        }
        else {//rating is available (set rating)
            holder.prodRating.setVisibility(View.VISIBLE);//rating bar visible
            holder.prodRating.setRating(p.getRating());
        }

        holder.productWebsite.setText(p.getWebsite());

        Picasso.get()
                .load(p.getImageUrl())
                .into(holder.productImage);

        holder.productListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.productListing.getContext());
                builder.setTitle("Product Details");
                //builder.setMessage("Title: "+p.getTitle()+'\n'+'\n'+"Category: "+p.getCategory()+'\n'+'\n'+String.format("Price: $%.2f",p.getPrice()));
                builder.setMessage("Title: "+p.getTitle()+'\n'+String.format("Price: $%.2f",p.getPrice()));
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
                DatabaseReference databaseRefUser = FirebaseDatabase
                        .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference("user");
                FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();

                databaseRefUser.child(usr.getUid().toString()).child("wishlist").child(p.getImageUrl().substring(p.getImageUrl().length()-15)).setValue(p);//add product

                /*
                databaseRefUser.child(usr.getUid().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                })*/

                // showing favourite
                holder.prodFavourite.setColorFilter(ContextCompat.getColor(holder.prodFavourite.getContext(), R.color.custom_red));//use custom red color
                /*
                Drawable unwrappedDrawable = AppCompatResources.getDrawable(holder.prodFavourite.getContext(), R.drawable.favourate_button);
                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                DrawableCompat.setTint(wrappedDrawable, Color.RED);*/

            }
        });
    }//end of onBindViewHolder

    @Override
    public int getItemCount() {
        return data.size();
    }

}
