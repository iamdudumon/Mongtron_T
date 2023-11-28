package com.example.mongtron_t.dialog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.mongtron_t.R;
import com.example.mongtron_t.model.UserInfoVO;

public class SignupAddAgeDialog extends DialogFragment {
    View view;
    View addView;

    public SignupAddAgeDialog(View addView){
        this.addView = addView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_signup_add_age, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    private void initButton(){
        android.widget.Button ageDialogCancelButton = view.findViewById(R.id.ageDialogCancelButton);
        android.widget.Button ageDialogSaveButton  = view.findViewById(R.id.ageDialogSaveButton);

        ageDialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ageDialogSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int age = readDialogAgeValue();
                android.widget.Button ageFragmentButton = addView.findViewById(R.id.ageFragmentButton);
                ageFragmentButton.setText(String.valueOf(age));
                ageFragmentButton.setTextColor(getResources().getColor(R.color.black));

                UserInfoVO.getInstance().setAge(age);
                dismiss();
            }
        });
    }

    private int readDialogAgeValue(){
        EditText signupAgeEditText = view.findViewById(R.id.signupAgeEditText);
        return Integer.parseInt(signupAgeEditText.getText().toString().equals("") ? "0" : signupAgeEditText.getText().toString());
    }
}