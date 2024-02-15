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
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mongtron_t.R;
import com.example.mongtron_t.model.UserInfo;



public class SignupFragment extends Fragment {
    View view;
    String email;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initButton();
        setEmailTextView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        UserInfo.getInstance().initUser();
//    }

    private void initButton(){
        Button createAccountButton = view.findViewById(R.id.createAccountButton);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                SignupPasswordFragment signupPasswordFragment = new SignupPasswordFragment();

                UserInfo.getInstance().setEmail(email);

                transaction.add(R.id.signupPasswordFragment, signupPasswordFragment);                         //패스워트 프래그먼트로 전환
                transaction.commit();
            }
        });
        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
//                transaction.remove(SignupFragment.this);
                requireActivity().finish();                                             //signup activity 를 종료하고 로그인 화면으로 이동
            }
        });

        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish();
//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void setEmailTextView(){
        TextView tempEmailTextView = view.findViewById(R.id.tempEmailTextView);
        assert this.getArguments() != null;
        email = this.getArguments().getString("email");
        tempEmailTextView.setText(email);
    }
}