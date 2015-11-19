package com.jinwang.jianwutong.chat.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.widget.Toast;

import com.jinwang.jianwutong.R;
import com.jinwang.jianwutong.ToastMsg;
import com.jinwang.jianwutong.activity.MainActivity;
import com.jinwang.jianwutong.chat.ChatActivity;
import com.jinwang.jianwutong.chat.ChatParams;
import com.jinwang.jianwutong.chat.DateTimeUtil;
import com.jinwang.jianwutong.chat.Params;
import com.jinwang.jianwutong.chat.Util;
import com.jinwang.jianwutong.chat.entity.PushEntity;
import com.jinwang.jianwutong.chat.receiver.TickAlarmReceiver;
import com.jinwang.jianwutong.util.PreferenceUtil;
import com.jinwangmobile.core.dataparser.json.AbsJSONUtils;

import org.ddpush.im.v1.client.appuser.Message;
import org.ddpush.im.v1.client.appuser.UDPClientBase;

import java.io.IOException;
import java.nio.ByteBuffer;

public class OnlineService extends Service {
	
	protected PendingIntent tickPendIntent;
	protected TickAlarmReceiver tickAlarmReceiver = new TickAlarmReceiver();
	WakeLock wakeLock;
	MyUdpClient myUdpClient;
	Notification n;
	
	public class MyUdpClient extends UDPClientBase {

		public MyUdpClient(byte[] uuid, int appid, String serverAddr, int serverPort)
				throws Exception {
			super(uuid, appid, serverAddr, serverPort);
		}

		@Override
		public boolean hasNetworkConnection() {
			return Util.hasNetwork(OnlineService.this);
		}
		

		@Override
		public void trySystemSleep() {
			tryReleaseWakeLock();
		}

		@Override
		public void onPushMessage(Message message) {
			if(message == null){
				ToastMsg.showToast("empty msg");
				return;
			}
			if(message.getData() == null || message.getData().length == 0){
				ToastMsg.showToast("empty time");
				return;
			}

			//用不到，直接收自定义消息
			if(message.getCmd() == 16){// 0x10 通用推送信息
				notifyUser(16,"DDPush通用推送信息","时间："+ DateTimeUtil.getCurDateTime(),"收到通用推送信息");
			}
			if(message.getCmd() == 17){// 0x11 分组推送信息
				long msg = ByteBuffer.wrap(message.getData(), 5, 8).getLong();
				notifyUser(17,"DDPush分组推送信息",""+msg,"收到通用推送信息");
			}
			//用不到，直接收自定义消息end
			if(message.getCmd() == 32){// 0x20 自定义推送信息
				String str = null;
				try{
					str = new String(message.getData(),5,message.getContentLength(), "UTF-8");
				}catch(Exception e){
					str = Util.convert(message.getData(),5,message.getContentLength());
				}
				//ToastMsg.showToast(str);
				notifyUser(32, "DDPush自定义推送信息", "" + str, "收到自定义推送信息");
			}
			setPkgsInfo();
		}

	}

	public OnlineService() {
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.setTickAlarm();
		
		PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "OnlineService");
		
		resetClient();
		
