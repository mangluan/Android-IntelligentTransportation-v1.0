package com.icuter.shiti1.MainFragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.icuter.shiti1.R;
import com.icuter.shiti1.Util.MySQLite;
import com.icuter.shiti1.Util.NetRequest;
import com.icuter.shiti1.Util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 实时显示
 */
public class RealTimeShowViewPager extends Fragment {

    private MySQLite mySQLite;
    private LineChart lineChart;
    private Handler mHandler;
    private NetRequest mNetRequest;
    private LineData data;
    private List<String> xData;
    private LineDataSet dataSet;

    int index = 0;
    String ip;
    TextView tvTeble;
    int[] maxs = new int[]{ 40 , 60 , 8000 , 8000 , 500 , 5 };
    int[] counts = new int[]{ 4 , 6 ,8 ,8 ,5 ,5 };
    String[] teble = new String[]{ "温度", "湿度", "光照", "CO2", "PM2.5", "道理状况"};
    String[] keys = new String[]{ "SenseName", "SenseName", "SenseName", "SenseName", "SenseName" ,"RoadId"};
    String[] values = new String[]{ "temperature", "humidity", "co2", "LightIntensity", "pm2.5" ,"1"};
    String[] respost = new String[]{ "temperature", "humidity", "co2", "LightIntensity", "pm2.5" ,"Status"};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_real_time_show_pager,container,false);
        tvTeble = view.findViewById(R.id.teble_show_pager);

        lineChart = view.findViewById(R.id.lineChart);
        mySQLite = new MySQLite(getContext(),"Data");

        Bundle bundle = getArguments();
        if (bundle!=null)
            index = bundle.getInt("index");
        tvTeble.setText(teble[index]);
        ip = index == 5 ?  Tools.getIP("GetRoadStatus.do") : Tools.getIP("GetSenseByName.do") ;

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
                    String result = msg.obj.toString();
                    try {
                        JSONObject object = new JSONObject(result);
                        int value = object.getInt(respost[index]);

                        long time = System.currentTimeMillis();
                        mySQLite.ExecSQL("insert into HuanJingZhiShu (name ,_index , mTime) values (? ,? ,? )" ,
                                teble[index],String.valueOf(value), String.valueOf(time));

                        addEntry(value ,time);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (msg.what == -1) {
                    Toast.makeText(getContext(),"没有网络连接",Toast.LENGTH_LONG).show();
                }
            }
        };
    }
    private void addEntry(float num , long time) {
        //注意每次添加数据需要设置数据源
        lineChart.setData(data);
        //添加X轴标签
        SimpleDateFormat format = new SimpleDateFormat("MM:ss");
        xData.add(format.format(time));
        Entry entry=new Entry(num,dataSet.getEntryCount());
        //将该数据点加到lineData数据中，第二个参数为数据集的下标，因为只有一个数据集所以为0
        dataSet.addEntry(entry);
        //通知数据和图表数据已经改变
        data.notifyDataChanged();
        lineChart.notifyDataSetChanged();
        //设置图表显示最大数据的最大个数
        lineChart.setVisibleXRangeMaximum(20);
        //平移位置
        lineChart.moveViewToX(dataSet.getEntryCount()-1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mNetRequest.setLoop(false);
    }

    private void initView() {
        lineChart.setDescription("");
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis yLeft = lineChart.getAxisLeft();
        YAxis yRight = lineChart.getAxisRight();
        yLeft.setAxisMaxValue(maxs[index]);
        yLeft.setAxisMinValue(0f);
        yLeft.setLabelCount(counts[index],false);

        yLeft.setDrawAxisLine(false);
        yRight.setDrawAxisLine(false);
        yRight.setEnabled(false);

        Legend legend=lineChart.getLegend();
        legend.setEnabled(false);

        initData();
    }

    private void initData() {
        long _time = System.currentTimeMillis()-60000;
        mySQLite.ExecSQL("delete from HuanJingZhiShu where mTime < ?" , String.valueOf(_time));
        List<MyData> myList = mySQLite.ShowQuery("select * from HuanJingZhiShu where  name = '" + teble[index] +"'");

        xData = new ArrayList<>();
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < myList.size(); i++) {
            MyData data = myList.get(i);
            entries.add(new Entry(data.getIndex(),i));
            xData.add(data.getTime());
        }
        dataSet = new LineDataSet(entries,teble[index]);
        dataSet.setColor(Color.parseColor("#8F8F8F"));
        dataSet.setCircleColor(Color.parseColor("#8F8F8F"));
        dataSet.setDrawCircleHole(false);
        data = new LineData(xData, dataSet);
        lineChart.setData(data);
        lineChart.invalidate();
        initNetRequest();
    }

    private void initNetRequest() {
        try {
            mNetRequest = new NetRequest(getContext(),ip,mHandler);
            Map<String,String> params = new HashMap<>();
            params.put( keys[index],values[index]);
            params.put("UserName",Tools.getUserName(getContext()));
            mNetRequest.setParams(params);
            mNetRequest.setLoop(true);
            mNetRequest.setLoopTime(3000);
            new Thread(mNetRequest).start();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static class MyData{
        String name;
        int index;
        String time;

        public MyData(String name, int index, long _time) {
            this.name = name;
            this.index = index;
            SimpleDateFormat format = new SimpleDateFormat("MM:ss");
            this.time = format.format(_time);
        }

        public String getName() {
            return name;
        }

        public int getIndex() {
            return index;
        }

        public String getTime() {
            return time;
        }
    }

}
