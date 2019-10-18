package com.changdi.pm25check.service.imp;

import com.changdi.pm25check.model.PmData;
import com.changdi.pm25check.model.User;
import com.changdi.pm25check.service.UpdataService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by root on 17-1-8.
 */

public class UpdataImp {
    public static final String BASE_URL = "http://a1996.oicp.net:9080/";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;

    private UpdataService updataService;

    public UpdataImp(){
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build();

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        updataService=retrofit.create(UpdataService.class);
    }

    public void updata(Subscriber subscriber, PmData pmData, String gps, String userid){
        updataService.updata(gps,pmData.getPm2d5()+"",pmData.getPm10()+"",userid)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getUserid(User user,Subscriber<String> subscriber){
        updataService.getUserid(user.getUsername(),user.getPassword())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private static class SingletonHolder{
        private static final UpdataImp INSTANCE=new UpdataImp();
    }
    public static UpdataImp getInstance(){
        return SingletonHolder.INSTANCE;
    }
}
