package com.icuter.shiti1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.icuter.shiti1.MainFragment.BillManage;
import com.icuter.shiti1.MainFragment.CarPeccancy;
import com.icuter.shiti1.MainFragment.HLManage;
import com.icuter.shiti1.MainFragment.MaxSetting;
import com.icuter.shiti1.MainFragment.MeAccount;
import com.icuter.shiti1.MainFragment.PM2_5Index;
import com.icuter.shiti1.MainFragment.RealTimeShow;
import com.icuter.shiti1.MainFragment.RealTimeShowViewPager;
import com.icuter.shiti1.MainFragment.TripManage;

public class MainActivity extends AppCompatActivity {

    private String[] listStr;
    private Fragment[] fragments;
    FragmentManager fm;

    DrawerLayout mDrawerLayout;
    ListView mListView;
    TextView mTitle;
    ImageView mImageBtn;

    //跳转实时显示广播
    private MyBroadcast myBroadcast;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        IntentFilter intentFilter = new IntentFilter("icuter.DataShow");
        myBroadcast = new MyBroadcast();
        registerReceiver(myBroadcast, intentFilter);

        initView();
    }

    private void initView() {
        initFragments();

        mDrawerLayout = findViewById(R.id.DrawerLayout_main);
        mListView = findViewById(R.id.listview_main);
        mTitle = findViewById(R.id.title_main);
        mImageBtn = findViewById(R.id.image_main);

        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ( i == listStr.length-1) {
                    //更改自动登陆标记
                    SharedPreferences mSharedPreferences = getSharedPreferences("Data", MainActivity.this.MODE_PRIVATE);
                    SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                    mEditor.putBoolean("aotoLogIn", false).apply();
                    //跳转登录界面
                    LoginActivity.startAction(MainActivity.this);
                    //销毁当前界面
                    MainActivity.this.finish();
                }else {
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragment_main,fragments[i]).commit();
                    mTitle.setText(listStr[i]);
                    mDrawerLayout.closeDrawers();
                }
            }
        });
        mListView.setAdapter(new MyListAdapter());
    }

    private void initFragments() {
        mSharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
        if (mSharedPreferences.getString("UserRole","").equals("admin")){
            listStr = new String[]{ "我的账户" ,"红绿灯管理" , "账单管理" , "车辆违章" , "环境指标" , "实时显示" ,"阈值设置" , "出行管理" , "退出登录"};

            fragments = new Fragment[listStr.length];
            fragments[0] = new MeAccount();
            fragments[1] = new HLManage();
            fragments[2] = new BillManage();
            fragments[3] = new CarPeccancy();
            fragments[4] = new PM2_5Index();
            fragments[5] = new RealTimeShow();
            fragments[6] = new MaxSetting();
            fragments[7] = new TripManage();
        }else{
            listStr = new String[]{ "我的账户" , "账单管理" , "车辆违章" , "环境指标" , "实时显示" ,"阈值设置" , "出行管理" , "退出登录"};

            fragments = new Fragment[listStr.length];
            fragments[0] = new MeAccount();
            fragments[1] = new BillManage();
            fragments[2] = new CarPeccancy();
            fragments[3] = new PM2_5Index();
            fragments[4] = new RealTimeShow();
            fragments[5] = new MaxSetting();
            fragments[6] = new TripManage();
        }
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment_main,fragments[0]).commit();
    }

    class MyBroadcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int index = intent.getIntExtra("index",0);
            Bundle bundle = new Bundle();
            bundle.putInt("index",index);
            FragmentTransaction ft = fm.beginTransaction();
            if (!mSharedPreferences.getString("UserRole","R01").equals("admin")) {
                fragments[4].setArguments(bundle);
                ft.replace(R.id.fragment_main, fragments[4]).commit();
                mTitle.setText(listStr[4]);
            }else {
                fragments[5].setArguments(bundle);
                ft.replace(R.id.fragment_main, fragments[5]).commit();
                mTitle.setText(listStr[5]);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcast);
    }

    public static void startAction(Context context){
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }
    class MyListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return listStr.length;
        }

        @Override
        public Object getItem(int i) {
            return listStr[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(MainActivity.this).inflate(R.layout.support_simple_spinner_dropdown_item, viewGroup, false);
            TextView textView = view.findViewById(android.R.id.text1);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(0,0,20,0);
            textView.setText(listStr[i]);
            return view;
        }
    }
}
