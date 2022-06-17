package sg.edu.np.P05TeamB;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class profilefrag extends Fragment {

    int SELECT_PICTURE = 200;
    public profilefrag() {
        // Required empty public constructor
    }

    public static profilefrag newInstance(String param1, String param2) {
        profilefrag fragment = new profilefrag();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_profile, container, false);
        View V = inflater.inflate(R.layout.fragment_profile, container, false);
        ImageView profilepic = V.findViewById(R.id.imageView3);
        Picasso.get().load("https://s.yimg.com/ny/api/res/1.2/XQDn25S2GxDbItD31nyahQ--/YXBwaWQ9aGlnaGxhbmRlcjt3PTk2MDtoPTU4OTtjZj13ZWJw/https://s.yimg.com/os/creatr-uploaded-images/2021-02/e2698180-6b90-11eb-97fb-e40529b50439").into(profilepic);
        return  V;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference userRef = database.getReference("user");
        TextView email1 = view.findViewById(R.id.emailbox);
        TextView user1 = view.findViewById(R.id.usernamebox);
        Button email = view.findViewById(R.id.changeemail);
        Button user2 = view.findViewById(R.id.changeuser);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user!=null){
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String username = snapshot.child(user.getUid()).child("username").getValue().toString();

                    String email3 = user.getEmail();
                    email1.setText(email3);
                    user1.setText(username);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{

        }
//        email1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(profilefrag.this, Pop.class));
//            }
//        });

        //the plus icon to upload a new profile picture
        ImageView upload = view.findViewById(R.id.uploadPic);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImg();
            }
        });


    }

    //meethod to select image from gallery
    private void chooseImg(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile Picture"),SELECT_PICTURE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri pfpUri = data.getData();
                if (null != pfpUri) {
                    //upload to firebase
                }
            }
        }
    }//end of onActivityResult
}