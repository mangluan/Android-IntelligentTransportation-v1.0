package com.icuter.shiti1.MainFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.icuter.shiti1.R;

import java.text.SimpleDateFormat;


/**
 * 出行管理
 */
public class TripManage extends Fragment {

    ImageView nullImage;
    int[] image = new int[]{R.drawable.image_hong, R.drawable.image_huang, R.drawable.image_lv};
    int imageIndex = 0;
    private View view;
    private TextView tvTime;
    private TextView tvXianhao;
    private CalendarView timeView;

    private Switch mSwitch1;
    private Switch mSwitch2;
    private Switch mSwitch3;

    LinearLayout mLinearLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_trip_manage, container, false);
        initView();
        return view;
    }

    private void initView() {
        initData();
        nullImage = view.findViewById(R.id.null_image_trip_main);
        initImage();

        mSwitch1 = view.findViewById(R.id.switch_1_trip_main);
        mSwitch2 = view.findViewById(R.id.switch_2_trip_main);
        mSwitch3 = view.findViewById(R.id.switch_3_trip_main);

        timeView = view.findViewById(R.id.timeView_trip_main);

        tvTime = view.findViewById(R.id.time_tv_trip_main);
        tvXianhao = view.findViewById(R.id.xianhao_tv_trip_main);

        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String time = format.format(System.currentTimeMillis());
        tvTime.setText(time);
        xianhao(Integer.parseInt(time.substring(8, 10)));

        mLinearLayout = view.findViewById(R.id.linear_trip_main);

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLinearLayout.getVisibility() == View.GONE) {
                    mLinearLayout.setVisibility(View.VISIBLE);
                } else if (mLinearLayout.getVisibility() == View.VISIBLE) {
                    mLinearLayout.setVisibility(View.GONE);
                }
            }
        });

        timeView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                tvTime.setText(i + "年" + (i1 + 1) + "月" + i2 + "日");
                xianhao(i2);
                mLinearLayout.setVisibility(View.GONE);
            }
        });
    }

    private void xianhao(int hao) {
        if (hao % 2 == 0) {
            tvXianhao.setText("双号出行车辆：2");

            mSwitch1.setChecked(false);
            mSwitch2.setChecked(true);
            mSwitch3.setChecked(false);

            mSwitch1.setEnabled(false);
            mSwitch2.setEnabled(true);
            mSwitch3.setEnabled(false);

            mSwitch1.setBackgroundColor(Color.parseColor("#ffefefef"));
            mSwitch2.setBackgroundColor(Color.parseColor("#00efefef"));
            mSwitch3.setBackgroundColor(Color.parseColor("#ffefefef"));
        } else {
            tvXianhao.setText("单号出行车辆：1、3");

            mSwitch1.setChecked(true);
            mSwitch2.setChecked(false);
            mSwitch3.setChecked(true);

            mSwitch1.setEnabled(true);
            mSwitch2.setEnabled(false);
            mSwitch3.setEnabled(true);

            mSwitch1.setBackgroundColor(Color.parseColor("#00efefef"));
            mSwitch2.setBackgroundColor(Color.parseColor("#ffefefef"));
            mSwitch3.setBackgroundColor(Color.parseColor("#00efefef"));
        }
    }

    private void initImage() {
        final Handler mHandle = new Handler();
        mHandle.postDelayed(new Runnable() {
            @Override
            public void run() {
                nullImage.setBackgroundResource(image[imageIndex++ % 3]);
                mHandle.postDelayed(this, 3000);
            }
        }, 3000);
    }

    private void initData() {
    }
}
