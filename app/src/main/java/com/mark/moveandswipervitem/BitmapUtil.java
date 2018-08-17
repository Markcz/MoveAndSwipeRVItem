package com.mark.moveandswipervitem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;

/**
 * Created by mark on 18-8-12.
 */

public class BitmapUtil {

    static final String TAG = "BitmapUtil";


    /***
     * 本地图片路径
     * @param imagePath
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap decodeFile(String imagePath, int targetWidth, int targetHeight) {
        if (imagePath == null) {
            throw new NullPointerException(TAG + "====传入的图片路径不能为null");
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeFile(imagePath, options);
        options.inSampleSize = calculateSampleSize(options, targetWidth, targetHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, options);
    }


    // TODO: 2018/8/17 待实现
    public static Bitmap decodeStream(InputStream is, int targetWidth, int targetHeight){
        if (is == null){

        }

        return null;
    }


    private static int calculateSampleSize(BitmapFactory.Options options,
                                           int targetWidth, int targetHeight) {
        int sampleSize = 1;
        int width = options.outWidth;
        int height = options.outHeight;
        if (width > targetWidth && height > targetHeight){
            sampleSize = Math.min(width / targetWidth,height / targetHeight);
        }else if (width > targetWidth && height < targetHeight){
            sampleSize = Math.min(width / targetWidth ,targetHeight / height);
        }else if (width < targetWidth && height > targetHeight){
            sampleSize = Math.min(targetWidth / width ,targetHeight / height);
        }
        if (sampleSize == 0){
            sampleSize = 1;
        }
        Log.e(TAG,sampleSize+"");
        return sampleSize;
    }


}
