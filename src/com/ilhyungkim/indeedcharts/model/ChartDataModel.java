package com.ilhyungkim.indeedcharts.model;

import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.DialRenderer.Type;

import com.ilhyungkim.indeedcharts.R;
import com.ilhyungkim.indeedcharts.constantdata.ChartConstantData;
import com.ilhyungkim.indeedcharts.maneger.SavableDataManager;


public class ChartDataModel {
	
	private long dbKey = -1;
	private int id = -1;
	private int chartId = -1;
	private String title = SavableDataManager.getInstance().context.getResources().getString(R.string.chart_default_title);
	
	private String seriesName; //= ChartConstantData.SERIES_NAME;

	private String xAxis_name = ChartConstantData.X_AXIS_NAME;
	private String yAxis_name = ChartConstantData.Y_AXIS_NAME;

	private int colorPicked = 0;
	private PointStyle pointStyle = PointStyle.CIRCLE;
	private String pointStyleName = "CIRCLE";
	private Type type = Type.NEEDLE;
	private String typeName = "Needle";

	private int xValueTypeRequired = ChartConstantData.SAMPLE_DATA;
	private int yValueTypeRequired = ChartConstantData.SAMPLE_DATA;

	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	
	
	
	public long getDbKey() {
		return dbKey;
	}
	public void setDbKey(long dbKey) {
		this.dbKey = dbKey;
	}
	public int getId() {

		return this.id;
	}
	/***
	 *  set Id based on index of List where this data model stores in
	 * @param id
	 */
	public void setId(int id) {

		this.id =id;
	}
	public int getChartId() {
		return chartId;
	}
	/***
	 *  set ChartType Id. each drawing activity has its own id.
	 *  See{@code} /res/raw/charts.xml
	 * @param chartId
	 */
	public void setChartId(int chartId) {
		this.chartId = chartId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {

		this.title = title;
		
	}

	public int getColorPicked() {
		return colorPicked;
	}
	public void setColorPicked(int colorPicked) {
		this.colorPicked = colorPicked;
	}
	public String getXaxis_name() {
		return this.xAxis_name;
	}
	public void setXaxis_name(String xAxis_name) {
		this.xAxis_name = xAxis_name;
	}
	public String getYaxis_name() {
		return this.yAxis_name;
	}
	public void setYaxis_name(String yAxis_name) {
		this.yAxis_name = yAxis_name;
	}
	public double getMinX() {

		return this.minX;
	}
	public void setMinX(double minX) {
		this.minX = minX;
	}

	public double getMaxX() {

		return this.maxX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public double getMinY() {

		return this.minY;
	}
	public void setMinY(double minY) {
		this.minY = minY;
	}

	public double getMaxY() {

		return this.maxY;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}
	public PointStyle getPointStyle() {

		return this.pointStyle;
	}


	public void setPointStyle(PointStyle pointStyle) {
		this.pointStyle = pointStyle;
	}


	public String getPointStyleName() {

		return this.pointStyleName;
	}

	public void setPointStyleName(String pointStyleName) {
		this.pointStyleName = pointStyleName;
	}
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getSeriesName() {
		return this.seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}
	public int getxValueTypeRequired() {
		return xValueTypeRequired;
	}
	public void setxValueTypeRequired(int xValueTypeRequired) {
		this.xValueTypeRequired = xValueTypeRequired;
	}

	public int getyValueTypeRequired() {
		return yValueTypeRequired;
	}
	public void setyValueTypeRequired(int yValueTypeRequired) {
		this.yValueTypeRequired = yValueTypeRequired;
	}
}
