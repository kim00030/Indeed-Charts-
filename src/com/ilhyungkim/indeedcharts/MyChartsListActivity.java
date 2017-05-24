package com.ilhyungkim.indeedcharts;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ilhyungkim.indeedcharts.constantdata.ChartConstantData;
import com.ilhyungkim.indeedcharts.db.ChartsDBOpenHelper;
import com.ilhyungkim.indeedcharts.interfaces.IChartModel;
import com.ilhyungkim.indeedcharts.maneger.DialogManager;
import com.ilhyungkim.indeedcharts.maneger.SavableDataManager;
import com.ilhyungkim.indeedcharts.maneger.ToastMessageManager;
import com.ilhyungkim.indeedcharts.model.ChartData;
import com.ilhyungkim.indeedcharts.utilities.ChartHelper;
import com.ilhyungkim.indeedcharts.utilities.ChartUtility;

public class MyChartsListActivity extends ListActivity {
	
	private ArrayAdapter<IChartModel> adapter;
	private List<IChartModel> mySavedDatas;
	private Intent intentForTitle;
	private static final int MENU_DELETE_ID = 1002;
	private static final int COLOR_VALUE =-16537609;//-10409740
	/** index of selected item in this ListActivity*/
	private int currentSelectedIndex;
	/** Key for SharePreference*/
	private static final String SHOW_HINT_STATUS = "show_hint_status";
	/**SharePreferenceObject*/
	private SharedPreferences hintSetting;
	private String currentSelection;
	private String currentOrderBy;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		/**Create SharePreference Object*/
		if(hintSetting == null){
			hintSetting = getPreferences(MODE_PRIVATE);
		}
		/**
		 * set custom title and color on the Action Bar
		 */
		if(intentForTitle == null){
			intentForTitle = new Intent();
			/**Display 'My Charts' on Action Bar*/
			intentForTitle.putExtra(getResources().getString(R.string.chart_default_title), getResources().getString(R.string.mycharts));
			ChartHelper.setTitleOnActionBar( getActionBar(),intentForTitle);
			ChartHelper.setBackgroundColor(getActionBar(), COLOR_VALUE);//blue
		}
		/**why null, null? because I want to display all without any filtering*/
		//refreshDisplay(null,null);
		/** for delete list item process*/
		registerForContextMenu(getListView());
		/**show hint for removing saved items if condition below is matched */
		mySavedDatas = createSavedListMenu(null,null);
		if(mySavedDatas != null && mySavedDatas.size() >0 && getHintPropertyStatus() == false){
			DialogManager.showRemovingItemHintProperty(this);
		}
	}
	/***
	 * Method to get status from SharePreference object for Hint dialogue
	 * @return boolean
	 */
	private boolean getHintPropertyStatus(){
		
		boolean status = hintSetting.getBoolean(SHOW_HINT_STATUS, false);
		return status;
	}
	@Override
	protected void onResume() {
		
		super.onResume();
		SavableDataManager.getInstance().dbOpen();
		refreshDisplay(null,null);
		registerReceiver(mRceiver, new IntentFilter(DialogManager.INTENT_ACTION_SHOW_HINT_FOR_REMOVE_ITEMS));
		
	}
	@Override
	protected void onPause() {
		
		super.onPause();
		SavableDataManager.getInstance().dbClose();
		unregisterReceiver(mRceiver);
		//this.finish();
	}
	
	
	/***
	 * Method to display saved list items
	 * @param selection
	 * @param orderBy
	 */
	protected void refreshDisplay(String selection, String orderBy){
		/**save current selection and orderBy*/
		this.currentSelection = selection;
		this.currentOrderBy = orderBy;
		mySavedDatas = createSavedListMenu(selection,orderBy);
		
		if(mySavedDatas != null){
			/**Display whatever save charts are, even it's empty*/
			adapter = new MySavedChartListAdapter(this, R.layout.saved_item_chart, mySavedDatas);
			setListAdapter(adapter);
			if(mySavedDatas.size()<=0){
				String chart = "chart";
				/** if selection is Not null, means that user tries to sort the saved data out by charts*/
				if(selection != null){
					/**get chart Id in String selection*/
					char c = selection.charAt(selection.length()-1);
					/**convert chart Id as Char to int*/
					int chartId = Character.getNumericValue(c);
					/**get menu name associated with the chart Id*/
					chart = ChartUtility.getInstance().getMenuNameByChartId(chartId);
				}
				ToastMessageManager.show(this, "You have no saved "+ chart+" yet", Toast.LENGTH_SHORT);
			}
		}
		
	}
	
	/***
	 * Method to create saved list menus
	 * @return List<IChartModel> that contains all saved chart data
	 */
	private List<IChartModel> createSavedListMenu(String selection,String orderBy) {
		
		return SavableDataManager.getInstance().getAllChartDatas(selection,orderBy);
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);
		goToActivity(mySavedDatas.get(position),position);
	}
	/***
	 * Method to go to appropriate page
	 * @param chartModel IChartModel
	 * @param position index of ListView
	 */
	private void goToActivity(IChartModel chartModel, int position) {
		
		Class<?>[] activities = ChartUtility.getInstance().getAllActivities();
		/** set chartModel for selected list menu*/
		SavableDataManager.getInstance().setSelectedChartModel(chartModel);
		Intent intent = new Intent(MyChartsListActivity.this,activities[chartModel.getChartId()]);
		intent.putExtra(getResources().getString(R.string.chart_id), chartModel.getChartId());
		intent.putExtra(getResources().getString(R.string.chart_image), chartModel.getImage());
		intent.putExtra(getResources().getString(R.string.chart_default_title), ChartUtility.getInstance().getMenuNameByChartId(chartModel.getChartId()));
		intent.putExtra(getResources().getString(R.string.action_Type), getResources().getString(R.string.action_open_existing_chart));
		
		startActivity(intent);
	}
	/***
	 * Method to sort saved charts by their names in either ascending or descending order.
	 * @param itemId MenuItem Id
	 */
	private void sortByName(int itemId){
		
		refreshDisplay(null,null);
		if(itemId == R.id.ascend){
			Collections.sort(mySavedDatas, new Comparator<IChartModel>() {
	
				@Override
				public int compare(IChartModel object1, IChartModel object2) {
					int res = String.CASE_INSENSITIVE_ORDER.compare(object1.getMenuName(), object2.getMenuName());
					return res;
				}
			});
		}else if(itemId == R.id.descend){
		
		
			Collections.sort(mySavedDatas, new Comparator<IChartModel>() {
				
				@Override
				public int compare(IChartModel object1, IChartModel object2) {
					int res = String.CASE_INSENSITIVE_ORDER.compare(object2.getMenuName(), object1.getMenuName());
					return res;
				}
			});
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		case R.id.ascend:
		case R.id.descend:
			sortByName(item.getItemId());
			//refreshDisplay(null, ChartsDBOpenHelper.COLUMN_DATE+" DESC");
			break;
	
		case R.id.piechart:
		case R.id.linechart:
		case R.id.barchart:
		case R.id.stackrbarchart:
		case R.id.dialchart:
		case R.id.scatterchart:
			String chartId = ChartUtility.getInstance().getChartIdByMenuItemId(item.getItemId());
			refreshDisplay(ChartsDBOpenHelper.COLUMN_CHART_ID + "="+chartId, null);	
			break;
	
		}
		
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.mychart_list_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}
	
	
	/***
	 * To be able to display "delete" ContextMenu
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
	
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		currentSelectedIndex = (int) info.id;
		menu.add(0,MENU_DELETE_ID,0,ChartConstantData.DELETE);
		
	}
	/***
	 * To delete the selected item in this ListActivity
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		if(item.getItemId() == MENU_DELETE_ID){
			
			ChartData data = (ChartData) mySavedDatas.get(currentSelectedIndex);
			boolean deletSuccess = SavableDataManager.getInstance().deleteRowByDBKey(data.getDbKey());
			if(deletSuccess){
				refreshDisplay(this.currentSelection,this.currentOrderBy);
			}
		}
		return super.onContextItemSelected(item);
	}
	
	private BroadcastReceiver mRceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			String action = intent.getAction();
			switch (action) {
			case DialogManager.INTENT_ACTION_SHOW_HINT_FOR_REMOVE_ITEMS:
				/**save status of showing hint for deleting items into SharePreference*/
				boolean isChecked = intent.getBooleanExtra(action, false);
				SharedPreferences.Editor editor = hintSetting.edit();
				editor.putBoolean(SHOW_HINT_STATUS, isChecked);
				editor.commit();
				break;
			}
		}
	};
	
	
}
