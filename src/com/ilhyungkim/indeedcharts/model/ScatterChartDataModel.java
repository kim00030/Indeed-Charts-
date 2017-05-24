package com.ilhyungkim.indeedcharts.model;

import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.DialRenderer.Type;

import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;

public class ScatterChartDataModel extends ChartDataModel implements IDrawingChartInfo{

	private double[] xValues;
	private double[] yValues;
	
	@Override
	public long getDbKey() {
		return super.getDbKey();
	}
	@Override
	public void setDbKey(long dbKey) {
		super.setDbKey(dbKey);
	}
	public int getId() {
		return super.getId();
	}
	public void setId(int id) {
		super.setId(id);
	}
	
	public int getChartId() {
		return super.getChartId();
	}
	
	public void setChartId(int chartId) {
		super.setChartId(chartId);
	}
	
	@Override
	public void setXValues(double[] d) {
		
		this.xValues = d;
	}

	@Override
	public double[] getXValues() {
		
		return this.xValues;
	}

	@Override
	public void setYValues(double[] d) {
		this.yValues = d;
	}
	
	@Override
	public double[] getYValues() {
		
		return this.yValues;
	}

	
	@Override
	public double getXValueByIndex(int index) {
		
		return this.xValues[index];
	}
	
	@Override
	public double getYValueByIndex(int index) {
		
		return this.yValues[index];
	}
	
	public int getxValueTypeRequired() {
		return super.getxValueTypeRequired();
	}
	public void setxValueTypeRequired(int xValueTypeRequired) {
		super.setxValueTypeRequired(xValueTypeRequired);
	}
	public int getyValueTypeRequired() {
		return super.getyValueTypeRequired();
	}
	public void setyValueTypeRequired(int yValueTypeRequired) {
		super.setyValueTypeRequired(yValueTypeRequired);
	}
	public double getMinX() {
		return super.getMinX();
	}
	public void setMinX(double minX) {
		super.setMinX(minX);
	}
	public double getMaxX() {
		return super.getMaxX();
	}
	public void setMaxX(double maxX) {
		super.setMaxX(maxX);
	}
	public double getMinY() {
		return super.getMinY();
	}
	public void setMinY(double minY) {
		super.setMinY(minY);
	}
	public double getMaxY() {
		return super.getMaxY();
	}
	public void setMaxY(double maxY) {
		super.setMaxY(maxY);
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
	@Override
	public String getSeriesName() {
		return super.getSeriesName();
	}
	@Override
	public void setSeriesName(String seriesName) {
		super.setSeriesName(seriesName);
	}
	
	@Override
	public void setColorPicked(int colorPicked) {
		
		super.setColorPicked(colorPicked);
	}
	@Override
	public int getColorPicked() {
		
		return super.getColorPicked();
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
	
	
	
	@Override
	public String[] getX_StringValues() {
		
		return null;
	}

	@Override
	public void setX_StringValues(String[] x_StringValues) {
		
		
	}


	@Override
	public double getValue() {
		
		return 0;
	}

	@Override
	public void setValue(double value) {
		
		
	}

	@Override
	public String getX_StringValueByIndex(int index) {
		
		return null;
	}

	@Override
	public boolean isShowChartValues() {
		
		return false;
	}

	@Override
	public void setShowChartValues(boolean showChartValues) {
	
	}

	@Override
	public Type getType() {
		
		return null;
	}

	@Override
	public void setType(Type type) {
	
	}

	@Override
	public String getTypeName() {
		return super.getTypeName();
	}
	@Override
	public void setTypeName(String typeName) {
		super.setTypeName(typeName);
	}

}
