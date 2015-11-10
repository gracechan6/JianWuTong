package com.jinwang.jianwutong.activity;

import android.os.Bundle;

import com.jinwang.jianwutong.R;
import com.jinwang.jianwutong.fragment.ToolbarFragment;

public class NoticePostActivity extends WebviewActivity {

    /*
    *导航栏右侧不同按钮设置监听事件的区分标志*/
    public static final String RIGHT_FINISH="FINISH";

    //private ToolbarFragment toolbarFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_post);

        /*toolbarFragment= (ToolbarFragment) getFragmentManager()
                .findFragmentById(R.id.toolbarFragment);

        toolbarFragment.initToolBar();
        toolbarFragment.setTitle(getString(R.string.notice_inform_new));
        toolbarFragment.setRightOfToolbar(getString(R.string.finish),null,RIGHT_FINISH);
        toolbarFragment.setLeftOfToolbar(getString(R.string.back),getResources().getDrawable(R.drawable.icon_back));*/
        initToolBar();
        setTitle(getString(R.string.notice_inform_new));
        setRightOfToolbar(getString(R.string.finish), null, RIGHT_FINISH);
        setLeftOfToolbar(getString(R.string.back),getResources().getDrawable(R.drawable.icon_back));
    }
}
