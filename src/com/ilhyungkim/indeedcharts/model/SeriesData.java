package com.ilhyungkim.indeedcharts.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SeriesData implements Parcelable{

	private int seriesId;
	private String newSeriesName;
	private int seriesColor;
	private double x;
	private double y;
	
	public SeriesData(){
		
	}
	
	public SeriesData(Parcel read){
		
		seriesId = read.readInt();
		newSeriesName = read.readString();
		seriesColor = read.readInt();
		x = read.readDouble();
		y = read.readDouble();
	}

	public int getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(int seriesId) {
		this.seriesId = seriesId;
	}

	public String getNewSeriesName() {
		return newSeriesName;
	}
	public void setNewSeriesName(String newSeriesName) {
		this.newSeriesName = newSeriesName;
	}
	public int getSeriesColor() {
		return seriesColor;
	}
	
	public void setSeriesColor(int seriesColor) {
		this.seriesColor = seriesColor;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public int describeContents() {
		
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeInt(seriesId);
		dest.writeString(newSeriesName);
		dest.writeInt(seriesColor);
		dest.writeDouble(x);
		dest.writeDouble(y);
		
	}
	public static final Parcelable.Creator<SeriesData> CREATOR = new Parcelable.Creator<SeriesData>() {

		@Override
		public SeriesData createFromParcel(Parcel source) {
			
			return new SeriesData(source);
		}

		@Override
		public SeriesData[] newArray(int size) {
			
			return new SeriesData[size];
		}
	};
	
}
