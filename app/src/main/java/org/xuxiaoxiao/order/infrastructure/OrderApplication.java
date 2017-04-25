package org.xuxiaoxiao.order.infrastructure;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Created by WuQiang on 2017/4/25.
 */

public class OrderApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //提供以下两种方式进行初始化操作：

        //第一：默认初始化
        Bmob.initialize(this, "038e6f5a6a9e7785a3050f2f64850d55");
//        Bmob.initialize(this, "Your Application ID");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);
    }
}
