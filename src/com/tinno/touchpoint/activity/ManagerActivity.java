package com.tinno.touchpoint.activity;

import com.tinno.touchpoint.R;
import com.tinno.touchpoint.recevier.MyAdminReceiver;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 
 * @author lzq
 *	2015.9.24
 */
public class ManagerActivity extends Activity {
	Intent intent = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ComponentName componentName = new ComponentName(this, MyAdminReceiver.class);		
		intent = new Intent(Intent.ACTION_MAIN);														
		intent.setAction(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);					
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);	
		startActivity(intent);
		this.finish();
	}
}
