package com.lxl.interfaceevent;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.reflect.InvocationTargetException;


public class MainThreadHandler extends Handler {
    private Subscription subscription;
    private Object eventType;

    public MainThreadHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        try {
            subscription.method.invoke(subscription.subscrible,eventType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void post(Subscription subscription,Object eventType){
        this.subscription=subscription;
        this.eventType=eventType;
        sendMessage(Message.obtain());
    }
}
