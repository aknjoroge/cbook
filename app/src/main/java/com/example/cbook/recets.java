package com.example.cbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class recets extends AppCompatActivity {
    public RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseUser using;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String rckey;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recets);


        fAuth = FirebaseAuth.getInstance();
        recyclerView=findViewById(R.id.recyclermenucart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        using=fAuth.getCurrentUser();

        fStore = FirebaseFirestore.getInstance();
        userid = fAuth.getCurrentUser().getUid();


    }
    public void oncartclick(){

        android.app.AlertDialog dialog = new AlertDialog.Builder(this,R.style.AlertDialogStyle)
                .setTitle("Opptions")
                .setMessage("Remove Receit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fStore.collection("receits").document("rc").collection(userid).document(rckey)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(recets.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(recets.this, "Error deleting document", Toast.LENGTH_SHORT).show();
                                    }
                                });


                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirestoreRecyclerOptions<forcart> options = new FirestoreRecyclerOptions.Builder<forcart>()
                .setQuery(fStore.collection("receits").document("rc").collection(userid),forcart.class).build();
        FirestoreRecyclerAdapter<forcart,cartviewholder> adapter= new FirestoreRecyclerAdapter<forcart, cartviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull cartviewholder holder, int position, @NonNull final forcart model) {

                holder.txtpname.setText("Name :"+model.getName()+".");
                holder.txttime.setText("Time: "+model.getTime());
                holder.txtpamount.setText(model.getAmount()+"/=");
                holder.txtdate.setText("Date: "+model.getDate());
                rckey=model.getKey();

                holder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        oncartclick();
                    }
                });
            }

            @NonNull
            @Override
            public cartviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cartitem,parent,false);
                cartviewholder holder =new cartviewholder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }




}