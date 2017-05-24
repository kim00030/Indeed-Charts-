package com.ilhyungkim.indeedcharts.interfaces;

import android.widget.EditText;
import android.widget.TableRow;

import com.ilhyungkim.indeedcharts.model.SeriesData;

public interface IManagerInterface {

	public void showXYValueChangeDialog(int seriesIndex);
	public void addRow(SeriesData sd);
	public void addRow(String x, double y);
	public void removeRow(TableRow tr, EditText editTextForX,EditText editTextForY);
	public void updateButtonStatus();
}
