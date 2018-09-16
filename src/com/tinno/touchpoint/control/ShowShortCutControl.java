package com.tinno.touchpoint.control;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.tinno.touchpoint.R;
import com.tinno.touchpoint.interfaces.ShowShortCutDialogCallBack;

/**
 * 
 * @author lzq
 * 2015.9.24
 */
public class ShowShortCutControl {
	
	Dialog mDeleteShortCutDialog = null;
	ShowShortCutDialogCallBack mShowShortCutDialogCallBack = null;
	private Context mContext;
	
	public ShowShortCutControl(Context mContext)
	{
		this.mContext = mContext;
	}

	public void setDeletShortCutDialog(Drawable mIcon)
	{
		mDeleteShortCutDialog = new AlertDialog.Builder(mContext). 
				setTitle("删除快捷方式"). 
				setMessage("确定删除此快捷方式？").
				setIcon(mIcon).
				setNegativeButton("取消", new DialogInterface.OnClickListener() { 
					@Override 
					public void onClick(DialogInterface dialog, int which) { 
						// TODO Auto-generated method stub  
					} 
				}).
				setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mShowShortCutDialogCallBack.deleteShortCut();
					}
				}).create();
		mDeleteShortCutDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
		mDeleteShortCutDialog.show(); 
	}
	
	public void setShowShortCutDialogCallBack(ShowShortCutDialogCallBack mShowShortCutDialogCallBack)
	{
		this.mShowShortCutDialogCallBack = mShowShortCutDialogCallBack;
	}
}
