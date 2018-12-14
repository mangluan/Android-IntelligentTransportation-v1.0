package com.icuter.shiti1.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.icuter.shiti1.R;

/**
 * Created by 50834 on 2018/12/1.
 */

public class DialogMainMeAccount extends Dialog {

    Context mContext;
    EditText etMoney;
    Button btBc;
    Button btQx;
    private TextView mEtMoney2;


    public DialogMainMeAccount(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_main_me_account);

        etMoney = findViewById(R.id.money_et_dialog_main);
        btBc = findViewById(R.id.baocun_btn_dialog_main);
        btQx = findViewById(R.id.qx_btn_dialog_main);

        btBc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etMoney.getText().toString().equals("")) {
                    Toast.makeText(mContext, "金额未输入", Toast.LENGTH_LONG).show();
                } else {
                    int Duan = Integer.parseInt(etMoney.getText().toString());
                    if (Duan < 1 || Duan > 999) {
                        Toast.makeText(mContext, "金额输入错误:（1-999）", Toast.LENGTH_LONG).show();
                        etMoney.setText("");
                    } else {
                        mBcListener.bcBtn();
                    }
                }
            }
        });
        btQx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogMainMeAccount.this.dismiss();
            }
        });
    }

    public int getMoney() {
        return Integer.parseInt(etMoney.getText().toString());
    }

    public interface bcListener {
        void bcBtn();
    }

    private bcListener mBcListener;

    public void setBcListener(bcListener bcListener) {
        mBcListener = bcListener;
    }
}
