package com.kryptono.testtask;


import android.app.Activity;
import android.app.KeyguardManager;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.content.Context.KEYGUARD_SERVICE;

public class ForegroundBackgroundListener implements LifecycleObserver {

    Context context;
    ForegroundBackgroundListener(Context context)
    {
        this.context = context;

    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStart() {
        Log.v("ProcessLog", "APP IS ON FOREGROUND");
        if (!MainApplication.isForeground)
            lockScreen();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop() {
        MainApplication.isForeground = false;
        Log.v("ProcessLog", "APP IS IN BACKGROUND");
    }


    private void lockScreen()
    {
        Activity activity = (Activity) context;
        Intent intent = new Intent(activity, LockActivity.class);
        context.startActivity(intent);
        
//        Activity activity = (Activity) context;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            KeyguardManager km = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
//
//            if (km.isKeyguardSecure()) {
//                //Intent authIntent = km.createConfirmDeviceCredentialIntent(context.getString(R.string.app_name), "Unlock my app");
//                Intent authIntent = km.createConfirmDeviceCredentialIntent(null, null);
//                activity.startActivityForResult(authIntent, MainActivity.INTENT_AUTHENTICATE);
//            }
//        }
    }

}