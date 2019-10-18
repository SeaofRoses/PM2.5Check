package com.changdi.pm25check.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.changdi.pm25check.model.PmData;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import rx.Subscriber;

/**
 * Created by root on 16-12-27.
 */

public class BluTool {
    BluetoothAdapter adapter;
    BluetoothDevice device;
    Subscriber<? super PmData> subscriber;
    private BluetoothSocket socket;
    DataInputStream dp;
    float pm2d5;
    float pm10;
    private byte[] rbyte = new byte[10];
    private PmData pmData = new PmData();


    public BluTool(Subscriber<? super PmData> subscriber) {
        this.subscriber=subscriber;
    }

    public PmData getPmData() {
        return pmData;
    }

    public void setPmData(PmData pmData) {
        this.pmData = pmData;
    }

    public void seach() {
        //得到BluetoothAdapter对象
        adapter = BluetoothAdapter.getDefaultAdapter();
        //判断BluetoothAdapter对象是否为空，如果为空，则表明本机没有蓝牙设备
        if (adapter != null) {
            System.out.println("本机拥有蓝牙设备");
            //调用isEnabled()方法判断当前蓝牙设备是否可用
            if (!adapter.isEnabled()) {
                //如果蓝牙设备不可用的话,创建一个intent对象,该对象用于启动一个Activity,提示用户启动蓝牙适配器
                subscriber.onCompleted();

            }
            //得到所有已经配对的蓝牙适配器对象
            Set<BluetoothDevice> devices = adapter.getBondedDevices();
            if (devices.size() > 0) {
                //用迭代
                for (Iterator iterator = devices.iterator(); iterator.hasNext(); ) {
                    //得到BluetoothDevice对象,也就是说得到配对的蓝牙适配器
                    device = (BluetoothDevice) iterator.next();
                    //得到远程蓝牙设备的地址
                    if ("ch".equals(device.getName())) {
                        System.out.println("蓝牙名称：" + device.getName());
                        break;
                    }
                }
                connect();
            }
        } else {
            System.out.println("没有蓝牙设备");
        }
    }

    public void connect() {
        try {
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            socket.connect();
            dp = new DataInputStream(socket.getInputStream());

        } catch (Exception e) {
         //   subscriber.onError(e);
        }
    }

    public void getData() {
        while (true) {
            while (socket==null&&!socket.isConnected());
            try {
                dp.read(rbyte);
                if (rbyte[0] == (byte) 0xAA && rbyte[1] == (byte) 0xc0) {
                    float g8 = (rbyte[3] & 0xFF) * 256;
                    float d8 = (rbyte[2] & 0xFF);
                    pm2d5 = (g8 + d8) / 10;
                    pm10 = ((float) ((rbyte[5] & 0xFF) * 256) + (float) (rbyte[4] & 0xFF)) / 10;
                    pmData.setPm2d5(pm2d5);
                    pmData.setPm10(pm10);
                    subscriber.onNext(pmData);
                }
            } catch (IOException e) {
            //    subscriber.onError(e);
            }
        }
    }
}
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (socket != null && socket.isConnected()){
//                    try {
//                        dp.read(rbyte);
//                        if (rbyte[0]==(byte)0xAA){
//                            float g8=(rbyte[3]&0xFF)*256;
//                            float d8=(rbyte[2]&0xFF);
//                            pm2d5=(g8+d8)/10;
//                            pm10=((float)((rbyte[5]&0xFF)*256)+(float) (rbyte[4]&0xFF))/10;
//                            pmData.setPm2d5(pm2d5);
//                            pmData.setPm10(pm10);
//                            subscriber.onNext(pmData);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//}

