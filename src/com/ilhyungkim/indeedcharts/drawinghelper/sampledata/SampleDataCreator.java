package com.ilhyungkim.indeedcharts.drawinghelper.sampledata;

import java.util.List;
import java.util.Random;

import android.os.Bundle;

import com.ilhyungkim.indeedcharts.constantdata.ChartConstantData;
import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;

public class SampleDataCreator {

	public static final int PIE_CHART = 1;
	public static final int LINE_CHART = 2;
	public static final int BAR_CHART = 3;
	public static final int STACKED_BAR_CHART = 4;
	public static final int DIAL_BAR_CHART = 5;

	public static Bundle createSampleDataByChartId(String axis,List<IDrawingChartInfo> list){
		
		Bundle sampleData = null;
		int chartId = list.get(0).getChartId();
		
		switch (chartId) {
	
		case BAR_CHART:
			sampleData = generateSampleDataForBarChart(axis,list);
			break;
		case STACKED_BAR_CHART:
			sampleData = generateSampleDataForStackedBarChart(axis,list);
			break;
		
		default:
			break;
		}; 
		return sampleData;
	}
	
	public static Bundle generateSampleValueByRange(double rangeMin,double rangeMax) {
		
		Bundle bundle = null;
		Random r = new Random();
		double value = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		
		bundle = new Bundle();
		bundle.putDouble(ChartConstantData.VALUES, Math.floor(value));
		return bundle;
	}
	public static Bundle generateSampleDataByRange(List<IDrawingChartInfo> list,double rangeMin,double rangeMax) {
		
		Bundle bundle = new Bundle();
		
		double min =0;
		double max = 100;
		Random r = new Random();
		double value = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		
		if(list != null && list.size() > 1){
			min = list.get(0).getMinY();
			max = list.get(0).getMaxY();
		}
		bundle.putDouble(ChartConstantData.Y_MIN, min);
		bundle.putDouble(ChartConstantData.Y_MAX, max);
		bundle.putDouble(ChartConstantData.VALUES, Math.floor(value));
		
		return bundle;
	}
	public static Bundle generateSampleDataForStackedBarChart(String axis,List<IDrawingChartInfo> list){
		

		Bundle bundle = null;
		double[] newValues;
	
		if(axis == "x"){

			String[] sampleX ={"1"};
			/** if NOT 1st sample chart */
			if(list.size() >1){
				
				sampleX = list.get(0).getX_StringValues();
			}
			String[] xLables = new String[sampleX.length];
			for(int i = 0;i<sampleX.length;i++){
				xLables[i] = sampleX[i];
			}
			
			bundle = new Bundle();
			bundle.putStringArray(ChartConstantData.VALUES, xLables);
			bundle.putDouble(ChartConstantData.X_MIN, 0.5);
			bundle.putDouble(ChartConstantData.X_MAX, 12);
			

		}
		if(axis == "y"){
			double[] y = {100};//100,135
		
			newValues = new double[y.length];
			for (int i = 0; i < y.length; i++) {
				
				
				Random rand = new Random(); 
				int value = rand.nextInt(200) + 1;
				newValues[i] = y[i] + value;
				
			}
			
			bundle = new Bundle();
			bundle.putDoubleArray(ChartConstantData.VALUES, newValues);
			bundle.putDouble(ChartConstantData.Y_MIN, 0);
			bundle.putDouble(ChartConstantData.Y_MAX, ChartConstantData.DEFAULT_Y_MAX);
		}
		return bundle;
	}
	
	public static Bundle generateSampleDataForBarChart(String axis,List<IDrawingChartInfo> list){
		
		
		Bundle bundle = null;
		double[] newValues;
	
		if(axis == "x"){

			String[] sampleX ={"2014","2015"};
			/** if NOT 1st sample chart */
			if(list.size() >1){
				
				sampleX = list.get(0).getX_StringValues();
			}
			String[] xLables = new String[sampleX.length];
			for(int i = 0;i<sampleX.length;i++){
				xLables[i] = sampleX[i];
			}
			
			bundle = new Bundle();
			bundle.putStringArray(ChartConstantData.VALUES, xLables);
			bundle.putDouble(ChartConstantData.X_MIN, 0.5);
			bundle.putDouble(ChartConstantData.X_MAX, 6);
			
		}
		
		if(axis == "y"){
			double[] y = {100,135};//100,135
			/**If 1st sample chart */
			if(list.size() == 1){
				
				newValues = new double[y.length];
				for (int i = 0; i < y.length; i++) {
					
					Random rand = new Random(); 
					int value = rand.nextInt(50) + 1;
					newValues[i] = y[i] + value;
				}
			}else{
				
				newValues = list.get(0).getYValues();
			}
			
			bundle = new Bundle();
			bundle.putDoubleArray(ChartConstantData.VALUES, newValues);
			bundle.putDouble(ChartConstantData.Y_MIN, 0);
			bundle.putDouble(ChartConstantData.Y_MAX, ChartConstantData.DEFAULT_Y_MAX);
		}
		
		return bundle;
	}
	public static Bundle generateSampleDataForScatterChart(){
		
		Bundle bundle = new Bundle();
		int nr = 5;
		double[] xValues = new double[nr];
	    double[] yValues = new double[nr];
		Random r = new Random();
	
		
		for(int i = 0; i< nr;i++){
			 xValues[i] = i + r.nextInt() % 10;
		     yValues[i] = i * 2 + r.nextInt() % 10;
		}
		bundle.putDoubleArray(ChartConstantData.X_VALUES, xValues);
		bundle.putDoubleArray(ChartConstantData.Y_VALUES, yValues);
		bundle.putDouble(ChartConstantData.X_MIN, -10);
		bundle.putDouble(ChartConstantData.X_MAX, 30);
		bundle.putDouble(ChartConstantData.Y_MIN, -10);
		bundle.putDouble(ChartConstantData.Y_MAX, 51);
		return bundle;
	}
}
