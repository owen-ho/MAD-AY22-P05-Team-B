package sg.edu.np.MulaSave.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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

import sg.edu.np.MulaSave.HomePage.FriendsActivity;
import sg.edu.np.MulaSave.HomePage.Post;
import sg.edu.np.MulaSave.LoginActivity;
import sg.edu.np.MulaSave.MainActivity;
import sg.edu.np.MulaSave.NotificationActivity;
import sg.edu.np.MulaSave.ProductSuggestionProvider;
import sg.edu.np.MulaSave.ProfileEdit;
import sg.edu.np.MulaSave.R;
import sg.edu.np.MulaSave.SelectProfilePic;

public class ProfileFragment extends Fragment {

    int SELECT_PICTURE = 200;
    private String profilePicLink = MainActivity.profilePicLink;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView noOfFriends, noOfPosts;
    int count;
    CardView friendCard, postCard;
    TabLayout barTab;

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
        ProfileInnerFragAdapter profileInnerFragAdapter = new ProfileInnerFragAdapter(getChildFragmentManager());
        profileInnerFragAdapter.addFragment(new ChildPostFragment(), "Posts");
        profileInnerFragAdapter.addFragment(new ChildUploadFragment(),"Uploads");
        profileInnerFragAdapter.addFragment(new ChildReserveFragment(), "Reserved");
        viewPager.setAdapter(profileInnerFragAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference userRef = database.getReference("user");
        DatabaseReference postRef = database.getReference("post");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();

        TextView email1 = view.findViewById(R.id.emailbox);
        TextView usertext = view.findViewById(R.id.usernamebox);
        ImageView profilepic = view.findViewById(R.id.pfpbox);
        profilepic.setVisibility(View.INVISIBLE);

        ImageView infobutton = view.findViewById(R.id.changeinfo);
        ImageView logoutbutton = view.findViewById(R.id.logoutBtn);
        ImageView notificationbtn = view.findViewById(R.id.notificationBtn);

        noOfFriends = view.findViewById(R.id.noOfFriends);
        noOfPosts = view.findViewById(R.id.noOfPosts);
        friendCard = view.findViewById(R.id.friendCard);
        postCard = view.findViewById(R.id.postCard);
        barTab = view.findViewById(R.id.tabLayout);

        noOfFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FriendsActivity.class);
                startActivity(intent);
            }
        });

        userRef.child(usr.getUid()).child("friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noOfFriends.setText(String.valueOf(snapshot.getChildrenCount()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                count = 0;
                for (DataSnapshot ss : snapshot.getChildren()){
                    Post post = ss.getValue(Post.class);
                    if (post.getCreatorUid().equals(usr.getUid())){
                        count+=1;
                    }
                }
                noOfPosts.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //start friends activity
        friendCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),FriendsActivity.class);
                startActivity(i);
            }
        });

        //navigate to user's own post on click
        postCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabLayout.Tab tab = barTab.getTabAt(0);
                tab.select();//navigate back to the own post tab if the user is not
            }
        });

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
        }

        infobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileEdit.class);
                startActivity(intent);
            }
        });

        notificationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
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
                //Clear query history
                MainActivity.query=null;

                //Clear suggestion history
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getActivity(),
                        ProductSuggestionProvider.AUTHORITY, ProductSuggestionProvider.MODE);
                suggestions.clearHistory();

                //Clear query history list stored in SharedPreferences
                getContext().getSharedPreferences("Recent API queries", 0).edit().clear().commit();

                //Sign out from firebase authentication
                FirebaseAuth.getInstance().signOut();

                //Send user back to login screen
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

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){//user's own posts tab
                    ChildPostFragment.cpfSrollTop();
                }
                else if(tab.getPosition() == 1){//user's own uploaded products
                    ChildUploadFragment.cufSrollTop();
                }
                else{//user's reserved products
                    ChildReserveFragment.crfScrollTop();
                }
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