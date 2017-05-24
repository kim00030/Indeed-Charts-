package com.ilhyungkim.indeedcharts.drawinghelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.ilhyungkim.indeedcharts.LineChartActivity;
import com.ilhyungkim.indeedcharts.constantdata.ChartConstantData;
import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;
import com.ilhyungkim.indeedcharts.maneger.DialogManager;
import com.ilhyungkim.indeedcharts.model.SeriesData;
import com.ilhyungkim.indeedcharts.model.wrappers.DataWrapper;
import com.ilhyungkim.indeedcharts.utilities.ChartHelper;

public class LineChartDrawingHelper extends DrawingHelper{

	XYMultipleSeriesDataset dataset;
	XYMultipleSeriesRenderer renderer;
	
	protected List<Integer> colors;
	protected List<PointStyle> styles;
	
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
	
	private Bundle generateSampleData(String axis){
		
		double[] xValues = { 1 };
		double[] yValues = {12};
//		double[] xValues = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
//		double[] yValues = {12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1, 23.6, 20.3, 17.2,13.9};
		double[] newValues;
		double random;
		Bundle bundle = new Bundle();
		
		if(axis == "x"){
			
			newValues = new double[xValues.length];
			for(int i= 0; i < xValues.length;i++){
				
				random = new Random().nextDouble();
				newValues[i] = xValues[i]+ Math.round(random *100)/100.0;
			}
			bundle.putDoubleArray(ChartConstantData.VALUES, newValues);
			bundle.putDouble(ChartConstantData.X_MIN, 0.5);
			bundle.putDouble(ChartConstantData.X_MAX, 15);
		}
		if(axis == "y"){
			
			newValues = new double[yValues.length];
			for (int i = 0; i < yValues.length; i++) {
				
				random = new Random().nextDouble();
				newValues[i] = yValues[i]+Math.round(random *100)/100.0;
			}
			bundle.putDoubleArray(ChartConstantData.VALUES, newValues);
			bundle.putDouble(ChartConstantData.Y_MIN, 0);
			bundle.putDouble(ChartConstantData.Y_MAX, 32);
			
		}
		return bundle;
		
	}
	
	/**Constructor*/
	public LineChartDrawingHelper(Activity pActivity,int pDrawingCanvasId){
		
		super(pActivity, pDrawingCanvasId);
		
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
		List<double[]> x = new ArrayList<double[]>();
		//for y values
		List<double[]> y = new ArrayList<double[]>();
		//for color data
		colors = new ArrayList<Integer>();
		//for style data
		styles = new ArrayList<PointStyle>();

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
			
			//set x Min and Max values
			if(action == ACTION_ADD_SERIES && list.get(i).getxValueTypeRequired() == ChartConstantData.SAMPLE_DATA){
				
				Bundle sampleData = generateSampleData("x");
				x.add(sampleData.getDoubleArray(ChartConstantData.VALUES));
				list.get(i).setXValues(sampleData.getDoubleArray(ChartConstantData.VALUES));

				minX = sampleData.getDouble(ChartConstantData.X_MIN);
				maxX = sampleData.getDouble(ChartConstantData.X_MAX);
				list.get(i).setMinX(minX);
				list.get(i).setMaxX(maxX);
				list.get(i).setxValueTypeRequired(ChartConstantData.USER_DEFINED_DATA);
				
				
			}else{
				x.add(list.get(i).getXValues());
				minX = list.get(i).getMinX();
				maxX = list.get(i).getMaxX();
				list.get(i).setMinX(minX);
				list.get(i).setMaxX(maxX);
				list.get(i).setxValueTypeRequired(ChartConstantData.USER_DEFINED_DATA);
			}
			//set y Min and Max values
			if(action == ACTION_ADD_SERIES && list.get(i).getyValueTypeRequired() == ChartConstantData.SAMPLE_DATA){
				
				Bundle sampleData = generateSampleData("y");
				y.add(sampleData.getDoubleArray(ChartConstantData.VALUES));
				list.get(i).setYValues(sampleData.getDoubleArray(ChartConstantData.VALUES));
				
				minY = sampleData.getDouble(ChartConstantData.Y_MIN);
				maxY = sampleData.getDouble(ChartConstantData.Y_MAX);
				list.get(i).setMinY(minY);
				list.get(i).setMaxY(maxY);
				list.get(i).setyValueTypeRequired(ChartConstantData.USER_DEFINED_DATA);
				
			}else{
				y.add(list.get(i).getYValues());
				
				minY = list.get(i).getMinY();
				maxY = list.get(i).getMaxY();
				list.get(i).setMinY(minY);
				list.get(i).setMaxY(maxY);
				list.get(i).setyValueTypeRequired(ChartConstantData.USER_DEFINED_DATA);
			}
			
			//set line chart color
			int color = list.get(i).getColorPicked() == 0? ChartHelper.getRandomColor():list.get(i).getColorPicked();
			colors.add(color);
			list.get(i).setColorPicked(color);
			//set point styles
			PointStyle ps = list.get(i).getPointStyle() !=null ? list.get(i).getPointStyle():PointStyle.CIRCLE;
			styles.add(ps);
			list.get(i).setPointStyle(ps);
			/**set current name of point style */
			list.get(i).setPointStyleName(getPointStyleName(ps));
			
		}//END for
		
		setAll(list);
		
