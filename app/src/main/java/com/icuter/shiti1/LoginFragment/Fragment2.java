package com.icuter.shiti1.LoginFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.icuter.shiti1.R;

public class Fragment2 extends Fragment {

    public interface TitileListener {
        void mFinish();
    }

    private TitileListener mListener;

    public void setListener(TitileListener listener) {
        mListener = listener;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment2, null);

        mSharedPreferences = mContext.getSharedPreferences("Admin", mContext.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        final EditText etName = view.findViewById(R.id.name_et_fragm2);
        final EditText etPwd = view.findViewById(R.id.pwd_et_fragm2);
        final EditText etPwd2 = view.findViewById(R.id.pwd2_et_fragm2);
        final EditText etPhone = view.findViewById(R.id.phone_et_fragm2);

        {//禁用输入框，改为网络验证登陆
            etName.setEnabled(false);
            etPwd.setEnabled(false);
            etPwd2.setEnabled(false);
            etPhone.setEnabled(false);

            etName.setBackgroundResource(R.drawable.hei_hui);
            etPwd.setBackgroundResource(R.drawable.hei_hui);
            etPwd2.setBackgroundResource(R.drawable.hei_hui);
            etPhone.setBackgroundResource(R.drawable.hei_hui);
        }

        Button qx = view.findViewById(R.id.qx_btn_frag2);
        Button signin = view.findViewById(R.id.sign_btn_frag2);

//        etPwd2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (!b) {
//                    if (etPwd.getText().toString().equals(""))
//                        return;
//                    else {
//                        if (!etPwd.getText().toString().equals(etPwd2.getText().toString())) {
//                            Toast.makeText(mContext, "两次密码输入不一致", Toast.LENGTH_LONG).show();
//                            etPwd2.setText("");
//                        }
//                    }
//                }
//            }
//        });
        //注册
//        signin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (etName.getText().toString().equals("")) {
//                    Toast.makeText(mContext, "用户名未输入", Toast.LENGTH_LONG).show();
//                } else if (etPwd.getText().toString().equals("") || etPwd2.getText().toString().equals("")) {
//                    Toast.makeText(mContext, "密码未输入", Toast.LENGTH_LONG).show();
//                } else {
//                    mEditor.putString(etName.getText().toString(), etPwd2.getText().toString());
//                    mEditor.putString(etName.getText().toString() + "Phone", etPhone.getText().toString());
//                    mEditor.apply();
//
//                    SharedPreferences.Editor edit = mContext.getSharedPreferences("Data",mContext.MODE_PRIVATE).edit();
//                    edit.putString("id",etName.getText().toString());
//                    edit.putString("pwd",etPwd.getText().toString());
//                    edit.apply();
//
//                    getFragmentManager().popBackStack();
//                }
//            }
//        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
                mListener.mFinish();
            }
        });
        qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
                mListener.mFinish();
            }
        });
        return view;
    }
}
