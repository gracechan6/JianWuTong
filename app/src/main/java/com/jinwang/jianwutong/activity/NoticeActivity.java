package com.jinwang.jianwutong.activity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jinwang.jianwutong.R;
import com.jinwang.jianwutong.fragment.ToolbarFragment;

public class NoticeActivity extends WebviewActivity {

    private RadioGroup radioGroup;

    //private ToolbarFragment toolbarFragment;

    /*
    *导航栏右侧不同按钮设置监听事件的区分标志*/
    public static final String RIGHT_NOTICE_NEW="NOTICE_NEW";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        radioGroup= (RadioGroup) findViewById(R.id.notice_radioGroup);
        radioGroup.setOnCheckedChangeListener(checkedChangeListener);

       /* toolbarFragment= (ToolbarFragment) getFragmentManager()
                .findFragmentById(R.id.toolbarFragment);*/
//        toolbarFragment.initToolBar();
//        /*获取目前有几条通知公告未阅读的*/
//
//        toolbarFragment.setTitle(getString(R.string.notice_inform) + "(" + ")");
//        toolbarFragment.setRightOfToolbar(getString(R.string.notice_new), null,RIGHT_NOTICE_NEW);
//        toolbarFragment.setLeftOfToolbar(getString(R.string.back),
//                getResources().getDrawable(R.drawable.icon_back));

        initToolBar();

        setTitle(getString(R.string.notice_inform)+"("+")");
        setRightOfToolbar(getString(R.string.notice_new), null,RIGHT_NOTICE_NEW);
        setLeftOfToolbar(getString(R.string.back),getResources().getDrawable(R.drawable.icon_back));


    }

    RadioGroup.OnCheckedChangeListener checkedChangeListener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int rbId = group.getCheckedRadioButtonId();
            switch (rbId){
                case R.id.posted:

                    break;
                case R.id.unpost:

                    break;
                case R.id.undone:

                    break;
                case R.id.expired:

                    break;
                default:break;
            }
        }
    };

}
