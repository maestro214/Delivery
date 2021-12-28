package com.example.delivery;

import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;

import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.MarkerIcons;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Frag_map extends Fragment implements OnMapReadyCallback {

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.map_frag,container,false);

        MapFragment mapFragment = (MapFragment) getParentFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getParentFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);


        return view;
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {


        FirebaseUser user = mAuth.getCurrentUser();
        String id2 = user.getUid();
        Log.d("ididid",id2);

        CollectionReference productRef = mStore.collection("user");

                        mStore.collection("user").whereEqualTo("documentId",id2).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot document : task.getResult()){
                                        Map<String, Object> str = document.getData();
                                        String address = String.valueOf(str.get(FirebaseID.address));
                                        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
                                        List<Address> list = null;
                                        Log.d("address",address);
                                        //String str = "서울시 광진구 자양동 230-13";

                                        LatLng xy = null;
                                        try {
                                            list = geocoder.getFromLocationName(address, 10);
                                            double x = list.get(0).getLatitude();
                                            double y = list.get(0).getLongitude();
                                            xy = new LatLng(x, y);

                                            Log.d("test", xy.toString());


                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        naverMap.setMapType(NaverMap.MapType.Navi);
                                        Marker marker = new Marker();
                                        marker.setPosition(xy);
                                        marker.setIcon(OverlayImage.fromResource(R.drawable.home));
                                        marker.setWidth(80);
                                        marker.setHeight(80);
                                        marker.setMap(naverMap);

                                        CameraPosition cameraPosition = new CameraPosition(new LatLng(36.659812305882014, 127.72590), 6, 0, 0);
                                        naverMap.setCameraPosition(cameraPosition);
                                        Log.d("god", "진행중");                    }
                                }else {

                                }
                            }
                        });





                    }





    }

