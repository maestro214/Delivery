package com.example.delivery.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkModule {

    private static final String BASE_URL = "https://apis.tracker.delivery/";
    private static Retrofit mRetrofit;
    private static ApiService mApiService;

    public static Retrofit getRetrofit() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return mRetrofit;
    }

    public static ApiService getApiService() {
        if (mApiService == null) {
            mApiService = getRetrofit().create(ApiService.class);
        }
        return mApiService;
    }
}
