package com.jinwang.jianwutong.chat;

import android.app.Activity;
import android.content.BroadcastReceiver;
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
import com.jinwang.jianwutong.ToastMsg;
import com.jinwang.jianwutong.activity.MainActivity;
import com.jinwang.jianwutong.chat.adapter.ChatAdapter;
import com.jinwang.jianwutong.chat.entity.ChatEntity;
import com.jinwang.jianwutong.chat.entity.PushEntity;
import com.jinwang.jianwutong.chat.service.OnlineService;
import com.jinwang.jianwutong.fragment.ToolbarFragment;
import com.jinwang.jianwutong.util.LogUtil;
import com.jinwang.jianwutong.util.PreferenceUtil;
import com.jinwangmobile.core.dataparser.json.AbsJSONUtils;

import org.ddpush.im.v1.client.appserver.Pusher;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chenss on 2015/10/29.
 */
public class ChatActivity extends Activity {

    public static final String RIGHT_HISDETAIL="RIGHT_HISDETAIL";
    public static final String NEW_MESSAGE_RECV="NEW_MESSAGE_RECV";

    private ToolbarFragment toolbarFragment;

    private ChatAdapter chatAdapter;
    private EditText sendtext;
    private ListView chatlsv;
    private List<ChatEntity> chatLists ;
    private ChatEntity chatEntity;
    private String hisName="";

    /*ddpush  params*/
    private Intent startSrv;
    private IntentFilter mFilter = new IntentFilter();
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(NEW_MESSAGE_RECV)){
                //聊天界面是否开启，未读消息数+1
                if (intent.getStringExtra("Sender").equals(hisName)){
                    List<ChatEntity> mList=ChatModel.querySenderChat(hisName,10);
                    chatLists.clear();
                    chatLists.addAll(mList);
                    chatAdapter.notifyDataSetChanged();
                    chatlsv.setSelection(chatlsv.getCount() - 1);
                }
                else {
                    ToastMsg.showToast("您有新的消息，请注意查收。");
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加广播
        /*IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(NEW_MESSAGE_RECV);
        this.registerReceiver(mReceiver,intentFilter);*/

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
    protected void onStart() {
        super.onStart();
        mFilter.addAction(NEW_MESSAGE_RECV);
        registerReceiver(mReceiver,mFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mReceiver);
    }


    /**
     * 发送消息
     * @param view
     */
    public void doSend(View view){

        String sendData = sendtext.getText().toString();
        if (sendData.length()==0) {
            sendtext.requestFocus();
            return;//未输入内容点击发送，不操作直接返回。
        }
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
            LogUtil.i("ddpush-hisname:",hisName+"!!!"+uuid.toString());
        }catch (Exception e){
            Toast.makeText(this.getApplicationContext(), "错误："+e.getMessage(), Toast.LENGTH_SHORT).show();
            //对方名字有误 无法转换
            return;
        }

        //以json格式传送消息
        String jsonResult = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("from", PreferenceUtil.getName(getApplicationContext()));
            jsonObject.put("to",hisName);
            jsonObject.put("text",sendData);
            //json转成字符串格式
            jsonResult = jsonObject.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }

        byte[] msg = null;
        try {
            msg = jsonResult.getBytes("UTF-8");
        } catch (Exception e) {
            Toast.makeText(this.getApplicationContext(), "错误："+e.getMessage(), Toast.LENGTH_SHORT).show();
            sendtext.requestFocus();
            return;
        }

        /*把消息存储到数据库,更新现有list*/
        try {
            PushEntity entity= AbsJSONUtils.defaultInstance().JSON2Object(msg,PushEntity.class);
            ChatModel.save2DBChat(entity);
            ChatEntity centity=new ChatEntity(entity.getFrom(),entity.getTo(),entity.getText(),DateTransform.getStringNowDate());
            chatLists.add(centity);
            chatAdapter.notifyDataSetChanged();
            chatlsv.setSelection(chatlsv.getCount()-1);
            sendtext.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //推送消息
        new Thread(new DdPushSendTask(this,ChatParams.SERVER_IP,port,uuid,msg)).start();
    }

    /**
     * 加载历史消息，（显示定量，下拉刷新--未实现）
     */
    protected void initChatList(){
        if(chatlsv == null){
            chatlsv = (ListView) findViewById(R.id.chatlsv);
            chatLists = new ArrayList<>();
            chatAdapter = new ChatAdapter(this,getApplicationContext(),chatLists);
            chatlsv.setAdapter(chatAdapter);
        }
        chatLists.addAll(ChatModel.querySenderChat( hisName,10));
    }

    /**
     * 发送消息  线程
     */
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
