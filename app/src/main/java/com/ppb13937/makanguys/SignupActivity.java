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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText editUsername,editNama,editEmail,editPassword,editPasswordKonfirmasi,editNomorTelephone,editAlamat;
    private Button btnDaftar;
    private TextView txtLogin;

    private FirebaseAuth mAuth;

    FirebaseDatabase  database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        editNama = findViewById(R.id.editEmailLogin);
        editEmail = findViewById(R.id.editEmailForgot);
        editNomorTelephone = findViewById(R.id.editNomorDaftar);
        editPassword = findViewById(R.id.editPasswordDaftar);
        editPasswordKonfirmasi = findViewById(R.id.editPasswordKonfirmasiDaftar);
        btnDaftar = findViewById(R.id.buttonReset);
        txtLogin = findViewById(R.id.txtViewLoginForgot);
        editAlamat = findViewById(R.id.editAlamatDaftar);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(SignupActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
                /*
                database =  FirebaseDatabase.getInstance();
                reference  =  database.getReference("users");
                String username = editUsername.getText().toString();
                String name = editNama.getText().toString();
                String email = editEmail.getText().toString();
                String phoneNumber = editNomorTelephone.getText().toString();
                String password = editPassword.getText().toString();

                HelperClass helperClass = new HelperClass(username,name,email,password,phoneNumber);
                reference.child(name).setValue(helperClass);

                Toast.makeText(SignupActivity.this,"Akun berhasil didaftarkan!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
                */
            }
        });
    }

    private void registerUser(){
        String name = editNama.getText().toString();
        String email = editEmail.getText().toString();
        String phoneNumber = editNomorTelephone.getText().toString();
        String password = editPassword.getText().toString();
        String address = editAlamat.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            HelperClass helperClass = new HelperClass(name,email,password,phoneNumber,address);
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(helperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            showMainActivity();
                                        }
                                    });
                        } else {
                            Toast.makeText(SignupActivity.this,"Auth failed!",
                            Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void showMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}