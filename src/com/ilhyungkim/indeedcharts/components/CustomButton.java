package com.ilhyungkim.indeedcharts.components;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.MotionEvent;
import android.widget.Button;

public class CustomButton extends Button{

	public CustomButton(Activity pActivity) {
		super(pActivity);
	}

	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouchEvent(MotionEvent event) {
		
		return false;
	}
	
}
