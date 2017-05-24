package com.ilhyungkim.indeedcharts.maneger;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ilhyungkim.indeedcharts.ChartActivity;
import com.ilhyungkim.indeedcharts.R;
import com.ilhyungkim.indeedcharts.interfaces.IAlertDialogInterface;
import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;
import com.ilhyungkim.indeedcharts.utilities.ChartHelper;

public class SeriesValueChangeManager extends DialogManager implements
		IAlertDialogInterface {

	private Activity activity;
	private ArrayList<IDrawingChartInfo> seriesList;
	private LinearLayout topLinearLayout;
	
	public SeriesValueChangeManager(Activity activity){
		this.activity = activity;
	}
	
	public void show(ArrayList<IDrawingChartInfo> seriesList){
		
		this.seriesList = seriesList;
		topLinearLayout = new LinearLayout(this.activity);
		final double minValue = seriesList.get(0).getMinY();
		final double maxValue = seriesList.get(0).getMaxY();
		
		
		topLinearLayout.setOrientation(LinearLayout.VERTICAL);
		/**
		 * set Title on the dialog window
		 */
		TextView title = createCustomTextView(activity, activity.getResources().getString(R.string.chart_change_values));
		
		alertDialogBuilder = new AlertDialog.Builder(activity);
		alertDialogBuilder.setCustomTitle(title);
		//Now topLinearLayout is included in Dialog 
		alertDialogBuilder.setView(topLinearLayout);
		
		ScrollView scrollView = new ScrollView(activity);
		topLinearLayout.addView(scrollView);
		
		/**Create TableLayout*/
		TableLayout tableLayout = new TableLayout(activity);
		
		/**make all columns stretchable*/
		tableLayout.setStretchAllColumns(true);
		/**add this TableLayout in ScrollView*/
		scrollView.addView(tableLayout);
		
		//add all EditTexts that create in loop below
		final ArrayList<EditText> allEditTexts = new ArrayList<EditText>();
		
		/**iterate seriesList that contains current series names and their EditTexts*/
		for (IDrawingChartInfo iDrawingChartInfo : seriesList) {
			
			TableRow tableRow = new TableRow(activity);
			//Display current series name(s)
			TextView currentSeries = new TextView(activity);
			currentSeries.setGravity(Gravity.CENTER_HORIZONTAL);
			currentSeries.setText(iDrawingChartInfo.getSeriesName());
			currentSeries.setTextScaleX(TEXT_SCALE);
			currentSeries.setTextColor(iDrawingChartInfo.getColorPicked());
			
			//Create EditText for new series name user types in
			final EditText value_et = new EditText(activity);
			value_et.requestFocus();
			ChartHelper.setEditTextMaxLength(value_et, MAX_LENGTH_SERIES_VALUES);
			ChartHelper.setInputType(value_et);
			String stringValue = String.valueOf(iDrawingChartInfo.getValue());
			value_et.setText(stringValue);
			value_et.setSelection(value_et.getText().length());
			//add all EditTexts created through this loop
			allEditTexts.add(value_et);
			tableRow.addView(currentSeries);
			tableRow.addView(value_et);
			tableLayout.addView(tableRow);
			
		}//End for
		
		alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				try {
					
					
					for(int i = 0; i<allEditTexts.size();i++){
						
						double newValue = Double.parseDouble(allEditTexts.get(i).getText().toString().trim());
						if(newValue >= minValue && newValue <= maxValue )
						{
							ArrayList<IDrawingChartInfo> seriesList = SeriesValueChangeManager.this.seriesList;
							seriesList.get(i).setValue(Math.floor(newValue));
							
							
						}else{
							ToastMessageManager.show(activity, "Updating has failed becaue the value is not in a set of current minimum("+minValue+")" +
									" and maximum("+maxValue+") values", Toast.LENGTH_LONG);
							
						}
					}
					
				} catch (NumberFormatException e) {
					
					e.printStackTrace();
				}
				
				
				Intent intent = createIntent(INTENT_ACTION_EDIT_CHART);
				activity.sendBroadcast(intent);
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
