package com.cmarkandroidapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cmarkandroidapp.Orders.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewOrderActivity extends AppCompatActivity {

    DatabaseReference mDatabaseRef;
    ListView listView;
    Order order = new Order();
    List<String> orderList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        listView = findViewById(R.id.listv);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("orders");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void showData(DataSnapshot dataSnapshot){
        for(DataSnapshot ds:dataSnapshot.getChildren()){
            order = ds.getValue(Order.class);
             orderList = new ArrayList<>();
            orderList.add(order.getUsername());
            orderList.add(order.getEmail());
            orderList.add(order.getOrderTitle());
            orderList.add(order.getOrderDesc());
        }
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,orderList);
        listView.setAdapter(adapter);
    }
}
