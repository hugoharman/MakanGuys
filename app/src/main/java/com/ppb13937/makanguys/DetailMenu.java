package com.ppb13937.makanguys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ppb13937.makanguys.apiclient.APIClient;
import com.ppb13937.makanguys.apiclient.MakanGuysInterface;
import com.ppb13937.makanguys.apiclient.Resto;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMenu extends AppCompatActivity {
    TextView namaMakanan,hargaMakanan,deskripsiMakanan;
    ImageView gambarMakanan;
    Button tambahKeranjang;
    ImageView imageBack;
    MakanGuysInterface makanGuysInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);
        namaMakanan = findViewById(R.id.namaItem_cart);
        hargaMakanan = findViewById(R.id.hargaMenu_detail);
        deskripsiMakanan = findViewById(R.id.deskripsiMenu_detail);
        tambahKeranjang = findViewById(R.id.btnTambahKeranjang_menu);
        gambarMakanan = findViewById(R.id.gambarMenu_detail);
        imageBack = findViewById(R.id.imageBack);
        makanGuysInterface = APIClient.getClient().create(MakanGuysInterface.class);
        Intent intent = getIntent();
        int restoID = intent.getIntExtra("restoID",0);
        int itemID = intent.getIntExtra("itemID",0);
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
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tambahKeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CartHelper.isSharedPreferencesExist(getApplicationContext())){
                    Log.d("menu value","Shared Preferences exist!");
                    if(CartHelper.getItemAmount(getApplicationContext(),itemID) != 1){
                        //update shared preference
                        DetailRestoMenu.updateCart(getApplicationContext(),restoID,itemID,1);
                        Log.d("menu value","Shared Preferences updated!");
                    }
                }
                else
                {
                    Log.d("menu value","Shared Preferences doesnt exist!");
                    DetailRestoMenu.saveCart(getApplicationContext(),restoID,itemID,1);
                }

                Call<List<Resto>> getResto = makanGuysInterface.getRestoByID(restoID);
                getResto.enqueue(new Callback<List<Resto>>() {
                    @Override
                    public void onResponse(Call<List<Resto>> call, Response<List<Resto>> response) {
                        ArrayList<Resto> listResto = (ArrayList<Resto>) response.body();
                        String namaResto = listResto.get(0).getName();
                        String alamat = listResto.get(0).getAddress();
                        Intent intent = new Intent(DetailMenu.this, DetailRestoMenu.class);
                        intent.putExtra("idResto", String.valueOf(restoID));
                        intent.putExtra("namaResto", namaResto);
                        intent.putExtra("alamatResto",alamat);
                        Log.d("namaResto",namaResto);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<List<Resto>> call, Throwable t) {

                    }
                });
            }
        });

    }
}