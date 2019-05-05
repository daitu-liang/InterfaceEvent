package com.lxl.interfaceevent;

import org.greenrobot.eventbus.EventBus;

public class EventBusUtils {
    public static void register(Object subscriber) {
        if(!EventBus.getDefault().isRegistered(subscriber)){
            EventBus.getDefault().register(subscriber);
        }

    }

    public static void unregister(Object subscriber) {
        if(EventBus.getDefault().isRegistered(subscriber)){
            EventBus.getDefault().unregister(subscriber);
        }

    }

    public static void sendEvent(ToastEvent event) {
        EventBus.getDefault().post(event);
    }

    public static void sendStickyEvent(ToastEvent event) {
        EventBus.getDefault().postSticky(event);
    }


}
