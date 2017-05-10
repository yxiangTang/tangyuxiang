package demo.tyx.com.reader.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import demo.tyx.com.mylibrary.Util.ShareData;
import demo.tyx.com.mylibrary.net.NetConfig;


public class BaseActivity extends Activity {

    protected NetConfig netConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            netConfig = NetConfig.getInstance();
        }


//        Bmob.initialize(this, "3efe775af47704bb32ce662bea85c738");
////第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
//        BmobConfig config = new BmobConfig.Builder(this)
//                //设置appkey
//                .setApplicationId("3efe775af47704bb32ce662bea85c738")
//                //请求超时时间（单位为秒）：默认15s
//                .setConnectTimeout(30)
//                //文件分片上传时每片的大小（单位字节），默认512*1024
//                .setUploadBlockSize(1024 * 1024)
//                //文件的过期时间(单位为秒)：默认1800s
//                .setFileExpiration(2500)
//                .build();
//        Bmob.initialize(config);

        ShareData.InitData(this);
//        skinStatusBarColorEnable();

    }
}
