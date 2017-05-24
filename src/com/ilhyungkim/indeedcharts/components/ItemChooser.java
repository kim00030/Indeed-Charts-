package com.ilhyungkim.indeedcharts.components;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ilhyungkim.indeedcharts.ChartActivity;
import com.ilhyungkim.indeedcharts.interfaces.IAlertDialogInterface;
import com.ilhyungkim.indeedcharts.interfaces.IDrawingChartInfo;
import com.ilhyungkim.indeedcharts.interfaces.IManagerInterface;
import com.ilhyungkim.indeedcharts.maneger.DialogManager;
import com.ilhyungkim.indeedcharts.utilities.ChartHelper;

public class ItemChooser implements IAlertDialogInterface{

	private Activity activity;
	private IManagerInterface manager;
	private  AlertDialog.Builder alertDialogBuilder;
	private  AlertDialog alertDialog;
	private ArrayList<IDrawingChartInfo> seriesList;
	private RadioButton[] radioBtns;
	private String title;
	
	
	public ItemChooser(Activity activity, IManagerInterface manager,ArrayList<IDrawingChartInfo> pSeriesList,String title){
		
		this.activity = activity;
		this.manager = manager;
		this.seriesList = pSeriesList;
		this.title = title;
		
	}

	public void createDialog() {
		
		/**Create top layer that places in the dialog*/ 
		final LinearLayout topLinearLayout = new LinearLayout(activity);
		topLinearLayout.setOrientation(LinearLayout.VERTICAL);

		/**for saving reference of all radio buttons*/
		radioBtns = new RadioButton[seriesList.size()];
		/**create radio group*/
		RadioGroup rg = new RadioGroup(activity);
		rg.setOrientation(RadioGroup.VERTICAL);
		
		/**set Title on the dialog window*/
		TextView title = DialogManager.createCustomTextView(activity, this.title);
		alertDialogBuilder = new AlertDialog.Builder(activity);
		alertDialogBuilder.setCustomTitle(title);
		alertDialogBuilder.setView(topLinearLayout);
		////////////////////////////////////////////
		for (int i = 0; i < seriesList.size(); i++) {
			
			final LinearLayout subLayout = new LinearLayout(activity);
			subLayout.setOrientation(LinearLayout.HORIZONTAL);
			radioBtns[i] = new RadioButton(activity);
			radioBtns[i].setText(seriesList.get(i).getSeriesName());
			radioBtns[i].setTextColor(seriesList.get(i).getColorPicked());
			rg.addView(radioBtns[i],i);
		}
		/**add scrollView in case, too many series created*/
		ScrollView sv = new ScrollView(activity);
		topLinearLayout.addView(sv);
		sv.addView(rg);
		
		 /**radio button listener*/
		 rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				((AlertDialog)alertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
			}
		});
		
		alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				int selectedIndex = -1;
				for (int i = 0; i < radioBtns.length; i++) {
					
					if(radioBtns[i].isChecked()){
						
						selectedIndex = i;
						break;
					}
				}
				/**pass selected index which is eventually index of seriesList to whoever calls*/
				if(selectedIndex >=0)ItemChooser.this.manager.showXYValueChangeDialog(selectedIndex);
				ChartHelper.dismiss(alertDialog);
				
			}
		});
		alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				ChartHelper.dismiss(alertDialog);
				
			}
		});
		
		
		///////////////////////////////////
		alertDialog = alertDialogBuilder.create();
		 /**
		  * disable dismissing ability when click on outside of dialog
		  */
		 //alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
		/**save this references in ChartActivity*/
		ChartActivity chartActivity = (ChartActivity) this.activity;
		chartActivity.currentDialogManager = this;
		((AlertDialog)alertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
	}

	@Override
	public void dismissAlertDialog() {
		ChartHelper.dismiss(alertDialog);
	}
	
}
