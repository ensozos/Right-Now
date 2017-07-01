package com.auth.csd.rightnow;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

/**
 * Created by ilias on 1/7/2017.
 */

public class MyApplication extends Application {

    private static final Object TAG = "MyApplication";
    private static MyApplication _sInstance;

    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        _sInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return _sInstance;
    }

    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {

        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        //if tag is null all request with default are cancelled
        if (mRequestQueue != null) {
            if (tag != null) {
                mRequestQueue.cancelAll(tag);
            } else {
                mRequestQueue.cancelAll(TAG);
            }
        }
    }

}
