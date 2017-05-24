package com.ilhyungkim.indeedcharts.maneger;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ilhyungkim.indeedcharts.ChartActivity;
import com.ilhyungkim.indeedcharts.interfaces.IAlertDialogInterface;
import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;
import com.ilhyungkim.indeedcharts.model.SeriesData;
import com.ilhyungkim.indeedcharts.model.wrappers.DataWrapper;
import com.ilhyungkim.indeedcharts.utilities.ChartHelper;

public class SeriesNameChangeManager extends DialogManager implements IAlertDialogInterface{

	private Activity activity;
	private ArrayList<IDrawingChartInfo> seriesList;
	private LinearLayout topLinearLayout;
	private AlertDialog.Builder alertDialogBuilder;
	private AlertDialog alertDialog;
	
	public SeriesNameChangeManager(Activity activity){
		this.activity = activity;
	}
	public void show(ArrayList<IDrawingChartInfo> seriesList){
		
		ChartHelper.dismiss(alertDialog);
		this.seriesList = seriesList;
		topLinearLayout = new LinearLayout(activity);
		topLinearLayout.setOrientation(LinearLayout.VERTICAL);
		
		/**
		 * set Title on the dialog window
		 */
		String mainTitle = CHANGE_SERIES_NAME;
		TextView title = createCustomTextView(activity, mainTitle);
		
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
		/**Save series Id and new names on it*/
		final ArrayList<SeriesData> seriesDatas = new ArrayList<SeriesData>();
		/**iterate seriesList that contains current series names*/
		for (IDrawingChartInfo iDrawingChartInfo : seriesList) {
			
			TableRow tableRow = new TableRow(activity);
			//Display current series name(s)
			TextView currentSeries = new TextView(activity);
			currentSeries.setText(iDrawingChartInfo.getSeriesName());
			currentSeries.setTextScaleX(TEXT_SCALE);
			currentSeries.setTextColor(iDrawingChartInfo.getColorPicked());
			
			//Create EditText for new series name user types in
			final EditText newSeriesEditText = new EditText(activity);
			//limit the user's input
			ChartHelper.setEditTextMaxLength(newSeriesEditText, MAX_LENGTH_SERIES_NAME);
			//add all EditTexts created through this loop
			allEditTexts.add(newSeriesEditText);
			//add all Series Ids passed through this loop
			SeriesData sd = new SeriesData();
			sd.setSeriesId(iDrawingChartInfo.getId());
			seriesDatas.add(sd);
			//add these layouts to be shown in this dialog
			tableRow.addView(currentSeries);
			
			tableRow.addView(newSeriesEditText);
			tableLayout.addView(tableRow);
		}
		alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				Intent intent = null;
				DataWrapper dataWrapper = new DataWrapper();
				int length = allEditTexts.size();
				for (int i = 0; i < length; i++) {
					//if empty , get current series name
					if(allEditTexts.get(i).getText().toString().trim().length() == 0){
						ArrayList<IDrawingChartInfo> seriesList = SeriesNameChangeManager.this.seriesList;
						seriesDatas.get(i).setNewSeriesName(seriesList.get(i).getSeriesName());
					}else{
						
						//set new series names that user type in and wrap it up in DataWrapper object
						seriesDatas.get(i).setNewSeriesName(allEditTexts.get(i).getText().toString());
					}
					dataWrapper.setSeriesDatas(seriesDatas);
				}
				//send
				intent =  createIntent(INTENT_ACTION_CHANGE_SERIES_NAME);
				intent.putExtra(INTENT_ACTION_CHANGE_SERIES_NAME,dataWrapper);
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
