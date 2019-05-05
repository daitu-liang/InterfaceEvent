package com.lxl.interfaceevent.refactorcode;

import java.util.Map;

public interface IHttpProcess {
    void post(String url, Map<String, Object> params, ICallBack callBack);
}
