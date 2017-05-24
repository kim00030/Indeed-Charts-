package com.ilhyungkim.indeedcharts.maneger;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ilhyungkim.indeedcharts.R;
import com.ilhyungkim.indeedcharts.constantdata.ChartConstantData;
import com.ilhyungkim.indeedcharts.drawinghelper.sampledata.SampleDataCreator;
import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;
import com.ilhyungkim.indeedcharts.model.DialChartDataModel;
import com.ilhyungkim.indeedcharts.utilities.ChartHelper;

public class MinMaxValueChangeManager {
	
	private Activity activity;
	private List<IDrawingChartInfo> seriesList;
	private  AlertDialog.Builder alertDialogBuilder;
	private  AlertDialog alertDialog;
	private EditText minValue_et;
	private EditText maxValue_et;
	private TableLayout tableLayout;
	
	public MinMaxValueChangeManager(Activity activity){
		
		this.activity = activity;
		
	}
	public void show(List<IDrawingChartInfo> seriesList){
		
		this.seriesList = seriesList;
		/**Create top layer that places in the dialog*/
		final LinearLayout topLinearLayout = new LinearLayout(activity);
		topLinearLayout.setOrientation(LinearLayout.VERTICAL);
		
		/**set Title on the dialog window*/
		String t = activity.getResources().getString(R.string.chart_change_min_max_values);
		TextView title = DialogManager.createCustomTextView(activity, t);
		alertDialogBuilder = new AlertDialog.Builder(activity);
		alertDialogBuilder.setCustomTitle(title);
		alertDialogBuilder.setView(topLinearLayout);
		//////////////////////////////////////////
		/**Create table */
		tableLayout = new TableLayout(activity);
		tableLayout.setStretchAllColumns(true);
		
		TableRow headers1 = new TableRow(activity);
		/**set color in light pink*/
		headers1.setBackgroundResource(R.drawable.xy_value_change_menu_bar);
		TextView minValue_tv = new TextView(activity);
		minValue_tv.setGravity(Gravity.CENTER_HORIZONTAL);
		minValue_tv.setText("Minimum value");
		TextView maxValue_tv = new TextView(activity);
		maxValue_tv.setGravity(Gravity.CENTER_HORIZONTAL);
		maxValue_tv.setText("Maximum value");
		headers1.addView(minValue_tv);
		headers1.addView(maxValue_tv);
		
		/**EditText for min and max values*/
		TableRow xMinMaxEditTexts = new TableRow(activity);
		minValue_et = new EditText(activity);
		ChartHelper.setInputType(minValue_et);
		ChartHelper.setEditTextMaxLength(minValue_et, DialogManager.MAX_LENGTH_XY_MIN_MAX);
		xMinMaxEditTexts.addView(minValue_et);
		addListnerForMinMax(minValue_et);
		/***************** show current xMin set previously.*/
		minValue_et.setText(String.valueOf(this.seriesList.get(0).getMinY()));
		minValue_et.setSelection(minValue_et.getText().length());
		/**EditText for Max x*/
		maxValue_et = new EditText(activity);
		ChartHelper.setEditTextMaxLength(maxValue_et, DialogManager.MAX_LENGTH_XY_MIN_MAX);
		ChartHelper.setInputType(maxValue_et);
		xMinMaxEditTexts.addView(maxValue_et);
		addListnerForMinMax(maxValue_et);
		/***************** show current xMax set previously.*/
		maxValue_et.setText(String.valueOf(this.seriesList.get(0).getMaxY()));
		
		/**add those rows to the table*/
		tableLayout.addView(headers1);
		tableLayout.addView(xMinMaxEditTexts);
		
		topLinearLayout.addView(tableLayout);
		
		/**Update button*/
		alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				boolean needToReValue = false;
				try {
					double minValue = Double.parseDouble(minValue_et.getText().toString().trim());
					double maxValue = Double.parseDouble(maxValue_et.getText().toString().trim());
					
					for (IDrawingChartInfo iDrawingChartInfo : MinMaxValueChangeManager.this.seriesList) {
						
						double currentValue = ((DialChartDataModel)iDrawingChartInfo).getValue();
						if(currentValue > maxValue){
							Bundle bundle = SampleDataCreator.generateSampleValueByRange(minValue, maxValue);
							double newValue = bundle.getDouble(ChartConstantData.VALUES);
							((DialChartDataModel)iDrawingChartInfo).setValue(newValue);
							needToReValue = true;
						}
						iDrawingChartInfo.setMinY(minValue);
						iDrawingChartInfo.setMaxY(maxValue);
					}
					
					if(needToReValue){
						ToastMessageManager.show(activity, "Category Value has modified because the values were larger " +
								"than the new set of range", Toast.LENGTH_LONG);
					}
					Intent intent = new Intent(DialogManager.INTENT_ACTION_EDIT_MIN_MAX_VALUES);
					MinMaxValueChangeManager.this.activity.sendBroadcast(intent);
					ChartHelper.dismiss(alertDialog);
				} catch (NumberFormatException e) {
					
					e.printStackTrace();
				}
			}
		});
		/**Cancel button*/
		alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				ChartHelper.dismiss(alertDialog);
				
			}
		});
		
		
		
		//////////////////////////////////////
		alertDialog = alertDialogBuilder.create();
		
		/**
		* disable dismissing ability when click on outside of dialog
		*/
		//alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();

	}
	
	private void addListnerForMinMax(EditText et) {
	
		final EditText editText = et; 
		
		editText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				 /** Update button's status if they can be enabled or not*/
				updateButtonStatus();
			}
		});
	
	}
	private void updateButtonStatus() {
		
		DialogManager.alertDialogButtonEnabled(alertDialog,true);
		
		try {
			if(ChartHelper.isEmpty(minValue_et) || ChartHelper.isEmpty(maxValue_et))	
			{
				DialogManager.alertDialogButtonEnabled(alertDialog,false);
				
			}
		} catch (NumberFormatException e) {
			
			e.printStackTrace();
		}
		
	}
	
}
