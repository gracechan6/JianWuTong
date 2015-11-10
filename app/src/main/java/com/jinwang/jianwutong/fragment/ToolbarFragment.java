package com.jinwang.jianwutong.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jinwang.jianwutong.R;
import com.jinwang.jianwutong.activity.MainActivity;
import com.jinwang.jianwutong.activity.NoticeActivity;
import com.jinwang.jianwutong.activity.NoticeInformDetailActivity;
import com.jinwang.jianwutong.activity.NoticePostActivity;

/**
 * Created by Chenss on 2015/10/20.
 */
public class ToolbarFragment extends Fragment {

    private View view;
    private Toolbar mToolBar;
    private TextView mTitle;
    private TextView mRight,mLeft;
    private Drawable drawable;

    private Toolbar.LayoutParams lp;

    private Activity activity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.toolbar_fragment,container,false);
        setActivity();
        return view;
    }

    public void setActivity(){
        activity=getActivity();
    }

    /**
     * 初始化导航栏
     */
    public void initToolBar() {
        mToolBar = (Toolbar) view.findViewById(R.id.toolbar);
        //setSupportActionBar(mToolBar);
        mToolBar.setBackgroundColor(getResources().getColor(R.color.toolbar_bg));

        //设置标题
        lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        mTitle = new TextView(activity.getApplicationContext());
        mTitle.setTextColor(getResources().getColor(R.color.black));
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mToolBar.addView(mTitle, lp);
    }

    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title)
    {
        this.mTitle.setText(title);
    }

    /**
     * 设置导航栏左侧返回
     * @param s
     * @param icon
     */
    public void setLeftOfToolbar(String s,Drawable icon){
        if (mLeft == null){
            lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.LEFT;
            mLeft = new TextView(activity.getApplicationContext());
            //drawable=getResources().getDrawable(R.drawable.ic_launcher_blue);
            //drawable.setBounds(0, 0, 50, 50);
            //mRight.setCompoundDrawables(drawable, null, null, null);
            mLeft.setText("");
            mLeft.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            mLeft.setTextColor(getResources().getColor(R.color.bottom_text_selected));
            mLeft.setGravity(Gravity.CENTER);
            mLeft.setPadding(10, 0, 0, 0);
            mToolBar.addView(mLeft, lp);
        }
        if (s!=null)  mLeft.setText(s);
        else  mLeft.setText("");
        if (icon != null) {
            drawable = icon;
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mLeft.setCompoundDrawables(drawable, null, null, null);
        }
        else
            mLeft.setCompoundDrawables(null, null, null, null);
        mLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    /**
     * 设置导航栏右侧操作，flag作为区分不同操作的标志
     * @param s
     * @param icon
     * @param flag
     */
    public void setRightOfToolbar(String s,Drawable icon,final String flag){
        if (mRight == null){
            lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.RIGHT;
            mRight = new TextView(activity.getApplicationContext());
            mRight.setText("");
            mRight.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            mRight.setTextColor(getResources().getColor(R.color.bottom_text_selected));
            mRight.setPadding(0, 0, 10, 0);
            mRight.setGravity(Gravity.CENTER);
            mToolBar.addView(mRight, lp);
        }

        if (s!=null)  mRight.setText(s);
        else  mRight.setText("");
        if (icon != null) {
            drawable = icon;
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mRight.setCompoundDrawables(drawable, null, null, null);
        }
        else
            mRight.setCompoundDrawables(null, null, null, null);
        if (flag!=null) {
            mRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (flag) {
                        case MainActivity.RIGHT_FAVORITE:
                            //点击收藏夹后如何操作
                            break;
                        case MainActivity.RIGHT_MESSAGE:
                            //点击消息图标后如何操作
                            break;
                        case NoticeActivity.RIGHT_NOTICE_NEW:
                            startActivity(new Intent(activity.getApplicationContext(),NoticePostActivity.class));
                            break;
                        case NoticePostActivity.RIGHT_FINISH:
                            //完成事件
                            break;
                        case NoticeInformDetailActivity.RIGHT_RETRACTION:
                            //撤稿点击事件
                            break;
                        default:
                            startActivity(new Intent(activity.getApplicationContext(), NoticeActivity.class));
                            break;
                    }
                    //startActivity(new Intent(getApplicationContext(), NoticeActivity.class));
                }
            });
        }
    }

    /**
     * 是否隐藏导航栏，默认开启
     * 注意，有个操作隐藏后之后操作要记得开回来
     * @param b
     */
    public void hideToolbar(Boolean b){
        if (b){
            if (mToolBar.getVisibility()==View.VISIBLE)
                mToolBar.setVisibility(View.GONE);
        }
        else {
            if (mToolBar.getVisibility()==View.GONE)
            mToolBar.setVisibility(View.VISIBLE);
        }
    }

}
