package com.ilhyungkim.indeedcharts.maneger;

import android.app.Activity;
import android.widget.Toast;

public class ToastMessageManager {
	
	public static void show(Activity pActivity, String msg,int duration){
		
		Toast.makeText(pActivity, msg, duration).show();

	}

}
