package com.example.zhong.paulapp.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.example.zhong.paulapp.R;

public class LoadingActivity extends Dialog {

    public LoadingActivity(Context context) {
        super(context, R.style.ImageloadingDialogStyle);
        //setOwnerActivity((Activity) context);// 设置dialog全屏显示
    }

    private LoadingActivity(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
    }

}
