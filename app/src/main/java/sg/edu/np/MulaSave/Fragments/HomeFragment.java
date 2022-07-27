package sg.edu.np.MulaSave.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import sg.edu.np.MulaSave.HomePage.FriendsActivity;
import sg.edu.np.MulaSave.HomePage.AddPostActivity;
import sg.edu.np.MulaSave.HomePage.HomePostFragmentAdapter;
import sg.edu.np.MulaSave.HomePage.LikedPostActivity;
import sg.edu.np.MulaSave.MainActivity;
import sg.edu.np.MulaSave.R;
import sg.edu.np.MulaSave.HomePage.HomeExplorePosts;
import sg.edu.np.MulaSave.HomePage.HomeFriendsPosts;

public class HomeFragment extends Fragment {

    private Parcelable recyclerViewState;
    ImageView addFriend, addPost, viewLikes;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView  hPageView;
    TabLayout barTab;

    FirebaseDatabase databaseRef = FirebaseDatabase
            .getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference databaseRefUser = databaseRef.getReference("user");
    DatabaseReference databaseRefPost = databaseRef.getReference("post");
    FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();

    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        addFragmentPost(view);
        // Inflate the layout for this fragment
        return view;
    }

    //create this method because getView() only works after onCreateView()
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        addFriend = view.findViewById(R.id.hAddFirend);//get the add friend and post imageviews
        addPost = view.findViewById(R.id.hAddPost);
        viewLikes = view.findViewById(R.id.hLikes);
        hPageView = view.findViewById(R.id.hPageView);
        barTab = view.findViewById(R.id.homeTabLayout);

        addFriend.setOnClickListener(new View.OnClickListener() {//set on click listener
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), FriendsActivity.class);//go to add friends class
                startActivity(i);
            }
        });

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//go to add post activity
                Intent i = new Intent(getActivity(), AddPostActivity.class);
                startActivity(i);
            }
        });

        viewLikes.setOnClickListener(new View.OnClickListener() {//start activity to see the liked posts
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), LikedPostActivity.class);
                startActivity(i);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*// Save state
                private Parcelable recyclerViewState;
                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

                // Restore state
                recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);*/
                viewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }//end of onview created method

    private void addFragmentPost(View view) {
        tabLayout = view.findViewById(R.id.homeTabLayout);
        viewPager = view.findViewById(R.id.homeViewPager);
        HomePostFragmentAdapter homePostFragmentAdapter = new HomePostFragmentAdapter(getChildFragmentManager());
        homePostFragmentAdapter.addFragment(new HomeExplorePosts(), "Explore");
        homePostFragmentAdapter.addFragment(new HomeFriendsPosts(),"Friends");
        viewPager.setAdapter(homePostFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void onPause() {
        super.onPause();
        if(tabLayout.getSelectedTabPosition()==1){//if the index of the selected tab is 1 (friends layout for posts)
            MainActivity.homeFriends = true;
        }
        else{
            MainActivity.homeFriends = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MainActivity.homeFriends == true){//if user lastviewed the friends tab
            TabLayout.Tab tab = barTab.getTabAt(1);
            tab.select();//navigate back to the own post tab if the user is not
        }
    }
}