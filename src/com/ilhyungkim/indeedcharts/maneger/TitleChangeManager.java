package com.ilhyungkim.indeedcharts.maneger;

import java.util.ArrayList;

import com.ilhyungkim.indeedcharts.ChartActivity;
import com.ilhyungkim.indeedcharts.R;
import com.ilhyungkim.indeedcharts.interfaces.IAlertDialogInterface;
import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;
import com.ilhyungkim.indeedcharts.utilities.ChartHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TitleChangeManager extends DialogManager implements IAlertDialogInterface{

	private Activity activity;
	private LinearLayout topLinearLayout;
	private ArrayList<IDrawingChartInfo> list;
	private AlertDialog.Builder alertDialogBuilder;
	private AlertDialog alertDialog;
	
	public TitleChangeManager(Activity activity){
		
		this.activity = activity;
	}
	
	@SuppressWarnings("deprecation")
	public void show(ArrayList<IDrawingChartInfo> pList){
		
		ChartHelper.dismiss(alertDialog);
		list = pList;
		//Set Title of Dialog
		TextView title =createCustomTextView(activity, activity.getResources().getString(R.string.chart_default_title));
		alertDialogBuilder = new AlertDialog.Builder(activity);
		alertDialogBuilder.setCustomTitle(title);
		//Create top-most layout
		topLinearLayout = new LinearLayout(activity);
		
		topLinearLayout.setOrientation(LinearLayout.VERTICAL);
		//Create layout params
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		llp.leftMargin = MARGIN_LEFT_IN_LAYOUT; 
		
		EditText et = new EditText(activity);
		et.setId(R.id.chart_title);
		/**show current title */
		 et.setText(list.get(0).getTitle());
		 /**place cursor at the end of text in EditText*/
		 et.setSelection(et.getText().length());
		//et.setInputType(data.getInputType());
		 topLinearLayout.addView(et, llp);
		 
			alertDialogBuilder.setView(topLinearLayout);
			
			alertDialogBuilder.setPositiveButton(OK, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					EditText title = (EditText) topLinearLayout.findViewById(R.id.chart_title);
					
					Intent intent = null;
					if(title != null && title.getText().toString().trim().length() >0){
						/**update title user enters*/
						for (IDrawingChartInfo drawingChartInfo : list) {
							
							drawingChartInfo.setTitle(title.getText().toString().trim());
						}
						
						intent =  createIntent(INTENT_ACTION_EDIT_TITLE);
						//intent.putExtra(title.getHint().toString(),title.getText().toString());
						activity.sendBroadcast(intent);
					}
					ChartHelper.dismiss(alertDialog);
				}
			});
			
			alertDialogBuilder.setNegativeButton(CANCEL, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					ChartHelper.dismiss(alertDialog);
					
				}
			});
			
			 alertDialog = alertDialogBuilder.create();
			 alertDialog.show();
			ChartActivity chartActivity = (ChartActivity) this.activity;
			chartActivity.currentDialogManager = this;
	}
	public void dismissAlertDialog(){
		ChartHelper.dismiss(alertDialog);
	}
}
