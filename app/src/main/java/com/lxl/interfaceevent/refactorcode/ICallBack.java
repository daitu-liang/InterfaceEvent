package com.lxl.interfaceevent.refactorcode;

public interface ICallBack {

    void onSuccess(String response);

    void onFail(Throwable e);
}
