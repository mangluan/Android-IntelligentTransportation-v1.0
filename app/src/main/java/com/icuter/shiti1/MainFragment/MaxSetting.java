package com.icuter.shiti1.MainFragment;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.icuter.shiti1.R;
import com.icuter.shiti1.Util.NetRequest;
import com.icuter.shiti1.Util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;


/**
 * 阀值管理
 */
public class MaxSetting extends Fragment implements View.OnClickListener {

    private View view;
    private EditText etWendu;
    private EditText etShidu;
    private EditText etGuangzhao;
    private EditText etCo2;
    private EditText etPm;
    private EditText etDaolu;
    private Button btnBC;
    private SharedPreferences mSharedPreferences;
    private Switch mSwitch;
    private SharedPreferences.Editor editor;
    private NetRequest mNetRequest;
    private Handler mHandler;
    private Handler nHandler;
    private NetRequest nNetRequest;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_max_setting, container, false);
        initView();
        initData();
        return view;
    }

    private void initView() {
        mSharedPreferences = getContext().getSharedPreferences("Max", Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();

        etWendu = view.findViewById(R.id.wendu_et_max_main);
        etShidu = view.findViewById(R.id.shidu_et_max_main);
        etGuangzhao = view.findViewById(R.id.guanzhao_et_max_main);
        etCo2 = view.findViewById(R.id.co2_et_max_main);
        etPm = view.findViewById(R.id.pm_et_max_main);
        etDaolu = view.findViewById(R.id.daoluzhuantai_et_max_main);
        btnBC = view.findViewById(R.id.baocun_btn_max_main);
        mSwitch = view.findViewById(R.id.switch_max_main);

        btnBC.setOnClickListener(this);

        etWendu.setText(mSharedPreferences.getString("wen", ""));
        etShidu.setText(mSharedPreferences.getString("shi", ""));
        etGuangzhao.setText(mSharedPreferences.getString("guang", ""));
        etCo2.setText(mSharedPreferences.getString("co2", ""));
        etPm.setText(mSharedPreferences.getString("pm2.5", ""));
        etDaolu.setText(mSharedPreferences.getString("dao", ""));

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    etWendu.setEnabled(false);
                    etShidu.setEnabled(false);
                    etGuangzhao.setEnabled(false);
                    etCo2.setEnabled(false);
                    etPm.setEnabled(false);
                    etDaolu.setEnabled(false);

                    etWendu.setBackgroundResource(R.drawable.hei_hui);
                    etShidu.setBackgroundResource(R.drawable.hei_hui);
                    etGuangzhao.setBackgroundResource(R.drawable.hei_hui);
                    etCo2.setBackgroundResource(R.drawable.hei_hui);
                    etPm.setBackgroundResource(R.drawable.hei_hui);
                    etDaolu.setBackgroundResource(R.drawable.hei_hui);
                } else {
                    etWendu.setEnabled(true);
                    etShidu.setEnabled(true);
                    etGuangzhao.setEnabled(true);
                    etCo2.setEnabled(true);
                    etPm.setEnabled(true);
                    etDaolu.setEnabled(true);

                    etWendu.setBackgroundResource(R.drawable.heikuang);
                    etShidu.setBackgroundResource(R.drawable.heikuang);
                    etGuangzhao.setBackgroundResource(R.drawable.heikuang);
                    etCo2.setBackgroundResource(R.drawable.heikuang);
                    etPm.setBackgroundResource(R.drawable.heikuang);
                    etDaolu.setBackgroundResource(R.drawable.heikuang);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        editor.putString("wen", etWendu.getText().toString());
        editor.putString("shi", etShidu.getText().toString());
        editor.putString("guang", etGuangzhao.getText().toString());
        editor.putString("co2", etCo2.getText().toString());
        editor.putString("pm2.5", etPm.getText().toString());
        editor.putString("dao", etDaolu.getText().toString());
        editor.apply();
        Toast.makeText(getContext(), "保存成功", Toast.LENGTH_LONG).show();
        initMax();
    }

    private void initData() {
        initMax();
        initHandler();
        try {
            mNetRequest = new NetRequest(getContext(), Tools.getIP("GetAllSense.do"), mHandler);
            Map<String, String> params = new HashMap<>();
            params.put("UserName", Tools.getUserName(getContext()));
            mNetRequest.setParams(params);
            mNetRequest.setLoop(true);
            mNetRequest.setLoopTime(10000);
            new Thread(mNetRequest).start();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private int[] max = new int[6];

    private void initMax() {
        try {
            max[0] = Integer.parseInt(mSharedPreferences.getString("wen", ""));
            max[1] = Integer.parseInt(mSharedPreferences.getString("shi", ""));
            max[2] = Integer.parseInt(mSharedPreferences.getString("guang", ""));
            max[3] = Integer.parseInt(mSharedPreferences.getString("co2", ""));
            max[4] = Integer.parseInt(mSharedPreferences.getString("pm2.5", ""));
            max[5] = Integer.parseInt(mSharedPreferences.getString("dao", ""));
        } catch (NumberFormatException e) {
        }
    }

    private void Notify(String str, int index, int i) {
        NotificationManager manager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getContext());
        notification.setTicker("警告");
        notification.setSmallIcon(R.drawable.skills);
        notification.setContentText(str + "报警，阈值" + max[index] + "，当前值" + i + "。");
        notification.setAutoCancel(true);
        manager.notify(index, notification.build());
    }

    @Override
    public void onStop() {
        super.onStop();
        nNetRequest.setLoop(false);
        mNetRequest.setLoop(false);
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    try {
                        nNetRequest = new NetRequest(getContext(), Tools.getIP("GetRoadStatus.do"), nHandler);
                        Map<String, String> params = new HashMap<>();
                        params.put("RoadId", "1");
                        params.put("UserName", Tools.getUserName(getContext()));
                        nNetRequest.setParams(params);
                        new Thread(nNetRequest).start();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    String result = msg.obj.toString();
                    try {
                        JSONObject object = new JSONObject(result);
                        int temperature = object.getInt("temperature");
                        int humidity = object.getInt("humidity");
                        int LightIntensity = object.getInt("LightIntensity");
                        int co2 = object.getInt("co2");
                        int pm25 = object.getInt("pm2.5");

                        if (temperature > max[0])
                            Notify("温度", 0, temperature);
                        if (humidity > max[1])
                            Notify("湿度", 1, humidity);
                        if (LightIntensity > max[2])
                            Notify("光照强度", 2, LightIntensity);
                        if (co2 > max[3])
                            Notify("Co2", 3, co2);
                        if (pm25 > max[4])
                            Notify("pm2.5", 4, pm25);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msg.what == -1) {
                    Toast.makeText(getContext(), "没有网络连接", Toast.LENGTH_LONG).show();
                }
            }
        };
        nHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    JSONObject object = new JSONObject(msg.obj.toString());
                    int Status = object.getInt("Status");

                    if (Status > max[5])
                        Notify("道路状况", 5, Status);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
