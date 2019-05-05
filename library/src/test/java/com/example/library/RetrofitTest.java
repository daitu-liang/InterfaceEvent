package com.example.library;

import android.util.Log;

import com.example.library.http.Field;
import com.example.library.http.GET;
import com.example.library.http.POST;
import com.example.library.http.Query;

import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Call;
import okhttp3.RequestBody;

//http://co-api.51wnl.com/calendar/vacations?token=A2E0C3CDEA081D3BFC34F8FE23A15886&type=1&timestamp=1462377600&client=ceshi
public class RetrofitTest {

    interface ApiService {

        @GET("calendar/vacations")
        Call getUserInfo(@Query("token") String token, @Query("type") String type, @Query("timestamp") String timestamp, @Query("client") String client);

        @POST("calendar/vacations")
        Call getLogin(@Field("loginName") String loginName,@Field("password") String password);
    }

    @Test
    public void retrofit() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://co-api.51wnl.com/")
//                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService api = retrofit.create(ApiService.class);

        Call call = api.getUserInfo("A2E0C3CDEA081D3BFC34F8FE23A15886", "1", "1462377600", "ceshi");

        Response response=call.execute();
        if(response!=null&&response.body()!=null){
            System.out.println("返回结果get=" + response.body().string());
        }


        Call call2 = api.getLogin("liang","123456");
        Response response2=call2.execute();
        if(response2!=null&&response2.body()!=null){
            System.out.println("返回结果post=" + response2.body().string());
        }
    }
}