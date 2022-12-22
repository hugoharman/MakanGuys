package com.ppb13937.makanguys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditUser extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email, nama, telp, alamat;
    private Button update_user;
    private ProgressBar progressBarEdit;
    ImageView iv_back;
    //user data
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.et_edit_email);
        nama = findViewById(R.id.et_edit_nama);
        telp = findViewById(R.id.et_edit_telp);
        alamat = findViewById(R.id.et_edit_alamat);
        update_user = findViewById(R.id.btn_update_user);
        progressBarEdit = findViewById(R.id.progressBarUpdate);
        progressBarEdit.setVisibility(View.GONE);

        iv_back = findViewById(R.id.back_btn);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditUser.this, MainActivity.class));
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        userID = user.getUid();


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    String nm = userProfile.name;
                    String em = userProfile.email;
                    String tlp = userProfile.phonenumber;
                    String almt = userProfile.address;

                    email.setText(em);
                    nama.setText(nm);
                    telp.setText(tlp);
                    alamat.setText(almt);

                }else {
                    email.setText("");
                    nama.setText("");
                    telp.setText("");
                    alamat.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditUser.this, "Gagal Mengambil Data User", Toast.LENGTH_LONG).show();
            }
        });

        update_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBarEdit.setVisibility(View.VISIBLE);
                updateUser();
            }
        });


    }

    private void updateUser(){
        String defEmail = email.getText().toString();
        String newNama = nama.getText().toString();
        String newTelp = telp.getText().toString();
        String newAlamat = alamat.getText().toString();

        User newUserData = new User(defEmail, newNama, newTelp, newAlamat);

        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(newUserData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressBarEdit.setVisibility(View.GONE);
                            Toast.makeText(EditUser.this, "Berhasil Memperbarui Data User", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            progressBarEdit.setVisibility(View.GONE);
                            Toast.makeText(EditUser.this, "Gagal Memperbarui Data User", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}