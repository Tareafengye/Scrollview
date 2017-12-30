package com.scrollviewdemo.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.scrollviewdemo.R;
import com.scrollviewdemo.hoder.HolderImageLoader;
import com.scrollviewdemo.hoder.MulitiTypeSupport;
import com.scrollviewdemo.hoder.RecyclerCommonAdapter;
import com.scrollviewdemo.hoder.ViewHolders;
import com.scrollviewdemo.model.GankGay;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by weijinran ，Email 994425089@qq.com，on 2017/10/24.
 * Describe:
 * PS: Not easy to write code, please indicate.
 */

public class YouAdapter extends RecyclerCommonAdapter<GankGay.ResultsBean> {
    Context mContext;
    public YouAdapter(Context context, List<GankGay.ResultsBean> mDatas, MulitiTypeSupport<GankGay.ResultsBean> typeSupport) {
        super(context, mDatas, typeSupport);
        this.mContext = context;

    }

    @Override
    protected void convert(ViewHolders holder, final GankGay.ResultsBean item, final int position) {
        holder.setImagePath(R.id.iv_img, item.getUrl(), new HolderImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String path) {

                Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(imageView);
            }
        });
    }

}
