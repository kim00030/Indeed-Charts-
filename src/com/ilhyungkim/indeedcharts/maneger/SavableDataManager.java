package com.ilhyungkim.indeedcharts.maneger;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;

import com.ilhyungkim.indeedcharts.db.ChartsDataSource;
import com.ilhyungkim.indeedcharts.interfaces.IChartModel;
import com.ilhyungkim.indeedcharts.model.ChartData;

public class SavableDataManager {

	private static SavableDataManager instance;
	private IChartModel selectedChartModel;
	public ChartsDataSource datasource;
	public Context context;
	
	public static SavableDataManager getInstance(){
		if(instance == null){
			instance = new SavableDataManager();
		}
		return instance;
	}
	/***
	 * Method to initialize DB
	 * @param context
	 */
	public void initializeDB(Context context){
		
		this.context = context;
		datasource = new ChartsDataSource(context);
	}
	/***
	 * Method to open DB
	 */
	public void dbOpen(){
		
		try {
			datasource.open();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	/***
	 * Method to close DB
	 */
	public void dbClose(){
		
		try {
			datasource.close();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	/***
	 * Method to delete row in DB
	 * @param dbkey
	 * @return true if deleting row in DB successfully, otherwise false
	 */
	public boolean deleteRowByDBKey(long dbkey){
		
		return datasource.deleteRowByDBKey(dbkey);
	}
	
	/***
	 * Method to insert chart to DB
	 * @param data ChartData Object
	 * @return true if successfully insert chartData to DB, otherwise false
	 */
	public boolean insertChartData(ChartData data){
		
		ChartData insertedDBdata = datasource.insertChartData(data);
		if(insertedDBdata.getDbKey() != -1){
			return true;
			
		}else{
			return false;
			
		}
	}
	/***
	 * Method to get all chart data from DB
	 * @return List that contains IChartModel objects
	 */
	public List<IChartModel> getAllChartDatas(String selection,String orderBy) {
		
		return datasource.findAll(selection, orderBy);
	}
	
	/***
	 * Getter for selected chart
	 * @return IChartModel object
	 */
	public IChartModel getSelectedChartModel() {
		return selectedChartModel;
	}
	/***
	 * Setter for selected chart
	 * @param selectedChartModel that is IChartModel type
	 */
	public void setSelectedChartModel(IChartModel selectedChartModel) {
		this.selectedChartModel = selectedChartModel;
	}
	/***
	 * Getter for current running activity
	 * @return
	 */


	/***
	 * Method to format Date
	 * @return date-formatted string
	 */
	@SuppressLint("SimpleDateFormat")
	public String createDateFormat(){

		Calendar cal = Calendar.getInstance(new Locale("en_US"));
		
		String date_str = new java.text.SimpleDateFormat("EEEE, dd/MM/yyyy/hh:mm:ss")
        .format(cal.getTime());
		
		
		return date_str;
	}
}
