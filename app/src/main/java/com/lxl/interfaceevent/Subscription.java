package com.lxl.interfaceevent;

import java.lang.reflect.Method;

public class Subscription {
    public Method method;
    public Object subscrible;
    public ThreadMode mode;

    public Subscription(Method method, Object subscrible, ThreadMode mode) {
        this.method = method;
        this.subscrible = subscrible;
        this.mode = mode;
    }
}
