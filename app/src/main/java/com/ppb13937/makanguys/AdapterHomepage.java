package com.ppb13937.makanguys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import com.bumptech.glide.Glide;

public class AdapterHomepage extends RecyclerView.Adapter<AdapterHomepage.MyViewHolder> {

    private Context mContext;
    private ArrayList<String> array_gambar, array_rating;

    public AdapterHomepage(Context mContext, ArrayList<String> array_gambar, ArrayList<String> array_rating) {
        super();
        this.mContext = mContext;
        this.array_gambar = array_gambar;
        this.array_rating = array_rating;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.template_rv_menu, parent, false);
        return new AdapterHomepage.MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView rating;
        public ImageView gambar_buku;
        public CardView cv_menu_utama;

        public MyViewHolder(View itemView) {
            super(itemView);
            cv_menu_utama = itemView.findViewById(R.id.cv_menu_utama);
            gambar_buku = itemView.findViewById(R.id.gambarItem_cart);
            rating = itemView.findViewById(R.id.hargaItem_cart);
        }
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        TextView rating = holder.rating;
        ImageView gambar_buku = holder.gambar_buku;
        Glide.with(holder.itemView.getContext()).load(array_gambar.get(position)).into(holder.gambar_buku);
        rating.setText(array_rating.get(position));
        gambar_buku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(gambar_buku.getContext(), DetailRestoMenu.class);
                intent.putExtra("link_gambar", array_gambar.get(position));
                gambar_buku.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return array_gambar.size();
    }
}

