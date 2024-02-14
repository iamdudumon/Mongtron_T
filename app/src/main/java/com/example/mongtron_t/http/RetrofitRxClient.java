package com.example.mongtron_t.http;

import com.example.mongtron_t.response.AddedFriendResponse;
import com.example.mongtron_t.response.OthersResponse;
import com.example.mongtron_t.model.UserInfoVO;
import com.example.mongtron_t.model.UserPositionVO;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRxClient {
    static private RetrofitService retrofitService;
    static public CompositeDisposable disposable;                   //나중에 앱이 비정상적으로 종료할 때 disposable.clear() 메소드를 호출하자 모든 액티비티에서

    static public void setRetrofit() {
        final String BASEURL = "http://210.123.135.176:5877/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
        disposable = new CompositeDisposable();
    }

    public Single<AddedFriendResponse> friendListGet(){
        return retrofitService.doGetFriendList(UserInfoVO.getInstance().getId());
    }

    static public Single<OthersResponse> nearbyOthersGet(){
        return retrofitService.doGetNearbyOthers(UserInfoVO.getInstance().getId(),
                UserPositionVO.getInstance().getLatitude(), UserPositionVO.getInstance().getLongitude(), UserPositionVO.getInstance().getRadiusInfo());
    }
}
