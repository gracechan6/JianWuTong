package com.jinwang.jianwutong.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.jinwang.jianwutong.R;
import com.jinwang.jianwutong.chat.ChatActivity;
import com.jinwang.jianwutong.chat.adapter.MsgAdapter;
import com.jinwang.jianwutong.chat.entity.MsgEntity;
import com.jinwang.jianwutong.chat.service.OnlineService;
import com.jinwang.jianwutong.fragment.BottomFragment;
import com.jinwang.jianwutong.util.PhoneScreenUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 *
 */
public class MainActivity extends WebviewActivity {

    private RadioGroup radioGroup;
    /*导航栏右侧不同按钮设置监听事件的区分标志*/
    public static final String RIGHT_FAVORITE="FAVORITE";
    public static final String RIGHT_MESSAGE="MESSAGE";

    //private ToolbarFragment toolbarFragment;
    private BottomFragment bottomFragment;

    /*消息栏目需要用到的控件及数据定义*/
    private SwipeRefreshLayout srlayout;
    private SwipeMenuListView msgsLsv;
    private List<MsgEntity> msgsLists=new ArrayList<>();
    private static Boolean order=false;//true:按时间从最近到最远排序
    private MsgAdapter msgAdapter ;

    /*消息列表进入聊天界面参数*/
    public static final String HISNAME="HISNAME";

    /**
     * DDPUSH 终端重置
     */
    protected void startDDPush() {
        super.onStart();
        Intent startSrv=new Intent(this,OnlineService.class);
        startSrv.putExtra("CMD", "RESET");
        this.startService(startSrv);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* toolbarFragment= (ToolbarFragment) getFragmentManager()
                .findFragmentById(R.id.toolbarFragment);

        toolbarFragment.initToolBar();
        toolbarFragment.setTitle(getString(R.string.app_name));
        toolbarFragment.setRightOfToolbar("公告管理", null, "SAD");*/

        startDDPush();
        initToolBar();
        setTitle(getString(R.string.app_name));
        setRightOfToolbar("公告管理", null, "SAD");

        bottomFragment = (BottomFragment) getFragmentManager()
                .findFragmentById(R.id.bottomFragment);
//        判断是否有未读消息，测试未读消息为3

        bottomFragment.setUnReadMsg(333);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(checkedChangeListener);

        srlayout = (SwipeRefreshLayout) findViewById(R.id.srlayout);
        srlayout.setOnRefreshListener(refreshListener);
    }

