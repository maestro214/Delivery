package com.example.delivery.retrofit;

import com.example.delivery.models.ApiData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("carriers/{carrier_id}/tracks/{track_id}")
    Call<ApiData> getPosts(@Path("carrier_id") String carrier, @Path("track_id")String track );
}
