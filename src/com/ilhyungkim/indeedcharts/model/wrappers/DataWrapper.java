package com.ilhyungkim.indeedcharts.model.wrappers;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.ilhyungkim.indeedcharts.model.SeriesData;

public class DataWrapper implements Parcelable{

	private List<SeriesData> seriesDatas = new ArrayList<SeriesData>();
	
	public DataWrapper(){
		seriesDatas = new ArrayList<SeriesData>();
	}
	public DataWrapper(Parcel read){
		
		read.readTypedList(seriesDatas, SeriesData.CREATOR);
	}
	
	public List<SeriesData> getSeriesDatas() {
		return seriesDatas;
	}
	public void setSeriesDatas(List<SeriesData> seriesDatas) {
		this.seriesDatas = seriesDatas;
	}
	@Override
	public int describeContents() {
		
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeTypedList(seriesDatas);
	}

	public static final Parcelable.Creator<DataWrapper> CREATOR = new Creator<DataWrapper>() {
		
		@Override
		public DataWrapper[] newArray(int size) {
			
			return new DataWrapper[size];
		}
		
		@Override
		public DataWrapper createFromParcel(Parcel source) {
			
			return new DataWrapper(source);
		}
	};
}
