package sg.edu.np.MulaSave;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SellerPaymentView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_payment_view);

        ImageView sellerpaymentback = findViewById(R.id.SellerPaymentBack);
        ImageView declinepaymentbtn = findViewById(R.id.DeclinePaymentBtn);
        ImageView acceptpaymentbtn = findViewById(R.id.AcceptPaymentBtn);

        sellerpaymentback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}