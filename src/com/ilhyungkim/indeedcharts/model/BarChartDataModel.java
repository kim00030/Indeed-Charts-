package com.ilhyungkim.indeedcharts.model;

import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.DialRenderer.Type;

import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;

public class BarChartDataModel extends ChartDataModel implements IDrawingChartInfo{

	private double[] yValues;
	private String[] x_StringValues;
	private boolean showChartValues = true;
	
	@Override
	public long getDbKey() {
		return super.getDbKey();
	}
	@Override
	public void setDbKey(long dbKey) {
		super.setDbKey(dbKey);
	}
	
	@Override
	public int getId() {
		
		return super.getId();
	}

	@Override
	public void setId(int id) {
		
		super.setId(id);
	}
	@Override
	public int getChartId() {
		return super.getChartId();
	}
	@Override
	public void setChartId(int chartId) {
		super.setChartId(chartId);
	}
	
	@Override
	public void setTitle(String t){
		
		super.setTitle(t);
	}
	@Override
	public String getTitle(){
		
		return super.getTitle();
	}
	@Override
	public void setXValues(double[] d) {
	}

	@Override
	public double[] getXValues() {
		
		return null;
	}

	@Override
	public void setYValues(double[] d) {
		
		this.yValues = d;
	}

	@Override
	public double[] getYValues() {
		
		return this.yValues;
	}

	public String[] getX_StringValues() {
		return x_StringValues;
	}

	public void setX_StringValues(String[] x_StringValues) {
		this.x_StringValues = x_StringValues;
	}
	public String getX_StringValueByIndex(int index){
		
		return this.x_StringValues[index];
	}
	
	@Override
	public double getXValueByIndex(int index) {
		
		return 0;
	}

	@Override
	public double getYValueByIndex(int index) {
		
		if(this.yValues.length <= index){
			
			return 0;
		}else{
			return this.yValues[index];
		}
	}

	@Override
	public double getMinX() {
		
		return super.getMinX();
	}

	@Override
	public void setMinX(double minX) {
		super.setMinX(minX);
	}

	@Override
	public double getMaxX() {
		
		return super.getMaxX();
	}

	@Override
	public void setMaxX(double maxX) {
		super.setMaxX(maxX);
	}

	@Override
	public double getMinY() {
	
		return super.getMinY();
	}

	@Override
	public void setMinY(double minY) {
		super.setMinY(minY);
	}

	@Override
	public double getMaxY() {
		
		return super.getMaxY();
	}

	@Override
	public void setMaxY(double maxY) {
		super.setMaxY(maxY);
	}

	@Override
	public String getSeriesName() {
	
		return super.getSeriesName();
	}

	@Override
	public void setSeriesName(String seriesName) {
		
		super.setSeriesName(seriesName);
	}

	@Override
	public PointStyle getPointStyle() {
	
		return super.getPointStyle();
	}

	@Override
	public void setPointStyle(PointStyle pointStyle) {
		
		super.setPointStyle(pointStyle);
	}

	@Override
	public String getPointStyleName() {
		
		return super.getPointStyleName();
	}

	@Override
	public void setPointStyleName(String pointStyleName) {
		super.setPointStyleName(pointStyleName);
	}

	public boolean isShowChartValues() {
		return showChartValues;
	}

	public void setShowChartValues(boolean showChartValues) {
		this.showChartValues = showChartValues;
	}

	@Override
	public int getxValueTypeRequired() {
		
		return super.getxValueTypeRequired();
	}

	@Override
	public void setxValueTypeRequired(int xValueTypeRequired) {
		super.setxValueTypeRequired(xValueTypeRequired);
	}

	@Override
	public int getyValueTypeRequired() {
		return super.getyValueTypeRequired();
	}

	@Override
	public void setyValueTypeRequired(int yValueTypeRequired) {
		super.setyValueTypeRequired(yValueTypeRequired);
	}
	@Override
	public String getXaxis_name() {
		return super.getXaxis_name();
	}
	@Override
	public void setXaxis_name(String xAxis_name) {
		super.setXaxis_name(xAxis_name);
	}
	@Override
	public String getYaxis_name() {
		return super.getYaxis_name();
	}
	@Override
	public void setYaxis_name(String yAxis_name) {
		super.setYaxis_name(yAxis_name);
	}
	public void setColorPicked(int colorPicked) {
		
		super.setColorPicked(colorPicked);
	}
	public int getColorPicked() {
		
		return super.getColorPicked();
	}


	@Override
	public Type getType() {
		return super.getType();
	}
	@Override
	public void setType(Type type) {
		super.setType(type);
	}
	@Override
	public String getTypeName() {
		return super.getTypeName();
	}
	@Override
	public void setTypeName(String typeName) {
		super.setTypeName(typeName);
	}

	@Override
	public double getValue() {
		
		return 0;
	}

	@Override
	public void setValue(double value) {
	
	}

}
