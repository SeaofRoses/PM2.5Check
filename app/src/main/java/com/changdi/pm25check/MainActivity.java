package com.changdi.pm25check;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.baidu.location.BDLocation;
import com.changdi.pm25check.baidu.service.LocationApplication;
import com.changdi.pm25check.baidu.service.LocationService;
import com.changdi.pm25check.baidu.service.MBDLocationListener;
import com.changdi.pm25check.model.PmData;
import com.changdi.pm25check.presenter.PMPresenter;
import com.changdi.pm25check.tools.ScreenCap;
import com.changdi.pm25check.view.DashboardView3;
import com.changdi.pm25check.view.PMView;

import java.io.File;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements PMView, View.OnClickListener {
    private LocationService locationService;
    private MBDLocationListener mListener;
    private static final int BAIDU_READ_PHONE_STATE = 100;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    String local;

    @Override
    public void local(BDLocation location) {
        local = location.getLongitude() + "," + location.getLatitude();
        pmPresenter.upData(pmData, "1", local);
    }

    @BindView(R.id.dashboard_view_pm2d5)
    DashboardView3 pm2d5;
    @BindView(R.id.dashboard_view_pm10)
    DashboardView3 pm10;
    @BindView(R.id.sharewep)
    Button sharewep;
    @BindView(R.id.sharezone)
    Button sharezone;
    PMPresenter pmPresenter;
    PmData pmData;
    Boolean isStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        ButterKnife.bind(this);//绑定控件
        sharewep.setOnClickListener(this);
        sharezone.setOnClickListener(this);
        pm10.setmHeaderText("pm10数据");
        pmPresenter = new PMPresenter(this);

        pmPresenter.getData();
        getPermissions();
        pm2d5.setOnClickListener(this);
        pm10.setOnClickListener(this);


    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        // -----------location config ------------
        mListener = new MBDLocationListener(this);
        locationService = ((LocationApplication) getApplication()).locationService;

        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        locationService.setLocationOption(locationService.getOption());


    }

    @Override
    public void showData(PmData pmData) {//设置数据
        this.pmData = pmData;
        if (isStart == false) {
            locationService.start();
            isStart = true;
        }
        pm2d5.setCreditValue(pmData.getPm2d5());
        pm10.setCreditValue(pmData.getPm10());

    }

    @Override
    public void errorMessage(Throwable e) {

    }

    @Override
    public void openBle() {//提醒打开蓝牙
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivity(intent);
    }

    public void getPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义)
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, BAIDU_READ_PHONE_STATE);
            }
        } else {
//            locationService.unregisterListener(mListener); //注销掉监听
//            locationService.stop(); //停止定位服务s
        }
    }

    @Override
    public void onClick(View view) {
        pm2d5.postInvalidate();
        switch (view.getId()) {
            case R.id.sharewep:
                verifyStoragePermissions(this);
                File file = ScreenCap.bitMap2File(ScreenCap.activityShot(this));
                Uri imageUri = Uri.fromFile(file);
                Intent shareIntent = new Intent();
                //发送图片到朋友圈
                ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
                shareIntent.setComponent(comp);
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.setType("image/*");
                startActivity(Intent.createChooser(shareIntent, "分享图片"));
                break;
            case R.id.sharezone:
                verifyStoragePermissions(this);
                file = ScreenCap.bitMap2File(ScreenCap.activityShot(this));
                imageUri = Uri.fromFile(file);
                shareIntent = new Intent();
                //发送图片到qq
                comp = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
                shareIntent.setComponent(comp);
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.setType("image/*");
                startActivity(Intent.createChooser(shareIntent, "分享图片"));
                break;

        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
