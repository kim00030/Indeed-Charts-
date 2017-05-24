package com.ilhyungkim.indeedcharts.drawinghelper;

import java.util.List;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.app.Activity;

import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;

public class StackedBarChartDrawingHelper extends BarChartDrawingHelper {

	
	public StackedBarChartDrawingHelper(Activity pActivity, int pDrawingCanvasId) {
		super(pActivity, pDrawingCanvasId);
		
	}
	
	public void draw(List<IDrawingChartInfo> list,XYMultipleSeriesDataset pDataset,XYMultipleSeriesRenderer pRenderer,
			String action)
	{
		super.draw(list, pDataset, pRenderer, action);
	}
	
	
	
}
