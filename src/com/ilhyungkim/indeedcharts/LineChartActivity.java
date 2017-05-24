package com.ilhyungkim.indeedcharts;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ilhyungkim.indeedcharts.drawinghelper.LineChartDrawingHelper;
import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;
import com.ilhyungkim.indeedcharts.maneger.DialogManager;
import com.ilhyungkim.indeedcharts.maneger.MarkerStyleManager;
import com.ilhyungkim.indeedcharts.maneger.SavableDataManager;
import com.ilhyungkim.indeedcharts.maneger.XYValueChangeManager;
import com.ilhyungkim.indeedcharts.model.ChartData;
import com.ilhyungkim.indeedcharts.model.LineChartDataModel;
import com.ilhyungkim.indeedcharts.model.wrappers.DataWrapper;

public class LineChartActivity extends ChartActivity {

	private IDrawingChartInfo lineChartDataModel;
	private LineChartDrawingHelper lineChartDrawingHelper;

	private MarkerStyleManager markerStyleManager = new MarkerStyleManager(this);
	private XYValueChangeManager xyValueChangeManager = new XYValueChangeManager(this);
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		
		super.onRestoreInstanceState(savedInstanceState);
		 
//		 lineChartDataModel = (LineChartDataModel) savedInstanceState.getSerializable("current_LineChartDataModel");
//		 currentMenuOpen_Id = (Integer)savedInstanceState.getSerializable("current_ currentMenuOpen_Id"); 
//		 list = (ArrayList<IDrawingChartInfo>)savedInstanceState.getSerializable("current_ list"); 
//		 currentDataset = (XYMultipleSeriesDataset) savedInstanceState.getSerializable("current_ currentDataset"); 
//		 renderer =  (XYMultipleSeriesRenderer) savedInstanceState.getSerializable("current_ renderer"); 
//	 
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		super.onSaveInstanceState(outState);

		
//		outState.putSerializable("current_ currentMenuOpen_Id", currentMenuOpen_Id);
//		outState.putSerializable("current_ currentDataset", currentDataset);
//		outState.putSerializable("current_ renderer", renderer);
//		outState.putSerializable("current_ list", list);
//	
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		createChartDrawHelper();
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		//register BroadCastRecieverReceiver
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_EDIT_TITLE));
		//registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_EDIT_XY_NAME));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_EDIT_XY_AXIS));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_EDIT_XY_VALUES));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_CHANGE_SERIES_NAME));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_COLOR_PICK_FOR_SERIES));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_EDIT_MARKER_STYLE));
		
	}
	@Override
	protected void onPause() {
		
		super.onPause();
		unregisterReceiver(mReciver);
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		
		super.onConfigurationChanged(newConfig);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.line_chart_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		super.checkIfHomeButtonSelected(item.getItemId());
		openDialog(item.getItemId());
		return true;
	}
	private void openDialog(int pItemId){

		if(hasCreateSeries(pItemId) == false && pItemId != android.R.id.home){
			showAddSeriesFirstMsg();
			return;
		}

		switch (pItemId) {
	
		/**
		 * Add Series menu
		 */
		case R.id.line_chart_add:
			addSeries();
			break;
			
		case R.id.line_chart_series_name_change:
			seriesNameChangeManager.show(list);
			break;
			
		case R.id.line_chart_title:	
			
			titleChangeManager.show(list);
			break;
			
		case R.id.line_chart_xy_axis:	
			xyAxisPropertyManager.show(this.list);
			break;
		
		case R.id.line_chart_xy_values:	
			
			xyValueChangeManager.init(this.list);
			

			break;
			
		case R.id.line_chart_color_line:	
			
			colorChangeManager.showEditColorOfLines(this.list);
			
			break;
		case R.id.line_chart_marker_style:	
			markerStyleManager.showChangeMarkerStyle(this.list);
			
			break;
		case R.id.line_chart_save:
			saveProcess();
			break;
		}
	}
	
	/**
	 * initial set for menus's visibility when user clicks on "Add Series"
	 * @param none
	 * @return none
	 */
	private void addSeries(){
		
		if(list == null){
			list = new ArrayList<>();
		}
		//create new LineChartDataModel
		lineChartDataModel = new LineChartDataModel();
		/** set chart id*/
		lineChartDataModel.setChartId(chartId);
		//add it to list. lineChartDataModel represents a series
		list.add(lineChartDataModel);
		//set id on lineChartDataModel object
		lineChartDataModel.setId(list.indexOf(lineChartDataModel));
		lineChartDrawingHelper.draw(list,null,null,getResources().getString(R.string.action_add_series));

	}
	private boolean hasCreateSeries(int menuItemId){
		
		boolean status = true;
		int rendererCount = 0;
		if(mRenderer != null){
			rendererCount = mRenderer.getSeriesRendererCount();
		}
		if(rendererCount <= 0 && menuItemId != R.id.line_chart_add){
			status = false;
		}
		return status;
	}
	
	@Override
	protected void drawExistingCharts(){
		
		createChartDrawHelper();
		/** get selected saved chart data model*/
		ChartData chartData = (ChartData) SavableDataManager.getInstance().getSelectedChartModel();
		list = chartData.getList();
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setId(list.indexOf(list.get(i)));
			lineChartDrawingHelper.draw(list,null,null,getResources().getString(R.string.action_redraw));
		}
		
	}
	/***
	 * Method to create ChartDrawHelper object
	 */
	private void createChartDrawHelper(){
		
		if(lineChartDrawingHelper == null){
			/**
			 * Create instance of class to help drawing chart
			 */
			lineChartDrawingHelper = new LineChartDrawingHelper(this,R.id.chart);
			
		}
	}
	//Listening if data passed from DialogManager
	private BroadcastReceiver mReciver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			String action = intent.getAction();
			switch (action) {

			case DialogManager.INTENT_ACTION_CHANGE_SERIES_NAME:
				
				DataWrapper dataWrapper = (DataWrapper) intent.getParcelableExtra(DialogManager.INTENT_ACTION_CHANGE_SERIES_NAME);
				
				lineChartDrawingHelper.updateSeriesName(list, dataWrapper,currentDataset,mRenderer);
				
				break;
			case DialogManager.INTENT_ACTION_EDIT_TITLE:	
			case DialogManager.INTENT_ACTION_EDIT_MARKER_STYLE:
			case DialogManager.INTENT_ACTION_COLOR_PICK_FOR_SERIES:
			/** change names on x-axis and y-axis , xmin,xmax,ymin and ymax*/
			case DialogManager.INTENT_ACTION_EDIT_XY_AXIS:
			case DialogManager.INTENT_ACTION_EDIT_XY_VALUES:	
				
				lineChartDrawingHelper.draw(list,null,null,getResources().getString(R.string.action_redraw));
				break;

			default:
				break;
			}
		}
	};
}
