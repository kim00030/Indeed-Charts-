package com.ilhyungkim.indeedcharts;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ilhyungkim.indeedcharts.interfaces.IChartModel;
import com.ilhyungkim.indeedcharts.maneger.SavableDataManager;
import com.ilhyungkim.indeedcharts.utilities.ChartHelper;
import com.ilhyungkim.indeedcharts.utilities.ChartUtility;

public class MainActivity extends ListActivity {

	private List<IChartModel> chartDatas;
	private ArrayAdapter<IChartModel> adapter;
	private ChartsPullParser chartPullParser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ChartHelper.setBackgroundColor(getActionBar(),Color.MAGENTA);
		chartDatas = getMenuDataFromXML();
		/** set ListAdapter*/
		adapter = new ChartListAdapter(this, R.layout.item_chart, chartDatas);
		setListAdapter(adapter);
		SavableDataManager.getInstance().initializeDB(this);
		
		MainActivityData data = new MainActivityData(chartDatas);
		InitializeHandler handler = new InitializeHandler();
		handler.execute(data);
		//ChartUtility.getInstance().initializeMenuMap(chartDatas);
		
	}
	@Override
	protected void onResume() {
		
		super.onResume();
		SavableDataManager.getInstance().datasource.open();
	}
	@Override
	protected void onPause() {
		
		super.onPause();
		SavableDataManager.getInstance().datasource.close();
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);
		goToActivity(chartDatas.get(position),position);
		
	}
	/***
	 * Method to be executed to go to Chart Activity 
	 * @param chartModel that is clicked
	 * @param position of clicked chartModel
	 */
	private void goToActivity(IChartModel chartModel,int position) {
		
		Class<?>[] activities = ChartUtility.getInstance().getAllActivities();
		Intent intent = new Intent(MainActivity.this,activities[chartModel.getChartId()]);
		intent.putExtra(getResources().getString(R.string.chart_id), chartModel.getChartId());
		intent.putExtra(getResources().getString(R.string.chart_image), chartModel.getImage());
		intent.putExtra(getResources().getString(R.string.chart_default_title), chartModel.getMenuName());
		intent.putExtra(getResources().getString(R.string.action_Type), getResources().getString(R.string.action_create_new_chart));
		startActivity(intent);
	}

	/***
	 * Method to get menu data from xml 
	 * @return list contains IChartModel objects
	 */
	private List<IChartModel> getMenuDataFromXML() {

		chartPullParser = new ChartsPullParser();
		return chartPullParser.parseXML(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.my_charts:
			
			/** go to a page that lists all saved charts if any*/
			Intent intent = new Intent(MainActivity.this, MyChartsListActivity.class);
			startActivity(intent);
			break;
		/**Exit button clicks*/
		case R.id.exit:
			this.finish();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	private class MainActivityData{
		
		public List<IChartModel> chartDatas;
		
		public MainActivityData(List<IChartModel> chartDatas){
			
			this.chartDatas = chartDatas;
		}
		
	}
	private class InitializeHandler extends AsyncTask<MainActivityData , Void, Void>{

	
		@Override
		protected Void doInBackground(MainActivityData... params) {
			
			ChartUtility.getInstance().initializeMenuMap(params[0].chartDatas);
			return null;
		}

		
		
	}
}
