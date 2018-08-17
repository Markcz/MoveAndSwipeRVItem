package com.mark.moveandswipervitem;


import android.app.Application;
import android.os.Environment;
import com.jakewharton.disklrucache.DiskLruCache;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        int versionCode = mApp.getPackageManager().getPackageArchiveInfo(mApp.getPackageName(),0).versionCode;
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
    public void put(String key){
        if (key != null){
            try {
                String hashKey = hashKeyForDisk(key);
                DiskLruCache.Editor editor = mDiskLruCache.edit(hashKey);
                OutputStream os = editor.newOutputStream(0);
                save(key,os);
                editor.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void save(String key, OutputStream os) {
        // TODO: 2018/8/17 将文件存入本地缓存 待实现
    }

    /***
     *
     * @param key
     * @return
     */
    private InputStream get(String key){
        if (key != null){
            try {
                String hashKey = hashKeyForDisk(key);
                DiskLruCache.Snapshot snapshot = mDiskLruCache.get(hashKey);
                if (snapshot != null){
                    return snapshot.getInputStream(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



    public String hashKeyForDisk(String key) {
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

    private String bytesToHexString(byte[] bytes) {
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
