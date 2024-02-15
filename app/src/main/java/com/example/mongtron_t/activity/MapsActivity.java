package com.example.mongtron_t.activity;

import static com.google.android.gms.location.LocationRequest.Builder.IMPLICIT_MIN_UPDATE_INTERVAL;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mongtron_t.R;
import com.example.mongtron_t.adpater.InfoWindowAdapter;
import com.example.mongtron_t.databinding.ActivityMapsBinding;
import com.example.mongtron_t.dialog.CustomAlterDialog;
import com.example.mongtron_t.fragment.FuncBarFragment;
import com.example.mongtron_t.http.RetrofitRxClient;
import com.example.mongtron_t.model.OtherVO;
import com.example.mongtron_t.model.UserInfo;
import com.example.mongtron_t.service.AddedFriendService;
import com.example.mongtron_t.tool.MarkerFunc;
import com.example.mongtron_t.model.AddedFriendVO;
import com.example.mongtron_t.service.UserPositionService;

import com.example.mongtron_t.model.UserPosition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback {
    public static GoogleMap mMap;                          //marker 와 타사용자를 한 곳에 묶기 위해 map 사용
    private MarkerFunc markerFunc;
    private UserPositionService userPositionService;

    private FusedLocationProviderClient mFusedLocationProviderClient; // Deprecated 된 FusedLocationApi 를 대체
    private LocationRequest locationRequest;

    @Override
    public void onBackPressed() {                   //뒤로가기 버튼 클릭 시 현재 액티비티 종료
        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);                   //locationRequest 를 stop 하지 않으면 다시 map 에 진입 시 계속 callback 중첩이 됨
        RetrofitRxClient.disposable.clear();
        super.onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST, renderer -> {
            Log.e("TAG", "<onMapsSdkInitialized>");
        });

        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 15000)           //위치 업데이트 주기
                .setWaitForAccurateLocation(true)
                .setMinUpdateIntervalMillis(IMPLICIT_MIN_UPDATE_INTERVAL)   //명시적인 최소 업데이트 간격을 설정
                .setMaxUpdateDelayMillis(100000)    //위치 업데이트가 지연될 수 있는 최대 시간 == 100s
                .build();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        markerFunc = new MarkerFunc();
        userPositionService = new UserPositionService(getApplicationContext());
        initButton();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {                                                    //레이아웃 크기 동적으로 할당
        super.onWindowFocusChanged(hasFocus);
        int displayHeight = MainActivity.displayHeight;

        LinearLayout linearLayout = findViewById(R.id.funcBarBox);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) linearLayout.getLayoutParams();
        layoutParams.height = displayHeight;
        layoutParams.width = (int)(displayHeight * (30.0/7.0));
        layoutParams.bottomMargin = (int) (displayHeight * (25.0/14.0));
        linearLayout.setLayoutParams(layoutParams);

    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setInfoWindowAdapter(new InfoWindowAdapter(this));

        setDefaultLocation();
        startLocationUpdates();

        //+ gps off 상태면 메세지 뛰우기
        if (!UserPosition.getInstance().isGpsState() && UserInfo.getInstance().getId() > 0) {          //gps 노출은 off 이고 로그인 상태일 때
            CustomAlterDialog customAlterDialog = new CustomAlterDialog("알림문",
                    "App 내 GPS 상태가 Off 입니다.\n서비스 이용을 위해서는 활성화가 필요합니다.", MapsActivity.this);
            customAlterDialog.getBuilder().setPositiveButton("On", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {                                //dialog 창으로 바로 gps 상태를 변경
                    userPositionService.toggleGpsState();
                }
            });

            customAlterDialog.showDialLog();
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        OtherVO user = MarkerFunc.currentMarkerMap.get(marker);
        if (user == null) return true;                                     //자신의 mark 클릭 시 이벤트 무시
        else {
            marker.showInfoWindow();
            return false;
        }
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        AddedFriendService addedFriendService = new AddedFriendService(this);
        OtherVO user = MarkerFunc.currentMarkerMap.get(marker);

        AddedFriendVO friendVO = new AddedFriendVO(user.getId(), user.getNickName(), user.getSex(), true, user.getDistance());
        if (addedFriendService.isAddedFriendVO(friendVO.getFriendId()))                          //db에 이미 존재하는 친구라면 클릭 이벤트 종료
            Toast.makeText(this, "이미 존재하는 친구", Toast.LENGTH_SHORT).show();
        else {
            addedFriendService.insertAddedFriend(friendVO);     //DB에 저장
            AddedFriendVO.friendsList.add(friendVO);        //메모리 상에 저장
            Toast.makeText(this, marker.getTitle() + "을 친구등록 완료", Toast.LENGTH_SHORT).show();
        }
    }


    private void initButton() {
        //프레그먼트(맵상의 기능바)를 load
        android.widget.Button menuMoveButton = findViewById(R.id.menuMoveButton);
        menuMoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //라운드 박스 그림자 지우기 -> 안 지우면 박스가 가장 상단에 표시 됨
                LinearLayout funcBarBox = findViewById(R.id.funcBarBox);
                funcBarBox.setElevation(0);

                //onClick과 함께 transaction 객체를 생성해 commit 오류 방지
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                FuncBarFragment fragment = new FuncBarFragment();

                Bundle bundle = new Bundle();                                                           //액티비티에서 프래그먼트로 데이터를 넘겨줌
                bundle.putString("map", String.valueOf(mMap));
                fragment.setArguments(bundle);

                transaction.replace(R.id.funcBar, fragment);
                transaction.commit();
            }
        });
        //친구 화면으로 이동
        Button friendButton = findViewById(R.id.friendListMoveButton);
        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FriendListActivity.class);
                startActivity(intent);
            }
        });
    }

    //GPS 활성화 off 시 초기 위치를 서울로 설정(휴대폰 설정)
    private void setDefaultLocation() {
        markerFunc.removeMakerAll();

        LatLng seoul = new LatLng(37.56, 126.97);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));                 // 초기 위치
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));                         // 줌의 정도
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(seoul, 17);
        mMap.moveCamera(cameraUpdate);
    }

    private void startLocationUpdates() {
        Log.e("TAG", "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");

        if (checkPermission()) {
            mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }
    }

    private boolean checkPermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                Location location = locationList.get(locationList.size() - 1); //가장 최신 location 객체를 get?

                LatLng currentPosition
                        = new LatLng(location.getLatitude(), location.getLongitude());
                String markerSnippet = "위도:" + location.getLatitude()
                        + " 경도:" + location.getLongitude();
                Log.e("TAG", "onLocationResult : " + markerSnippet);

                markerFunc.removeMakerAll();
                markerFunc.markMeLocation(markerSnippet, currentPosition);

                if (UserInfo.getInstance().getId() > 0 && UserPosition.getInstance().isGpsState()) {           //로그인 상태 + gps 표시여부가 on 이어야
                    userPositionService.storeCurrentLocation(currentPosition);
                    userPositionService.getNearbyOthersLocation(currentPosition, markerFunc);
                }
            }
        }
    };
}

