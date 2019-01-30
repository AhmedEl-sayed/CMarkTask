package com.cmarkandroidapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmarkandroidapp.Interface.MyInterfaceClass;
import com.cmarkandroidapp.Users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,MyInterfaceClass{

    private EditText userNameEditText,emailEditText,passwordEditText;
    private TextView haveAccTv;
    private Button registerBtn;
    private FirebaseAuth fAuth;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Views
        userNameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.mail);
        passwordEditText = findViewById(R.id.password);
        findViewById(R.id.haveacc).setOnClickListener(this);
        findViewById(R.id.btn).setOnClickListener(this);

        fAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");


    }

    @Override
    public void CreateAccount(String email, String password) {
        if(!Validation()){
            return;
        }
        fAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User();
                            user.setUserName(userNameEditText.getText().toString());
                            user.setEmail(emailEditText.getText().toString());

                            FirebaseUser fUser = fAuth.getCurrentUser();
                            String uID = fUser.getUid();
                            mDatabaseReference.child(uID).setValue(user);

                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            intent.putExtra("username",userNameEditText.getText().toString());
                            intent.putExtra("email",emailEditText.getText().toString());
                            startActivity(intent);
                        }
                        else {
                            Log.w("CreateUserFaild",task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public boolean Validation() {
        boolean valid = true;

        String userName = userNameEditText.getText().toString();
        if(TextUtils.isEmpty(userName)){
            userNameEditText.setError("Username Reuires");
            userNameEditText.getHint();
            valid=false;
        } else {
            userNameEditText.setError(null);
        }
        String email = emailEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email Required.");
            userNameEditText.getHint();
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        String password = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password Required.");
            userNameEditText.getHint();
            valid = false;
        } else {
            passwordEditText.setError(null);
        }


        return valid;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        switch (i){
            case R.id.btn:
                CreateAccount(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());

                break;

            case R.id.haveacc:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
           }

    }
}
