package com.example.library;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class Retrofit {
    public static final String TAG="Retrofit";
     final Map<Method, ServiceMethod> serviceMethodCache = new ConcurrentHashMap<>();
     Call.Factory callFactory;
       HttpUrl baseUrl;
    public Call.Factory callFactory() {
        return callFactory;
    }
    public void setCallFactory(Call.Factory callFactory) {
        this.callFactory = callFactory;
    }

    public HttpUrl baseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(HttpUrl baseUrl) {
        this.baseUrl = baseUrl;
    }



    Retrofit(Call.Factory callFactory, HttpUrl baseUrl) {
        this.callFactory = callFactory;
        this.baseUrl = baseUrl;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(final Class<T> service) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service },
                new InvocationHandler() {
                    @Override public Object invoke(Object proxy, Method method,  Object[] args)
                            throws Throwable {
                        ServiceMethod serviceMethod =
                                (ServiceMethod) loadServiceMethod(method);
                        OkHttpCall okHttpCall = new OkHttpCall(serviceMethod, args);
                        return okHttpCall;
                    }
                });
    }

    private ServiceMethod loadServiceMethod(Method method) {
        ServiceMethod result = serviceMethodCache.get(method);
        if (result != null) return result;

        synchronized (serviceMethodCache) {
            result = serviceMethodCache.get(method);
            if (result == null) {
                result = new ServiceMethod.Builder(this, method).build();
                serviceMethodCache.put(method, result);
            }
        }
        return result;
    }


    public static final class Builder {
        private okhttp3.Call.Factory callFactory;
        private HttpUrl baseUrl;

        public Builder setCallFactory(Call.Factory callFactory) {
            this.callFactory = callFactory;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            HttpUrl httpUrl = HttpUrl.parse(baseUrl);
            if (httpUrl == null) {
                throw new IllegalArgumentException("Illegal URL: " + baseUrl);
            }
            return baseUrl(httpUrl);
        }

        public Builder baseUrl(HttpUrl baseUrl) {
            List<String> pathSegments = baseUrl.pathSegments();
            if (!"".equals(pathSegments.get(pathSegments.size() - 1))) {
                throw new IllegalArgumentException("baseUrl must end in /: " + baseUrl);
            }
            this.baseUrl = baseUrl;
            return this;
        }

        public Retrofit build() {
            if (baseUrl == null) {
                throw new IllegalStateException("Base URL required.");
            }

            okhttp3.Call.Factory callFactory = this.callFactory;
            if (callFactory == null) {
                callFactory = new OkHttpClient();
            }
            return new Retrofit(callFactory, baseUrl);
        }

    }


}
