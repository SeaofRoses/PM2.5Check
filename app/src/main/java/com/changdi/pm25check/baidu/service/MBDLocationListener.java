package com.changdi.pm25check.baidu.service;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.changdi.pm25check.view.PMView;

/**
 * Created by root on 17-1-7.
 */

public class MBDLocationListener implements BDLocationListener {
    private PMView pmView;
    public MBDLocationListener(PMView pmView){
        this.pmView=pmView;
    }
    @Override
    public void onReceiveLocation(BDLocation location) {
        // TODO Auto-generated method stub
        if (null != location && location.getLocType() != BDLocation.TypeServerError) {
            pmView.local(location);
        }
    }
}
