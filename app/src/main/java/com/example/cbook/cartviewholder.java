package com.example.cbook;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class cartviewholder extends  RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtpname,txttime,txtpamount,txtdate;
    itemclicklistener listener;

    public cartviewholder(@NonNull View itemView) {
        super(itemView);
        txttime=itemView.findViewById(R.id.cartproducttoataltime);
        txtpname = itemView.findViewById(R.id.cartproductname);
        txtpamount = itemView.findViewById(R.id.cartproductamount);
        txtdate=itemView.findViewById(R.id.cartproductdate);



    }
    public void setItemClickListener(itemclicklistener listener){
        this.listener =listener;
    }

    @Override
    public void onClick(View v) {
        listener.onclick(v,getAdapterPosition(),false);

    }
}
