package com.jinwang.jianwutong.chat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinwang.jianwutong.util.AreaUtil;
import com.jinwang.jianwutong.R;
import com.jinwang.jianwutong.config.SystemConfig;
import com.jinwang.jianwutong.chat.entity.MsgEntity;
import com.jinwangmobile.ui.base.view.CircleImageView;

import java.io.File;
import java.util.List;

/**
 * Created by Chenss on 2015/10/22.
 */
public class MsgAdapter extends BaseAdapter {
    class ViewHolder{
        CircleImageView head;
        TextView unread,name,latestMsg,latestTime,branch;
        GradientDrawable myGrad;
    }

    private Context context;
    private List<MsgEntity> lists;
    private LayoutInflater inflater;

    private ViewHolder viewHolder;

    private int color;
    private MsgEntity msgEntity;

    private float x, ux;
   // private PopupWindow popupWindow;
    //private Button del;

    public MsgAdapter(Context context, List<MsgEntity> lists) {
        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);
        //initPopWindow(inflater);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.layout_msg,null);
            viewHolder = new ViewHolder();
            viewHolder.head = (CircleImageView) convertView.findViewById(R.id.head);
            viewHolder.unread = (TextView) convertView.findViewById(R.id.unread);
            viewHolder.latestMsg = (TextView) convertView.findViewById(R.id.latestMsg);
            viewHolder.latestTime = (TextView) convertView.findViewById(R.id.latestTime);
            viewHolder.branch = (TextView) convertView.findViewById(R.id.branch);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.myGrad = (GradientDrawable) viewHolder.branch.getBackground();
            convertView.setTag(viewHolder);//把convertView存起来，不用每次重新获取相应实例
        }
        else
            viewHolder = (ViewHolder) convertView.getTag();

        msgEntity=lists.get(position);
        String path= SystemConfig.PATH_HEAD+msgEntity.getName()+SystemConfig.HEAD_TYPE;
        File file=new File(path);
        if (file.exists()){
            Bitmap bm= BitmapFactory.decodeFile(path);
            viewHolder.head.setImageDrawable(new BitmapDrawable(bm));
        }
        if (msgEntity.getUnread()!=0){
            viewHolder.unread.setText(String.valueOf(msgEntity.getUnread()));
            viewHolder.unread.setVisibility(View.VISIBLE);
        }
        viewHolder.name.setText(msgEntity.getName());
        viewHolder.latestMsg.setText(msgEntity.getLatestMsg());
        viewHolder.latestTime.setText(msgEntity.getLatestTime());

        color= getColor(msgEntity.getBranch());
        viewHolder.myGrad.setColor(color);
        viewHolder.branch.setText(msgEntity.getBranch());

        //convertView.setOnTouchListener(onTouchListener);
        return convertView;
    }
/*
    private View.OnTouchListener onTouchListener= new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //当按下时处理
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                //设置背景为选中状态
                v.setBackgroundResource(R.drawable.msg_listitem_pressed);
                // 获取按下时的x轴坐标
                x = event.getX();
            }else if (event.getAction() == MotionEvent.ACTION_UP){
                //松开处理
                v.setBackgroundResource(R.drawable.msg_listitem_nomal);
                // 获取松开时的x坐标
                ux = event.getX();
                // 按下和松开绝对值差当大于5时显示删除按钮，否则不显示
                if (Math.abs(x - ux) > 5) {
                    showPopWindow(v);
                }
                else{
                    //处理选中事件！！ 即进入对应界面
                }
            }else if (event.getAction() == MotionEvent.ACTION_MOVE) {// 当滑动时背景为选中状态
                v.setBackgroundResource(R.drawable.msg_listitem_pressed);

            } else {// 其他模式
                // 设置背景为未选中正常状态
                v.setBackgroundResource(R.drawable.msg_listitem_nomal);
            }

            return true;
        }
    };


    *//**
     * 初始化PopupWindow
     * @param inflater
     *//*
    private void initPopWindow(LayoutInflater inflater){
        View view=inflater.inflate(R.layout.pop_slide_del_layout,null);
        popupWindow = new PopupWindow(view, AbsListView.LayoutParams.WRAP_CONTENT,
                android.view.WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 要设置背景
        popupWindow.setAnimationStyle(R.style.AnimationPopDel);// 动画
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        //popupWindow = new PopupWindow(view,200,100);
        del = (Button) view.findViewById(R.id.btn_del_pop);
    }

    *//**
     * 显示PopupWindow
     * @param v
     *//*
    private void showPopWindow(View v){
        //popupWindow.setFocusable(false);
       // popupWindow.setOutsideTouchable(true);//设置此项可点击Popupwindow外区域消失，注释则不消失
        //popupWindow.setBackgroundDrawable(new BitmapDrawable());

        //popupWindow.setAnimationStyle(R.style.AnimationPopDel);

        //设置出现位置
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
                location[0] + v.getWidth() / 2 - popupWindow.getWidth() / 2,
                location[1] - popupWindow.getHeight());
    }*/

    private int getColor(String branch) {
        int color=0;
        switch (branch){
            case AreaUtil.SJ:
                color=context.getResources().getColor(R.color.sj);
                break;
            case AreaUtil.JD:
            case AreaUtil.YZ:
                color=context.getResources().getColor(R.color.jd_yz);
                break;
            case AreaUtil.HS:
            case AreaUtil.JB:
                color=context.getResources().getColor(R.color.hs_jb);
                break;
            case AreaUtil.ZH:
                color=context.getResources().getColor(R.color.zh);
                break;
            case AreaUtil.BL:
                color=context.getResources().getColor(R.color.bl);
                break;
            case AreaUtil.FH:
                color=context.getResources().getColor(R.color.fh);
                break;
            case AreaUtil.YY:
                color=context.getResources().getColor(R.color.yy);
                break;
            default:
                color=context.getResources().getColor(R.color.yy);
                break;
        }
        return color;
    }

}