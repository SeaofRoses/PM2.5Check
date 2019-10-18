package com.changdi.pm25check.service.imp;

import android.content.Context;

import com.changdi.pm25check.bluetooth.BluTool;
import com.changdi.pm25check.model.PmData;
import com.changdi.pm25check.service.PMService;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by root on 16-12-27.
 */

public class PMServiceImp implements PMService {
    BluTool bluTool;

    @Override
    public void getData(Subscriber<PmData> subscriber) {

        Observable observable = Observable.create(new Observable.OnSubscribe<PmData>() {
            @Override
            public void call(Subscriber<? super PmData> subscriber) {
                initBlutooth(subscriber);
            }
        });

        observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void initBlutooth(Subscriber<? super PmData> subscriber){//初始化蓝牙链接
        bluTool =new BluTool(subscriber);
        bluTool.seach();
        //bluTool.connect();
        bluTool.getData();
    }
    private static class SingletonHolder{
        private static final PMServiceImp INSTANCE=new PMServiceImp();
    }
    public static PMServiceImp getInstance(){return SingletonHolder.INSTANCE;}
}
