package sg.edu.np.MulaSave;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.internal.InternalTokenProvider;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.stream.Stream;


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
                showProductDialog(holder.productListing.getContext(), p.getImageUrl(),p.getPrice(),p.getWebsite(),p.getLink());/*
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
                builder.show();*/
            }
        });

        /*String wishlistUnique = (p.getTitle()
                .concat(p.getImageUrl().substring(p.getImageUrl().length()-15))).replaceAll("[^a-zA-Z0-9]", "");//use title and last 15 characters of image as a unique key*/
                //to ensure ID of product is a valid firebase database path
        String wishlistUnique = (p.getTitle() + (p.getImageUrl().substring(p.getImageUrl().length()-15))+ p.getWebsite()).replaceAll("[^a-zA-Z0-9]", "");


        DatabaseReference databaseRefUser = FirebaseDatabase
                .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("user");
        FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();

        databaseRefUser.child(usr.getUid().toString()).child("wishlist").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(wishlistUnique)){
                    holder.prodFavourite.setColorFilter(ContextCompat.getColor(holder.prodFavourite.getContext(), R.color.custom_red));//use custom red color
                }
                else{
                    holder.prodFavourite.setColorFilter(ContextCompat.getColor(holder.prodFavourite.getContext(), R.color.custom_gray));//use custom gray color
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.prodFavourite.setOnClickListener(new View.OnClickListener() {//on click listener for favourite button in searching of product
            @Override
            public void onClick(View view) {
                databaseRefUser.child(usr.getUid().toString()).child("wishlist").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.getResult().hasChild(wishlistUnique)){
                            new AlertDialog.Builder(holder.itemView.getContext()).setTitle("Remove Item from Wishlist").setMessage((p.getTitle()).substring(0, Math.min(p.getTitle().length(), 50)) + " .....\n")
                                    .setPositiveButton("Remove", new DialogInterface.OnClickListener() {//confirm remove item from wishlist
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            databaseRefUser.child(usr.getUid().toString()).child("wishlist").child(wishlistUnique).removeValue();
                                            holder.prodFavourite.setColorFilter(ContextCompat.getColor(holder.prodFavourite.getContext(), R.color.custom_gray));//use custom gray color
                                            data.remove(holder.getAdapterPosition());
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {//dismiss
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    }).show();

                        }
                        else{
                            databaseRefUser.child(usr.getUid().toString()).child("wishlist").child(wishlistUnique).setValue(p);//add product if the product does not exist in the database
                            holder.prodFavourite.setColorFilter(ContextCompat.getColor(holder.prodFavourite.getContext(), R.color.custom_red));//use custom red color
                        }
                        notifyDataSetChanged();
                    }
                });

                // showing favourite
                holder.prodFavourite.setColorFilter(ContextCompat.getColor(holder.prodFavourite.getContext(), R.color.custom_red));//use custom red color
            }
        });
    }//end of onBindViewHolder

    @Override
    public int getItemCount() {
        return data.size();
    }

    //custom product dialog
    private void showProductDialog(Context context,String picUri, Double price, String website, String link) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.product_dialog,null,false);
        builder.setView(view);
        //load information
        ImageView pic = view.findViewById(R.id.dialogPic);
        Uri picU = Uri.parse(picUri);
        Picasso.get().load(picU).into(pic);
        ((TextView) view.findViewById(R.id.dialogPrice)).setText("$ " + String.format("$ %.2f",price));
        ((TextView) view.findViewById(R.id.dialogWebsite)).setText(website);

        final AlertDialog alertDialog = builder.create();
        //open browser
        view.findViewById(R.id.dialogOpen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                context.startActivity(browserIntent);
            }
        });

        //close browser
        view.findViewById(R.id.dialogClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();//close
            }
        });

        alertDialog.show();
    }
}


