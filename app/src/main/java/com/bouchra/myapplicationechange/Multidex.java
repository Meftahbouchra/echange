package com.bouchra.myapplicationechange;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;


public class Multidex extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
