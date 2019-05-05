package com.lxl.interfaceevent;

import android.os.Looper;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class EventManager {
    private  MainThreadHandler mainThreadHandler;
    HashMap<String, EventMsg> msgHashMap = new HashMap<>();
    HashMap<String, EventMsg2> msgHashMap2 = new HashMap<>();
    private  HashMap<Class<?>, ArrayList<Subscription>> subscriptionsByEventType=new HashMap<>();
    private static volatile EventManager intance;

    private EventManager() {
        mainThreadHandler=new MainThreadHandler(Looper.getMainLooper());
    }

    public static EventManager getInstance() {
        if (intance == null) {
            synchronized (EventManager.class) {
                if (intance == null) {
                    intance = new EventManager();
                }
            }
        }
        return intance;
    }


    public void register(Object subcrible){
        Class<?> clazz = subcrible.getClass();
        Method[] methods = clazz.getMethods();
        for(Method method:methods){
            String name=method.getName();
//            Log.i("==MainActivity","register---name="+name);
            if(name!=null&&name.startsWith("onEvent")){
                Class<?> param = method.getParameterTypes()[0];
                Log.i("==MainActivity","register----param-》》"+param+"  ---param.getName()=》》"+param.getName());
                ArrayList<Subscription> subscriptions = subscriptionsByEventType.get(param);
                if(subscriptions==null){
                    subscriptions=new ArrayList<>();
                    subscriptionsByEventType.put(param,subscriptions);
                }

                if(name.substring("onEvent".length()).length()==0){
                    subscriptions.add(new Subscription(method,subcrible,ThreadMode.PostThread));
                }else{
                    subscriptions.add(new Subscription(method,subcrible,ThreadMode.MainThread));

                }
                
            }
        }
    }


    public void unregister(Object subscriber){
        Class<?> clazz=subscriber.getClass();
        Method[] methods=clazz.getMethods();
        for(Method method:methods){
            String name=method.getName();
            if(name.startsWith("onEvent")){
                Class<?> param=method.getParameterTypes()[0];
                ArrayList<Subscription> subscriptions=subscriptionsByEventType.get(param);
                if(subscriptions!=null){
                    for(Subscription subscription:subscriptions){
                        if(subscription.subscrible==subscriber) {
                            subscriptions.remove(subscription);
                        }

                    }
                }
            }
        }
    }

    public void post(Object eventType){
        Class<?> clazz= eventType.getClass();
        ArrayList<Subscription> subscriptions= subscriptionsByEventType.get(clazz);
        if(subscriptions==null){
            Log.d("==MainActivity","EventBus: no subscriber has subscribed to this event");
        }else {

            for (Subscription subscription:subscriptions){
                switch (subscription.mode){
                    case MainThread:
                        mainThreadHandler.post(subscription,eventType);
                        break;
                    case PostThread:
                        try {
                            subscription.method.invoke(subscription.subscrible,eventType);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }
    
    public void addMsg(EventMsg eventMsg) {
        if (msgHashMap != null) {
            msgHashMap.put(eventMsg.name, eventMsg);
        }
    }

    public <P> void postMsg(String name, P p) {
        if (msgHashMap != null) {
            EventMsg info = msgHashMap.get(name);
            if (info != null) {
                info.function(p);
            }
        }
    }

    public void addMsg( EventMsg2 eventMsg) {
        if (msgHashMap2 != null) {
            msgHashMap2.put(eventMsg.name, eventMsg);
        }
    }

    public <T, P> T postMsg(String name, P p, Class<T> t) {
        if (msgHashMap2 != null) {
            EventMsg2 info = msgHashMap2.get(name);
            if (info != null) {
                if (t != null) {
                    return t.cast(info.function(p));
                }

            }
        }
        return null;

    }
}
