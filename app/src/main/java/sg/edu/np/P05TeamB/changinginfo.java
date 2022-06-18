package sg.edu.np.P05TeamB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        Button back = findViewById(R.id.back);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-ay22-p05-team-b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference userRef = database.getReference("user");




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(changinginfo.this,MainActivity.class);
                startActivity(intent);
            }
        });

        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(user!=null){
                    if(!user1.getText().toString().equals("")){
                        userRef.child(user.getUid()).child("username").setValue(user1.getText().toString()).addOnCompleteListener(changinginfo.this,new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "username updated", Toast.LENGTH_SHORT).show();

                                }else{
                                    try{

                                        throw task.getException();
                                    }catch(Exception e){
                                        Log.e("username",e.toString());
                                    }
                                }
                            }
                        });
                    }
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

                }

            }
        });
    }
}