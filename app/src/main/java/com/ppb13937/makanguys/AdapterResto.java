package com.ppb13937.makanguys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ppb13937.makanguys.apiclient.Resto;

import java.net.URL;
import java.util.ArrayList;

public class AdapterResto extends RecyclerView.Adapter<AdapterResto.ViewHolder> {

    ArrayList<Resto> listResto;

    public AdapterResto(ArrayList<Resto> listResto) {
        this.listResto = listResto;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.template_rv_resto, parent, false));
        return holder;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Context context = holder.itemView.getContext();
        Resto resto = listResto.get(position);
        holder.namaResto.setText(resto.getName());
        float randfloat= (float) ((Math.random())*(5-4.5)+4.5);
        holder.ratingResto.setText(String.format("%.2f",randfloat));
        try {
            URL url = new URL(resto.getImage_url());
            Glide.with(context)
                    .load(url)
                    .into(holder.imageResto);
        } catch (Exception e) {

        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailRestoMenu.class);
                intent.putExtra("idResto",resto.getId());
                intent.putExtra("locationResto",resto.getLocation());
                intent.putExtra("namaResto", resto.getName());
                intent.putExtra("alamatResto", resto.getAddress());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listResto.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView namaResto, ratingResto;
        public ImageView imageResto;
        public Button tambahKeranjang;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namaResto = itemView.findViewById(R.id.namaItem_cart);
            ratingResto = itemView.findViewById(R.id.ratingResto);
            tambahKeranjang = itemView.findViewById(R.id.btnTambahKeranjang_menu);
            imageResto = itemView.findViewById(R.id.gambarItem_cart);
        }

    }

}
