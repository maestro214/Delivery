package com.example.delivery;

import android.util.Log;

import com.example.delivery.retrofit.DeliveryService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeliveryApi {


    public String DeliveryApi(String com, String num) throws MalformedURLException {

        String urlApi = "https://apis.tracker.delivery/carriers/"+com+"/tracks/"+num;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlApi)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DeliveryService deliveryService = retrofit.create(DeliveryService.class);
        try {
            URL url = new URL(urlApi);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            Log.d("tag",connection.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


        return com;
    }
}
