package com.kryptono.testtask;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;


public class MainApplication extends Application {
    private static Context context;

    public static boolean isForeground = true;

    @Override
    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();

        //getFCMToken();
        //CurrencyData.getCoinShowSharedPreferences(context);
    }

    public static Context getAppContext() {
        return MainApplication.context;
    }

}
