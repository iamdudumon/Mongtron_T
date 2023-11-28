package com.example.mongtron_t.dialog;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class CustomAlterDialog {
    private final AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    public CustomAlterDialog(String title, String message, Context context) {
        this.builder = new AlertDialog.Builder(context);
        this.builder.setTitle(title).setMessage(message);

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {                 //기본 값으로 취소 문장과 다이얼로그 끄는 기능을 함 -> override로 수정 가능
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelDialog();
            }
        });
    }

    public AlertDialog.Builder getBuilder() {
        return builder;
    }

    public void showDialLog(){
        this.alertDialog = builder.create();
        alertDialog.show();
    }

    public void cancelDialog(){
        alertDialog.cancel();
    }
}
