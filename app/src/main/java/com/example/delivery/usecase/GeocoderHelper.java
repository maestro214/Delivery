package com.example.delivery.usecase;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.naver.maps.geometry.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeocoderHelper {

    private final Context mContext;

    public GeocoderHelper(Context mContext) {
        this.mContext = mContext;
    }

    public List<LatLng> getLocationsBy(String locationName) throws IOException {
        Geocoder geocoder = new Geocoder(mContext);

        List<LatLng> locations = new ArrayList<>();

        List<Address> addresses = geocoder.getFromLocationName(locationName, 10);
        double x = addresses.get(0).getLatitude();
        double y = addresses.get(0).getLongitude();

        LatLng xy = new LatLng(x, y);
        locations.add(xy);

        return locations;
    }
}
