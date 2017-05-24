package com.ilhyungkim.indeedcharts.components;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.MotionEvent;

import com.ilhyungkim.indeedcharts.interfaces.IManagerInterface;

public class AddRowButton extends CustomButton {

	private IManagerInterface manager;
	
	//private XYValueChangeManager xyValueChangeManager;
	private boolean status = true;
	public AddRowButton(Activity pActivity,IManagerInterface manager) {
		super(pActivity);
		this.manager = manager;
		this.setText("Add Row");
		
		
	}
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		manager.updateButtonStatus();
		if(status == true ){
			/**Excutes adding row on table*/
			manager.addRow(null);
			//xyValueChangeManager.addRow(null);
		}
		
		return super.onTouchEvent(event);
	}
	
	public void buttonEnabled(boolean status){
		this.status = status;
		this.setEnabled(status);
	}
}
