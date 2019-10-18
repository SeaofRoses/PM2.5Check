package com.changdi.pm25check.presenter;

import android.content.Context;
import android.util.Log;

import com.changdi.pm25check.model.PmData;
import com.changdi.pm25check.model.User;
import com.changdi.pm25check.service.imp.PMServiceImp;
import com.changdi.pm25check.service.imp.UpdataImp;
import com.changdi.pm25check.view.PMView;

import rx.Subscriber;

/**
 * Created by root on 16-12-27.
 */

public class PMPresenter {
    private PMView pmView;
    
    public PMPresenter(PMView pmView){
        this.pmView=pmView;
    }
    public void getData(){
        Subscriber subscriber=new Subscriber<PmData>() {

            @Override
            public void onCompleted() {
                pmView.openBle();
            }

            @Override
            public void onError(Throwable e) {
                System.out.print("e:"+e);
                //pmView.errorMessage(e);
            }

            @Override
            public void onNext(PmData pmData) {
                pmView.showData(pmData);
            }
        };

        PMServiceImp.getInstance().getData(subscriber);
    }

    public void upData(PmData pmData,String userid,String gps){
        Subscriber subscriber=new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("e",e+"");
            }

            @Override
            public void onNext(Object o) {
                Log.e("e",o+"");
            }
        };
        UpdataImp.getInstance().updata(subscriber,pmData,gps,userid);
    }

    public void getUserid(User user){
        Subscriber subscriber=new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String o) {

            }
        };
        UpdataImp.getInstance().getUserid(user,subscriber);
    }
}
