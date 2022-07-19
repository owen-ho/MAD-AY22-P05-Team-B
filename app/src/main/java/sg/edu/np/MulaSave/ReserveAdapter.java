package sg.edu.np.MulaSave;

import android.app.AlertDialog;
import android.os.CountDownTimer;
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
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ReserveAdapter extends RecyclerView.Adapter<ReserveAdapter.reserveViewHolder> {
    //adapter shared by shopping, wishlist and uploads
    private ArrayList<Product> data;

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

        new CountDownTimer(86400000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                holder.rTime.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                holder.rTime.setText("00:00:00");
            }
        }.start();

        holder.UnreserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.UnreserveBtn.getContext());
                View v = LayoutInflater.from(holder.UnreserveBtn.getContext()).inflate(R.layout.unreserve_dialog,null,false);
                builder.setView(v);
                final AlertDialog alertDialog = builder.create();
                TextView noUnReserve = v.findViewById(R.id.nounReserve);
                TextView confirmUnReserve = v.findViewById(R.id.confirmunReserve);
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
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        holder.uploadpaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class reserveViewHolder extends RecyclerView.ViewHolder {
        TextView rTitle,rPrice, rTime,rWebsite;
        ImageView rImage, UnreserveBtn, uploadpaymentBtn;
        public reserveViewHolder(@NonNull View itemView) {
            super(itemView);
            rTitle = itemView.findViewById(R.id.rTitle);
            rPrice = itemView.findViewById(R.id.rPrice);
            rTime = itemView.findViewById(R.id.rTime);
            rWebsite = itemView.findViewById(R.id.rWebsite);
            rImage = itemView.findViewById(R.id.rImage);
            UnreserveBtn = itemView.findViewById(R.id.UnreserveBtn);
            uploadpaymentBtn = itemView.findViewById(R.id.uploadPayment);
        }
    }
}
