package com.ilhyungkim.indeedcharts.maneger;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ilhyungkim.indeedcharts.ChartActivity;
import com.ilhyungkim.indeedcharts.R;
import com.ilhyungkim.indeedcharts.components.AddRowButton;
import com.ilhyungkim.indeedcharts.components.ItemChooser;
import com.ilhyungkim.indeedcharts.components.RemoveRowButton;
import com.ilhyungkim.indeedcharts.interfaces.IAlertDialogInterface;
import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;
import com.ilhyungkim.indeedcharts.interfaces.IManagerInterface;
import com.ilhyungkim.indeedcharts.model.SeriesData;
import com.ilhyungkim.indeedcharts.utilities.ChartHelper;

public class XYValueChangeManagerWithAllowXAsString implements IManagerInterface,IAlertDialogInterface{
	
	/**id for EditText for x and y */
	private static final int X_VALUE = 0;
	private static final int Y_VALUE = 1;
	private Activity activity;
	private ArrayList<IDrawingChartInfo> seriesList;
	private  AlertDialog.Builder alertDialogBuilder;
	private  AlertDialog alertDialog;
	private AddRowButton addRowButton;
	private TableLayout tableLayout;
	
	private TableRow tableRow;
	private ScrollView scrollView;
	private ArrayList<EditText> xValues = new ArrayList<>();
	private ArrayList<EditText> yValues = new ArrayList<>();
	private ArrayList<TableRow> tableRows = new ArrayList<>();
	
	private EditText editTextForX;
	private EditText editTextForY;

	private ItemChooser seriesChooser;
	private int currentSeriesIndex = -1;
	
	public XYValueChangeManagerWithAllowXAsString(Activity pActivity){
		
		activity = pActivity;
	}
	public void init(ArrayList<IDrawingChartInfo> pSeriesList){
		
		this.seriesList = pSeriesList;
		
		/**if series is only one, skip series chooser dialog*/
		if(this.seriesList.size() == 1){
			showXYValueChangeDialog(this.seriesList.size()-1);
			
		}else{
			
			if(seriesChooser != null){
				seriesChooser = null;
			}
			seriesChooser = new ItemChooser(this.activity, this,this.seriesList,"Choose series");
			seriesChooser.createDialog();
		}
		
	}
	
