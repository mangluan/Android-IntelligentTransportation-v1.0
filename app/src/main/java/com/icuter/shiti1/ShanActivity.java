package com.icuter.shiti1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class ShanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shan);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //隐藏系统栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ShanActivity.this.finish();
            }
        }, 3000);
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, ShanActivity.class);
        context.startActivity(intent);
    }

}
