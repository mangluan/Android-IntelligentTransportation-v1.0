package com.icuter.shiti1.MainFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.icuter.shiti1.R;
import com.icuter.shiti1.Util.MySQLite;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 账单管理
 */
public class BillManage extends Fragment {

    List<ListData> mDataList;
    SimpleDateFormat mFormat;
    private ListView mListView;
    private TextView tvNull;
    private View view;
    private MySQLite mySQLite;
    private Spinner mSpinner;
    private MyAdapter myAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_bill_manage,null);
        mySQLite = new MySQLite(getContext(),"Data");
        initView();
        return view;
    }

    private void initView() {
        mSpinner = view.findViewById(R.id.spinner_bill_main);
        tvNull = view.findViewById(R.id.null_List_tv_bill_main);
        initData();
        mListView = view.findViewById(R.id.bill_list_main);
        myAdapter = new MyAdapter();
        mListView.setAdapter(myAdapter);
        mListView.setEnabled(false);
    }

    private void initData() {
        mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = mFormat.format(System.currentTimeMillis());

        mDataList = new ArrayList<>();
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        mDataList = mySQLite.BillQuery("select * from ChongZhiJiLu order by time desc");
                        myAdapter.notifyDataSetChanged();
                        if (mDataList.size()==0)
                            tvNull.setVisibility(View.VISIBLE);
                        else
                            tvNull.setVisibility(View.GONE);
                        break;
                    case 1:
                        mDataList = mySQLite.BillQuery("select * from ChongZhiJiLu order by time asc");
                        myAdapter.notifyDataSetChanged();
                        if (mDataList.size()==0)
                            tvNull.setVisibility(View.VISIBLE);
                        else
                            tvNull.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    static public class ListData{
        private int carID;
        private int money;
        private String name;
        private String time;

        public ListData(int carID, int money, String name, String time) {
            this.carID = carID;
            this.money = money;
            this.name = name;
            this.time = time;
        }

        public int getCarID() {
            return carID;
        }

        public int getMoney() {
            return money;
        }

        public String getName() {
            return name;
        }

        public String getTime() {
            return time;
        }
    }

    class MyAdapter extends BaseAdapter {

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

            view = LayoutInflater.from(getContext()).inflate(R.layout.main_bill_manage_list,null);
            TextView index = view.findViewById(R.id.index_bill_list_main);
            TextView carID = view.findViewById(R.id.carId_bill_list_main);
            TextView money = view.findViewById(R.id.money_bill_list_main);
            TextView name = view.findViewById(R.id.name_bill_list_main);
            TextView time = view.findViewById(R.id.time_bill_list_main);

            index.setText(i+1+"");
            carID.setText(data.getCarID()+"");
            money.setText(data.getMoney()+"");
            name.setText(data.getName());
            time.setText(data.getTime());
            return view;
        }
    }
}
