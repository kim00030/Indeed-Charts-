package com.ilhyungkim.indeedcharts.maneger;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilhyungkim.indeedcharts.ChartActivity;
import com.ilhyungkim.indeedcharts.R;
import com.ilhyungkim.indeedcharts.interfaces.IAlertDialogInterface;
import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;
import com.ilhyungkim.indeedcharts.utilities.ChartHelper;

public class VisibilityOfChartValuesManager implements IAlertDialogInterface{

	private Activity activity;
	private  AlertDialog.Builder alertDialogBuilder;
	private  AlertDialog alertDialog;
	private ArrayList<IDrawingChartInfo> list;
	private CheckBox ch;
	
	public VisibilityOfChartValuesManager(Activity activity){
		
		this.activity = activity;
	}
	
	public void show(ArrayList<IDrawingChartInfo> list){
		
		ChartHelper.dismiss(alertDialog);
		this.list = list;
		/**Create top layer that places in the dialog*/ 
		final LinearLayout topLinearLayout = new LinearLayout(activity);
		topLinearLayout.setOrientation(LinearLayout.VERTICAL);

		/**set Title on the dialog window*/
		String t = activity.getResources().getString(R.string.chart_visibility_chart_value);
		TextView title = DialogManager.createCustomTextView(activity, t);
		alertDialogBuilder = new AlertDialog.Builder(activity);
		alertDialogBuilder.setCustomTitle(title);
		alertDialogBuilder.setView(topLinearLayout);
		/**create check box*/
		ch = new CheckBox(this.activity);
		/**set initial checked. i used index 0 of list object. it's ok because all list objects has same value*/
		ch.setChecked(this.list.get(0).isShowChartValues());
		ch.setText("Show Chart Values");
		topLinearLayout.addView(ch);
		
		alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				CheckBox ch = VisibilityOfChartValuesManager.this.ch;
				ArrayList<IDrawingChartInfo> list = VisibilityOfChartValuesManager.this.list;
				
				for (IDrawingChartInfo iDrawingChartInfo : list) {
					iDrawingChartInfo.setShowChartValues(ch.isChecked());
				}
				Intent intent = DialogManager.createIntent(DialogManager.Intent_ACTION_SHOW_CHART_VALUES);
				
				VisibilityOfChartValuesManager.this.activity.sendBroadcast(intent);
				
				ChartHelper.dismiss(alertDialog);
			}
		});
		alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				ChartHelper.dismiss(alertDialog);
			}
		});
		
		
		alertDialog = alertDialogBuilder.create();
		/**
		 *disable dismissing ability when click on outside of dialog
		 */
		//alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
		ChartActivity chartActivity = (ChartActivity) this.activity;
		chartActivity.currentDialogManager = this;
	}

	@Override
	public void dismissAlertDialog() {
		ChartHelper.dismiss(alertDialog);
	}
}
