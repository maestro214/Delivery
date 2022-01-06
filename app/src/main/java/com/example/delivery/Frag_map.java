package com.example.delivery;

import static com.naver.maps.map.NaverMap.MapType.*;

import android.content.Context;
import android.location.Address;
import android.os.Bundle;

import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delivery.adapters.PostAdapter_map;
import com.example.delivery.models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import java.io.IOException;
import java.security.AllPermission;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Frag_map extends Fragment implements OnMapReadyCallback {

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private NaverMap naverMap;
    private List<LatLng> point = new ArrayList();
    private Context context;

    private RecyclerView mPostRecyclerView;
//    private PostAdapter_map mAdapter;
    private List<Post> mDatas;


    public void setPoint(Context context,List pointlist) {
        this.point = pointlist;
        this.context = context;
        System.out.println(point);
        if (point != null) {
            for (int i = 0; i < point.size(); i++) {
                LatLng xy = point.get(i);

                Marker marker1 = new Marker();
                marker1.setPosition(xy);
                marker1.setMap(naverMap);

            }
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.map_frag,container,false);

        mPostRecyclerView = view.findViewById(R.id.main_recyclerview_map);
        view.findViewById(R.id.btn_mapadd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                point.add(new LatLng(37.5269675,126.7573928));
//                point.add(new LatLng(37.2949948, 127.10805289999999));
//                point.add(new LatLng(37.3308523, 126.9375047));
//                point.add(new LatLng(37.5775499, 126.88286919999999));
//                if (point != null) {
//                    for (int i = 0; i < point.size(); i++) {
//                        LatLng xy = point.get(i);
//
//                        Marker marker1 = new Marker();
//                        marker1.setPosition(xy);
//                        marker1.setMap(naverMap);
//
//                    }
//                }
//
//

                CustomDialog_map customDialog_map = new CustomDialog_map(getContext());

                customDialog_map.setCanceledOnTouchOutside(true);
                customDialog_map.setCancelable(true);
                customDialog_map.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                customDialog_map.show();


                PostAdapter_map postAdapter_map = new PostAdapter_map( new PostAdapter_map.CustomDialogListener_map() {
                    @Override
                    public void itemViewclick(String i) {



                    }
                });

            }



        });


        MapFragment mapFragment = (MapFragment) getParentFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getParentFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap ) {

        this.naverMap =naverMap;
        FirebaseUser user = mAuth.getCurrentUser();
        String id2 = user.getUid();
        Context context;






        mStore.collection("user").whereEqualTo("documentId",id2).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Map<String, Object> str = document.getData();
                        String address = String.valueOf(str.get(FirebaseID.address));
                        Geocoder geocoder = new Geocoder((AppCompatActivity)getContext());
                        List<Address> list = null;

                        LatLng xy = null;
                        try {
                            list = geocoder.getFromLocationName(address, 10);
                            double x = list.get(0).getLatitude();
                            double y = list.get(0).getLongitude();
                            xy = new LatLng(x, y);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        naverMap.setMapType(Navi);
                        Marker marker = new Marker();
                        marker.setPosition(xy);
                        marker.setIcon(OverlayImage.fromResource(R.drawable.home));
                        marker.setWidth(80);
                        marker.setHeight(80);
                        marker.setMap(naverMap);

                        CameraPosition cameraPosition = new CameraPosition(new LatLng(36.659812305882014, 127.72590), 6, 0, 0);
                        naverMap.setCameraPosition(cameraPosition);
                    }
                }else {

                }
            }
        });





                    }



        }







