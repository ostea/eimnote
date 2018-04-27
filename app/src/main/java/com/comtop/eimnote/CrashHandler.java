package com.comtop.eimnote;

import android.util.Log;

import java.lang.Thread.UncaughtExceptionHandler;


class CrashHandler implements UncaughtExceptionHandler {

    private static final String TAG = "Global";
    private final UncaughtExceptionHandler defaultHandler;

    CrashHandler() {
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && defaultHandler != null) {
            defaultHandler.uncaughtException(thread, ex);
        }
    }

    private boolean handleException(final Throwable ex) {
        Log.e(TAG, "Catch a global exception", ex);
        return true;
    }

}
