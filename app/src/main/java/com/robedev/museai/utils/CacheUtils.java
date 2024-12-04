package com.robedev.museai.utils;

import com.bumptech.glide.Glide;
import android.content.Context;

public class CacheUtils {

    public static void clearGlideCache(Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
            }
        }).start();
    }
}
