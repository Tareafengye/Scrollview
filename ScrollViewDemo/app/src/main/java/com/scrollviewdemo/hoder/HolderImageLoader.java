package com.scrollviewdemo.hoder;

import android.widget.ImageView;

/**
 * Created by Administrator on 2017/12/6 0006.
 */

public interface HolderImageLoader {
    /**
     * 需要去复写这个方法来加载图片
     *
     * @param imageView
     * @param path
     */
    void loadImage(ImageView imageView, String path);
}
