package demo.tyx.com.reader;


import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


/**
 * Created by admin on 2017/4/20.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        SkinMaterialManager.init(this);          // material design 控件换肤初始化[可选]
//        SkinCardViewManager.init(this);          // CardView 控件换肤初始化[可选]
//        SkinCompatManager.init(this).loadSkin(); // 基础控件换肤初始化并加载当前皮肤库(保存在SharedPreferences中)

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }
}
