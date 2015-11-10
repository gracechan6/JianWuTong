package com.jinwang.jianwutong.fragment;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jinwang.jianwutong.R;

/**
 * Created by Chenss on 2015/10/16.
 */
public class BottomNoticeFrament extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottom_notice_fragment,container,false);
        return view;
    }

}
