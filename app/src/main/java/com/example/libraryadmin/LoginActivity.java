package com.example.libraryadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextView txtEmail_LoginActivity, txtPassword_LoginActivity;
    Button btnLogIn_LoginActivity, btnRegistration_LoginActivity, btnForgotPassword_LoginActivity;
FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addControls();
        addEvents();
    }

    private void addEvents() {
        //Login
        btnLogIn_LoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtEmail_LoginActivity.getText().toString().length()==0||txtPassword_LoginActivity.getText().toString().length()==0){
                    Toast.makeText(LoginActivity.this,"No Input Found!",Toast.LENGTH_SHORT).show();

                }else{
         firebaseAuth.signInWithEmailAndPassword(txtEmail_LoginActivity.getText().toString(),txtPassword_LoginActivity.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 if (task.isSuccessful()) {
                     Toast.makeText(LoginActivity.this, "Signed In Successfully!", Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                     startActivity(intent);
                 } else {
                     Toast.makeText(LoginActivity.this, "Signed In Failed!", Toast.LENGTH_SHORT).show();
                 }
             }

});}}

        });
        //Register
        btnRegistration_LoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterNewAccountActivity.class);
                startActivity(intent);
            }
        });
        //Forgot
        btnForgotPassword_LoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        firebaseAuth=FirebaseAuth.getInstance();
        txtEmail_LoginActivity=findViewById(R.id.txtEmail_LoginActivity);
        txtPassword_LoginActivity=findViewById(R.id.txtPassword_LoginActivity);
        btnLogIn_LoginActivity=findViewById(R.id.btnLogIn_LoginActivity);
        btnRegistration_LoginActivity=findViewById(R.id.btnRegistration_LoginActivity);
        btnForgotPassword_LoginActivity=findViewById(R.id.btnForgotPassword_LoginActivity);
    }
}
