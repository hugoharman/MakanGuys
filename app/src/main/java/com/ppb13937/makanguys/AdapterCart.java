package com.ppb13937.makanguys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ppb13937.makanguys.apiclient.APIClient;
import com.ppb13937.makanguys.apiclient.MakanGuysInterface;
import com.ppb13937.makanguys.apiclient.MenuMakanan;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.ViewHolder> {

    ArrayList<Cart> listCart;
    MakanGuysInterface makanGuysInterface;

    public AdapterCart(ArrayList<Cart> listCart) {
        this.listCart = listCart;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.template_rv_cart, parent, false));
        return holder;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        makanGuysInterface = APIClient.getClient().create(MakanGuysInterface.class);
        Context context = holder.itemView.getContext();
        Cart keranjang = listCart.get(position);
        //get nama item using API
        Call<List<MenuMakanan>> getMenu = makanGuysInterface.getMenu(keranjang.getItemID());
        Log.d("itemID", String.valueOf(keranjang.getItemID()));
        Log.d("getRestoMenu", "itemAmount: " + keranjang.getAmount());
        holder.jumlahItem.setText(String.valueOf(keranjang.getAmount()));
        try {
            Response<List<MenuMakanan>> response = getMenu.execute();
            ArrayList<MenuMakanan> listKeranjang = (ArrayList<MenuMakanan>) response.body();
            Log.d("response", "onBindViewHolder: " + response.body());
            String nama = listKeranjang.get(0).getName();
            int harga = Integer.parseInt(listKeranjang.get(0).getPrice());
            holder.namaItem.setText(nama);
            holder.hargaItem.setText(String.valueOf(harga));

            if (listKeranjang.get(0).getImage_url() != null && !listKeranjang.get(0).getImage_url().equals("")) {
                        try {
                            URL url = new URL(listKeranjang.get(0).getImage_url());
                            Glide.with(context)
                                    .load(url)
                                    .into(holder.imageItem);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        holder.btnInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int jumlah = Integer.parseInt(holder.jumlahItem.getText().toString());
                jumlah++;

                holder.jumlahItem.setText(String.valueOf(jumlah));
                if (jumlah != 0 ) {
                    if(CartHelper.isSharedPreferencesExist(context)){
                        Log.d("menu value","Shared Preferences exist!");
                        if(CartHelper.getItemAmount(context,keranjang.getItemID()) != jumlah){
                            //update shared preference
                            DetailRestoMenu.updateCart(context,keranjang.getIDResto(),keranjang.getItemID(),jumlah);
                            Log.d("menu value","Shared Preferences updated!");
                        }
                    }
                    else
                    {
                        Log.d("menu value","Shared Preferences doesnt exist!");
                    }
                }

            }
        });
        holder.btnDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int jumlah = Integer.parseInt(holder.jumlahItem.getText().toString());

                if (jumlah != 0 ) {
                    jumlah--;
                    holder.jumlahItem.setText(String.valueOf(jumlah));
                    if(CartHelper.isSharedPreferencesExist(context)){
                        Log.d("menu value","Shared Preferences exist!");
                        if(CartHelper.getItemAmount(context,keranjang.getItemID()) != jumlah){
                            //update shared preference
                            DetailRestoMenu.updateCart(context,keranjang.getIDResto(),keranjang.getItemID(),jumlah);
                            Log.d("menu value","Shared Preferences updated!");
                        }
                    }else{
                        Log.d("menu value","Shared Preferences doesnt exist!");
                    }
                    if (CartHelper.getItemAmount(context, keranjang.getItemID()) == 0) {
                        Log.d("menu value", "remove from sharedpreferences");
                        DetailRestoMenu.removeFromCart(context, keranjang.getIDResto(), keranjang.getItemID());
                    }
                    if(CartHelper.getItemAmount(context,keranjang.getItemID()) == 0) {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("fragment", "cart");
                        context.startActivity(intent);
                    }
                }else {
                    if(CartHelper.isSharedPreferencesExist(context)) {
                        Log.d("menu value", "Shared Preferences exist!");
                        if (CartHelper.getItemAmount(context, keranjang.getItemID()) == 0) {
                            Log.d("menu value", "remove from sharedpreferences");
                            DetailRestoMenu.removeFromCart(context, keranjang.getIDResto(), keranjang.getItemID());
                        }
                        if(CartHelper.getItemAmount(context,keranjang.getItemID()) == 0) {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("fragment", "cart");
                            context.startActivity(intent);
                        }
                    }
                }

            }
        });


        //holder.txtCurhat.setText(resto.getAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, resto.getName(), Toast.LENGTH_SHORT).show();
                /*
                Intent intent = new Intent(context, DetailRestoMenu.class);
                intent.putExtra("idResto",resto.getId());
                // Log.d("adapter",resto.getId());
                intent.putExtra("namaResto", resto.getName());
                intent.putExtra("alamatResto", resto.getAddress());
                context.startActivity(intent);
                */
            }
        });

    }

    @Override
    public int getItemCount() {
        return listCart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView namaItem;
        public TextView hargaItem;
        public TextView jumlahItem;
        public ImageView imageItem;
        public Button btnInc, btnDec;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namaItem = itemView.findViewById(R.id.namaItem_cart);
            hargaItem = itemView.findViewById(R.id.hargaItem_cart);
            jumlahItem = itemView.findViewById(R.id.jumlahItem_cart);
            imageItem = itemView.findViewById(R.id.gambarItem_cart);
            btnInc = itemView.findViewById(R.id.btnInc_cart);
            btnDec = itemView.findViewById(R.id.btnDec_cart);

        }

    }

}
