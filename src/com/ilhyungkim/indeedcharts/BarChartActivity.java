package com.ilhyungkim.indeedcharts;

import java.util.ArrayList;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ilhyungkim.indeedcharts.drawinghelper.BarChartDrawingHelper;
import com.ilhyungkim.indeedcharts.interfaces.IBarChartActivity;
import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;
import com.ilhyungkim.indeedcharts.maneger.DialogManager;
import com.ilhyungkim.indeedcharts.maneger.SavableDataManager;
import com.ilhyungkim.indeedcharts.maneger.VisibilityOfChartValuesManager;
import com.ilhyungkim.indeedcharts.maneger.XYValueChangeManagerWithAllowXAsString;
import com.ilhyungkim.indeedcharts.model.BarChartDataModel;
import com.ilhyungkim.indeedcharts.model.ChartData;
import com.ilhyungkim.indeedcharts.model.wrappers.DataWrapper;
/***
 * This activity is for creating Bar chart
 * @author Dan Kim
 *
 */
public class BarChartActivity extends ChartActivity implements IBarChartActivity{
	
	protected IDrawingChartInfo barChartDataModel;
	protected BarChartDrawingHelper barChartDrawingHelper;
	protected XYValueChangeManagerWithAllowXAsString xYValueChangeManagerWithAllowXAsString = new XYValueChangeManagerWithAllowXAsString(this);
	protected VisibilityOfChartValuesManager checkBoxmanager = new VisibilityOfChartValuesManager(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		createChartDrawHelper();
	}
	/***
	 * Method to create BarChartDrawingHelper class to help creating Bar chart
	 */
	private void createChartDrawHelper() {
		
		/**
		 * Create instance of class to help drawing chart
		 */
		if(barChartDrawingHelper == null){
			barChartDrawingHelper = new BarChartDrawingHelper(this,R.id.chart);
		}
		
	}
	@Override
	protected void drawExistingCharts(){
		
		createChartDrawHelper();
		/** get selected saved chart data model*/
		ChartData chartData = (ChartData) SavableDataManager.getInstance().getSelectedChartModel();
		list = chartData.getList();
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setId(list.indexOf(list.get(i)));
			barChartDrawingHelper.draw(list,null,null,getResources().getString(R.string.action_redraw));
		}
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_EDIT_TITLE));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_CHANGE_SERIES_NAME));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_COLOR_PICK_FOR_SERIES));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_EDIT_XY_NAME));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_EDIT_XY_VALUES));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_EDIT_MARKER_STYLE));
		registerReceiver(mReciver, new IntentFilter(DialogManager.Intent_ACTION_SHOW_CHART_VALUES));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReciver);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.bar_chart_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		super.checkIfHomeButtonSelected(item.getItemId());
		openDialog(item.getItemId());
		return super.onOptionsItemSelected(item);
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
		case R.id.bar_chart_add:
			addSeries();
			break;
			
		case R.id.bar_chart_title:
			
			titleChangeManager.show(list);
			break;
		
		case R.id.bar_chart_series_name_change:
			seriesNameChangeManager.show(list);
			break;
		case R.id.bar_chart_xy_name_change:
			xyAxisPropertyManager.showOnlyXYNameChanges(list);
			break;
		
		case R.id.bar_chart_xy_values:
			xYValueChangeManagerWithAllowXAsString.init(this.list);
			break;
		case R.id.bar_chart_color_line:
			colorChangeManager.showEditColorOfLines(this.list);
			break;
		case R.id.bar_chart_visibility_chart_values:
			
			checkBoxmanager.show(this.list);
			break;
		case R.id.bar_chart_save:
			saveProcess();
			break;
		};
	}
	
	private void addSeries() {
		
		if(list == null){
			list = new ArrayList<>();
		}
		//create new BarChartDataModel
		barChartDataModel = new BarChartDataModel();
		barChartDataModel.setChartId(chartId);
		//add it to list. lineChartDataModel represents a series
		list.add(barChartDataModel);
		//set id on lineChartDataModel object
		barChartDataModel.setId(list.indexOf(barChartDataModel));
		barChartDrawingHelper.draw(list,null,null,getResources().getString(R.string.action_add_series));
		
	}
	protected BroadcastReceiver mReciver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
		
			String action = intent.getAction();
			switch (action) {

			case DialogManager.INTENT_ACTION_CHANGE_SERIES_NAME:
				DataWrapper dataWrapper = (DataWrapper) intent.getParcelableExtra(DialogManager.INTENT_ACTION_CHANGE_SERIES_NAME);
				barChartDrawingHelper.updateSeriesName(list, dataWrapper, currentDataset, mRenderer);
				break;
				
			case DialogManager.INTENT_ACTION_EDIT_XY_NAME:
				barChartDrawingHelper.draw(list, currentDataset,mRenderer,null);
				break;
			case DialogManager.INTENT_ACTION_EDIT_TITLE:
			case DialogManager.INTENT_ACTION_EDIT_XY_VALUES:
			case DialogManager.INTENT_ACTION_COLOR_PICK_FOR_SERIES:
			case DialogManager.Intent_ACTION_SHOW_CHART_VALUES:
				barChartDrawingHelper.draw(list, null,null,getResources().getString(R.string.action_redraw));
				break;

			}
		}
	};
	
	
	protected boolean hasCreateSeries(int menuItemId){
		
		boolean status = true;
		int rendererCount = 0;
		if(mRenderer != null){
			rendererCount = mRenderer.getSeriesRendererCount();
		}
		if(rendererCount <= 0 && menuItemId != R.id.bar_chart_add){
			status = false;
		}
		return status;
	}

	public XYMultipleSeriesDataset getCurrentDataset() {
		return currentDataset;
	}

	public void setCurrentDataset(XYMultipleSeriesDataset currentDataset) {
		this.currentDataset = currentDataset;
	}

	public XYMultipleSeriesRenderer getmRenderer() {
		return mRenderer;
	}

	public void setmRenderer(XYMultipleSeriesRenderer mRenderer) {
		this.mRenderer = mRenderer;
	}
}
