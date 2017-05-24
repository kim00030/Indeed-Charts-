package com.ilhyungkim.indeedcharts.model;

import org.achartengine.renderer.DialRenderer.Type;

import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;

public class DialChartDataModel extends ChartDataModel implements IDrawingChartInfo{

	private boolean showChartValues = true;
	
	private double value = -1;
	
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
	public String getSeriesName() {
		return super.getSeriesName();
	}

	public void setSeriesName(String seriesName) {
		super.setSeriesName(seriesName);
	}
	
	@Override
	public boolean isShowChartValues() {
		return showChartValues;
	}

	@Override
	public void setShowChartValues(boolean showChartValues) {
		this.showChartValues = showChartValues;
	}
	public void setColorPicked(int colorPicked) {
		
		super.setColorPicked(colorPicked);
	}
	public int getColorPicked() {
		
		return super.getColorPicked();
	}

	public Type getType() {
		return super.getType();
	}

	public void setType(Type type) {
		super.setType(type);
	}
	
	public String getTypeName() {
		return super.getTypeName();
	}

	public void setTypeName(String typeName) {
		super.setTypeName(typeName);
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
	public void setYValues(double[] d) {
		
	}

	@Override
	public double[] getYValues() {
		return null;
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

}
