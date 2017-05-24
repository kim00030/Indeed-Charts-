package com.ilhyungkim.indeedcharts.model;

import java.util.ArrayList;

import com.ilhyungkim.indeedcharts.interfaces.IChartModel;
import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;

public class ChartData implements IChartModel{
	

	private long dbKey = -1;
	private int chartId = -1;
	private String menuName = null;
	private String image = null;
	private String description = null;
	private String date = null;
	private ArrayList<IDrawingChartInfo> list = null;
	
	/***
	 * 
	 * @return id set by DB
	 */
	public long getDbKey() {
		return this.dbKey;
	}
	/***
	 * 
	 * @param dbKey set by DB
	 */
	public void setDbKey(long dbKey) {
		this.dbKey = dbKey;
	}
	/***
	 * To get id representing type of charts, defined in charts.xml
	 * @return id representing type of charts
	 */
	public int getChartId() {
		return chartId;
	}
	/***
	 * To set id representing type of charts, defined in charts.xml
	 * @param id representing type of charts
	 */
	public void setChartId(int id) {
		this.chartId = id;
	}
	public String getMenuName() {
		return menuName;
	}
	/***
	 * Method to set title that will be menu name in saved chart list page
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	/***
	 * Method to get name of chart image file without file extension
	 */
	public String getImage() {
		return image;
	}
	/***
	 * Method to set name of chart image file without file extension
	 */
	public void setImage(String image) {
		this.image = image;
	}
	/***
	 * Method to get chart's description showed in Main page
	 */
	public String getDescription() {
		return description;
	}
	
	/***
	 * Method to set chart's description showed in Main page
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/***
	 * Method to get list that contains IDrawingChartInfo 
	 * @return list that contains IDrawingChartInfo
	 */
	public ArrayList<IDrawingChartInfo> getList() {
		return list;
	}

	/***
	 * Method to get list that contains IDrawingChartInfo 
	 * 
	 */
	public void setList(ArrayList<IDrawingChartInfo> list) {
		this.list = list;
	}
	/***
	 * 
	 * @return date that saved in DB
	 */
	public String getDate() {
		return date;
	}
	/***
	 * 
	 * @param date that saved in DB
	 */
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public String toString() {
		
		return this.getMenuName();
	}
}
