package sg.edu.np.MulaSave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.time.Instant;

public class descriptionpage extends AppCompatActivity {
    DatabaseReference databaseRefUser = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("user");
    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();

    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descriptionpage);
        TextView title = findViewById(R.id.Title);
        TextView price = findViewById(R.id.Price);
        TextView description =findViewById(R.id.Description);
        TextView condition =findViewById(R.id.Condition);
        TextView meetup = findViewById(R.id.Meetup);
        TextView username = findViewById(R.id.Sellerusername);

        ImageView pic = findViewById(R.id.imageView16);
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



        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if (product.getSellerUid().equals(usr.getUid())){
            reserve.setVisibility(View.GONE);
            chat.setVisibility(View.INVISIBLE);
            removeReserve.setVisibility(View.INVISIBLE);
            databaseRefUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds: snapshot.getChildren()){
                        for (DataSnapshot ds1: ds.child("Reserve").getChildren()){
                            Product prod = ds1.getValue(Product.class);
                            if (!product.getImageUrl().equals(prod.getImageUrl())){
                                removeReserve.setVisibility(View.GONE);
                            }
                            else{
                                removeReserve.setVisibility(View.VISIBLE);

                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if (!product.getSellerUid().equals(usr.getUid())){
            removeReserve.setVisibility(View.GONE);
            reserve.setVisibility(View.INVISIBLE);
            databaseRefUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds: snapshot.getChildren()){
                        for (DataSnapshot ds1: ds.child("Sold").getChildren()){
                            Product prod = ds1.getValue(Product.class);
                            if (!product.getImageUrl().equals(prod.getImageUrl())){ // Product in this page is not equals to the product in the firebase
                                reserve.setVisibility(View.VISIBLE);
                            }
                            else{
                                reserve.setVisibility(View.GONE);

                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        databaseRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                        if (snapshot1.getKey().toString().equals("Reserve")){
                            for (DataSnapshot snapshot2 : snapshot1.getChildren()){
                                String ReserveUnique = ((product.getImageUrl()).replaceAll("[^a-zA-Z0-9]", ""));
                                if(snapshot2.getKey().toString().equals(ReserveUnique)){
                                    reserve.setVisibility(View.INVISIBLE);
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

        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(descriptionpage.this);
                View v = LayoutInflater.from(descriptionpage.this).inflate(R.layout.reserve_dialog,null,false);
                builder.setView(v);
                final AlertDialog alertDialog = builder.create();
                TextView noReserve = v.findViewById(R.id.noReserve);
                TextView confirmResrve = v.findViewById(R.id.confirmReserve);
                noReserve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                confirmResrve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String ReserveUnique = ((product.getImageUrl()).replaceAll("[^a-zA-Z0-9]", ""));
                        databaseRefUser.child(usr.getUid().toString()).child("Reserve").child(ReserveUnique).setValue(product);//add product if the product does not exist in the database
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }

        });

        removeReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            for (DataSnapshot ds1: ds.child("Reserve").getChildren()){
                                Product p = ds1.getValue(Product.class);
                                if (p.getSellerUid().equals(usr.getUid())){
                                    ds1.getRef().removeValue();
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
}