package com.ilhyungkim.indeedcharts;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.res.Resources.NotFoundException;

import com.ilhyungkim.indeedcharts.interfaces.IChartModel;
import com.ilhyungkim.indeedcharts.model.ChartData;

public class ChartsPullParser {

	private static final String CHART_ID = "chartId";
	private static final String CHART_MENU_NAME = "chartMenuName";
	private static final String CHART_DESC = "description";
	private static final String CHART_IMAGE = "image";
	
	private ChartData currentChart= null;
	private String currentTag = null;
	List<IChartModel> charts = new ArrayList<IChartModel>();

	/***
	 * Method to parse XML data and create ChartModels with them, then add them to List
	 * @param context
	 * @return List<IChartModel>
	 */
	public List<IChartModel> parseXML(Context context) {

		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			
			InputStream stream = context.getResources().openRawResource(R.raw.charts);
			xpp.setInput(stream, null);

			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					handleStartTag(xpp.getName());
				} else if (eventType == XmlPullParser.END_TAG) {
					currentTag = null;
				} else if (eventType == XmlPullParser.TEXT) {
					handleText(xpp.getText());
				}
				eventType = xpp.next();
			}

		} catch (NotFoundException e) {
			
		} catch (XmlPullParserException e) {
			
		} catch (IOException e) {
			
		}

		return charts;
	}

	private void handleText(String text) {
		String xmlText = text;
		if (currentChart != null && currentTag != null) {
			if (currentTag.equals(CHART_ID)) {
				Integer id = Integer.parseInt(xmlText);
				currentChart.setChartId(id);
			} 
			else if (currentTag.equals(CHART_MENU_NAME)) {
				currentChart.setMenuName(xmlText);
			}
			else if (currentTag.equals(CHART_DESC)) {
				currentChart.setDescription(xmlText);
			}
			else if (currentTag.equals(CHART_IMAGE)) {
				currentChart.setImage(xmlText);
			}
			
		}
	}

	private void handleStartTag(String name) {
		if (name.equals("chart")) {
			currentChart = new ChartData();
			charts.add(currentChart);
		}
		else {
			currentTag = name;
		}
	}
}
