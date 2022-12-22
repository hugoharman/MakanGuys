package com.ppb13937.makanguys.apiclient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MakanGuysInterface {
    @GET("makanguys/resto.php")
    Call<List<Resto>> getResto();

    @GET("makanguys/menu.php")
    Call<List<MenuMakanan>> getRestoMenu();

    @GET("makanguys/menu.php")
    Call<List<MenuMakanan>> getMenu(@Query("id") int restoid);

    @GET("makanguys/menu.php")
    Call<List<MenuMakanan>> getRestoMenuByID(@Query("restoid") int restoid);

    @FormUrlEncoded
    @POST("curhat/")
    Call<Resto> postCurhat(@Field("nama")String nama, @Field("konten")String konten);

    @DELETE("curhat/")
    Call<Resto> delCurhat(@Query("id") int id);

    @GET("makanguys/resto.php")
    Call<List<Resto>> getRestoByID(@Query("id") int idResto);
}
