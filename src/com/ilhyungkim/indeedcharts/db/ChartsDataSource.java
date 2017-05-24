package com.ilhyungkim.indeedcharts.db;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.achartengine.chart.PointStyle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ilhyungkim.indeedcharts.constantdata.ChartConstantData;
import com.ilhyungkim.indeedcharts.interfaces.IChartModel;
import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;
import com.ilhyungkim.indeedcharts.model.BarChartDataModel;
import com.ilhyungkim.indeedcharts.model.ChartData;
import com.ilhyungkim.indeedcharts.model.DialChartDataModel;
import com.ilhyungkim.indeedcharts.model.LineChartDataModel;
import com.ilhyungkim.indeedcharts.model.PieChartDataModel;
import com.ilhyungkim.indeedcharts.model.ScatterChartDataModel;

public class ChartsDataSource {

	private SQLiteOpenHelper dbHelper;
	private SQLiteDatabase database;
	private HashMap<String, PointStyle> pointStyleMap = new HashMap<>();
	private HashMap<String, org.achartengine.renderer.DialRenderer.Type> typeStyleMap = new HashMap<String, org.achartengine.renderer.DialRenderer.Type>();
	
	/**columns on DB*/
	private String[] allColumns = {
		
			ChartsDBOpenHelper.COLUMN_MAIN_ID,
			ChartsDBOpenHelper.COLUMN_CHART_ID,
			ChartsDBOpenHelper.COLUMN_TITLE,
			ChartsDBOpenHelper.COLUMN_DATE,
			ChartsDBOpenHelper.COLUMN_IMAGE_NAME,
			ChartsDBOpenHelper.COLUMN_SREIES_DATA
	};
	/***
	 * This constructor creates SQLIteOpenHelper
	 * @param context
	 */
	public ChartsDataSource(Context context){
		
		dbHelper = new ChartsDBOpenHelper(context);
		init();
	}
	private void init() {
		
		/**Map that contains PointStyle objects with their associated key*/
		pointStyleMap.put("CIRCLE", PointStyle.CIRCLE);
		pointStyleMap.put("DIAMOND", PointStyle.DIAMOND);
		pointStyleMap.put("TRIANGLE", PointStyle.TRIANGLE);
		pointStyleMap.put("SQUARE", PointStyle.SQUARE);
		/**Map that Type objects used in Dial chart*/
		typeStyleMap.put("Needle", org.achartengine.renderer.DialRenderer.Type.NEEDLE);
		typeStyleMap.put("Arrow", org.achartengine.renderer.DialRenderer.Type.ARROW);
		
	}
	/***
	 * Method to open DB
	 */
	public void open(){
		
		database = dbHelper.getWritableDatabase();
	}
	/***
	 * Method to close DB
	 */
	public void close(){
		
		dbHelper.close();
	}
	/***
	 *  Method to figure out if database is opened
	 * @return true if database is opened, false if database is closed
	 */
	public boolean isDBOpen(){
		
		if(database == null) return false;
	
		return database.isOpen();
	}
	/***
	 * Method to delete requiring row
	 * @param dbKey
	 * @return true if successfully deleting requiring row, otherwise false
	 */
	public boolean deleteRowByDBKey(long dbKey){
		
		if(!isDBOpen()){
			open();
		}
		String whereClause = ChartsDBOpenHelper.COLUMN_MAIN_ID +"="+dbKey;
		return database.delete(ChartsDBOpenHelper.TABLE_CHARTS, whereClause, null) > 0;
	}
	/***
	 * Method to set dbId to each series data
	 * @param list that contains each series data
	 * @param dbId that generated from DB after inserting sql query
	 */
	private void setDBKeyOnEachDataModel(ArrayList<IDrawingChartInfo> list,long dbId){
		
		for (IDrawingChartInfo drawingChart : list) {
			drawingChart.setDbKey(dbId);
		}
	}
	

