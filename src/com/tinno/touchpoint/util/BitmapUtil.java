package com.tinno.touchpoint.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 
 * @author lzq
 * 2015.9.21
 */
public class BitmapUtil {

	public static Drawable roateImage(Bitmap bitmap,int w,int h)
	{
		Bitmap BintmapOrg = bitmap;
		int width = BintmapOrg.getWidth();
		int height = BintmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;		 
		float scaleWidth = (float)newWidth / width;
		float scaleHeight = (float)newHeight /height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		matrix.postRotate(180);
		Bitmap resizeBitmap = Bitmap.createBitmap(BintmapOrg, 0, 0, width, height, matrix, true);

		return new BitmapDrawable(resizeBitmap);
	}

}
