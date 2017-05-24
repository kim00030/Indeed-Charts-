
package com.ilhyungkim.indeedcharts;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilhyungkim.indeedcharts.interfaces.IChartModel;
import com.ilhyungkim.indeedcharts.model.ChartData;


/***
 * This ArrayAdapter class creates and handles Main Chart Menu List on screen
 * @author Dan Kim
 *
 */
@SuppressLint("ViewHolder")
public class ChartListAdapter extends ArrayAdapter<IChartModel> {

	protected Context context;
	protected List<IChartModel> objects;
	protected int resource;
	/***
	 * This ArrayAdapter class creates and handles Main Chart Menu List on screen
	 * @param context 
	 * @param resource that being used in this ArrayAdapter.
	 * @param objects that contains information that will be displayed 
	 */
	public ChartListAdapter(Context context, int resource,
			List<IChartModel> objects) {
		super(context, resource, objects);
		
		this.context = context;
		this.objects = objects;
		this.resource = resource;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(this.resource, null);
		
		//get current selected list item
		ChartData chartData = (ChartData) this.objects.get(position);
		
		//Chart image icon
		ImageView iv = (ImageView) view.findViewById(R.id.chart_image);
		int imageResorce = context.getResources().getIdentifier(chartData.getImage(), "drawable", context.getPackageName());
		iv.setImageResource(imageResorce);
		
		TextView MenuName = (TextView) view.findViewById(R.id.chart_name);
		MenuName.setText(chartData.getMenuName());
		
		TextView description = (TextView) view.findViewById(R.id.chart_desc);
		description.setText(chartData.getDescription());
		
		return view;
	}
}
