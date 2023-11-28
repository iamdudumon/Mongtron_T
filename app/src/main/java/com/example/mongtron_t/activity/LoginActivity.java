package com.example.mongtron_t.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mongtron_t.R;
import com.example.mongtron_t.dialog.CustomProgressDialog;
import com.example.mongtron_t.user.AddedFriendDAO;
import com.example.mongtron_t.user.UserInfoDAO;
import com.example.mongtron_t.model.UserInfoVO;


public class LoginActivity extends AppCompatActivity {
    UserInfoDAO userInfoDAO;

    Button loginContinueButton;
    CustomProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userInfoDAO = new UserInfoDAO(getApplicationContext());
        loginContinueButton = findViewById(R.id.loginContinueButton);
        dialog = new CustomProgressDialog(LoginActivity.this);

        initBeforeCheckEmail();
        initBackButton();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initBeforeCheckEmail() {
        loginContinueButton.setOnClickListener(v -> {
            dialog.showProgressDialog();
            String email = readEmail();
            if (email.length() == 0) {
                Toast.makeText(getApplicationContext(), "Please rewrite email.", Toast.LENGTH_SHORT).show();
                dialog.cancelProgressDialog();
                return;
            }

            new Thread(() -> {
                try {
                    //이메일 체크
                    boolean emailCheekResult = userInfoDAO.emailCheck(email);
                    dialog.cancelProgressDialog();
                    if (emailCheekResult) {                          //이메일이 존재, 바로 비밀번호로 로그인 가능
                        Log.e("TAG", "해당 이메일 존재");
                        Handler handler = new Handler(Looper.getMainLooper());                  //메인 쓰레드가 아닌 다른 쓰레드에서 UI를 변경하기 위해서는 handler 사용
                        handler.postDelayed(this::initAfterCheckEmail, 0);

                    } else {                                           //해당 이메일이 존재하지 않으므로 회원가입 과정으로 이동
                        Log.e("TAG", "해당 이메일 존재 X -> 회원가입");

                        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);              //로그인 액티비티로 전환
                        intent.putExtra("email", email);                                                  // 액티비티 간 데이터 전달 (회원가입 화면으로 이메일 문자열 전송)
                        startActivity(intent);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }).start();
        });
    }

    private void initAfterCheckEmail() {
        TextView introductionTextView = findViewById(R.id.introductionTextView);
        EditText loginEmailEditText = findViewById(R.id.loginEmailEditText);
        loginEmailEditText.setFocusable(false);                                              //이메일 존재 시 이메일 수정 못 하게!
        introductionTextView.setText("로그인을 위해 비밀번호를 입력해주세요.");
        loginContinueButton.setText("LOGIN");

        //이메일이 존재하면 이메일 edit 아래에 gone 처리 한 view 들을 다시 등장
        TextView passwordLabel = findViewById(R.id.passwordLabel);
        EditText loginPasswordEditText = findViewById(R.id.loginPasswordEditText);
        Space space1 = findViewById(R.id.space1);
        Space space2 = findViewById(R.id.space2);

        passwordLabel.setVisibility(View.VISIBLE);
        loginPasswordEditText.setVisibility(View.VISIBLE);
        space1.setVisibility(View.VISIBLE);
        space2.setVisibility(View.VISIBLE);

        loginContinueButton.setOnClickListener(v -> {
            dialog.showProgressDialog();
            String[] emailPassword = readEmailPassword();
            UserInfoVO.getInstance().setEmail(emailPassword[0]);
            UserInfoVO.getInstance().setPassword(emailPassword[1]);
            userInfoDAO = new UserInfoDAO(getApplicationContext());

            new Thread(() -> {          //네트워크 작업은 무조건 메인 thread 와 다른 thread 로 구동해야함
                try {
                    boolean loginResult;
                    loginResult = userInfoDAO.login();
                    dialog.cancelProgressDialog();

                    if (loginResult) {                          //로그인 성공
                        AddedFriendDAO addedFriendDAO = new AddedFriendDAO(getApplicationContext());
                        addedFriendDAO.getServerFriendList();

                        Log.e("TAG", "로그인 성공");
                        finish();
                    } else
                        Log.e("TAG", "로그인 실패");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    private void initBackButton() {
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    private String readEmail() {
        EditText loginEmailEditText = findViewById(R.id.loginEmailEditText);
        return loginEmailEditText.getText().toString();
    }

    private String[] readEmailPassword() {
        EditText loginEmailEditText = findViewById(R.id.loginEmailEditText);
        EditText loginPasswordEditText = findViewById(R.id.loginPasswordEditText);
        return new String[]{loginEmailEditText.getText().toString(), loginPasswordEditText.getText().toString()};
    }
}