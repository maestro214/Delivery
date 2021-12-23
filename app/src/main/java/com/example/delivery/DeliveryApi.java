package com.example.delivery;

import android.util.Log;

import com.example.delivery.models.ApiPost;
import com.example.delivery.retrofit.DeliveryService;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
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
