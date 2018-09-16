package com.tinno.touchpoint.view;

import com.tinno.touchpoint.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * 
 * @author android
 * 2015.9.21
 */
public class AppInfoDialog extends Dialog {

	Context mContext = null;

	public AppInfoDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appinfo_dialog);
		this.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
		this.setTitle("选择程序");
	}
}
