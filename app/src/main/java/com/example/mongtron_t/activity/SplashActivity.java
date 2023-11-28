package com.example.mongtron_t.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mongtron_t.R;
import com.example.mongtron_t.http.RetrofitClient;
import com.example.mongtron_t.http.RetrofitRxClient;
import com.example.mongtron_t.user.AddedFriendDAO;
import com.example.mongtron_t.user.UserInfoDAO;
import com.example.mongtron_t.user.UserPositionDAO;

import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity {
    UserInfoDAO userInfoDAO;
    UserPositionDAO userPositionDAO;
    AddedFriendDAO addedFriendDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView gif_image = findViewById(R.id.splash_gif);
        Glide.with(this).load(R.drawable.splash_gif).into(gif_image);

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }, 300); // 3초 후(3000) 스플래시 화면을 닫습니다 (보통 사용하는 시간)
        RetrofitClient.setRetrofit();                           //앱 실행시 초기 한번만 실행할 수 있도록...
        RetrofitRxClient.setRetrofit();

        userInfoDAO = new UserInfoDAO(getApplicationContext());
        userPositionDAO = new UserPositionDAO(getApplicationContext());
        addedFriendDAO = new AddedFriendDAO(getApplicationContext());
        //자동 로그인 시 저장할 메모리에 저장할 요소들...
        userInfoDAO.autoLogin();
        userPositionDAO.autoLogin();
        addedFriendDAO.autoLogin();

        new Thread(() -> {
            //서버로부터 공개키를 수신
//            RetrofitClient retrofitClient = new RetrofitClient();
//            String publicKey = retrofitClient.startGET();
//            publicKey = publicKey.replace("-----BEGIN PUBLIC KEY-----\n", "").replace("-----END PUBLIC KEY-----\n", "");
//
//            //수신받은 공개키를 내부 파일에 저장
//            userInfoDAO.storePublicKey(publicKey);
//
//            //서버와 자동 로그인 통신
//
//
//           //RSA 공개 키로 AES 키를 암호화 -> AES static 변수에 저장
//            try {
//                RSA rsa = new RSA();
//                rsa.generateRSAPublicKey(publicKey);
//                rsa.encryptedAESKey(AES.aesKey, rsa.getRSAPublicKey());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            //AES 키 생성
//            AES aes = new AES();
//            try {
//                aes.generateAESKey();
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            }
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            //메인 액티비티로 이동
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }).start();
    }
}