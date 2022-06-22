package sg.edu.np.MulaSave;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import sg.edu.np.MulaSave.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private ArrayList<Product> productList;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);//set user to be signed in persistently
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //so that fragment starts at home when user enters (instead of no fragment)
        replacefragment(new homefrag());

        int intentFragment = getIntent().getExtras().getInt("frgToLoad");
        if(intentFragment==3){
            replacefragment(new profilefrag());
            binding.bottomNavigationView.getMenu().getItem(3).setChecked(true);
        }else{

        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    replacefragment(new homefrag());
                    break;
                case R.id.Shop:
                    replacefragment(new shoppingfrag());
                    break;
                case R.id.Wishlist:
                    replacefragment(new wishlistfrag());
                    break;
                case R.id.profile:
                    replacefragment(new profilefrag());
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