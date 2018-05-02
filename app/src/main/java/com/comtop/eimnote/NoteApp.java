package com.comtop.eimnote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

/**
 * Author chaos
 * Description:
 * DATE: 2018/4/27
 * Email: oscc92@gmail.com
 */

public class NoteApp extends Application implements Application.ActivityLifecycleCallbacks{

    public static int sActivityCount = 0;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private int foregroundCount = 0;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        sActivityCount++;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (foregroundCount == 0) {
            // Global.setInForeground(true);
        }
        foregroundCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        foregroundCount--;
        if (foregroundCount == 0) {
            // Global.setInForeground(false);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        sActivityCount--;
    }
}
