package com.ilhyungkim.indeedcharts.drawinghelper;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DialRenderer;
import org.achartengine.renderer.DialRenderer.Type;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.ilhyungkim.indeedcharts.DialChartActivity;
import com.ilhyungkim.indeedcharts.constantdata.ChartConstantData;
import com.ilhyungkim.indeedcharts.drawinghelper.sampledata.SampleDataCreator;
import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;
import com.ilhyungkim.indeedcharts.maneger.DialogManager;
import com.ilhyungkim.indeedcharts.model.DialChartDataModel;
import com.ilhyungkim.indeedcharts.model.SeriesData;
import com.ilhyungkim.indeedcharts.model.wrappers.DataWrapper;
import com.ilhyungkim.indeedcharts.utilities.ChartHelper;

public class DialChartDrawingHelper extends DrawingHelper{

	protected DialRenderer renderer;
	protected SimpleSeriesRenderer sr;
	protected GraphicalView chartView;
	protected  CategorySeries category;
	protected LinearLayout layout;
	
	protected SimpleSeriesRenderer[] simpleRenderers;
	
	//minimum y
	double minY = -1;
	//maximum y
	double maxY = -1;
	ArrayList<Double> values;
	
	public DialChartDrawingHelper(Activity pActivity,int pDrawingCanvasId){
		
		super(pActivity, pDrawingCanvasId);
	}
	
	public void draw(List<IDrawingChartInfo> list,String action)
	{
		//Initialize
		if(renderer != null){
			renderer = null;
		}
		if(chartView != null){
			chartView = null;
		}
		
		if(category != null){
			category = null;
		}
		category = new CategorySeries(mainTitle);
		
		int listSize = list.size();
		simpleRenderers = new SimpleSeriesRenderer[listSize];
		Type[] types = new Type[listSize];
		values = new ArrayList<>();
		
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
				
				double max = list.get(i).getMaxY() == 0 ? 100:list.get(i).getMaxY();
				Bundle sampleData = SampleDataCreator.generateSampleDataByRange(list,list.get(i).getMinY(),max);
				double value = sampleData.getDouble(ChartConstantData.VALUES);
				if(((DialChartDataModel)list.get(i)).getValue() == -1){
					((DialChartDataModel)list.get(i)).setValue(value);
					category.add(seriesName,value);
				}else{
					category.add(seriesName,((DialChartDataModel)list.get(i)).getValue());
				}
				
				minY = sampleData.getDouble(ChartConstantData.Y_MIN);
				maxY = sampleData.getDouble(ChartConstantData.Y_MAX);
				list.get(i).setMinY(minY);
				list.get(i).setMaxY(maxY);
				
				list.get(i).setyValueTypeRequired(ChartConstantData.USER_DEFINED_DATA);
				
				types[i] = Type.NEEDLE;
				list.get(i).setType(types[i] );
				
			}else{
			
				category.add(seriesName,((DialChartDataModel)list.get(i)).getValue());
				minY = list.get(i).getMinY();
				maxY = list.get(i).getMaxY();
				list.get(i).setMinY(minY);
				list.get(i).setMaxY(maxY);
				types[i] = list.get(i).getType();
				list.get(i).setyValueTypeRequired(ChartConstantData.USER_DEFINED_DATA);

			}
			list.get(i).setTypeName(getPointStyleName(types[i]));
			int color = list.get(i).getColorPicked() == 0? ChartHelper.getRandomColor():list.get(i).getColorPicked();
			list.get(i).setColorPicked(color);
			simpleRenderers[i] = new SimpleSeriesRenderer();
			simpleRenderers[i].setColor(color);
		}//End for
		try {
			mainTitle = list.get(0).getTitle();
			minY = list.get(0).getMinY();
			maxY = list.get(0).getMaxY();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		if(renderer == null){
			renderer = new DialRenderer();
			renderer.setChartTitleTextSize(20);
		    renderer.setLabelsTextSize(50);//was 15
		    renderer.setLegendTextSize(30);//was 15
		    renderer.setMargins(new int[] {50, 80, 100, 22});
		   renderer.setZoomButtonsVisible(true);
		    //renderer.setAxesColor(Color.LTGRAY);
		   // renderer.setShowGridX(true);
		    renderer.setLabelsTextSize(20);//10
		    renderer.setLabelsColor(Color.BLACK);
		    renderer.setShowLabels(true);
		    renderer.setVisualTypes(types);
		    renderer.setMinValue(minY);
		    renderer.setMaxValue(maxY);
		    renderer.setChartTitle(mainTitle);
		    renderer.setChartTitleTextSize(ChartConstantData.MAIN_TITLE_SIZE);
		   
		}
		for(int i = 0; i< simpleRenderers.length;i++){
			renderer.addSeriesRenderer(simpleRenderers[i]);
			
		}
		((DialChartActivity)this.activity).setmRenderer(renderer);
		getBarChartView(category,renderer);
		
		
	}
	@SuppressWarnings("deprecation")
	public void getBarChartView( CategorySeries category,DialRenderer renderer){
		
		if(chartView == null){
			chartView = ChartFactory.getDialChartView(activity, category, renderer);
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
	private String getPointStyleName(Type type){
		int foundIndex = -1;
		for (int i = 0; i < DialogManager.types.length; i++) {
			if(type.equals( DialogManager.types[i])){
				foundIndex = i;
				break;
			}
		}
		return foundIndex != -1 ? DialogManager.pointStyleNames[foundIndex]:null;
	}
}
