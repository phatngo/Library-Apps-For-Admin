package com.example.libraryadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class RegisterNewAccountActivity extends AppCompatActivity {
TextView txtEmail_RegisterNewAccountActivity, txtPassword_RegisterNewAccountActivity;
Button btnRegisterAccount_RegisterNewAccountActivity;
FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_account);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnRegisterAccount_RegisterNewAccountActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtEmail_RegisterNewAccountActivity.getText().toString().length() == 0 || txtPassword_RegisterNewAccountActivity.getText().toString().length() == 0) {
                    Toast.makeText(RegisterNewAccountActivity.this, "Account name or password cannot be blank!", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(txtEmail_RegisterNewAccountActivity.getText().toString(),
                            txtPassword_RegisterNewAccountActivity.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendVerificationEmail();
                                Toast.makeText(RegisterNewAccountActivity.this, "Verification Email Sent!", Toast.LENGTH_SHORT).show();
                                txtEmail_RegisterNewAccountActivity.setText("");
                                txtPassword_RegisterNewAccountActivity.setText("");
                                Intent intent=new Intent(RegisterNewAccountActivity.this,LoginActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(RegisterNewAccountActivity.this, "Account Already Existed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void addControls() {
        txtEmail_RegisterNewAccountActivity=findViewById(R.id.txtEmail_RegisterNewAccountActivity);
        txtPassword_RegisterNewAccountActivity=findViewById(R.id.txtPassword_RegisterNewAccountActivity);
        btnRegisterAccount_RegisterNewAccountActivity=findViewById(R.id.btnRegisterAccount_RegisterNewAccountActivity);
        firebaseAuth=FirebaseAuth.getInstance();
    }


    private void sendVerificationEmail() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Email sent.");}
                else{
                    Log.d(TAG, "failed");
                }
            }
        });
    }}