package com.ilhyungkim.indeedcharts;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ilhyungkim.indeedcharts.drawinghelper.StackedBarChartDrawingHelper;
import com.ilhyungkim.indeedcharts.maneger.DialogManager;
import com.ilhyungkim.indeedcharts.model.BarChartDataModel;
import com.ilhyungkim.indeedcharts.model.wrappers.DataWrapper;

public class StackedBarChartActivity extends BarChartActivity {

	private StackedBarChartDrawingHelper stackedBarDrawingHelper; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		stackedBarDrawingHelper = new StackedBarChartDrawingHelper(this, R.id.chart);
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		
		registerReceiver(stackBarReciver, new IntentFilter(DialogManager.INTENT_ACTION_EDIT_TITLE));
		registerReceiver(stackBarReciver, new IntentFilter(DialogManager.INTENT_ACTION_EDIT_XY_VALUES));
		registerReceiver(stackBarReciver, new IntentFilter(DialogManager.INTENT_ACTION_CHANGE_SERIES_NAME));
		registerReceiver(stackBarReciver, new IntentFilter(DialogManager.INTENT_ACTION_COLOR_PICK_FOR_SERIES));
		registerReceiver(stackBarReciver, new IntentFilter(DialogManager.Intent_ACTION_SHOW_CHART_VALUES));
	}
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(stackBarReciver);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.stackedbar_chart_menu, menu);
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
		case R.id.stacked_bar_chart_add:
			addSeries();
			break;	
		
		case R.id.stacked_bar_chart_title:
			
			titleChangeManager.show(list);
			break;
		case R.id.stacked_bar_chart_series_name_change:
			seriesNameChangeManager.show(list);
			break;
		
		case R.id.stacked_bar_chart_xy_name_change:
			xyAxisPropertyManager.showOnlyXYNameChanges(list);
			break;
		case R.id.stacked_bar_chart_xy_values:
			xYValueChangeManagerWithAllowXAsString.init(this.list);
			break;
		
		case R.id.stacked_bar_chart_color_line:
			colorChangeManager.showEditColorOfLines(this.list);
			break;
			
		case R.id.stacked_bar_chart_visibility_chart_values:
			checkBoxmanager.show(this.list);
			break;
		case R.id.stacked_bar_chart_save:
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
		//set chart id that represents chart type
		barChartDataModel.setChartId(chartId);
		//add it to list. lineChartDataModel represents a series
		list.add(barChartDataModel);
		//set id on lineChartDataModel object
		barChartDataModel.setId(list.indexOf(barChartDataModel));
		stackedBarDrawingHelper.draw(list,null,null,getResources().getString(R.string.action_add_series));
		
	}
	private BroadcastReceiver stackBarReciver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
		
			String action = intent.getAction();
			switch (action) {
//			case DialogManager.INTENT_ACTION_EDIT_TITLE:
//				
//				//get returning title value
//				String t = intent.getStringExtra(DialogManager.ENTER_TITLE);
//				barChartDataModel.setTitle(t);
//				stackedBarDrawingHelper.draw(list, currentDataset,mRenderer,null);
//				
//				break;
				
			case DialogManager.INTENT_ACTION_CHANGE_SERIES_NAME:
				DataWrapper dataWrapper = (DataWrapper) intent.getParcelableExtra(DialogManager.INTENT_ACTION_CHANGE_SERIES_NAME);
				stackedBarDrawingHelper.updateSeriesName(list, dataWrapper, currentDataset, mRenderer);
				break;
				
			case DialogManager.INTENT_ACTION_EDIT_XY_NAME:
				stackedBarDrawingHelper.draw(list, currentDataset,mRenderer,null);
				break;
			case DialogManager.INTENT_ACTION_EDIT_TITLE:
			case DialogManager.INTENT_ACTION_EDIT_XY_VALUES:
			case DialogManager.INTENT_ACTION_COLOR_PICK_FOR_SERIES:
			case DialogManager.Intent_ACTION_SHOW_CHART_VALUES:
				stackedBarDrawingHelper.draw(list, null,null,getResources().getString(R.string.action_redraw));
				break;
				
			default:
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
		if(rendererCount <= 0 && menuItemId != R.id.stacked_bar_chart_add){
			status = false;
		}
		return status;
	}
}
