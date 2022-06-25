package sg.edu.np.MulaSave;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
        ImageView back = findViewById(R.id.backButtonChangePassword);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        forgetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    auth.sendPasswordResetEmail(email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        forgetbutton.setEnabled(false);
                                        forgetbutton.setClickable(false);
                                        Log.d("ForgottenPasswordEmail", "Email sent.");
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@androidx.annotation.NonNull Exception e) {
                                    Toast.makeText(ForgetPassword.this, "Enter a valid email",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                catch(Exception e){
                    Log.w("forget password", String.valueOf(e));
                    Toast.makeText(ForgetPassword.this, "Enter a valid email",Toast.LENGTH_SHORT).show();
                }
                //line will not be reached if email is correct as user will be directed to the login page
                email.setText("");//reset the email text for user to input agn
            }
        });
    }
}