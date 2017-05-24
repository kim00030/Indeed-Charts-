package com.ilhyungkim.indeedcharts.drawinghelper;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.ilhyungkim.indeedcharts.constantdata.ChartConstantData;
import com.ilhyungkim.indeedcharts.drawinghelper.sampledata.SampleDataCreator;
import com.ilhyungkim.indeedcharts.interfaces.IBarChartActivity;
import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;
import com.ilhyungkim.indeedcharts.model.SeriesData;
import com.ilhyungkim.indeedcharts.model.wrappers.DataWrapper;
import com.ilhyungkim.indeedcharts.utilities.ChartHelper;

public class BarChartDrawingHelper extends DrawingHelper{

	private static final int STACKED_BAR = 4;
	
	protected XYMultipleSeriesDataset dataset;
	protected XYMultipleSeriesRenderer renderer;
	protected List<Integer> colors;
	protected boolean showChartValues = true;
	//minimum x
	double minX = -1;
	//maximum x
	double maxX = -1;
	//minimum y
	double minY = -1;
	//maximum y
	double maxY = -1;
	
	protected GraphicalView chartView;
	protected LinearLayout layout;
	
	public BarChartDrawingHelper(Activity pActivity,int pDrawingCanvasId){
		
		super(pActivity,pDrawingCanvasId);
		
	}
	/***
	 * 
	 * @param list		List containing current series data 
	 * @param dataWrapper	Wrapper object that has List containing new series data
	 * @param pDataset 	Dataset in current thread
	 * @param pRenderer Renderer in current thread
	 */
	public void updateSeriesName(List<IDrawingChartInfo> list, DataWrapper dataWrapper,XYMultipleSeriesDataset pDataset,XYMultipleSeriesRenderer pRenderer){
		
		/** retrieve data containing new series data */
		List<SeriesData> seriesDatas = dataWrapper.getSeriesDatas();
		/** must be same size, otherwise something went wrong */
		if(list.size() == seriesDatas.size()){
			for (int i = 0; i < list.size(); i++) {
				/** update only series name user typed in */
				if (seriesDatas.get(i).getNewSeriesName().trim().length()>0) {
					list.get(i).setSeriesName(seriesDatas.get(i).getNewSeriesName());
				}
			}
			/**Take redraw process with new updated*/
			draw(list,pDataset,pRenderer,null);
		}
		
	}
	public void draw(List<IDrawingChartInfo> list,XYMultipleSeriesDataset pDataset,XYMultipleSeriesRenderer pRenderer,
			String action)
	{
		//Initialize
		if(renderer != null){
			renderer = null;
		}
		if(chartView != null){
			chartView = null;
		}
		//Reset data set and renderer 
		if(pDataset != null){
			dataset = pDataset;
		}
		if(pRenderer != null){
			renderer= pRenderer;
		}
		int listSize = list.size();
		
		//For series names
		String[] seriesNames = new String[listSize];
		//for x values
		List<String[]> x = new ArrayList<String[]>();
		//for y values
		List<double[]> y = new ArrayList<double[]>();
		//for color data
		colors = new ArrayList<Integer>();
		
		for (int i = 0; i < listSize; i++) {
			
			//set Main Title
			if(list.get(i).getTitle() != null){
				mainTitle = list.get(i).getTitle();
			}else{
				list.get(i).setTitle(mainTitle);
			}
			//set X - axis name
			if(list.get(i).getXaxis_name() != null){
				xAxis_name = list.get(i).getXaxis_name();
			}else{
				list.get(i).setXaxis_name(xAxis_name);
			}
			//set y - axis name
			if(list.get(i).getYaxis_name() != null){
				yAxis_name = list.get(i).getYaxis_name();
			}else{
				list.get(i).setYaxis_name(yAxis_name);
			}
			//set series names
			seriesNames[i] = list.get(i).getSeriesName() != null ? list.get(i).getSeriesName():ChartHelper.getDefaultSeriesName(i+1,"Series");
			list.get(i).setSeriesName(seriesNames[i]);
			//set x Min and Max values AND x values
			if(action == ACTION_ADD_SERIES && list.get(i).getxValueTypeRequired() == ChartConstantData.SAMPLE_DATA){
				
				Bundle sampleData = SampleDataCreator.createSampleDataByChartId("x", list);//generateSampleData("x",list);
				x.add(sampleData.getStringArray(ChartConstantData.VALUES));
				list.get(i).setX_StringValues(sampleData.getStringArray(ChartConstantData.VALUES));
				
				minX = sampleData.getDouble(ChartConstantData.X_MIN);
				maxX = sampleData.getDouble(ChartConstantData.X_MAX);
				list.get(i).setMinX(minX);
				list.get(i).setMaxX(maxX);
				list.get(i).setxValueTypeRequired(ChartConstantData.USER_DEFINED_DATA);
			}else{
				
				x.add(list.get(i).getX_StringValues());
				minX = list.get(i).getMinX();
				maxX = list.get(i).getMaxX();
				list.get(i).setMinX(minX);
				list.get(i).setMaxX(maxX);
				list.get(i).setxValueTypeRequired(ChartConstantData.USER_DEFINED_DATA);
			}
			//set y Min and Max values AND y values
			if(action == ACTION_ADD_SERIES && list.get(i).getyValueTypeRequired() == ChartConstantData.SAMPLE_DATA){
				
				Bundle sampleData = SampleDataCreator.createSampleDataByChartId("y", list);//generateSampleData("y",list);
				y.add(sampleData.getDoubleArray(ChartConstantData.VALUES));
				list.get(i).setYValues(sampleData.getDoubleArray(ChartConstantData.VALUES));
				
				minY = sampleData.getDouble(ChartConstantData.Y_MIN);
				maxY = sampleData.getDouble(ChartConstantData.Y_MAX);
				list.get(i).setMinY(minY);
				list.get(i).setMaxY(maxY);
				list.get(i).setyValueTypeRequired(ChartConstantData.USER_DEFINED_DATA);
				
			}else{
				
				y.add(list.get(i).getYValues());
				minX = list.get(i).getMinX();
				maxX = list.get(i).getMaxX();
				list.get(i).setMinX(minX);
				list.get(i).setMaxX(maxX);
				
				minY = list.get(i).getMinY();
				maxY = list.get(i).getMaxY();
				list.get(i).setMinY(minY);
				list.get(i).setMaxY(maxY);
				list.get(i).setyValueTypeRequired(ChartConstantData.USER_DEFINED_DATA);

			}
			
			int color = list.get(i).getColorPicked() == 0? ChartHelper.getRandomColor():list.get(i).getColorPicked();
			colors.add(color);
			list.get(i).setColorPicked(color);
			showChartValues = list.get(0).isShowChartValues();
			
		}//END for
		
		try {
			mainTitle = list.get(0).getTitle();
			xAxis_name = list.get(0).getXaxis_name();
			yAxis_name = list.get(0).getYaxis_name();
		} catch (Exception e) {
		
			e.printStackTrace();
		}
		
		if(renderer == null){
			renderer = new XYMultipleSeriesRenderer();
			//renderer.setXLabels(12);
			renderer.setYLabels(10);
			renderer.setShowGrid(true);
			renderer.setXLabelsAlign(Align.CENTER);
			renderer.setYLabelsAlign(Align.RIGHT);
			renderer.setZoomButtonsVisible(true);
			renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
			renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });
			renderer.setXLabels(0);
			
