package com.tinno.touchpoint.service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.tinno.touchpoint.R;
import com.tinno.touchpoint.activity.FloatApplication;
import com.tinno.touchpoint.activity.ManagerActivity;
import com.tinno.touchpoint.adapter.AppInfoAdapter;
import com.tinno.touchpoint.adapter.AppInfoPageAdapter;
import com.tinno.touchpoint.control.ShowAppInfoControl;
import com.tinno.touchpoint.control.ShowShortCutControl;
import com.tinno.touchpoint.interfaces.ShowAppDialogCallBack;
import com.tinno.touchpoint.interfaces.ShowShortCutDialogCallBack;
import com.tinno.touchpoint.model.AppInfo;
import com.tinno.touchpoint.model.ShortCutInfo;
import com.tinno.touchpoint.recevier.MyAdminReceiver;
import com.tinno.touchpoint.util.AppInfoUtil;
import com.tinno.touchpoint.util.DensityUtil;
import com.tinno.touchpoint.util.ScreenShotUtil;
import com.tinno.touchpoint.util.SharePreference;
import com.tinno.touchpoint.view.AppInfoDialog;
import com.tinno.touchpoint.view.FloatPointView;
import com.tinno.touchpoint.view.SemiCircleLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author lzq
 *	2015.9.17
 */
public class FloatSer extends Service implements OnClickListener,OnTouchListener,OnLongClickListener{

	private final static String TAG = "FloatSer";
	
	public final static int MENU_WIDTH = 110;
	public final static int MENU_HEIGHT = 170;
	
	public final static int FLOATVIEW_STATU = 1000;
	public final static int SEMICIRCLE_STATU = 1001;
	public final static int SHORTCUT_STATU = 1002;
	
	private final IBinder mBinder = new LocalBinder();
	private final Object mScreenshotLock = new Object();
	private ServiceConnection mScreenshotConnection = null;
	
	ShowAppInfoControl mShowAppInfoControl = null;
	ShowShortCutControl mShowShortCutControl = null;
	
	private WindowManager windowManager = null; 
	private WindowManager.LayoutParams windowManagerParams = null; 
	public FloatPointView mFloatPointView = null;
	private Bitmap bitmap = null;
	private SemiCircleLayout mSemiCircleMenu = null;
	
	private int mCurrent_x = 0;
	private int mCurrent_y = 0;
	
	private int direction = 1;
	private int mCurrentMenuPosition = 0;
	private boolean isDirectionChanged = false;
	private boolean isShowTPScreen = true;
	
	public int mScreenStatu = FLOATVIEW_STATU;
	
	private int[] mMemuBg_n = new int[]{R.drawable.close_n,R.drawable.lock_n,R.drawable.shortcut_n,
			R.drawable.page_n,R.drawable.drawpicture_n,R.drawable.home_n};
	private int[] mMemuBg_p = new int[]{R.drawable.close_p,R.drawable.lock_p,R.drawable.shortcut_p,
			R.drawable.page_p,R.drawable.drawpicture_p,R.drawable.home_p};
	
	private Handler mHandler = new Handler();
	  private Runnable mRunnable = new Runnable() {  
	        public void run() {  
	        	ComponentName cn = new ComponentName("com.android.systemui",
						"com.android.systemui.screenshot.TakeScreenshotService");	        	
				Intent intent = new Intent();
				intent.setComponent(cn);
				takeScreenshot(intent);
	        }  
	    };  
	public class LocalBinder extends Binder{
		public FloatSer getService() {
			return FloatSer.this;
		}
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d("LZQGOODBOYS", "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d("LZQGOODBOYS", "onBind");
		return mBinder;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.d("LZQGOODBOYS", "onCreate");
		initView();
		super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		Log.d("LZQGOODBOYS", "onStart");
		super.onStart(intent, startId);
	}
	 
	private void initView() { 
		
		initFloatPointView();
		initWindowManagerView();
	}
	
