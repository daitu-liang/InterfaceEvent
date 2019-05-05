package com.lxl.interfaceevent;

public abstract class EventMsg<P> extends IMsg {

    public EventMsg(String name) {
        super(name);
    }
    public  abstract  void function(P p);
}
