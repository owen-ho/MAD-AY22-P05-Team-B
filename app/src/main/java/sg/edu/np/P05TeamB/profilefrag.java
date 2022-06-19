package sg.edu.np.P05TeamB;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

        return  V;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference userRef = database.getReference("user");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();

        TextView email1 = view.findViewById(R.id.emailbox);
        TextView usertext = view.findViewById(R.id.usernamebox);
        ImageView profilepic = view.findViewById(R.id.imageView3);

        Button infobutton = view.findViewById(R.id.changeinfo);
        Button logoutbutton = view.findViewById(R.id.logoutBtn);

        if (user!=null){
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String username = snapshot.child(user.getUid()).child("username").getValue().toString();
                    String email3 = snapshot.child(user.getUid()).child("email").getValue().toString();

                    email1.setText(email3);
                    usertext.setText(username);
                    storageRef.child("profilepics/" + usr.getUid().toString() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {//user has set a profile picture before
                            Picasso.get().load(uri).into(profilepic);
                        }
                    }).addOnFailureListener(new OnFailureListener() {//file does not exist (user did not upload before)
                        @Override
                        public void onFailure(@NonNull Exception e) {//set default picture
                            Picasso.get().load("https://www.business2community.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640.png").into(profilepic);
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{

        }

        infobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),changinginfo.class);
                startActivity(intent);
            }
        });

        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getActivity(),LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

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
                    Intent i = new Intent(getActivity(),SelectProfilePic.class);
                    i.putExtra("path",pfpUri.toString());
                    startActivity(i);
                }
            }
        }
    }//end of onActivityResult
}