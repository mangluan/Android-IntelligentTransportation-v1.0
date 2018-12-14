package com.icuter.shiti1.MainFragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.icuter.shiti1.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 车辆违章
 */
public class CarPeccancy extends Fragment {
    View view;
    TextView btnSP;
    TextView btnTP;
    RecyclerView mRecycler;

    LinearLayout wzImageLayout;
    FrameLayout wzVideoLayout;
    TextView wzQxVideo;
    ImageView wzImage;
    VideoView wzVideo;

    List<spData> mSpData;
    int[] tpData = new int[]{ R.drawable.image, R.drawable.image, R.drawable.image, R.drawable.image };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_car_peccancy,container,false);
        initView();
        return view;
    }

    private void initView() {
        initData();
        wzImage = view.findViewById(R.id.wz_image_car_main);
        wzImageLayout = view.findViewById(R.id.wz_image_layout_car_main);
        wzVideo = view.findViewById(R.id.wz_video_car_main);
        wzQxVideo = view.findViewById(R.id.wz_video_tv_car_main);
        wzVideoLayout = view.findViewById(R.id.wz_video_layout_car_main);

        btnSP = view.findViewById(R.id.dvd_car_main);
        btnTP = view.findViewById(R.id.image_car_main);
        mRecycler = view.findViewById(R.id.rec_list_car_main);
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(),4));
        mRecycler.setAdapter(new spAdapter());


        wzImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wzImageLayout.setVisibility(View.GONE);
            }
        });
        wzQxVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wzVideoLayout.setVisibility(View.GONE);
            }
        });

        btnSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSP.setBackgroundResource(R.drawable.heikuang);
                btnTP.setBackgroundResource(R.drawable.hei_hui);
                mRecycler.setAdapter(new spAdapter());
            }
        });
        btnTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnTP.setBackgroundResource(R.drawable.heikuang);
                btnSP.setBackgroundResource(R.drawable.hei_hui);
                mRecycler.setAdapter(new tpAdapter());
            }
        });
    }

    private void initData() {
        mSpData = new ArrayList<>();
        mSpData.add(new spData(R.drawable.skills,"视频1"));
        mSpData.add(new spData(R.drawable.skills,"视频2"));
        mSpData.add(new spData(R.drawable.skills,"视频3"));
        mSpData.add(new spData(R.drawable.skills,"视频4"));
        mSpData.add(new spData(R.drawable.skills,"视频5"));
    }
    //视频适配器
    class spAdapter extends RecyclerView.Adapter{
        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView mImageView;
            TextView name;
            public ViewHolder(View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.image_car_view1);
                name = itemView.findViewById(R.id.name_car_view1);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.main_car_peccancy_view1,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.mImageView.setBackgroundResource(mSpData.get(position).getImage());
            viewHolder.name.setText(mSpData.get(position).getName());

            viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    wzVideoLayout.setVisibility(View.VISIBLE);
                    MediaController mediaController=new MediaController(getContext());
                    wzVideo.setMediaController(mediaController);
                    wzVideo.setVideoURI(Uri.parse("android.resource://com.icuter.shiti1/"+ R.raw.pm));
                    wzVideo.start();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mSpData.size();
        }
    }
    //图片适配器
    class tpAdapter extends RecyclerView.Adapter{
        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView mImageView;
            public ViewHolder(View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.name_car_view2);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.main_car_peccancy_view2,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.mImageView.setBackgroundResource(tpData[position]);

            viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    wzImage.setBackgroundResource(tpData[position]);
                    wzImageLayout.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        public int getItemCount() {
            return tpData.length;
        }
    }

    class spData{
      private int image;
      private String name;

        public spData(int image, String name) {
            this.image = image;
            this.name = name;
        }

        public int getImage() {
            return image;
        }

        public String getName() {
            return name;
        }
    }
}
