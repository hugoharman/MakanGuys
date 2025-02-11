package com.ppb13937.makanguys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ppb13937.makanguys.apiclient.APIClient;
import com.ppb13937.makanguys.apiclient.MakanGuysInterface;
import com.ppb13937.makanguys.apiclient.MenuMakanan;
import com.ppb13937.makanguys.apiclient.Resto;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRestoMenu extends AppCompatActivity {
    private static final String SHARED_PREFS_CART = "makanGuysCart";
    TextView namaResto,alamatResto;
    AdapterMenu adapter;
    static MakanGuysInterface makanGuysInterface;
    RecyclerView rv_menu;
    @SuppressLint("StaticFieldLeak")
    static LinearLayout ll_checkout;
    @SuppressLint("StaticFieldLeak")
    static TextView namaResto_cart;
    @SuppressLint("StaticFieldLeak")
    static TextView hargaItem_cart;
    @SuppressLint("StaticFieldLeak")
    static TextView jumlahItem_cart;
    public String nama;
    public String alamat;
    public static ArrayList<Cart> listCart;
    public ImageView imageLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_resto_menu);
        namaResto = findViewById(R.id.namaDetailResto);
        alamatResto = findViewById(R.id.alamatDetailResto);
        makanGuysInterface = APIClient.getClient().create(MakanGuysInterface.class);
        rv_menu = findViewById(R.id.rv_menu);
        ll_checkout = findViewById(R.id.ll_checkout);
        namaResto_cart = findViewById(R.id.restoName_cartPreview);
        hargaItem_cart = findViewById(R.id.itemPrice_cartPreview);
        jumlahItem_cart = findViewById(R.id.itemAmount_cartPreview);
        imageLocation = findViewById(R.id.imageLocation);
        Intent intent = getIntent();
        String idResto = intent.getStringExtra("idResto");
        String nama = intent.getStringExtra("namaResto");
        String alamat = intent.getStringExtra("alamatResto");
        String location = intent.getStringExtra("locationResto");


        Log.d("namaResto",nama);
        Log.d("alamatResto",alamat);

        rv_menu.setLayoutManager(new LinearLayoutManager(this));
        namaResto.setText(nama);
        alamatResto.setText(alamat);
        getAllMenu(Integer.parseInt(idResto));
        if(CartHelper.isSharedPreferencesExist(this)){
            Log.d("detail","shared preferences exist!");
            loadCart(this);
        }else {
            Log.d("detail", "shared preferences doesnt exist!");
        }
        if(ll_checkout.getVisibility() == View.VISIBLE) {
            ll_checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailRestoMenu.this, MainActivity.class);
                    intent.putExtra("fragment", "cart");
                    startActivity(intent);
                }
            });
        }
    imageLocation.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("location","clicked");
            if (location != null) {
                Log.d("location", location);
                String[] parts = location.split(",");
                double longitude = Double.parseDouble(parts[0]);
                double latitude = Double.parseDouble(parts[1]);
                try {
                    Uri map = Uri.parse("google.navigation:q=" + longitude + "," + latitude + "&mode=b");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, map);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                } catch (ActivityNotFoundException e) {
                    // If the Google Maps app is not installed, open the location in a web browser
                    String url = "https://www.google.com/maps?q=" + longitude + "," + latitude;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }

            }
        }
    });
    }


    private void getAllMenu(int id){
        Call<List<MenuMakanan>> getRestoMenu = makanGuysInterface.getRestoMenuByID(id);

        getRestoMenu.enqueue(new Callback<List<MenuMakanan>>() {
            @Override
            public void onResponse(Call<List<MenuMakanan>> call, Response<List<MenuMakanan>> response) {
                ArrayList<MenuMakanan> listMenu = (ArrayList<MenuMakanan>) response.body();

                adapter = new AdapterMenu(listMenu,nama,alamat);
                rv_menu.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<MenuMakanan>> call, Throwable t) {

            }
        });

    }

    public static void saveCart(Context context,int idResto, int idItem, int amountItem){
        CartHelper.saveCart(context,idResto,idItem,amountItem,listCart);
        FragmentCart.totalHarga = 0;
        FragmentCart.updateTotalPembayaran(context);
    }
    public static void loadCart(Context pls){
        listCart = CartHelper.loadCart(pls);
        if(listCart == null) return;
        if (listCart.size() == 0) {
            CartHelper.clearCart(pls);
            ll_checkout.setVisibility(View.GONE);
            if(listCart != null) {
                listCart.clear();
            }
            return;
        }
        int idResto = listCart.get(0).getIDResto();
        Call<List<Resto>> getResto = makanGuysInterface.getRestoByID(idResto);
        getResto.enqueue(new Callback<List<Resto>>() {
            @Override
            public void onResponse(Call<List<Resto>> call, Response<List<Resto>> response) {
                ArrayList<Resto> listResto = (ArrayList<Resto>) response.body();
                String namaResto = listResto.get(0).getName();
                namaResto_cart.setText(namaResto);
            }

            @Override
            public void onFailure(Call<List<Resto>> call, Throwable t) {

            }
        });

        int itemTotal = 0;
        int priceTotal = 0;
        for (int i = 0; i < listCart.size(); i++) {
            int idItem = listCart.get(i).getItemID();
            int amountItem = listCart.get(i).getAmount();

            Call<List<MenuMakanan>> getRestoMenu = makanGuysInterface.getRestoMenuByID(idResto);

            try {
                Response<List<MenuMakanan>> response = getRestoMenu.execute();
                ArrayList<MenuMakanan> listMenu = (ArrayList<MenuMakanan>) response.body();
                int priceItem = Integer.parseInt(listMenu.get(0).getPrice());
                itemTotal += amountItem;
                priceTotal += priceItem * amountItem;
            } catch (Exception e) {

            }
        }

        // Set the TextView values after the loop has completed4
        if(itemTotal == 0){
            ll_checkout.setVisibility(View.GONE);
        }
        if(itemTotal < 1) {
            jumlahItem_cart.setText(String.valueOf(itemTotal) + " item");
        }else {
            jumlahItem_cart.setText(String.valueOf(itemTotal) + " items");
        }
        hargaItem_cart.setText(String.valueOf(priceTotal));

        ll_checkout.setVisibility(LinearLayout.VISIBLE);
        if(ll_checkout.getVisibility() == View.VISIBLE) {
            ll_checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(pls, MainActivity.class);
                    intent.putExtra("fragment", "cart");
                    pls.startActivity(intent);
                }
            });
        }

    }
    public static void removeFromCart(Context context,int idResto, int idItem){
        CartHelper.removeFromCart(context,idResto,idItem,listCart);
        FragmentCart.totalHarga = 0;
        FragmentCart.updateTotalPembayaran(context);

    }
    public static void updateCart(Context pls,int idResto, int idItem, int amountItem){

        CartHelper.updateCart(pls,idResto,idItem,amountItem,listCart);
        FragmentCart.totalHarga = 0;
        FragmentCart.updateTotalPembayaran(pls);
    }

}