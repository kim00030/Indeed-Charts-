package com.ilhyungkim.indeedcharts;

import java.util.ArrayList;

import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DialRenderer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ilhyungkim.indeedcharts.drawinghelper.DialChartDrawingHelper;
import com.ilhyungkim.indeedcharts.maneger.DialogManager;
import com.ilhyungkim.indeedcharts.maneger.MinMaxValueChangeManager;
import com.ilhyungkim.indeedcharts.maneger.PointerStyleManager;
import com.ilhyungkim.indeedcharts.maneger.SavableDataManager;
import com.ilhyungkim.indeedcharts.model.ChartData;
import com.ilhyungkim.indeedcharts.model.DialChartDataModel;
import com.ilhyungkim.indeedcharts.model.wrappers.DataWrapper;

public class DialChartActivity extends ChartActivity {
	
	protected DialRenderer mRenderer;
	protected CategorySeries category;
	protected DialChartDataModel dialChartDataModel;
	
	protected DialChartDrawingHelper dialChartDrawingHelper;
	
	protected MinMaxValueChangeManager minMaxValueChangeManager = new MinMaxValueChangeManager(this);
	protected PointerStyleManager pointerStyleManager = new PointerStyleManager(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		createChartDrawHelper();
	}
	@Override
	protected void onResume() {
		
		super.onResume();
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_EDIT_TITLE));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_CHANGE_SERIES_NAME));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_EDIT_MIN_MAX_VALUES));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_COLOR_PICK_FOR_SERIES));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_EDIT_POINTER_TYPE));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_EDIT_CHART));
	}
	@Override
	protected void onPause() {
		
		super.onPause();
		unregisterReceiver(mReciver);
	}
	/***
	 * Method to create ChartDrawHelper object
	 */
	private void createChartDrawHelper(){
		
		if(dialChartDrawingHelper == null){
			/**
			 * Create instance of class to help drawing chart
			 */
			dialChartDrawingHelper = new DialChartDrawingHelper(this,R.id.chart);
			
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
			dialChartDrawingHelper.draw(list,getResources().getString(R.string.action_redraw));
		}
		
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.dial_chart_menu, menu);
		return super.onCreateOptionsMenu(menu);
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

		case R.id.dial_chart_add:
			addCategory();
			
			break;
		case R.id.dial_chart_title:
			
			titleChangeManager.show(list);
			break;
			
		case R.id.dial_chart_series_name_change:
			seriesNameChangeManager.show(list);
			break;
		
		/**to modify min & max values*/
		case R.id.dial_chart_min_max_values:
			minMaxValueChangeManager.show(list);
			break;
		case R.id.dial_chart_category_values:
			seriesValueChangeManager.show(list);
			break;
		case R.id.dial_chart_color_line:
			colorChangeManager.showEditColorOfLines(this.list);
			break;
		case R.id.dial_chart_pointer_type:
			pointerStyleManager.showChangePointerStyle(list);
			break;
		case R.id.dial_chart_save:
			saveProcess();
			break;
		};
		
	}
	
	private void addCategory() {
	
		if(list == null){
			list = new ArrayList<>();
		}
		
		/**create new DialChartModel*/
		dialChartDataModel = new DialChartDataModel();
		dialChartDataModel.setChartId(chartId);
		//add it to list.DialChartDataModel represents a series
		list.add(dialChartDataModel);
		//set id on DialCharDatatModel
		dialChartDataModel.setId(list.indexOf(dialChartDataModel));
		
		dialChartDrawingHelper.draw(list,getResources().getString(R.string.action_add_series));
		
	}
	protected BroadcastReceiver mReciver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			String action = intent.getAction();
			switch (action) {
			
//				case DialogManager.INTENT_ACTION_EDIT_TITLE:
//				
//					//get returning title value
//					String t = intent.getStringExtra(DialogManager.ENTER_TITLE);
//					dialChartDataModel.setTitle(t);
//					dialChartDrawingHelper.draw(list,action);
//					break;
				case DialogManager.INTENT_ACTION_CHANGE_SERIES_NAME:
					DataWrapper dataWrapper = (DataWrapper) intent.getParcelableExtra(DialogManager.INTENT_ACTION_CHANGE_SERIES_NAME);
					dialChartDrawingHelper.updateSeriesName(list, dataWrapper);
					break;
				case DialogManager.INTENT_ACTION_EDIT_TITLE:
				case DialogManager.INTENT_ACTION_EDIT_MIN_MAX_VALUES:
				case DialogManager.INTENT_ACTION_COLOR_PICK_FOR_SERIES:
				case DialogManager.INTENT_ACTION_EDIT_POINTER_TYPE:
				case DialogManager.INTENT_ACTION_EDIT_CHART:
					dialChartDrawingHelper.draw(list,null);
					break;
				
			};
			
		}
	};
	protected boolean hasCreateSeries(int menuItemId){
		
		boolean status = true;
		int rendererCount = 0;
		if(mRenderer != null){
			rendererCount = mRenderer.getSeriesRendererCount();
		}
		if(rendererCount <= 0 && menuItemId != R.id.dial_chart_add){
			status = false;
		}
		return status;
	}
	public DialRenderer getmRenderer() {
		return mRenderer;
	}
	public void setmRenderer(DialRenderer mRenderer) {
		this.mRenderer = mRenderer;
	}
	public CategorySeries getCategory() {
		return category;
	}
	public void setCategory(CategorySeries category) {
		this.category = category;
	}

}
