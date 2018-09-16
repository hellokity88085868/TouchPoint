package com.tinno.touchpoint.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.tinno.touchpoint.model.ShortCutInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * 
 * @author lzq
 *	2015.9.23
 */
public class SharePreference {
	private static Gson gson = new Gson();
	private static String[] shortcut_position = new String[]{"7","8","9","10","11"};
	public static String SHORTCUT_SP_NAME = "shortcut_data";

	public static List<ShortCutInfo> getDefaultShortCutList(){
		List<ShortCutInfo> list = new ArrayList<ShortCutInfo>();
		for (int i = 0; i < shortcut_position.length; i++) {
			ShortCutInfo model = new ShortCutInfo();
			model.position = i+7;
			model.isExist = false;
			model.appLabel = null;
			model.pakname = null;
			list.add(model);
		}
		return list;
	}
	
	@SuppressWarnings({"unchecked" })
	public static List<ShortCutInfo> getShortCutList(SharedPreferences prefs){
		List<ShortCutInfo> list = new ArrayList<ShortCutInfo>();
		if (prefs == null) {
			try {
				throw new Exception("this file not found");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.getMessage();
			}
		}else{
			Map<String,String> map = (Map<String, String>) prefs.getAll();
			Iterator<String> iterator = map.keySet().iterator();
			while (iterator.hasNext()) {
				ShortCutInfo model = gson.fromJson(map.get(iterator.next()), ShortCutInfo.class);
				list.add(model);
			}
		}
		return list;
	}

	public static void saveShortCutList(Context mContext,List<ShortCutInfo> list){
		SharedPreferences prefs = mContext.getSharedPreferences(SHORTCUT_SP_NAME, mContext.MODE_PRIVATE);
		Editor editor = prefs.edit();
		for (int i = 0; i < shortcut_position.length; i++) {
			Log.i("test", gson.toJson(list.get(i)));
			editor.putString(shortcut_position[i],gson.toJson(list.get(i)));
		}
		editor.commit();
	}

}
