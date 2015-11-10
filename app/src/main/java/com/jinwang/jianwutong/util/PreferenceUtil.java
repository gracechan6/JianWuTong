package com.jinwang.jianwutong.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.jinwang.jianwutong.chat.Params;

/**
 * Created by Chenss on 2015/10/27.
 */
public class PreferenceUtil {
    public static final String PREFERENCE = "PREFERENCE";

    public static final String PREFERENCE_NAME = "PREFERENCE_NAME";
    public static final String PREFERENCE_PWD = "PREFERENCE_PWD";

    public static void saveLoginInfo(Context context,String name,String pwd){
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREFERENCE_NAME,name);
        editor.putString(PREFERENCE_PWD, pwd);
        editor.commit();
    }

    public static String getName(Context context){
        SharedPreferences sp=context.getSharedPreferences(PREFERENCE,Context.MODE_PRIVATE);
        return sp.getString(PREFERENCE_NAME,"我");
    }

    public static final void  saveChatInfo(Context context){
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Params.SERVER_IP,Params.SERVER_IP);
        editor.putString(Params.SERVER_PORT,Params.SERVER_PORT);
        editor.putString(Params.PUSH_PORT,Params.PUSH_PORT);
        editor.putString(Params.USER_NAME,sp.getString(PREFERENCE_NAME,"我"));
        editor.putString(Params.SENT_PKGS, "0");
        editor.putString(Params.RECEIVE_PKGS, "0");
        editor.commit();
    }
}
