package com.tinno.touchpoint.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.tinno.touchpoint.R;
import com.tinno.touchpoint.adapter.AppInfoAdapter;
import com.tinno.touchpoint.adapter.AppInfoPageAdapter;
import com.tinno.touchpoint.interfaces.ShowAppDialogCallBack;
import com.tinno.touchpoint.model.AppInfo;
import com.tinno.touchpoint.util.AppInfoUtil;

/**
 * 
 * @author lzq
 * 2015.9.23
 */
public class ShowAppInfoControl {

	private final static String TAG = "ShowAppInfoControl";
	private final static int APP_ROW = 4;
	private final static int APP_LINE = 4;
	
	private Context mContext = null;
	Dialog mAppInfoDialog = null;
	private ShowAppDialogCallBack mShowAppDialogCallBack = null;
	
	private List<AppInfo> mAppInfoList= new ArrayList<AppInfo>();
	
	public ShowAppInfoControl(Context context)
	{
		this.mContext = context;
	}
	
	public void showAppInfoDialog()
	{
		LayoutInflater layoutInflater = LayoutInflater.from(mContext); 
        View mAppInfoView = layoutInflater.inflate(R.layout.appinfo_dialog, null);
        
        initAppInfoData();
        initAppInfoView(mAppInfoView);
        
        mAppInfoDialog = new AlertDialog.Builder(mContext). 
                setTitle("选择程序"). 
                setView(mAppInfoView). 
                setNegativeButton("取消", new DialogInterface.OnClickListener() { 
 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        // TODO Auto-generated method stub  
                    } 
                }).create();
        mAppInfoDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        mAppInfoDialog.show(); 
	}
	
	private void initAppInfoData()
	{
		mAppInfoList.clear();
		mAppInfoList = AppInfoUtil.getAllAppInfo(mContext);
	}
	
    private void initAppInfoView(View v)
    {
    	boolean isRemainder = false;
    	List<View> list_Views = new ArrayList<View>();  
    	int mPages = mAppInfoList.size()/(APP_ROW * APP_LINE);
    	if( mAppInfoList.size()%(APP_ROW * APP_LINE) != 0)
    	{
    		isRemainder = true;
    		mPages = mPages + 1;
    	}
    	else
    	{
    		isRemainder = false;
    		mPages = mPages;
    	}
    	
    	ViewPager mAppInfoPage = (ViewPager) v.findViewById(R.id.appinfo_page);
    	AppInfoPageAdapter mAppInfoPageAdapter = new AppInfoPageAdapter();
    	for (int i = 0; i < mPages; i++) {
    		if(i == (mPages-1))
    			list_Views.add(getViewPagerItem(i,isRemainder,true));
    		else
    			list_Views.add(getViewPagerItem(i,isRemainder,false));  
		}
    	mAppInfoPageAdapter.setViews(list_Views);
    	mAppInfoPage.setAdapter(mAppInfoPageAdapter);
    }
    
    private View getViewPagerItem(final int index,boolean isRemainder,boolean isLast)
    {
    	int page = index;
    	int page_statr_num = index*APP_LINE*APP_ROW;
    	int page_end_num = (index+1)*APP_LINE*APP_ROW;
    	
    	if(isLast && isRemainder)
    	{
    		page_end_num =	mAppInfoList.size();
    	}
    	
    	List<AppInfo> mAppInfo= new ArrayList<AppInfo>();
    	for(int i=page_statr_num; i<page_end_num; i++)
    	{
    		mAppInfo.add(mAppInfoList.get(i));
    	}
    	LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
    	View mGridViewLayout = inflater.inflate(R.layout.appinfo_gridview, null);
    	
    	GridView mGridView = (GridView) mGridViewLayout.findViewById(R.id.appinfo_gridview);
    	mGridView.setNumColumns(APP_LINE);    	
    	AppInfoAdapter mAppInfoAdapter = new AppInfoAdapter(mAppInfo,mContext);
    	mGridView.setAdapter(mAppInfoAdapter);
    	mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				AppInfo mCurrentAppInfo = new AppInfo();
				mCurrentAppInfo = mAppInfoList.get(position+(index)*APP_LINE*APP_ROW);
				reMoveDialog();
				mShowAppDialogCallBack.notifyShortCutChanged(mCurrentAppInfo);
			}
		});
    	
    	return mGridViewLayout;
    }
	
	private void reMoveDialog()
	{
		if(null != mAppInfoDialog)
		{
			mAppInfoDialog.dismiss();
		}
	}
	
	public void setShowAppDialogCallBack(ShowAppDialogCallBack mShowAppDialogCallBack)
	{
		this.mShowAppDialogCallBack = mShowAppDialogCallBack;
	}
}
