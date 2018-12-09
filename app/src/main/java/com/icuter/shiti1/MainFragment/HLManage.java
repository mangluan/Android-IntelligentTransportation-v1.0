package com.icuter.shiti1.MainFragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.icuter.shiti1.R;
import com.icuter.shiti1.Util.MySQLite;
import com.icuter.shiti1.Util.NetRequest;
import com.icuter.shiti1.Util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 红绿灯管理
 */
public class HLManage  extends Fragment {

    private ListView mList;
    private List<ListData> mDataList;
    private View view;
    private int index = 1;
    private Handler mHandler;
    private HLManageAdapter myAdapter;
    private MySQLite mySQLite;
    private Spinner mSpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_hl_manage,null);
        index = 1;
        mySQLite = new MySQLite(HLManage.this.getContext(),"Data");
        mySQLite.ExecSQL("delete from HongLvDENG");
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
                        String RedTime = jsonObject.getString("RedTime");
                        String GreenTime = jsonObject.getString("GreenTime");
                        String YellowTime = jsonObject.getString("YellowTime");
                        mySQLite.ExecSQL("insert into HongLvDENG (_id, Hong, Huang, Lv) values (?, ?, ?, ?)",
                                String.valueOf(index),RedTime,YellowTime,GreenTime);
                        if (index==5){
                            mDataList = mySQLite.HLQuery("select * from HongLvDENG order by _id asc");
                            myAdapter.notifyDataSetChanged();
                        }
                        if (index++<5){
                            initData();
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
        mDataList = new ArrayList<>();
        initData();
        mList = view.findViewById(R.id.hl_list_main);
        myAdapter = new HLManageAdapter();
        mList.setAdapter(myAdapter);
        mList.setEnabled(false);
        mSpinner = view.findViewById(R.id.spinner_hl_main);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        mDataList = mySQLite.HLQuery("select * from HongLvDENG order by _id asc");
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        mDataList = mySQLite.HLQuery("select * from HongLvDENG order by _id desc");
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        mDataList = mySQLite.HLQuery("select * from HongLvDENG order by Hong asc");
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 3:
                        mDataList = mySQLite.HLQuery("select * from HongLvDENG order by Hong desc");
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 4:
                        mDataList = mySQLite.HLQuery("select * from HongLvDENG order by Lv asc");
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 5:
                        mDataList = mySQLite.HLQuery("select * from HongLvDENG order by Lv desc");
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 6:
                        mDataList = mySQLite.HLQuery("select * from HongLvDENG order by Huang asc");
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 7:
                        mDataList = mySQLite.HLQuery("select * from HongLvDENG order by Huang desc");
                        myAdapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initData() {
        try {
                NetRequest netRequest = new NetRequest(getContext(), Tools.getIP("GetTrafficLightConfigAction.do") ,mHandler);
                Map<String,String> params = new HashMap<>();
                params.put("TrafficLightId",index+"");
                params.put("UserName",Tools.getUserName(getContext()));
                netRequest.setParams(params);
                new Thread(netRequest).start();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

     public static class ListData {
         private int index;
         private int hong;
         private int huang;
         private int lv;

         public ListData(int index, int hong, int huang, int lv) {
             this.hong = hong;
             this.huang = huang;
             this.lv = lv;
             this.index = index;
         }

         public int getIndex() {
             return index;
         }

         public int getHong() {
             return hong;
         }

         public int getHuang() {
             return huang;
         }

         public int getLv() {
             return lv;
         }
     }

    class HLManageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Object getItem(int i) {
            return mDataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ListData data = mDataList.get(i);

            view = LayoutInflater.from(getContext()).inflate(R.layout.main_hl_manage_list, null);
            TextView index = view.findViewById(R.id.index_hl_list_main);
            TextView hong = view.findViewById(R.id.hong_hl_list_main);
            TextView huang = view.findViewById(R.id.huang_hl_list_main);
            TextView lv = view.findViewById(R.id.lv_hl_list_main);

            index.setText(data.getIndex() + "");
            hong.setText(data.getHong()+"");
            huang.setText(data.getHuang()+"");
            lv.setText(data.getLv()+"");
            return view;
        }
    }
}