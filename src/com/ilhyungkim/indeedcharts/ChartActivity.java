package com.ilhyungkim.indeedcharts;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.ilhyungkim.indeedcharts.interfaces.IAlertDialogInterface;
import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;
import com.ilhyungkim.indeedcharts.maneger.ColorChangeManager;
import com.ilhyungkim.indeedcharts.maneger.SavableDataManager;
import com.ilhyungkim.indeedcharts.maneger.SeriesNameChangeManager;
import com.ilhyungkim.indeedcharts.maneger.SeriesValueChangeManager;
import com.ilhyungkim.indeedcharts.maneger.TitleChangeManager;
import com.ilhyungkim.indeedcharts.maneger.ToastMessageManager;
import com.ilhyungkim.indeedcharts.maneger.XYAxisPropertyManager;
import com.ilhyungkim.indeedcharts.model.ChartData;
import com.ilhyungkim.indeedcharts.model.EditTextData;
import com.ilhyungkim.indeedcharts.utilities.ChartHelper;

public class ChartActivity extends Activity {

	protected int chartId = -1;
	protected String imageName;
	protected String actionType;
	
	protected ArrayList<IDrawingChartInfo> list;
	public XYMultipleSeriesDataset currentDataset;
	public XYMultipleSeriesRenderer mRenderer;
	
	protected EditTextData data = null;
	protected List<EditTextData> editDatas;
	protected ColorChangeManager colorChangeManager = new ColorChangeManager(this);
	protected XYAxisPropertyManager xyAxisPropertyManager = new XYAxisPropertyManager(this);
	protected TitleChangeManager titleChangeManager = new TitleChangeManager(this);
	protected SeriesNameChangeManager seriesNameChangeManager = new SeriesNameChangeManager(this);
	protected SeriesValueChangeManager seriesValueChangeManager = new SeriesValueChangeManager(this);
	public IAlertDialogInterface currentDialogManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_line_chart_set);
		
		//SavableDataManager.getInstance().setCurrentActivity(this);
	
		/**get chart id that represents type of chart*/
		chartId = getIntent().getIntExtra(getResources().getString(R.string.chart_id), -1);
		/** image icon file name without extension*/
		imageName = getIntent().getStringExtra(getResources().getString(R.string.chart_image));
		/**get action type. either action for creating new chart or opening existing saved chart*/
		actionType = getIntent().getStringExtra(getResources().getString(R.string.action_Type));
		if(actionType.equals(getResources().getString(R.string.action_open_existing_chart))){
			
			drawExistingCharts();
		}
		/**
		 * set custom title and color on the Action Bar
		 */
		ChartHelper.setTitleOnActionBar( getActionBar(), getIntent());
		ChartHelper.setBackgroundColor(getActionBar(), Color.MAGENTA);

	}
	@Override
	protected void onResume() {
		
		super.onResume();
		/**database open*/
		try {
			SavableDataManager.getInstance().datasource.open();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	/***
	 * Method to draw existing chart that has been saved in DB
	 * 
	 */
	protected void drawExistingCharts(){
		//Do not do anything here. Child will be executed..
	}
	@Override
	protected void onPause() {
		
		super.onPause();
		/**database close*/
		try {
			SavableDataManager.getInstance().datasource.close();
			if(this.currentDialogManager != null)this.currentDialogManager.dismissAlertDialog();
			this.finish();
		} catch (Exception e) {
		
			e.printStackTrace();
		}

	}
	/***
	 *  method to check if home button is selected 
	 * @param itemId that is menu id
	 */
	protected void checkIfHomeButtonSelected(int itemId){
		
		switch (itemId) {
		
			case android.R.id.home:
				
				this.finish();
				
				break;
		}
	}
	/***
	 * To save chartData
	 */
	protected void saveProcess(){
		
		SaveChartTask mySaveChartTask= new SaveChartTask();
		mySaveChartTask.execute();

	}
	/***
	 * Method to show status of saving chart to DB
	 * @param status true if save it successfully, otherwise false
	 */
	protected void showSavingStatus(Boolean status){
		if(status == true){
			ToastMessageManager.show(this, "Saved", Toast.LENGTH_SHORT);
		}else{
			ToastMessageManager.show(this, "Fail to save", Toast.LENGTH_SHORT);
		}
	}
	/***
	 * Method to show ""Add Series First" message
	 */
	protected void showAddSeriesFirstMsg(){
		ToastMessageManager.show(this, "Add Series First", Toast.LENGTH_SHORT);
	}
	/***
	 * Class handles on saving chart in background
	 * @author owner
	 *
	 */
	private class SaveChartTask extends AsyncTask< Void,Void, Boolean>{

		@Override
		protected Boolean doInBackground( Void... params) {
		
			boolean success = false;
			if(list != null && list.size()>0){
				ChartData data = new ChartData();
				data.setDbKey(list.get(0).getDbKey());
				data.setChartId(list.get(0).getChartId());
				data.setMenuName(list.get(0).getTitle());
				data.setImage(imageName);
				data.setList(list);
				data.setDate(SavableDataManager.getInstance().createDateFormat());
				success = SavableDataManager.getInstance().insertChartData(data);
				
			}
			return success;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			
			showSavingStatus(result);
		}
	
		
	}
}
