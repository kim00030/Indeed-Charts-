package com.ilhyungkim.indeedcharts.utilities;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import yuku.ambilwarna.AmbilWarnaDialog;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.ilhyungkim.indeedcharts.R;
import com.ilhyungkim.indeedcharts.maneger.DialogManager;
import com.ilhyungkim.indeedcharts.maneger.SavableDataManager;
import com.ilhyungkim.indeedcharts.model.EditTextData;


public class ChartHelper {
	
	public static final String RETANGLE = "retangle";

	public static void colorpicker(Activity pActivity) {
		
		final Activity activity = pActivity;
		
		  //     initialColor is the initially-selected color to be shown in the rectangle on the left of the arrow.
      //     for example, 0xff000000 is black, 0xff0000ff is blue. Please be aware of the initial 0xff which is the alpha.
		
		AmbilWarnaDialog dialog = new AmbilWarnaDialog(activity, 0xff0000ff, new AmbilWarnaDialog.OnAmbilWarnaListener() {
			
			@Override
			public void onOk(AmbilWarnaDialog dialog, int color) {
				
				//Toast.makeText(activity, "Selected Color : " + color, Toast.LENGTH_LONG).show();
			
				Intent intent = new Intent(DialogManager.INTENT_ACTION_COLOR_PICK);
				intent.putExtra(DialogManager.INTENT_ACTION_PARAM_A, color);
				activity.sendBroadcast(intent);
			}
			
			@Override
			public void onCancel(AmbilWarnaDialog dialog) {
				
				
			}
		});
		 dialog.show();

	}
	

	public static boolean isEmpty(EditText et) {
		
		boolean b = true;
		
		if(et != null && et.getText().toString().trim().length()>0){
			
			b = false;
			
		}
		return b;
		
	}
	/**
	 * 
	 * @param hintText hintText
	 * @param inputType input Type
	 * @return ArrayList
	 */
	public static EditTextData createEditTextData(String hintText,int inputType){
		
		EditTextData etd = new EditTextData();
		etd.setHint(hintText);
		etd.setInputType(inputType);
		
		return etd;
	}
	/**
	 * 
	 * @param hintTexts hintTexts in array
	 * @param inputTypes inputTypes in array
	 * @param ids ids defined in id.xml in array
	 * @return ArrayList
	 */
	public static List<EditTextData> createEditTextData(String[] hintTexts,int[] inputTypes){
		
		List<EditTextData> list = new ArrayList<EditTextData>();
		
		for(int i = 0; i< hintTexts.length;i++){
		
			EditTextData etd = new EditTextData();
			etd.setHint(hintTexts[i]);
			etd.setInputType(inputTypes[i]);
			
			list.add(etd);
		}
		return list;
	}
	/**
	 * 
	 * @param visibleMenus The menus to be visible
	 * @param invisibleMenus The menus to be invisible
	 * @param menu Menu object
	 */
	public static void menuEnability(int[] visibleMenus,int[] invisibleMenus, Menu menu){
		
		/**
		 * Set visible menus
		 */
		
		if(visibleMenus != null){
			for (int i = 0; i < visibleMenus.length; i++) {
				MenuItem item = menu.findItem(visibleMenus[i]);
				item.setVisible(true);
			}
		}
		
		
		/**
		 * Set invisible menus
		 */
		if(invisibleMenus !=null){
			for (int i = 0; i < invisibleMenus.length; i++) {
				MenuItem item = menu.findItem(invisibleMenus[i]);
				item.setVisible(false);
			}
		}
	}
	/***
	 * Method to set title, home button and its icon
	 * @param bar ActionBar object
	 * @param intent Intent object
	 * @param activity Activity object
	 */
	public static void setTitleOnActionBar(ActionBar bar,Intent intent){
		
		try {
			bar.setTitle(intent.getStringExtra(SavableDataManager.getInstance().context.getResources().getString(R.string.chart_default_title)));
			bar.setHomeButtonEnabled(true);
			bar.setIcon(R.drawable.home_button);
		} catch (NotFoundException e) {
			
			e.printStackTrace();
		}catch(NullPointerException e2){
			e2.printStackTrace();
		}

	}
	/***
	 * Method to set background color on Action Bar
	 * @param bar ActionBar object
	 * @param colorValue integer color value
	 */
	public static void setBackgroundColor(ActionBar bar, int colorValue){
		
		
		bar.setBackgroundDrawable(new ColorDrawable(colorValue)); //Color.MAGENTA
		
	}
	
	public static void splitString(Activity activity,String s){
		
		if (s.contains(":")) {
		    Toast.makeText(activity, "Contain this character(:)", Toast.LENGTH_SHORT).show();
		} else {
		    //throw new IllegalArgumentException("String " + s + " does not contain -");
			Toast.makeText(activity, "NOT Contain this character(:)", Toast.LENGTH_SHORT).show();
		}
		
//		String string = "004-034556";
//		String[] parts = string.split("-");
//		String part1 = parts[0]; // 004
//		String part2 = parts[1]; // 034556
	}
	
	/***
	 * 
	 * @param shapeType		String represents shapeType
	 * @return Bitmap
	 */
	public static Bitmap createShape(String shapeType,int color){
		
		
		Bitmap bitmap = Bitmap.createBitmap((int) 80, 80, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		switch(shapeType){
			
			case RETANGLE:
				Paint paint = new Paint();
				paint.setColor(color);
				paint.setStyle(Paint.Style.FILL_AND_STROKE);
				paint.setStrokeWidth(10);
				float leftx = 20;
				float topy = 20;
				float rightx = 50;
				float bottomy = 50;
				canvas.drawRect(leftx, topy, rightx, bottomy, paint);
			break;
		}
	 
	    return bitmap;
	}
	
	public static void setInputType(EditText et){
		
		/**accepts the digits 0 through 9, plus the minus sign (only at the beginning) and/or decimal point (only one per field)*/
		et.setKeyListener(DigitsKeyListener.getInstance(true, true));

	}
	public static void dismiss(AlertDialog alertDialog){
		
		if(alertDialog !=null) alertDialog.dismiss();
	}
	
	public static String numberFormatted(double d){
		
		NumberFormat formatter = new DecimalFormat("#0.0");  
		return formatter.format(d);
	}
	public static int getRandomColor(){
		
		Random rand = new Random();
		// generate the random integers for r, g and b value
		int r = rand.nextInt(255);
		int g = rand.nextInt(255);
		int b = rand.nextInt(255);
		
		r = (r << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
	    g = (g << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
	    b = b & 0x000000FF; //Mask out anything not blue.

	    return 0xFF000000 | r | g | b; //0xFF000000 for 100% Alpha. Bitwise OR everything together
	}
	
	public static String getDefaultSeriesName(int index,String str){
		
		return str+String.valueOf(index);
	}
	/***
	 * 
	 * @param et  reference of EditText
	 * @param length	number of limit
	 */
	public static void setEditTextMaxLength(EditText et,int length) {
	    InputFilter[] FilterArray = new InputFilter[1];
	    FilterArray[0] = new InputFilter.LengthFilter(length);
	    et.setFilters(FilterArray);
	}
}
