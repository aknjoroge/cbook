package com.example.cbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {
    Button signin,signup;
    EditText name,phone,email,pass,aapin;
    String takename,takephone,takemail,takepass,takepin;
    ProgressBar progressBar;
    String userid;
    private FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        name=findViewById(R.id.regname);
        phone=findViewById(R.id.regphone);
        email=findViewById(R.id.regmail);
        pass=findViewById(R.id.regpass);
        aapin=findViewById(R.id.regpin);
        progressBar=findViewById(R.id.progressBar);
        takename=name.getText().toString();
        takephone=phone.getText().toString();
        takemail=email.getText().toString();
        takepin =aapin.getText().toString();


        takepass=pass.getText().toString();
        signup=findViewById(R.id.signupbtn);

        signin =findViewById(R.id.tosignin);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takename=name.getText().toString();
                takephone=phone.getText().toString();
                if(TextUtils.isEmpty(takemail=email.getText().toString())){
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
                if(TextUtils.isEmpty(takepin=aapin.getText().toString())){
                    aapin.setError("Enter an upload pin");
                    return;

                }
                progressBar.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(takemail,takepass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser fuser =fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(signup.this, "Verification Email Sent ", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(signup.this, "Error sending verification , Contact Admin", Toast.LENGTH_SHORT).show();
                                }
                            });


                            Toast.makeText(signup.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                            userid= fAuth.getCurrentUser().getUid();

                            DocumentReference documentReference = fStore.collection("users").document(userid);

                            Map<String, Object> user = new HashMap<>();
                            user.put("name", takename);
                            user.put("pass",takepass);
                            user.put("phone", takephone);
                            user.put("mail", takemail);
                            user.put("uploadpin", takepin);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void avoid) {
                                    Toast.makeText(signup.this, "user profile created", Toast.LENGTH_SHORT).show();


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(signup.this, "Failed create user", Toast.LENGTH_SHORT).show();
                                }
                            });
                            progressBar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else {
                            Toast.makeText(signup.this, "An error occured" +task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });


            }
        });


    }




}