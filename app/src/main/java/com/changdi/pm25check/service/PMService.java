package com.changdi.pm25check.service;


import com.changdi.pm25check.model.PmData;

import rx.Subscriber;

/**
 * Created by root on 16-12-27.
 */

public interface PMService {
    public void getData(Subscriber<PmData> subscriber);
}
