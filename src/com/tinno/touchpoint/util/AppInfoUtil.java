package com.tinno.touchpoint.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.tinno.touchpoint.model.AppInfo;

/**
 * 
 * @author lzq
 * 2015.9.23
 */
public class AppInfoUtil {
	private final static String TAG ="AppInfoUtil";
	private static PackageManager pm;

	public static List<AppInfo> getAllAppInfo(Context mContext)
	{
		List<AppInfo> mAppInfo= new ArrayList<AppInfo>();

		pm = mContext.getPackageManager();
		List<ApplicationInfo> listAppcations = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		Collections.sort(listAppcations,new ApplicationInfo.DisplayNameComparator(pm));

		mAppInfo.clear();
		for (ApplicationInfo app : listAppcations) {
			mAppInfo.add(getAppInfo(app));
		} 	

		return mAppInfo;
	}

	private static AppInfo getAppInfo(ApplicationInfo app) {
		AppInfo appInfo = new AppInfo();
		appInfo.setAppLabel((String) app.loadLabel(pm));
		appInfo.setAppIcon(app.loadIcon(pm));
		appInfo.setPkgName(app.packageName);
		return appInfo;
	}

	public static Drawable getPaknameIcon(List<AppInfo> mAppInfoList,String pakname)
	{
		Drawable icon = null;

		for(AppInfo mAppInfo : mAppInfoList)
		{
			if(mAppInfo.getPkgName().equals(pakname))
			{
				icon = mAppInfo.getAppIcon();
				break;
			}
		}

		return icon;
	}

	public static String getAppMainClassName(String packageName,Context mContext) {
		PackageInfo pi = null;
		try {
			pi = mContext.getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(pi.packageName);

		List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);

		if(apps.size() > 0)
		{
			ResolveInfo ri = apps.iterator().next();
			if (ri != null ) {
				return ri.activityInfo.name;
			}
		}
		return null;
	}
}
