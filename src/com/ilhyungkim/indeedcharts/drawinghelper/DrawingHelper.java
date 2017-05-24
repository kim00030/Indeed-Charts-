package com.ilhyungkim.indeedcharts.drawinghelper;

import android.app.Activity;

import com.ilhyungkim.indeedcharts.R;

public class DrawingHelper {

	protected String mainTitle;
	protected String xAxis_name;
	protected String yAxis_name;
	protected Activity activity; 
	protected int drawingCanvasId = 0;
	protected String ACTION_ADD_SERIES;
	protected String ACTION_REDRAW;
	
	public DrawingHelper(Activity pActivity,int pDrawingCanvasId){
		
		this.activity = pActivity;
		this.drawingCanvasId = pDrawingCanvasId;

		mainTitle = this.activity.getResources().getString(R.string.chart_default_title);
		xAxis_name = this.activity.getResources().getString(R.string.chart_default_xaxis);
		yAxis_name = this.activity.getResources().getString(R.string.chart_default_yaxis);
		ACTION_ADD_SERIES = this.activity.getResources().getString(R.string.action_add_series);
		ACTION_REDRAW = this.activity.getResources().getString(R.string.action_redraw);
		
		
		
	}
}
