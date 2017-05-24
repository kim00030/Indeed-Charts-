package com.ilhyungkim.indeedcharts;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ilhyungkim.indeedcharts.drawinghelper.ScatterChartDrawingHelper;
import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;
import com.ilhyungkim.indeedcharts.maneger.DialogManager;
import com.ilhyungkim.indeedcharts.maneger.MarkerStyleManager;
import com.ilhyungkim.indeedcharts.maneger.SavableDataManager;
import com.ilhyungkim.indeedcharts.maneger.XYValueChangeManager;
import com.ilhyungkim.indeedcharts.model.ChartData;
import com.ilhyungkim.indeedcharts.model.ScatterChartDataModel;
import com.ilhyungkim.indeedcharts.model.wrappers.DataWrapper;

public class ScatterChartActivity extends ChartActivity {
	
	private IDrawingChartInfo scatterChartDataModel;
	private ScatterChartDrawingHelper scatterChartDrawingHelper;
	
	private MarkerStyleManager markerStyleManager = new MarkerStyleManager(this);
	private XYValueChangeManager xyValueChangeManager = new XYValueChangeManager(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		createChartDrawHelper();
	}
	private void createChartDrawHelper() {
		
		if(scatterChartDrawingHelper == null){
			scatterChartDrawingHelper = new ScatterChartDrawingHelper(this, R.id.chart);
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
			scatterChartDrawingHelper.draw(list,null,null,getResources().getString(R.string.action_redraw));
		}
		
	}
	@Override
	protected void onResume() {
		
		super.onResume();
		//register BroadCastRecieverReceiver
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_EDIT_TITLE));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_CHANGE_SERIES_NAME));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_EDIT_XY_AXIS));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_EDIT_XY_VALUES));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_EDIT_MARKER_STYLE));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_COLOR_PICK_FOR_SERIES));
		
	}
	@Override
	protected void onPause() {
		
		super.onPause();
		unregisterReceiver(mReciver);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.scatter_chart_menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		super.checkIfHomeButtonSelected(item.getItemId());
		openDialog(item.getItemId());
		return true;
	}
	private void openDialog(int pItemId) {
	
		if(hasCreateSeries(pItemId) == false && pItemId != android.R.id.home){
			showAddSeriesFirstMsg();
			return;
		}
		switch (pItemId) {
		
			/**
			 * Add Series menu
			 */
			case R.id.scatter_chart_add:
				addSeries();
				break;
			case R.id.scatter_chart_title:	
				
				titleChangeManager.show(list);
				break;
				
			case R.id.scatter_chart_series_name_change:
				seriesNameChangeManager.show(list);
				break;
			case R.id.scatter_chart_xy_axis:	
				xyAxisPropertyManager.show(this.list);
				break;
			case R.id.scatter_chart_xy_values:	
				xyValueChangeManager.init(this.list);
				break;
			case R.id.scatter_chart_color_line:	
				colorChangeManager.showEditColorOfLines(this.list);
				break;
			case R.id.scatter_chart_marker_style:	
				markerStyleManager.showChangeMarkerStyle(this.list);
				
				break;
			
			case R.id.scatter_chart_save:	
				saveProcess();
				break;
				

		};
		
	}
	private void addSeries() {
		
		if(list == null){
			list = new ArrayList<>();
		}
		scatterChartDataModel = new ScatterChartDataModel();
		/** set chart id*/
		scatterChartDataModel.setChartId(chartId);
		//add it to list. lineChartDataModel represents a series
		list.add(scatterChartDataModel);
		//set id on lineChartDataModel object
		scatterChartDataModel.setId(list.indexOf(scatterChartDataModel));
		scatterChartDrawingHelper.draw(list,null,null,getResources().getString(R.string.action_add_series));

	}
	
	private BroadcastReceiver mReciver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			String action = intent.getAction();
			switch (action) {
//			case DialogManager.INTENT_ACTION_EDIT_TITLE:
//				//get returning title value
//				String t = intent.getStringExtra(DialogManager.ENTER_TITLE);
//				scatterChartDataModel.setTitle(t);
//				scatterChartDrawingHelper.draw(list,currentDataset,mRenderer,null);
//				break;
				
			case DialogManager.INTENT_ACTION_CHANGE_SERIES_NAME:
				
				DataWrapper dataWrapper = (DataWrapper) intent.getParcelableExtra(DialogManager.INTENT_ACTION_CHANGE_SERIES_NAME);
				
				scatterChartDrawingHelper.updateSeriesName(list, dataWrapper,currentDataset,mRenderer);
				
				break;
			case DialogManager.INTENT_ACTION_EDIT_TITLE:
			case DialogManager.INTENT_ACTION_EDIT_MARKER_STYLE:
			case DialogManager.INTENT_ACTION_COLOR_PICK_FOR_SERIES:
			case DialogManager.INTENT_ACTION_EDIT_XY_AXIS:
			case DialogManager.INTENT_ACTION_EDIT_XY_VALUES:	
				scatterChartDrawingHelper.draw(list,null,null,getResources().getString(R.string.action_redraw));

				break;
			default:
				break;
			};
			
		}
	};
	private boolean hasCreateSeries(int menuItemId) {
		
		boolean status = true;
		int rendererCount = 0;
		if(mRenderer != null){
			rendererCount = mRenderer.getSeriesRendererCount();
		}
		if(rendererCount <= 0 && menuItemId != R.id.scatter_chart_add){
			status = false;
		}
		return status;
	}
}
