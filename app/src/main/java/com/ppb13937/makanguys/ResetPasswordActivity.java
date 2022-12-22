package com.ppb13937.makanguys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText editEmail;
    private Button reset;
    private TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        editEmail = findViewById(R.id.editEmailForgot);
        reset = findViewById(R.id.buttonReset);
        login = findViewById(R.id.txtViewLoginForgot);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResetPasswordActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}