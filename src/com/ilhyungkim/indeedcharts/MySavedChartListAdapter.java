package com.ilhyungkim.indeedcharts;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilhyungkim.indeedcharts.interfaces.IChartModel;
import com.ilhyungkim.indeedcharts.model.ChartData;

public class MySavedChartListAdapter extends ChartListAdapter {

	public MySavedChartListAdapter(Context context, int resource,
			List<IChartModel> objects) {
		super(context, resource, objects);
		
	}
	
	@SuppressLint("ViewHolder")
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
		MenuName.setSingleLine(true);
		MenuName.setEllipsize(TextUtils.TruncateAt.END);
		
		TextView date = (TextView) view.findViewById(R.id.chart_date);
		date.setText("Saved on "+chartData.getDate());
		return view;

	}
}
