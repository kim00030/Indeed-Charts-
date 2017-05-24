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
import android.widget.Toast;

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

public class XYValueChangeManager implements IManagerInterface,IAlertDialogInterface{
	
	/**id for EditText for x and y */
	private static final int X_VALUE = 0;
	private static final int Y_VALUE = 1;
	private Activity activity;
	private ArrayList<IDrawingChartInfo> seriesList;
	private  AlertDialog.Builder alertDialogBuilder;
	private  AlertDialog alertDialog;
	private AddRowButton addRowButton;
	private TableLayout tableLayout;
	private TableLayout preTableLayout;
	

	private TableRow tableRow;
	private ScrollView scrollView;
	private ArrayList<EditText> xValues = new ArrayList<>();
	private ArrayList<EditText> yValues = new ArrayList<>();
	private ArrayList<TableRow> tableRows = new ArrayList<>();
	
	private EditText editTextForX;
	private EditText editTextForY;
	
	private EditText xMin_editText;
	private EditText xMax_editText;
	private EditText yMin_editText;
	private EditText yMax_editText;
	
	private ItemChooser seriesChooser;
	private int currentSeriesIndex = -1;
	
	public XYValueChangeManager(Activity pActivity){
		
		activity = pActivity;
	}
	public void init(ArrayList<IDrawingChartInfo> pSeriesList){
		
		this.seriesList = pSeriesList;
		int seriesSize = this.seriesList.size();
		/**if series is only one, skip series chooser dialog*/
		if(seriesSize == 1){
			showXYValueChangeDialog(seriesSize-1);
			
		}else{
			
			if(seriesChooser != null){
				seriesChooser = null;
			}
			seriesChooser = new ItemChooser(this.activity, this,this.seriesList,"Choose series");
			seriesChooser.createDialog();
		}
		
	}
	
	
	public void showXYValueChangeDialog(int seriesIndex){
		
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
		
		/**create bar for xmin & xMax & y min & yMax*/
		preTableLayout = new TableLayout(activity);
		preTableLayout.setStretchAllColumns(true);
		
		TableRow preHeaders = new TableRow(activity);
		preHeaders.setBackgroundResource(R.drawable.xy_value_change_menu_bar);
		
		TextView xMin_tv = new TextView(activity);
		xMin_tv.setGravity(Gravity.CENTER_HORIZONTAL);
		xMin_tv.setText("Start x");
		
		TextView xMax_tv = new TextView(activity);
		xMax_tv.setGravity(Gravity.CENTER_HORIZONTAL);
		xMax_tv.setText("End x");
		
		TextView yMin_tv = new TextView(activity);
		yMin_tv.setGravity(Gravity.CENTER_HORIZONTAL);
		yMin_tv.setText("Start y");
		
		TextView yMax_tv = new TextView(activity);
		yMax_tv.setGravity(Gravity.CENTER_HORIZONTAL);
		yMax_tv.setText("End y");
		preHeaders.addView(xMin_tv);
		preHeaders.addView(xMax_tv);
		preHeaders.addView(yMin_tv);
		preHeaders.addView(yMax_tv);
		
		TableRow minMaxes = new TableRow(activity);
		/**Disabled editable on these xMin,xMax,yMin and yMax, because i want to show their values*/
		xMin_editText = new EditText(activity);
		xMin_editText.setText(String.valueOf(seriesList.get(0).getMinX()));
		xMin_editText.setEnabled(false);
		xMin_editText.setFocusable(false);
		minMaxes.addView(xMin_editText);
		
		xMax_editText = new EditText(activity);
		xMax_editText.setText(String.valueOf(seriesList.get(0).getMaxX()));
		xMax_editText.setEnabled(false);
		xMax_editText.setFocusable(false);
		minMaxes.addView(xMax_editText);
		
		yMin_editText = new EditText(activity);
		yMin_editText.setText(String.valueOf(seriesList.get(0).getMinY()));
		yMin_editText.setEnabled(false);
		yMin_editText.setFocusable(false);
		minMaxes.addView(yMin_editText);
		
		yMax_editText = new EditText(activity);
		yMax_editText.setText(String.valueOf(seriesList.get(0).getMaxY()));
		yMax_editText.setEnabled(false);
		yMax_editText.setFocusable(false);
		minMaxes.addView(yMax_editText);
		
		preTableLayout.addView(preHeaders);
		preTableLayout.addView(minMaxes);
		topLinearLayout.addView(preTableLayout);

///////////////////////////////////////////////////////////////////////////////////////
		addRowButton = new AddRowButton(activity, this);
		topLinearLayout.addView(addRowButton);
		
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

		/**add rows for displaying current x & y sets*/
		int xLength = this.seriesList.get(currentSeriesIndex).getXValues().length;
	
		for (int i = 0; i < xLength; i++) {
			SeriesData sd = new SeriesData();
			sd.setX(this.seriesList.get(currentSeriesIndex).getXValueByIndex(i));
			sd.setY(this.seriesList.get(currentSeriesIndex).getYValueByIndex(i));
			addRow(sd);
		}
		/**place cursor at the end of 1st EditText field*/
		if(xValues != null & xValues.size()>0){
			
			EditText xValue = xValues.get(0);
			xValue.setSelection(xValue.getText().length());
		}
		///END Headers//////
		
		/**Update button */
		 alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				double[] resultX = new double[xValues.size()];
				double[] resultY= new double[yValues.size()];
				/**assumed xValues and yValuse must be same size*/
				for(int i = 0; i<xValues.size();i++){
					
					if(xValues.get(i).getText().toString().trim().length()>0){
						resultX[i] = Double.parseDouble(xValues.get(i).getText().toString());
					}
					if(yValues.get(i).getText().toString().trim().length()>0){
						resultY[i] = Double.parseDouble(yValues.get(i).getText().toString());
					}
				}
			
				
				Activity activity = XYValueChangeManager.this.activity;
				ArrayList<IDrawingChartInfo> seriesList = XYValueChangeManager.this.seriesList;
				int currentSeriesIndex = XYValueChangeManager.this.currentSeriesIndex;
				seriesList.get(currentSeriesIndex).setXValues(resultX);
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
				int numSeries = XYValueChangeManager.this.seriesList.size();
				if(numSeries >1){
					/** references */
					Activity activity =  XYValueChangeManager.this.activity;
					ArrayList<IDrawingChartInfo> seriesList =  XYValueChangeManager.this.seriesList;
					ItemChooser seriesChooser =  XYValueChangeManager.this.seriesChooser;
					
					if(seriesChooser != null){
						seriesChooser = null;
					}
					
					seriesChooser = new ItemChooser(activity, XYValueChangeManager.this,seriesList,"Choose series");
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
	public void addRow(String x, double y) {}
	/***
	 * Method to add Row on table for X and Y value
	 */
	public void addRow(SeriesData sd){
	
		/**Create TableRow*/
		tableRow = new TableRow(activity);
		
		/**add the table row to the ArrayList*/
		tableRows.add(tableRow);
		/**Create EditText for x value*/
		editTextForX = new EditText(activity);
		ChartHelper.setEditTextMaxLength(editTextForX, DialogManager.MAX_LENGTH_XY_VALUES);
		editTextForX.requestFocus();
		if(sd != null){
		
			editTextForX.setText(ChartHelper.numberFormatted(sd.getX()));
		}
		ChartHelper.setInputType(editTextForX);
		/**add x value in arrayList*/
		xValues.add(editTextForX);
		editTextForX.setId(X_VALUE);
		
		addEditTextListenerForXY(editTextForX);
		/**Create EditText for y value*/
		editTextForY = new EditText(activity);
		ChartHelper.setEditTextMaxLength(editTextForY, DialogManager.MAX_LENGTH_XY_VALUES);
		if(sd != null){
			editTextForY.setText(ChartHelper.numberFormatted(sd.getY()));
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
				
				if(editText_X.getText().toString().trim().length() == 0 || !isValueInRange(editText_X)){
					
					DialogManager.alertDialogButtonEnabled(alertDialog,false);
					addRowButton.buttonEnabled(false);

				}
			}
		}
		if(yValues != null){
			for (EditText editText_Y : yValues) {
				
				if(editText_Y.getText().toString().trim().length() == 0 || !isValueInRange(editText_Y)){
					
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
	/***
	 * 
	 * @param et EditText for x and y coordinates that user input
	 * @return true or false if the coordinates that user inputs is valid in the range
	 */
	public boolean isValueInRange(EditText et){
		
		try {
			double xMin = Double.parseDouble(XYValueChangeManager.this.xMin_editText.getText().toString().trim());
			double xMax = Double.parseDouble(XYValueChangeManager.this.xMax_editText.getText().toString().trim());
			double yMin = Double.parseDouble(XYValueChangeManager.this.yMin_editText.getText().toString().trim());
			double yMax = Double.parseDouble(XYValueChangeManager.this.yMax_editText.getText().toString().trim());
			boolean b = true;
			double input = Double.parseDouble(et.getText().toString().trim());
			String msg;
			
			switch(et.getId()){
				case X_VALUE:
					
					if(input>= xMin && input <= xMax){
						
						//ToastMessageManager.show(activity, input + " is valid as x cordinate.", Toast.LENGTH_SHORT);
					}else{
						
						msg = et.getText().toString().trim()+ " is Not in the range beween "+xMin + " and "+xMax;
						ToastMessageManager.show(activity, msg, Toast.LENGTH_SHORT);
						b = false;
					}
					break;
					
				case Y_VALUE:
					if(input>=yMin && input <= yMax){
						//ToastMessageManager.show(activity, input + " is valid as y cordinate.", Toast.LENGTH_SHORT);
					}else{
						msg = et.getText().toString().trim()+ " is Not in the range beween "+yMin + " and "+yMax;
						ToastMessageManager.show(activity, msg, Toast.LENGTH_SHORT);
						b = false;
					}
					break;
	
			};
				
			return b;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public void dismissAlertDialog() {
		ChartHelper.dismiss(alertDialog);
		
	}
	
}
