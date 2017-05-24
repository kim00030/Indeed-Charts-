package com.ilhyungkim.indeedcharts.interfaces;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

public interface IBarChartActivity {

	public XYMultipleSeriesDataset getCurrentDataset();
	public void setCurrentDataset(XYMultipleSeriesDataset currentDataset);
	public XYMultipleSeriesRenderer getmRenderer();

	public void setmRenderer(XYMultipleSeriesRenderer mRenderer);
}
