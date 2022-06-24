package sg.edu.np.MulaSave;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sg.edu.np.MulaSave.Fragments.ProfileFragment;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser usr = mAuth.getCurrentUser();
        if(usr!=null){
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            assert usr != null;
            i.putExtra("email",usr.getEmail());
            i.putExtra("uid",usr.getUid());
            startActivity(i);
            finish();
        }
        TextView registerPrompt = findViewById(R.id.registerPrompt);
        EditText email = findViewById(R.id.emailBox);
        EditText password = findViewById(R.id.passwordBox);
        Button loginBtn = findViewById(R.id.loginButton);
        TextView forgotpassword = findViewById(R.id.forgorpassword);

        registerPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().matches("") || password.getText().toString().matches("")){//check for empty inputs
                    Toast.makeText(LoginActivity.this,"Please ensure all fields are filled",Toast.LENGTH_SHORT).show();
                }
                else if ((Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) == false){//check if email is in proper format
                    Toast.makeText(LoginActivity.this,"Please enter a valid email",Toast.LENGTH_SHORT).show();
                    email.setText("");//clear email text
                }
                else{//basic validation done, try creating new account
                    signIn(email.getText().toString(),password.getText().toString());
                }
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(LoginActivity.this, ForgetPassword.class);
                startActivity(x);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    @Override
    public void onBackPressed() { //prevent user from entering app again after logging out
        //do nothing
    }

    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // User is signed in

                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                assert user != null;
                                i.putExtra("email",user.getEmail());
                                i.putExtra("uid",user.getUid());
                                startActivity(i);
                                finish();
                            } else {
                                // User is signed out
                                Log.d("signout", "onAuthStateChanged:signed_out");
                            }
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed. Try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void openfrag(){
        ProfileFragment frag = new ProfileFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        //bundle.putString("email",);
    }

}