package com.example.mongtron_t.service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.mongtron_t.http.RetrofitClient;
import com.example.mongtron_t.model.UserInfoVO;
import com.example.mongtron_t.model.UserPositionVO;
import com.example.mongtron_t.response.LoginResponse;
import com.example.mongtron_t.response.OthersResponse;

import java.util.Objects;

public class UserInfoService {                                  //사용자 정보를 내부 파일 SharedPreferences Class 에 load/store 하는 Class
    private final Context context;
    SharedPreferences autologin;
    SharedPreferences.Editor autoLoginEditor;

    public UserInfoService(Context context) {
        this.context = context;
        autologin = this.context.getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        autoLoginEditor = autologin.edit();
    }

    private void storeLoginInfo() {
        //자동 로그인을 위한 SharedPreferences Class 에 user 정보 저장
        autoLoginEditor.putInt("id", UserInfoVO.getInstance().getId());
        //autoLoginEditor.putString("name", UserInfoVO.getInstance().getName());
        autoLoginEditor.putString("nickName", UserInfoVO.getInstance().getNickname());
        autoLoginEditor.putString("email", UserInfoVO.getInstance().getEmail());
        autoLoginEditor.putString("password", UserInfoVO.getInstance().getPassword());
        autoLoginEditor.putInt("age", UserInfoVO.getInstance().getAge());
        autoLoginEditor.putString("sex", String.valueOf(UserInfoVO.getInstance().getSex()));
        autoLoginEditor.putString("nationality", UserInfoVO.getInstance().getNationality());
        autoLoginEditor.putString("embassyNum", UserInfoVO.getInstance().getEmbassyNum());
        autoLoginEditor.commit();
    }

    public void autoLogin() {                                    //자동 로그인 함수가 한번만 호출될까?


        if (autologin.getInt("id", -1) > 0) {                               //자동 로그인 할 유저 정보가 존재하면
            int id = autologin.getInt("id", -1);
            String email = autologin.getString("email", null);
            String password = autologin.getString("password", null);
            //String name = autologin.getString("name", null);
            String nickName = autologin.getString("nickName", null);
            int age = autologin.getInt("age", -1);
            //String phoneNo = autologin.getString("phoneNo", null);
            char sex = autologin.getString("sex", "0").charAt(0);
            String nationality = autologin.getString("nationality", null);
            String embassyNum = autologin.getString("embassyNum", null);

            UserInfoVO.getInstance().setUser(email, password, nickName, age, sex, nationality, embassyNum);
            UserInfoVO.getInstance().setId(id);
        } else
            UserInfoVO.getInstance().initUser();
    }

    //공개키를 저장하고 불러오는 메소드
//    public void storePublicKey(String publicKey) {
//        autoLoginEditor.putString("publicKey", publicKey);
//        autoLoginEditor.commit();
//    }
//
//    public String loadPublicKey() {
//        return autologin.getString("publicKey", null);
//    }

    public boolean register() {
        //회원가입 진행
        RetrofitClient retrofitClient = new RetrofitClient();
        int registerResponse = retrofitClient.registerPost();    //code 200이면 정상적인 회원가입, 아닐 시 회원가입 실패
        boolean result;
        String toastMsg;

        if (registerResponse == 200) {                                    //회원 가입의 결과에 따라 변수 값 할당
            toastMsg = "Successfully registered as a " + UserInfoVO.getInstance().getNickname();
//            UserInfoVO.getInstance().setId(registerResponse.getId());                                 //User 객체에 정상적인 id가 대입되면서 등록 사용자로 판단
            result = true;
        } else {
            toastMsg = "Server Intentional Error";
            result = false;
        }

        Handler handler = new Handler(Looper.getMainLooper());                  //쓰레드에서 토스트 메세지를 뛰우기 위해 handler 사용
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show();
            }
        }, 0);
        return result;
    }

    public boolean login() {
        RetrofitClient retrofitClient = new RetrofitClient();
        LoginResponse loginResponse = retrofitClient.loginPost();    //code 200이면 정상적인 로그인, 아닐 시 로그인 실패
        boolean result;
        String toastMsg;

        if(loginResponse == null){
            toastMsg = "Disconnected with server...";
            result = false;
        }
        else {
            if (loginResponse.getId() != -1) {                                    //회원 가입의 결과에 따라 변수 값 할당
                toastMsg = "Successfully logged in as a " + loginResponse.getNickName();
                UserInfoVO.getInstance().setUser(loginResponse.getEmail(), UserInfoVO.getInstance().getPassword(), //서버로 받아온 유저 정보를 userVO 객체에 할당
                        loginResponse.getNickName(), loginResponse.getAge(), Objects.equals(loginResponse.getSex(), "male") ? '1' : '2', loginResponse.getNationality(), loginResponse.getEmbassyNum());
                UserInfoVO.getInstance().setId(loginResponse.getId());
                result = true;

                storeLoginInfo();
            } else {
                toastMsg = "The email or password is incorrect.";
                result = false;
            }
        }

        Handler handler = new Handler(Looper.getMainLooper());                  //쓰레드에서 토스트 메세지를 뛰우기 위해 handler 사용
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show();
            }
        }, 0);
        return result;
    }

    public boolean emailCheck(String email){
        RetrofitClient retrofitClient = new RetrofitClient();
        return retrofitClient.emailCheckGet(email);
    }

    public boolean nicknameCheck(String nickname) {
        RetrofitClient retrofitClient = new RetrofitClient();
        return retrofitClient.nicknameCheckGet(nickname);
    }

    public void logout() {
        String publicKey =  autologin.getString("publicKey", null);

        Toast.makeText(context, autologin.getString("nickName", "--") + "님이 logout", Toast.LENGTH_SHORT).show();
        autoLoginEditor.clear();
        autoLoginEditor.commit();
        //기존의 정보 모두 clear
        UserInfoVO.getInstance().initUser();
        UserPositionVO.getInstance().initUserPosition();
        OthersResponse.getInstance().initListPosition();

        //RSA 공개 키는 다시 저장
        autoLoginEditor.putString("publicKey", publicKey);
        autoLoginEditor.commit();
    }

}