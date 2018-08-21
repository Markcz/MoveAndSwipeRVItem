package com.mark.moveandswipervitem.chche;


import android.app.Application;
import android.os.Environment;
import com.jakewharton.disklrucache.DiskLruCache;
import com.mark.moveandswipervitem.BuildConfig;
import com.mark.moveandswipervitem.exception.NotInitializeException;

import java.io.File;
import java.io.IOException;

/**
 * Created by chenzhen on 2018/8/13.
 */

public class DiskLruCacheManager {
    private static DiskLruCacheManager mDiskLruCacheManager;
    private static DiskLruCache mDiskLruCache;
    private static Application mApp;
    private final static long MAX_SIZE = 1024 * 1024 * 100; // 100MB
    public static void init(Application application){
        mApp = application;
    }


    public static DiskLruCacheManager getInstance(){
        if (mApp == null){
            throw new NotInitializeException("DiskLruCacheManager: 未初始化");
        }
        if (mDiskLruCacheManager == null){
            synchronized (DiskLruCacheManager.class){
                if (mDiskLruCacheManager == null){
                  mDiskLruCacheManager = new DiskLruCacheManager();
                }
            }
        }
        return mDiskLruCacheManager;
    }

    private DiskLruCacheManager (){
        File cacheDir = getCacheDir(mApp);
        int versionCode = BuildConfig.VERSION_CODE;
        try {
            mDiskLruCache = DiskLruCache.open(cacheDir,versionCode,1,MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private File getCacheDir(Application application){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()){
            return application.getExternalCacheDir();
        }
        return application.getCacheDir();
    }


    /**
     * 存储文件
     * @param key
     */
    public DiskLruCache.Editor put(String key){
        if (key != null){
            try {
                DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                if (editor != null){
                    return editor;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /***
     *
     * @param key
     * @return
     */
    public DiskLruCache.Snapshot get(String key){
        if (key != null){
            try {
                DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                if (snapshot != null){
                    return snapshot;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将缓存记录同步到journal文件中。
     */
    public void flushCache() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
