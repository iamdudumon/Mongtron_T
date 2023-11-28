package com.example.mongtron_t.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import androidx.core.content.ContextCompat;


import com.example.mongtron_t.R;
import com.example.mongtron_t.activity.MapsActivity;
import com.example.mongtron_t.model.OtherVO;
import com.example.mongtron_t.response.OthersResponse;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkerFunc {                                                           //maker 객체를 다루는 메소드를 정의하는 class
    public static final Map<Marker, OtherVO> currentMarkerMap = new HashMap<>();

    public void markMeLocation(String markerSnippet, LatLng currentLatLng) {        //자신의 위치 Maker 표시
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title("현재위치");
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);

        currentMarkerMap.put(MapsActivity.mMap.addMarker(markerOptions), null);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        MapsActivity.mMap.moveCamera(cameraUpdate);
    }

    public void markUsersLocation(Context context) {                                                            //타사용자 위치 Marker 표시
        MarkerOptions markerOptions = new MarkerOptions();
        List<OtherVO> users = OthersResponse.getInstance().getUsers();

        for (int i = 0; i < users.size(); i++) {
            OtherVO user = users.get(i);
            markerOptions.position(new LatLng(user.getLatitude(), user.getLongitude()));
            markerOptions.title(user.getNickName() + "님");
            markerOptions.snippet("Age: " + user.getAge());
            markerOptions.draggable(true);

            int marketImg = R.drawable.profile_null_img;
            switch (user.getSex()) {                            //기본 값은 null 이미지고 성별에 따른 아이콘 이미지가 변환
                case '1':
                    marketImg = R.drawable.marker_male_img;
                    break;
                case '2':
                    marketImg = R.drawable.marker_female_img;
                    break;
                default:
                    break;
            }
            //일반 png 이미지를 bitmap 으로 변환해야함
            BitmapDrawable bitmap = (BitmapDrawable) ContextCompat.getDrawable(context, marketImg);
            assert bitmap != null;
            Bitmap b = bitmap.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 125, 125, false);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

            currentMarkerMap.put(MapsActivity.mMap.addMarker(markerOptions), user);
        }
    }

    public void removeMakerAll() {                              //Marker 배열을 다 삭제 -> 초기화
        if (currentMarkerMap.size() != 0)
            for(Marker marker : currentMarkerMap.keySet()){
                marker.remove();
            }
    }
}
