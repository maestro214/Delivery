package com.example.delivery;

import static com.naver.maps.map.NaverMap.MapType.Navi;

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

import com.example.delivery.adapters.model.BasePostItem;
import com.example.delivery.mappers.CarrierIdMapper;
import com.example.delivery.models.Post;
import com.example.delivery.usecase.GeocoderHelper;
import com.example.delivery.usecase.GetDeliveryProgressRequest;
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

public class PostOnMapFragment extends Fragment implements OnMapReadyCallback, GetDeliveryProgressUseCase.Listener {
    private final static String LOCATIONS = "locations";

    // TODO : 데리터를 처리하는 로직 무조건 뷰에서 분리해야 함(UI가 있는 앱에서는 거의 분문율임). 그래서 MVC, MVP, MVVM 같은 패턴을 사용할 필요해 짐.
    private final FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private NaverMap naverMap;
    private final List<LatLng> point = new ArrayList<>();
    private List<Post> mDatas;

    /**
     * Fragment는 기본 생성자만 가져야 함. 추가적인 데이터를 넘길 필요가 있다면,
     * Bundle에 필요한 데이터를 담아서, setArguments해서 넘기고,
     * getArguments를 이용해서 읽어와서 처리해야 함. 가장 큰 이유는 Lifecycle 때문임.
     * 이렇게 하지 않으면, 예를 들어 디바이스를 로테이션 시키면 생성자에 넘겨줬던 데이터는 보관이 되지않으나,
     * Bundle에 담겨진 데이터는 안드로이드가 보관을 해 줌.
     */
    public static PostOnMapFragment newInstance(List<LatLng> locations) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(LOCATIONS, new ArrayList<>(locations));
        PostOnMapFragment fragment = new PostOnMapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public PostOnMapFragment() {

    }

    // TODO : 코드의 간결성이나 이벤트 호출시점 등을 봤을 때, 뷰의 초기화는 onViewCreate 메소드가 조금 더 합리적임.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_on_map, container, false);

        view.findViewById(R.id.btn_mapadd).setOnClickListener(v -> showDeliverySelectionDialog());

        MapFragment mapFragment = (MapFragment) getParentFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getParentFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        return view;
    }

    // TODO : 별도의 DialogManager 클래스를 두어서 모든 Dialog를 한곳에서 관리.
    private void showDeliverySelectionDialog() {
        DeliverySelectionDialog customDialog_map = new DeliverySelectionDialog(
                requireContext(),
                this::fetchDeliveryProgress
        );
        customDialog_map.setCanceledOnTouchOutside(true);
        customDialog_map.setCancelable(true);
        customDialog_map.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        customDialog_map.show();
    }

    private void fetchDeliveryProgress(BasePostItem post) {
        String trackId = post.getNumber();
        String carrierId = CarrierIdMapper.mapBy(post.getCompany());
        GetDeliveryProgressRequest request = new GetDeliveryProgressRequest(carrierId, trackId);

        getDeliveryProgressUseCase.fetch(request);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        setupMarkers();
    }

    private void setupMarkers() {
        Bundle arguments = getArguments();
        if (arguments == null) return;

        List<LatLng> locations = arguments.getParcelableArrayList(LOCATIONS);
        if (locations == null || locations.isEmpty()) return;

        for (LatLng location : locations) {
            Marker marker = new Marker();
            marker.setPosition(location);
            marker.setMap(naverMap);
        }
    }

    private GetDeliveryProgressUseCase getDeliveryProgressUseCase;

    private void setupRecyclerView() {
        getDeliveryProgressUseCase = new GetDeliveryProgressUseCase(
                new GeocoderHelper(requireContext())
        );
    }

    @Override
    public void onStart() {
        super.onStart();
        getDeliveryProgressUseCase.addListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getDeliveryProgressUseCase.removeListener(this);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        this.naverMap = naverMap;
        FirebaseUser user = mAuth.getCurrentUser();
        String id2 = user.getUid();

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
        // TODO : 에러시, 여기에 필요한 처리
    }

    @Override
    public void onDeliveryProgressFetched(List<LatLng> locations) {
        // TODO : 성공시, 여기에 필요한 처리
    }
    // endregion GetDeliveryProgress.Listener implementation
}







