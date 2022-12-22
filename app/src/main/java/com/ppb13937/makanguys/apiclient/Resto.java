package com.ppb13937.makanguys.apiclient;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Resto implements Serializable{
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("image_url")
    private String image_url;
    @SerializedName("address")
    private String address;
    @SerializedName("location")
    private String location;

    public Resto() {
    }
    public Resto(int id, String name, String image_url, String address, String location) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.address = address;
        this.location = location;
    }
    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getId() {
        return String.valueOf(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String nama) {
        this.name = name;
    }

}
