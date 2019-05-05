package com.example.library;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

class RequestBuilder {
    private final HttpUrl baseUrl;
    private final String method;
    private  String relativeUrl;

    //构建完整请求 包含URL  method  body
    private final Request.Builder requestBuilder;

    //构建完整URL
    private HttpUrl.Builder urlBuilder;
    private  FormBody.Builder formBuilder;


    public RequestBuilder(String httpMethod, HttpUrl baseUrl, String relativeUrl, boolean hasBody) {
        this.baseUrl=baseUrl;
        this.relativeUrl=relativeUrl;
        this.method=httpMethod;
        //初始化请求
        this.requestBuilder = new Request.Builder();
        if (hasBody) {
            // Will be set to 'body' in 'build'.
            //根据是否有请求体 实例化Form表单构建者
            formBuilder = new FormBody.Builder();
        }
    }

    public void addQueryParam(String name, String value) {
        if (relativeUrl != null) {
            // Do a one-time combination of the built relative URL and the base URL.
            urlBuilder = baseUrl.newBuilder(relativeUrl);
            if (urlBuilder == null) {
                throw new IllegalArgumentException(
                        "Malformed URL. Base: " + baseUrl + ", Relative: " + relativeUrl);
            }
            relativeUrl = null;
        }

        urlBuilder.addQueryParameter(name, value);
    }

    public void addFormField(String name, String value) {
        formBuilder.add(name, value);
    }
    
    public Request build() {
        HttpUrl url;
        HttpUrl.Builder urlBuilder = this.urlBuilder;
        if (urlBuilder != null) {
            url = urlBuilder.build();
        } else {
            // No query parameters triggered builder creation, just combine the relative URL and base URL.
            //noinspection ConstantConditions Non-null if urlBuilder is null.
            url = baseUrl.resolve(relativeUrl);
            if (url == null) {
                throw new IllegalArgumentException(
                        "Malformed URL. Base: " + baseUrl + ", Relative: " + relativeUrl);
            }
        }

        RequestBody body =null;
        if (body == null) {
            // Try to pull from one of the builders.
            if (formBuilder != null) {
                body = formBuilder.build();
            }
        }

        return requestBuilder
                .url(url)
                .method(method, body)
                .build();
    }
}
