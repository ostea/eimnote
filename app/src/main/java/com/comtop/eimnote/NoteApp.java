package com.comtop.eimnote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Author chaos
 * Description:
 * DATE: 2018/4/27
 * Email: oscc92@gmail.com
 */

public class NoteApp extends Application implements Application.ActivityLifecycleCallbacks{

    public static int sActivityCount = 0;
    private int foregroundCount = 0;

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .disableHtmlEscaping()
            .create();

    public static Context getContext() {
        return context;
    }

    private static final String BASE_URL="http://eim2.szcomtop.com:6888/notes/";

    public static String getBaseUrl() {
        return BASE_URL;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
