package com.tinno.touchpoint.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.SystemClock;
import android.os.storage.StorageManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

public class ScreenShotUtil {

	private Display mDisplay;
	private DisplayMetrics mDisplayMetrics;
	private Matrix mDisplayMatrix;
	private Bitmap mScreenBitmap;
	private WindowManager mWindowManager;
	private Context mContext;

	public ScreenShotUtil(Context context) {
		mContext = context;
	}


	public Boolean ScreenShot() {
		Log.i("LZQGOODBOY", "1111111111");
		mDisplayMatrix = new Matrix();

		mWindowManager = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		mDisplay = mWindowManager.getDefaultDisplay();
		mDisplayMetrics = new DisplayMetrics();

		// for eclips debug.
		//mDisplay.getRealMetrics(mDisplayMetrics);

		float[] dims = { mDisplayMetrics.widthPixels,
				mDisplayMetrics.heightPixels };
		float degrees = getDegreesForRotation(mDisplay.getRotation());
		boolean requiresRotation = (degrees > 0);
		Log.i("hello", "requiresRotation = "+requiresRotation);
		if (requiresRotation) {
			// Get the dimensions of the device in its native orientation
			mDisplayMatrix.reset();
			mDisplayMatrix.preRotate(-degrees);
			mDisplayMatrix.mapPoints(dims);
			dims[0] = Math.abs(dims[0]);
			dims[1] = Math.abs(dims[1]);
		}    
		
		//for eclips debug.
		//mScreenBitmap = SurfaceControl.screenshot((int) dims[0], (int) dims[1]);//line<><led light><20140408>yinhuiyong
		Log.i("LZQGOODBOY", "mScreenBitmap = "+mScreenBitmap);
		if (requiresRotation) {
			// Rotate the screenshot to the current orientation
			Bitmap ss = Bitmap.createBitmap(mDisplayMetrics.widthPixels,
					mDisplayMetrics.heightPixels, Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(ss);
			c.translate(ss.getWidth() / 2, ss.getHeight() / 2);
			c.rotate(degrees);
			c.translate(-dims[0] / 2, -dims[1] / 2);
			c.drawBitmap(mScreenBitmap, 0, 0, null);
			c.setBitmap(null);
			mScreenBitmap = ss;
		}
		// If we couldn't take the screenshot, notify the user
		Log.i("LZQGOODBOY", "mScreenBitmap = "+mScreenBitmap);
		if (mScreenBitmap == null) {
		Log.i("LZQGOODBOY", "22222222FALSE");
			return false;
		}
		// Optimizations
		//mScreenBitmap.setHasAlpha(false);
		mScreenBitmap.prepareToDraw();
		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy_MM_dd'_'HH_mm_ss");
		String date = sDateFormat.format(new java.util.Date());
		
		//String defpath = StorageManagerEx.getDefaultPath();
		String defpath = "";
		Log.d("LZQGOODBOY", "22222="+defpath);
		String mPath=null;
		if(defpath.equals("/storage/emulated/0")){
			mPath = "/storage/emulated/0/Pictures/Screenshots/";
		}
		else if(defpath.equals("/storage/sdcard0")){
			mPath = "/storage/sdcard0/Pictures/Screenshots/";

//			mPath = "/storage/emulated/0/MagicPoint/screenshot/";
		}else{
			mPath = "/storage/sdcard1/Pictures/Screenshots/";
		}
		File file = new File(mPath);
		file.mkdirs();
		return savePic(mScreenBitmap, mPath + date + "_"+SystemClock.elapsedRealtime()+".png");
		//return savePic(mScreenBitmap, mPath+date+".png");
	}

	/**
	 * @return the current display rotation in degrees
	 */
	private float getDegreesForRotation(int value) {
		switch (value) {
		case Surface.ROTATION_90:
			return 360f - 90f;
		case Surface.ROTATION_180:
			return 360f - 180f;
		case Surface.ROTATION_270:
			return 360f - 270f;
		}
		return 0f;
	}

	private Boolean savePic(Bitmap b, String fileName) {
		FileOutputStream fos = null;
		Boolean r = false;
		Log.i("hello", "savePic ,fileName = "+fileName);
		try {

			fos = new FileOutputStream(fileName);
			if (null != fos) {
				r = b.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
			}
			return r;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.i("hello", "FileNotFoundException = "+e.toString());
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("hello", "IOException = "+e.toString());
			return false;
		}
	}
	public Bitmap getBitmap(){
		return mScreenBitmap;
	}

}