    RadioGroup.OnCheckedChangeListener checkedChangeListener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int rbId = group.getCheckedRadioButtonId();
            hideMsgsLsv();
            //toolbarFragment.hideToolbar(false);
            hideToolbar(false);
            switch (rbId) {
                case R.id.index:
//                    toolbarFragment.setTitle(getString(R.string.app_name));
//                    toolbarFragment.setRightOfToolbar(null, null, null);
                    setTitle(getString(R.string.app_name));
                    setRightOfToolbar(null, null, null);
                    break;
                case R.id.message:
                    //LogUtil.d(getClass().getSimpleName(), "grace:" + getClass().getSimpleName().toString());
//                    toolbarFragment.setTitle(getString(R.string.message));
//                    toolbarFragment.setRightOfToolbar("",
//                            getResources().getDrawable(R.drawable.icon_header_add_chat), RIGHT_MESSAGE);
                    setTitle(getString(R.string.message));
                    setRightOfToolbar("",
                            getResources().getDrawable(R.drawable.icon_header_add_chat), RIGHT_MESSAGE);
                    showMsgsLsv();
                    break;
                case R.id.contacts:
//                    toolbarFragment.setTitle(getString(R.string.contacts));
//                    toolbarFragment.setRightOfToolbar(getString(R.string.favorite), null,RIGHT_FAVORITE);
                    setTitle(getString(R.string.contacts));
                    setRightOfToolbar(getString(R.string.favorite), null, RIGHT_FAVORITE);
                    break;
                case R.id.task:
                    //toolbarFragment.hideToolbar(true);
                    hideToolbar(true);
                    break;
                case R.id.me:
//                    toolbarFragment.setTitle(getString(R.string.me));
//                    toolbarFragment.setRightOfToolbar(null, null, null);
                    setTitle(getString(R.string.me));
                    setRightOfToolbar(null, null, null);
                    hideMsgsLsv();
                    break;
                default:
                    break;
            }
        }
    };

    private SwipeMenuCreator creator;

    private void initCreator(){
        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu swipeMenu) {
                //create 删除 item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(PhoneScreenUtils.getScreenWidth(getApplicationContext())/6);
                // set a icon
                deleteItem.setTitle("删除");
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setTitleSize(18);

                // add to menu
                swipeMenu.addMenuItem(deleteItem);
            }
        };
    }
    /**
     * 获取消息列表并显示
     */
    public void showMsgsLsv(){
        if (msgAdapter == null) {
            msgAdapter = new MsgAdapter(getApplicationContext(), msgsLists);
            msgsLsv = (SwipeMenuListView) findViewById(R.id.msgLsv);
            initCreator();
            if (creator != null)
                msgsLsv.setMenuCreator(creator);
            msgsLsv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu swipeMenu, int index) {
                    switch (index) {
                        case 0:
                            // delete
                            msgsLists.remove(position);
                            msgAdapter.notifyDataSetChanged();
                            break;
                        case 1:
                            // 未设置
                            break;
                    }
                    // false : close the menu; true : not close the menu
                    return false;
                }
            });
            //设置左划
            msgsLsv.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
            msgsLsv.setOnItemClickListener(onItemClickListener);
            msgsLsv.setAdapter(msgAdapter);
        }

        //获取消息列表
        /*example
        MsgEntity msgEntity=new MsgEntity();//once
        msgEntity.setBranch();
        msgEntity.setLatestMsg();
        msgEntity.setLatestTime();
        msgEntity.setName();
        msgEntity.setUnread();
        msgsLists.add(msgEntity);
        */

        MsgEntity msgEntity=new MsgEntity();
        msgEntity.setBranch("海曙");
        msgEntity.setLatestMsg("这个到时候我过来看下");
        msgEntity.setLatestTime("昨天");
        msgEntity.setName("陈虹");
        msgEntity.setUnread(0);
        msgsLists.add(msgEntity);

        msgEntity=new MsgEntity();
        msgEntity.setBranch("江东");
        msgEntity.setLatestMsg("嗯 好的");
        msgEntity.setLatestTime("20分钟前");
        msgEntity.setName("我");
        msgEntity.setUnread(2);
        msgsLists.add(msgEntity);

        msgEntity=new MsgEntity();
        msgEntity.setBranch("市局");
        msgEntity.setLatestMsg("下午会议室开会");
        msgEntity.setLatestTime("3分钟前");
        msgEntity.setName("于迪");
        msgEntity.setUnread(5);
        msgsLists.add(msgEntity);
        //initMsgsList();
        //Collections.reverse(msgsList);

        if (!order)
        {
            Collections.reverse(msgsLists);
            order=true;
        }

        //msgAdapter = new MsgAdapter(getApplicationContext(),msgsLists);

        //msgsLsv.setAdapter(msgAdapter);

        //显示消息列表
        if (srlayout.getVisibility()==View.GONE)
            srlayout.setVisibility(View.VISIBLE);
    }

    public void hideMsgsLsv(){
        if (srlayout.getVisibility()==View.VISIBLE)
            srlayout.setVisibility(View.GONE);
    }


    /**
     * 消息列表选中其中一个进入该页面
     */
    AdapterView.OnItemClickListener onItemClickListener = new
            AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //选中某一个
            Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
            intent.putExtra(HISNAME,msgsLists.get(position).getName());
            startActivity(intent);
        }
    };

    /**
     * 消息列表下拉刷新
     */
    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            MsgEntity msgEntity=new MsgEntity();
            msgEntity.setBranch("市局");
            msgEntity.setLatestMsg("下午会议室开会");
            msgEntity.setLatestTime("1分钟前");
            msgEntity.setName("龚华枫");
            msgEntity.setUnread(5);
            int i = compareSameEntity("龚华枫");
            if (i!=-1)
                msgsLists.remove(i);
            if (order)  Collections.reverse(msgsLists);
            msgsLists.add(msgEntity);
            Collections.reverse(msgsLists);
            //initMsgsList();
            //Collections.reverse(msgsList);
            msgAdapter.notifyDataSetChanged();
            srlayout.setRefreshing(false);
        }
    };

    /**
     * 判断消息列表是否已经有这个人存在
     * @param name
     * @return
     */
    protected int compareSameEntity(String name){
        int i = 0;
        for (;i<msgsLists.size();i++){
            if (msgsLists.get(i).getName().equals(name))
                break;
        }
        if (i==msgsLists.size())    return -1;
        return i;
    }

    /*protected void initMsgsList(){
        msgsList.clear();
        for (int i=0;i<msgsLists.size();i++)
            msgsList.add(msgsLists.get(i));
    }*/

}
