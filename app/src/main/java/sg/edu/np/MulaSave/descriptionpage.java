package sg.edu.np.MulaSave;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class descriptionpage extends AppCompatActivity {

    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descriptionpage);

        product = (Product) getIntent().getSerializableExtra("product");//get product from adapter
    }
}