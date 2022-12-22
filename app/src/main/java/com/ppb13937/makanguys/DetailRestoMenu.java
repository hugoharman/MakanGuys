package com.ppb13937.makanguys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ppb13937.makanguys.apiclient.APIClient;
import com.ppb13937.makanguys.apiclient.MakanGuysInterface;
import com.ppb13937.makanguys.apiclient.MenuMakanan;
import com.ppb13937.makanguys.apiclient.Resto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    static LinearLayout ll_checkout;
    static TextView namaResto_cart;
    static TextView hargaItem_cart;
    static TextView jumlahItem_cart;
    public static ArrayList<Cart> listCart;

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
        Intent intent = getIntent();
        String idResto = intent.getStringExtra("idResto");
        String nama = intent.getStringExtra("namaResto");
        String alamat = intent.getStringExtra("alamatResto");
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

    }

    private void getAllMenu(int id){
        Call<List<MenuMakanan>> getRestoMenu = makanGuysInterface.getRestoMenuByID(id);

        getRestoMenu.enqueue(new Callback<List<MenuMakanan>>() {
            @Override
            public void onResponse(Call<List<MenuMakanan>> call, Response<List<MenuMakanan>> response) {
                ArrayList<MenuMakanan> listMenu = (ArrayList<MenuMakanan>) response.body();

                adapter = new AdapterMenu(listMenu);
                rv_menu.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<MenuMakanan>> call, Throwable t) {

            }
        });

    }

    public static void saveCart(Context context,int idResto, int idItem, int amountItem){
        CartHelper.saveCart(context,idResto,idItem,amountItem,listCart);
    }
    public static void loadCart(Context pls){
        Log.d("hi","load cart");
        listCart = CartHelper.loadCart(pls);
        if (listCart.size() == 0) {
            ll_checkout.setVisibility(View.INVISIBLE);
            if(listCart != null) {
                listCart.clear();
            }
            return;
        }
        else{
            Log.d("hi","data loaded!");
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

            //get price of Item using API
            Call<List<MenuMakanan>> getRestoMenu = makanGuysInterface.getRestoMenuByID(idResto);

            try {
                // Make the synchronous API call
                Response<List<MenuMakanan>> response = getRestoMenu.execute();
                ArrayList<MenuMakanan> listMenu = (ArrayList<MenuMakanan>) response.body();
                int priceItem = Integer.parseInt(listMenu.get(0).getPrice());
                itemTotal += amountItem;
                priceTotal += priceItem * amountItem;
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }

        // Set the TextView values after the loop has completed4
        if(itemTotal == 0){
            ll_checkout.setVisibility(View.INVISIBLE);
        }
        if(itemTotal < 1) {
            jumlahItem_cart.setText(String.valueOf(itemTotal) + " item");
        }else {
            jumlahItem_cart.setText(String.valueOf(itemTotal) + " items");
        }
        hargaItem_cart.setText(String.valueOf(priceTotal));

        ll_checkout.setVisibility(LinearLayout.VISIBLE);


    }
    public static void removeFromCart(Context context,int idResto, int idItem){
        CartHelper.removeFromCart(context,idResto,idItem,listCart);

    }
    public static void updateCart(Context pls,int idResto, int idItem, int amountItem){
        CartHelper.updateCart(pls,idResto,idItem,amountItem,listCart);

    }
}