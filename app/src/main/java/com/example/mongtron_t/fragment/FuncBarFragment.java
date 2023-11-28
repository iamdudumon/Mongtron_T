package com.example.mongtron_t.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


import com.example.mongtron_t.R;
import com.example.mongtron_t.activity.MapsActivity;
import com.example.mongtron_t.dialog.CustomAlterDialog;
import com.example.mongtron_t.dialog.RadiusInfoDialog;
import com.example.mongtron_t.model.UserInfoVO;
import com.example.mongtron_t.model.UserPositionVO;
import com.google.android.gms.maps.GoogleMap;

public class FuncBarFragment extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_func_bar,container,false);
        initButton();

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initButton(){
        //fragment 상 뒤로가기(나가기 버튼)
        ImageButton exitButton = view.findViewById(R.id.funcBarExitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //다른 view에 있는 요소들 접근 불가!
//                LinearLayout funcBarBox = view.findViewById(R.id.funcBarBox);
//                funcBarBox.setElevation(20);

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.detach(FuncBarFragment.this);
                transaction.commit();
            }
        });

        ImageButton normalMapButton = view.findViewById(R.id.normalMapButton);
        normalMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapsActivity.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        ImageButton hybridMapButton = view.findViewById(R.id.hybridMapButton);
        hybridMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapsActivity.mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });


        Button radiusInfoButton = view.findViewById(R.id.radiusInfoButton);
        radiusInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!UserPositionVO.getInstance().isGpsState() || UserInfoVO.getInstance().getId() < 0){                  //로그아웃 상태이거나 gps가 off이면 반경정보 설정 못 하도록
                    CustomAlterDialog customAlterDialog = new CustomAlterDialog("알림문",
                            "App 내 GPS 상태가 Off 입니다.\n서비스 이용을 위해서는 활성화가 필요합니다.", getActivity());
                    customAlterDialog.showDialLog();
                }
                else {
                    RadiusInfoDialog radiusInfoDialogFragment = new RadiusInfoDialog();
                    radiusInfoDialogFragment.show(requireActivity().getSupportFragmentManager(), RadiusInfoDialog.TAG_EVENT_DIALOG);
                }
            }
        });

    }

}