package com.icuter.shiti1.LoginFragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.icuter.shiti1.MainActivity;
import com.icuter.shiti1.R;
import com.icuter.shiti1.Util.NetRequest;
import com.icuter.shiti1.Util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 50834 on 2018/12/1.
 */

public class Fragment1 extends Fragment {

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private EditText etName;
    private EditText etPwd;
    private CheckBox cbPwd;
    private CheckBox cbLogIn;
    private Button btnLogIn;
    private Handler mHandler;


    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        etName.setText(mSharedPreferences.getString("id",""));
        etPwd.setText(mSharedPreferences.getString("pwd",""));
        if ( !mSharedPreferences.getString("pwd","").equals("")){
            cbPwd.setChecked(true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (cbPwd.isChecked()){
            mEditor.putString("id",etName.getText().toString());
            mEditor.putString("pwd",etPwd.getText().toString());
        }else {
            mEditor.putString("id","");
            mEditor.putString("pwd","");
        }mEditor.apply();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment1, null);

        initHandler();

        mSharedPreferences = mContext.getSharedPreferences("Data", mContext.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        etName = view.findViewById(R.id.name_et_fragm);
        etPwd = view.findViewById(R.id.pwd_et_fragm);
        cbPwd = view.findViewById(R.id.pwd_cbox_fragm);
        cbLogIn = view.findViewById(R.id.login_cbox_fragm);
        btnLogIn = view.findViewById(R.id.logIn_btn_fragm);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etName.getText().toString().equals("")){
                    Toast.makeText(mContext, "用户名未输入", Toast.LENGTH_LONG).show();
                }else if (etPwd.getText().toString().equals("")){
                    Toast.makeText(mContext, "密码未输入", Toast.LENGTH_LONG).show();
                }else {
                    try {
                        NetRequest netRequest = new NetRequest(getContext(), Tools.IP+"user_login.do",mHandler);
                        Map<String,String> params = new HashMap<>();
                        params.put("UserName",etName.getText().toString());
                        params.put("UserPwd",etPwd.getText().toString());
                        netRequest.setParams(params);
                        new Thread(netRequest).start();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return view;
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1){
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if (jsonObject.getString("RESULT").equals("S")) {
                            //保存账户类型:R01:普通用户，R02：管理员，R03：超级管理员
                            mEditor = mSharedPreferences.edit();
                            mEditor.putString("UserRole",jsonObject.getString("UserRole")).apply();
                            //本地验证成功，登陆
                            MainActivity.startAction(mContext);
                            //保存自动登陆标记
                            if (cbLogIn.isChecked())
                                mEditor.putBoolean("aotoLogIn", true).apply();
                            //发送销毁登陆界面广播
                            Intent intent = new Intent("LoginFinish");
                            mContext.sendBroadcast(intent);
                        } else {
                            Toast.makeText(mContext, "用户名或密码错误", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (msg.what == -1) {
                    Toast.makeText(getContext(),"没有网络连接",Toast.LENGTH_LONG).show();
                }
            }
        };
    }
}
