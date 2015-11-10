package com.jinwang.jianwutong.chat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager.WakeLock;

import com.jinwang.jianwutong.chat.Util;
import com.jinwang.jianwutong.chat.service.OnlineService;


public class TickAlarmReceiver extends BroadcastReceiver {

	WakeLock wakeLock;
	
	public TickAlarmReceiver() {
		
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if(Util.hasNetwork(context) == false){
			return;
		}
		Intent startSrv = new Intent(context, OnlineService.class);
		startSrv.putExtra("CMD", "TICK");
		context.startService(startSrv);
	}

}
