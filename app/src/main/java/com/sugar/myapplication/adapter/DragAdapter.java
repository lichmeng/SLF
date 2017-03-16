package com.sugar.myapplication.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.bean.ChannelItem;

import java.util.List;

public class DragAdapter extends BaseAdapter {
	/** TAG*/
	private final static String TAG = "DragAdapter";
	/** 是否显示底部的ITEM */
	private boolean isItemShow = false;
	private Context context;
	/** 控制的postion */
	private int holdPosition;
	/** 是否改变 */
	private boolean isChanged = false;
	/** 是否可见 */
	boolean isVisible = true;
	/** 可以拖动的列表（即用户选择的频道列表） */
	public List<ChannelItem> channelList;
	/** TextView 频道内容 */
	private TextView item_text;
	/** 要删除的position */
	public int remove_position = -1;
	public int[] icons = {
			R.drawable.my_baby,
			R.drawable.my_coll,
			R.drawable.my_care,
			R.drawable.my_foot,
			R.drawable.my_card,
			R.drawable.my_child,
			R.drawable.my_vip,
			R.drawable.my_xiaomi,
			R.drawable.my_money,
			R.drawable.my_phone,
			R.drawable.my_guoguo,
			R.drawable.my_pifu,
			R.drawable.my_friends,
			R.drawable.my_share,
			R.drawable.my_question,
			R.drawable.my_comment,
			R.drawable.my_computer,
			R.drawable.my_email};
	private ImageView iv_icon;

	public DragAdapter(Context context, List<ChannelItem> channelList) {
		this.context = context;
		this.channelList = channelList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return channelList == null ? 0 : channelList.size();
	}

	@Override
	public ChannelItem getItem(int position) {
		// TODO Auto-generated method stub
		if (channelList != null && channelList.size() != 0) {
			return channelList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.subscribe_category_item, null);
		item_text = (TextView) view.findViewById(R.id.text_item);
		//iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
		ChannelItem channel = getItem(position);
		item_text.setText(channel.getName());
		Drawable topDrawable = App.mContext.getResources().getDrawable(icons[channel.getId() - 1]);
		topDrawable.setBounds(0,0,topDrawable.getMinimumWidth(),topDrawable.getMinimumHeight());
		item_text.setCompoundDrawables(null,topDrawable,null,null);
		if ((position == 0) || (position == 1)){
//			item_text.setTextColor(context.getResources().getColor(R.color.black));
			item_text.setEnabled(true);
			//iv_icon.setEnabled(false);
			item_text.setCompoundDrawables(null,topDrawable,null,null);
		}
		if (isChanged && (position == holdPosition) && !isItemShow) {
			item_text.setText("");
			item_text.setCompoundDrawables(null,null,null,null);
			item_text.setSelected(true);
			item_text.setEnabled(true);
			//iv_icon.setEnabled(true);
			isChanged = false;
		}
		if (!isVisible && (position == -1 + channelList.size())) {
			item_text.setText("");
			item_text.setCompoundDrawables(null,null,null,null);
			item_text.setSelected(true);
			item_text.setEnabled(true);
			//iv_icon.setEnabled(true);
		}
		if(remove_position == position){
			item_text.setText("");
			item_text.setCompoundDrawables(null,null,null,null);
		}
		return view;
	}

	/** 添加频道列表 */
	public void addItem(ChannelItem channel) {
		channelList.add(channel);
		notifyDataSetChanged();
	}

	/** 拖动变更频道排序 */
	public void exchange(int dragPostion, int dropPostion) {
		holdPosition = dropPostion;
		ChannelItem dragItem = getItem(dragPostion);
		Log.d(TAG, "startPostion=" + dragPostion + ";endPosition=" + dropPostion);
		if (dragPostion < dropPostion) {
			channelList.add(dropPostion + 1, dragItem);
			channelList.remove(dragPostion);
		} else {
			channelList.add(dropPostion, dragItem);
			channelList.remove(dragPostion + 1);
		}
		isChanged = true;
		notifyDataSetChanged();
	}
	
	/** 获取频道列表 */
	public List<ChannelItem> getChannnelLst() {
		return channelList;
	}

	/** 设置删除的position */
	public void setRemove(int position) {
		remove_position = position;
		notifyDataSetChanged();
	}

	/** 删除频道列表 */
	public void remove() {
		channelList.remove(remove_position);
		remove_position = -1;
		notifyDataSetChanged();
	}
	
	/** 设置频道列表 */
	public void setListDate(List<ChannelItem> list) {
		channelList = list;
	}
	
	/** 获取是否可见 */
	public boolean isVisible() {
		return isVisible;
	}
	
	/** 设置是否可见 */
	public void setVisible(boolean visible) {
		isVisible = visible;
	}
	/** 显示放下的ITEM */
	public void setShowDropItem(boolean show) {
		isItemShow = show;
	}
}