package com.changdi.pm25check.view;

import com.baidu.location.BDLocation;
import com.changdi.pm25check.model.PmData;

/**
 * Created by root on 16-12-27.
 */

public interface PMView {
    public void showData(PmData pmData);
    public void errorMessage(Throwable e);
    public void openBle();
    public void local(BDLocation location);
}
