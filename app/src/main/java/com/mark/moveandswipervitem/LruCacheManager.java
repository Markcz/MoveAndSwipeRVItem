package com.mark.moveandswipervitem;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by mark on 18-8-12.
 */

public class LruCacheManager {

    private static LruCacheManager mManager;
    private static LruCache<String, Bitmap> mLruCache;

    public static LruCacheManager getInstance() {
        if (mManager == null) {
            synchronized (LruCacheManager.class) {
                if (mManager == null) {
                    mManager = new LruCacheManager();
                }
            }
        }
        return mManager;
    }

    private LruCacheManager() {
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        mLruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public void put(String key,Bitmap bitmap){
        if (key != null && bitmap != null){
            mLruCache.put(key,bitmap);
        }
    }

    public Bitmap get(String key){
        if (key != null){
            return mLruCache.get(key);
        }
        return null;
    }
}
