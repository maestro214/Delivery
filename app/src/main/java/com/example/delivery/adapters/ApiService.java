package com.example.delivery.adapters;

import com.example.delivery.models.ApiData;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("/carriers/{carriers}/tracks/{tracks}")
    Call<ApiData> getPosts(@Path("carriers") String company, @Path("tracks") String number);

}
