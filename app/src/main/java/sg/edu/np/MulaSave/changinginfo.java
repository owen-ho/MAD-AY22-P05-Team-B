package sg.edu.np.MulaSave;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class changinginfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changingemail);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        EditText username = findViewById(R.id.changeuser);
        EditText email = findViewById(R.id.changeemail);
        EditText password = findViewById(R.id.changepass);
        Button confirmation = findViewById(R.id.confirmation);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference userRef = database.getReference("user");

        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user!=null){
                    if(!username.getText().toString().equals("")){
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
                    if (!email.getText().toString().equals("")){
                        //update email in firebase auth
                        user.updateEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    if(!password.getText().toString().equals("")){
                                        changePassword(user,password.getText().toString());
                                    }
                                    //Toast.makeText(changinginfo.this, "email updated", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

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
                    }
                    else if (!password.getText().toString().equals("")){
                        changePassword(user,password.getText().toString());
                    }
                }
                Intent intent = new Intent(changinginfo.this,MainActivity.class);
                intent.putExtra("frgToLoad",3); //Profile frag is the 4th fragment
                startActivity(intent);
            }
        });
    }
    private void changePassword(FirebaseUser user, String newPassword){
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