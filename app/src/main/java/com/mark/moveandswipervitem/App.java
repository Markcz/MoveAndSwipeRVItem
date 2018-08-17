package com.mark.moveandswipervitem;

import android.app.Application;

import com.mark.moveandswipervitem.chche.DiskLruCacheManager;

/**
 * Created by chenzhen on 2018/8/17.
 */

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        DiskLruCacheManager.init(this);
    }
}
