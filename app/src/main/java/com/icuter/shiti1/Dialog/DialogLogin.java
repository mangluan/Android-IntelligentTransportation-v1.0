package com.icuter.shiti1.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.icuter.shiti1.R;

/**
 * Created by 50834 on 2018/12/1.
 */

public class DialogLogin extends Dialog{

    Context mContext;
    EditText etIp;
    EditText etDuan;
    Button btBc;
    Button btQx;


    public DialogLogin(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login);

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("Data", mContext.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        etIp = findViewById(R.id.ip_et_dialog_login);
        etDuan = findViewById(R.id.duan_et_dialog_login);
        btBc = findViewById(R.id.baocun_btn_dialog_login);
        btQx = findViewById(R.id.qx_btn_dialog_login    );

        if (!sharedPreferences.getString("ip","").equals("")){
            etIp.setText(sharedPreferences.getString("ip",""));
            etDuan.setText(sharedPreferences.getString("duan",""));
        }

        etIp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (etIp.getText().toString().equals(""))
                        return;
                  if ( !etIp.getText().toString().matches(
                          "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])(\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)){3}$")){
                      Toast.makeText(mContext,"IP输入有误:（1-255.0-255.0-255.0-255）",Toast.LENGTH_LONG).show();
                      etIp.setText("");
                  }
                }
            }
        });
        etDuan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    if (etDuan.getText().toString().equals(""))
                        return;
                    int Duan = Integer.parseInt(etDuan.getText().toString());
                    if (Duan<0 || Duan > 65535){
                        Toast.makeText(mContext,"端口输入有误:（0-65535）",Toast.LENGTH_LONG).show();
                        etDuan.setText("");
                    }
                }
            }
        });

        btBc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etIp.getText().toString().equals("")) {
                    Toast.makeText(mContext, "IP未输入", Toast.LENGTH_LONG).show();
                }else if (etDuan.getText().toString().equals("")) {
                    Toast.makeText(mContext, "端口未输入", Toast.LENGTH_LONG).show();
                }else {
                    editor.putString("ip",etIp.getText().toString());
                    editor.putString("duan",etDuan.getText().toString());
                    editor.apply();
                    DialogLogin.this.dismiss();
                }
            }
        });
        btQx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogLogin.this.dismiss();
            }
        });
    }


}