	public void showXYValueChangeDialog(int seriesIndex){
		
		ChartHelper.dismiss(alertDialog);
		this.currentSeriesIndex = seriesIndex;
		initialzie();
		scrollView = new ScrollView(activity);
		
		/**Create top layer that places in the dialog*/
		final LinearLayout topLinearLayout = new LinearLayout(activity);
		topLinearLayout.setOrientation(LinearLayout.VERTICAL);

		/**set Title on the dialog window*/
		TextView title = DialogManager.createCustomTextView(activity, this.seriesList.get(this.currentSeriesIndex).getSeriesName());
		alertDialogBuilder = new AlertDialog.Builder(activity);
		alertDialogBuilder.setCustomTitle(title);
		alertDialogBuilder.setView(topLinearLayout);
		
		/**create add button*/
		addRowButton = new AddRowButton(activity, this);
		topLinearLayout.addView(addRowButton);
		/**scroll view for x and y fields*/
		topLinearLayout.addView(scrollView);
		/**Create TableLayout*/
		tableLayout = new TableLayout(activity);
		
		/**make all columns stretchable*/
		tableLayout.setStretchAllColumns(true);
		/**add this TableLayout in ScrollView*/
		scrollView.addView(tableLayout);
		/**Create First Row as Header*/
		TableRow headers = new TableRow(activity);
		/**set color in light pink*/
		headers.setBackgroundResource(R.drawable.xy_value_change_menu_bar);
		TextView headerX = new TextView(activity);
		headerX.setText("X-Axsis");
		headerX.setGravity(Gravity.CENTER_HORIZONTAL);
		TextView headerY = new TextView(activity);
		headerY.setGravity(Gravity.CENTER_HORIZONTAL);
		headerY.setText("Y-Axsis");
		
		headers.addView(headerX);
		headers.addView(headerY);
		tableLayout.addView(headers);

		/**add rows for displaying current x & y sets. length for x and y should be same*/
		int xLength = this.seriesList.get(currentSeriesIndex).getX_StringValues().length;
	
		for (int i = 0; i < xLength; i++) {
			
			String x = this.seriesList.get(currentSeriesIndex).getX_StringValueByIndex(i);
			double y = this.seriesList.get(currentSeriesIndex).getYValueByIndex(i);
			
			addRow(x,y);
		}
		/**Update button */
		 alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				String[] resultX = new String[xValues.size()];
				double[] resultY= new double[yValues.size()];
				
				Activity activity = XYValueChangeManagerWithAllowXAsString.this.activity;
				ArrayList<EditText> xValues = XYValueChangeManagerWithAllowXAsString.this.xValues;
				ArrayList<EditText> yValues = XYValueChangeManagerWithAllowXAsString.this.yValues;
				ArrayList<IDrawingChartInfo> seriesList = XYValueChangeManagerWithAllowXAsString.this.seriesList;
				/**assumed xValues and yValuse must be same size*/
				for(int i = 0; i<xValues.size();i++){
					/**get all x values user typed*/
					if(xValues.get(i).getText().toString().trim().length()>0){
						resultX[i] = xValues.get(i).getText().toString().trim();
						
					}
					/**get all y values user typed*/
					if(yValues.get(i).getText().toString().trim().length()>0){
						resultY[i] = Double.parseDouble(yValues.get(i).getText().toString().trim());
					}
				}
			
				int currentSeriesIndex = XYValueChangeManagerWithAllowXAsString.this.currentSeriesIndex;
								
				for (IDrawingChartInfo iDrawingChartInfo : seriesList) {
					iDrawingChartInfo.setX_StringValues(resultX);
				}
				seriesList.get(currentSeriesIndex).setYValues(resultY);
				Intent intent = DialogManager.createIntent(DialogManager.INTENT_ACTION_EDIT_XY_VALUES);
				activity.sendBroadcast(intent);
				

			}
		});
		 /**cancel button*/
		 alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(alertDialog != null){
					alertDialog.dismiss();
				}
				
				/**If series is more than one, */
				int numSeries = XYValueChangeManagerWithAllowXAsString.this.seriesList.size();
				if(numSeries >1){
					/** references */
					Activity activity =  XYValueChangeManagerWithAllowXAsString.this.activity;
					ArrayList<IDrawingChartInfo> seriesList =  XYValueChangeManagerWithAllowXAsString.this.seriesList;
					ItemChooser seriesChooser =  XYValueChangeManagerWithAllowXAsString.this.seriesChooser;
					
					if(seriesChooser != null){
						seriesChooser = null;
					}
					
					seriesChooser = new ItemChooser(activity, XYValueChangeManagerWithAllowXAsString.this,seriesList,"Choose series");
					seriesChooser.createDialog();
					
				}
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
		 /** Update button's status if they can be enabled or not*/
		 updateButtonStatus();
	}


	private void initialzie() {
		
		if(xValues != null ){
			 xValues = new ArrayList<>();
		}
		if(yValues != null ){
			yValues = new ArrayList<>();
		}
		if(tableRows != null ){
			tableRows = new ArrayList<>();
		}
	}
	@Override
	public void addRow(SeriesData sd) {
		
		if(sd == null){
			addRow(null,0.0d);
		}
		
	}
	/***
	 * Method to add Row on table for X and Y value
	 * 
	 */
	public void addRow(String x, double y){
	
		/**Create TableRow*/
		tableRow = new TableRow(activity);
		
		/**add the table row to the ArrayList*/
		tableRows.add(tableRow);
		/**Create EditText for x value*/
		editTextForX = new EditText(activity);
		ChartHelper.setEditTextMaxLength(editTextForX, DialogManager.MAX_LENGTH_XY_VALUES);
		editTextForX.requestFocus();
		if(x != null){
			editTextForX.setText(x);
		}
		//ChartHelper.setInputType(editTextForX);
		/**add x value in arrayList*/
		xValues.add(editTextForX);
		editTextForX.setId(X_VALUE);
		
		addEditTextListenerForXY(editTextForX);
		/**Create EditText for y value*/
		editTextForY = new EditText(activity);
		ChartHelper.setEditTextMaxLength(editTextForY, DialogManager.MAX_LENGTH_XY_VALUES);

		if(y != 0.0d){
			
			editTextForY.setText(ChartHelper.numberFormatted(y));
		}
		
		
		ChartHelper.setInputType(editTextForY);
		/**add y value in arrayList*/
		yValues.add(editTextForY);
		editTextForY.setId(Y_VALUE);
		addEditTextListenerForXY(editTextForY);
		
		/**Create Remove button*/
		RemoveRowButton removeBtn = new RemoveRowButton(activity,this,tableRow,editTextForX,editTextForY);
		/**Each TableRow has 2 EditTexts and 1 Remove button*/
		tableRow.addView(editTextForX);
		tableRow.addView(editTextForY);
		tableRow.addView(removeBtn);
	
		refreshDisplay();
		 /** Update button's status if they can be enabled or not*/
		updateButtonStatus();
		
	}
	private void addEditTextListenerForXY(EditText pEditText) {
		
		final EditText editText = pEditText;
		
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

				/**if user's input is '+' , set the empty string*/
				if(s.toString().compareTo("+")==0 ){
					
					editText.setText(null);
					return;
					
				}
				updateButtonStatus();
			}
		});
	}

	public void refreshDisplay(){
		
		tableLayout.addView(tableRow);
		
	}
	
	public void removeRow(TableRow tr, EditText editTextForX,EditText editTextForY){
		
		/**Remove current row by associated remove button in Table*/
		for (int q = 0; q < tableLayout.getChildCount(); q++) {
			if(tableLayout.getChildAt(q).equals(tr)){
				tableLayout.removeView(tr);
			}
		}
		/**Remove current TableRow created in arrayList*/
		for(int i = 0; i< tableRows.size();i++){
			
			if(tableRows.get(i).equals(tr)){
				
				tableRows.remove(tr);
				break;
			}
		
		}
		if(tableRows.size() <=0){
			DialogManager.alertDialogButtonEnabled(alertDialog,false);
			
		}
		/**Remove current EditText for X saved in arrayList*/
		for (int k = 0; k < xValues.size(); k++) {
			
			if(xValues.get(k).equals(editTextForX)){
				
				xValues.remove(editTextForX);
				break;
			}
		}
		/**Remove current EditText for Y saved in arrayList*/
		for (int j = 0; j < yValues.size(); j++) {
			if(yValues.get(j).equals(editTextForY)){
				yValues.remove(editTextForY);
				break;
			}
		}
		 /** Update button's status if they can be enabled or not*/
		updateButtonStatus();
	}
	
	/***
	 *  Update button's status if they can be enabled or not
	 */
	public void updateButtonStatus(){
		
		/** Enable Update button*/
		DialogManager.alertDialogButtonEnabled(alertDialog,true);
		/**Enable add button */
		addRowButton.buttonEnabled(true);
		
		/**Disable update button and add button when any EditText has empty */
		if(xValues != null){
			for (EditText editText_X : xValues) {
				
				if(editText_X.getText().toString().trim().length() == 0 ){
					
					DialogManager.alertDialogButtonEnabled(alertDialog,false);
					addRowButton.buttonEnabled(false);

				}
			}
		}
		if(yValues != null){
			for (EditText editText_Y : yValues) {
				
				if(editText_Y.getText().toString().trim().length() == 0 ){
					
					DialogManager.alertDialogButtonEnabled(alertDialog,false);
					addRowButton.buttonEnabled(false);
				}
			}
		}
		
		/**Disable Update button when there's no table row created*/
		if(tableRows != null && tableRows.size() <= 0){
			DialogManager.alertDialogButtonEnabled(alertDialog,false);
		}
		
	}
	@Override
	public void dismissAlertDialog() {
		ChartHelper.dismiss(alertDialog);		
	}


}
