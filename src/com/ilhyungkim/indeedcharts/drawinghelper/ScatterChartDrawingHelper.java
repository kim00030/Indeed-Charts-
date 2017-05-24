package com.ilhyungkim.indeedcharts.drawinghelper;


import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.ilhyungkim.indeedcharts.ScatterChartActivity;
import com.ilhyungkim.indeedcharts.constantdata.ChartConstantData;
import com.ilhyungkim.indeedcharts.drawinghelper.sampledata.SampleDataCreator;
import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;
import com.ilhyungkim.indeedcharts.utilities.ChartHelper;

public class ScatterChartDrawingHelper extends LineChartDrawingHelper{

	public ScatterChartDrawingHelper(Activity pActivity,int pDrawingCanvasId){

		super(pActivity,pDrawingCanvasId);
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
			
			if(action == ACTION_ADD_SERIES && list.get(i).getyValueTypeRequired() == ChartConstantData.SAMPLE_DATA){
				
				Bundle sampleData = SampleDataCreator.generateSampleDataForScatterChart();
				
				x.add(sampleData.getDoubleArray(ChartConstantData.X_VALUES));
				y.add(sampleData.getDoubleArray(ChartConstantData.Y_VALUES));
				minX = sampleData.getDouble(ChartConstantData.X_MIN);
				maxX = sampleData.getDouble(ChartConstantData.X_MAX);
				minY = sampleData.getDouble(ChartConstantData.Y_MIN);
				maxY = sampleData.getDouble(ChartConstantData.Y_MAX);
				
				list.get(i).setXValues(sampleData.getDoubleArray(ChartConstantData.X_VALUES));
				list.get(i).setYValues(sampleData.getDoubleArray(ChartConstantData.Y_VALUES));
				list.get(i).setMinX(sampleData.getDouble(ChartConstantData.X_MIN));
				list.get(i).setMaxX(sampleData.getDouble(ChartConstantData.X_MAX));
				list.get(i).setMinY(sampleData.getDouble(ChartConstantData.Y_MIN));
				list.get(i).setMaxY(sampleData.getDouble(ChartConstantData.Y_MAX));
				list.get(i).setyValueTypeRequired(ChartConstantData.USER_DEFINED_DATA);
			}else{
				
				x.add(list.get(i).getXValues());
				y.add(list.get(i).getYValues());
				minX = list.get(i).getMinX();
				maxX = list.get(i).getMaxX();
				minY = list.get(i).getMinY();
				maxY = list.get(i).getMaxY();
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

		}//End for
		
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
		 getScatterChartView(currentDataset,renderer);
		 //save current dataset in main 
		 ((ScatterChartActivity)activity).currentDataset = currentDataset;
		 //save current renderer in main 
		 ((ScatterChartActivity)activity).mRenderer = renderer;
		
	}
	
	
	@SuppressWarnings("deprecation")
	private void getScatterChartView(XYMultipleSeriesDataset currentDataset,
			XYMultipleSeriesRenderer renderer)
	{
		
		if(chartView == null){
			chartView = ChartFactory.getScatterChartView(activity,  currentDataset, renderer);
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

}
