package com.lxl.interfaceevent.net;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET
    Call<RequestBody> getUserInfo(@Query("id") String id);
}
