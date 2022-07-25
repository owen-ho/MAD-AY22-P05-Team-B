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
    private final List<MessageListener> messageListenerList = new ArrayList<>();
    private String uid;
    private String username;
    private RecyclerView messagerecycleview;
    private MessageAdapter messageadapter;
    private ImageView userprofilepic;
    private int unseenmessage = 0;
    private String chatkey="";
    private String lastmessage = "";
    private boolean dataset = false;
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

        //Product productclass = (Product) getIntent().getSerializableExtra("product");//get product from adapter
//        Product products = (Product) getIntent().getSerializableExtra("product");//get product from adapter
//        String sellerid = products.getSellerUid();
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
                                                      public void onFailure(@NonNull Exception e) {//set default picture
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




        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int getchatcount = (int)snapshot.getChildrenCount();
                Log.v("chatcount",String.valueOf(getchatcount));

                if(getchatcount >0){
                    Log.v("chatcount",String.valueOf(getchatcount));
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                        final String getkey = dataSnapshot1.getKey();
                        if (getkey != null){
                            chatkey = getkey;

                        }

                        if(dataSnapshot1.hasChild("user_1")&&dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")){
                            final String getuserone = dataSnapshot1.child("user_1").getValue(String.class);
                            final String getusertwo = dataSnapshot1.child("user_2").getValue(String.class);

                            Log.v("test1",getuserone);
                            Log.v("test2",getusertwo);
//                                        Log.v("sellerid",sellerid);
                            Log.v("uid",user.getUid());

                            if((getuserone.equals(user.getUid())|| getusertwo.equals(user.getUid()))){
                                sellerid = getuserone.equals(user.getUid())?getusertwo:getuserone;


                                Log.v("test","hello");
                                for(DataSnapshot chatdatasnapshot: dataSnapshot1.child("messages").getChildren()){
                                    if(dataSnapshot1.child("messages").hasChildren()){
                                        final long getmessagekey = Long.parseLong(chatdatasnapshot.getKey());
                                        final long getlastseenmessage = Long.parseLong(MemoryData.getlastmsgts(ChatFeature.this,getkey));
                                        //String getlastseenmessage = chatdatasnapshot.child("msg").getValue().toString();
                                        lastmessage = chatdatasnapshot.child("msg").getValue(String.class);
//                                        if(getmessagekey>getlastseenmessage){
//                                            unseenmessage++;
//                                        }

                                    }
                                }   userRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        //messageListenerList.clear();
                                        Log.v("nnb","puacb");
                                        unseenmessage = 0;
//                                        lastmessage = "";

                                        chatkey="";
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                            dataset = false;
                                            if (dataSnapshot.getKey().toString().equals(sellerid)){
                                                getname = dataSnapshot.child("username").getValue(String.class);
                                                Log.v("namenamexd",getname);
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }
                        }
                        Log.v("testing",sellerid);
                    }
                }
                if(!dataset){
                    //dataset = true;
                    Log.v("testing1",sellerid);
                    Log.v("nametest",getname);

                    MessageListener messagelistiners = new MessageListener(getname,user.getUid(),lastmessage,getprofilepic,unseenmessage,chatkey,sellerid);
                    Log.v("Lastmessage",lastmessage);
                    if (!messagelistiners.getLastmessage().equals("")){
                        Log.v("yesif","ok");

                        messageListenerList.clear();
                        messageListenerList.add(messagelistiners);
                        messageadapter.updatedata(messageListenerList);

                        messageadapter.notifyDataSetChanged();
                        dataset = true;

                    }
                    else{
                        Log.v("yesif","no");

                    }
                    dataset = true;

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
}