package com.ilhyungkim.indeedcharts.maneger;

import java.util.ArrayList;

import org.achartengine.chart.PointStyle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ilhyungkim.indeedcharts.ChartActivity;
import com.ilhyungkim.indeedcharts.R;
import com.ilhyungkim.indeedcharts.interfaces.IAlertDialogInterface;
import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;
import com.ilhyungkim.indeedcharts.utilities.ChartHelper;

public class MarkerStyleManager implements IAlertDialogInterface{
	
	protected Activity activity;
	protected StyleDialogManager styleDialogManager;
	protected  AlertDialog.Builder alertDialogBuilder;
	protected  AlertDialog alertDialog;
	protected int selectedRadioBtnIndex = -1;
	protected RadioButton[] radioBtns;
	protected PointStyle[] newStyles;
	protected ArrayList<IDrawingChartInfo> seriesList;
	
	
	public MarkerStyleManager(Activity pActivity){
		
		activity = pActivity;
		init();
	}
	
	protected void init(){
		/**This is to manage for showing list of styles*/
		styleDialogManager = new StyleDialogManager(activity,this);
	}
	
	/***
	 * 
	 * @param newNameOfStyle	string literal of style user selected. it will be called from 
	 * 							StyleDialogManager class
	 */
	public void updateRadioButtonText(String newNameOfStyle,PointStyle pointStyle){
		
		/** Display series name  and name of style*/
		String str = seriesList.get(selectedRadioBtnIndex).getSeriesName()+":  <b>"+newNameOfStyle+"</b>";
		radioBtns[selectedRadioBtnIndex].setText(Html.fromHtml(str));
		newStyles[selectedRadioBtnIndex] = pointStyle;
	}
	@SuppressWarnings("deprecation")
	public void showChangeMarkerStyle(ArrayList<IDrawingChartInfo> pSeriesList){
		
		seriesList = pSeriesList;
		/**Create top layer that places in the dialog*/ 
		final LinearLayout topLinearLayout = new LinearLayout(activity);
		topLinearLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		llp.leftMargin = DialogManager.MARGIN_LEFT_IN_LAYOUT;
		
		/**for saving reference of all radio buttons*/
		radioBtns = new RadioButton[seriesList.size()];
		/** for saving current series point style*/
		newStyles = new PointStyle[seriesList.size()];
		/**create radio group*/
		RadioGroup rg = new RadioGroup(activity);
		rg.setOrientation(RadioGroup.VERTICAL);
		/**set Title on the dialog window*/
		String t = activity.getResources().getString(R.string.chart_change_marker_style);
		TextView title = DialogManager.createCustomTextView(activity, t);
		alertDialogBuilder = new AlertDialog.Builder(activity);
		alertDialogBuilder.setCustomTitle(title);
		alertDialogBuilder.setView(topLinearLayout);
		/**create radio buttons*/
		for (int i = 0; i<seriesList.size();i++) {

			radioBtns[i] = new RadioButton(activity);
			String str = seriesList.get(i).getSeriesName()+":  <b>"+seriesList.get(i).getPointStyleName()+"</b>";
			radioBtns[i].setText(Html.fromHtml(str));
			newStyles[i]= seriesList.get(i).getPointStyle();
			rg.addView(radioBtns[i], i);
		}
		/**add scrollView in case, too many series created*/
		ScrollView sv = new ScrollView(activity);
		topLinearLayout.addView(sv);
		sv.addView(rg);
		
		/**radio button listener*/
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
				((AlertDialog)alertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
				for (int i = 0; i < MarkerStyleManager.this.radioBtns.length; i++) {
					if(MarkerStyleManager.this.radioBtns[i].isChecked()){
						MarkerStyleManager.this.selectedRadioBtnIndex = i;
						MarkerStyleManager.this.styleDialogManager.showStyleList(i);
						break;
					}
				}
			}
		});
		/** Update button listener*/
		alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
				for(int i = 0;i<seriesList.size();i++) {
					/**set new marker style*/
					PointStyle ps = MarkerStyleManager.this.newStyles[i];
					seriesList.get(i).setPointStyle(ps);
				}
				Intent intent = DialogManager.createIntent(DialogManager.INTENT_ACTION_EDIT_MARKER_STYLE);
				MarkerStyleManager.this.activity.sendBroadcast(intent);
			
				ChartHelper.dismiss(alertDialog);
				
			}
		});
		 /**Cancel button listener*/
		alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				ChartHelper.dismiss(alertDialog);
				
			}
		});
		
		alertDialog = alertDialogBuilder.create();
		 /**
		  * disable dismissing ability when click on outside of dialog
		  */
		 //alertDialog.setCanceledOnTouchOutside(false);
		 alertDialog.show();
		 /**save this class references in ChartActivity */
		 ChartActivity chartActivity = (ChartActivity) this.activity;
		 chartActivity.currentDialogManager = this;
		 ((AlertDialog)alertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

	}

	@Override
	public void dismissAlertDialog() {
		if(styleDialogManager != null) styleDialogManager.dismissAlertDialog();
		ChartHelper.dismiss(this.alertDialog);
	}
	
}
