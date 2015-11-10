package com.jinwang.jianwutong;

import android.app.ListActivity;
import android.widget.Toast;

/**
 * Created by Chenss on 2015/10/30.
 */
public class ToastMsg extends ListActivity {

    public static void showToast(String msg){
        Toast.makeText(BaseApplication.getContext(),msg,Toast.LENGTH_SHORT).show();
    }
}
