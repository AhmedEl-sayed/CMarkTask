package com.cmarkandroidapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cmarkandroidapp.Users.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    String username,email,userId;
    DatabaseReference mDatabaseReference;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseAuth mAuth;
    FirebaseUser fbUser;
    TextView usernameTv,emailTv;
    User userObject=new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.logout).setOnClickListener(this);
        findViewById(R.id.addbtn).setOnClickListener(this);
        findViewById(R.id.view).setOnClickListener(this);
        usernameTv = findViewById(R.id.namee);
        emailTv = findViewById(R.id.emaill);
        mAuth=FirebaseAuth.getInstance();
        fbUser = mAuth.getCurrentUser();
         userId= fbUser.getUid();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userObject = dataSnapshot.getValue(User.class);
                usernameTv.setText(userObject.getUserName());
                emailTv.setText(userObject.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("HomeActivity", "loadPost:onCancelled", databaseError.toException());

            }
        });

    }



    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View view) {
        int i=view.getId();
        if(i==R.id.logout){

            mAuth.signOut();
            fbUser = mAuth.getCurrentUser();
            if(fbUser!=null){
                Toast.makeText(this,"user still exit",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this,"Logout done successfully",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
        if(i==R.id.addbtn){
            Intent intent = new Intent(this,OrderActivity.class);
            startActivity(intent);
        }
        if(i==R.id.view){
            Intent intent = new Intent(this,ViewOrderActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListner!=null){
            mAuth.removeAuthStateListener(mAuthListner);
        }
    }
}
