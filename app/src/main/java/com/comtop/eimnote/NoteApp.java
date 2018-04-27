package com.comtop.eimnote;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * Author chaos
 * Description:
 * DATE: 2018/4/27
 * Email: oscc92@gmail.com
 */

public class NoteApp extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
    }

}
