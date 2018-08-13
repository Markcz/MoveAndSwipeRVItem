package com.mark.moveandswipervitem;


import com.jakewharton.disklrucache.DiskLruCache;

/**
 * Created by chenzhen on 2018/8/13.
 */

public class DiskLruCacheManager {
    private static DiskLruCacheManager mDiskLruCacheManager;
    private static DiskLruCache mDiskLruCache;



    public DiskLruCacheManager getInstance(){
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
       // mDiskLruCache = DiskLruCache.open()

    }

}