		//notifyRunning();
	}

	@Override
	public int onStartCommand(Intent param, int flags, int startId) {
		if(param == null){
			return START_STICKY;
		}
		String cmd = param.getStringExtra("CMD");
		if(cmd == null){
			cmd = "";
		}
		if(cmd.equals("TICK")){
			if(wakeLock != null && wakeLock.isHeld() == false){
				wakeLock.acquire();
			}
		}
		if(cmd.equals("RESET")){
			if(wakeLock != null && wakeLock.isHeld() == false){
				wakeLock.acquire();
			}
			resetClient();
		}
		if(cmd.equals("TOAST")){
			String text = param.getStringExtra("TEXT");
			if(text != null && text.trim().length() != 0){
				Toast.makeText(this, text, Toast.LENGTH_LONG).show();
			}
		}
		
		setPkgsInfo();

		return START_STICKY;
	}

	/**
	 * 收包发包数更新
	 */
	protected void setPkgsInfo(){
		if(this.myUdpClient == null){
			return;
		}
		long sent = myUdpClient.getSentPackets();
		long received = myUdpClient.getReceivedPackets();
		SharedPreferences account = this.getSharedPreferences(Params.DEFAULT_PRE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = account.edit();
		editor.putString(Params.SENT_PKGS, ""+sent);
		editor.putString(Params.RECEIVE_PKGS, ""+received);
		editor.commit();
	}
	
	protected void resetClient(){
		//SharedPreferences account = this.getSharedPreferences(Params.DEFAULT_PRE_NAME, Context.MODE_PRIVATE);
		String serverIp = ChatParams.SERVER_IP;
		String serverPort = ChatParams.SERVER_PORT;
		String pushPort = ChatParams.PUSH_PORT;
		String userName = PreferenceUtil.getName(getApplicationContext());
		if(serverIp == null || serverIp.trim().length() == 0
				|| serverPort == null || serverPort.trim().length() == 0
				|| pushPort == null || pushPort.trim().length() == 0
				|| userName == null || userName.trim().length() == 0){
			return;
		}
		if(this.myUdpClient != null){
			try{myUdpClient.stop();}catch(Exception e){}
		}
		try{
			myUdpClient = new MyUdpClient(Util.md5Byte(userName), 1, serverIp, Integer.parseInt(serverPort));
			myUdpClient.setHeartbeatInterval(50);
			myUdpClient.start();
			/*SharedPreferences.Editor editor = account.edit();
			editor.putString(Params.SENT_PKGS, "0");
			editor.putString(Params.RECEIVE_PKGS, "0");
			editor.commit();*/
		}catch(Exception e){
			Toast.makeText(this.getApplicationContext(), "操作失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		//Toast.makeText(this.getApplicationContext(), "ddpush：终端重置", Toast.LENGTH_LONG).show();
	}
	
	protected void tryReleaseWakeLock(){
		if(wakeLock != null && wakeLock.isHeld() == true){
			wakeLock.release();
		}
	}

	/**
	 * 定时任务 每隔5分钟触发发一次广播TickAlarmReceiver
	 */
	protected void setTickAlarm(){
		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this,TickAlarmReceiver.class);
		int requestCode = 0;  
		tickPendIntent = PendingIntent.getBroadcast(this,
				requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		//小米2s的MIUI操作系统，目前最短广播间隔为5分钟，少于5分钟的alarm会等到5分钟再触发！2014-04-28
		long triggerAtTime = System.currentTimeMillis();
		int interval = 300 * 1000;  
		alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime, interval, tickPendIntent);
	}
	
	protected void cancelTickAlarm(){
		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmMgr.cancel(tickPendIntent);  
	}
	
	protected void notifyRunning(){
		//grace added --不同版本控制notification显示更新
		//不同版本间可共用的属性
		NotificationManager notificationManager=(NotificationManager)this.
				getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent = new Intent(this,MainActivity.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

		//如果当前使用手机版本号小于11 则使用setLatestEventInfo方法
		/*if (Build.VERSION.SDK_INT<=11){
			n = new Notification();
			n.contentIntent = pi;
			//此方法高版本已经弃用使用Notification.Builder instead!
			//n.setLatestEventInfo(this, "DDPushDemoUDP", "正在运行", pi);

		}*/
			Notification.Builder builder = new Notification.Builder(getApplicationContext());
			builder.setContentIntent(pi)
					.setContentTitle("DDPushDemoUdp")//标题
					.setContentText("正在运行")//内容
					.setAutoCancel(true)//设置可以清除
					.build();
			n = builder.getNotification();


		//added end

		//n.defaults = Notification.DEFAULT_ALL;
		//n.flags |= Notification.FLAG_SHOW_LIGHTS;  
		//n.flags |= Notification.FLAG_AUTO_CANCEL;
		n.flags |= Notification.FLAG_ONGOING_EVENT;
		n.flags |= Notification.FLAG_NO_CLEAR;
		//n.iconLevel = 5;
		           
		n.icon = R.drawable.ic_launcher;
		n.when = System.currentTimeMillis();
		n.tickerText = "DDPushDemoUDP正在运行";
		notificationManager.notify(0, n);
	}
	
	protected void cancelNotifyRunning(){
		NotificationManager notificationManager=(NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(0);
	}
	
	public void notifyUser(int id, String title, String msg, String tickerText){
		PushEntity entity = null;
		try {
			entity = AbsJSONUtils.defaultInstance().JSON2Object(msg,PushEntity.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*新消息存储到数据库*/


		/*发送广播*/
		Intent intent=new Intent();
		intent.setAction(ChatActivity.NEW_MESSAGE_RECV);
		intent.putExtra("Sender",entity.getFrom());
		sendBroadcast(intent);

		/*NotificationManager notificationManager=(NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification n = new Notification();
		Intent intent = new Intent(this,MainActivity.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);*/

		/*n.contentIntent = pi;

		n.setLatestEventInfo(this, title, content, pi);*/

		//grace  --add  api controls
		//如果当前使用手机版本号小于11 则使用setLatestEventInfo方法
		/*if (Build.VERSION.SDK_INT<=11){
			n = new Notification();
			n.contentIntent = pi;
			//此方法高版本已经弃用使用Notification.Builder instead!
			//n.setLatestEventInfo(this, "DDPushDemoUDP", "正在运行", pi);

		}
		//本项目最低版本为14，因此不存在该问题
		*/

		/*Notification.Builder builder = new Notification.Builder(getApplicationContext());
		builder.setContentIntent(pi)
				.setContentTitle(title)//标题
				.setContentText(content)//内容
				.setAutoCancel(true)//设置可以清除
				.build();
		n = builder.getNotification();
		//added end

		n.defaults = Notification.DEFAULT_ALL;
		n.flags |= Notification.FLAG_SHOW_LIGHTS;
		n.flags |= Notification.FLAG_AUTO_CANCEL;

		n.icon = R.drawable.ic_launcher;  
		n.when = System.currentTimeMillis();
		n.tickerText = tickerText;
		notificationManager.notify(id, n);*/
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//this.cancelTickAlarm();
		//cancelNotifyRunning();
		this.tryReleaseWakeLock();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


}
