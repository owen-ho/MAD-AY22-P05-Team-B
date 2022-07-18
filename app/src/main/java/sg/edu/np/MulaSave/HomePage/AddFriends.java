package sg.edu.np.MulaSave.HomePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import sg.edu.np.MulaSave.FriendsFragments.ExploreFragment;
import sg.edu.np.MulaSave.FriendsFragments.FriendsFragment;
import sg.edu.np.MulaSave.FriendsFragments.FriendsActivityAdapter;
import sg.edu.np.MulaSave.FriendsFragments.RequestsFragment;
import sg.edu.np.MulaSave.R;

public class AddFriends extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        tabLayout = findViewById(R.id.tabLayoutFriends);
        viewPager = findViewById(R.id.viewPagerFriends);

        tabLayout.setupWithViewPager(viewPager);

        FriendsActivityAdapter adapter = new FriendsActivityAdapter(getSupportFragmentManager());
        adapter.addFragment(new FriendsFragment(), "Friends");
        adapter.addFragment(new RequestsFragment(), "Requests");
        adapter.addFragment(new ExploreFragment(), "Explore");
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TextView txt = findViewById(R.id.textView6);
                txt.setText(adapter.getPageTitle(viewPager.getCurrentItem()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}