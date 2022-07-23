package sg.edu.np.MulaSave.HomePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import sg.edu.np.MulaSave.FriendsFragments.ExploreFragment;
import sg.edu.np.MulaSave.FriendsFragments.FriendsFragment;
import sg.edu.np.MulaSave.FriendsFragments.FriendsActivityAdapter;
import sg.edu.np.MulaSave.FriendsFragments.RequestsFragment;
import sg.edu.np.MulaSave.R;
import sg.edu.np.MulaSave.User;

public class AddFriends extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    FriendsActivityAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        tabLayout = findViewById(R.id.tabLayoutFriends);
        viewPager = findViewById(R.id.viewPagerFriends);

        tabLayout.setupWithViewPager(viewPager);

        adapter = new FriendsActivityAdapter(getSupportFragmentManager());
        adapter.addFragment(new FriendsFragment(), "Friends");
        adapter.addFragment(new RequestsFragment(), "Requests");
        adapter.addFragment(new ExploreFragment(), "Explore");
        viewPager.setAdapter(adapter);
        //viewPager.setOffscreenPageLimit(3);
        TextView txt = findViewById(R.id.textView6);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.notifyDataSetChanged();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(viewPager.getCurrentItem()==1){
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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

    public static boolean addNewUser(User u, ArrayList<User> users){
        for (User user : users){
            if(user.getUid().equals(u.getUid())){
                return false;//return false if duplicate
            }
        }
        return true;//return true if no duplicate
    }

}