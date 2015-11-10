package com.jinwang.jianwutong.config;

import android.os.Environment;

/**
 * Created by Chenss on 2015/8/24.
 */
public class SystemConfig {

    //ENCODING------GBK
    public static final String SERVER_CHAR_SET="GBK";

    //URL CONFIG
    public static final String SERVER_IP="121.40.250.221";
    public static final String SERVER_PORT="8080";

    public static final String URL_BASE = "http://" + SERVER_IP + ":" + SERVER_PORT;


    public static final String URL_LOGIN = URL_BASE + "/ybpt/web/ybmobile/MobileLogin_Login.action";

        //===URL PARAMTERS


    //public static final String KEY_ = "";

        //===URL PARAMTERS

    //public static final String VALUE_ = "";

    //本地头像保存的路径
    public static final String PATH_HEAD= Environment.getExternalStorageDirectory()+"/JianWuTong/msgHead/";
    public static final String HEAD_TYPE= ".png";

}
