package com.example.mongtron_t.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.mongtron_t.R;
import com.example.mongtron_t.dialog.ReSignupDialog;
import com.example.mongtron_t.fragment.SignupFragment;

public class SignupActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        ReSignupDialog reSignupDialog = new ReSignupDialog();
        reSignupDialog.show(getSupportFragmentManager(), "dialog_event");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //signup fragment load
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SignupFragment signupFragment = new SignupFragment();

        transaction.add(R.id.signupFragment, signupFragment);
        transaction.commit();

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");                                    //로그인 액티비티에서 받은 이메일 데이터 받기


        Bundle bundle = new Bundle();                                                           //액티비티에서 프래그먼트로 데이터를 넘겨줌
        bundle.putString("email", email);
        signupFragment.setArguments(bundle);
    }

}