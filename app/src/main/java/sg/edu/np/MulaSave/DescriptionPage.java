package sg.edu.np.MulaSave;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import sg.edu.np.MulaSave.chat.Chat;

public class DescriptionPage extends AppCompatActivity {
    DatabaseReference databaseRefUser = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("user");
    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();

    DatabaseReference databaseRefProduct = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("product");


    Product product;

    /**
     * This class would:
     * 1. Show the description and details of the product
     * 2. Allow the user to chat with the seller with a chat button
     * 3. Allow the user to reserve a product with a reserve product button
     * 4. Allow the seller to mark their product as unreserved and to allow the product to be reserved again.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialising the variables and Views from the xml
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_descriptionpage);
        TextView title = findViewById(R.id.Title);
        TextView price = findViewById(R.id.Price);
        TextView description =findViewById(R.id.Description);
        TextView condition =findViewById(R.id.Condition);
        TextView meetup = findViewById(R.id.Meetup);
        TextView username = findViewById(R.id.Sellerusername);

        ImageView pic = findViewById(R.id.imageView16);
        ImageView backdescriptionpage = findViewById(R.id.BackDescriptionPage);
        Button chat = findViewById(R.id.Chat);
        Button reserve = findViewById(R.id.Reserve);
        Button removeReserve = findViewById(R.id.removeReserve);


        product = (Product) getIntent().getSerializableExtra("product");//get product from adapter
        title.setText(product.getTitle());
        price.setText("$" +String.valueOf(product.getPrice()));
        description.setText(product.getDesc());
        condition.setText(product.getCondition());
        meetup.setText(product.getMeetup());
        username.setText(product.getWebsite());
        Picasso.get().load(product.getImageUrl()).into(pic);

        // Let the user click back to the previous page using the back button located in this description page
        backdescriptionpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // When the user clicks the chat button it would intent to the chat activity
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DescriptionPage.this, Chat.class);
                i.putExtra("sellerid",product.getSellerUid());
                startActivity(i);
            }
        });

        //If the current user owns the product being shown
        if (product.getSellerUid().equals(usr.getUid())){
            reserve.setVisibility(View.GONE); //To hide the reserve button so the owner of the product cannot reserve their own product
            chat.setVisibility(View.INVISIBLE); //To hide the Chat button so the owner of the product cannot chat to themself
            removeReserve.setVisibility(View.INVISIBLE); //To hide the unreserve button by default

            //Check if current product(product) is being reserved by another user
            databaseRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean isReserved=false;
                    for(DataSnapshot ds: snapshot.getChildren()){ //Cycle through users in the firebase
                        for (DataSnapshot ds1: ds.child("Reserve").getChildren()){//Cycle through the firebase to look for the products inside the users reserve
                            Product prod = ds1.getValue(Product.class); //To convert the object in the firebase into a product
                            if (product.getImageUrl().equals(prod.getImageUrl())){ //If current product image url not equals to the product image url in any users reserve
                                isReserved=true;
                            }
                        }
                    }
                    if (isReserved) {//Unreserve button will not be shown
                        removeReserve.setVisibility(View.VISIBLE);
                    }else{//Unreserve button will be shown
                        removeReserve.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        //If current user does not own the product being shown
        if (!product.getSellerUid().equals(usr.getUid())){
            removeReserve.setVisibility(View.GONE); //To hide the unreserve button by default
            reserve.setVisibility(View.INVISIBLE);//To hide the reserve button by default
            // To disable buyers to reserve the product if the product is marked as sold and enable reserving of product if product is not sold
            databaseRefUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean isSold = false;
                    for(DataSnapshot ds: snapshot.getChildren()){//Cycle through users in firebase
                        for (DataSnapshot ds1: ds.child("Sold").getChildren()){//Cycle through the firebase to look for the products inside sold
                            Product prod = ds1.getValue(Product.class);//To convert the object in the firebase into a product
                            if (product.getImageUrl().equals(prod.getImageUrl())){ //If current product image url not equals to the product image url in sold
                                isSold=true;
                            }
                        }
                    }
                    if (isSold) {
                        reserve.setVisibility(View.GONE);
                    }else {
                        reserve.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        // To disable buyers to reserve the product if the product is currently being reserved by another user and enable reserving if product is available
        databaseRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){//Cycle through users in firebase
                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){//Cycle through attributes in users
                        if (snapshot1.getKey().toString().equals("Reserve")){// If attribute is reserved
                            for (DataSnapshot snapshot2 : snapshot1.getChildren()){//Cycle through reserved in the firebase
                                String ReserveUnique = ((product.getImageUrl()).replaceAll("[^a-zA-Z0-9]", ""));//To get the id of the product in reserved
                                if(snapshot2.getKey().toString().equals(ReserveUnique)){//If the product is being reserved
                                    reserve.setVisibility(View.INVISIBLE);//The reserve button would be set invisible
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //To add the product in the users/buyers reserved list in the firebase when they confirm on reserving the product
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DescriptionPage.this);
                View v = LayoutInflater.from(DescriptionPage.this).inflate(R.layout.reserve_dialog,null,false);
                builder.setView(v);
                final AlertDialog alertDialog = builder.create();
                TextView noReserve = v.findViewById(R.id.noReserve);
                TextView confirmResrve = v.findViewById(R.id.confirmReserve);
                //When the users/buyers click no button in the alertdialog
                noReserve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();// the alertdialog is dismissed and the product is not added to the users reserve list
                    }
                });

                //When the users/buyers click on the confirm button in the alertdialog
                confirmResrve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String ReserveUnique = ((product.getImageUrl()).replaceAll("[^a-zA-Z0-9]", ""));//To get the id of the current product
                        databaseRefUser.child(usr.getUid().toString()).child("Reserve").child(ReserveUnique).setValue(product);//add product if the product does not exist in the database
                        addReserveNotifications(usr.getUid(), product.getSellerUid(), product.getAsin());//adding of notification to the seller to inform that the product has been reserved by current user (buyer)
                        alertDialog.dismiss();
                    }
                });
                if (alertDialog.getWindow() != null){
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
                }
                alertDialog.show();
            }

        });

        // To allow the seller of the product to unreserve the product and mark their listing as available so another user can reserve
        removeReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){//Cycle through users in the firebase
                            for (DataSnapshot ds1: ds.child("Reserve").getChildren()){//Cycle through the firebase to look for products in reserve
                                Product p = ds1.getValue(Product.class);//To convert the object in the firebase into a product
                                if (p.getSellerUid().equals(usr.getUid())){//If the product's seller id matches the user id
                                    ds1.getRef().removeValue(); //The seller is able to remove the reserve in the firebase for their own product
                                }
                            }
                        }
                        removeReserve.setVisibility(View.GONE);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    /**
     * This is to save notification to the firebase:
     * 1. To notify the seller that a buyer has reserved their product
     * @param buyerid
     * @param sellerid
     * @param productid
     */
    private void addReserveNotifications(String buyerid, String sellerid, String productid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("notifications").child(sellerid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", buyerid);
        hashMap.put("text", "Reserved your product");
        hashMap.put("productid", productid);
        hashMap.put("isproduct",true);

        reference.push().setValue(hashMap);
    }
}