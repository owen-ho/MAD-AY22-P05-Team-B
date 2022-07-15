package sg.edu.np.MulaSave;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class descriptionpage extends AppCompatActivity {

    Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descriptionpage);
        TextView title = findViewById(R.id.Title);
        TextView price = findViewById(R.id.Price);
        TextView description =findViewById(R.id.Description);
        TextView condition =findViewById(R.id.Condition);
        TextView meetup = findViewById(R.id.Meetup);
        TextView username = findViewById(R.id.Sellerusername);

        ImageView pic = findViewById(R.id.imageView16);
        Button chat = findViewById(R.id.Chat);
        Button reserve = findViewById(R.id.Reserve);


        product = (Product) getIntent().getSerializableExtra("product");//get product from adapter
        title.setText(product.getTitle());
        price.setText("$" +String.valueOf(product.getPrice()));
        description.setText(product.getDesc());
        condition.setText(product.getCondition());
        meetup.setText(product.getMeetup());
        username.setText(product.getWebsite());
        Picasso.get().load(product.getImageUrl()).into(pic);




        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(descriptionpage.this, chatfeaturetesting.class);
                i.putExtra("product",product);
                startActivity(i);

            }
        });

        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });










    }
}