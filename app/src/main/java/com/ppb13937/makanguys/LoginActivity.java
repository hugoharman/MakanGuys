package com.ppb13937.makanguys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private TextView register, lupaPassword;
    private EditText editUsername, editPassword;
    private Button login;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register = findViewById(R.id.txtViewDaftar);
        lupaPassword = findViewById(R.id.txtViewLupa);
        editUsername = findViewById(R.id.editEmailLogin);
        editPassword = findViewById(R.id.editPasswordLogin);
        login = findViewById(R.id.buttonLogin);
        //Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

        lupaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,ResetPasswordActivity.class);
                startActivity(intent);

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateEmail() || !validatePassword()){
                 }else{
                    authenticateUser();
                }
            }
        });
    }

    public Boolean validateEmail(){
        String val  = editUsername.getText().toString();
        if(val.isEmpty()){
            editUsername.setError("Email tidak boleh kosong!");
            return false;
        }else{
            editUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String val  = editPassword.getText().toString();
        if(val.isEmpty()){
            editPassword.setError("Password tidak boleh kosong!");
            return false;
        }
        else{
            editPassword.setError(null);
            return true;
        }
    }

    private void authenticateUser(){
        String userUsername = editUsername.getText().toString().trim();
        String userPassword = editPassword.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(userUsername, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent =  new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this,"Authentication Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}