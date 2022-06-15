package sg.edu.np.P05TeamB;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import java.util.ArrayList;

public class SearchProduct extends AppCompatActivity {

    public static Activity fa;
    private ArrayList<Product> sProductList;

    //public ArrayList<Product> sProductList = initSProduct();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        SearchView searchView = findViewById(R.id.searchQuery1);
        //make the whole search bar clickable
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        //navigate to new activity after entering
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                getIntent().putExtra("testing","testing");
                recreate();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        sProductList = new ArrayList<Product>();
        RecyclerView sProductRView = findViewById(R.id.shoppingrecyclerview1);
        SearchProductAdapter pAdapter = new SearchProductAdapter(sProductList, this);

        //use gridlayout manager to control the number of cards per row in recyclerview
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2,GridLayoutManager.VERTICAL,false);
        sProductRView.setLayoutManager(gridLayoutManager);
        sProductRView.setAdapter(pAdapter);
    }


    //method to initialise random product for testing
    /*private ArrayList<Product> initSProduct(){
        ArrayList<Product> sProductList = new ArrayList<>();
        while(sProductList.size() < 20){
            Random rand = new Random();
            Integer randId;
            String randName = "Name" + Integer.toString(rand.nextInt());
            Double price = 4.50;
            Float rating = 4.3f;
            String websiteName = "Lazada";

            //ensure id does not clash
            while (true){
                Boolean repeatId = false;//set repeating id conition to false
                randId = Math.abs(rand.nextInt());
                for(Product product : sProductList){
                    if(product.getID() == randId){
                        repeatId = true;//set condition to false if there is repeating id found
                    }
                }
                if(repeatId == false){
                    break;//break the while loop if there is no repeats
                }
            }
            Product p = new Product(randId,randName,price,rating,websiteName);
            sProductList.add(p);
        }
        return sProductList;
    }*/
}