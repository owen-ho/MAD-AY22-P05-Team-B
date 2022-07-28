package sg.edu.np.MulaSave;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ReserveAdapter extends RecyclerView.Adapter<ReserveAdapter.reserveViewHolder> {
    //adapter shared by shopping, wishlist and uploads
    private ArrayList<Product> data;

    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    DatabaseReference databaseRefUser = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("user");
    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();



    public ReserveAdapter(ArrayList<Product> input){
        this.data = input;
    }

    @NonNull
    @Override
    public reserveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reserve_row,parent,false);
        return new reserveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull reserveViewHolder holder, int position) {
        Product product = data.get(position);
        holder.rTitle.setText(product.getTitle());
        String price = "0.0";
        if (product.getPrice() != null) {
            price = String.format("$%.2f", product.getPrice());
        }
        holder.rPrice.setText(price);
        holder.rWebsite.setText(product.getWebsite());

        Picasso.get()
                .load(product.getImageUrl())
                .into(holder.rImage);

        holder.UnreserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.UnreserveBtn.getContext());
                View v = LayoutInflater.from(holder.UnreserveBtn.getContext()).inflate(R.layout.unreserve_dialog,null,false);
                builder.setView(v);
                final AlertDialog alertDialog = builder.create();
                TextView noUnReserve = v.findViewById(R.id.noRemoveUpload);
                TextView confirmUnReserve = v.findViewById(R.id.confirmRemoveUpload);
                noUnReserve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                confirmUnReserve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String ReserveUnique = ((product.getImageUrl()).replaceAll("[^a-zA-Z0-9]", ""));
                        databaseRefUser.child(usr.getUid().toString()).child("Reserve").child(ReserveUnique).removeValue();
                        data.clear();
                        ReserveAdapter.this.notifyDataSetChanged();
                        addUnreserveNotifications(usr.getUid(), product.getSellerUid(), product.getAsin());
                        alertDialog.dismiss();
                    }
                });
                if (alertDialog.getWindow() != null){
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
                }
                alertDialog.show();
            }
        });

        holder.uploadpaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.uploadpaymentBtn.getContext(), UploadPayment.class);
                intent.putExtra("product",product);
                holder.uploadpaymentBtn.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class reserveViewHolder extends RecyclerView.ViewHolder {
        TextView rTitle,rPrice,rWebsite;
        ImageView rImage, UnreserveBtn, uploadpaymentBtn;
        public reserveViewHolder(@NonNull View itemView) {
            super(itemView);
            rTitle = itemView.findViewById(R.id.rTitle);
            rPrice = itemView.findViewById(R.id.rPrice);
            rWebsite = itemView.findViewById(R.id.rWebsite);
            rImage = itemView.findViewById(R.id.rImage);
            UnreserveBtn = itemView.findViewById(R.id.UnreserveBtn);
            uploadpaymentBtn = itemView.findViewById(R.id.uploadPayment);
        }
    }

    private void addUnreserveNotifications(String buyerid, String sellerid, String productid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("notifications").child(sellerid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", buyerid);
        hashMap.put("text", "Unreserved your product");
        hashMap.put("productid", productid);
        hashMap.put("isproduct",true);

        reference.push().setValue(hashMap);
    }
}
