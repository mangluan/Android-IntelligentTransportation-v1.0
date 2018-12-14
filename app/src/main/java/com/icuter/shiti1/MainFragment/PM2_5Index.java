package com.icuter.shiti1.MainFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
 * 环境指标
 */
public class PM2_5Index extends Fragment {

    private Handler mHandler;
    private Handler nHandler;
    private List<ListData> mListData;
    private RecyclerView mRecyclerView;
    private View view;
    private MyAdapter myAdapter;
    private NetRequest mNetRequest;
    private int[] max = new int[6];
    private MySQLite mySQLite;
    private String time;
    private long _time;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_pm25_index, null);
        mySQLite = new MySQLite(getContext(),"Data");
        initHandler();
        initMax();
        initView();
        return view;
    }

    private void initMax() {
        SharedPreferences mSharedPreferences = getContext().getSharedPreferences("Max", Context.MODE_PRIVATE);
        try {
            max[0] = Integer.parseInt(mSharedPreferences.getString("wen", ""));
            max[1] = Integer.parseInt(mSharedPreferences.getString("shi", ""));
            max[2] = Integer.parseInt(mSharedPreferences.getString("guang", ""));
            max[3] = Integer.parseInt(mSharedPreferences.getString("co2", ""));
            max[4] = Integer.parseInt(mSharedPreferences.getString("pm2.5", ""));
            max[5] = Integer.parseInt(mSharedPreferences.getString("dao", ""));
        }catch (NumberFormatException e){
        }
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1){
                    mListData.clear();
                    try {
                        NetRequest netRequest = new NetRequest(getContext(), Tools.getIP("GetRoadStatus.do") ,nHandler);
                        Map<String,String> params = new HashMap<>();
                        params.put("RoadId","1");
                        params.put("UserName",Tools.getUserName(getContext()));
                        netRequest.setParams(params);
                        new Thread(netRequest).start();
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

                        mListData.add(new ListData("温度", temperature));
                        mListData.add(new ListData("湿度", humidity));
                        mListData.add(new ListData("光照", LightIntensity));
                        mListData.add(new ListData("CO2", co2));
                        mListData.add(new ListData("PM2.5", pm25));

                        time = String.valueOf(System.currentTimeMillis());
                        _time = System.currentTimeMillis()-60000;
                        mySQLite.ExecSQL("delete from HuanJingZhiShu where mTime < ?" , String.valueOf(_time));
                        mySQLite.ExecSQL("insert into HuanJingZhiShu (name ,_index , mTime) values (? ,? ,? )" ,
                                "温度",String.valueOf(temperature), time);
                        mySQLite.ExecSQL("insert into HuanJingZhiShu (name ,_index , mTime) values (? ,? ,? )" ,
                                "湿度",String.valueOf(humidity), time);
                        mySQLite.ExecSQL("insert into HuanJingZhiShu (name ,_index , mTime) values (? ,? ,? )" ,
                                "光照",String.valueOf(LightIntensity), time);
                        mySQLite.ExecSQL("insert into HuanJingZhiShu (name ,_index , mTime) values (? ,? ,? )" ,
                                "CO2",String.valueOf(co2), time);
                        mySQLite.ExecSQL("insert into HuanJingZhiShu (name ,_index , mTime) values (? ,? ,? )" ,
                                "PM2.5",String.valueOf(pm25), time);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (msg.what == -1) {
                    Toast.makeText(getContext(),"没有网络连接",Toast.LENGTH_LONG).show();
                }
            }
        };
        nHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    JSONObject object = new JSONObject(msg.obj.toString());
                    int Status = object.getInt("Status");

                    mListData.add(new ListData("道路状态", Status ));
                    myAdapter.notifyDataSetChanged();

                    mySQLite.ExecSQL("insert into HuanJingZhiShu (name ,_index , mTime) values (? ,? ,? )" ,
                            "道路状态",String.valueOf(Status),time);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void initView() {
        initData();
        mRecyclerView = view.findViewById(R.id.rec_list_pm25_main);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        myAdapter = new MyAdapter();
        mRecyclerView.setAdapter(myAdapter);
    }

    private void initData() {
        mListData = new ArrayList<>();
        try {
            mNetRequest = new NetRequest(getContext(),Tools.getIP("GetAllSense.do") ,mHandler);
            Map<String,String> params = new HashMap<>();
            params.put("UserName",Tools.getUserName(getContext()));
            mNetRequest.setParams(params);
            mNetRequest.setLoop(true);
            mNetRequest.setLoopTime(3000);
            new Thread(mNetRequest).start();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNetRequest.setLoop(false);
    }

    static public class ListData {
        private String name;
        private int index;

        public ListData(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public int getIndex() {
            return index;
        }
    }

    class MyAdapter extends RecyclerView.Adapter {

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            TextView index;
            FrameLayout mLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name_pm_list_main);
                index = itemView.findViewById(R.id.index_pm_list_main);
                mLayout = itemView.findViewById(R.id.layout_pm_list_main);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.main_pm25_index_list, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.mLayout.setBackgroundColor(Color.GREEN);
            viewHolder.name.setText(mListData.get(position).getName());
            viewHolder.index.setText(mListData.get(position).getIndex() + "");

            if (mListData.get(position).getIndex()>max[position]){
                viewHolder.mLayout.setBackgroundColor(Color.RED);
            }

            viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent("icuter.DataShow");
                    intent.putExtra("index", position);
                    getContext().sendBroadcast(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mListData.size();
        }


    }

}
