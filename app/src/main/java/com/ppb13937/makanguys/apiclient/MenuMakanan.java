package com.ppb13937.makanguys.apiclient;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MenuMakanan implements Serializable{
    @SerializedName("id")
    private int id;
    @SerializedName("restoid")
    private int restoid;
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private String price;
    @SerializedName("image_url")
    private String image_url;
    @SerializedName("description")
    private String description;

    public MenuMakanan() {
    }
    public MenuMakanan(int id, int restoid, String name, String price, String image_url, String description) {
        this.id = id;
        this.restoid = restoid;
        this.name = name;
        this.price = price;
        this.image_url = image_url;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRestoid() {
        return restoid;
    }

    public void setRestoid(int restoid) {
        this.restoid = restoid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
