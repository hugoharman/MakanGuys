package com.ppb13937.makanguys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class History {
    private int idResto;
    private ArrayList<Integer> items;
    private ArrayList<Integer> jumlahItems;
    private long tanggalOrder;
    private int totalHarga;
    // Add this no-argument constructor


    public History(int idResto, ArrayList<Integer> items, ArrayList<Integer> jumlahItems, long tanggalOrder, int totalHarga) {
        this.idResto = idResto;
        this.items = items;
        this.jumlahItems = jumlahItems;
        this.tanggalOrder = tanggalOrder;
        this.totalHarga = totalHarga;
    }

    public int getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(int totalHarga) {
        this.totalHarga = totalHarga;
    }

    public int getIdResto() {
        return idResto;
    }

    public void setIdResto(int idResto) {
        this.idResto = idResto;
    }

    public ArrayList<Integer> getItems() {
        return items;
    }

    public void setItems(ArrayList<Integer> items) {
        this.items = items;
    }

    public ArrayList<Integer> getJumlahItems() {
        return jumlahItems;
    }

    public void setJumlahItems(ArrayList<Integer> jumlahItems) {
        this.jumlahItems = jumlahItems;
    }

    public long getTanggalOrder() {
        return tanggalOrder;
    }

    public void setTanggalOrder(long tanggalOrder) {
        this.tanggalOrder = tanggalOrder;
    }
}
