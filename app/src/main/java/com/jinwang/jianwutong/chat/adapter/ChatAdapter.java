package com.jinwang.jianwutong.chat.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jinwang.jianwutong.ActionSheet;
import com.jinwang.jianwutong.DateTransform;
import com.jinwang.jianwutong.R;
import com.jinwang.jianwutong.TextManager;
import com.jinwang.jianwutong.ToastMsg;
import com.jinwang.jianwutong.chat.entity.ChatEntity;
import com.jinwang.jianwutong.util.PreferenceUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by Chenss on 2015/10/27.
 */
public class ChatAdapter extends BaseAdapter {

    class ViewHolder{
        TextView time,hisText,myText;
    }
    private List<ChatEntity> lists;
    private ViewHolder viewHolder;
    private Context context;
    private LayoutInflater inflater;
    private ChatEntity chatEntity;
    private Activity activity;

    /*长按信息文本 弹出复制|删除*/
    private PopupWindow popupWindow;
    private TextView copyTv,deleteTv;


    public ChatAdapter(Activity activity,Context context, List<ChatEntity> lists) {
        this.activity = activity;
        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);
        initPopWindow(inflater);
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.chat_single_listview,null);
            viewHolder = new ViewHolder();
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.hisText = (TextView) convertView.findViewById(R.id.hisText);
            viewHolder.myText = (TextView) convertView.findViewById(R.id.myText);
            convertView.setTag(viewHolder);//把convertView存起来，不用每次重新获取相应实例
        }
        else
            viewHolder = (ViewHolder) convertView.getTag();

        chatEntity = lists.get(position);
        /*处理时间显示情况*/
        if (position>0) {
            Date a= DateTransform.makeString2Date(lists.get(position).getTime());
            Date b= DateTransform.makeString2Date(lists.get(position-1).getTime());
            if (DateTransform.compareInterval(a,b)>5) {
                showTime();
                viewHolder.time.setText(DateTransform.isThisYear(chatEntity.getTime()));
            }
            else
                hideTime();
        }else {
            showTime();
            viewHolder.time.setText(DateTransform.isThisYear(chatEntity.getTime()));
        }

        /*消息内容分开处理显示左侧还是右侧*/
        if (chatEntity.getName().equals(PreferenceUtil.getName(context))){
            viewHolder.myText.setText(chatEntity.getText());
            viewHolder.myText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showPopWindow(v);
                    copyTv.setOnTouchListener(new tvOnTouch(context, position));
                    deleteTv.setOnTouchListener(new tvOnTouch(context, position));
                    return false;
                }
            });
            showRight();
        }
        else {
            viewHolder.hisText.setText(chatEntity.getText());
            viewHolder.hisText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showPopWindow(v);
                    copyTv.setOnTouchListener(new tvOnTouch(context, position));
                    deleteTv.setOnTouchListener(new tvOnTouch(context, position));
                    return false;
                }
            });
            showLeft();
        }

        return convertView;
    }

    private void showTime(){
        if (viewHolder.time.getVisibility() == View.GONE)
            viewHolder.time.setVisibility(View.VISIBLE);
    }

    private void hideTime(){
        if (viewHolder.time.getVisibility() == View.VISIBLE)
            viewHolder.time.setVisibility(View.GONE);
    }

    private void showLeft(){
        if (viewHolder.hisText.getVisibility() == View.GONE)
            viewHolder.hisText.setVisibility(View.VISIBLE);
        if (viewHolder.myText.getVisibility() == View.VISIBLE)
            viewHolder.time.setVisibility(View.GONE);
    }

    private void showRight(){
        if (viewHolder.myText.getVisibility() == View.GONE)
            viewHolder.myText.setVisibility(View.VISIBLE);
        if (viewHolder.hisText.getVisibility() == View.VISIBLE)
            viewHolder.hisText.setVisibility(View.GONE);
    }

    /**
     * 初始化PopupWindow
     * @param inflater
     */
    private void initPopWindow(LayoutInflater inflater){
        View view=inflater.inflate(R.layout.pop_cpdl_layout,null);
        popupWindow = new PopupWindow(view,200,100);
        copyTv = (TextView) view.findViewById(R.id.pop_copy_tv);
        deleteTv = (TextView) view.findViewById(R.id.pop_delete_tv);
    }

    /**
     * 显示PopupWindow
     * @param v
     */
    private void showPopWindow(View v){
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(true);
        //设置此项可点击Popupwindow外区域消失，注释则不消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        //设置出现位置
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
                location[0]+v.getWidth()/2-popupWindow.getWidth()/2,
                location[1]-popupWindow.getHeight());
    }


    class tvOnTouch implements View.OnTouchListener{
        private Context mContext;
        private int mPosition;

        public tvOnTouch(Context context, int position) {
            // TODO Auto-generated method stub
            this.mContext = context;
            this.mPosition = position;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v.getId() == R.id.pop_copy_tv) {
                TextView tv = (TextView) v;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {// 按下
                    tv.setTextColor(0xff00CD66);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {// 松开
                    tv.setTextColor(0xffffffff);
                    TextManager.copyText(mContext, lists.get(mPosition).getText());
                    ToastMsg.showToast("复制成功");

                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                }
            } else {
                TextView tv = (TextView) v;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {// 按下
                    tv.setTextColor(0xff00CD66);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {// 松开
                    tv.setTextColor(0xffffffff);
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                    showActionSheet(tv);
                }
            }
            return true;
        }

        private void showActionSheet(View view){
            context.setTheme(R.style.ActionSheetStyleIOS7);
            ActionSheet menuView = new ActionSheet(activity);
            menuView.setCancelButtonTitle("取消");// before add items
            menuView.addItems("删除");
            menuView.setItemClickListener(clickListener);
            menuView.setCancelableOnTouchMenuOutside(true);
            menuView.showMenu();
        }

        private ActionSheet.MenuItemClickListener clickListener= new ActionSheet.MenuItemClickListener() {
            @Override
            public void onItemClick(int itemPosition) {
                if (itemPosition == 0) {
                    lists.remove(mPosition);
                    notifyDataSetChanged();
                    ToastMsg.showToast("删除成功");
                }
            }
        };

    }
}
