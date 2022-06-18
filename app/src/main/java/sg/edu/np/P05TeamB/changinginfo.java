package sg.edu.np.P05TeamB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class changinginfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changingemail);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        EditText user1 =findViewById(R.id.changeuser);
        EditText email = findViewById(R.id.changeemail);
        EditText password = findViewById(R.id.changepass);
        Button confirmation = findViewById(R.id.confirmation);

        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(user!=null){
                    if (!email.getText().toString().equals("")){
                        user.updateEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "email updated", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                    if (!password.getText().toString().equals("")){

                        user.updatePassword(password.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "password updated", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                    }
                    Log.d("email",email.getText().toString());
                    Log.d("email",password.getText().toString());



                }
            }

        });



    }
}