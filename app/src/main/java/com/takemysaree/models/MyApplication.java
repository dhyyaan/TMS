package com.takemysaree.models;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.io.File;



/**
 * Created by think360user on 27/2/17.
 */

public class MyApplication extends Application {
    public static final String TAG = ImgController.class.getSimpleName();
    RequestQueue queue;


    private static MyApplication mInstance;



    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        File cacheLocation;

        // If we have external storage use it for the disk cache. Otherwise we use
        // the cache dir
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            cacheLocation = new File(
                    Environment.getExternalStorageDirectory() + "/Android-BitmapCache");
        } else {
            cacheLocation = new File(getFilesDir() + "/Android-BitmapCache");
        }
        cacheLocation.mkdirs();


        queue = Volley.newRequestQueue(getApplicationContext());
        mInstance = this;


    }


    public RequestQueue getRequestQueue() {
        return queue;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        request.setShouldCache(false);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequest(Object tag) {
        getRequestQueue().cancelAll(tag);
    }

    public static MyApplication getApplication(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

}
