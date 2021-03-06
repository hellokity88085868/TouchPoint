package com.tinno.touchpoint.util;

import android.content.Context;

/**
 * 
 * @author android
 *  2015.10.15
 */
public class DensityUtil {

	public static int dip2px(Context context, float dpValue) {  
		final float scale = context.getResources().getDisplayMetrics().density;  
		return (int) (dpValue * scale + 0.5f);  
	}  
 
	public static int px2dip(Context context, float pxValue) {  
		final float scale = context.getResources().getDisplayMetrics().density;  
		return (int) (pxValue / scale + 0.5f);  
	} 
}
