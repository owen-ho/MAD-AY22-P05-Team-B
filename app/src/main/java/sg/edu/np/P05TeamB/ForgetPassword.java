package sg.edu.np.P05TeamB;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        EditText email = findViewById(R.id.ForgetEmail);
        Button forgetbutton = findViewById(R.id.forgetpasswordbutton);
        forgetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.sendPasswordResetEmail(email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("ForgottenPasswordEmail", "Email sent.");
                                }
                                Intent i = new Intent(ForgetPassword.this,LoginActivity.class);
                                startActivity(i);
                            }
                        });
            }
        });
    }
}