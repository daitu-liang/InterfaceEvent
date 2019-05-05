package com.lxl.interfaceevent;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class ProxyUnitTest {

    interface  HOST{
        @GET("/user/age")
        Call<ResponseBody> getUserInfo(@Query("id") String id,@Query("key") String key);
    }

    @Test
    public void name() {
    }

    @Test
    public void retrofitTest(){
        HOST host= (HOST) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{HOST.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("method.getName()======="+method.getName());

                Annotation[] an = method.getAnnotations();
                System.out.println("method.getName()======="+an.toString());

                GET anva = method.getAnnotation(GET.class);
                
                System.out.println(" anva.value()======="+ anva.value());
                Type[] type = method.getGenericParameterTypes();
                Annotation[][] pan = method.getParameterAnnotations();
                for (int i = 0; i < pan.length; i++) {
                    System.out.println(" ======"+type[i]+"==============");
                    Annotation[] parameterAnnotations = pan[i];
                    System.out.println(" ===parameterAnnotations==="+Arrays.toString(parameterAnnotations)+"==============");
                }
                for (Annotation[] annotations : pan) {
                    System.out.println(" 获取方法参数的注解======"+ Arrays.toString(annotations));
                }
                System.out.println(" 获取方法参数的值====="+Arrays.toString(args));



                return null;
            }
        });
        host.getUserInfo("id-kakkaxi","key-lai");
    }
}
