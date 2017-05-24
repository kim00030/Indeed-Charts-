package com.ilhyungkim.indeedcharts.maneger;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ilhyungkim.indeedcharts.ChartActivity;
import com.ilhyungkim.indeedcharts.R;
import com.ilhyungkim.indeedcharts.interfaces.IAlertDialogInterface;
import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;
import com.ilhyungkim.indeedcharts.utilities.ChartHelper;

public class XYAxisPropertyManager implements IAlertDialogInterface{
	
	private Activity activity;
	private List<IDrawingChartInfo> seriesList;
	private  AlertDialog.Builder alertDialogBuilder;
	private  AlertDialog alertDialog;
	
	private TableLayout tableLayout;
	
	private EditText xMin_editText;
	private EditText xMax_editText;
	private EditText yMin_editText;
	private EditText yMax_editText;
	private EditText xName_editText;
	private EditText yName_editText;
	
	public XYAxisPropertyManager(Activity activity){
		
		this.activity = activity;
	}
	public void showOnlyXYNameChanges(List<IDrawingChartInfo> seriesList){
		
		this.seriesList = seriesList;
		
		/**Create top layer that places in the dialog*/
		final LinearLayout topLinearLayout = new LinearLayout(activity);
		topLinearLayout.setOrientation(LinearLayout.VERTICAL);

		/**set Title on the dialog window*/
		String t = activity.getResources().getString(R.string.chart_change_xy_axis);
		TextView title = DialogManager.createCustomTextView(activity, t);
		alertDialogBuilder = new AlertDialog.Builder(activity);
		alertDialogBuilder.setCustomTitle(title);
		alertDialogBuilder.setView(topLinearLayout);
		///////////////////////////////////
		
		/**Create table */
		tableLayout = new TableLayout(activity);
		tableLayout.setStretchAllColumns(true);
		
		/**Header for "Name of X-axis" and Y-axis*/
		TableRow headers1 = new TableRow(activity);
		/**set color in light pink*/
		headers1.setBackgroundResource(R.drawable.xy_value_change_menu_bar);
		TextView nameX = new TextView(activity);
		nameX.setGravity(Gravity.CENTER_HORIZONTAL);
		nameX.setText("Name of X-axis");
		TextView nameY = new TextView(activity);
		nameY.setGravity(Gravity.CENTER_HORIZONTAL);
		nameY.setText("Name of Y-axis");
		headers1.addView(nameX);
		headers1.addView(nameY);
		
		/** EditText for name of X and Y*/
		TableRow xyNameEditTexts = new TableRow(activity);
		xName_editText = new EditText(activity);
		ChartHelper.setEditTextMaxLength(xName_editText, DialogManager.MAX_LENGTH_XY_AXIS);
		xyNameEditTexts.addView(xName_editText);
		yName_editText = new EditText(activity);
		ChartHelper.setEditTextMaxLength(yName_editText, DialogManager.MAX_LENGTH_XY_AXIS);
		xyNameEditTexts.addView(yName_editText);
		
		/************Show current name on x-axis and y-axis set previously*****/
		xName_editText.setText(this.seriesList.get(0).getXaxis_name());
		yName_editText.setText(this.seriesList.get(0).getYaxis_name());
		
		tableLayout.addView(headers1);
		tableLayout.addView(xyNameEditTexts);
		topLinearLayout.addView(tableLayout);
		
		/**Update button*/
		alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				String nameX = xName_editText.getText().toString().trim();
				String nameY = yName_editText.getText().toString().trim();
				
			
				for (IDrawingChartInfo iDrawingChartInfo : XYAxisPropertyManager.this.seriesList) {
					iDrawingChartInfo.setXaxis_name(nameX);
					iDrawingChartInfo.setYaxis_name(nameY);
					
				}
				Intent intent = new Intent(DialogManager.INTENT_ACTION_EDIT_XY_NAME);
				XYAxisPropertyManager.this.activity.sendBroadcast(intent);
				ChartHelper.dismiss(alertDialog);
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
		ChartActivity chartActivity = (ChartActivity) this.activity;
		chartActivity.currentDialogManager = this;
	}
	
