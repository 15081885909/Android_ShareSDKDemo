package qf.com.android_sharesdkdemo;

import android.app.Application;

import cn.sharesdk.framework.ShareSDK;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2017/2/16.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ShareSDK.initSDK(this);
//        SMSSDK.initSDK(this, "1b62ee808a7d6", "2a2a8ac576b15f49918dfd38dcd5f3b4");
    }
}
