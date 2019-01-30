package com.cmarkandroidapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cmarkandroidapp.Interface.MyInterfaceClass;
import com.cmarkandroidapp.Orders.Order;
import com.cmarkandroidapp.Users.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    EditText orderTitleEt,orderDescEt;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseRef,userDatabaseRef;
    User userObject = new User();
    Order orderObject = new Order();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        orderTitleEt = findViewById(R.id.ordertitle);
        orderDescEt = findViewById(R.id.orderdesc);

        findViewById(R.id.order).setOnClickListener(this);
        mAuth= FirebaseAuth.getInstance();
        FirebaseUser fUser = mAuth.getCurrentUser();
        String uID = fUser.getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(uID);

        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userObject = dataSnapshot.getValue(User.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("HomeActivity", "loadPost:onCancelled", databaseError.toException());

            }
        });

    }

    public void CreateOrder() {

        orderObject.setUsername(userObject.getUserName());
        orderObject.setEmail(userObject.getEmail());
        orderObject.setOrderTitle(orderTitleEt.getText().toString());
        orderObject.setOrderDesc(orderDescEt.getText().toString());
        FirebaseUser fUser = mAuth.getCurrentUser();
        mDatabaseRef.child("orders").push().setValue(orderObject);
    }
    @Override
    public void onClick(View view) {
        int i=view.getId();
        if(i==R.id.order){
        CreateOrder();
            Toast.makeText(this,"Order created",Toast.LENGTH_SHORT).show();
        }
    }
}
