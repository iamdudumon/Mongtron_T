package com.example.mongtron_t.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mongtron_t.R;
import com.example.mongtron_t.user.AddedFriendDAO;
import com.example.mongtron_t.model.AddedFriendVO;
import com.example.mongtron_t.user.UserInfoDAO;
import com.example.mongtron_t.model.UserInfoVO;
import com.example.mongtron_t.user.UserPositionDAO;
import com.example.mongtron_t.model.UserPositionVO;


public class SettingActivity extends AppCompatActivity {
    UserInfoDAO userInfoDAO;
    UserPositionDAO userPositionDAO;
    AddedFriendDAO addedFriendDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        userInfoDAO = new UserInfoDAO(getApplicationContext());
        userPositionDAO = new UserPositionDAO(getApplicationContext());
        addedFriendDAO = new AddedFriendDAO(getApplicationContext());

        initProfileGPSSwitch();
        initButton();
        initProfileText();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(UserInfoVO.getInstance().getId() > 0 )initProfileImg();
    }

    private void initButton() {
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initProfileText() {                                                                                     //자동 로그인 정보가 있으면 사용자의 프로필 정보를 표시
        if (UserInfoVO.getInstance().getId() > 0) {
            TextView settingNicknameTextView = findViewById(R.id.settingNicknameTextView);
            TextView settingEmailTextView = findViewById(R.id.settingEmailTextView);

            settingNicknameTextView.setText(UserInfoVO.getInstance().getNickname());
            settingEmailTextView.setText(UserInfoVO.getInstance().getEmail());

            Button logoutButton = findViewById(R.id.logoutButton);                                                                //로그아웃 버튼으로 변환
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();

                    userInfoDAO.logout();                           //logout 메소드 호출
                    addedFriendDAO.deleteAllAddedFriend();          //로그아웃과 동시에 친구 목록 db 에서 삭제
                    if (AddedFriendVO.friendsList.size() > 0) AddedFriendVO.friendsList.clear();
                }
            });

        }
    }

    private void initProfileImg() {
        ImageView linearLayout = findViewById(R.id.profileImg);                                                         //프로필 화면의 기본 이미지 비율 1:1 조정
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        layoutParams.width = linearLayout.getHeight();
        linearLayout.setLayoutParams(layoutParams);

        TextView profileAgeTextView = findViewById(R.id.profileAgeTextView);
        TextView profileNationalityTextView = findViewById(R.id.profileNationalityTextView);
        TextView profileSexTextView = findViewById(R.id.profileSexTextView);


        //Age 항목 각 텍스트 크기와 속성을 달리 함
        String userAge = String.valueOf(UserInfoVO.getInstance().getAge());
        String ageLabel = userAge + "\nAge";

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(ageLabel);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(24, true), 0, userAge.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.BLACK), 0, userAge.length()
                , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(14, true), userAge.length(), ageLabel.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.main_background)), userAge.length(), ageLabel.length()
                , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        profileAgeTextView.setText(spannableStringBuilder);

        //국적마다 다른 이미지를 load
        Drawable topDrawable = profileNationalityTextView.getCompoundDrawables()[1];
        //성별 이미지와 국적 이미지의 크기를 일정하게 맞춤
        //먼저 국적 이미지의 크기를 read
        int nationalityHeight = topDrawable.getIntrinsicHeight();
        int nationalityWidth = topDrawable.getIntrinsicWidth();

        Drawable newDrawable = null;
        switch (UserInfoVO.getInstance().getNationality()) {
            case "USA":
                newDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.usa_img);
                break;
            case "Korea":
                newDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.korea_img);
                break;
            case "Japan":
                newDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.japan_img);
                break;
            case "UK":
                newDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.uk_img);
                break;
            case "Germany":
                newDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.germany_img);
                break;
            case "Russia":
                newDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.russia_img);
                break;
            case "Turkey":
                newDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.turkey_img);
                break;
            case "China":
                newDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.china_img);
        }
        assert newDrawable != null;
        newDrawable.setBounds(topDrawable.getBounds());
        profileNationalityTextView.setCompoundDrawables(null, newDrawable, null, null);

        //성별에 따른 이미지 변경
        topDrawable = profileSexTextView.getCompoundDrawables()[1];
        switch (UserInfoVO.getInstance().getSex()) {
            case '1':
                newDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.profile_male_img);
                break;
            case '2':
                newDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.profile_female_img);
                break;
        }
        assert newDrawable != null;
        newDrawable.setBounds(topDrawable.getBounds());
        //성별 이미지 크기를 국적 이미지 크기와 똑같이 설정
        Drawable resizedDrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(((BitmapDrawable) newDrawable).getBitmap(), dpToPx(), dpToPx(), true));
        resizedDrawable.setBounds(0, 0, nationalityWidth, nationalityHeight);
        profileSexTextView.setCompoundDrawables(null, resizedDrawable, null, null);


    }

    private int dpToPx() {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(30 * density);
    }

    private void initProfileGPSSwitch() {
        SwitchCompat gpsSwitch = findViewById(R.id.gpsSwitch);
        gpsSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPositionDAO.toggleGpsState();
            }
        });
        gpsSwitch.setChecked(UserPositionVO.getInstance().isGpsState());
    }
}