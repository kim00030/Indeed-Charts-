package com.ilhyungkim.indeedcharts;

import java.util.ArrayList;

import org.achartengine.renderer.DefaultRenderer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ilhyungkim.indeedcharts.drawinghelper.PieChartDrawingHelper;
import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;
import com.ilhyungkim.indeedcharts.maneger.DialogManager;
import com.ilhyungkim.indeedcharts.maneger.SavableDataManager;
import com.ilhyungkim.indeedcharts.model.ChartData;
import com.ilhyungkim.indeedcharts.model.PieChartDataModel;
import com.ilhyungkim.indeedcharts.model.wrappers.DataWrapper;

public class PieChartActivity extends ChartActivity {
	
	protected IDrawingChartInfo pieChartDataModel;
	protected PieChartDrawingHelper pieChartDrawingHelper;
	protected DefaultRenderer defaultRenderer;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		createChartDrawHelper();
	}

	private void createChartDrawHelper() {
		
		if(pieChartDrawingHelper == null){
			pieChartDrawingHelper = new PieChartDrawingHelper(this,R.id.chart);
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
			pieChartDrawingHelper.draw(list,getResources().getString(R.string.action_redraw));
		}
		
	}
	@Override
	protected void onResume() {
		
		super.onResume();
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_EDIT_TITLE));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_CHANGE_SERIES_NAME));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_EDIT_CHART));
		registerReceiver(mReciver, new IntentFilter(DialogManager.INTENT_ACTION_COLOR_PICK_FOR_SERIES));

	}
	
	@Override
	protected void onPause() {
		
		super.onPause();
		unregisterReceiver(mReciver);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.pie_chart_menu, menu);
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
		case R.id.pie_chart_add:
			addSeries();
			break;
			
		case R.id.pie_chart_title:	
			titleChangeManager.show(list);
			break;
		case R.id.pie_chart_series_name_change:
			seriesNameChangeManager.show(list);
			break;
		case R.id.pie_chart_series_values:	
			seriesValueChangeManager.show(list);
			break;
		
		case R.id.pie_chart_color_line:	
			colorChangeManager.showEditColorOfLines(this.list);
			break;

		case R.id.pie_chart_save:	
			saveProcess();
			break;
		default:
			break;
		}
	}
	private void addSeries() {
		
		if(list == null){
			list = new ArrayList<>();
		}
		
		int loop = list.size() == 0? 2:1;
		for(int i = 0; i< loop;i++){
			pieChartDataModel = new PieChartDataModel();
			pieChartDataModel.setChartId(chartId);
			list.add(pieChartDataModel);
			pieChartDataModel.setId(list.indexOf(pieChartDataModel));
			pieChartDrawingHelper.draw(list,getResources().getString(R.string.action_add_series));
		}
	}


	private boolean hasCreateSeries(int menuItemId){
		
		boolean status = true;
		int rendererCount = 0;
		if(defaultRenderer != null){
			rendererCount = defaultRenderer.getSeriesRendererCount();
		}
		if(rendererCount <= 0 && menuItemId != R.id.pie_chart_add){
			status = false;
		}
		return status;
	}
	
	public DefaultRenderer getDefaultRenderer() {
		return defaultRenderer;
	}

	public void setDefaultRenderer(DefaultRenderer defaultRenderer) {
		this.defaultRenderer = defaultRenderer;
	}

	protected BroadcastReceiver mReciver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			String action = intent.getAction();
			switch (action) {
//			case DialogManager.INTENT_ACTION_EDIT_TITLE:
//				
//				//get returning title value
//				String t = intent.getStringExtra(DialogManager.ENTER_TITLE);
//				pieChartDataModel.setTitle(t);
//				pieChartDrawingHelper.draw(list,action);
//				break;
			
			case DialogManager.INTENT_ACTION_CHANGE_SERIES_NAME:
				DataWrapper dataWrapper = (DataWrapper) intent.getParcelableExtra(DialogManager.INTENT_ACTION_CHANGE_SERIES_NAME);
				pieChartDrawingHelper.updateSeriesName(list, dataWrapper);
				
				break;
			case DialogManager.INTENT_ACTION_EDIT_TITLE:
			case DialogManager.INTENT_ACTION_COLOR_PICK_FOR_SERIES:
			case DialogManager.INTENT_ACTION_EDIT_CHART:
			
				pieChartDrawingHelper.draw(list,null);
				break;
			default:
				break;
			}
		}
	};
	
}
