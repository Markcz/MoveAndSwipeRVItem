package com.mark.moveandswipervitem.image;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.widget.ImageView;
import com.jakewharton.disklrucache.DiskLruCache;
import com.mark.moveandswipervitem.chche.DiskLruCacheManager;
import com.mark.moveandswipervitem.chche.LruCacheManager;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mark on 18-8-12.
 */

public class ImageLoader {

    private static final String TAG = "ImageLoader";

    private static final float CORNER_RADIUS = 10f; //圆角半径


    private static Handler mHandler = new Handler(Looper.getMainLooper()); // 线程切换

    /***
     * 使用线程池解码 解决卡顿问题
     * 显示本地图片 圆角化处理
     * @param path
     * @param imageView
     * @param w
     * @param h
     */
    public static void displayRoundImage(final String path, final ImageView imageView, final int w, final int h) {
        if (path != null && imageView != null) {
            imageView.setImageDrawable(new ColorDrawable(Color.BLUE)); // 占位图
            Bitmap bitmap = LruCacheManager.getInstance().get(path);
            if (bitmap != null) {
                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(imageView.getResources(), bitmap);
                if (drawable != null) {
                    drawable.setCornerRadius(CORNER_RADIUS);
                    imageView.setImageDrawable(drawable);
                }
            } else {
                DecodeBitmapThreadPool.post(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap origin = BitmapUtil.decodeFile(path, w, h);
                        if (origin != null) {
                            LruCacheManager.getInstance().put(path, origin);
                            final RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory
                                    .create(imageView.getResources(), origin);
                            drawable.setCornerRadius(CORNER_RADIUS);
                            if (drawable != null) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (drawable != null) {
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


    public static void displayRoundImage(final String url, final ImageView imageView, final int w, final int h, boolean fromNet) {
        if (url != null && imageView != null) {
            imageView.setImageDrawable(new ColorDrawable(Color.GREEN)); // 占位图
            // 从内存缓存中读取
            final String urlKey = hashKeyForDisk(url);
            Log.e(TAG, urlKey);
            Bitmap bitmap = LruCacheManager.getInstance().get(urlKey);
            if (bitmap != null) {
                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(imageView.getResources(), bitmap);
                if (drawable != null) {
                    drawable.setCornerRadius(CORNER_RADIUS);
                    imageView.setImageDrawable(drawable);
                    Log.e(TAG, "diaplayImageWithNet -- LruCache");
                }
            } else {
                // 从磁盘缓存中读取
                final DiskLruCache.Snapshot snapshot = DiskLruCacheManager.getInstance().get(urlKey);
                if (snapshot != null) {
                    DecodeBitmapThreadPool.post(new Runnable() {
                        @Override
                        public void run() {
                            InputStream is = snapshot.getInputStream(0);
                            if (is != null) {
                                Bitmap temp = BitmapUtil.decodeStream(is, w, h);
                                if (temp != null) {
                                    // 将Bitmap放入内存缓存
                                    LruCacheManager.getInstance().put(urlKey, temp);
                                    final RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(imageView.getResources(), temp);
                                    drawable.setCornerRadius(CORNER_RADIUS);
                                    if (drawable != null) {
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView.setImageDrawable(drawable);
                                                Log.e(TAG, "diaplayImageWithNet -- DiskLruCache");
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    });
                } else {
                    // 从网络下载
                    Log.e(TAG, "diaplayImageWithNet -- Net");
                    DecodeBitmapThreadPool.post(new Runnable() {
                        @Override
                        public void run() {
                            InputStream is = null;
                            try {
                                DiskLruCache.Snapshot snapshot = DiskLruCacheManager.getInstance().get(urlKey);
                                if (snapshot == null) {
                                    DiskLruCache.Editor editor = DiskLruCacheManager.getInstance().put(urlKey);
                                    if (editor != null) {
                                        OutputStream outputStream = editor.newOutputStream(0);
                                        boolean b = downloadImage(url, outputStream);
                                        if (b) {
                                            editor.commit();
                                        } else {
                                            editor.abort();
                                        }
                                    }
                                    snapshot = DiskLruCacheManager.getInstance().get(urlKey);
                                }
                                if (snapshot != null) {
                                    is = snapshot.getInputStream(0);
                                    if (is != null) {
                                        final Bitmap bitmap = BitmapUtil.decodeStream(is, w, h);
                                        if (bitmap != null) {
                                            LruCacheManager.getInstance().put(urlKey, bitmap);
                                            mHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(imageView.getResources(), bitmap);
                                                    if (drawable != null) {
                                                        drawable.setCornerRadius(CORNER_RADIUS);
                                                        imageView.setImageDrawable(drawable);
                                                        Log.e(TAG, "diaplayImageWithNet -- DiskLruCache");
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "diaplayImageWithNet -- DiskLruCache" + e.getMessage());
                            } finally {
                                if (is != null) {
                                    try {
                                        is.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                }

            }
        }
    }


    /**
     * 将缓存记录同步到journal文件中。
     */
    public static void flush() {
        DiskLruCacheManager.getInstance().flushCache();
    }


    /***
     * 从网络下载图片
     * @param imagrUrl
     * @param outputStream
     * @return
     */
    private static boolean downloadImage(String imagrUrl, OutputStream outputStream) {

        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(imagrUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * url转为hash值
     *
     * @param key
     * @return
     */
    private static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

}
