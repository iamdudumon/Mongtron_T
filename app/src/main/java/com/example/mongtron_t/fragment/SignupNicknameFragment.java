package com.example.mongtron_t.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mongtron_t.R;
import com.example.mongtron_t.dialog.CustomProgressDialog;
import com.example.mongtron_t.dialog.ReSignupDialog;
import com.example.mongtron_t.service.UserInfoService;
import com.example.mongtron_t.model.UserInfoVO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignupNicknameFragment extends Fragment {
    View view;
    CustomProgressDialog dialog;

    UserInfoService userInfoService;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userInfoService = new UserInfoService(requireActivity());
        dialog = new CustomProgressDialog(requireActivity());

        initButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup_nickname, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    private void initButton(){
        Button signupNicknameNextButton = view.findViewById(R.id.signupNicknameNextButton);
        signupNicknameNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = readNickname();
                if(isValidNickname(nickname)) {         //닉네임 유효성 검사
                    dialog.showProgressDialog();

                    new Thread(() -> {
                        boolean nicknameCheckResult = userInfoService.nicknameCheck(nickname);
                        dialog.cancelProgressDialog();
                        if (!nicknameCheckResult) {                          //닉네임이 중복X
                            UserInfoVO.getInstance().setNickname(nickname);

                            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                            SignupAddFragment signupAddFragment = new SignupAddFragment();

                            transaction.add(R.id.signupAddFragment, signupAddFragment);                              //추가 프래그먼트로 전환
                            transaction.commit();
                        } else {
                            Toast.makeText(requireActivity(), "Nickname already exists.", Toast.LENGTH_SHORT).show();
                        }
                    }).start();
                }else{  //닉네임 유효성 통과 X
                    Toast.makeText(requireActivity(), "Please rewrite your nickname.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReSignupDialog reSignupDialog = new ReSignupDialog();
                reSignupDialog.show(requireActivity().getSupportFragmentManager(), "dialog_event");
            }
        });
    }

    private String readNickname(){
        EditText nicknameEditText = view.findViewById(R.id.nicknameEditText);
        return nicknameEditText.getText().toString();
    }

    public boolean isValidNickname(String nickname) {
        // Regular expression pattern to match English, Korean, and numbers, up to 11 characters
        String pattern = "^[a-zA-Z0-9가-힣]{1,11}$";

        // Create a Pattern object
        Pattern compiledPattern = Pattern.compile(pattern);

        // Create a Matcher object for the given nickname
        Matcher matcher = compiledPattern.matcher(nickname);

        // Check if the nickname matches the pattern
        return matcher.matches();
    }
}