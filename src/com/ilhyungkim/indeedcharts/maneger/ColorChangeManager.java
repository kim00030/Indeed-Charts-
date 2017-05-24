package com.ilhyungkim.indeedcharts.maneger;

import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class ColorChangeManager implements IAlertDialogInterface{
	
	private Activity activity;
	private  AlertDialog.Builder alertDialogBuilder;
	private  AlertDialog alertDialog;
	private int[] selectedColors;
	private int selectedRadioBtnIndex = -1;
	private RadioButton[] radioBtns;
	private ArrayList<IDrawingChartInfo> seriesList;
	private AmbilWarnaDialog dialog;
	
	public ColorChangeManager(Activity pActivity){
		
		this.activity = pActivity;
	}
	/***
	 * This method will be open up color picker accordingly
	 * @param pSeriesList ArrayList that contains information of current series 
	 */
	public void showEditColorOfLines(ArrayList<IDrawingChartInfo> pSeriesList){
		
		ChartHelper.dismiss(alertDialog);
		seriesList = pSeriesList;
		/**Create top layer that places in the dialog*/ 
		final LinearLayout topLinearLayout = new LinearLayout(activity);
		topLinearLayout.setOrientation(LinearLayout.VERTICAL);
		
		/**for saving selected colors for radio buttons*/
		selectedColors = new int[seriesList.size()];
		/**for saving reference of all radio buttons*/
		radioBtns = new RadioButton[seriesList.size()];
		/**create radio group*/
		RadioGroup rg = new RadioGroup(activity);
		rg.setOrientation(RadioGroup.VERTICAL);
		/**set Title on the dialog window*/
		String t = activity.getResources().getString(R.string.chart_change_color_chart);
		TextView title = DialogManager.createCustomTextView(activity, t);
		alertDialogBuilder = new AlertDialog.Builder(activity);
		alertDialogBuilder.setCustomTitle(title);
		alertDialogBuilder.setView(topLinearLayout);
		/**create radio buttons*/
		for (int i = 0; i<seriesList.size();i++) {
			
			final LinearLayout subLayout = new LinearLayout(activity);
			subLayout.setOrientation(LinearLayout.HORIZONTAL);
			selectedColors[i] = seriesList.get(i).getColorPicked();
			radioBtns[i] = new RadioButton(activity);
			radioBtns[i].setText(seriesList.get(i).getSeriesName());
			radioBtns[i].setTextColor(seriesList.get(i).getColorPicked());
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
				
				for (int i = 0; i < ColorChangeManager.this.radioBtns.length; i++) {
					if(ColorChangeManager.this.radioBtns[i].isChecked()){
						/**save selected btn's index*/
						ColorChangeManager.this.selectedRadioBtnIndex = i;
						break;
					}
				}
				int index = ColorChangeManager.this.selectedRadioBtnIndex;
				ArrayList<IDrawingChartInfo> seriesList = ColorChangeManager.this.seriesList;
				int currentColor = seriesList.get(index).getColorPicked();
				openColorpicker(currentColor);//0xff0000ff
			}
		});
		/** OK button listener*/
		 alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				 /**Change selected radio button's text color*/
					for (int i = 0; i < ColorChangeManager.this.radioBtns.length; i++) {
						int colorPicked = ColorChangeManager.this.selectedColors[i];	
						ColorChangeManager.this.seriesList.get(i).setColorPicked(colorPicked);
					
					}
					Intent intent = DialogManager.createIntent(DialogManager.INTENT_ACTION_COLOR_PICK_FOR_SERIES);
					
					ColorChangeManager.this.activity.sendBroadcast(intent);
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
	}
	
	public  void openColorpicker(int defaultColor) {

		//     initialColor is the initially-selected color to be shown in the rectangle on the left of the arrow.
      //     for example, 0xff000000 is black, 0xff0000ff is blue. Please be aware of the initial 0xff which is the alpha.
		dialog = new AmbilWarnaDialog(activity, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
			
			@Override
			public void onOk(AmbilWarnaDialog dialog, int color) {
				
				 /**Change selected radio button's text color*/
				for (int i = 0; i < ColorChangeManager.this.radioBtns.length; i++) {
						
					if(i == ColorChangeManager.this.selectedRadioBtnIndex){
						/**change radio button's text color*/
						ColorChangeManager.this.radioBtns[i].setTextColor(color);
						/**save selected colors*/
						ColorChangeManager.this.selectedColors[i] = color;
					}
				}
				dismissColorPicker();
			}
			
			@Override
			public void onCancel(AmbilWarnaDialog dialog) {
				dismissColorPicker();
			}
		});
		 dialog.show();
		 
	}
	@Override
	public void dismissAlertDialog() {
		ChartHelper.dismiss(alertDialog);
		dismissColorPicker();
	}
	/***
	 * Method to dismiss color picker dialog
	 */
	public void dismissColorPicker(){
		
		if (dialog != null) {
			
			try {
				dialog.getDialog().dismiss();
				dialog = null;
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}

	}
}
