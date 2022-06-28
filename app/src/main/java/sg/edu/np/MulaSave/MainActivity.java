package sg.edu.np.MulaSave;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import sg.edu.np.MulaSave.Fragments.CommunityFragment;
import sg.edu.np.MulaSave.Fragments.HomeFragment;
import sg.edu.np.MulaSave.Fragments.ProfileFragment;
import sg.edu.np.MulaSave.Fragments.ShoppingFragment;
import sg.edu.np.MulaSave.Fragments.WishlishFragment;
import sg.edu.np.MulaSave.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    //For use in making recyclerviews persistent
    public static ArrayList<Product> productList;
    public static ArrayList<Product> homeproductList;
    public static String profilePicLink;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //so that fragment starts at home when user enters (instead of no fragment)
        replacefragment(new HomeFragment());

        int intentFragment = getIntent().getExtras().getInt("frgToLoad");
        if(intentFragment==4){
            replacefragment(new ProfileFragment());
            binding.bottomNavigationView.getMenu().getItem(4).setChecked(true);
        }else{

        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    replacefragment(new HomeFragment());
                    break;
                case R.id.Shop:
                    replacefragment(new ShoppingFragment());
                    break;
                case R.id.Wishlist:
                    replacefragment(new WishlishFragment());
                    break;
                case R.id.Community:
                    replacefragment(new CommunityFragment());
                    break;
                case R.id.profile:
                    replacefragment(new ProfileFragment());
                    break;

            }
            return true;
        });

    }

    private void replacefragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

}