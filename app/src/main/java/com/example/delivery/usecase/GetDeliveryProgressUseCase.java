package com.example.delivery.usecase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.delivery.convertcom.convertcom;
import com.example.delivery.convertcom.LocationMapper;
import com.example.delivery.models.ApiData;
import com.example.delivery.models.Post;
import com.example.delivery.retrofit.ApiService;
import com.example.delivery.retrofit.NetworkModule;
import com.naver.maps.geometry.LatLng;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetDeliveryProgressUseCase {

    public interface Listener {
        void onDeliveryProgressFailure(Exception e);

        void onDeliveryProgressFetched(List<LatLng> locations);
    }

    private final GeocoderHelper geocoderHelper;
    private final Set<Listener> mListeners = new HashSet<>();

    public GetDeliveryProgressUseCase(GeocoderHelper geocoderHelper) {
        this.geocoderHelper = geocoderHelper;
    }

    public void addListener(Listener listener) {
        mListeners.add(listener);
    }

    public void remoteListener(Listener listener) {
        mListeners.remove(listener);
    }

    public void fetch(Post post) {
        String carrier = post.getCompany();
        String trackId = post.getNumber();

        convertcom convertcom = new convertcom();
        String carrier_id = convertcom.convertcompany(carrier);

        ApiService service = NetworkModule.getApiService();
        Call<ApiData> call = service.getPosts(carrier_id, trackId);

        call.enqueue(new Callback<ApiData>() {
            @Override
            public void onResponse(@NonNull Call<ApiData> call, @NonNull Response<ApiData> response) {
                handleResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<ApiData> call, @NonNull Throwable t) {
                handleError(new RuntimeException(t));
            }
        });
    }

    private void handleResponse(@NonNull Response<ApiData> response) {
        if (response.isSuccessful()) {
            handleSuccess(response);
        } else {
            handleError(new RuntimeException(response.message()));
        }
    }

    private void handleError(Exception exception) {
        Log.d("test", "실패");
        notifyFailure(exception);
    }

    private void handleSuccess(@NonNull Response<ApiData> response) {
        ApiData apiData = response.body();
        assert apiData != null;
        int test = apiData.getProgresses().size();

        try {
            for (int i = 0; i < test; i++) {
                String locationName = apiData.getProgresses().get(i).getLocation().toString();

                locationName = LocationMapper.mapBy(locationName);

                List<LatLng> locations = geocoderHelper.getLocationsBy(locationName);
                notifySuccess(locations);
            }
        } catch (IOException e) {
            notifyFailure(e);
        }
    }

    private void notifySuccess(List<LatLng> locations) {
        for (Listener listener : mListeners) {
            listener.onDeliveryProgressFetched(locations);
        }
    }

    private void notifyFailure(Exception e) {
        for (Listener listener : mListeners) {
            listener.onDeliveryProgressFailure(e);
        }
    }
}
