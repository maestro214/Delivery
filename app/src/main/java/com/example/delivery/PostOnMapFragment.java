package com.example.delivery;

import static com.naver.maps.map.NaverMap.MapType.Navi;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delivery.adapters.PostOnMapAdapter;
import com.example.delivery.models.Post;
import com.example.delivery.usecase.GeocoderHelper;
import com.example.delivery.usecase.GetDeliveryProgressUseCase;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostOnMapFragment extends Fragment implements OnMapReadyCallback, GetDeliveryProgressUseCase.Listener, PostOnMapAdapter.ItemClickListener {

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private NaverMap naverMap;
    private List<LatLng> point = new ArrayList();
    private Context context;

    private RecyclerView mPostRecyclerView;
    private List<Post> mDatas;


    public PostOnMapFragment(Context context, List pointlist) {
        this.point = pointlist;
        this.context = context;

        if (point.size() == pointlist.size()) {
            System.out.println(point.toString());
        } else {
            if (point != null) {
                for (int i = 0; i < point.size(); i++) {
                    LatLng xy = point.get(i);

                    Marker marker1 = new Marker();
                    marker1.setPosition(xy);
                    marker1.setMap(naverMap);

                }
            }

        }
    }

    public PostOnMapFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_frag, container, false);

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

                DeliverySelectionDialog customDialog_map = new DeliverySelectionDialog(getContext(), PostOnMapFragment.this);

                customDialog_map.setCanceledOnTouchOutside(true);
                customDialog_map.setCancelable(true);
                customDialog_map.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                customDialog_map.show();


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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
    }

    private GetDeliveryProgressUseCase getDeliveryProgressUseCase;

    private void setupRecyclerView() {
        getDeliveryProgressUseCase = new GetDeliveryProgressUseCase(
            new GeocoderHelper(requireContext())
        );

        mPostRecyclerView = requireView().findViewById(R.id.main_recyclerview_map);
        PostOnMapAdapter postOnMapAdapter = new PostOnMapAdapter(null, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDeliveryProgressUseCase.addListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getDeliveryProgressUseCase.remoteListener(this);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        this.naverMap = naverMap;
        FirebaseUser user = mAuth.getCurrentUser();
        String id2 = user.getUid();
        Context context;


        mStore.collection("user").whereEqualTo("documentId", id2).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> str = document.getData();
                        String address = String.valueOf(str.get(FirebaseID.address));
                        Geocoder geocoder = new Geocoder((AppCompatActivity) getContext());
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
                } else {

                }
            }
        });


    }

    // region GetDeliveryProgress.Listener implementation
    @Override
    public void onDeliveryProgressFailure(Exception e) {

    }

    @Override
    public void onDeliveryProgressFetched(List<LatLng> locations) {

    }
    // endregion GetDeliveryProgress.Listener implementation

    @Override
    public void onPostOnMapClicked(Post post) {
        getDeliveryProgressUseCase.fetch(post);
    }
}







