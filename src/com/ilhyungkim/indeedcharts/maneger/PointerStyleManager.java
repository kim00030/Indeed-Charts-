package com.ilhyungkim.indeedcharts.maneger;

import java.util.ArrayList;

import org.achartengine.renderer.DialRenderer.Type;

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
import com.ilhyungkim.indeedcharts.interfaces.IStyleManager;
import com.ilhyungkim.indeedcharts.utilities.ChartHelper;

public class PointerStyleManager extends MarkerStyleManager implements IStyleManager,IAlertDialogInterface{
	
	
	protected PointerStyleDialogManager pointerStyleDialogManager;
	protected Type[] newTypes;
	
	public PointerStyleManager(Activity pActivity) {
		
		super(pActivity);
		init();
	}
	@Override
	protected void init(){
		pointerStyleDialogManager = new PointerStyleDialogManager(activity,this);
		
	}
	/***
	 * 
	 * @param newNameOfStyle	string literal of style user selected. it will be called from 
	 * 							PointerStyleDialogManager class
	 * @param type Type object of Dial Chart
	 */
	public void updateRadioButtonText(String newNameOfStyle,Type type){
		
		/** Display series name  and type name*/
		String str = seriesList.get(selectedRadioBtnIndex).getSeriesName()+":   <b>"+newNameOfStyle+"</b>";
		radioBtns[selectedRadioBtnIndex].setText(Html.fromHtml(str));
		newTypes[selectedRadioBtnIndex] = type;
	}
	@SuppressWarnings("deprecation")
	public void showChangePointerStyle(ArrayList<IDrawingChartInfo> pSeriesList){
		
		seriesList = pSeriesList;
		/**Create top layer that places in the dialog*/ 
		final LinearLayout topLinearLayout = new LinearLayout(activity);
		topLinearLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		llp.leftMargin = DialogManager.MARGIN_LEFT_IN_LAYOUT;
		
		/**for saving reference of all radio buttons*/
		radioBtns = new RadioButton[seriesList.size()];
		/** for saving current series point style*/
		newTypes = new Type[seriesList.size()];
		/**create radio group*/
		RadioGroup rg = new RadioGroup(activity);
		rg.setOrientation(RadioGroup.VERTICAL);
		/**set Title on the dialog window*/
		String t = activity.getResources().getString(R.string.chart_change_pointer_style);
		TextView title = DialogManager.createCustomTextView(activity, t);
		alertDialogBuilder = new AlertDialog.Builder(activity);
		alertDialogBuilder.setCustomTitle(title);
		alertDialogBuilder.setView(topLinearLayout);
		/**create radio buttons*/
		for (int i = 0; i<seriesList.size();i++) {

			radioBtns[i] = new RadioButton(activity);
			//****I naeed to add pointType name property in DialChart Data model
			String str = seriesList.get(i).getSeriesName()+":   <b>"+seriesList.get(i).getTypeName()+"</b>";
			radioBtns[i].setText(Html.fromHtml(str));
			newTypes[i]= seriesList.get(i).getType();
			rg.addView(radioBtns[i], i);
		}
		/**add scrollView in case, too many series created*/
		ScrollView sv = new ScrollView(activity);
		topLinearLayout.addView(sv);
		sv.addView(rg);
		/////////////////////
		/**radio button listener*/
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
				((AlertDialog)alertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
				for (int i = 0; i < PointerStyleManager.this.radioBtns.length; i++) {
					if(PointerStyleManager.this.radioBtns[i].isChecked()){
						PointerStyleManager.this.selectedRadioBtnIndex = i;
						PointerStyleManager.this.pointerStyleDialogManager.showStyleList(i);
						break;
					}
				}
			}
		});
		//////////////
		/** OK button listener*/
		alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				/**set new types*/
				for(int i = 0;i<seriesList.size();i++) {
					Type type = PointerStyleManager.this.newTypes[i];
					seriesList.get(i).setType(type);
				}
				Intent intent = DialogManager.createIntent(DialogManager.INTENT_ACTION_EDIT_POINTER_TYPE);
				PointerStyleManager.this.activity.sendBroadcast(intent);
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
		 ChartActivity chartActivity = (ChartActivity) this.activity;
		 chartActivity.currentDialogManager = this;
		 ((AlertDialog)alertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

	}
	public void dismissAlertDialog() {
		
		if(pointerStyleDialogManager != null ) pointerStyleDialogManager.dismissAlertDialog();
		ChartHelper.dismiss(this.alertDialog);
		
	}
	
}
