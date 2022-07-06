package sg.edu.np.MulaSave;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class descriptionpage extends AppCompatActivity {

    Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descriptionpage);
        TextView title = findViewById(R.id.Title);
        TextView price = findViewById(R.id.Price);
        Button chat = findViewById(R.id.Chat);
        Button reserve = findViewById(R.id.Reserve);


        product = (Product) getIntent().getSerializableExtra("product");//get product from adapter
        title.setText(product.getTitle());
        price.setText("$" +String.valueOf(product.getPrice()));

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });










    }
}