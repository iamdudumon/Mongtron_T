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
import com.example.mongtron_t.dialog.ReSignupDialog;
import com.example.mongtron_t.model.UserInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignupPasswordFragment extends Fragment {
    View view;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup_password, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    private void initButton(){
        Button signupPasswordNextButton = view.findViewById(R.id.signupPasswordNextButton);
        signupPasswordNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] password = readPassword();
                if (isValidPassword(password[0])) {
                    if (password[0].equals(password[1])) {
                        UserInfo.getInstance().setPassword(password[0]);
                        //두 패스워드가 같다면 프래그먼트 이동
                        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                        SignupNicknameFragment signupNicknameFragment = new SignupNicknameFragment();

                        transaction.add(R.id.signupNicknameFragment, signupNicknameFragment);                         //패스워트 프래그먼트로 전환
                        transaction.commit();
                    } else {
                        Toast.makeText(requireActivity(), "The two passwords do not match.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(requireActivity(), "Please rewrite the password.", Toast.LENGTH_SHORT).show();
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

    private String[] readPassword(){                                                                    //password fragment 에서 입력받은 두 패스워드를 리턴
        String[] password = new String[2];
        EditText passwordEditText = view.findViewById(R.id.passwordEditText);
        EditText confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText);

        password[0] = passwordEditText.getText().toString();
        password[1] = confirmPasswordEditText.getText().toString();

        return password;
    }

    private boolean isValidPassword(String password) {
        // 최소 8자, 최대 20자 상수 선언
        final int MIN = 8;
        final int MAX = 20;

        // 영어, 숫자, 특수문자 포함한 MIN to MAX 글자 정규식
        final String REGEX =
                "^((?=.*\\d)(?=.*[a-zA-Z])(?=.*[\\W]).{" + MIN + "," + MAX + "})$";
        // 3자리 연속 문자 정규식
        final String SAMEPT = "(\\w)\\1\\1";
        // 공백 문자 정규식
        final String BLANKPT = "(\\s)";

        // 정규식 검사객체
        Matcher matcher;

        // 공백 체크
        if (password == null || "".equals(password)) {
            return false;
        }

        // ASCII 문자 비교를 위한 UpperCase
        String tmpPw = password.toUpperCase();
        // 문자열 길이
        int strLen = tmpPw.length();

        // 글자 길이 체크
        if (strLen > 20 || strLen < 8) {
            return false;
        }

        // 공백 체크
        matcher = Pattern.compile(BLANKPT).matcher(tmpPw);
        if (matcher.find()) {
            return false;
        }

        // 비밀번호 정규식 체크
        matcher = Pattern.compile(REGEX).matcher(tmpPw);
        if (!matcher.find()) {
            return false;
        }

        // 동일한 문자 3개 이상 체크
        matcher = Pattern.compile(SAMEPT).matcher(tmpPw);
        if (matcher.find()) {
            return false;
        }

        // 연속된 문자 / 숫자 3개 이상 체크

        // ASCII Char를 담을 배열 선언
        int[] tmpArray = new int[strLen];

        // Make Array
        for (int i = 0; i < strLen; i++) {
            tmpArray[i] = tmpPw.charAt(i);
        }

        // Validation Array
        for (int i = 0; i < strLen - 2; i++) {
            // 첫 글자 A-Z / 0-9
            if ((tmpArray[i] > 47
                    && tmpArray[i + 2] < 58)
                    || (tmpArray[i] > 64
                    && tmpArray[i + 2] < 91)) {
                // 배열의 연속된 수 검사
                // 3번째 글자 - 2번째 글자 = 1, 3번째 글자 - 1번째 글자 = 2
                if (Math.abs(tmpArray[i + 2] - tmpArray[i + 1]) == 1
                        && Math.abs(tmpArray[i + 2] - tmpArray[i]) == 2) {
                    char c1 = (char) tmpArray[i];
                    char c2 = (char) tmpArray[i + 1];
                    char c3 = (char) tmpArray[i + 2];
                    return false;
                }
            }
        }
        // Validation Complete
        return true;
    }
}