			for(int i = 0; i<x.size();i++){
				
				String[] xAxis = x.get(i);
				for(int k = 0; k<xAxis.length;k++){
					renderer.addXTextLabel(k+1,xAxis[k]);
				}
			}
			
		}
		//code if(...)below executes ONLY when requesting action is add series or redrawing is in need
		if(action == ACTION_ADD_SERIES || action == ACTION_REDRAW){
			setRenderer(renderer, colors,showChartValues);
		}

		setMaxY(list);
		 //(XYMultipleSeriesRenderer renderer, String title, String xTitle, String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor)
		 setChartSettings(renderer, mainTitle, xAxis_name, yAxis_name, minX, maxX, minY, maxY,
		        Color.LTGRAY, Color.LTGRAY);

		 XYMultipleSeriesDataset currentDataset = buildDataset(seriesNames, y);
		
		 getBarChartView(currentDataset,renderer, list.get(0).getChartId());
		 //save current dataset in main 
		 ((IBarChartActivity)activity).setCurrentDataset(currentDataset);
		 //save current renderer in main 
		 ((IBarChartActivity)activity).setmRenderer(renderer);
	}
	/***
	 * Method to figure out maxY value among y values user entered
	 * @param list
	 */
	protected void setMaxY(List<IDrawingChartInfo> list) {
		
		double largest = ChartConstantData.DEFAULT_Y_MAX;
		
		for (int i = 0; i < list.size(); i++) {
			
			double[] d = list.get(i).getYValues();
			for (int j = 0; j < d.length; j++) {
				if(d[j] >= largest){
					maxY = d[j] + 50;
					largest = maxY;
				}
			}
		}
	
	}
	@SuppressWarnings("deprecation")
	public void getBarChartView(XYMultipleSeriesDataset currentDataset,XYMultipleSeriesRenderer renderer,int charId){
		
		
		if(chartView == null){
			Type type = Type.DEFAULT;
			if(charId == STACKED_BAR){
				type = Type.STACKED;
			}
		
			
			chartView = ChartFactory.getBarChartView(activity, currentDataset, renderer, type);
			layout = (LinearLayout) activity.findViewById(drawingCanvasId);
			int num = layout.getChildCount();
			if(num > 0){
				layout.removeAllViews();
			}
			layout.addView(chartView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
			chartView.repaint();
		}else{
			
			chartView.repaint();
		}
		

	}
	
	 protected XYMultipleSeriesDataset buildDataset(String[] seriesNames,List<double[]> yValues) {
		 
		 dataset = new XYMultipleSeriesDataset();
		 
		 addXYSeries(dataset, seriesNames, yValues, 0);
		 return dataset;
		 
	 }
	 public void addXYSeries(XYMultipleSeriesDataset dataset, String[] sereisNames,List<double[]> yValues, int scale) {
		 
		 int length = sereisNames.length;
		 for(int i = 0;i<length;i++){
			 CategorySeries series = new CategorySeries(sereisNames[i]);
			 double[] yV = yValues.get(i);
			 int seriesLength = yV.length;
			 for (int k = 0; k < seriesLength; k++) {
				 series.add("Bar "+(k+1), yV[k]);
		     }
			 dataset.addSeries(series.toXYSeries());
			
		 }

	 }
	/**
	   * Sets a few of the series renderer settings.
	   * 
	   * @param renderer the renderer to set the properties to
	   * @param title the chart title
	   * @param xTitle the title for the X axis
	   * @param yTitle the title for the Y axis
	   * @param xMin the minimum value on the X axis
	   * @param xMax the maximum value on the X axis
	   * @param yMin the minimum value on the Y axis
	   * @param yMax the maximum value on the Y axis
	   * @param axesColor the axes color
	   * @param labelsColor the labels color
	   */
	protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
		String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
	      int labelsColor) {
	    renderer.setChartTitle(title);
	    renderer.setXTitle(xTitle);
	    renderer.setYTitle(yTitle);
	    renderer.setXAxisMin(xMin);
	    renderer.setXAxisMax(xMax);
	    renderer.setYAxisMin(yMin);
	    renderer.setYAxisMax(yMax);
	    renderer.setAxesColor(axesColor);
	    renderer.setLabelsColor(labelsColor);
	}
	protected void setRenderer(XYMultipleSeriesRenderer renderer, List<Integer> colors,boolean showChartValues) {
		
	 /**for axis labels*/
		renderer.setAxisTitleTextSize(ChartConstantData.AXIS_TITLE_SIZE);
	  /**for Main title*/
		renderer.setChartTitleTextSize(ChartConstantData.MAIN_TITLE_SIZE);
		renderer.setZoomButtonsVisible(true);
		renderer.setLabelsTextSize(ChartConstantData.LABELS_SIZE);
		renderer.setLegendTextSize(ChartConstantData.LEGEND_SIZE);//20
		renderer.setMargins(new int[] {50, 80, 100, 22});//50, 50, 50, 22
	  
		int length = colors.size();
		for (int i = 0; i < length; i++) {
		  
		  XYSeriesRenderer r = new XYSeriesRenderer();
		  r.setColor(colors.get(i));
		  r.setDisplayChartValues(showChartValues);
		  r.setChartValuesTextAlign(Align.RIGHT);// why this makes center align? 
		  r.setChartValuesTextSize(20);
		  r.setChartValuesSpacing(10);
		  renderer.addSeriesRenderer(r);
		}
	  	
	}
	
}
