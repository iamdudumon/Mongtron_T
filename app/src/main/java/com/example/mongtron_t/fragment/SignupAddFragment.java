package com.example.mongtron_t.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.mongtron_t.R;
import com.example.mongtron_t.dialog.CustomProgressDialog;
import com.example.mongtron_t.dialog.ReSignupDialog;
import com.example.mongtron_t.dialog.SignupAddAgeDialog;
import com.example.mongtron_t.dialog.SignupAddNationalityDialog;
import com.example.mongtron_t.dialog.SignupAddSexDialog;
import com.example.mongtron_t.model.UserInfo;
import com.example.mongtron_t.service.UserInfoService;
import com.example.mongtron_t.tool.ResultMsg;


public class SignupAddFragment extends Fragment {
    View view;
    CustomProgressDialog dialog;

    UserInfoService userInfoService;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userInfoService = new UserInfoService(getContext());
        dialog = new CustomProgressDialog(getContext());

        initButton();
        initSpinner();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup_add, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    private void initButton() {
        Button signupAddNextButton = view.findViewById(R.id.signupAddNextButton);
        //서버로 회원가입 요청버튼
        signupAddNextButton.setOnClickListener(v -> {
            Log.e("TAG", UserInfo.getInstance().getEmail() + ", " + UserInfo.getInstance().getPassword());
            if (UserInfo.getInstance().getSex() != '0'
                    && !UserInfo.getInstance().getNationality().equals("null")
                    && UserInfo.getInstance().getAge() >= 0) {
                Log.e("TAG", UserInfo.getInstance().getEmail() + ", " + UserInfo.getInstance().getPassword());
                dialog.showProgressDialog();
                requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                new Thread(() -> {
                    try {
                        Log.e("TAG", UserInfo.getInstance().getEmail() + ", " + UserInfo.getInstance().getPassword());
                        Log.e("TAG", UserInfo.getInstance().getNickname());
                        ResultMsg registerResult = userInfoService.register();
                        UserInfo.getInstance().initUser();
                        dialog.cancelProgressDialog();

                        Handler handler = new Handler(Looper.getMainLooper());                  //메인 스레드 이외에서 ui 변경시 핸들러 사용
                        handler.postDelayed(() -> {
                            requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);      //터치 방지 해제
                            registerResult.showToastMsg(getContext());
                        }, 0);

                        if (registerResult.isResult()) {                              //회원가입 성공
                            Log.e("TAG", "회원가입 성공");
                            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                            SignupCompleteFragment fragment = new SignupCompleteFragment();
                            transaction.add(R.id.signupCompleteFragment, fragment);                         //회원가입 완료 프래그먼트로 전환
                            transaction.commit();
                        } else {
                            Log.e("TAG", "회원가입 실패");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });

        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            ReSignupDialog reSignupDialog = new ReSignupDialog();
            reSignupDialog.show(requireActivity().getSupportFragmentManager(), "dialog_event");
        });
    }

    private void initSpinner() {
        //스피너를 직접 구현하지 않고 스피너 모양의 버튼을 구현해 다이얼로그 프래그먼트를 load
        android.widget.Button sexFragmentButton = view.findViewById(R.id.sexFragmentButton);
        sexFragmentButton.setOnClickListener(v -> {
            SignupAddSexDialog signupAddSexFragment = new SignupAddSexDialog(view);
            signupAddSexFragment.show(requireActivity().getSupportFragmentManager(), "dialog_event");
        });

        LinearLayout nationalityFragmentButton = view.findViewById(R.id.nationalityFragmentButton);
        nationalityFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupAddNationalityDialog signupAddNationalityDialog = new SignupAddNationalityDialog(view);
                signupAddNationalityDialog.show(requireActivity().getSupportFragmentManager(), "dialog_event");
            }
        });

        android.widget.Button ageFragmentButton = view.findViewById(R.id.ageFragmentButton);
        ageFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupAddAgeDialog signupAddAgeDialog = new SignupAddAgeDialog(view);
                signupAddAgeDialog.show(requireActivity().getSupportFragmentManager(), "dialog_event");
            }
        });
    }
}