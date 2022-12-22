package com.ppb13937.makanguys;

public class Cart {
    private int IDResto,itemID,amount;
    public Cart(int IDResto,int itemID,int amount){
        this.IDResto = IDResto;
        this.itemID = itemID;
        this.amount = amount;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getAmount() {
        return amount;
    }

    public int getIDResto() {
        return IDResto;
    }

    public void setIDResto(int IDResto) {
        this.IDResto = IDResto;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
