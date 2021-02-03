package com.example.cbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class home extends AppCompatActivity {
    Button torecbtn,upload;
    EditText name,amount,homepin;
    FirebaseUser using;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userid;
    String  gpin;
    String ctime,cdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        torecbtn=findViewById(R.id.rcbtn);
        fAuth = FirebaseAuth.getInstance();
        using=fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        homepin=findViewById(R.id.rcpin);

        userid = fAuth.getCurrentUser().getUid();
        upload=findViewById(R.id.uploadbtn);
        name=findViewById(R.id.rcname);
        amount=findViewById(R.id.rcamount);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(name.getText().toString())){
                    name.setError("field required");
                    return;
                }
                if(TextUtils.isEmpty(amount.getText().toString())){
                    amount.setError("field required");
                    return;
                }
                if(TextUtils.isEmpty(homepin.getText().toString())){
                    homepin.setError("field required");
                    return;
                }

                String userpin = gpin;

                if(!homepin.getText().toString().equals(userpin)){
                  //  Toast.makeText(home.this, gpin, Toast.LENGTH_SHORT).show();
                    Toast.makeText(home.this, "ERROR IN UPLOAD PIN", Toast.LENGTH_SHORT).show();
                 return;
                }

                String fname=name.getText().toString();
                String famount=amount.getText().toString();
                String all=fname+famount;

                Calendar calendar= Calendar.getInstance();
                SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd,yyyy");
                cdate =currentdate.format(calendar.getTime());

                SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
                ctime =currenttime.format(calendar.getTime());
                String key= ctime+cdate;

                DocumentReference documentReference = fStore.collection("receits").document("rc").collection(userid).document(key);


                Map<String,Object> user =new HashMap<>();
                user.put("name",fname);
                user.put("amount",famount);
                user.put("time",ctime);
                user.put("date",cdate);
                user.put("key",key);
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Toast.makeText(home.this, "Document stored", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(home.this, "Error in storing Document", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        checkpin();
        torecbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),recets.class));
            }
        });

    }

    private void checkpin() {

        final DocumentReference documentReference = fStore.collection("users").document(userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String ptake=documentSnapshot.getString("uploadpin");
              gpin=ptake;


            }
        });



    }
}