package com.jinwang.jianwutong.chat;

import android.content.Context;

import com.jinwang.jianwutong.DateTransform;
import com.jinwang.jianwutong.chat.entity.ChatEntity;
import com.jinwang.jianwutong.chat.entity.PushEntity;
import com.jinwang.jianwutong.db.JwtDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chenss on 2015/11/16.
 */
public class ChatModel {
    private static JwtDB jwtDB = JwtDB.getInstance();

    public static List<ChatEntity> querySenderChat(String sender,int num){
        List<ChatEntity> chatLists = new ArrayList<>();
        /*从本地数据库获取数据存储*/
        chatLists =jwtDB.getChatList(sender,num);

        //time格式：yyyy年MM月dd日 HH:MM
        /*ChatEntity chatEntity = new ChatEntity("龚华枫", "me", "我是龚华枫！", "2013年11月02日,周一,09:45");
        chatLists.add(chatEntity);
        chatEntity = new ChatEntity("龚华枫", "me", "明天开会，时间地点你安排下。", "2015年11月02日,周一,02:49");
        chatLists.add(chatEntity);
        chatEntity = new ChatEntity("我", "me", "好的，安排到第一会议室，下午13点开始。", DateTransform.getStringNowDate());
        chatLists.add(chatEntity);*/

        return chatLists;
    }

    public static Boolean save2DBChat(PushEntity entity){
        /*添加到Lists并存储到本地数据库*/

        ChatEntity chatEntity=new ChatEntity(entity.getFrom(),
                entity.getTo(),
                entity.getText(),
                DateTransform.getStringNowDate());

        return jwtDB.insertChat(chatEntity);
    }

    /**
     * 删除某条好友的记录
     * @param entity
     * @return
     */
    public static Boolean delDBChat(ChatEntity entity){
        return jwtDB.delChat(entity);
    }

}
