package sg.edu.np.MulaSave;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference userRef = database.getReference("user");
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        TextView loginPrompt = findViewById(R.id.loginPrompt);
        EditText email = findViewById(R.id.emailRegister);
        EditText password = findViewById(R.id.passwordRegister);
        EditText passwordRe = findViewById(R.id.passwordRegisterRe);
        Button registerBtn = findViewById(R.id.registerButton);
        EditText Username = findViewById(R.id.usernameRegister);


        //check if user already logged in
        if(!MemoryData.getdata(this).isEmpty()){
            Intent i = new Intent(RegisterActivity.this,MainActivity.class);
            User u = new User();
            FirebaseUser user = mAuth.getCurrentUser();
            assert user != null;
            i.putExtra("email"," ");
            i.putExtra("uid", MemoryData.getdata(this));
            i.putExtra("username", MemoryData.getname(this));
            startActivity(i);
            finish();

        }

        loginPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().matches("") || password.getText().toString().matches("") || Username.getText().toString().matches("")){//check for empty inputs
                    Toast.makeText(RegisterActivity.this,"Please ensure all fields are filled",Toast.LENGTH_SHORT).show();
                }
                else if ((Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) == false){//check if email is in proper format
                    Toast.makeText(RegisterActivity.this,"Please enter a valid email",Toast.LENGTH_SHORT).show();
                    email.setText("");//clear email text
                }
                else if((password.getText().toString().equals(passwordRe.getText().toString()))== false){//passwords do not matct
                    Toast.makeText(RegisterActivity.this,"Passwords do not match",Toast.LENGTH_SHORT).show();
                    //clear passwords
                    password.setText("");
                    passwordRe.setText("");
                }
                else if (password.getText().toString().length() < 6){//password too short (lower than 6 char)
                    Toast.makeText(RegisterActivity.this,"Please ensure password is at least 6 characters",Toast.LENGTH_SHORT).show();
                }
                else{//basic validation done, try creating new account
                    createAccount(email.getText().toString(),password.getText().toString(),Username.getText().toString());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    private void createAccount(String email, String password,String Username){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            User u = new User();
                            u.uid = user.getUid();
                            u.email = user.getEmail();
                            u.username = Username;
                            userRef.child(u.uid).setValue(u);//Write user into realtime db

                            Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                            assert user != null;
                            i.putExtra("email",user.getEmail());
                            i.putExtra("uid",user.getUid());

                            // save username to memory
                            MemoryData.savename(Username,RegisterActivity.this);

                            // save uid to memory
                            MemoryData.savedata(user.getUid(),RegisterActivity.this);
                            startActivity(i);
                        } else {//sign in fail, likely due to email
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication Failed. Try another email",
                                    Toast.LENGTH_SHORT).show();
                            EditText rEmail = findViewById(R.id.emailRegister);
                            rEmail.setText("");
                        }
                    }
                });
    }
}