package sg.edu.np.MulaSave;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.Collections;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView; // creation of recyclerview item
    private NotificationAdapter notificationAdapter;
    private ArrayList<Notification> notificationList; // adding notification into an arraylist


    public ImageView backbuttonNotif; //backbutton

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification); //setting the content of the activity to the notification activity
        recyclerView = findViewById(R.id.notif_recycler_view);
        recyclerView.setHasFixedSize(true); //recycler view has a fixed size
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        notificationList = new ArrayList<>();  //create list of notifications to store inside so we can display later
        notificationAdapter = new NotificationAdapter(this, notificationList); //new notification adapter
        recyclerView.setAdapter(notificationAdapter);
        backbuttonNotif = findViewById(R.id.BackbuttonNotif);

        readNotification();


        backbuttonNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    private void readNotification(){ //what happens when user read notifications
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //referencing firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("notifications").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationList.clear(); //clear notification
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Notification notification = snapshot.getValue(Notification.class);
                    notificationList.add(notification);
                }
                Collections.reverse(notificationList);
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}