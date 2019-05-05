package com.lxl.interfaceevent.refactorcode;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class HttpCallBack<Result> implements ICallBack {



    @Override
    public void onSuccess(String response) {
        Gson gson=new Gson();
        Class<?> clz=analysisClassInfo(this);
        Result result=(Result)gson.fromJson(response,clz);
        onSuccess(result);
    }

    protected  Class<?> analysisClassInfo(Object object){
        Type genType = object.getClass().getGenericSuperclass();//返回父类 - 含泛型
        Type[] actualTypeArguments = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) actualTypeArguments[0];
    }

    protected abstract void onSuccess(Result result);

    @Override
    public void onFail(Throwable e) {

    }
}
