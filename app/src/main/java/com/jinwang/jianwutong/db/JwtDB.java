package com.jinwang.jianwutong.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.jinwang.jianwutong.BaseApplication;
import com.jinwang.jianwutong.chat.entity.ChatEntity;
import com.jinwang.jianwutong.chat.entity.MsgEntity;
import com.jinwang.jianwutong.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by Chenss on 2015/11/19.
 */
public class JwtDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "Jwt";

    /**
     * 表名
     */
    public final String TABLE_CHAT="tbChat";
    public final String TABLE_MSG="tbMsg";

    /**
     * 字段
     */
    public final String COLUMN_ID="id";
    public final String COLUMN_SENDER ="sender";
    public final String COLUMN_Receiver ="receiver";
    public final String COLUMN_MSG="msg";
    public final String COLUMN_TIME="time";
    public final String COLUMN_NAME="name";
    public final String COLUMN_LATESTMSG="latestMsg";
    public final String COLUMN_LATESTTIME="latestTime";
    public final String COLUMN_UNREAD="unread";
    public final String COLUMN_BRANCH="branch";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static JwtDB jwtDB;

    private SQLiteDatabase db;

    /**
     * 将构造方法私有化
     */
    private JwtDB() {
        DBHelper dbHelper = new DBHelper(BaseApplication.getContext(),
                DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取JwtDB的实例。
     */
    public synchronized static JwtDB getInstance() {
        if (jwtDB == null) {
            jwtDB = new JwtDB();
        }
        return jwtDB;
    }

    //MSG增删查改

    /**
     * 消息列表
     * @return
     */
    public ArrayList<MsgEntity> getMsgList(){
        ArrayList<MsgEntity> msgList=new ArrayList<>();
        MsgEntity entity;
        Cursor cursor=db.query(TABLE_MSG, null, null, null, null, null, "id desc");
        if (cursor.moveToFirst()){
            do {
                entity = new MsgEntity(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LATESTMSG)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LATESTTIME)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_UNREAD)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_BRANCH)));
                msgList.add(entity);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return msgList;
    }

    protected Boolean ifexistMsg(String name){
        Cursor cursor = db.rawQuery("select * from " + TABLE_MSG + " where " + COLUMN_NAME + "=?",
                new String[]{name});
        int count = 0;
        if (cursor.moveToFirst()) {
            do {
                count++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (count == 0) {
            return false;
        }
        return true;
    }

    /**
     * 更新消息列表
     * @param entity
     * exist 1存在 0不存在  -1未知
     * @return
     */
    public Boolean updateMsg(MsgEntity entity,int exist){
        if (exist == -1) {
            if (!ifexistMsg(entity.getName())) {
                return insertMsg(entity,0);
            }
        }else if (exist ==0){
            return insertMsg(entity,exist);
        }
        Boolean insert=false;
        db.beginTransaction();
        try {
            if (delMsg(entity)) {
                if (insertMsg(entity, 0)) {
                    db.setTransactionSuccessful();
                }
            }
        }catch (Exception e){
            LogUtil.e(getClass().getSimpleName(),e.getMessage().toString());
        }finally {
            db.endTransaction();
        }
        return insert;
    }

    /**
     * 删除对应联系人
     * @param entity
     * @return
     */
    public Boolean delMsg(MsgEntity entity){
        if (db.delete(TABLE_MSG,COLUMN_ID+"=?",new String[]{String.valueOf(entity.getId())})>0)
            return true;
        return false;
    }

    /**
     * 添加至消息列表
     * @param entity
     * exist 1存在 0不存在  -1未知
     * @return
     */
    public Boolean insertMsg(MsgEntity entity,int exist){
        if (exist == -1) {
            if (!ifexistMsg(entity.getName())) {
                return insertMsg(entity,0);
            }else
                return updateMsg(entity,1);
        }else if (exist==1){
            return updateMsg(entity,1);
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME,entity.getName());
        values.put(COLUMN_LATESTMSG,entity.getLatestMsg());
        values.put(COLUMN_LATESTTIME,entity.getLatestTime());
        values.put(COLUMN_UNREAD, entity.getUnread());
        values.put(COLUMN_BRANCH,entity.getBranch());

        long i=db.insert(TABLE_MSG, null, values);
        if (i==1)
            return true;
        return false;
    }

    //Chat增删查改

    /**
     * 获取聊天内容
     * @param hisName
     * @param num
     * @return
     */
    public ArrayList<ChatEntity> getChatList(String hisName,int num){
        ArrayList<ChatEntity> chatList=new ArrayList<>();
        ChatEntity entity;
        //sql语句暂时还不确定是否正确
        /*String select="select top 10 from " +
                "(select * from "+TABLE_CHAT+" where "+COLUMN_SENDER+"="+hisName+" order by id desc limit 0,10 )a"+
                "(select * from "+TABLE_CHAT+" where "+COLUMN_Receiver+"="+hisName+" order by id desc limit 0,10 )b " +
                "group by a.id order by id desc";*/
        Cursor cursor=db.rawQuery("select * from "+TABLE_CHAT+" where "+ COLUMN_SENDER +"='"+hisName
                +"' or "+ COLUMN_Receiver +"='"+hisName
                +"' order by id desc limit 0,10",null);
        if (cursor.moveToFirst()){
            do {
                entity = new ChatEntity(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_SENDER)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_Receiver)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_MSG)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TIME)));
                chatList.add(entity);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return chatList;
    }

    /**
     * 删除某一条聊天内容
     * @param entity
     * @return
     */
    public Boolean delChat(ChatEntity entity){
        //db.execSQL("delete from "+TABLE_CHAT+" where id="+entity.getId());
        if (db.delete(TABLE_CHAT,COLUMN_ID+"=?",new String[]{String.valueOf(entity.getId())})>0)
            return true;
        return false;
    }

    /**
     * 有新的消息收到或发出,存到数据库
     * @param entity
     * @return
     */
    public Boolean insertChat(ChatEntity entity){
        ContentValues values =new ContentValues();
        values.put(COLUMN_SENDER,entity.getSender());
        values.put(COLUMN_Receiver,entity.getReceiver());
        values.put(COLUMN_MSG,entity.getMsg());
        values.put(COLUMN_TIME,entity.getTime());
        if (db.insert(TABLE_CHAT,null,values)>0)
            return true;
        return false;
    }
}
