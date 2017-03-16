package com.sugar.myapplication.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

public class OtherAdapter extends BaseAdapter {
	private Context context;
	public List<ChannelItem> channelList;
	private TextView item_text;
	/** 是否可见 */
	boolean isVisible = true;
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
	public OtherAdapter(Context context, List<ChannelItem> channelList) {
		this.context = context;
		this.channelList = channelList;
	}

	@Override
	public int getCount() {
		return channelList == null ? 0 : channelList.size();
	}

	@Override
	public ChannelItem getItem(int position) {
		if (channelList != null && channelList.size() != 0) {
			return channelList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
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
		//iv_icon.setImageResource(icons[position]);
		if (!isVisible && (position == -1 + channelList.size())){
			item_text.setText("");
			item_text.setCompoundDrawables(null,null,null,null);
		}
		if(remove_position == position){
			item_text.setText("");
			item_text.setCompoundDrawables(null,null,null,null);
		}
		return view;
	}
	
	/** 获取频道列表 */
	public List<ChannelItem> getChannnelLst() {
		return channelList;
	}
	
	/** 添加频道列表 */
	public void addItem(ChannelItem channel) {
		channelList.add(channel);
		notifyDataSetChanged();
	}

	/** 设置删除的position */
	public void setRemove(int position) {
		remove_position = position;
		notifyDataSetChanged();
		// notifyDataSetChanged();
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
}