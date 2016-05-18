package com.example.slidingmenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

public class SlidingMenu extends ViewGroup implements OnClickListener {

	private int screenWidth;
	private int screenHeight;
	private int menuWidth;

	private Scroller scroller;
	private boolean isOpen = false;

	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point outSize = new Point();
		display.getSize(outSize);
		screenWidth = outSize.x;
		screenHeight = outSize.y;
		menuWidth = screenWidth * 4 / 5;
		scroller = new Scroller(context);
		// 确保viewgroup可以消耗touch事件
		setClickable(true);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int count = getChildCount();
		// 先测量孩子大小
		for (int i = 0; i < count; i++) {
			if (getChildAt(i).getVisibility() != GONE) {
				int sizeWidth = 0;
				int sizeHeight = screenHeight;
				int mode = MeasureSpec.EXACTLY;
				if (i == 0) {
					// menu
					sizeWidth = menuWidth;
				} else {
					// content
					sizeWidth = screenWidth;
				}
				getChildAt(i).measure(
						MeasureSpec.makeMeasureSpec(sizeWidth, mode),
						MeasureSpec.makeMeasureSpec(sizeHeight, mode));
			}
		}

		// 再测量自己大小
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}

	/**
	 * 宽度为menu的宽度加上屏幕的宽度
	 * 
	 * @param widthMeasureSpec
	 * @return
	 */
	private int measureWidth(int widthMeasureSpec) {

		return menuWidth + screenWidth;

	}

	/**
	 * 高度为屏幕的高度
	 * 
	 * @param heightMeasureSpec
	 * @return
	 */
	private int measureHeight(int heightMeasureSpec) {

		return screenHeight;

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			// c初始状态隐藏menu，显示content
			getChildAt(0).layout(-menuWidth, 0, 0, screenHeight);
			getChildAt(1).layout(0, 0, screenWidth, screenHeight);
			// 当menu打开的时候，点击content关闭menu
			getChildAt(1).setOnClickListener(this);
		}

	}

	private float preX = 0;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			preX = ev.getX();

		case MotionEvent.ACTION_MOVE:
			float moveX = ev.getX();
			if (Math.abs(moveX - preX) >= 10) {
				return true;
			}
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float moveX = event.getX();
			int dx = (int) (moveX - preX);
			if (getScrollX() >= -menuWidth && getScrollX() <= 0) {
				this.scrollBy(-dx, 0);
				if (getScrollX() < -menuWidth) {
					this.scrollTo(-menuWidth, 0);
				} else if (getScrollX() > 0) {
					this.scrollTo(0, 0);
				}
			}
			preX = moveX;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (getScrollX() > -menuWidth / 2) {
				// 关闭menu
				isOpen = true;
				close();
			} else {
				// 打开menu
				isOpen = false;
				open();
			}
			invalidate();
			break;
		}
		return super.onTouchEvent(event);
	}

	public void open() {
		if (!isOpen) {
			isOpen = true;
			((MyLinearLayout) getChildAt(1)).isMenuOpen(true);
			scroller.startScroll(getScrollX(), 0, Math.abs(getScrollX())
					- menuWidth, 0);
			invalidate();
		}
	}

	public void close() {
		if (isOpen) {
			isOpen = false;
			((MyLinearLayout) getChildAt(1)).isMenuOpen(false);
			scroller.startScroll(getScrollX(), 0, Math.abs(getScrollX()), 0);
			invalidate();
		}
	}

	public void toggle() {
		if (isOpen) {
			close();
		} else {
			open();
		}
	}

	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			this.scrollTo(scroller.getCurrX(), 0);
			postInvalidate();
		}
	}

	@Override
	public void onClick(View v) {
		if (isOpen) {
			close();
		}
	}

}
