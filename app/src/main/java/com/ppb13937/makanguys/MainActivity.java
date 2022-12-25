package com.ppb13937.makanguys;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ppb13937.makanguys.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        user = FirebaseAuth.getInstance().getCurrentUser();
        replaceFragment(new FragmentHomepage());

        Intent intent = getIntent();
        String message = intent.getStringExtra("fragment");
        if(message != null){
            if(message.equals("cart")){
                activityMainBinding.bottomNavigationView.setSelectedItemId(R.id.cart);
                replaceFragment(new FragmentCart());
            }
            else if(message.equals("history")){
                activityMainBinding.bottomNavigationView.setSelectedItemId(R.id.riwayat);
                replaceFragment(new FragmentHistory());
            }
        }

        activityMainBinding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_utama:
                    replaceFragment(new FragmentHomepage());
                    break;
                case R.id.cart:
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
        //Do nothing
    }
}
