package com.ppb13937.makanguys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ppb13937.makanguys.apiclient.APIClient;
import com.ppb13937.makanguys.apiclient.MakanGuysInterface;
import com.ppb13937.makanguys.apiclient.MenuMakanan;
import com.ppb13937.makanguys.apiclient.Resto;

import java.lang.reflect.Array;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.ViewHolder> {

    ArrayList<History> listHistory;
    MakanGuysInterface makanGuysInterface;

    public AdapterHistory(ArrayList<History> listHistory) {
        this.listHistory = listHistory;
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
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.template_rv_historyorder, parent, false));
        return holder;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        makanGuysInterface = APIClient.getClient().create(MakanGuysInterface.class);
        Context context = holder.itemView.getContext();
        History history = listHistory.get(position);
        int idResto = history.getIdResto();
        Log.d("AdapterHistory","Resto Name: "+history.getIdResto());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
        String date = sdf.format(new Date(history.getTanggalOrder()));
        holder.orderDate.setText(date);
        Call<List<Resto>> getResto = makanGuysInterface.getRestoByID(idResto);
        try {
            // Make the synchronous API call
            Response<List<Resto>> response = getResto.execute();
            ArrayList<Resto> infoResto = (ArrayList<Resto>) response.body();
            Log.d("response", "onBindViewHolder: " + response.body());
            String nama = infoResto.get(0).getName();
            holder.restoName.setText(nama);

            //set imageResto from API
            try {
                URL url = new URL(infoResto.get(0).getImage_url());
                Glide.with(context)
                        .load(url)
                        .into(holder.imageURL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<Integer> listItems = history.getItems();
        StringBuilder items = new StringBuilder();
        for(int i = 0; i < listItems.size();i++){
            Log.d("AdapterHistory","Item: "+listItems.get(i));
            //get item name
            Call<List<MenuMakanan>> getMenu = makanGuysInterface.getMenu(listItems.get(i));
            try {
                // Make the synchronous API call
                Response<List<MenuMakanan>> response = getMenu.execute();
                ArrayList<MenuMakanan> infoMenu = (ArrayList<MenuMakanan>) response.body();
                Log.d("response", "onBindViewHolder: " + response.body());
                String nama = infoMenu.get(0).getName();
                items.append(history.getJumlahItems().get(i)).append(" ").append(nama).append(", ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        holder.orderList.setText(items.toString());
        Log.d("AdapterHistory","Items: "+items);
        /*
        holder.restoName.setText(history.getNamaResto());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = sdf.format(history.getTanggalOrder()).toString();
        holder.orderDate.setText(formattedDate);
        holder.totalPayment.setText(String.format("Rp. %,d", history.getHargaItem()));
        try {
            URL url = new URL(history.getImageItem());
            Glide.with(context)
                    .load(url)
                    .into(holder.imageURL);
        } catch (Exception e) {

        }
        */




    }

    @Override
    public int getItemCount() {
        return listHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView restoName;
        public TextView orderDate;
        public TextView orderList;
        public ImageView imageURL;
        public TextView totalPayment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restoName = itemView.findViewById(R.id.namaResto_history);
            orderDate = itemView.findViewById(R.id.tanggalPesan_history);
           orderList = itemView.findViewById(R.id.orderList_history);
            imageURL = itemView.findViewById(R.id.gambarResto_history);
           totalPayment = itemView.findViewById(R.id.totalPembayaran_history);
        }

    }

}
