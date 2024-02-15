package com.example.mongtron_t.service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.mongtron_t.http.RetrofitClient;
import com.example.mongtron_t.http.RetrofitRxClient;
import com.example.mongtron_t.model.UserPositionVO;
import com.example.mongtron_t.response.OthersResponse;
import com.example.mongtron_t.tool.MarkerFunc;
import com.google.android.gms.maps.model.LatLng;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserPositionService {
    private final Context context;
    SharedPreferences autologin;
    SharedPreferences.Editor autoLoginEditor;

    public UserPositionService(Context context) {
        this.context = context;
        autologin = this.context.getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        autoLoginEditor = autologin.edit();
    }

    public void autoLogin(){
        RetrofitClient.setRetrofit();                           //앱 실행시 초기 한번만 실행할 수 있도록...

        if (autologin.getInt("id", -1) > 0) {
            boolean gpsState = autologin.getBoolean("gpsState", false);
            int radiusInfo = autologin.getInt("radiusInfo", 5);                           //기본 값 5km

            if (gpsState != UserPositionVO.getInstance().isGpsState())
                UserPositionVO.getInstance().setGpsState();
            UserPositionVO.getInstance().setRadiusInfo(radiusInfo);
        }
    }

    public void toggleGpsState(){
        UserPositionVO.getInstance().setGpsState();                                             //gps 상태를 반대로 toggle
        autoLoginEditor.putBoolean("gpsState", UserPositionVO.getInstance().isGpsState());
        autoLoginEditor.commit();

        RetrofitClient.stateUpdatePatch();
    }

    public void storeRadiusInfo(int radiusInfo){
        UserPositionVO.getInstance().setRadiusInfo(radiusInfo);
        autoLoginEditor.putInt("radiusInfo", radiusInfo);
        autoLoginEditor.commit();
    }

    public void getNearbyOthersLocation(LatLng currentPosition, MarkerFunc markerFunc){
        UserPositionVO.getInstance().setGpsPosition((float) currentPosition.latitude, (float) currentPosition.longitude);

        RetrofitRxClient.disposable.add(RetrofitRxClient.nearbyOthersGet()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<OthersResponse>() {
                    @Override
                    public void onSuccess(@NonNull OthersResponse othersResponse) {
                        if (othersResponse.getCode() == 400) {
                            Log.e("TAG", "런타임 오류 GPS Update 실패");
                            return;
                        }
                        OthersResponse.getInstance().setLength(othersResponse.getLength());

                        if (othersResponse.getUsers() != null) {
                            OthersResponse.getInstance().setUsers(othersResponse.getUsers());
                            markerFunc.markUsersLocation(context);

                            for (int i = 0; i < othersResponse.getLength(); i++)
                                Log.e("TAG", i + ": " + othersResponse.getUsers().get(i).getNickName() + "(distance: " + othersResponse.getUsers().get(i).getDistance() + ")");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("TAG", "Server 와 Disconnected!");
                    }
                })
        );
        //gps 공유나, 채팅을 사용하면 retrofitClient 객체를 굳이 삭제하고 다시 생성하고를 반복해야 할까?
        //Solution1. call을 애초에 execute(비동기)로 실행하고 thread 처리를 한다.
        //Solution2. observer pattern 사용?
    }

    public void storeCurrentLocation(LatLng currentPosition) {
        UserPositionVO.getInstance().setGpsPosition((float) currentPosition.latitude, (float) currentPosition.longitude);
        RetrofitClient.coordinateUpdatePatch();
    }
}
