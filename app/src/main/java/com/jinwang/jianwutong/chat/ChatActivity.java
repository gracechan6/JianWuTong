package com.jinwang.jianwutong.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jinwang.jianwutong.DateTransform;
import com.jinwang.jianwutong.R;
import com.jinwang.jianwutong.activity.MainActivity;
import com.jinwang.jianwutong.chat.adapter.ChatAdapter;
import com.jinwang.jianwutong.chat.entity.ChatEntity;
import com.jinwang.jianwutong.chat.service.OnlineService;
import com.jinwang.jianwutong.fragment.ToolbarFragment;
import com.jinwang.jianwutong.util.LogUtil;
import com.jinwang.jianwutong.util.PreferenceUtil;

import org.ddpush.im.v1.client.appserver.Pusher;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chenss on 2015/10/29.
 */
public class ChatActivity extends Activity {

    public static final String RIGHT_HISDETAIL="RIGHT_HISDETAIL";

    private ToolbarFragment toolbarFragment;

    private ChatAdapter chatAdapter;
    private EditText sendtext;
    private ListView chatlsv;
    private List<ChatEntity> chatLists ;
    private ChatEntity chatEntity;
    private String hisName="";

    /*ddpush  params*/
    private Intent startSrv;

    @Override
    protected void onStart() {
        super.onStart();
        startSrv=new Intent(this,OnlineService.class);
        startSrv.putExtra("CMD","RESET");
        this.startService(startSrv);
        freshCurrentInfo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //

        setContentView(R.layout.activity_chat);
        hisName=getIntent().getStringExtra(MainActivity.HISNAME);
        toolbarFragment = (ToolbarFragment) getFragmentManager().findFragmentById(R.id.toolbarFragment);
        toolbarFragment.initToolBar();
        toolbarFragment.setTitle(hisName);
        toolbarFragment.setLeftOfToolbar(getString(R.string.back), getResources().getDrawable(R.drawable.icon_back));
        toolbarFragment.setRightOfToolbar(null, getResources().getDrawable(R.drawable.icon_back), RIGHT_HISDETAIL);

        sendtext = (EditText) findViewById(R.id.sendtext);
        initChatList();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 发送消息
     * @param view
     */
    public void doSend(View view){

        String text = sendtext.getText().toString();
        if (text.length()>0)
            return;//未输入内容点击发送，不操作直接返回。
        /*发送消息*/
        int port;
        try {
            port=Integer.parseInt(ChatParams.PUSH_PORT);
        }catch (Exception e){
            Toast.makeText(this.getApplicationContext(), "推送端口格式错误：" + ChatParams.PUSH_PORT, Toast.LENGTH_SHORT).show();
            return;
        }
        byte[] uuid = null;
        try {
            uuid = Util.md5Byte(hisName);
        }catch (Exception e){
            Toast.makeText(this.getApplicationContext(), "错误："+e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        byte[] msg = null;
        try {
            msg = text.getBytes("UTF-8");
        } catch (Exception e) {
            Toast.makeText(this.getApplicationContext(), "错误："+e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        //推送消息
        new Thread(new DdPushSendTask(this,ChatParams.SERVER_IP,port,uuid,msg)).start();
    }

    protected void freshCurrentInfo(){
        PreferenceUtil.saveChatInfo(getApplicationContext());
        String uuid = null;

        try {
            uuid=Util.md5(hisName);
        } catch (Exception e) {
            uuid="";
        }
        if(hisName == null || hisName.length() == 0){
            uuid="";
        }

        LogUtil.i("ddpush",uuid);
        //刷新界面
        /*try {
            this.findViewById(R.layout.activity_chat).postInvalidate();
        }catch (Exception e){}*/
    }

    protected void initChatList(){
        if(chatlsv == null){
            chatlsv = (ListView) findViewById(R.id.chatlsv);
            chatLists = new ArrayList<>();
            chatAdapter = new ChatAdapter(this,getApplicationContext(),chatLists);
            chatlsv.setAdapter(chatAdapter);
        }
        //time格式：yyyy年MM月dd日 HH:MM
        chatEntity = new ChatEntity("龚华枫","我是龚华枫！", "2013年11月02日,周一,09:45");
        chatLists.add(chatEntity);
        chatEntity = new ChatEntity("龚华枫","明天开会，时间地点你安排下。", "2015年11月02日,周一,02:49");
        chatLists.add(chatEntity);
        chatEntity = new ChatEntity("我","好的，安排到第一会议室，下午13点开始。", DateTransform.getStringNowDate());
        chatLists.add(chatEntity);
    }

    class DdPushSendTask implements Runnable{
        private Context context;
        private String serverIp;
        private int port;
        private byte[] uuid;
        private byte[] msg;

        public DdPushSendTask(Context context, String serverIp, int port, byte[] uuid, byte[] msg){
            this.context = context;
            this.serverIp = serverIp;
            this.port = port;
            this.uuid = uuid;
            this.msg = msg;
        }

        public void run(){
            Pusher pusher = null;
            Intent startSrv = new Intent(context, OnlineService.class);
            startSrv.putExtra("CMD", "TOAST");
            try{
                boolean result;
                pusher = new Pusher(serverIp,port, 1000*5);
                result = pusher.push0x20Message(uuid,msg);
                if(result){
                    startSrv.putExtra("TEXT", "自定义信息发送成功");
                }else{
                    startSrv.putExtra("TEXT", "发送失败！格式有误");
                }
            }catch(Exception e){
                e.printStackTrace();
                startSrv.putExtra("TEXT", "发送失败！"+e.getMessage());
            }finally{
                if(pusher != null){
                    try{pusher.close();}catch(Exception e){};
                }
            }
            context.startService(startSrv);
        }
    }
}
