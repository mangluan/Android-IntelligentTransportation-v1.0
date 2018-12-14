package com.icuter.shiti1.MainFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.icuter.shiti1.R;


public class RealTimeShow extends Fragment {

    ImageView[] mImages;
    ViewPager mViewPager;
    private View view;
    private FragmentManager fm;

    int index = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_real_time_show, container, false);
        fm = getChildFragmentManager();


        Bundle bundle = getArguments();
        if (bundle != null)
            index = bundle.getInt("index");

        initView();
        return view;
    }

    private void initView() {
        mViewPager = view.findViewById(R.id.viewpager_show_main);

        LinearLayout layout = view.findViewById(R.id.layout_show_main);
        mImages = new ImageView[layout.getChildCount()];
        for (int i = 0; i < mImages.length; i++) {
            mImages[i] = (ImageView) layout.getChildAt(i);
            mImages[i].setTag(i);
            mImages[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImagesEnnabled((Integer) view.getTag());
                    mViewPager.setCurrentItem((Integer) view.getTag());
                }
            });
        }
        mImages[0].setEnabled(false);


        mViewPager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                RealTimeShowViewPager show = new RealTimeShowViewPager();
                Bundle bundle = new Bundle();
                bundle.putInt("index", position);
                show.setArguments(bundle);
                return show;
            }

            @Override
            public int getCount() {
                return 6;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ImagesEnnabled(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewPager.setCurrentItem(index);
    }

    private void ImagesEnnabled(int position) {
        for (ImageView image : mImages) {
            image.setEnabled(true);
        }
        mImages[position].setEnabled(false);
    }
}
