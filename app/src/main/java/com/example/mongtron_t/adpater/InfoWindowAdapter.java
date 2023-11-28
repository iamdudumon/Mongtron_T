package com.example.mongtron_t.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mongtron_t.R;
import com.example.mongtron_t.model.OtherVO;
import com.example.mongtron_t.tool.MarkerFunc;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private final Context context;

    public InfoWindowAdapter(Context context) {
        this.context = context;
        this.mWindow = LayoutInflater.from(this.context).inflate(R.layout.custom_info_window, null);
    }

    private void renderCustomInfoWindow(Marker marker){
        OtherVO markerUser = MarkerFunc.currentMarkerMap.get(marker);

        // Set the title and snippet text views of the info window
        TextView infoWindowNicknameTextView = mWindow.findViewById(R.id.infoWindowNicknameTextView);
        TextView infoWindowAgeTextView = mWindow.findViewById(R.id.infoWindowAgeTextView);
        TextView infoWindowSexTextView = mWindow.findViewById(R.id.infoWindowSexTextView);
        ImageView infoWindowImgView = mWindow.findViewById(R.id.infoWindowImgView);


        assert markerUser != null;
        infoWindowNicknameTextView.setText(markerUser.getNickName());
        infoWindowAgeTextView.setText(String.valueOf(markerUser.getAge()));

        char userSex = markerUser.getSex();
        infoWindowSexTextView.setText(userSex== '0' ? "Sex" : userSex == '1' ? "Male" : "Female");
        infoWindowImgView.setImageResource(userSex== '0' ?
                R.drawable.profile_null_img : userSex == '1' ?
                R.drawable.profile_male_img : R.drawable.profile_female_img);

    }

//    private void initButton(Marker marker, Context context){        //구글 map api에서 말풍선은 단순 화면을 렌더링해서 버튼을 추가 못 함!
//        android.widget.Button infoWindowAddFriendButton = mWindow.findViewById(R.id.infoWindowAddFriendButton);
//        infoWindowAddFriendButton.setOnClickListener(v -> {
//            Log.e("TAG", "친구등롞!!");
//            AddedFriendDAO addedFriendDAO = new AddedFriendDAO(context);
//            OthersVO user = MarkerFunc.currentMarkerMap.get(marker);
//            if (user == null) return;                                     //자신의 mark 클릭 시 이벤트 무시
//
//            AddedFriendVO friendVO = new AddedFriendVO(user.getId(), user.getNickName(), user.getSex(), true, user.getDistance());
//            addedFriendDAO.insertAddedFriend(friendVO);     //DB에 저장
//            AddedFriendVO.friendsList.add(friendVO);        //메모리 상에 저장
//        });
//    }

    @Override
    public View getInfoWindow(@NonNull Marker marker) { //배경 및 스타일 포함하여 전체 모양을 사용자 지정
        renderCustomInfoWindow(marker);
        return mWindow;
    }

    @Override
    public View getInfoContents(@NonNull Marker marker) {   //기본 배경 및 스타일을 그대로 유지하면서 콘텐츠만 사용자 지정

        return mWindow;
    }
}