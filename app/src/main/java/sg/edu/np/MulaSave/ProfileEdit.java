package sg.edu.np.MulaSave;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ProfileEdit extends AppCompatActivity {

    public Boolean finishAct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changingemail);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        EditText username = findViewById(R.id.changeuser);
        EditText email = findViewById(R.id.changeemail);
        EditText password = findViewById(R.id.changepass);
        Button confirmation = findViewById(R.id.confirmation);
        ImageView back = findViewById(R.id.backButtonProfile);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference userRef = database.getReference("user");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAct = true; //condition to determine the activity finishes, if user inputs are valid, activity will finish
                if(user!=null){//ensure user is logged in
                    if(!username.getText().toString().equals("")){//check for changes in username
                        userRef.child(user.getUid()).child("username").setValue(username.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //Toast.makeText(changinginfo.this, "username updated", Toast.LENGTH_SHORT).show();
                                }else{
                                    try{
                                        throw Objects.requireNonNull(task.getException());
                                    }catch(Exception e){
                                        Log.e("username",e.toString());
                                    }
                                }
                            }
                        });
                    }
                    if (!email.getText().toString().equals("")){//check for changes in email
                        if ((Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())){//ensure format is email
                            //update email in firebase auth
                            user.updateEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if(!password.getText().toString().equals("")){
                                            changePassword(user,password.getText().toString());
                                            finishAct = false;//set the condition to false since there are errors with changes
                                        }
                                        //update email in realtime db
                                        userRef.child(user.getUid()).child("email").setValue(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    //Toast.makeText(changinginfo.this, "realtime db email updated", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    try{
                                                        throw Objects.requireNonNull(task.getException());
                                                    }catch(Exception e){
                                                        Log.e("email",e.toString());
                                                    }
                                                }
                                            }
                                        });
                                        //Toast.makeText(changinginfo.this, "email updated", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else{
                            //input is invalid
                            Toast.makeText(ProfileEdit.this,"Enter a valid email",Toast.LENGTH_SHORT).show();
                        }
                    }//end of change email
                    else if (!password.getText().toString().equals("")){//if user wants to change password
                        changePassword(user,password.getText().toString());
                    }
                }
                //ensure that there are fields changed
                if (!username.getText().toString().equals("") && !email.getText().toString().equals("") && !password.getText().toString().equals("")){
                    finishAct = true;
                }
                if (finishAct == true){
                    Toast.makeText(ProfileEdit.this, "Changes Updated!", Toast.LENGTH_SHORT).show();
                    finish();//To send the user back to the profile fragment after updating details
                }
                //no inputs at all
                if (username.getText().toString().equals("") && email.getText().toString().equals("") && password.getText().toString().equals("")){
                    Toast.makeText(ProfileEdit.this, "No changes found", Toast.LENGTH_SHORT).show();//toast message to notify no inputs
                }
            }
        });//end of onclick
    }
    private void changePassword(FirebaseUser user, String newPassword){//change password in firebase auth
        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(changinginfo.this, "password updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}