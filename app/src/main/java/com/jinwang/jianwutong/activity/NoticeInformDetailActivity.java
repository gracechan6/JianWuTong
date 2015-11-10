package com.jinwang.jianwutong.activity;

import android.os.Bundle;

import com.jinwang.jianwutong.R;
import com.jinwang.jianwutong.fragment.ToolbarFragment;

public class NoticeInformDetailActivity extends WebviewActivity {

    /*
    *导航栏右侧不同按钮设置监听事件的区分标志*/
    public static final String RIGHT_RETRACTION="RETRACTION";

    //private ToolbarFragment toolbarFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_inform_detail);

//        toolbarFragment= (ToolbarFragment) getFragmentManager()
//                .findFragmentById(R.id.toolbarFragment);
//        toolbarFragment.initToolBar();
//        toolbarFragment.setRightOfToolbar(getString(R.string.retraction), null, RIGHT_RETRACTION);
//        toolbarFragment.setLeftOfToolbar(getString(R.string.back),getResources().getDrawable(R.drawable.icon_back));

        initToolBar();
        setRightOfToolbar(getString(R.string.retraction), null, RIGHT_RETRACTION);
        setLeftOfToolbar(getString(R.string.back),getResources().getDrawable(R.drawable.icon_back));
    }
}
