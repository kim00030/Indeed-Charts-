package com.ilhyungkim.indeedcharts.interfaces;

import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.DialRenderer.Type;


public interface IDrawingChartInfo{

	/**DB key */
	public long getDbKey();
	public void setDbKey(long dbKey);
	/** id based on List's index*/
	public int getId();
	public void setId(int id);
	public int getChartId();
	public void setChartId(int chartId);
	
	public void setColorPicked(int colorPicked);
	public int getColorPicked();
	public void setTitle(String t);
	public String getTitle();
	public String getXaxis_name();
	public void setXaxis_name(String xAxis_name);
	public String getYaxis_name();
	public void setYaxis_name(String yAxis_name);

	public void setXValues(double[] d);
	public double[] getXValues();
	public String[] getX_StringValues(); 
	public void setX_StringValues(String[] x_StringValues);
	
	public void setYValues(double[] d);
	public double[] getYValues();
	
	public double getValue();
	public void setValue(double value);
	
	public double getXValueByIndex(int index);
	public double getYValueByIndex(int index);
	public String getX_StringValueByIndex(int index);
	
	public double getMinX();
	public void setMinX(double minX);
	public double getMaxX();
	public void setMaxX(double maxX);
	public double getMinY();
	public void setMinY(double minY);
	public double getMaxY();
	public void setMaxY(double maxY);
	
	public String getSeriesName();
	public void setSeriesName(String seriesName);

	public PointStyle getPointStyle();
	public void setPointStyle(PointStyle pointStyle);
	public String getPointStyleName();
	public void setPointStyleName(String pointStyleName);
	
	public int getxValueTypeRequired();
	public void setxValueTypeRequired(int xValueTypeRequired);
	public int getyValueTypeRequired();
	public void setyValueTypeRequired(int yValueTypeRequired);
	public boolean isShowChartValues();
	public void setShowChartValues(boolean showChartValues);

	public Type getType();
	public void setType(Type type);
	public String getTypeName();
	public void setTypeName(String typeName);
}
