package com.ppb13937.makanguys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText editEmail;
    private Button resetBtn;
    private TextView login;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        editEmail = findViewById(R.id.editEmailForgot);
        resetBtn = findViewById(R.id.btnResetPassword);
        login = findViewById(R.id.txtViewLoginForgot);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editEmail.getText().toString().isEmpty()){
                    editEmail.setError("Email tidak boleh kosong");
                    editEmail.requestFocus();
                    return;
                }
                String email = editEmail.getText().toString();

                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ResetPasswordActivity.this, "Silahkan cek email anda", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ResetPasswordActivity.this, "Gagal mengirim email", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });



    }
}