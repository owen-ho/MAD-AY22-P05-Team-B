package sg.edu.np.P05TeamB;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ShoppingRecyclerAdapter extends RecyclerView.Adapter<ShoppingViewHolder> {
    private ArrayList<Product> data;

    public ShoppingRecyclerAdapter(ArrayList<Product> input) {
        data = input;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.shopping_view_holder;
    }

    @Override
    public ShoppingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ShoppingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShoppingViewHolder holder, int position) {
        Product p = data.get(position);
        holder.productTitle.setText(p.title);
        String price = String.format("$%.2f",p.price);
        holder.productPrice.setText(price);
        Picasso.get()
                .load(p.image)
                .into(holder.productImage);
        holder.productListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.productListing.getContext());
                builder.setTitle("Product Details");
                builder.setMessage("Title: "+p.title+'\n'+'\n'+"Category: "+p.category+'\n'+'\n'+String.format("Price: $%.2f",p.price));
                builder.setCancelable(false);
                builder.setPositiveButton("Open", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        //Opens store page for item
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(p.link));
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
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
