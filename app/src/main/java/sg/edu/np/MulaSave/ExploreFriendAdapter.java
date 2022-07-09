package sg.edu.np.MulaSave;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExploreFriendAdapter extends RecyclerView.Adapter<ExploreFriendAdapter.ExploreFriendViewHolder>{

    ArrayList<User> exploreList;
    DatabaseReference databaseRefUser = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("user");
    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();

    public ExploreFriendAdapter(ArrayList<User> _exploreList){
        this.exploreList = _exploreList;
    }
    @NonNull
    @Override
    public ExploreFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;//find view and return the viewholder
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_view_row,parent,false);
        return new ExploreFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreFriendViewHolder holder, int position) {
        databaseRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {//get data on success
                for (DataSnapshot ss : snapshot.getChildren()){
                    User extractUser = ss.getValue(User.class);
                    exploreList.add(extractUser);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return exploreList.size();
    }

    //viewholder
    public class ExploreFriendViewHolder extends RecyclerView.ViewHolder{
        ImageView userPic;
        TextView userName, negativeText, positiveText;
        public ExploreFriendViewHolder(View itemView){
            super(itemView);
            userPic = itemView.findViewById(R.id.userPic);
            userName = itemView.findViewById(R.id.userName);
            negativeText = itemView.findViewById(R.id.negativeText);
            positiveText = itemView.findViewById(R.id.positiveText);
        }
    }
}
