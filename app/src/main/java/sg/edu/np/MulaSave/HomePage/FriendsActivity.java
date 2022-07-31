package sg.edu.np.MulaSave.HomePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import sg.edu.np.MulaSave.FriendsFragments.ExploreFragment;
import sg.edu.np.MulaSave.FriendsFragments.FriendsFragment;
import sg.edu.np.MulaSave.FriendsFragments.FriendsActivityAdapter;
import sg.edu.np.MulaSave.FriendsFragments.RequestsFragment;
import sg.edu.np.MulaSave.R;
import sg.edu.np.MulaSave.User;

public class FriendsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    public static ViewPager viewPager;
    FriendsActivityAdapter adapter;
    ImageView friendsBackTrack;
    static ImageView refreshViewPager;
    public static String targetUserUid;
    public static Integer targetTab;
    String TAG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        //the variables may not be initiated yet
        try{
            targetUserUid = getIntent().getExtras().getString("targetUserUid");
            targetTab = getIntent().getExtras().getInt("targetTab",2);
        }
        catch (Exception e){
            Log.i(TAG,"No target user and tab");
        }


        tabLayout = findViewById(R.id.tabLayoutFriends);
        viewPager = findViewById(R.id.viewPagerFriends);
        friendsBackTrack = findViewById(R.id.friendsBackTrack);

        //onclick listeners for the back button
        friendsBackTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tabLayout.setupWithViewPager(viewPager);

        adapter = new FriendsActivityAdapter(getSupportFragmentManager());
        adapter.addFragment(new FriendsFragment(), "Friends");
        adapter.addFragment(new RequestsFragment(), "Requests");
        adapter.addFragment(new ExploreFragment(), "Explore");
        viewPager.setAdapter(adapter);

        refreshViewPager = findViewById(R.id.refeshViewPager);
        refreshViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.getAdapter().notifyDataSetChanged();
            }
        });


        //passing target data
        if(targetUserUid != null && targetTab != null){
            TabLayout.Tab tab = tabLayout.getTabAt(targetTab);
            tab.select();
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //clear the queries and static variables when the user leaves the tab
                targetUserUid = null;
                targetTab = null;
                if(ExploreFragment.searchFriendExplore != null){
                    ExploreFragment.searchFriendExplore.setQuery("",true);
                }
                if(FriendsFragment.searchFriendList != null){
                    FriendsFragment.searchFriendList.setQuery("",true);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {//method to scroll to the top when the user presses the tab again
                if(tab.getPosition() == 0){
                    FriendsFragment.ffScrollTop();
                }
                else if (tab.getPosition() == 1){
                    RequestsFragment.ffScrollTop();
                }
                else{
                    ExploreFragment.ffScrollTop();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!(adapter == null)){
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * This method ensures that there are no duplicated users in the recyclerviews in each fragments
     * under the FriendsActivity
     * @param u the user to check
     * @param users the list of users to check if user u is in the list
     * @return boolean object stating whether to add the new user
     */
    public static boolean addNewUser(User u, ArrayList<User> users){
        for (User user : users){
            if(user.getUid().equals(u.getUid())){
                return false;//return false if duplicate
            }
        }
        return true;//return true if no duplicate
    }

    /**
     * clicks on the refreshPage button to call the refreshing function
     */
    public static void refreshPage(){
        refreshViewPager.performClick();
    }

    @Override
    protected void onPause() {
        super.onPause();
        targetTab = null;//set back the targets to null on pause
        targetUserUid = null;
    }
}