	private void initFloatPointView()
	{
		mFloatPointView = new FloatPointView(getApplicationContext()); 
		mFloatPointView.setImageResource(R.drawable.touch_point_p); 
		mFloatPointView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
//                  mFloatPointView.setImageResource(R.drawable.ic_launcher);
					break;
				case MotionEvent.ACTION_UP:
					mFloatPointView.setImageResource(R.drawable.touch_point_p);
					break;
				default:
					break;
				}
				return false;
			}
		});
		mFloatPointView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(null != mFloatPointView)
				{
					if(mFloatPointView.mAlignSide)
					{
						reMovePointView();
						setMenusStatu();
						mScreenStatu = SEMICIRCLE_STATU;
					}
				}
			}
		});
	}
	
	private void initWindowManagerView()
	{
		windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); 
		windowManagerParams = ((FloatApplication) getApplication()).getWindowParams(); 
		windowManagerParams.type = LayoutParams.TYPE_PHONE; 
		windowManagerParams.format = PixelFormat.RGBA_8888; 
		windowManagerParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL 
				| LayoutParams.FLAG_NOT_FOCUSABLE; 
		windowManagerParams.gravity = Gravity.LEFT | Gravity.TOP; 
		windowManagerParams.x = 0; 
		windowManagerParams.y = 0; 
		windowManagerParams.width = LayoutParams.WRAP_CONTENT; 
		windowManagerParams.height = LayoutParams.WRAP_CONTENT;
		windowManager.addView(mFloatPointView, windowManagerParams); 
	}
	
    public void setMenusStatu()
    {	
		windowManagerParams.width = DensityUtil.dip2px(getApplicationContext(), MENU_WIDTH);
        windowManagerParams.height =DensityUtil.dip2px(getApplicationContext(), MENU_HEIGHT);
        this.direction = getDirection();
        if(1 == direction)
        {
        	setManagerParamsPosition(0,(int) mFloatPointView.y - DensityUtil.dip2px(getApplicationContext(), MENU_HEIGHT)/2);
        	setCurrentXY(0,(int) mFloatPointView.y);
        }
        else
        {
        	setManagerParamsPosition(mFloatPointView.width,(int) mFloatPointView.y - DensityUtil.dip2px(getApplicationContext(), MENU_HEIGHT)/2);
        	setCurrentXY(mFloatPointView.width,(int) mFloatPointView.y);
        }
        
    	windowManager.addView(setMenu(direction), windowManagerParams);
    }
	
    // 1 is left
	@SuppressLint("NewApi")
	private SemiCircleLayout setMenu(int direction)
	{
		if(null == mSemiCircleMenu)
		{
			if(null == mSemiCircleMenu)
			{
				mSemiCircleMenu = new SemiCircleLayout(getApplicationContext(),direction);
			} 
		}
		mSemiCircleMenu.mDirection = direction;
		mSemiCircleMenu.invalidate();
		
		mSemiCircleMenu.removeAllViews();
		int[] btn_ids = new int[]{0,1,2,3,4,5};
		for(int btn_id : btn_ids)
		{
			ImageButton btn = new ImageButton(getApplicationContext());
			btn.setOnClickListener(this);
			btn.setOnTouchListener(this);
			btn.setId(btn_id);
			btn.setBackgroundResource(mMemuBg_n[btn_id]);
			mSemiCircleMenu.addView(btn);
		}
		return mSemiCircleMenu;
	}
	
	@SuppressLint("NewApi")
	public void reMovePointView()
	{
		if(mFloatPointView.isAttachedToWindow())
		{
			mFloatPointView.removeRunable();
			windowManager.removeViewImmediate(mFloatPointView);
		}
	}
	
	@SuppressLint("NewApi")
	public void reMoveSemiCircleView()
	{
		if(mSemiCircleMenu.isAttachedToWindow())
		{
			if(isShowTPScreen)
			{
				windowManager.removeViewImmediate(mSemiCircleMenu);
				isShowTPScreen = false;
			}
		}
	}
	
	private void setCurrentXY(int x,int y)
	{
		this.mCurrent_x = x;
		this.mCurrent_y = y;
	}
	
	private void setManagerParamsPosition(int x, int y)
	{
		windowManagerParams.x = x;
		windowManagerParams.y = y;
	}
	
	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		mCurrentMenuPosition = v.getId();
		if(mCurrentMenuPosition > 6 && mCurrentMenuPosition < 12)
		{
			showDeleShortCutDialog(mCurrentMenuPosition);
		}
		return true;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(v.getId() == 6)
			{
				if(1 == direction)
				{
					v.setBackgroundResource(R.drawable.right_arrows_p);
				}
				else
				{
					v.setBackgroundResource(R.drawable.left_arrows_p);
				}
			} else if(v.getId() > 6){
				v.setBackgroundResource(R.drawable.addapp_p);
			} else{
				v.setBackgroundResource(mMemuBg_p[v.getId()]);
			}
			break;
		case MotionEvent.ACTION_UP:
			if(v.getId() == 6)
			{
				if(1 == direction)
				{
					v.setBackgroundResource(R.drawable.right_arrows_n);
				}
				else
				{
					v.setBackgroundResource(R.drawable.left_arrows_n);
				}
			} else if(v.getId() > 6){
				v.setBackgroundResource(R.drawable.addapp_n);
			} else{
				v.setBackgroundResource(mMemuBg_n[v.getId()]);
			}
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		mCurrentMenuPosition = v.getId();
		switch (v.getId()) {
		case 0:
			goBackPointStatu();
			break;
		case 1:
			lockScreen();
			break;
		case 2:
			mScreenStatu = SHORTCUT_STATU;
			ShowShortCut(null);
			break;
		case 3:
            
			break;
		case 4:
//			doScreenShot();
			reMoveSemiCircleView();
			mHandler.post(mRunnable);
			break;
		case 5:
			goHomeLaunch();
			break;
			
		case 6:
			goBackMenuStatu();
			break;
		case 7:
			goShortCut(mCurrentMenuPosition);
			break;
		case 8:
			goShortCut(mCurrentMenuPosition);
			break;
		case 9:
			goShortCut(mCurrentMenuPosition);
			break;
		case 10:
			goShortCut(mCurrentMenuPosition);
			break;
		case 11:
			goShortCut(mCurrentMenuPosition);
			break;
		default:
			break;
		}
	}
	
	private void goBackPointStatu()
	{
		if(null != mFloatPointView )
		{
			mScreenStatu = FLOATVIEW_STATU;
			mFloatPointView.startRunnable(); 
			windowManagerParams.width = LayoutParams.WRAP_CONTENT; 
			windowManagerParams.height = LayoutParams.WRAP_CONTENT;
			setManagerParamsPosition(mCurrent_x, mCurrent_y - mFloatPointView.getHeight()/2);
			windowManager.removeViewImmediate(mSemiCircleMenu);
			windowManager.addView(mFloatPointView, windowManagerParams);
		}
	}
	
	private void lockScreen()
	{
		DevicePolicyManager manager;
		boolean adminActive;
		Intent intent;
		
		manager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE); 	
		ComponentName componentName = new ComponentName(this, MyAdminReceiver.class);		
		adminActive = manager.isAdminActive(componentName); 								
		if (adminActive) {																	
			manager.lockNow();	
		} else {
			intent = new Intent(getApplicationContext(), ManagerActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
			startActivity(intent);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void doScreenShot()
	{
		setFloatInVisiable();
		ScreenShotUtil mScreenShotUtil = new ScreenShotUtil(getApplicationContext());
		if(mScreenShotUtil.ScreenShot())
			Toast.makeText(getApplicationContext(), "Screen Shot Success!!!", Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(getApplicationContext(), "Screen Shot Failed!!!", Toast.LENGTH_SHORT).show();
		setFloatVisibale();
	}
	
	private void goHomeLaunch()
	{
		Intent intent = new Intent(Intent.ACTION_MAIN);  
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        intent.addCategory(Intent.CATEGORY_HOME);  
        startActivity(intent);  
	}
	
	public void goBackMenuStatu()
	{
		mScreenStatu = SEMICIRCLE_STATU;
		setMenu(direction);
		windowManager.updateViewLayout(mSemiCircleMenu, windowManagerParams);
	}
	private void goShortCut(int position)
	{
		ShortCutInfo mShortCutInfo = new ShortCutInfo();
		mShortCutInfo = getShortCut(position);
		if(isHaveShortCut(mShortCutInfo))
		{
			startApplication(mShortCutInfo.pakname);
			ShowShortCut(null);
		}
		else
		{
			showAppInfo();
		}
	}
	
	private void setFloatInVisiable()
	{
		if(null != mSemiCircleMenu)
		{
			mSemiCircleMenu.setVisibility(View.GONE);
			windowManager.updateViewLayout(mSemiCircleMenu, windowManagerParams);
		}
	}
	
	private void setFloatVisibale()
	{
		if(null != mSemiCircleMenu)
		{
			mSemiCircleMenu.setVisibility(View.VISIBLE);
			windowManager.updateViewLayout(mSemiCircleMenu, windowManagerParams);
		}
	}
	
	private void reMoveMenuView()
	{
		mSemiCircleMenu.removeAllViews();
		windowManager.removeViewImmediate(mSemiCircleMenu);
	}
	
	private ShortCutInfo getShortCut(int position)
	{
		List<ShortCutInfo> mShortCutList = getShortCutListData(null);
		for (int i = 0; i < mShortCutList.size(); i++) {
			if(mShortCutList.get(i).position == position)
			{
				return mShortCutList.get(i);
			}
		}
		return null;
	}
	private boolean isHaveShortCut(ShortCutInfo mShortCutInfo)
	{
		return mShortCutInfo.isExist;
	}
	
	private void showDeleShortCutDialog(int position)
	{
		List<AppInfo> mAppInfoList = new ArrayList<AppInfo>();
		ShortCutInfo mShortCutInfo = new ShortCutInfo();
		mAppInfoList = AppInfoUtil.getAllAppInfo(this);
		mShortCutInfo = getShortCut(position);
		if(mShowShortCutControl == null)
		{
			mShowShortCutControl = new ShowShortCutControl(this);
			mShowShortCutControl.setShowShortCutDialogCallBack(mShowShortCutDialogCallBack);
		}
		mShowShortCutControl.setDeletShortCutDialog(AppInfoUtil.getPaknameIcon(mAppInfoList, mShortCutInfo.getPakname()));
	}
	
	private ShowShortCutDialogCallBack mShowShortCutDialogCallBack = new ShowShortCutDialogCallBack() {
		
		@Override
		public void deleteShortCut() {
			// TODO Auto-generated method stub
			ShowShortCut(getNullShortCut(mCurrentMenuPosition));
		}
	};
	
	private List<ShortCutInfo> getShortCutListData(ShortCutInfo mShortCut)
	{
		List<ShortCutInfo> mShortCutList = new ArrayList<ShortCutInfo>();
		SharedPreferences mPrefs;
		mPrefs = getSharedPreferences(SharePreference.SHORTCUT_SP_NAME, MODE_PRIVATE);
		if(mPrefs.getAll().isEmpty())
		{
			SharePreference.saveShortCutList(this, SharePreference.getDefaultShortCutList());
		}
		mShortCutList = SharePreference.getShortCutList(mPrefs);
		
		if(null != mShortCut)
		{
			for (int i = 0; i < mShortCutList.size(); i++) {

				if(mShortCutList.get(i).position == mShortCut.position)
				{
					mShortCutList.remove(i);
					mShortCutList.add(mShortCut);
					SharePreference.saveShortCutList(this, mShortCutList);
					break;
				}
			}
		}
		
		return mShortCutList;
	}
	
	@SuppressLint("NewApi")
	private void ShowShortCut(ShortCutInfo mShortCut)
	{
		List<ShortCutInfo> mShortCutList = new ArrayList<ShortCutInfo>();
		List<AppInfo> mAppInfoList = new ArrayList<AppInfo>();
		mShortCutList = getShortCutListData(mShortCut);
		mAppInfoList = AppInfoUtil.getAllAppInfo(this);

		mSemiCircleMenu.removeAllViews();
		int[] btn_ids = new int[]{6,7,8,9,10,11};
		int[] shortCutBg_n = new int[6];
		for(int i=0;i<6;i++)
		{
			ImageButton btn = new ImageButton(getApplicationContext());
			
			LinearLayout.LayoutParams mBtnPara = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			mBtnPara.height = DensityUtil.dip2px(getApplicationContext(), 30);
			mBtnPara.width = DensityUtil.dip2px(getApplicationContext(), 30);
			if(i == 0)
			{
				if(1 == direction)
				{
					btn.setBackgroundResource(R.drawable.right_arrows_n);
				}
				else
				{
					btn.setBackgroundResource(R.drawable.left_arrows_n);
				}
			}
			else
			{
				for(int j = 0;j<mShortCutList.size();j++)
				{
					if(mShortCutList.get(j).position == btn_ids[i] && mShortCutList.get(j).isExist)
					{
						btn.setBackgroundDrawable(AppInfoUtil.getPaknameIcon(mAppInfoList, mShortCutList.get(j).getPakname()));
						btn.setOnLongClickListener(this);
						break;
					}
					else
					{
						if(j == (mShortCutList.size()-1))
						{
							btn.setBackgroundResource(R.drawable.addapp_n);
							btn.setOnTouchListener(this);
						}
					}
				}
			}
			btn.setLayoutParams(mBtnPara);
			btn.setId(btn_ids[i]);
			btn.setOnClickListener(this);
			mSemiCircleMenu.addView(btn);
		}
		if(mSemiCircleMenu.isAttachedToWindow())
		{
			windowManager.updateViewLayout(mSemiCircleMenu, windowManagerParams);
		}
		else
		{
			windowManager.addView(mSemiCircleMenu, windowManagerParams);
		}
	}
	
	@SuppressLint("NewApi")
	private synchronized int getDirection()
	{
		int mDirection = 0;
		int formDirection = this.direction;
		
		if((int) windowManagerParams.x < mFloatPointView.width/2 )
		{
			mDirection = 1;
		}
		else
		{
			mDirection = 0;
		}
		
		if(formDirection != mDirection)
		{
			isDirectionChanged = true;
		}
		else 
		{
			isDirectionChanged = false;
		}
		
//		Log.d("LZQISGOODBOYS", "(int) windowManagerParams.x:"+(int) windowManagerParams.x);
//		Log.d("LZQISGOODBOYS", "mFloatPointView.width/2:"+mFloatPointView.width/2);
//		Log.d("LZQISGOODBOYS", "mDirection:"+mDirection);
		return mDirection;
	}
	
	
	private void showAppInfo()
	{
		if(null == mShowAppInfoControl)
		{
			mShowAppInfoControl = new ShowAppInfoControl(this);
			mShowAppInfoControl.setShowAppDialogCallBack(mShowAppDialogCallBack);
		}
		mShowAppInfoControl.showAppInfoDialog();
	}
	
	private ShowAppDialogCallBack mShowAppDialogCallBack = new ShowAppDialogCallBack() {
		
		@Override
		public void notifyShortCutChanged(AppInfo mCurrentAppInfo) {
			// TODO Auto-generated method stub
			ShowShortCut(getShortCut(mCurrentAppInfo));
		}
	};
	
	private ShortCutInfo getShortCut(AppInfo mCurrentAppInfo)
	{
		ShortCutInfo mShortCut = new ShortCutInfo();
		mShortCut.isExist = true;
		mShortCut.position = mCurrentMenuPosition;
		mShortCut.appLabel = mCurrentAppInfo.getAppLabel();
		mShortCut.pakname = mCurrentAppInfo.getPkgName();
		return mShortCut;
	}
	
	private ShortCutInfo getNullShortCut(int position)
	{
		ShortCutInfo mShortCut = new ShortCutInfo();
		mShortCut.isExist = false;
		mShortCut.position = position;
		mShortCut.appLabel = null;
		mShortCut.pakname = null;
		return mShortCut;
	}
	
	private void startApplication(String pakname)
	{
		String mAppMainClassName = AppInfoUtil.getAppMainClassName(pakname, this);
		if(!TextUtils.isEmpty(mAppMainClassName))
		{
			ComponentName com = new ComponentName(pakname, mAppMainClassName);
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
			intent.setComponent(com);
			startActivity(intent);
		}
		else
		{
			Toast.makeText(this, "sorry,is error", Toast.LENGTH_SHORT);
		}
	}
	
	private void takeScreenshot(Intent intent) {
		ServiceConnection conn = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				synchronized (mScreenshotLock) {
					if (mScreenshotConnection != this) {
						return;
					}
					Messenger messenger = new Messenger(service);
					Message msg = Message.obtain(null, 1);
					final ServiceConnection myConn = this;
					Handler h = new Handler(mHandler.getLooper()) {
						@Override
						public void handleMessage(Message msg) {
							synchronized (mScreenshotLock) {
								if (mScreenshotConnection == myConn) {
									getApplicationContext().unbindService(mScreenshotConnection);
									mScreenshotConnection = null;
									mHandler.removeCallbacks(mScreenshotTimeout);
									roateSemicirleView();
								}
							}
						}
				};
					msg.replyTo = new Messenger(h);
					msg.arg1 = msg.arg2 = 0;
					// if (mStatusBar != null && mStatusBar.isVisibleLw())
					msg.arg1 = 1;
					// if (mNavigationBar != null &&
					// mNavigationBar.isVisibleLw())
					msg.arg2 = 1;
					try {
						messenger.send(msg);
						Log.d(TAG, "messenger.send(msg)");
					} catch (RemoteException e) {
				}
				}
			}
			@Override
			public void onServiceDisconnected(ComponentName name) {
			}
		};
		try {
			if (getApplicationContext().bindService(intent, conn, Context.BIND_AUTO_CREATE)) {
				mScreenshotConnection = conn;
				mHandler.postDelayed(mScreenshotTimeout, 1000);
			}
		} catch (Exception e) {
			mScreenshotConnection = null;
		}
	}	
	
    private final Runnable mScreenshotTimeout = new Runnable() {
		@Override
		public void run() {
			synchronized (mScreenshotLock) {
				if (mScreenshotConnection != null) {
					getApplicationContext().unbindService(mScreenshotConnection);
					mScreenshotConnection = null;
					roateSemicirleView();
				}
			}
		}
	};
	 
	
	//Roatae landscape Portrait
	public void notifyChangedXY()
	{
		windowManagerParams.width = DensityUtil.dip2px(getApplicationContext(), MENU_WIDTH);
        windowManagerParams.height =DensityUtil.dip2px(getApplicationContext(), MENU_HEIGHT);
        this.direction = getDirection();
        if(1 == direction)
        {
        	setManagerParamsPosition(0,(int) mFloatPointView.y - DensityUtil.dip2px(getApplicationContext(), MENU_HEIGHT)/2);
        	setCurrentXY(0,(int) mFloatPointView.y);
        }
        else
        {
        	setManagerParamsPosition(mFloatPointView.width,(int) mFloatPointView.y - DensityUtil.dip2px(getApplicationContext(), MENU_HEIGHT)/2);
        	setCurrentXY(mFloatPointView.width,(int) mFloatPointView.y);
        }
	}
	
	@SuppressLint("NewApi")
	public void roateFloatView()
	{
		if(null != mFloatPointView)
		{
			mScreenStatu = FLOATVIEW_STATU;
			windowManagerParams.width = LayoutParams.WRAP_CONTENT; 
			windowManagerParams.height = LayoutParams.WRAP_CONTENT;
			mFloatPointView.setAlignSide(null, direction, windowManagerParams.x);
			mFloatPointView.startRunnable();
			setManagerParamsPosition(mCurrent_x, mCurrent_y - mFloatPointView.getHeight()/2);
			if(mFloatPointView.isAttachedToWindow())
			{
				windowManager.updateViewLayout(mFloatPointView, windowManagerParams);
			}
			else
			{
				windowManager.addView(mFloatPointView, windowManagerParams);
			}
		}
	}
	
	public void roateSemicirleView()
	{
		if(!isShowTPScreen)
		{
			mScreenStatu = SEMICIRCLE_STATU;
			Log.d("LZQISGOODBOYS", "direction:"+direction);
			windowManager.addView(setMenu(direction), windowManagerParams);
			isShowTPScreen = true;
		}
	}
	
	public void roateShortCutView()
	{
		ShowShortCut(null);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		
		if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_PORTRAIT) {
			roateScreen();
		} 
		else if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
			roateScreen();
		}
	}
	
	private void roateScreen()
	{
		int width = windowManager.getDefaultDisplay().getWidth(); 
	    int height = windowManager.getDefaultDisplay().getHeight(); 
	     
	    mFloatPointView.width = width;
	    mFloatPointView.height = height;
	    
	    if(mScreenStatu == FLOATVIEW_STATU)
	    {
	    	reMovePointView();
	    	notifyChangedXY();
	    	roateFloatView();
	    }
	    else if(mScreenStatu == SEMICIRCLE_STATU)
	    {
	    	reMoveSemiCircleView();
	    	notifyChangedXY();
	    	roateSemicirleView();
	    }
	    else if(mScreenStatu == SHORTCUT_STATU)
	    {
	    	reMoveSemiCircleView();
	    	notifyChangedXY();
	    	roateShortCutView();
	    }
	}
}
