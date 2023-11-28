package com.example.mongtron_t.dialog;

import android.app.ProgressDialog;
import android.content.Context;


public class CustomProgressDialog {
    private ProgressDialog dialog;
    private final Context context;

    public CustomProgressDialog(Context context){
        this.context = context;
    }

    public void showProgressDialog(){                       //저장 버튼 후 서버로부터 응답을 받아올 때까지를 표시하기 위한 progress bar
        dialog = new ProgressDialog(
                context, android.R.style.Theme_Material_Dialog_Alert);
        dialog.setCanceledOnTouchOutside(false);                        //화면 터치 시 다이로그 꺼짐 방지
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);  //원 모양 설정
        dialog.setMessage("Connecting to server...");
        dialog.show();
    }

    public void cancelProgressDialog(){
        dialog.cancel();
    }
}
