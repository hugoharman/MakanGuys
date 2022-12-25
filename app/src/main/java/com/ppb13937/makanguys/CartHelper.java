package com.ppb13937.makanguys;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ppb13937.makanguys.apiclient.MenuMakanan;
import com.ppb13937.makanguys.apiclient.Resto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartHelper implements Serializable {
    private static final String SHARED_PREFS_CART = "makanGuysCart";
    private static final String CART_ITEMS_KEY = "cartItems";


    public static boolean isSharedPreferencesExist(Context context){
        SharedPreferences sharedPrefs =  context.getSharedPreferences(SHARED_PREFS_CART, Context.MODE_PRIVATE);
        if(!sharedPrefs.contains("initialized")){
            return false;
        }
        else{
            return sharedPrefs.getBoolean("initialized", true);
        }
    }

    public static int getItemAmount(Context context, int itemID) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_CART, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        String json = sharedPreferences.getString("listCart", null);

        Type listType = new TypeToken<List<Cart>>() {}.getType();
        ArrayList<Cart> listCart = gson.fromJson(json, listType);

        for (Cart cart : listCart) {
            if (cart.getItemID() == itemID) {
                return cart.getAmount();
            }
        }
        return 0;
    }
        public static ArrayList<Cart> loadCart(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_CART, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        String json = sharedPreferences.getString("listCart", null);

        Type listType = new TypeToken<List<Cart>>() {}.getType();
        ArrayList<Cart> listCart = gson.fromJson(json, listType);

        return listCart;
    }


    public static ArrayList<Cart> saveCart(Context context, int idResto, int idItem, int amountItem, ArrayList<Cart> listCart) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_CART, Context.MODE_PRIVATE);

        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.setPrettyPrinting();

        Gson gson = gsonBuilder.create();

        if (listCart == null) {
            listCart = new ArrayList<>();
        }

        listCart.add(new Cart(idResto, idItem, amountItem));

        String json = gson.toJson(listCart);

        sharedPreferences
                .edit()
                .putInt("idResto", idResto)
                .putBoolean("initialized", true)
                .putString("listCart", json)
                .apply();

        return listCart;
    }
    
    public static void removeFromCart(Context context, int idResto, int idItem, ArrayList<Cart> listCart) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_CART, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        String json = sharedPreferences.getString("listCart", null);

        Type listType = new TypeToken<List<Cart>>() {
        }.getType();
        ArrayList<Cart> cartList = gson.fromJson(json, listType);

        if (cartList != null) {
            for (int i = 0; i < cartList.size(); i++) {
                if (cartList.get(i).getItemID() == idItem) {
                    if (cartList.get(i).getAmount() == 1 || cartList.get(i).getAmount() == 0) {
                        cartList.remove(i);
                    } else {
                        if (cartList.get(i).getAmount() > 1) {
                            cartList.get(i).setAmount(cartList.get(i).getAmount() - 1);
                        }
                    }
                }
            }
        }

        // Convert the updated list of Cart objects back to a JSON string using Gson
        String updatedJson = gson.toJson(cartList);
        // Save the JSON string back to shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("listCart", updatedJson);
        editor.apply();

    }
        public static int ifItemExist(Context context, int idItem) {
        ArrayList<Cart> listCart = loadCart(context);

        for (int i = 0; i < listCart.size(); i++) {
            if (listCart.get(i).getItemID() == idItem) {
                return i;
            }
        }

        return -1;
    }
    //clear cart shared preferences
    public static void clearCart(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_CART,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static int getExistingRestoID(Context context){
        if(!isSharedPreferencesExist(context)){
            return -1;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_CART,Context.MODE_PRIVATE);
        int idResto = sharedPreferences.getInt("idResto",-1);
        return idResto;
    }

    public static void updateCart(Context context,int idResto, int idItem, int amountItem,ArrayList<Cart> listCart){
        // First, get a reference to the shared preferences object
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_CART, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Create a Gson object (you can use the same GsonBuilder that you used to save the list, if desired)
        Gson gson = new Gson();

        // Get the list of Cart objects from shared preferences as a JSON string
        String json = sharedPreferences.getString("listCart", null);

        // Convert the JSON string to a list of Cart objects using Gson
        Type listType = new TypeToken<List<Cart>>() {}.getType();
        ArrayList<Cart> existingList = gson.fromJson(json, listType);

        // Update the list of Cart objects as needed
        int index = ifItemExist(context,idItem);
        if (index == -1) {
            // Add a new Cart object to the list
            existingList.add(new Cart(idResto, idItem, amountItem));
        } else {
            // Update the existing Cart object in the list
            existingList.set(index, new Cart(idResto, idItem, amountItem));
        }

        // Convert the updated list of Cart objects back to a JSON string using Gson
        String updatedJson = gson.toJson(existingList, listType);

        // Save the JSON string to shared preferences
        editor.putString("listCart", updatedJson);
        editor.apply();
    }



}
