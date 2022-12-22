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
import com.ppb13937.makanguys.apiclient.MenuMakanan;

import java.net.URL;
import java.util.ArrayList;

public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.ViewHolder> {

    ArrayList<MenuMakanan> listMenu;

    public AdapterMenu(ArrayList<MenuMakanan> listMenu) {
        this.listMenu = listMenu;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.template_rv_menu, parent, false));
        return holder;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Context context = holder.itemView.getContext();
        MenuMakanan menu = listMenu.get(position);
        holder.namaMenu.setText(menu.getName());
        holder.hargaMenu.setText(menu.getPrice());
        try {
            URL url = new URL(menu.getImage_url());
            Glide.with(context)
                    .load(url)
                    .into(holder.gambarMenu);
        } catch (Exception e) {

        }
        if(CartHelper.isSharedPreferencesExist(context)) {
            int amountItem = CartHelper.getItemAmount(context, menu.getId());
            Log.d("amountItem", String.valueOf(amountItem));
            if(amountItem > 0) {
                setupInputNumberLayout(context, holder,amountItem,menu.getId(),menu.getRestoid());
            }
        }
        holder.btnTambahKeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartHelper.loadCart(context);
                setupInputNumberLayout(context, holder, 1,menu.getId(),menu.getRestoid());
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check Detail Menu
                Intent intent = new Intent(context, DetailMenu.class);
                intent.putExtra("namaMenu", menu.getName());
                intent.putExtra("gambarMenu", menu.getImage_url());
                intent.putExtra("deskripsiMenu", menu.getDescription());
                intent.putExtra("hargaMenu", menu.getPrice());
                context.startActivity(intent);
            }
        });

    }

    private void setupInputNumberLayout(Context context, ViewHolder holder, int amountItem, int itemid, int idresto) {
        // Inflate the input number layout
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout inputNumberLayout = (LinearLayout) inflater.inflate(R.layout.input_number_layout, null);

        // Add the input number layout to the view hierarchy
        LinearLayout parentLayout = holder.itemView.findViewById(R.id.parent_layout);
        parentLayout.addView(inputNumberLayout);

        // Hide the button
        holder.btnTambahKeranjang.setVisibility(View.GONE);

        // Get a reference to the input number EditText and the increment and decrement buttons
        TextView inputNumber = inputNumberLayout.findViewById(R.id.jumlahItem_menu);
        Button btnIncrement = inputNumberLayout.findViewById(R.id.btnInc_menu);
        Button btnDecrement = inputNumberLayout.findViewById(R.id.btnDec_menu);

        // Set the value of the input number EditText to 1
        inputNumber.setText(String.valueOf(amountItem));
        if(CartHelper.isSharedPreferencesExist(context)){
            Log.d("menu value","Shared Preferences exist!");
            if(CartHelper.getItemAmount(context,itemid) != amountItem){
                //update shared preference
                DetailRestoMenu.updateCart(context,idresto,itemid,amountItem);
                Log.d("menu value","Shared Preferences updated!");
            }
        }else{
            Log.d("menu value","Shared Preferences doesnt exist!");
            DetailRestoMenu.saveCart(context,idresto,itemid,amountItem);
        }
        btnIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Integer.parseInt(inputNumber.getText().toString());
                value++;
                inputNumber.setText(String.valueOf(value));

                if (value != 0 ) {
                    if(CartHelper.isSharedPreferencesExist(context)){
                        Log.d("menu value","Shared Preferences exist!");
                        if(CartHelper.getItemAmount(context,itemid) != value){
                            //update shared preference
                            DetailRestoMenu.updateCart(context,idresto,itemid,value);
                            Log.d("menu value","Shared Preferences updated!");
                        }
                    }else{
                        Log.d("menu value","Shared Preferences doesnt exist!");
                    }
                }
//                Log.d("testgg",inputNumber.getText().toString());
                DetailRestoMenu.loadCart(context);

            }
        });

        btnDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("testgg",inputNumber.getText().toString());
                // Decrement the value in the input number EditText
                int value = Integer.parseInt(inputNumber.getText().toString());
                if(value != 0) value--;
                if (value != 0 ) {
                    if(CartHelper.isSharedPreferencesExist(context)){
                        Log.d("menu value","Shared Preferences exist!");
                        if(CartHelper.getItemAmount(context,itemid) != value){
                            //update shared preference
                            DetailRestoMenu.updateCart(context,idresto,itemid,value);
                            Log.d("menu value","Shared Preferences updated!");
                        }
                    }else{
                        Log.d("menu value","Shared Preferences doesnt exist!");
                    }
                }
                if (value == 0) {
                    parentLayout.removeView(inputNumberLayout);
                    holder.btnTambahKeranjang.setVisibility(View.VISIBLE);
                    if(CartHelper.isSharedPreferencesExist(context)){
                        Log.d("menu value","Shared Preferences exist!");
                        if(CartHelper.getItemAmount(context,itemid) != value){
                            //remove from cart shared preference
                            DetailRestoMenu.removeFromCart(context,idresto,itemid);

                        }
                    }else{
                        Log.d("menu value","Shared Preferences doesnt exist!");
                    }
                }
                inputNumber.setText(String.valueOf(value));
                DetailRestoMenu.loadCart(context);

            }
        });
        DetailRestoMenu.loadCart(context);
    }

    @Override
    public int getItemCount() {
        return listMenu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView namaMenu, hargaMenu;
        public ImageView gambarMenu;
        public Button btnTambahKeranjang;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namaMenu = itemView.findViewById(R.id.namaItem_cart);
            hargaMenu = itemView.findViewById(R.id.hargaItem_cart);
            btnTambahKeranjang = itemView.findViewById(R.id.btnTambahKeranjang_menu);
            gambarMenu = itemView.findViewById(R.id.gambarItem_cart);
        }

    }

}
