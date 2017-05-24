package com.ilhyungkim.indeedcharts.model;

import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.DialRenderer.Type;

import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;

public class PieChartDataModel extends ChartDataModel implements IDrawingChartInfo{
	
	
	private double value = -1;
	private boolean showChartValues = true;
	
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
	public void setTitle(String title) {
		
		super.setTitle(title);
	}
	@Override
	public String getTitle() {
		
		return super.getTitle();
	}
	public String getSeriesName() {
		return super.getSeriesName();
	}

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
	public void setXValues(double[] d) {
		
	}
	@Override
	public double[] getXValues() {
		return null;
	}
	@Override
	public String[] getX_StringValues() {
		return null;
	}
	@Override
	public void setX_StringValues(String[] x_StringValues) {
		
	}
	
	@Override
	public double getXValueByIndex(int index) {
		return 0;
	}
	@Override
	public double getYValueByIndex(int index) {
		return 0;
	}
	@Override
	public String getX_StringValueByIndex(int index) {
		
		return null;
	}
	@Override
	public boolean isShowChartValues() {
		
		return showChartValues;
	}
	@Override
	public void setShowChartValues(boolean showChartValues) {
		this.showChartValues = showChartValues;
	}
		
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	public int getyValueTypeRequired() {
		return super.getyValueTypeRequired();
	}
	public void setyValueTypeRequired(int yValueTypeRequired) {
		super.setyValueTypeRequired(yValueTypeRequired);
	}
	@Override
	public void setYValues(double[] d) {
		
		
	}
	@Override
	public double[] getYValues() {
		
		return null;
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
}
