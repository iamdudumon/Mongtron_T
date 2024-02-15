package com.example.mongtron_t.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mongtron_t.R;
import com.example.mongtron_t.dialog.CustomAlterDialog;
import com.example.mongtron_t.model.UserInfo;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> activityResultLauncher;
    PermissionListener permissionlistener;

    static int displayHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.e("TAG", "Back 완료");
                    }
                }
        );
        permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "권한 허가", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "권한 거부" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        //위치 활성화 체크 후 권한 체크
        if(!checkLocationServicesStatus()){
            CustomAlterDialog customAlterDialog = new CustomAlterDialog("위치 서비스 비활성화",
                    "앱을 사용하기 위해서는 위치 서비스가 필요합니다.\\n\"\n" +
                            "                + \"위치 설정을 수정하실래요?", MainActivity.this);
            customAlterDialog.getBuilder().setPositiveButton("설정", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent callGPSSettingIntent
                            = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    activityResultLauncher.launch(callGPSSettingIntent);
                }
            });
            customAlterDialog.showDialLog();
        }
        checkPermission();
        initButton();                                                                   //view 들을 초기화
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {                                                    //레이아웃 크기 동적으로 할당
        super.onWindowFocusChanged(hasFocus);

        LinearLayout linearLayout = findViewById(R.id.mainButtonBox);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        layoutParams.width = (int)(linearLayout.getHeight() * (30.0/7.0));
        linearLayout.setLayoutParams(layoutParams);
        displayHeight = linearLayout.getHeight();
    }

    //사용자에게 권한을 체크 후 권한 수락
    private void checkPermission() {
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("앱 사용을 위한 권한 설정이 필요합니다.")
                .setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.CALL_PHONE
                )
                .check();
    }

    //GPS 활성화 유무 판단
    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void initButton(){
        Button settingMoveButton = findViewById(R.id.settingMoveButton);
        settingMoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그인 상태면 프로필 화면 전환 아닐 시 로그인 화면으로 전환
                Intent intent = UserInfo.getInstance().getId() > 0 ? new Intent((getApplicationContext()), SettingActivity.class) : new Intent((getApplicationContext()), LoginActivity.class);
                startActivity(intent);
            }
        });

        Button mapMoveButton = findViewById(R.id.mapMoveButton);
        mapMoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        //대사관으로 SOS call
        ImageButton sosCallButton = findViewById(R.id.sosCallButton);
        sosCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그인 상태에서만 sos call 기능 가능
                if (UserInfo.getInstance().getId() > 0) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + UserInfo.getInstance().getEmbassyNum().replace("-", "")));
                    try {
                        startActivity(callIntent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}