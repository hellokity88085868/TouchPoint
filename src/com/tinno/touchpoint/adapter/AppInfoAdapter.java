package com.tinno.touchpoint.adapter;

import java.util.ArrayList;
import java.util.List;

import com.tinno.touchpoint.R;
import com.tinno.touchpoint.model.AppInfo;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author lzq
 * 2015.9.22
 */

public class AppInfoAdapter extends BaseAdapter{

	List<AppInfo> mAppInfoList= new ArrayList<AppInfo>();
	LayoutInflater infater = null;

	 public AppInfoAdapter(List<AppInfo> mAppInfoList,Context context) {
		// TODO Auto-generated constructor stub
		 infater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 this.mAppInfoList = mAppInfoList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAppInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			view = infater.inflate(R.layout.appinfo_item, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} 
		else{
			view = convertView ;
			holder = (ViewHolder) convertView.getTag() ;
		}
		if(!TextUtils.isEmpty(mAppInfoList.get(position).getAppLabel()) && null != mAppInfoList.get(position).getAppIcon())
		{
			holder.img_icon.setImageDrawable(mAppInfoList.get(position).getAppIcon());
			holder.txt_name.setText(mAppInfoList.get(position).getAppLabel());
		}
		return view;
	}
	
	class ViewHolder  {  
		ImageView img_icon;  
		TextView txt_name;  
		
		public ViewHolder(View view) {
			this.img_icon = (ImageView) view.findViewById(R.id.img_icon);
			this.txt_name = (TextView) view.findViewById(R.id.txt_name);
		}
	}  
}
