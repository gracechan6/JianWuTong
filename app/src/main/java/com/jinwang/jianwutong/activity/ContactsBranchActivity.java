package com.jinwang.jianwutong.activity;

import android.os.Bundle;

import com.jinwang.jianwutong.R;
import com.jinwang.jianwutong.fragment.ToolbarFragment;

/**
 * Created by Chenss on 2015/10/20.
 */
public class ContactsBranchActivity extends WebviewActivity {

    private ToolbarFragment toolbarFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*toolbarFragment= (ToolbarFragment) getFragmentManager()
                .findFragmentById(R.id.toolbarFragment);*/
//        toolbarFragment.initToolBar();
//        /*获取对应部门的名字，设置成导航栏标题*/
//
//        toolbarFragment.setTitle("宁波市检察院");
//        toolbarFragment.setLeftOfToolbar(getString(R.string.back)
//                ,getResources().getDrawable(R.drawable.icon_back));

        initToolBar();
        setTitle("宁波市检察院");
        setLeftOfToolbar(getString(R.string.back)
               ,getResources().getDrawable(R.drawable.icon_back));
    }

}
