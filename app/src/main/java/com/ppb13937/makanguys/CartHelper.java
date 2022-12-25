package com.ppb13937.makanguys;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ppb13937.makanguys.apiclient.MenuMakanan;
import com.ppb13937.makanguys.apiclient.Resto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartHelper {
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

    public static int getItemAmount(Context context,int itemID){
        if(!isSharedPreferencesExist(context)){
            return 0;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_CART,Context.MODE_PRIVATE);
        String data = sharedPreferences.getString("pref_data",null);
        try {
            JSONObject mainObj = new JSONObject(data);
            JSONArray ja = mainObj.getJSONArray("cart");
            for(int i = 0; i < ja.length(); i++){
                JSONObject jo = ja.getJSONObject(i);
                int idItem = jo.getInt("idItem");
                int amountItem = jo.getInt("amountItem");
                if(idItem == itemID){
                    return amountItem;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static ArrayList<Cart> loadCart(Context context) {
        ArrayList<Cart> listCart = new ArrayList<>();
        if(!isSharedPreferencesExist(context)){
            return listCart;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_CART, Context.MODE_PRIVATE);

        int idResto = sharedPreferences.getInt("idResto", 0);
        String cartString = sharedPreferences.getString("pref_data", "");

        try {
            JSONObject jsonObj = new JSONObject(cartString);
            if(jsonObj.has("cart")) {
                JSONArray jsonArray = jsonObj.getJSONArray("cart");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int idItem = jsonObject.getInt("idItem");
                    int amountItem = jsonObject.getInt("amountItem");
                    listCart.add(new Cart(idResto, idItem, amountItem));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listCart;
    }

    public static ArrayList<Cart> saveCart(Context context,int idResto, int idItem, int amountItem, ArrayList<Cart> listCart){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_CART,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            JSONObject jo = new JSONObject();
            jo.put("idItem", idItem);
            jo.put("amountItem",amountItem);
            JSONArray ja = new JSONArray();
            ja.put(jo);
            JSONObject mainObj = new JSONObject();
            mainObj.put("cart", ja);
            editor.putInt("idResto", idResto);
            editor.putBoolean("initialized", true);
            editor.putString("pref_data", mainObj.toString()).commit();
            if (listCart == null) {
                listCart = new ArrayList<>();
            }
            listCart.add(new Cart(idResto,idItem,amountItem));

        } catch (JSONException json) {

        }
        editor.apply();
        Log.d("hi","data saved!");
        return listCart;

    }
    public static void removeFromCart(Context context,int idResto, int idItem,ArrayList<Cart> listCart){
        if(!isSharedPreferencesExist(context)){
            return;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_CART,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonHistory = sharedPreferences.getString("pref_data","");
        try {
            JSONObject jsonObj = new JSONObject(jsonHistory);
            JSONArray mJsonArrayProperty = jsonObj.getJSONArray("cart");
            try {
                int index = 0;
                JSONArray ja = new JSONArray();
                if(mJsonArrayProperty.length() == 0) return;
                int actualLength = 0;
                for (;index < mJsonArrayProperty.length(); index++) {
                    JSONObject mJsonObjectProperty = mJsonArrayProperty.getJSONObject(index);
                    int datanum1 = mJsonObjectProperty.getInt("idItem");
                    int datanum2 = mJsonObjectProperty.getInt("amountItem");
                    JSONObject gg = new JSONObject();
                    if(datanum1 != idItem) {
                        actualLength++;
                        gg.put("idItem", datanum1);
                        gg.put("amountItem", datanum2);
                        ja.put(gg);
                    }else{
                        //remove from listCart
                        if(listCart != null) {
                            for (int i = 0; i < listCart.size(); i++) {
                                if (listCart.get(i).getItemID() == idItem) {
                                    if (listCart.get(i).getAmount() == 1) {
                                        listCart.remove(i);
                                    } else {
                                        if (listCart.get(i).getAmount() > 1) {
                                            listCart.get(i).setAmount(listCart.get(i).getAmount() - 1);
                                        }
                                    }
                                }
                            }
                        }
                        actualLength--;
                    }
                }
                if(listCart != null) {
                    Log.d("test", listCart.toString());
                }
                JSONObject mainObj = new JSONObject();
                editor.putInt("idResto", idResto);
                if(actualLength != -1) {
                    mainObj.put("cart", ja);
                    editor.putBoolean("initialized", true);
                }else{
                    editor.putBoolean("initialized", false);
                }
                editor.putString("pref_data", mainObj.toString()).commit();
                editor.apply();
                Log.d("hi",String.valueOf(mainObj));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public static int ifItemExist(Context context,int idItem){
        if(!isSharedPreferencesExist(context)){
            return -1;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_CART,Context.MODE_PRIVATE);
        String data = sharedPreferences.getString("pref_data",null);
        try {
            JSONObject mainObj = new JSONObject(data);
            JSONArray ja = mainObj.getJSONArray("cart");
            for(int i = 0; i < ja.length(); i++){
                JSONObject jo = ja.getJSONObject(i);
                int idItem2 = jo.getInt("idItem");
                int amountItem = jo.getInt("amountItem");
                Log.d("hi",String.valueOf(idItem2));
                Log.d("hi",String.valueOf(amountItem));
                if(idItem == idItem2){
                    return i;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
        if(!isSharedPreferencesExist(context)) {
            saveCart(context,idResto,idItem,amountItem,listCart);
            return;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_CART,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonHistory = sharedPreferences.getString("pref_data","");
        try {
            JSONObject jsonObj = new JSONObject(jsonHistory);
            JSONArray mJsonArrayProperty = jsonObj.getJSONArray("cart");
            Log.d("hi",String.valueOf(mJsonArrayProperty));
            /*
            Log.d("test",mJsonArrayProperty.getString(0));
            JSONObject mJsonObjectPropertyz = mJsonArrayProperty.getJSONObject(0);
            int datanumz = mJsonObjectPropertyz.getInt("idItem");
            */
            // int datanum2 = mJsonObjectPropertyz.getInt("amountItem");
            //Log.d("hiya",String.valueOf(datanumz));
            try {
                int index = 0;
                JSONObject jo = new JSONObject();
                if(ifItemExist(context,idItem) == -1) {
                    Log.d("hi", "Added new items!");
                    jo.put("idItem", idItem);
                    jo.put("amountItem", amountItem);
                }
                JSONArray ja = new JSONArray();
                if(ifItemExist(context,idItem) == -1) {
                    ja.put(jo);
                }
                if(mJsonArrayProperty.length() == 0) return;
                for (;index < mJsonArrayProperty.length(); index++) {
                    JSONObject mJsonObjectProperty = mJsonArrayProperty.getJSONObject(index);
                    int datanum1 = mJsonObjectProperty.getInt("idItem");
                    int datanum2 = mJsonObjectProperty.getInt("amountItem");
                    JSONObject gg = new JSONObject();
                    if(datanum1 == idItem) {
                        gg.put("idItem", idItem);
                        gg.put("amountItem", amountItem);
                    }else {
                        gg.put("idItem", datanum1);
                        gg.put("amountItem", datanum2);
                    }
                    ja.put(gg);
                }

                JSONObject mainObj = new JSONObject();
                mainObj.put("cart", ja);
                editor.putInt("idResto", idResto);
                editor.putBoolean("initialized", true);
                editor.putString("pref_data", mainObj.toString()).commit();
                editor.apply();
                Log.d("hi",String.valueOf(mainObj));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
