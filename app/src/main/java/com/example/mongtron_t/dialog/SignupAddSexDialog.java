package com.example.mongtron_t.dialog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.mongtron_t.R;
import com.example.mongtron_t.model.UserInfoVO;

public class SignupAddSexDialog extends DialogFragment {
    View view;
    View addView;

    public SignupAddSexDialog(View addView){
        this.addView = addView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_signup_add_sex, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    private void initButton(){
        android.widget.Button sexDialogCancelButton = view.findViewById(R.id.sexDialogCancelButton);
        android.widget.Button sexDialogSaveButton  = view.findViewById(R.id.sexDialogSaveButton);

        sexDialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        sexDialogSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sex = readDialogSexValue() == '1' ? "male" : "female";
                android.widget.Button sexFragmentButton = addView.findViewById(R.id.sexFragmentButton);
                sexFragmentButton.setText(sex);
                sexFragmentButton.setTextColor(getResources().getColor(R.color.black));

                UserInfoVO.getInstance().setSex(readDialogSexValue());
                dismiss();
            }
        });
    }

    public char readDialogSexValue() {
        RadioButton male = view.findViewById(R.id.maleRadioButton);
        RadioButton female = view.findViewById(R.id.femaleRadioButton);

        /*기본 값으로 '0'을 가짐
         * "남자"radio button 체크되면 => Sex 값에 '1'
         "여자"radio button 체크되면 => Sex 값에 '2' */
        if (male.isChecked()) {
            //male.setDra
            return '1';
        } else if (female.isChecked()) {
            return '2';
        }
        return '0';
    }
}