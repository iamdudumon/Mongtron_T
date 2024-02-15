package com.example.mongtron_t.tool;

import android.content.Context;
import android.widget.Toast;

public class ResultMsg {
    private final boolean result;
    private final String msg;

    public ResultMsg(boolean result, String msg) {
        this.result = result;
        this.msg = msg;
    }

    public boolean isResult(){
        return this.result;
    }

    public void showToastMsg(Context context){
        Toast.makeText(context, this.msg, Toast.LENGTH_SHORT).show();
    }
}
