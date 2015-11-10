package com.jinwang.jianwutong.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinwang.jianwutong.R;
import com.jinwang.jianwutong.util.PhoneScreenUtils;

/**
 * Created by Chenss on 2015/10/16.
 */
public class BottomFragment extends Fragment {

    private View view;
    private Activity activity;
    private TextView tvUnread;

    //private RadioButton rbMsg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bottom_fragment,container,false);
        setActivity();
        tvUnread= (TextView) view.findViewById(R.id.tvUnread);
        int width = PhoneScreenUtils.getScreenWidth(activity.getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                PhoneScreenUtils.getWidgetWidth(tvUnread),
                PhoneScreenUtils.getWidgetHeight(tvUnread));
        layoutParams.setMargins(width * 3 / 10 , 0, 0, 0);
        tvUnread.setLayoutParams(layoutParams);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void setActivity(){
        activity=getActivity();
    }

    public void setUnReadMsg(int num){
        if (num>0) {
            if (num<99)
                tvUnread.setText(String.valueOf(num));
            else
                tvUnread.setText("99+");

            tvUnread.setVisibility(View.VISIBLE);
        }
        else
            tvUnread.setVisibility(View.GONE);
    }
}
