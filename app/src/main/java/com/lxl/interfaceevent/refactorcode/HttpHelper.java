package com.lxl.interfaceevent.refactorcode;

import java.util.Map;

public class HttpHelper implements IHttpProcess{
    private static volatile HttpHelper httpHelper;
    private HttpHelper(){}
    public static HttpHelper getInstance(){
        if(httpHelper==null){
            synchronized (HttpHelper.class){
                if(httpHelper==null){
                    httpHelper=new HttpHelper();
                }
            }
        }
        return httpHelper;
    }

    private IHttpProcess iHttpProcess;

    public IHttpProcess getiHttpProcess() {
        return iHttpProcess;
    }

    public void setiHttpProcess(IHttpProcess iHttpProcess) {
        this.iHttpProcess = iHttpProcess;
    }

    @Override
    public void post(String url, Map<String, Object> params, ICallBack callBack) {
        iHttpProcess.post(url,params,callBack);
    }
}
