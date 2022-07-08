package sg.edu.np.MulaSave.Fragments;

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
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
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

import sg.edu.np.MulaSave.ChildUploadFragment;
import sg.edu.np.MulaSave.Documentation;
import sg.edu.np.MulaSave.LoginActivity;
import sg.edu.np.MulaSave.MainActivity;
import sg.edu.np.MulaSave.R;
import sg.edu.np.MulaSave.SelectProfilePic;
import sg.edu.np.MulaSave.ProfileEdit;

public class ProfileFragment extends Fragment {

    int SELECT_PICTURE = 200;
    private String profilePicLink = MainActivity.profilePicLink;
    TabLayout tabLayout;
    ViewPager viewPager;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        addFragment(view);


        return  view;
    }

    private void addFragment(View view) {
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        NestedFragAdapter nestedFragAdapter = new NestedFragAdapter(getChildFragmentManager());
        nestedFragAdapter.addFragment(new ChildPostFragment(), "Posts");
        nestedFragAdapter.addFragment(new ChildUploadFragment(),"Uploads");
        viewPager.setAdapter(nestedFragAdapter);
        tabLayout.setupWithViewPager(viewPager);
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
        ImageView profilepic = view.findViewById(R.id.pfpbox);
        profilepic.setVisibility(View.INVISIBLE);

        ImageView infobutton = view.findViewById(R.id.changeinfo);
        ImageView logoutbutton = view.findViewById(R.id.logoutBtn);

        ImageView documentation = view.findViewById(R.id.infoDocumentation);

        //load this as the default picture first
        if (profilePicLink!=null) {
            Picasso.get().load(profilePicLink).into(profilepic);
        }else{
            Picasso.get().load("https://www.business2community.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640.png").into(profilepic);
        }


        if (user!=null){
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    String username;
                    if(snapshot.child(user.getUid()).child("username").exists()){ //Check if username exists to prevent crash
                        username = snapshot.child(user.getUid()).child("username").getValue().toString();
                    }else{
                        username = "";
                    }

                    String email3 = snapshot.child(user.getUid()).child("email").getValue().toString();

                    email1.setText(email3);
                    usertext.setText(username);
                    storageRef.child("profilepics/" + usr.getUid().toString() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {//user has set a profile picture before
                            Picasso.get().load(uri).into(profilepic);
                            MainActivity.profilePicLink = uri.toString();
                        }
                    }).addOnFailureListener(new OnFailureListener() {//file does not exist (user did not upload before)
                        @Override
                        public void onFailure(@NonNull Exception e) {//set default picture

                        }
                    });
                    profilepic.setVisibility(View.VISIBLE);//set to visible from default of invisible
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        }else{

        }

        documentation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Documentation.class);
                startActivity(intent);
            }
        });

        infobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileEdit.class);
                startActivity(intent);
            }
        });

        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.profilePicLink = null;//Clear profilepic so that new user's profilepic is loaded instead
                if (MainActivity.productList != null) {//Clear productist so that previous user's queries are not seen
                    MainActivity.productList.clear();
                }
//                if (MainActivity.homeproductList != null) {
//                    MainActivity.homeproductList.clear();
//                    MainActivity.homeproductList = null;
//                }
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getActivity(), LoginActivity.class);
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

    //method to select image from gallery
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
                    Intent i = new Intent(getActivity(), SelectProfilePic.class);
                    i.putExtra("path",pfpUri.toString());
                    startActivity(i);
                }
            }
        }
    }//end of onActivityResult
}