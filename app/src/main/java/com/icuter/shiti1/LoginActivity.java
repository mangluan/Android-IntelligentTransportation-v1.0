package com.icuter.shiti1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icuter.shiti1.Dialog.DialogLogin;
import com.icuter.shiti1.LoginFragment.Fragment1;
import com.icuter.shiti1.LoginFragment.Fragment2;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;

    MyBroadcast mMyBroadcast = new MyBroadcast();

    private TextView titleMain;
    private LinearLayout wlTitle;

    FragmentManager fm;
    private Fragment1 fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fragment1 = new Fragment1();
        fragment1.setContext(this);

        mSharedPreferences = getSharedPreferences("Data",MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        IntentFilter intentFilter = new IntentFilter("LoginFinish");
        registerReceiver(mMyBroadcast,intentFilter);

        if (mSharedPreferences.getBoolean("aotoLogIn",false)){
            //自动登录
            MainActivity.startAction(this);
            //发送销毁当前活动广播
            Intent intent = new Intent("LoginFinish");
            sendBroadcast(intent);
        }

        initAction();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMyBroadcast);
    }

    private void initView() {
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment_login, fragment1).commit();

        titleMain = findViewById(R.id.title_login);
        wlTitle = findViewById(R.id.title_layout_login);

        wlTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogLogin dialogLogin = new DialogLogin(LoginActivity.this);
                dialogLogin.show();
            }
        });
    }


    private void initAction() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) actionBar.hide();
        if ( mSharedPreferences.getInt("shan",0) == 0 ){
            ShanActivity.startAction(this);
            mEditor.putInt("shan",1).apply();
        }
    }

    public void SingIn(View view) {
        final FragmentTransaction ft = fm.beginTransaction();
        Fragment2 fragment2 = new Fragment2();
        fragment2.setContext(this);
        fragment2.setListener(new Fragment2.TitileListener() {
            @Override
            public void mFinish() {
                titleMain.setText("用户登录");
            }
        });
        ft.replace(R.id.fragment_login,fragment2).addToBackStack(null).commit();
        titleMain.setText("用户注册");
    }

    public static void startAction(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            titleMain.setText("用户登录");
        }
        return super.onKeyDown(keyCode, event);
    }

    class MyBroadcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LoginActivity.this.finish();
                }
            }, 1);
        }
    }

}
