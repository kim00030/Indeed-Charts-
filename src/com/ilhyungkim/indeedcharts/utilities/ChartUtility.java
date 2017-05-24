package com.ilhyungkim.indeedcharts.utilities;

import java.util.HashMap;
import java.util.List;

import com.ilhyungkim.indeedcharts.BarChartActivity;
import com.ilhyungkim.indeedcharts.DialChartActivity;
import com.ilhyungkim.indeedcharts.LineChartActivity;
import com.ilhyungkim.indeedcharts.PieChartActivity;
import com.ilhyungkim.indeedcharts.R;
import com.ilhyungkim.indeedcharts.ScatterChartActivity;
import com.ilhyungkim.indeedcharts.StackedBarChartActivity;
import com.ilhyungkim.indeedcharts.interfaces.IChartModel;

public class ChartUtility {
	
	private static ChartUtility instance;
	private HashMap<String, String> menuMap;
	private HashMap<Integer, String> chartIdMaps;
	
	public static ChartUtility getInstance(){
		
		if(instance == null){
			instance = new ChartUtility();
		}
		return instance;
	}
	/***
	 * Method to initialize menu map that contains chartId and its associated type of chart
	 * key=> chartId, value=> name of chart
	 * @param chartDatas that is List object contains IChartModel objects
	 */
	public void initializeMenuMap(List<IChartModel> chartDatas){
		
		menuMap = new HashMap<>();
		for (IChartModel chartModel : chartDatas) {
			String key = String.valueOf(chartModel.getChartId());
			String value = chartModel.getMenuName();
			menuMap.put(key, value);
		}
	}
	/***
	 * Method to get menu name by its associated chart Id
	 * @param chartId
	 * @return menu name associated with passing chart Id
	 */
	public String getMenuNameByChartId(int chartId){
		
		return menuMap.get(String.valueOf(chartId));
	}
	/***
	 * Method to find chartId as String by menuItem Id
	 * @param menuItemId
	 * @return chartId as String
	 */
	public String getChartIdByMenuItemId(int menuItemId){
		
		if(chartIdMaps == null){
			chartIdMaps = new HashMap<>();
			chartIdMaps.put(R.id.piechart, "1");
			chartIdMaps.put(R.id.linechart, "2");
			chartIdMaps.put(R.id.barchart, "3");
			chartIdMaps.put(R.id.stackrbarchart, "4");
			chartIdMaps.put(R.id.dialchart, "5");
			chartIdMaps.put(R.id.scatterchart, "6");
		}
		return chartIdMaps.get(menuItemId);
	}
	/***
	 * Method to get all ChartActivities being used
	 * @return Array that contains all Classes of ChartActivity being used in this app
	 */
	public Class<?>[] getAllActivities(){
		
		Class<?>[] chartActivities = {null,PieChartActivity.class,LineChartActivity.class,BarChartActivity.class,
				StackedBarChartActivity.class,DialChartActivity.class,ScatterChartActivity.class};
		
		return chartActivities;

	}
	
}
