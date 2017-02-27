package qf.com.android_sharesdkdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;

import cn.sharesdk.tencent.qq.QQ;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class MainActivity extends AppCompatActivity {

    private Platform plat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_btn:
                showShare();
                break;
            case R.id.qq_btn:
                authorize(new QQ(this));
                break;
            case R.id.weichat:
                authorize(new SinaWeibo(this));
                break;
            case R.id.sms:
                showSMS();
                break;
            case R.id.remove_btn:
                if (plat != null) {
                    plat.removeAccount(true);
                }
                break;
        }
    }

    //分享方法
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

    //第三方平台登录
    private void authorize(Platform plat) {
        this.plat = plat;
        String userId = plat.getDb().getUserId();
        if (!TextUtils.isEmpty(userId)) {
            Toast.makeText(this, "您已登录成功", Toast.LENGTH_SHORT).show();
        } else {
            plat.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    switch (i) {
                        case Platform.ACTION_AUTHORIZING:
                            Log.i("1620", "onComplete: " + Thread.currentThread().getName());
                            break;
                        case Platform.ACTION_USER_INFOR:
                            Set<Map.Entry<String, Object>> entries = hashMap.entrySet();
                            for (Map.Entry<String, Object> entry : entries) {
                                String key = entry.getKey();
                                Object value = entry.getValue();
                                Log.i("1620", "onComplete: " + key + "----" + value);
                            }
                            break;
                    }
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {

                }

                @Override
                public void onCancel(Platform platform, int i) {

                }
            });
//            plat.authorize();//要功能不要数据
            plat.showUser(null);//要数据不要功能
        }
    }

    //短信验证页面
    private void showSMS() {
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");
                    Log.i("1620", "afterEvent: ------"+country+phone);
                }
            }
        });
        registerPage.show(this);
    }
}
