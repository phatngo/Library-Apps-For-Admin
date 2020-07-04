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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    TextView txtEmail_ChangePassword, txtCurrentPassword_ChangePassword, txtNewPassword_ChangePassword;
    Button btnChangePassword_ChangePassword;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        addControls();
        addEvents();
    }
    private void addEvents() {
btnChangePassword_ChangePassword.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        auth.signInWithEmailAndPassword(txtEmail_ChangePassword.getText().toString(),txtCurrentPassword_ChangePassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    user.updatePassword(txtNewPassword_ChangePassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ChangePasswordActivity.this,"Password Changed!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(ChangePasswordActivity.this,"Wrong Password",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
});
    }
    private void addControls() {
            txtEmail_ChangePassword = findViewById(R.id.txtEmail_ChangePassword);
            txtEmail_ChangePassword.setText(user.getEmail());
            txtCurrentPassword_ChangePassword = findViewById(R.id.txtCurrentPassword_ChangePassword);
            txtNewPassword_ChangePassword = findViewById(R.id.txtNewPassword_ChangePassword);
            btnChangePassword_ChangePassword = findViewById(R.id.btnChangePassword_ChangePassword);
        }
    }
