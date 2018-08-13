package com.mark.moveandswipervitem;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by mark on 18-8-12.
 */

public class ImageLoader {

    private static final String TAG = "ImageLoader";

    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public static void displayImage(String path, ImageView imageView,int w, int h,boolean round){
        if (round){
           displayRoundImageSync(path,imageView,w,h);
        }else {
            displayImageAsync(path,imageView,w,h);
        }
    }

    public static void displayImageAsync(final String path, final ImageView imageView, final int w, final int h){
        if (path != null && imageView != null){
            final Bitmap bitmap = LruCacheManager.getInstance().get(path);
            if (bitmap != null){
                imageView.setImageBitmap(bitmap);
            }else {
                DecodeBitmapThreadPool.post(new Runnable() {
                    @Override
                    public void run() {
                       final Bitmap origin = BitmapUtil.decodeFile(path,w,h);
                        if (origin != null){
                            LruCacheManager.getInstance().put(path,bitmap);
                        }
                       mHandler.post(new Runnable() {
                           @Override
                           public void run() {
                               if (origin != null){
                                   imageView.setImageBitmap(bitmap);
                               }
                           }
                       });
                    }
                });
            }
        }
    }

    public static void displayRoundImageAsync(final String path, final ImageView imageView, final int w, final int h){
        if (path != null && imageView != null){
            Bitmap bitmap = LruCacheManager.getInstance().get(path);
            if (bitmap != null){
                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(imageView.getResources(),bitmap);
                if (drawable != null){
                    drawable.setCornerRadius(30);
                    imageView.setImageDrawable(drawable);
                }
            }else {
                DecodeBitmapThreadPool.post(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap origin = BitmapUtil.decodeFile(path,w,h);
                        if (origin != null){
                            LruCacheManager.getInstance().put(path,origin);
                            final RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory
                                    .create(imageView.getResources(),origin);
                            drawable.setCornerRadius(30);
                            if (drawable != null){
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (drawable != null){
                                            imageView.setImageDrawable(drawable);
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }
    }

    public static void displayRoundImageSync(String path, ImageView imageView, int w, int h){
            if (path != null && imageView != null){
                Bitmap bitmap = LruCacheManager.getInstance().get(path);
                if (bitmap != null){
                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(imageView.getResources(),bitmap);
                    if (drawable != null){
                        drawable.setCornerRadius(30);
                        imageView.setImageDrawable(drawable);
                        Log.e(TAG,"displayRoundImageSync -- LruCache");
                    }
                }else {
                    final Bitmap origin = BitmapUtil.decodeFile(path,w,h);
                    if (origin != null){
                        LruCacheManager.getInstance().put(path,origin);
                        final RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory
                                .create(imageView.getResources(),origin);
                        drawable.setCornerRadius(30);
                        if (drawable != null){
                            imageView.setImageDrawable(drawable);
                            Log.e(TAG,"displayRoundImageSync -- Origin");
                        }
                    }
                }
            }
        }
}
