package sg.edu.np.MulaSave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.MulaSave.messages.MessageAdapter;
import sg.edu.np.MulaSave.messages.MessageListener;

public class ChatFeature extends AppCompatActivity {
    private List<MessageListener> messageListenerList;
    private String uid;
    private String username;
    private RecyclerView messagerecycleview;
    private MessageAdapter messageadapter;
    private ImageView userprofilepic;
    private int unseenmessage = 0;
    private String chatkey="0";
    private String lastmessage = "";
    private boolean dataset = false;
    boolean recreate = false;
    String currentuser= "";
    String sellerid="";
    String getname="";
    String getprofilepic="";

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference userRef = database.getReference("user");
    DatabaseReference chatRef = database.getReference("Chat");
    private FirebaseAuth mAuth;



    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        messageListenerList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        currentuser=mAuth.getCurrentUser().getUid();
        setContentView(R.layout.activity_chatfeature);
        messagerecycleview=findViewById(R.id.messgaerecycleview);
        user.getUid(); //own uid
        messagerecycleview.setHasFixedSize(true);
        messagerecycleview.setLayoutManager(new LinearLayoutManager(this));
        messagerecycleview.setAdapter(new MessageAdapter(messageListenerList, ChatFeature.this));



        //set adpater to recycleview
        messageadapter = new MessageAdapter(messageListenerList, ChatFeature.this);
        messagerecycleview.setAdapter( messageadapter);
        ImageView backbutton = findViewById(R.id.backbutton1);


        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        /**
         * Check if user is null
         * if not null get user profile picture else use default image
         */
        if (user!=null){
            userRef.addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot snapshot) {
                      String username;
                      if (snapshot.child(user.getUid()).child("username").exists()) { //Check if username exists to prevent crash
                          username = snapshot.child(user.getUid()).child("username").getValue().toString();
                      } else {
                          username = "";
                      }
                      storageRef.child("profilepics/" + user.getUid().toString() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                          @Override
                          public void onSuccess(Uri uri) {//user has set a profile picture before
                              userprofilepic = findViewById(R.id.userprofilepic1);
                              Picasso.get().load(uri).into(userprofilepic);
                              progressDialog.dismiss();

                          }
                      }).addOnFailureListener(new OnFailureListener() {//file does not exist (user did not upload before)
                          @Override
                          public void onFailure(@NonNull Exception e) {
                              progressDialog.dismiss();
                          }
                      });
                  }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressDialog.dismiss();
                }
            });
        }

        /**
         * Get chat history from all user and setting last message based on firebase and doing validation
         */
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageListenerList.clear();
                int getchatcount = (int)snapshot.getChildrenCount();
                if(getchatcount >0){
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                        MessageListener messageListener = new MessageListener();
                        final String getkey = dataSnapshot1.getKey();
                        if (getkey != null){
                            messageListener.setChatkey(getkey);
                        }

                        if(dataSnapshot1.hasChild("user_1")&&dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")){
                            final String getuserone = dataSnapshot1.child("user_1").getValue(String.class);
                            final String getusertwo = dataSnapshot1.child("user_2").getValue(String.class);
                            if((getuserone.equals(user.getUid())|| getusertwo.equals(user.getUid()))){
                                sellerid = getuserone.equals(user.getUid())?getusertwo:getuserone; // setting seller id
                                // looping firebase and setting last message
                                for(DataSnapshot chatdatasnapshot: dataSnapshot1.child("messages").getChildren()){
                                    if(dataSnapshot1.child("messages").hasChildren()){
                                        final long getmessagekey = Long.parseLong(chatdatasnapshot.getKey());
                                        final long getlastseenmessage = Long.parseLong(MemoryData.getlastmsgts(ChatFeature.this,getkey));
                                        lastmessage = chatdatasnapshot.child("msg").getValue(String.class); //Setting lastmessage based on the validation
                                        messageListener.setLastmessage(lastmessage);
                                    }
                                }
                                messageListener.setSellerid(sellerid);
                                messageListenerList.clear();
                                storageRef.child("profilepics/" + sellerid + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {//user has set a profile picture before
                                        messageListener.setProfilepic(uri.toString());
                                        messageListener.setUnseenMessages(unseenmessage);
                                        addNewMessageListener(messageListener, (ArrayList<MessageListener>) messageListenerList);
                                        messageadapter.notifyDataSetChanged();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        messageListener.setProfilepic("https://www.business2community.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640.png");
                                        messageListener.setUnseenMessages(unseenmessage);
                                        messageListener.setSellerid(sellerid);
                                        addNewMessageListener(messageListener, (ArrayList<MessageListener>) messageListenerList);
                                        messageadapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    }//
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Check if messagelistner already exist and if exist then it will remove from the list before adding the updated listener.
     * @param messageListener
     * @param messageListenerList
     */
    private void addNewMessageListener(MessageListener messageListener, ArrayList<MessageListener> messageListenerList){
        for (MessageListener m : messageListenerList){
            if(m.getChatkey().equals(messageListener.getChatkey())){
                messageListenerList.remove(m);
                break;
            }
        }
        messageListenerList.add(messageListener);
    }
}