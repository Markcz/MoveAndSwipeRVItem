package com.mark.moveandswipervitem;

import android.graphics.Bitmap;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenzhen on 2018/8/13.
 */

public class DecodeBitmapThreadPool {

    private static DecodeBitmapThreadPool mInstance;
    private ThreadPoolExecutor mExecutor;
    private static int MAX_POOL_SIZE = 4;
    private static final int KEEP_ALIVE = 10;
    private BlockingQueue<Runnable> mQueue = new LinkedBlockingQueue<>();


    private DecodeBitmapThreadPool() {
        int coreNum = Runtime.getRuntime().availableProcessors();
        MAX_POOL_SIZE = coreNum * 2;
        mExecutor = new ThreadPoolExecutor(coreNum
                , MAX_POOL_SIZE
                , KEEP_ALIVE
                , TimeUnit.SECONDS
                , mQueue
        );
    }

    public synchronized static void post(Runnable runnable) {
        if (mInstance == null) {
            mInstance = new DecodeBitmapThreadPool();
        }
        mInstance.mExecutor.execute(runnable);
    }


}
