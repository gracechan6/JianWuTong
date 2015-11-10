package com.jinwang.jianwutong;

import android.app.Application;
import android.content.Context;

/**
 * Created by Chenss on 2015/10/30.
 */
public class BaseApplication extends Application {




    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
