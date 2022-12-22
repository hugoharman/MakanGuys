package com.ppb13937.makanguys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.URL;

public class DetailMenu extends AppCompatActivity {
    TextView namaMakanan,hargaMakanan,deskripsiMakanan;
    ImageView gambarMakanan;
    Button tambahKeranjang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);
        namaMakanan = findViewById(R.id.namaItem_cart);
        hargaMakanan = findViewById(R.id.hargaMenu_detail);
        deskripsiMakanan = findViewById(R.id.deskripsiMenu_detail);
        tambahKeranjang = findViewById(R.id.btnTambahKeranjang_menu);
        gambarMakanan = findViewById(R.id.gambarMenu_detail);
        Intent intent = getIntent();
        String nama = intent.getStringExtra("namaMenu");
        String harga = intent.getStringExtra("hargaMenu");
        String deskripsi = intent.getStringExtra("deskripsiMenu");
        String image_url = intent.getStringExtra("gambarMenu");
        namaMakanan.setText(nama);
        hargaMakanan.setText(harga);

        deskripsiMakanan.setText(deskripsi);
        try {
            URL url = new URL(image_url);
            Glide.with(this)
                    .load(url)
                    .into(gambarMakanan);
        } catch (Exception e) {

        }
    }
}