		if(renderer == null){
			renderer = new XYMultipleSeriesRenderer();
			renderer.setXLabels(12);
			renderer.setYLabels(10);
			renderer.setShowGrid(true);
			renderer.setXLabelsAlign(Align.RIGHT);
			renderer.setYLabelsAlign(Align.RIGHT);
			renderer.setZoomButtonsVisible(true);
			renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
			renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });
		}
		//code if(...)below executes ONLY when requesting action is add series
		if(action == ACTION_ADD_SERIES || action == ACTION_REDRAW){
			setRenderer(renderer, colors, styles);
			
			int rendererSize = renderer.getSeriesRendererCount();
			for (int j = 0; j < rendererSize; j++) {
				((XYSeriesRenderer) renderer.getSeriesRendererAt(j)).setFillPoints(true);
			
			}
		}
		
		 //(XYMultipleSeriesRenderer renderer, String title, String xTitle, String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor)
		 setChartSettings(renderer, mainTitle, xAxis_name, yAxis_name, minX, maxX, minY, maxY,
		        Color.LTGRAY, Color.LTGRAY);
		 
		 XYMultipleSeriesDataset currentDataset = buildDataset(seriesNames, x, y);
		 getLineChartView(currentDataset,renderer);
		 //save current dataset in main 
		 ((LineChartActivity)activity).currentDataset = currentDataset;
		 //save current renderer in main 
		 ((LineChartActivity)activity).mRenderer = renderer;
		
	}
	protected void setAll(List<IDrawingChartInfo> list){
		try {
			mainTitle = list.get(0).getTitle();
			xAxis_name  = list.get(0).getXaxis_name();
			yAxis_name  = list.get(0).getYaxis_name();
			maxX = list.get(0).getMaxX();
			minX = list.get(0).getMinX();
			maxY = list.get(0).getMaxY();
			minY = list.get(0).getMinY();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	@SuppressWarnings("deprecation")
	public void getLineChartView(XYMultipleSeriesDataset currentDataset,XYMultipleSeriesRenderer renderer){
		
		if(chartView == null){
			chartView = ChartFactory.getLineChartView(activity,  currentDataset, renderer);
			layout = (LinearLayout) activity.findViewById(drawingCanvasId);
			int num = layout.getChildCount();
			if(num > 0){
				layout.removeAllViews();
			}
			num =layout.getChildCount(); 
			layout.addView(chartView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
			chartView.repaint();
			
		}else{
			
			chartView.repaint();
		}
	}
	

	/**
	   * Builds an XY multiple dataset using the provided values.
	   * 
	   * @param titles the series titles
	   * @param xValues the values for the X axis
	   * @param yValues the values for the Y axis
	   * @return the XY multiple dataset
	   */
	  protected XYMultipleSeriesDataset buildDataset(String[] seriesNames, List<double[]> xValues,
	      List<double[]> yValues) {
		 //if(dataset == null){
		  dataset = new XYMultipleSeriesDataset();
		 //}
	    addXYSeries(dataset, seriesNames, xValues, yValues, 0);
	    return dataset;
	  }
	  
	  public void addXYSeries(XYMultipleSeriesDataset dataset, String[] sereisNames, List<double[]> xValues,
		      List<double[]> yValues, int scale) {
		    int length = sereisNames.length;
		    
		    for(int i = 0;i<length;i++){
		    	XYSeries series = new XYSeries(sereisNames[i], scale);
		    	double[] xV = xValues.get(i);
		    	double[] yV = yValues.get(i);
		    	int seriesLength = xV.length;
		     	for (int k = 0; k < seriesLength; k++) {
		     		series.add(xV[k], yV[k]);
		     	}
		     	dataset.addSeries(series);
		    }
		   
	  }
	  
	
	/**
	   * Builds an XY multiple series renderer.
	   * 
	   * @param colors the series rendering colors
	   * @param styles the series point styles
	   * @return the XY multiple series renderers
	   */
//	protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
//	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
//	    setRenderer(renderer, colors, styles);
//	    return renderer;
//	}
	
	protected void setRenderer(XYMultipleSeriesRenderer renderer, List<Integer> colors, List<PointStyle> styles) {
	    //x,y axis title
		renderer.setAxisTitleTextSize(ChartConstantData.AXIS_TITLE_SIZE);//was 16
	    //title
		renderer.setChartTitleTextSize(ChartConstantData.MAIN_TITLE_SIZE);
	    //x, y values
	    renderer.setLabelsTextSize(ChartConstantData.LABELS_SIZE);
	    //for legends
	    renderer.setLegendTextSize(ChartConstantData.LEGEND_SIZE); //was 15 
	    renderer.setPointSize(5f);
	    //int[] margins = {marginT, marginL, marginB, marginR};
	    renderer.setMargins(new int[] { 50, 80, 100, 22 });// was 50, 50, 50, 22 ,top, left, bottom, right
	    //renderer.setFitLegend(true);

	    int length = colors.size();
	    for (int i = 0; i < length; i++) {
	   
		    XYSeriesRenderer r = new XYSeriesRenderer();
		    r.setColor(colors.get(i));
		    r.setPointStyle(styles.get(i));
		   // r.setDisplayChartValues(true);
		    r.setChartValuesSpacing(10);
		    renderer.addSeriesRenderer(r);
		   
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
	
	/***
	 * 
	 * @param ps PointStyle object set being using in current series
	 * @return name of point style
	 */
	protected String getPointStyleName(PointStyle ps){
		
		int foundIndex = -1;
		
		for (int i = 0; i < DialogManager.styles.length; i++) {
			if(ps.equals(DialogManager.styles[i])){
				foundIndex = i;
				break;
			}
		}
		
		return foundIndex != -1? DialogManager.styleNames[foundIndex]:null;
	}
	
}
