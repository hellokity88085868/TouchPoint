package com.tinno.touchpoint.activity;

import android.app.Application;
import android.content.Intent;
import android.view.WindowManager;
 
/**
 * 
 * @author lzq
 * 2015.9.17
 */
public class FloatApplication  extends Application{
    private WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams(); 
    public WindowManager.LayoutParams getWindowParams() { 
    return windowParams; 
    } 
}
