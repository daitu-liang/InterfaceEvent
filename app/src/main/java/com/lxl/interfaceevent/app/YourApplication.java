package com.lxl.interfaceevent.app;

import android.app.Application;

import com.lxl.interfaceevent.refactorcode.HttpHelper;
import com.lxl.interfaceevent.refactorcode.OkhttpPro;

public class YourApplication extends Application {//implements HasActivityInjector

    @Override
    public void onCreate() {
        super.onCreate();
        HttpHelper.getInstance().setiHttpProcess(new OkhttpPro());

    }

//    @Override
//    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
//        return DaggerAppComponent.builder().application(this).build();
//    }

}
