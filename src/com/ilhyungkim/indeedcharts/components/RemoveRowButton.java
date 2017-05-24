package com.ilhyungkim.indeedcharts.components;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TableRow;

import com.ilhyungkim.indeedcharts.interfaces.IManagerInterface;

public class RemoveRowButton extends CustomButton {

	private IManagerInterface manager;
	private TableRow tr;
	private EditText xValue;
	private EditText yValue;

	
	public RemoveRowButton(Activity pActivity,IManagerInterface manager, TableRow tr,EditText xValue, EditText yValue) {
		super(pActivity);
		this.manager = manager;
		this.tr = tr;
		this.xValue = xValue;
		this.yValue = yValue;
				
		
		this.setText("Remove");
	}

	public IManagerInterface getManager() {
		return manager;
	}
	
	public void setManager(IManagerInterface manager) {
		this.manager = manager;
	}
	
	public TableRow getTr() {
		return tr;
	}

	public void setTr(TableRow tr) {
		this.tr = tr;
	}
	public EditText getxValue() {
		return xValue;
	}
	public void setxValue(EditText xValue) {
		this.xValue = xValue;
	}
	public EditText getyValue() {
		return yValue;
	}
	public void setyValue(EditText yValue) {
		this.yValue = yValue;
	}
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		manager.removeRow(getTr(),getxValue(),getyValue());
		return super.onTouchEvent(event);
	}
}
