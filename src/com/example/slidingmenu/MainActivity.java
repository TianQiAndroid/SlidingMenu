package com.example.slidingmenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
SlidingMenu slidingMenu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		slidingMenu = (SlidingMenu) findViewById(R.id.sm);
	}
	
	public void open(View v){
		slidingMenu.open();
	}
	
	public void close(View v){
		slidingMenu.close();
	}
	
	public void toggle(View v){
		slidingMenu.toggle();
	}
}
