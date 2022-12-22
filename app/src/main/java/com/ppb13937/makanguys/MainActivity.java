package com.ppb13937.makanguys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ppb13937.makanguys.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        replaceFragment(new FragmentHomepage());
        Intent intent = getIntent();
        String message = intent.getStringExtra("fragment");
        if(message != null){
            if(message.equals("cart")){
                activityMainBinding.bottomNavigationView.setSelectedItemId(R.id.cari);
                replaceFragment(new FragmentCart());
            }
            else if(message.equals("history")){
                activityMainBinding.bottomNavigationView.setSelectedItemId(R.id.riwayat);
                replaceFragment(new FragmentHistory());
            }
        }
        /*
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.isEmailVerified()) {
            replaceFragment(new FragmentHomepage());
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Untuk Menggunakan E-Library, Anda diharuskan untuk melakukan verifikasi Email terlebih dahulu\n\n" +
                            "Klik Oke untuk mendapatkan Email Verifikasi")
                    .setTitle("Verifikasi Email");
            builder.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    user.sendEmailVerification();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    Toast.makeText(MainActivity.this, "Silahkan cek email anda untuk verifikasi!", Toast.LENGTH_LONG).show();
                }
            });
            //alertDialogMain = builder.create();
            //alertDialogMain.show();
        }
        */
        activityMainBinding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_utama:
                    replaceFragment(new FragmentHomepage());
                    break;
                case R.id.cari:
                    replaceFragment(new FragmentCart());
                    break;
                case R.id.riwayat:
                    replaceFragment(new FragmentHistory());
                    break;
                case R.id.profil:
                    replaceFragment(new FragmentProfile());
                    break;
            }

            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_layout, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {

    }
}
