package com.jehm.wowrandomapp.App;

import android.app.Application;
import android.os.SystemClock;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Tiempo para el splashscreen
        SystemClock.sleep(3000);
    }
}