	public void show(List<IDrawingChartInfo> seriesList){
		
		this.seriesList = seriesList;
		
		/**Create top layer that places in the dialog*/
		final LinearLayout topLinearLayout = new LinearLayout(activity);
		topLinearLayout.setOrientation(LinearLayout.VERTICAL);

		/**set Title on the dialog window*/
		String t = activity.getResources().getString(R.string.chart_change_xy_axis);
		TextView title = DialogManager.createCustomTextView(activity, t);
		alertDialogBuilder = new AlertDialog.Builder(activity);
		alertDialogBuilder.setCustomTitle(title);
		alertDialogBuilder.setView(topLinearLayout);
		///////////////////////////////////
		
		/**Create table */
		tableLayout = new TableLayout(activity);
		tableLayout.setStretchAllColumns(true);
		
		/**Header for "Name of X-axis" and Y-axis*/
		TableRow headers1 = new TableRow(activity);
		/**set color in light pink*/
		headers1.setBackgroundResource(R.drawable.xy_value_change_menu_bar);
		TextView nameX = new TextView(activity);
		nameX.setGravity(Gravity.CENTER_HORIZONTAL);
		nameX.setText("Name of X-axis");
		TextView nameY = new TextView(activity);
		nameY.setGravity(Gravity.CENTER_HORIZONTAL);
		nameY.setText("Name of Y-axis");
		headers1.addView(nameX);
		headers1.addView(nameY);
		
		/** EditText for name of X and Y*/
		TableRow xyNameEditTexts = new TableRow(activity);
		xName_editText = new EditText(activity);
		ChartHelper.setEditTextMaxLength(xName_editText, DialogManager.MAX_LENGTH_XY_AXIS);
		xyNameEditTexts.addView(xName_editText);
		yName_editText = new EditText(activity);
		ChartHelper.setEditTextMaxLength(yName_editText, DialogManager.MAX_LENGTH_XY_AXIS);
		xyNameEditTexts.addView(yName_editText);
		addListenerForXYNames(xName_editText);
		addListenerForXYNames(yName_editText);
		/************Show current name on x-axis and y-axis set previously*****/
		xName_editText.setText(this.seriesList.get(0).getXaxis_name());
		xName_editText.setSelection(xName_editText.getText().length());
		yName_editText.setText(this.seriesList.get(0).getYaxis_name());
		
		/**Header for StartX and StartY*/
		TableRow headers2 = new TableRow(activity);
		/**set color in light pink*/
		headers2.setBackgroundResource(R.drawable.xy_value_change_menu_bar);
		TextView xMin_tv = new TextView(activity);
		xMin_tv.setText("Start X");
		xMin_tv.setGravity(Gravity.CENTER_HORIZONTAL);
		headers2.addView(xMin_tv);
		TextView xMax_tv = new TextView(activity);
		xMax_tv.setText("End X");
		xMax_tv.setGravity(Gravity.CENTER_HORIZONTAL);
		headers2.addView(xMax_tv);
		
		/**EditText for minX and maxX*/
		TableRow xMinMaxEditTexts = new TableRow(activity);
		xMin_editText = new EditText(activity);
		ChartHelper.setEditTextMaxLength(xMin_editText, DialogManager.MAX_LENGTH_XY_MIN_MAX);
		ChartHelper.setInputType(xMin_editText);
		xMinMaxEditTexts.addView(xMin_editText);
		addListnerForXYMinMax(xMin_editText);
		/***************** show current xMin set previously.*/
		xMin_editText.setText(String.valueOf(this.seriesList.get(0).getMinX()));
		
		/**EditText for Max x*/
		xMax_editText = new EditText(activity);
		ChartHelper.setEditTextMaxLength(xMax_editText, DialogManager.MAX_LENGTH_XY_MIN_MAX);
		ChartHelper.setInputType(xMax_editText);
		xMinMaxEditTexts.addView(xMax_editText);
		addListnerForXYMinMax(xMax_editText);
		/***************** show current xMax set previously.*/
		xMax_editText.setText(String.valueOf(this.seriesList.get(0).getMaxX()));
		
		/**Header for StartY and start Y*/
		TableRow headers3 = new TableRow(activity);
		headers3.setBackgroundResource(R.drawable.xy_value_change_menu_bar);
		TextView yMin_tv = new TextView(activity);
		yMin_tv.setText("Start Y");
		yMin_tv.setGravity(Gravity.CENTER_HORIZONTAL);
		headers3.addView(yMin_tv);
		TextView yMax_tv = new TextView(activity);
		yMax_tv.setText("End Y");
		yMax_tv.setGravity(Gravity.CENTER_HORIZONTAL);
		headers3.addView(yMax_tv);
		
		/**EditText for min y and maxY*/
		TableRow yMinMaxEditTexts = new TableRow(activity);
		yMin_editText = new EditText(activity);
		ChartHelper.setEditTextMaxLength(yMin_editText, DialogManager.MAX_LENGTH_XY_MIN_MAX);
		ChartHelper.setInputType(yMin_editText);
		yMinMaxEditTexts.addView(yMin_editText);
		addListnerForXYMinMax(yMin_editText);
		/***************** show current yMin set previously.*/
		yMin_editText.setText(String.valueOf(this.seriesList.get(0).getMinY()));
		
		/**EditText for Max y*/
		yMax_editText = new EditText(activity);
		ChartHelper.setEditTextMaxLength(yMax_editText, DialogManager.MAX_LENGTH_XY_MIN_MAX);
		ChartHelper.setInputType(yMax_editText);
		yMinMaxEditTexts.addView(yMax_editText);
		addListnerForXYMinMax(yMax_editText);
		/***************** show current yMax set previously.*/
		yMax_editText.setText(String.valueOf(this.seriesList.get(0).getMaxY()));
		
		/**add those rows to the table*/
		tableLayout.addView(headers1);
		tableLayout.addView(xyNameEditTexts);
		tableLayout.addView(headers2);
		tableLayout.addView(xMinMaxEditTexts);
		tableLayout.addView(headers3);
		tableLayout.addView(yMinMaxEditTexts);
		topLinearLayout.addView(tableLayout);
		
		/**Update button*/
		alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				String nameX = xName_editText.getText().toString().trim();
				String nameY = yName_editText.getText().toString().trim();
				
				double xMin = Double.parseDouble(xMin_editText.getText().toString().trim());
				double xMax = Double.parseDouble(xMax_editText.getText().toString().trim());
				double yMin = Double.parseDouble(yMin_editText.getText().toString().trim());
				double yMax = Double.parseDouble(yMax_editText.getText().toString().trim());
				
				for (IDrawingChartInfo iDrawingChartInfo : XYAxisPropertyManager.this.seriesList) {
					iDrawingChartInfo.setXaxis_name(nameX);
					iDrawingChartInfo.setYaxis_name(nameY);
					iDrawingChartInfo.setMinX(xMin);
					iDrawingChartInfo.setMaxX(xMax);
					iDrawingChartInfo.setMinY(yMin);
					iDrawingChartInfo.setMaxY(yMax);
				}
				Intent intent = new Intent(DialogManager.INTENT_ACTION_EDIT_XY_AXIS);
				XYAxisPropertyManager.this.activity.sendBroadcast(intent);
				ChartHelper.dismiss(alertDialog);
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
		 ChartActivity chartActivity = (ChartActivity) this.activity;
		 chartActivity.currentDialogManager = this;
		 updateButtonStatus();
	}
	
	
	private void addListenerForXYNames(EditText et) {
		
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
	private void addListnerForXYMinMax(EditText et) {
		
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
		if(ChartHelper.isEmpty(xMin_editText) || ChartHelper.isEmpty(xMax_editText)|| ChartHelper.isEmpty(yMin_editText)||
				ChartHelper.isEmpty(yMax_editText)	|| ChartHelper.isEmpty(xName_editText)||ChartHelper.isEmpty(yName_editText))	
		{
			
			DialogManager.alertDialogButtonEnabled(alertDialog,false);
			
		}

	}
	@Override
	public void dismissAlertDialog() {
		ChartHelper.dismiss(alertDialog);
	}
}
