package com.ppb13937.makanguys;

public class HelperClass {
    String name,email,phonenumber, address;

    public HelperClass(String name, String email, String phonenumber, String address) {
        this.name = name;
        this.email = email;
        this.phonenumber = phonenumber;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