	/***
	 * Method to find all saved chart data
	 * @return  List<IChartModel> that contains all saved chart data
	 */
	public List<IChartModel> findAll(String selection, String orderBy){
		
		/**if database is not open*/
		if(!isDBOpen()){
			open();
		}
		
		/** create List that will be added saved chart data*/
		List<IChartModel> charts = new ArrayList<IChartModel>();
		
		Cursor cursor = database.query(ChartsDBOpenHelper.TABLE_CHARTS, allColumns, selection, null, null, null, orderBy);
		int numDBData = cursor.getCount();
		if(numDBData > 0){
			while(cursor.moveToNext()){
				
				long id = cursor.getLong(cursor.getColumnIndex(ChartsDBOpenHelper.COLUMN_MAIN_ID));
				int chartId = cursor.getInt(cursor.getColumnIndex(ChartsDBOpenHelper.COLUMN_CHART_ID));
				String title = cursor.getString(cursor.getColumnIndex(ChartsDBOpenHelper.COLUMN_TITLE));
				String date = cursor.getString(cursor.getColumnIndex(ChartsDBOpenHelper.COLUMN_DATE));
				String iconImageName = cursor.getString(cursor.getColumnIndex(ChartsDBOpenHelper.COLUMN_IMAGE_NAME));
				String seriesData = cursor.getString(cursor.getColumnIndex(ChartsDBOpenHelper.COLUMN_SREIES_DATA));
				ArrayList<IDrawingChartInfo> list = convertToListFromJson(chartId,title,seriesData);
				ChartData chart = new ChartData();
				chart.setDbKey(id);
				setDBKeyOnEachDataModel(list,id);
				chart.setChartId(chartId);
				chart.setMenuName(title);
				chart.setDate(date);
				chart.setImage(iconImageName);
				chart.setList(list);
				charts.add(chart);
			}
		}
		return charts;
	}
	/***
	 * Method to insert chart user creates into database
	 * @param ChartData 
	 * @return ChartData data that sets new insertId returned by DB.
	 */
	public ChartData insertChartData(ChartData data){
		
		try {
			ContentValues values = new ContentValues();
			
			values.put(ChartsDBOpenHelper.COLUMN_CHART_ID, data.getChartId());
			values.put(ChartsDBOpenHelper.COLUMN_TITLE, data.getMenuName());
			values.put(ChartsDBOpenHelper.COLUMN_IMAGE_NAME, data.getImage());
			values.put(ChartsDBOpenHelper.COLUMN_DATE, data.getDate());
			String seriesData = convertToJson(data.getList());
			values.put(ChartsDBOpenHelper.COLUMN_SREIES_DATA, seriesData);
			
			long insertId;
			boolean recordFound = isDataAlreadyInDB(data.getList().get(0).getDbKey());
			/** if record found, update instead of insert*/
			if(recordFound){
				String whereClause = ChartsDBOpenHelper.COLUMN_MAIN_ID + "="+data.getList().get(0).getDbKey();
				database.update(ChartsDBOpenHelper.TABLE_CHARTS, values, whereClause, null);
			}else{
				
				insertId = database.insert(ChartsDBOpenHelper.TABLE_CHARTS, null, values);
				data.setDbKey(insertId);
				ArrayList<IDrawingChartInfo> list = data.getList();
				setDBKeyOnEachDataModel(list,insertId);
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return data;
		
	}
	/***
	 * Method to figure out if record with given key is in DB
	 * @param dbKey that generated by DB after inserting sql query
	 * @return true if record with dbKey found, otherwise false
	 */
	private boolean isDataAlreadyInDB(long dbKey){
		
		/**if database is not open*/
		if(!isDBOpen()){
			open();
		}
	
		Cursor cursor = database.rawQuery("SELECT * FROM "+ChartsDBOpenHelper.TABLE_CHARTS+" WHERE "
				
				+ChartsDBOpenHelper.COLUMN_MAIN_ID+"="+dbKey, null);
		
		int rowCount = cursor.getCount();
		
		return rowCount > 0;
		
	}
	/***
	 * Method to get PointStyle object associated with name passed as parameter
	 * @param pointStyleName that is a name of PointStyle
	 * @return PointStyle object associated with name passed in this method
	 */
	private PointStyle getPointStyleBy(String pointStyleName){
		
//		HashMap<String, PointStyle> pointStyleMap = new HashMap<>();
		pointStyleMap.put("CIRCLE", PointStyle.CIRCLE);
		pointStyleMap.put("DIAMOND", PointStyle.DIAMOND);
		pointStyleMap.put("TRIANGLE", PointStyle.TRIANGLE);
		pointStyleMap.put("SQUARE", PointStyle.SQUARE);
	
		PointStyle pointStyle =  pointStyleMap.get(pointStyleName);
		return pointStyle;
		
	}
	
	private org.achartengine.renderer.DialRenderer.Type getTypeStyleBy(String typeName){
		
	//	HashMap<String, org.achartengine.renderer.DialRenderer.Type> typeStyleMap = new HashMap<String, org.achartengine.renderer.DialRenderer.Type>();
		typeStyleMap.put("Needle", org.achartengine.renderer.DialRenderer.Type.NEEDLE);
		typeStyleMap.put("Arrow", org.achartengine.renderer.DialRenderer.Type.ARROW);
		
		org.achartengine.renderer.DialRenderer.Type typeStyle = typeStyleMap.get(typeName);
		
		return typeStyle;
	}
	/***
	 * Method to create ChartDataModel, based on chart Id
	 * @param chartId
	 * @return IDrawingChartInfo
	 */
	private IDrawingChartInfo createChartDataModelBy(int chartId){
		
		IDrawingChartInfo drawingChartInfo = null;
		
		switch (chartId) {
		case 1:
			drawingChartInfo = new PieChartDataModel();
			break;
		case 2:
			drawingChartInfo = new LineChartDataModel();
			break;
		case 3:
		case 4:
			drawingChartInfo = new BarChartDataModel();
			break;
		case 5:
			drawingChartInfo = new DialChartDataModel();
			break;
			
		case 6:
			drawingChartInfo = new ScatterChartDataModel();
			break;

		}
		return drawingChartInfo;
	}
	/***
	 * Method to convert to list from series data formed in JSON 
	 * @param chartId that represents type of chart
	 * @param title title of chart
	 * @param seriesData that contains series data
	 * @return list contains IDrawingChartInfo
	 */
	private ArrayList<IDrawingChartInfo> convertToListFromJson(int chartId,String title,String seriesData){
		
		try {
			Gson gson = new Gson();
			Type doubleArraytype = new TypeToken<double[]>(){}.getType();
			Type stringArraytype = new TypeToken<String[]>(){}.getType();
			JSONArray jsonArr = new JSONArray(seriesData);
			
			ArrayList<IDrawingChartInfo> list = new ArrayList<IDrawingChartInfo>();
			for (int i = 0; i < jsonArr.length(); i++) {
				
				IDrawingChartInfo drawingChartInfo = createChartDataModelBy(chartId);
				drawingChartInfo.setChartId(chartId);
				drawingChartInfo.setTitle(title);
				
				String seriesName = jsonArr.getJSONObject(i).getString(ChartConstantData.SERIES_NAME);
				drawingChartInfo.setSeriesName(seriesName);
				int colorValue = jsonArr.getJSONObject(i).getInt(ChartConstantData.COLOR_VALUE);
				drawingChartInfo.setColorPicked(colorValue);
				String xAxisName = jsonArr.getJSONObject(i).getString(ChartConstantData.X_AXIS_NAME);
				drawingChartInfo.setXaxis_name(xAxisName);
				String yAxisName = jsonArr.getJSONObject(i).getString(ChartConstantData.Y_AXIS_NAME);
				drawingChartInfo.setYaxis_name(yAxisName);
				
				String x = jsonArr.getJSONObject(i).getString(ChartConstantData.X_VALUES);
				double[]  listX = gson.fromJson(x, doubleArraytype);
				drawingChartInfo.setXValues(listX);
				String y = jsonArr.getJSONObject(i).getString(ChartConstantData.Y_VALUES);
				double[]  listY = gson.fromJson(y, doubleArraytype);
				drawingChartInfo.setYValues(listY);
				
				String xstringValues = jsonArr.getJSONObject(i).getString(ChartConstantData.X_STRING_VALUES);
				String[] list_String_X = gson.fromJson(xstringValues, stringArraytype);
				drawingChartInfo.setX_StringValues(list_String_X);
				
				double value =  jsonArr.getJSONObject(i).getDouble(ChartConstantData.VALUE);
				drawingChartInfo.setValue(value);
				
				double minX = jsonArr.getJSONObject(i).getDouble(ChartConstantData.X_MIN);
				drawingChartInfo.setMinX(minX);
				double maxX = jsonArr.getJSONObject(i).getDouble(ChartConstantData.X_MAX);
				drawingChartInfo.setMaxX(maxX);
				
				double minY = jsonArr.getJSONObject(i).getDouble(ChartConstantData.Y_MIN);
				drawingChartInfo.setMinY(minY);
				
				double maxY = jsonArr.getJSONObject(i).getDouble(ChartConstantData.Y_MAX);
				drawingChartInfo.setMaxY(maxY);
				
				String pointStyleName = jsonArr.getJSONObject(i).getString(ChartConstantData.POINT_STYLE_NAME);
				drawingChartInfo.setPointStyleName(pointStyleName);
				
				PointStyle pointStyle = getPointStyleBy(pointStyleName);
				drawingChartInfo.setPointStyle(pointStyle);
				
				int xValueTypedrequired = jsonArr.getJSONObject(i).getInt(ChartConstantData.X_VALUE_TYPE_REQUIRED);
				drawingChartInfo.setxValueTypeRequired(xValueTypedrequired);
				int yValueTypedrequired = jsonArr.getJSONObject(i).getInt(ChartConstantData.Y_VALUE_TYPE_REQUIRED);
				drawingChartInfo.setxValueTypeRequired(yValueTypedrequired);
				
				boolean isShowChartValues = jsonArr.getJSONObject(i).getBoolean(ChartConstantData.IS_SHOW_CHART_VALUES);
				drawingChartInfo.setShowChartValues(isShowChartValues);
				
				String typeName = jsonArr.getJSONObject(i).getString(ChartConstantData.TYPE_NAME);
				drawingChartInfo.setTypeName(typeName);
				list.add(drawingChartInfo);
				drawingChartInfo.setType(getTypeStyleBy(typeName));
				////Need to set Type object associated with type name////////////////////////////////////
			}
			return list;
			
		} catch (JSONException e) {
			
			e.printStackTrace();
			return null;
		}
		
	}
	/***
	 *  Method to convert series data to one chunk of string that is Json format.
	 * @param list
	 * @return converted string formatted list
	 */
	private String convertToJson(List<IDrawingChartInfo> list){
		
		Gson gson = new Gson();
		JSONArray data = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
		
			try {
				JSONObject json = new JSONObject();
				json.put(ChartConstantData.SERIES_NAME, list.get(i).getSeriesName());
				json.put(ChartConstantData.COLOR_VALUE, list.get(i).getColorPicked());
				json.put(ChartConstantData.X_AXIS_NAME, list.get(i).getXaxis_name());
				json.put(ChartConstantData.Y_AXIS_NAME,list.get(i).getYaxis_name());
				json.put(ChartConstantData.X_VALUES, gson.toJson(list.get(i).getXValues()));
				json.put(ChartConstantData.Y_VALUES, gson.toJson(list.get(i).getYValues()));
				json.put(ChartConstantData.X_STRING_VALUES, gson.toJson(list.get(i).getX_StringValues()));
				json.put(ChartConstantData.VALUE, list.get(i).getValue());
				json.put(ChartConstantData.X_MIN, list.get(i).getMinX());
				json.put(ChartConstantData.X_MAX, list.get(i).getMaxX());
				json.put(ChartConstantData.Y_MIN, list.get(i).getMinY());
				json.put(ChartConstantData.Y_MAX, list.get(i).getMaxY());
				json.put(ChartConstantData.POINT_STYLE_NAME,  list.get(i).getPointStyleName());
				json.put(ChartConstantData.X_VALUE_TYPE_REQUIRED, list.get(i).getxValueTypeRequired());
				json.put(ChartConstantData.Y_VALUE_TYPE_REQUIRED, list.get(i).getyValueTypeRequired());
				json.put(ChartConstantData.IS_SHOW_CHART_VALUES,  list.get(i).isShowChartValues());
				json.put(ChartConstantData.TYPE_NAME, list.get(i).getTypeName());
				data.put(json);
				
				
			} catch (JSONException e) {
			
				e.printStackTrace();
				return null;
			}
		}
		return data.toString();
	}
}
