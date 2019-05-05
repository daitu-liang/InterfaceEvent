package com.example.library;

import android.util.Log;

import com.example.library.http.Field;
import com.example.library.http.FormUrlEncoded;
import com.example.library.http.GET;
import com.example.library.http.Multipart;
import com.example.library.http.POST;
import com.example.library.http.Query;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

class ServiceMethod {

    static final String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";
    static final Pattern PARAM_URL_REGEX = Pattern.compile("\\{(" + PARAM + ")\\}");
    static final Pattern PARAM_NAME_REGEX = Pattern.compile(PARAM);

    private final Call.Factory callFactory;
    private final HttpUrl baseUrl;
    private final String httpMethod;
    private final String relativeUrl;
    private final boolean hasBody;
    private final ParameterHandler[] parameterHandlers;

    public ServiceMethod(Builder builder) {
        this.callFactory = builder.retrofit.callFactory();
        this.baseUrl = builder.retrofit.baseUrl();
        this.httpMethod = builder.httpMethod;
        this.relativeUrl = builder.relativeUrl;
        this.parameterHandlers = builder.parameterHandlers;
        this.hasBody = builder.hasBody;
    }
    /** Builds an HTTP request from method arguments.
     * 发起请求
     *  实例化RequestBuilder对象 对参数进行拼接 完成完整URL包含参数的拼接
     * */
    Call toCall( Object... args) {
        RequestBuilder requestBuilder = new RequestBuilder(httpMethod, baseUrl, relativeUrl ,hasBody);
        ParameterHandler[] handlers = parameterHandlers;
        int argumentCount = args != null ? args.length : 0;
        //Proxy方法的参数个数是否等于参数的数组（手动添加）的长度，做校验
        if (argumentCount != handlers.length) {
            throw new IllegalArgumentException("Argument count (" + argumentCount
                    + ") doesn't match expected count (" + handlers.length + ")");
        }
        //循环拼接每个参数名和参数值
        for (int i = 0; i < argumentCount; i++) {
            handlers[i].apply(requestBuilder, args[i].toString());
        }
        return callFactory.newCall(requestBuilder.build()) ;
    }
    public static class Builder {
        private final Retrofit retrofit;
        private final Method method;
        private final Annotation[] methodAnnotations;
        private final Type[] parameterTypes;
        private final Annotation[][] parameterAnnotationsArray;
        boolean hasBody;
        private String httpMethod;
        private String relativeUrl;
        private Set<String> relativeUrlParamNames;
        private ParameterHandler[] parameterHandlers;
        private boolean isFormEncoded;
        private boolean isMultipart;
        private boolean gotQuery;

        public Builder(Retrofit retrofit, Method method) {
            this.retrofit = retrofit;
            this.method = method;
            this.methodAnnotations = method.getAnnotations();
            this.parameterTypes = method.getGenericParameterTypes();
            this.parameterAnnotationsArray = method.getParameterAnnotations();
        }

        public ServiceMethod build() {
            for (Annotation annotation : methodAnnotations) {
                parseMethodAnnotation(annotation);
            }
            if (httpMethod == null) {
                Log.i(Retrofit.TAG, "HTTP method annotation is required (e.g., @GET, @POST, etc.).");
            }
            if (!hasBody) {
                if (isMultipart) {
                    Log.i(Retrofit.TAG,
                            "Multipart can only be specified on HTTP methods with request body (e.g., @POST).");
                }
                if (isFormEncoded) {
                    Log.i(Retrofit.TAG, "FormUrlEncoded can only be specified on HTTP methods with "
                            + "request body (e.g., @POST).");
                }
            }

            int parameterCount = parameterAnnotationsArray.length;
            parameterHandlers = new ParameterHandler[parameterCount];
            for (int p = 0; p < parameterCount; p++) {
                Type parameterType = parameterTypes[p];
                //获取每个参数的注解
                Annotation[] parameterAnnotations = parameterAnnotationsArray[p];
                if (parameterAnnotations == null) {
                    Log.i(Retrofit.TAG, p + "No Retrofit annotation found.");
                }
//                Log.i(Retrofit.TAG, "ServiceMethod-parameterAnnotations.toString()= " + parameterAnnotations.toString());
                parameterHandlers[p] = parseParameter(p, parameterAnnotations);
            }


            return new ServiceMethod(this);
        }

        private ParameterHandler parseParameter(int p, Annotation[] annotations) {
            ParameterHandler result = null;
            //遍历参数的注解
            for (Annotation annotation : annotations) {
                //注解可能是Query  Field等
                ParameterHandler annotationAction = parseParameterAnnotation(
                        p, annotations, annotation);
                if (annotationAction == null) {
                    continue;
                }
                if (result != null) {
                    Log.i(Retrofit.TAG, "Multiple Retrofit annotations found, only one allowed.=" + p);
                }
                result = annotationAction;
            }
            if (result == null) {
                Log.i(Retrofit.TAG, "No Retrofit annotation found.=" + p);
            }
            return result;
        }

        private ParameterHandler parseParameterAnnotation(
                int p, Annotation[] annotations, Annotation annotation) {
            if (annotation instanceof Query) {
                Query query = (Query) annotation;
                String name = query.value();//参数 注解  的值id
                //传过去的参数是注解的值，并非参数值， 参数值由代理Proxy方法传入
                //@Query("id"),  注解的值 id
                return new ParameterHandler.Query(name);
            } else if (annotation instanceof Field) {
                Field field = (Field) annotation;
                String name = field.value();
                return new ParameterHandler.Field(name);
            }
            return null; // Not a Retrofit annotation.
        }

        private void parseMethodAnnotation(Annotation annotation) {
            if (annotation instanceof GET) {
                parseHttpMethodAndPath("GET", ((GET) annotation).value(), false);
            } else if (annotation instanceof POST) {
                parseHttpMethodAndPath("POST", ((POST) annotation).value(), true);
            } else if (annotation instanceof Multipart) {
                if (isFormEncoded) {
                    Log.i(Retrofit.TAG, "Only one encoding annotation is allowed.");
                }
                isMultipart = true;
            } else if (annotation instanceof FormUrlEncoded) {
                if (isMultipart) {
                    Log.i(Retrofit.TAG, "Only one encoding annotation is allowed.");
                }
                isFormEncoded = true;
            }
        }

        //注解名称  值   是否有请求体
        private void parseHttpMethodAndPath(String httpMethod, String value, boolean hasBody) {
            if (this.httpMethod != null) {
                Log.i(Retrofit.TAG, "Only one HTTP method is allowed. Found: %s and %s." +
                        this.httpMethod + httpMethod);
            }
            this.httpMethod = httpMethod;
            this.hasBody = hasBody;
            if (value.isEmpty()) {
                return;
            }
            // Get the relative URL path and existing query string, if present.
            int question = value.indexOf('?');
            if (question != -1 && question < value.length() - 1) {
                // Ensure the query string does not have any named parameters.
                String queryParams = value.substring(question + 1);
                Matcher queryParamMatcher = PARAM_URL_REGEX.matcher(queryParams);
                if (queryParamMatcher.find()) {
                    Log.i(Retrofit.TAG, "URL query string \"%s\" must not have replace block. "
                            + "For dynamic query parameters use @Query." + queryParams);
                }
            }
            this.relativeUrl = value;
            this.relativeUrlParamNames = parsePathParameters(value);
        }
    }

    static Set<String> parsePathParameters(String path) {
        Matcher m = PARAM_URL_REGEX.matcher(path);
        Set<String> patterns = new LinkedHashSet<>();
        while (m.find()) {
            patterns.add(m.group(1));
        }
        return patterns;
    }
}
