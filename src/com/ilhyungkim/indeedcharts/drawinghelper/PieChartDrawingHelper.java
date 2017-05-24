package com.ilhyungkim.indeedcharts.drawinghelper;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.ilhyungkim.indeedcharts.PieChartActivity;
import com.ilhyungkim.indeedcharts.constantdata.ChartConstantData;
import com.ilhyungkim.indeedcharts.drawinghelper.sampledata.SampleDataCreator;
import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;
import com.ilhyungkim.indeedcharts.model.PieChartDataModel;
import com.ilhyungkim.indeedcharts.model.SeriesData;
import com.ilhyungkim.indeedcharts.model.wrappers.DataWrapper;
import com.ilhyungkim.indeedcharts.utilities.ChartHelper;

public class PieChartDrawingHelper extends DrawingHelper{

	protected int DEFAULT_MIN = 0;
	protected int DEFAULT_MAX = 100;
	protected GraphicalView chartView;
	protected LinearLayout layout;
	/** The main renderer for the main data set. */
	private DefaultRenderer defaultRenderer;
	/** The main series that will include all the data. */
	private CategorySeries distributionSeries;
	
	protected List<Integer> colors;
	
	public PieChartDrawingHelper(Activity pActivity,int pDrawingCanvasId){
		
		super(pActivity, pDrawingCanvasId);
	}
	
	@SuppressWarnings("deprecation")
	public void draw(List<IDrawingChartInfo> list,String action){
		
		//Initialize
		if(defaultRenderer != null){
			defaultRenderer = null;
		}
		if(chartView != null){
			chartView = null;
		}
		
		if(distributionSeries != null){
			distributionSeries = null;
		}
		distributionSeries = new CategorySeries(mainTitle);
	
		//for color data
		colors = new ArrayList<Integer>();
		int listSize = list.size();
		
		for(int i = 0; i<listSize;i++){
			
			//set Main Title
			if(list.get(i).getTitle() != null){
				mainTitle = list.get(i).getTitle();
			}else{
				list.get(i).setTitle(mainTitle);
			}
			String seriesName = list.get(i).getSeriesName() != null ? list.get(i).getSeriesName():ChartHelper.getDefaultSeriesName(i+1, "Series");
			list.get(i).setSeriesName(seriesName);
			if(action == ACTION_ADD_SERIES && list.get(i).getyValueTypeRequired() == ChartConstantData.SAMPLE_DATA){
				
				Bundle sampleData = SampleDataCreator.generateSampleDataByRange(null, 30, 50);
				double value = sampleData.getDouble(ChartConstantData.VALUES);
			
				if(((PieChartDataModel)list.get(i)).getValue() == -1){
					((PieChartDataModel)list.get(i)).setValue(value);
					distributionSeries.add(seriesName,value);
				}else{
					distributionSeries.add(seriesName,((PieChartDataModel)list.get(i)).getValue());
				}
				
				list.get(i).setyValueTypeRequired(ChartConstantData.USER_DEFINED_DATA);
			}else{
				
				distributionSeries.add(seriesName, ((PieChartDataModel)list.get(i)).getValue());
				list.get(i).setyValueTypeRequired(ChartConstantData.USER_DEFINED_DATA);
			}
			int color = list.get(i).getColorPicked() == 0? ChartHelper.getRandomColor():list.get(i).getColorPicked();
			colors.add(color);
			list.get(i).setColorPicked(color);
			list.get(i).setMinY(DEFAULT_MIN);
			list.get(i).setMaxY(DEFAULT_MAX);
		}//END FOR
		
		try {
			mainTitle = list.get(0).getTitle();
		} catch (Exception e) {
		
			e.printStackTrace();
		}
		if (defaultRenderer == null) {
			defaultRenderer = new DefaultRenderer();
			defaultRenderer.setChartTitle(mainTitle);
			
	    	defaultRenderer.setChartTitleTextSize(ChartConstantData.AXIS_TITLE_SIZE);
	    	defaultRenderer.setLabelsColor(Color.BLACK);
	    	defaultRenderer.setZoomButtonsVisible(true);
	    	defaultRenderer.setLabelsTextSize(ChartConstantData.LABELS_SIZE);
	    	defaultRenderer.setLegendTextSize(ChartConstantData.LEGEND_SIZE);//20
	    	defaultRenderer.setMargins(new int[] {50, 80, 100, 22});//50, 50, 50, 22
	    	//defaultRenderer.setPanEnabled(false);
		}
		int seriesCount = distributionSeries.getItemCount();
		for(int i = 0; i< seriesCount;i++){
			SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();    	
    		seriesRenderer.setColor(colors.get(i));
    		
    		//seriesRenderer.setDisplayChartValues(false);//Dosen't work
    		// Adding a renderer for a slice
    		defaultRenderer.addSeriesRenderer(seriesRenderer);
		}

		((PieChartActivity)this.activity).setDefaultRenderer(defaultRenderer);
		if(chartView == null){
			
			chartView = ChartFactory.getPieChartView(activity,distributionSeries, defaultRenderer);
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
	
	public void updateSeriesName(List<IDrawingChartInfo> list, DataWrapper dataWrapper){
		
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
		}
		/**Take redraw process with new updated*/
		draw(list,null);
	}
}
