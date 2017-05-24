package com.ilhyungkim.indeedcharts.preferenceactivity;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;

public class EditSelectedSeriesActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		PreferenceCategory mainCategory  = new PreferenceCategory(this);
		//Root
		PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);
		mainCategory.setTitle("Category Title");  
		root.addPreference(mainCategory);
		CharSequence[] entries = {"Two", "One", "Other"};  
		
		final ListPreference listPref = new ListPreference(this);
		listPref.setKey("keyDevice");
		listPref.setDefaultValue("one");
		listPref.setEntries(entries);
		listPref.setEntryValues(entries);  
		listPref.setDialogTitle("Title");  
		 listPref.setTitle("Title 2");  
		 listPref.setSummary("Summary");  
		
		mainCategory.addPreference(listPref);
		this.setPreferenceScreen(root);
	}
}
