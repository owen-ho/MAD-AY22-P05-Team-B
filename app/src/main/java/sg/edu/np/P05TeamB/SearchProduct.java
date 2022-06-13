package sg.edu.np.P05TeamB;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class SearchProduct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        //below codes for testing only
        ArrayList<String> pNameList = new ArrayList<>();
        pNameList.add("a");
        pNameList.add("b");
        pNameList.add("c");
        pNameList.add("d");

        RecyclerView sProductList = findViewById(R.id.sProductView);
        SearchProductAdapter pAdapter = new SearchProductAdapter(pNameList, this);

        //use gridlayout manager to control the number of cards per row in recyclerview
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2,GridLayoutManager.VERTICAL,false);
        sProductList.setLayoutManager(gridLayoutManager);
        sProductList.setAdapter(pAdapter);
    }
}