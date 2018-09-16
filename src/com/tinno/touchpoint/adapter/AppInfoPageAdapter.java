package com.tinno.touchpoint.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author lzq
 * 2015.9.22
 */
public class AppInfoPageAdapter extends PagerAdapter{
	private final static String TAG = "AppInfoPageAdapter";
	List<View> mViews = new ArrayList<View>();  

	public void setViews(List<View> views) {
		mViews = views;
		Log.d(TAG, "setViews"+mViews.size());
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		Log.d(TAG, "getCount"+mViews.size());

		if(null == mViews)
			return 0;
		else
			return mViews.size();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Log.d(TAG, "instantiateItem"+position);
		container.addView(mViews.get(position));
		return mViews.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(mViews.get(position));
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		Log.d(TAG, "isViewFromObject");
		return arg0 == arg1;
	}

}
