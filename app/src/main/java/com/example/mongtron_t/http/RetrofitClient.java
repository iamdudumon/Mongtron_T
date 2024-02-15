package com.example.mongtron_t.http;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mongtron_t.model.UserInfo;
import com.example.mongtron_t.response.LoginResponse;
import com.example.mongtron_t.model.UserPosition;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static RetrofitService retrofitService;                     //위치 업데이트를 위해 업데이트마다 멤버변수를 재 생성하기 보단 static 으로 고정?

    static public void setRetrofit() {
        final String BASEURL = "http://210.123.135.176:5877/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
    }

//    public String startGET() {
//        Call<String> call = retrofitService.doGETStart();
//        Response<String> response = null;
//        try {
//            response = call.execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        assert Objects.requireNonNull(response).body() != null;
//        return response.body();
//    }

    public boolean registerPost() {
        Call<Void> call = retrofitService.doPostRegister(UserInfo.getInstance().getEmail(), UserInfo.getInstance().getPassword()
                , UserInfo.getInstance().getNickname(), UserInfo.getInstance().getAge(), UserInfo.getInstance().getSex(), UserInfo.getInstance().getNationality());

        Response<Void> response;            //retrofit 통신을 동기화 처리, execute() 메소드를 호출하고 결과값을 리턴받음
        try {
            response = call.execute();
            return response.code() == 200;
        }catch (IOException e) {
            Log.e("TAG", "Disconnected With Server");
            e.printStackTrace();
            return false;
        }
    }

    public LoginResponse loginPost() {
        Call<LoginResponse> call = retrofitService.doPostLogin(UserInfo.getInstance().getEmail(), UserInfo.getInstance().getPassword());

        Response<LoginResponse> response;
        try {
            response = call.execute();
            return response.body();
        }catch (IOException e) {
            Log.e("TAG", "Disconnected With Server");
            e.printStackTrace();
            return null;
        }
    }

//    public void aesKeyCheckPost(String encryptedAESKeyToString, Pair<String, String> encryptedEmailPair) throws Exception {
//        Log.e("TAG", "암호화된 이메일: " + encryptedEmailPair.first);
//
//        Call<Integer> call = retrofitService.doPostAESKeyCheck(encryptedAESKeyToString, encryptedEmailPair.first, encryptedEmailPair.second);
//        Response<Integer> response;
//        response = call.execute();
//        if (response.code() != 200 || response.body() != 200) {
//            Log.e("TAG", "런타임 오류, AES 키 체크 실패");
//        }
//    }

    public boolean emailCheckGet(String email) {
        Call<Void> call = retrofitService.doGetEmailCheck(email);

        Response<Void> response;
        try {
            response = call.execute();
            if (response.isSuccessful()) {
                return false;
            }
            else if(response.code() == 409){
                return true;
            }
            else {
                Log.e("TAG", "code: " + response.code());
                throw new RuntimeException("Server Intentional Error");
            }
        }catch (IOException e){
            Log.e("TAG", "Disconnected With Server");
            e.printStackTrace();
            return true;
        }

    }

    public boolean nicknameCheckGet(String nickname) {
        Call<Void> call = retrofitService.doGetNicknameCheck(nickname);

        Response<Void> response;
        try {
            response = call.execute();
            if (response.code() == 200) {
                return false;
            }
            else if(response.code() == 409){
                return true;
            }
            else {
                throw new RuntimeException("Server Intentional Error");
            }
        } catch (RuntimeException e) {
            Log.e("TAG", "Server Intentional Error");
            e.printStackTrace();
            return true;
        }catch (IOException e) {
            Log.e("TAG", "Disconnected With Server");
            e.printStackTrace();
            return true;
        }
    }

    static public void coordinateUpdatePatch(){
        Call<Void> call = retrofitService.doPatchCoordinateUpdate(UserInfo.getInstance().getId(),
                UserPosition.getInstance().getLatitude(), UserPosition.getInstance().getLongitude(), UserPosition.getInstance().getRadiusInfo());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
    static public void stateUpdatePatch() {
        Call<Integer> call = retrofitService.doPatchStateUpdate(UserInfo.getInstance().getId(), UserPosition.getInstance().isGpsState());

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (!response.isSuccessful() || response.code() == 400) {
                    Log.e("TAG", "런타임 오류 GPS State Update 실패: " + response.code());
                    stateUpdatePatch(); //다시 서버와 통신
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                Log.e("TAG", "Server 와 DisConnected!" + "\n" + t.getMessage());
            }
        });
    }

    public void friendAddPost(int friendId) {
        Call<Void> call = retrofitService.doPostFriendAdd(UserInfo.getInstance().getId(), friendId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful() || response.code() == 400) {
                    Log.e("TAG", "런타임 오류 친구 등록 실패: " + response.code());
//                    friendAddPatch(friendId);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, Throwable t) {
                Log.e("TAG", "Server 와 DisConnected!" + "\n" + t.getMessage());
            }
        });
    }

    public void friendRemoveDelete(int friendId) {
        Call<Void> call = retrofitService.doDeleteFriendRemove(UserInfo.getInstance().getId(), friendId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful() || response.code() == 400) {
                    Log.e("TAG", "런타임 오류 친구 삭제 실패: " + response.code());
//                    friendDeletePatch(friendId);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, Throwable t) {
                Log.e("TAG", "Server 와 DisConnected!");
            }
        });
    }

}
