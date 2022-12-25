package com.ppb13937.makanguys;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
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
    public AdapterMenu(ArrayList<MenuMakanan> listMenu, String restoName, String Alamat) {
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
                Log.d("getExistingRestoId", String.valueOf(CartHelper.getExistingRestoID(context)));
                Log.d("getRestoId", String.valueOf(menu.getRestoid()));
                if(CartHelper.getExistingRestoID(context) == menu.getRestoid() || CartHelper.getExistingRestoID(context) == -1) {
                    DetailRestoMenu.loadCart(context);
                    setupInputNumberLayout(context, holder, 1,menu.getId(),menu.getRestoid());
                    CartHelper.loadCart(context);

                }else{
                    //show dialog to clear cart
                    Log.d("show dialog", "show dialog");
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Peringatan");
                    builder.setMessage("Anda sudah memilih menu dari restoran lain. Apakah anda ingin menghapus menu dari restoran sebelumnya?");

                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CartHelper.clearCart(context);
                            DetailRestoMenu.loadCart(context);
                            setupInputNumberLayout(context, holder, 1,menu.getId(),menu.getRestoid());
                            CartHelper.loadCart(context);
                        }
                    });
                    //tidak
                    builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();

                }

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check Detail Menu
                Intent intent = new Intent(context, DetailMenu.class);
                intent.putExtra("itemID", menu.getId());
                intent.putExtra("restoID", menu.getRestoid());
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
            if(CartHelper.getItemAmount(context,itemid) != amountItem){
                //update shared preference
                DetailRestoMenu.updateCart(context,idresto,itemid,amountItem);
            }
        }else{
            DetailRestoMenu.saveCart(context,idresto,itemid,amountItem);
        }
        btnIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CartHelper.getExistingRestoID(context) == idresto) {
                    Log.d("getExistingRestoID", String.valueOf(CartHelper.getExistingRestoID(context)));
                    Log.d("menu value", "Shared Preferences exist!");
                    int value = Integer.parseInt(inputNumber.getText().toString());
                    value++;
                    inputNumber.setText(String.valueOf(value));
                    if (CartHelper.getItemAmount(context, itemid) != value) {
                        //update shared preference
                        DetailRestoMenu.updateCart(context, idresto, itemid, value);
                        Log.d("menu value", "Shared Preferences updated!");
                    }
                }else{
                    //show dialog to clear cart
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Peringatan");
                    builder.setMessage("Anda sudah memilih menu dari restoran lain. Apakah anda ingin menghapus menu dari restoran sebelumnya?");

                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //clear cart

                            CartHelper.clearCart(context);
                            //update shared preference
                            int value = Integer.parseInt(inputNumber.getText().toString());
                            DetailRestoMenu.saveCart(context, idresto, itemid, value);
                            value++;
                            inputNumber.setText(String.valueOf(value));
                            if (CartHelper.getItemAmount(context, itemid) != value) {
                                DetailRestoMenu.updateCart(context, idresto, itemid, value);
                            }
                        }
                    });
                }
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
                        if(CartHelper.getItemAmount(context,itemid) != value){
                            //update shared preference
                            DetailRestoMenu.updateCart(context,idresto,itemid,value);
                        }
                    }
                }
                if (value == 0) {
                    parentLayout.removeView(inputNumberLayout);
                    holder.btnTambahKeranjang.setVisibility(View.VISIBLE);
                    if(CartHelper.isSharedPreferencesExist(context)){
                        if(CartHelper.getItemAmount(context,itemid) != value){
                            //remove from cart shared preference
                            DetailRestoMenu.removeFromCart(context,idresto,itemid);

                        }
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
