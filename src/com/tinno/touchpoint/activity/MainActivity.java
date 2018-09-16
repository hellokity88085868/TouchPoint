package com.tinno.touchpoint.activity;

import com.tinno.touchpoint.R;
import com.tinno.touchpoint.service.FloatSer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
/**
 * 
 * @author lzq
 * 2015.9.17
 */
public class MainActivity extends Activity {
	Intent intent = null;
	FloatSer mFloatSer = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 
		intent = new Intent(this,FloatSer.class);
//		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		startService(intent);
	}
	
//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		// TODO Auto-generated method stub
//		super.onConfigurationChanged(newConfig);
//		if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_PORTRAIT) {
//			roateScreen();
//		} 
//		else if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
//			roateScreen();
//		}
//	}
	
//	private void roateScreen()
//	{
//		WindowManager wm = this.getWindowManager(); 
//		int width = wm.getDefaultDisplay().getWidth(); 
//	    int height = wm.getDefaultDisplay().getHeight(); 
//	     
//	    mFloatSer.mFloatPointView.width = width;
//	    mFloatSer.mFloatPointView.height = height;
//	    
//	    
//	    if(mFloatSer.mScreenStatu == mFloatSer.FLOATVIEW_STATU)
//	    {
//	    	mFloatSer.reMovePointView();
//	    	mFloatSer.notifyChangedXY();
//	    	mFloatSer.roateFloatView();
//	    }
//	    else if(mFloatSer.mScreenStatu == mFloatSer.SEMICIRCLE_STATU)
//	    {
//	    	mFloatSer.reMoveSemiCircleView();
//	    	mFloatSer.notifyChangedXY();
//	    	mFloatSer.roateSemicirleView();
//	    }
//	    else if(mFloatSer.mScreenStatu == mFloatSer.SHORTCUT_STATU)
//	    {
//	    	mFloatSer.reMoveSemiCircleView();
//	    	mFloatSer.notifyChangedXY();
//	    	mFloatSer.roateShortCutView();
//	    }
//	}
	
	private ServiceConnection mConnection = new ServiceConnection() 
	{
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			mFloatSer = ((FloatSer.LocalBinder)service).getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
		}
	};
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if(null != intent)
		{
			stopService(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
