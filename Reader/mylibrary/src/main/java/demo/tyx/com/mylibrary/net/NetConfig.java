package demo.tyx.com.mylibrary.net;

import android.content.Context;
import android.util.Log;

/**
 * Created by admin on 2017/5/5.
 */

public class NetConfig {
    /**
     * 吾志网URl
     */
    private static final String API_URl="https://wuzhi.me";
    private String INDEX_API_URl="/last";
    private static NetConfig netConfig;
    private static final String TAG = "NetConfig";
    private NetConfig(){

    }

    public static NetConfig getInstance() {
        if (netConfig == null) {
            netConfig=new NetConfig();
        }
        return netConfig;
    }

    public  String getINDEX_API_URl() {
        return API_URl+INDEX_API_URl;
    }
    public String setIndexItemUrl(String temp){return API_URl+temp;}
}
