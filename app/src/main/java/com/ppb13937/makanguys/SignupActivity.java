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
        btnDaftar = findViewById(R.id.btnResetPassword);
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
            }
        });
    }

    private void registerUser(){
        String name = editNama.getText().toString();
        String email = editEmail.getText().toString();
        String phoneNumber = editNomorTelephone.getText().toString();
        String password = editPassword.getText().toString();
        String secondaryPassword = editPasswordKonfirmasi.getText().toString();
        String address = editAlamat.getText().toString();
        if(name.isEmpty()){
            editNama.setError("Nama tidak boleh kosong");
            editNama.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editEmail.setError("Email tidak boleh kosong");
            editEmail.requestFocus();
            return;
        }
        if(phoneNumber.isEmpty()){
            editNomorTelephone.setError("Nomor telepon tidak boleh kosong");
            editNomorTelephone.requestFocus();
            return;
        }
        if(address.isEmpty()){
            editAlamat.setError("Alamat tidak boleh kosong");
            editAlamat.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editPassword.setError("Password tidak boleh kosong");
            editPassword.requestFocus();
            return;
        }
        if(secondaryPassword.isEmpty()){
            editPasswordKonfirmasi.setError("Konfirmasi password tidak boleh kosong");
            editPasswordKonfirmasi.requestFocus();
            return;
        }
        if(password.equals(secondaryPassword)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                HelperClass helperClass = new HelperClass(name, email, phoneNumber, address);
                                FirebaseDatabase.getInstance().getReference("users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(helperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                showMainActivity();
                                            }
                                        });
                            } else {
                                Toast.makeText(SignupActivity.this, "Auth failed!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(SignupActivity.this, "Password tidak sama!",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void showMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}