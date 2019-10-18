package com.changdi.pm25check.service;

import java.util.ArrayList;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by root on 17-1-8.
 */

public interface UpdataService {
    @POST("/updata.action")
    Observable<Object> updata(@Query("gps") String gps,@Query("pm2d5") String pm2d5,@Query("pm10") String pm10,@Query("userid") String userid);
    @POST("user/user")
    Observable<String> getUserid(@Query("username") String username,@Query("password") String password);
}
