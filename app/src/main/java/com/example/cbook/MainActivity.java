package com.example.cbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button forlogin;
    EditText email,pass;
    String  takeemail,takepass;
    TextView signs;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signs=findViewById(R.id.signuptxt);
        signs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),signup.class));
            }
        });



        forlogin=findViewById(R.id.tohomebtn);
        progressBar=findViewById(R.id.progressBar2);
        email=findViewById(R.id.loginemail);
        pass=findViewById(R.id.loginpass);

        takeemail=email.getText().toString();
        takepass=pass.getText().toString();

        fAuth = FirebaseAuth.getInstance();


        forlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(takeemail=email.getText().toString())){
                    email.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty( takepass=pass.getText().toString())){
                    pass.setError("password required");
                    return;
                }
                takepass=pass.getText().toString();
                if(takepass.length()< 6 ){
                    pass.setError("password need to be above 6 characters");
                }
                progressBar.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(takeemail,takepass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),home.class));
                        }else {

                            Toast.makeText(MainActivity.this, "An error occured"
                                    +task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);

                        }

                    }
                });
            }
        });
    }
}