package com.example.libraryadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
TextView txtEmail_ForgotPasswordActivity;
Button btnConfirm_ForgotPasswordActivity;
FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        firebaseAuth=FirebaseAuth.getInstance();
        txtEmail_ForgotPasswordActivity=findViewById(R.id.txtEmail_ForgotPasswordActivity);
        btnConfirm_ForgotPasswordActivity=findViewById(R.id.btnConfirm_ForgotPasswordActivity);
        btnConfirm_ForgotPasswordActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
firebaseAuth.sendPasswordResetEmail(txtEmail_ForgotPasswordActivity.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {
             if(task.isSuccessful()){
                 Toast.makeText(ForgotPasswordActivity.this,"Email sent!",Toast.LENGTH_SHORT).show();
             }else{
                 Toast.makeText(ForgotPasswordActivity.this,"Email not found!",Toast.LENGTH_SHORT).show();
             }
    }
});
            }
        });
    }
}
