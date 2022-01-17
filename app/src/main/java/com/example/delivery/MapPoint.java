package com.example.delivery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

public class MapPoint extends AppCompatActivity implements OnMapReadyCallback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_post_on_map);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if(mapFragment == null){
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map,mapFragment).commit();
        }

        mapFragment.getMapAsync(this);


    }


        @Override
        public void onMapReady (@NonNull NaverMap naverMap){


            Marker marker = new Marker();
            marker.setPosition(new LatLng(37.5549802, 126.9462631));
            marker.setMap(naverMap);
        }


    }
