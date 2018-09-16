package com.tinno.touchpoint.view;

import com.tinno.touchpoint.activity.FloatApplication;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * 
 * @author lzq
 * 2015.9.17
 */
public class FloatPointView extends ImageView {
	
	private final static String TAG = "FloatPointView";
	public float x; 
	public float y; 

	private float mTouchX; 
	private float mTouchY; 

	private float mStartX; 
	private float mStartY; 

	private OnClickListener mClickListener; 
	public FloatPointView mFloatPointView = null;

	private WindowManager windowManager = (WindowManager) getContext() 
			.getApplicationContext().getSystemService(Context.WINDOW_SERVICE); 
	private WindowManager.LayoutParams windowManagerParams = ((FloatApplication) getContext() 
			.getApplicationContext()).getWindowParams(); 
	
	public int width; 
	public int height;
	
	private int width_wp = windowManagerParams.width;
	private int height_wp = windowManagerParams.height;
	
	private float distance;
	private Boolean direction;
	private float mCurrent_x = 0;
	private float mCurrent_y = 0;
	private int count = 0;
	private int statusBarHeight;
    private int dirciton_bg = 0;

	private final static int MOVE_TIMES = 30;
	public Boolean mAlignSide = false;
	
	public Handler mHandler = new Handler();  
	public Runnable mRunnable = null;  
	
	public FloatPointView(Context context) {
		super(context);
		mFloatPointView = this;
		if(null == mRunnable)
		{
			startRunnable();
		}
		width = windowManager.getDefaultDisplay().getWidth(); 
		height = windowManager.getDefaultDisplay().getHeight();
		// TODO Auto-generated constructor stub
	}
	
	@Override 
	public void setOnClickListener(OnClickListener l) { 
		this.mClickListener = l; 
	}
	
	@Override 
	public boolean onTouchEvent(MotionEvent event){
		Rect frame = new Rect(); 
		getWindowVisibleDisplayFrame(frame); removeCallbacks(mRunnable);
		statusBarHeight = frame.top;

		x = event.getRawX(); 
		y = event.getRawY() - statusBarHeight; 

		switch (event.getAction()) { 
		case MotionEvent.ACTION_DOWN: 
			mTouchX = event.getX(); 
			mTouchY = event.getY(); 
			mStartX = x; 
			mStartY = y; 
			break; 
		case MotionEvent.ACTION_MOVE: 
			updateViewPosition(); 
			break; 
		case MotionEvent.ACTION_UP: 
			updateViewPosition(); 
			mTouchX = mTouchY = 0; 
			if ((x - mStartX) < 5 && (y - mStartY) < 5) { 
				if(mClickListener!=null) { 
					mClickListener.onClick(this); 
				} 
			} 
			setAlignSide(event,0,0);
			if(null != mRunnable)
			{
				mHandler.post(mRunnable);
			}
			break; 
		} 
		return false;
	}
	
	@SuppressLint("NewApi")
	public void setAlignSide(MotionEvent event,int directions,float moveDistance)
	{
//		width = windowManager.getDefaultDisplay().getWidth(); 
//		height = windowManager.getDefaultDisplay().getHeight();
		
		if(null != event)
		{
			mCurrent_x = event.getRawX();
			mCurrent_y = event.getRawY() - statusBarHeight;

			if(mCurrent_x>= (width/2))
			{
				distance = (width - mCurrent_x)/MOVE_TIMES;
				direction = true;
			}
			else
			{
				distance = mCurrent_x/MOVE_TIMES;
				direction = false;   
			}
		}
		else 
		{
			if(1 == directions)
			{
				distance = moveDistance/MOVE_TIMES;
				direction = true;
			}
			else
			{
				distance = (width - moveDistance)/MOVE_TIMES;
				direction = false;
			}
		}
	}

	@SuppressLint("NewApi")
	private void updateViewPosition() { 
//		windowManagerParams.x = (int) (x - mTouchX); 
//		windowManagerParams.y = (int) (y - mTouchY); 
		windowManagerParams.x = (int) (x - this.getWidth()/2); 
		windowManagerParams.y = (int) (y - this.getHeight()/2); 
		if(null != mFloatPointView && mFloatPointView.isAttachedToWindow())
		{
			windowManager.updateViewLayout(mFloatPointView, windowManagerParams); 
		}
	} 
	
	public void removeRunable()
	{
		mRunnable = null;
	}
	
	public void startRunnable()
	{
		

		count = 0;
		mRunnable = new Runnable() {  
			@SuppressLint("NewApi")
			public void run() {  
				if(count < MOVE_TIMES)
				{
					count ++;
					if(direction)
					{
						mCurrent_x = mCurrent_x + distance;
						windowManagerParams.x = (int)(mCurrent_x - mFloatPointView.getWidth()/2); 
						windowManagerParams.y = (int) (mCurrent_y - mFloatPointView.getHeight()/2); 
					}
					else
					{
						mCurrent_x = mCurrent_x - distance;
						windowManagerParams.x = (int)(mCurrent_x - mFloatPointView.getWidth()/2); 
						windowManagerParams.y = (int) (mCurrent_y - mFloatPointView.getHeight()/2); 
					}
					if(null != mFloatPointView && mFloatPointView.isAttachedToWindow())
					{
						windowManager.updateViewLayout(mFloatPointView, windowManagerParams); 
					}
					mHandler.postDelayed(mRunnable,15);  
				}
				else
				{
					count = 0;
					mAlignSide = true;
				}
			}
		};
	}

}
