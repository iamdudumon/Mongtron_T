package com.example.mongtron_t.http;


import com.example.mongtron_t.response.AddedFriendResponse;
import com.example.mongtron_t.response.LoginResponse;
import com.example.mongtron_t.response.OthersResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitService {
    //시작 스플래시 화면
    @GET("start")
    Call<String> doGETStart();

    //회원가입, 로그인과 관련
    // @GET( EndPoint-자원위치(URI) )
    @FormUrlEncoded
    @POST("register")
    Call<Void> doPostRegister(@Field("email") String email, @Field("password") String password,
                                          @Field("nickname") String nickname, @Field("age") int age , @Field("sex") char sex, @Field("nationality") String nationality);       //@Body 는 클라이언트가 서버로 보내는 data 형식
    @FormUrlEncoded
    @POST("users/login")
    Call<LoginResponse> doPostLogin(@Field("email") String email, @Field("password") String password);
    @GET("users/email/{email}")
    Call<Void> doGetEmailCheck(@Path("email") String email);
    @GET("nicknameCheck/{nickname}")
    Call<Void> doGetNicknameCheck(@Path("nickname") String nickname);

    //위치 정보와 관련
    @FormUrlEncoded
    @POST("gps/position")
    Single<OthersResponse> doPostPositionUpdate(@Field("id") int id, @Field("nickName") String nickName, @Field("latitude") double latitude, @Field("longitude") double longitude, @Field("radiusInfo") int radiusInfo);
    @FormUrlEncoded
    @PATCH("gps/state/{id}")
    Call<Integer> doPatchStateUpdate(@Path("id") int id, @Field("gpsState") boolean gpsState);

    //친구 목록과 관련
    @PATCH("friend/add/{my_id}/{friend_id}")
    Call<Integer> doPatchFriendAdd(@Path("my_id") int myId, @Path("friend_id") int friendId);
    @PATCH("friend/delete/{my_id}/{friend_id}")
    Call<Integer> doPatchFriendDelete(@Path("my_id") int myId, @Path("friend_id") int friendId);
    @GET("friend/select/{id}")
    Single<AddedFriendResponse> doGetFriendList(@Path("id") int id);

    //보안 관련
//    @FormUrlEncoded
//    @POST("aesKeyCheck")
//    Call<Integer> doPostAESKeyCheck(@Field("aesKey") String encryptedAESKey, @Field("email") String email, @Field("iv") String iv);

}
