package sg.edu.np.MulaSave;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class wishlistFilterAdapter extends RecyclerView.Adapter<wishlistFilterAdapter.wishlistFilterViewHolder> {
    ArrayList<String> filters = new ArrayList<>(Arrays.asList("Default" ,"Price [Low - High]","Price [High - Low]","Name [a - z]","Name [z - a]","Rating [Low - High]","Rating [High - Low]"));
    View view;
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
    //3 = shopping view

    public wishlistFilterAdapter(View _view, ShoppingRecyclerAdapter _wishlistAdapter, ArrayList<Product> _wProdList,int _wishOrCom){
        this.view = _view;
        this.wishlistAdapter = _wishlistAdapter;
        this.wProdList = _wProdList;
        this.wishOrCom = _wishOrCom;
    }


    @NonNull
    @Override
    public wishlistFilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_filter,parent,false);
        return new wishlistFilterViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull wishlistFilterViewHolder holder, int position) {
        if(wishOrCom==3){
            filters = new ArrayList<>(Arrays.asList("Price [Low - High]","Price [High - Low]","Name [a - z]","Name [z - a]","Rating [Low - High]","Rating [High - Low]"));//prevent
        }
        String s = filters.get(position);
        holder.filterText.setText(s);
        cardList.add(holder.filterCard);

        //determine path to access, since adapter is used by multiple fragments
        SearchView searchView;
        String path;
        if(wishOrCom == 1){
            path = "/user/" + usr.getUid().toString()+"/wishlist";//get the path to database
            searchView = view.findViewById(R.id.wishSearch);//get the corresponding searchview
        }
        else if(wishOrCom == 3){

            path = "shopping";
            searchView = view.findViewById(R.id.searchQuery);
        }
        else{
            path = "/product";
            searchView = view.findViewById(R.id.uploadSearch);
        }



        //check if any filters are active
        Boolean allGray = true;
        for(CardView cardView : cardList){
            if (cardView.getCardBackgroundColor().getDefaultColor()== Color.parseColor("#fdb915")){
                allGray = false;
            }
        }

        //if none of the filters are active, set the first filter ("Default") to be active
        if (allGray){
            if (s.equals(filters.get(0))){
                holder.filterCard.setCardBackgroundColor(Color.parseColor("#fdb915"));//set active
            }
        }


        holder.filterText.setOnClickListener(new View.OnClickListener() {//filter set on click
            @Override
            public void onClick(View view) {
                searchView.setIconified(true);//first time is to clear the inputs from user
                searchView.setIconified(true);//second time is to close the searchview
                //if there are no inputs, the first line will close the searchview but the second line will not crash

                if (path=="shopping"){
                    if (s.equals("Price [Low - High]")){//sorting price low to high
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
                    else if (s.equals("Rating [Low - High]")){//sort rating from low to high
                        Collections.sort(wProdList,productRatingLowHigh);
                    }
                    else if (s.equals("Rating [High - Low]")){//sort rating from high to low
                        Collections.sort(wProdList,productRatingHighLow);
                    }
                    //index = holder.getAdapterPosition();
                    for(CardView cardView : cardList){
                        cardView.setCardBackgroundColor(Color.parseColor("#D8D8D8"));//set inactive (gray)
                    }
                    holder.filterCard.setCardBackgroundColor(Color.parseColor("#fdb915"));//set active
                    wishlistAdapter.notifyDataSetChanged();
                }
                else{
                    database.getReference(path).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            wProdList.clear();//clear list
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
                            else if (s.equals("Rating [Low - High]")){//sort rating from low to high
                                Collections.sort(wProdList,productRatingLowHigh);
                            }
                            else if (s.equals("Rating [High - Low]")){//sort rating from high to low
                                Collections.sort(wProdList,productRatingHighLow);
                            }
                            //index = holder.getAdapterPosition();
                            for(CardView cardView : cardList){
                                cardView.setCardBackgroundColor(Color.parseColor("#D8D8D8"));//set inactive (gray)
                            }
                            holder.filterCard.setCardBackgroundColor(Color.parseColor("#fdb915"));//set active
                            wishlistAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.w("error", "loadPost:onCancelled", error.toException());
                        }
                    });
                }
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
        public wishlistFilterViewHolder(View itemView, int viewType){
            super(itemView);
            filterCard = itemView.findViewById(R.id.filterCard);
            filterText = itemView.findViewById(R.id.filterText);
        }
    }//end of wishlistFilterViewHolder

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

    //custom comparator for low to high product rating
    public Comparator<Product> productRatingLowHigh = new Comparator<Product>() {
        @Override
        public int compare(Product p1, Product p2) {
            return Float.compare(p1.getRating(),p2.getRating());
        }
    };

    //custom comparator for high to low product rating
    public Comparator<Product> productRatingHighLow = new Comparator<Product>() {
        @Override
        public int compare(Product p1, Product p2) {
            return Float.compare(p2.getRating(),p1.getRating());
        }
    };
}
