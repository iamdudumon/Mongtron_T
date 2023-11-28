package com.example.mongtron_t.dialog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mongtron_t.R;
import com.example.mongtron_t.user.UserPositionDAO;
import com.example.mongtron_t.model.UserPositionVO;


public class RadiusInfoDialog extends DialogFragment {
    public static final String TAG_EVENT_DIALOG = "dialog_event";

    View view;
    private TextView radiusInfoTextView;
    private int radiusInfoValue;

    public RadiusInfoDialog(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.dialog_raidus_info_dialog,container,false);
        initView();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initView(){
        radiusInfoValue = UserPositionVO.getInstance().getRadiusInfo();
        radiusInfoTextView = view.findViewById(R.id.radiusInfoTextView);
        radiusInfoTextView.setText(String.valueOf(radiusInfoValue));

        android.widget.Button radiusInfoDownButton = view.findViewById(R.id.radiusInfoDownButton);
        android.widget.Button radiusInfoUpButton = view.findViewById(R.id.radiusInfoUpButton);
        android.widget.Button radiusInfoSaveButton = view.findViewById(R.id.radiusInfoSaveButton);
        android.widget.Button radiusInfoCancelButton = view.findViewById(R.id.radiusInfoCancelButton);

        //반경 정보 최대값 50, 최소 값 1로 설정
        radiusInfoDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radiusInfoValue >= 2)
                    radiusInfoTextView.setText(String.valueOf(--radiusInfoValue));
            }
        });
        radiusInfoUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radiusInfoValue <= 49)
                    radiusInfoTextView.setText(String.valueOf(++radiusInfoValue));
            }
        });
        radiusInfoSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserPositionDAO userPositionDAO = new UserPositionDAO(getActivity());
                userPositionDAO.storeRadiusInfo(radiusInfoValue);
                dismiss();
            }
        });
        radiusInfoCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}