package com.ilhyungkim.indeedcharts.maneger;

import org.achartengine.chart.PointStyle;

import com.ilhyungkim.indeedcharts.ChartActivity;
import com.ilhyungkim.indeedcharts.R;
import com.ilhyungkim.indeedcharts.interfaces.IAlertDialogInterface;
import com.ilhyungkim.indeedcharts.utilities.ChartHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class StyleDialogManager implements IAlertDialogInterface{

	protected Activity activity;
	private MarkerStyleManager markerStyleManager;
	protected  AlertDialog.Builder alertDialogBuilder;
	protected  AlertDialog alertDialog;
	protected int selectedRadioBtnIndex = -1;
	protected RadioButton[] radioBtns;
	
	public StyleDialogManager(Activity pActivity){
		activity = pActivity;
	}
	public StyleDialogManager(Activity pActivity,MarkerStyleManager pMarkerStyleManager){
		
		activity = pActivity;
		markerStyleManager = pMarkerStyleManager;
		
	}
	@SuppressWarnings("deprecation")
	public void showStyleList(int pSelectedSeriesIndex) {
		
		/**Create top layer that places in the dialog*/ 
		final LinearLayout topLinearLayout = new LinearLayout(activity);
		topLinearLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		llp.leftMargin = DialogManager.MARGIN_LEFT_IN_LAYOUT;
		
		/**for saving reference of all radio buttons*/
		radioBtns = new RadioButton[DialogManager.styleNames.length];
		/**create radio group*/
		RadioGroup rg = new RadioGroup(activity);
		rg.setOrientation(RadioGroup.VERTICAL);
		/**set Title on the dialog window*/
		String t = activity.getResources().getString(R.string.chart_styles);
		TextView title = DialogManager.createCustomTextView(activity, t);
		alertDialogBuilder = new AlertDialog.Builder(activity);
		alertDialogBuilder.setCustomTitle(title);
		alertDialogBuilder.setView(topLinearLayout);
		/**create radio buttons*/
		for (int i = 0; i<DialogManager.styleNames.length;i++) {

			radioBtns[i] = new RadioButton(activity);
			radioBtns[i].setText(DialogManager.styleNames[i]);
			rg.addView(radioBtns[i], i);
		}
		/**add scrollView in case, too many series created*/
		ScrollView sv = new ScrollView(activity);
		topLinearLayout.addView(sv);
		sv.addView(rg);
		///
		/**radio button listener*/
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
			
				((AlertDialog)alertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
				for (int i = 0; i < StyleDialogManager.this.radioBtns.length; i++) {
					if(StyleDialogManager.this.radioBtns[i].isChecked()){
						/**save selected index of radio button*/
						StyleDialogManager.this.selectedRadioBtnIndex = i;
						break;
					}
				}
			}
		});
		/** OK button listener*/
		alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				String nameOfStyle = DialogManager.styleNames[StyleDialogManager.this.selectedRadioBtnIndex];
				PointStyle ps = DialogManager.styles[StyleDialogManager.this.selectedRadioBtnIndex];
				/**pass name of style user selected to the MarkerStyleManager for update*/
				StyleDialogManager.this.markerStyleManager.updateRadioButtonText(nameOfStyle,ps);
				
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
		
		 /**save this class reference in ChartActivity*/
		 ChartActivity chartActivity = (ChartActivity) this.activity;
		 chartActivity.currentDialogManager = this;
		 ((AlertDialog)alertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
	}
	@Override
	public void dismissAlertDialog() {
		
		ChartHelper.dismiss(this.alertDialog);
		ChartHelper.dismiss(markerStyleManager.alertDialog);
	
	}
	
}
