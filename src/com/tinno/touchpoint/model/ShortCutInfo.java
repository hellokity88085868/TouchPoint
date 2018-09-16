package com.tinno.touchpoint.model;

import java.util.List;

import android.graphics.drawable.Drawable;

/**
 * 
 * @author lzq
 * 2015.9.23
 */
public class ShortCutInfo {
	public int position;
	public boolean isExist;
	public String appLabel; 
	public String pakname;

	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getAppLabel() {
		return appLabel;
	}
	public void setAppLabel(String appLabel) {
		this.appLabel = appLabel;
	}
	public boolean isExist() {
		return isExist;
	}
	public void setExist(boolean isExist) {
		this.isExist = isExist;
	}
	public String getPakname() {
		return pakname;
	}
	public void setPakname(String pakname) {
		this.pakname = pakname;
	}
}
