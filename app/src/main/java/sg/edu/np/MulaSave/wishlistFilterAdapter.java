package sg.edu.np.MulaSave;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class wishlistFilterAdapter extends RecyclerView.Adapter<wishlistFilterAdapter.wishlistFilterViewHolder> {
    ArrayList<String> filters = new ArrayList<>(Arrays.asList("Default" ,"Price [Low - High]","Price [High - Low]","Name [a - z]","Name [z - a]"));
    Context context;
    ShoppingRecyclerAdapter wishlistAdapter;
    ArrayList<Product> wProdList;
    FirebaseDatabase database = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/");
            //.getReference("user");
    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<CardView> cardList = new ArrayList<CardView>();

    int wishOrCom;
    //1 = wishlist view
    //2 = uploads view

    public wishlistFilterAdapter( ShoppingRecyclerAdapter _wishlistAdapter, ArrayList<Product> _wProdList,int _wishOrCom){
        this.context = context;
        this.wishlistAdapter = _wishlistAdapter;
        this.wProdList = _wProdList;
        this.wishOrCom = _wishOrCom;
    }

    @Override
    public int getItemViewType(final int position) {
        if (this.wishOrCom == 1){
            return 1;//wishlist view
        }
        else{
            return 2;//community upload view
        }
    }//

    @NonNull
    @Override
    public wishlistFilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_filter,parent,false);
        return new wishlistFilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull wishlistFilterViewHolder holder, int position) {
        String s = filters.get(position);
        holder.filterText.setText(s);

        cardList.add(holder.filterCard);

        //determine path to access, since adapter is used by wishlist fragment and the community fragment
        String path;
        if(wishOrCom == 1){
            Log.i("knn", "onBindViewHolder: wishlist");
            path = "/user/" + usr.getUid().toString()+"/wishlist";
        }
        else{
            Log.i("knn", "onBindViewHolder: uploads");
            path = "/product";
        }


        //once user enters the view, it will be set to default
        if (s.equals("Default")){
            holder.filterCard.setCardBackgroundColor(Color.parseColor("#4CAF50"));//set active
        }

        holder.filterText.setOnClickListener(new View.OnClickListener() {//filter set on click
            @Override
            public void onClick(View view) {
                wProdList.clear();//clear list
                database.getReference(path).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ss : snapshot.getChildren()){
                            Product product = ss.getValue(Product.class);
                            wProdList.add(product);
                        }
                        if (s.equals("Default")){
                            //dont sort
                        }
                        else if (s.equals("Price [Low - High]")){//sorting price low to high
                            Collections.sort(wProdList,productPriceLowHigh);
                        }
                        else if (s.equals("Price [High - Low]")){//sorting price high to low
                            Collections.sort(wProdList,productPriceHighLow);
                        }
                        else if (s.equals("Name [a - z]")){//sorting title a to z
                            Collections.sort(wProdList,productNameAZComparator);
                        }
                        else if (s.equals("Name [z - a]")){//sorting title z to a
                            Collections.sort(wProdList,productNameZAComparator);
                        }
                        //index = holder.getAdapterPosition();
                        for(CardView cardView : cardList){
                            cardView.setCardBackgroundColor(Color.parseColor("#D8D8D8"));//set inactive (gray)
                        }
                        holder.filterCard.setCardBackgroundColor(Color.parseColor("#4CAF50"));//set active
                        wishlistAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("error", "loadPost:onCancelled", error.toException());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    public class wishlistFilterViewHolder extends RecyclerView.ViewHolder {
        TextView filterText;
        CardView filterCard;
        public wishlistFilterViewHolder(View itemView){
            super(itemView);
            filterCard = itemView.findViewById(R.id.filterCard);
            filterText = itemView.findViewById(R.id.filterText);
        }
    }

    //custom comparator for ascending order name
    public Comparator<Product> productNameAZComparator = new Comparator<Product>() {
        @Override
        public int compare(Product p1, Product p2) {
            return p1.getTitle().compareTo(p2.getTitle());
        }
    };

    //custom comparator for descending order name
    public Comparator<Product> productNameZAComparator = new Comparator<Product>() {
        @Override
        public int compare(Product p1, Product p2) {
            return p2.getTitle().compareTo(p1.getTitle());
        }
    };

    //custom comparator for low to high product price
    public Comparator<Product> productPriceLowHigh = new Comparator<Product>() {
        @Override
        public int compare(Product p1, Product p2) {
            return Double.compare(p1.getPrice(),p2.getPrice());
        }
    };

    //custom comparator for high to low product price
    public Comparator<Product> productPriceHighLow = new Comparator<Product>() {
        @Override
        public int compare(Product p1, Product p2) {
            return Double.compare(p2.getPrice(),p1.getPrice());
        }
    };
}
