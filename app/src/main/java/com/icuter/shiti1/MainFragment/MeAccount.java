package com.icuter.shiti1.MainFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.icuter.shiti1.Dialog.DialogMainMeAccount;
import com.icuter.shiti1.R;
import com.icuter.shiti1.Util.MySQLite;
import com.icuter.shiti1.Util.NetRequest;
import com.icuter.shiti1.Util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 我的账户
 */
public class MeAccount extends Fragment {

    private View view;
    private @SuppressLint("HandlerLeak")
    Handler mHandler;
    private Spinner mSpinner;
    private Context mContext;
    private TextView mMoney;
    private Button chaxun;
    private Button chongzhi;
    private DialogMainMeAccount mDialog;
    private TextView mMoney2;
    private Handler mHandler2;
    private int money;
    private MySQLite mySQLite;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_me_account,null);
        mySQLite = new MySQLite(MeAccount.this.getContext(),"Data");
        initHandler();
        initView();
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
                        String money = jsonObject.getString("Balance");
                        mMoney.setText(money+"元");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (msg.what == -1) {
                    Toast.makeText(getContext(),"没有网络连接",Toast.LENGTH_LONG).show();
                }
            }
        };
        mHandler2 = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1){
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String RESULT = jsonObject.getString("RESULT");
                        if (RESULT.equals("S")){
                            Toast.makeText(MeAccount.this.getContext(),"充值成功",Toast.LENGTH_LONG).show();
                            initData();
                            //保存数据库
                            SharedPreferences sharedPreferences = getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
                            SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                            String time = format.format(new Date());
                            mySQLite.ExecSQL("insert into ChongZhiJiLu (carID ,money , UserName , time ) values (? ,? ,?, ?)",
                                    String.valueOf(mSpinner.getSelectedItemPosition()+1),String.valueOf(money),sharedPreferences.getString("id","admin"),time);
                        }else if (RESULT.equals("F  ")) {
                            Toast.makeText(MeAccount.this.getContext(),"充值失败",Toast.LENGTH_LONG).show();
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

    private void initView() {
        mSpinner = view.findViewById(R.id.spinner_meAccount_main);
        mMoney = view.findViewById(R.id.money_tv_meAccount_main);
        mMoney2 = view.findViewById(R.id.money2_tv_meAccount_main);
        initData();
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                initData();
                mMoney2.setText("0元");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        chaxun = view.findViewById(R.id.chaxun_btn_meAccount_main);
        chongzhi = view.findViewById(R.id.chongzhi_btn_meAccount_main);

        chaxun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
            }
        });
        chongzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog = new DialogMainMeAccount(MeAccount.this.getContext());
                mDialog.setBcListener(new DialogMainMeAccount.bcListener() {
                    @Override
                    public void bcBtn() {
                        money = mDialog.getMoney();
                        mMoney2.setText(money +"元");
                        try {
                            NetRequest netRequest = new NetRequest(mContext, Tools.IP+"SetCarAccountRecharge.do",mHandler2);
                            Map<String,String> params = new HashMap<>();
                            SharedPreferences sharedPreferences = getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
                            params.put("CarId",(mSpinner.getSelectedItemPosition()+1)+"");
                            params.put("Money", money +"");
                            params.put("UserName",sharedPreferences.getString("id","admin"));
                            netRequest.setParams(params);
                            new Thread(netRequest).start();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        mDialog.dismiss();
                    }
                });
                mDialog.show();
            }
        });
    }

    private void initData() {
        try {
            NetRequest netRequest = new NetRequest(mContext, Tools.IP+"GetCarAccountBalance.do",mHandler);
            Map<String,String> params = new HashMap<>();
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
            params.put("CarId",(mSpinner.getSelectedItemPosition()+1)+"");
            params.put("UserName",sharedPreferences.getString("id","admin"));
            netRequest.setParams(params);
            new Thread(netRequest).start();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
