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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener ,MyInterfaceClass{

    private EditText emailET,passwordET;
    private Button loginBtn;
    private TextView loginTxt;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.pass);
        findViewById(R.id.loginbtn).setOnClickListener(this);
        findViewById(R.id.signup).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        int i=view.getId();
        switch (i){
            case R.id.loginbtn:
                CreateAccount(emailET.getText().toString(),
                        passwordET.getText().toString());
                break;

            case R.id.signup:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
        }
    }

    @Override
    public void CreateAccount(String email, String password) {
        if(!Validation()){
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Log.w("LoginFaild",task.getException());
                            Toast.makeText(LoginActivity.this,"Login Faild",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean Validation() {
        boolean valid=true;
        String mail = emailET.getText().toString();
        String pass = passwordET.getText().toString();
        if(TextUtils.isEmpty(mail)){
            emailET.setError("Username Required");
            emailET.getHint();
            valid=false;
        }
        if(TextUtils.isEmpty(pass)){
            passwordET.setError("Password Required");
            passwordET.getHint();
            valid=false;
        }
        return valid;
    }
}
