package com.tinno.touchpoint.view;

import com.tinno.touchpoint.R;
import com.tinno.touchpoint.service.FloatSer;
import com.tinno.touchpoint.util.BitmapUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
/**
 * 
 * @author lzq
 * 2015.9.17
 */
public class SemiCircleLayout extends ViewGroup {

	private final static String TAG = "SemiCircleLayout";
	private int mDegreeDelta; 
	public int mDirection = 1;
	
	public SemiCircleLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public SemiCircleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SemiCircleLayout(Context context,int i) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mDirection = i;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		
		Log.d("LZQISGOODBOYs", "mDirection"+mDirection);
		final int count = getChildCount();

		mDegreeDelta = 180/(count-2);

		final int parentLeft = getPaddingLeft();
		final int parentRight = right - left - getPaddingRight();

		final int parentTop = getPaddingTop();
		final int parentBottom = bottom - top - getPaddingBottom();

		if (count < 1 ) {
			return;
		}
		for (int i = 0; i < count; i++) {

			final View child = getChildAt(i);
			if (child.getVisibility() != GONE) {

				final int width = child.getMeasuredWidth();
				final int height = child.getMeasuredHeight();

				int childLeft;
				int childTop;
				if (count == 1) {
					childLeft = parentLeft + (parentRight - parentLeft - width) / 2;
					childTop = parentTop + (parentBottom - parentTop - height) / 2 ;
					child.layout(childLeft, childTop,childLeft+width,childTop+height);
				}else{
					if(0 == mDirection)
					{
						this.setBackgroundResource(R.drawable.menu_bg);
						if(i == 0)
						{
							childLeft = (int)(parentLeft + (parentRight - parentLeft - parentBottom/6));
							childTop = (int)(parentTop + (parentBottom - parentTop - height) / 2) ;
							child.layout(childLeft, childTop,childLeft+width,childTop+height);
						}
						else
						{
							childLeft = (int)(parentLeft + (parentRight - parentLeft - parentBottom/6) - (parentBottom/3)*Math.sin(((i-1)*mDegreeDelta*Math.PI/180)));
							childTop = (int) (parentTop + (parentBottom - parentTop-height) / 2 - (parentBottom/3)*Math.cos(((i-1)*mDegreeDelta*Math.PI/180))) ;
							child.layout(childLeft, childTop,childLeft+width,childTop+height);
						}
					}
					else
					{
						Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.menu_bg);
						this.setBackgroundDrawable(BitmapUtil.roateImage(bitmap, FloatSer.MENU_WIDTH, FloatSer.MENU_HEIGHT));
						if(i == 0)
						{
							childLeft = (int)(parentLeft);
							childTop = (int)(parentTop + (parentBottom - parentTop - height) / 2) ;
							child.layout(childLeft, childTop,childLeft+width,childTop+height);
						}
						else
						{
							childLeft = (int)(parentLeft + (parentBottom/3)*Math.sin(((i-1)*mDegreeDelta*Math.PI/180)));
							childTop = (int) (parentTop + (parentBottom - parentTop-height) / 2 - (parentBottom/3)*Math.cos(((i-1)*mDegreeDelta*Math.PI/180))) ;
							child.layout(childLeft, childTop,childLeft+width,childTop+height);
						}
					}
				}
			}
		}     
	}
	
	@Override  
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)  
	{  
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);  
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);  

		measureChildren(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(sizeWidth, sizeHeight);
	}
	
}
