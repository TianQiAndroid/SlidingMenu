package com.example.slidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MyLinearLayout extends LinearLayout {

	private boolean isMenuOpen = false;
	
	public MyLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	public MyLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(isMenuOpen){
			return true;
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	public void isMenuOpen(boolean isMenuOpen){
		this.isMenuOpen = isMenuOpen;
	}


}
