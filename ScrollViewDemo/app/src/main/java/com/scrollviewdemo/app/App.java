package com.scrollviewdemo.app;

import android.app.Application;

import com.scrollviewdemo.util.Utils;

/**
 * Created by Administrator on 2017/12/2 0002.
 */

public class App extends Application {
    public static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Utils.init(this);
    }

    public static App getInstance() {
        return app;
    }
}
