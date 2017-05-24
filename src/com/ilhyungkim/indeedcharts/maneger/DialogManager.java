package com.ilhyungkim.indeedcharts.maneger;

import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.DialRenderer.Type;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ilhyungkim.indeedcharts.utilities.ChartHelper;



public class DialogManager {
	
	public static final String[] styleNames= {"CIRCLE","DIAMOND","TRIANGLE","SQUARE"};
	public static final PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND,
		    PointStyle.TRIANGLE, PointStyle.SQUARE };
	
	public static final String[] pointStyleNames= {"Needle","Arrow"};
	public static final Type[] types = new Type[] { Type.NEEDLE,Type.ARROW};
	
	public static final int MARGIN_LEFT_IN_LAYOUT = 15;
	public static final int MAX_LENGTH_SERIES_NAME = 12;
	public static final int MAX_LENGTH_XY_AXIS = 14;
	public static final int MAX_LENGTH_XY_MIN_MAX = 10;
	public static final int MAX_LENGTH_XY_VALUES = 10;
	public static final int MAX_LENGTH_SERIES_VALUES = 12;
	public static final String CHANGE_SERIES_NAME = "Change Series Name";
	//public static final String CHANGE_CATEGORY_NAME = "Change Category Name";

	public static final String ENTER_TITLE = "Enter Title";

	public static final String INTENT_ACTION_EDIT_CHART = "Intent_Action_Edit_Chart";
	public static final String INTENT_ACTION_EDIT_TITLE = "Intent_Action_Edit_Title";
	public static final String INTENT_ACTION_EDIT_XY_NAME = "Intent_Action_Edit_XY_NAMES";
	public static final String INTENT_ACTION_EDIT_XY_AXIS = "Intent_Action_Edit_XY_AXIS";
	public static final String INTENT_ACTION_EDIT_XY_VALUES = "Intent_Action_Edit_XY_Values";
	public static final String INTENT_ACTION_EDIT_MIN_MAX_VALUES = "Intent_Action_Edit_Min_Max_Values";
	public static final String INTENT_ACTION_EDIT_MARKER_STYLE = "Intent_Action_Edit_Marker_Style";
	public static final String INTENT_ACTION_EDIT_POINTER_TYPE = "Intent_Action_Edit_Pointer_Style";
	public static final String INTENT_ACTION_CHANGE_SERIES_NAME = "Intent_Action_Change_Series_Name";
	public static final String INTENT_ACTION_SHOW_HINT_FOR_REMOVE_ITEMS = "Intent_Action_Show_Hint_For_Remove_Items";
	public static final String INTENT_ACTION_COLOR_PICK = "Intent_Action_Color_Pick";
	public static final String INTENT_ACTION_COLOR_PICK_FOR_SERIES = "Intent_Action_Color_Pick_For_Series";
	public static final String INTENT_ACTION_COLOR_PICK_FOR_COLOR_OF_LINES_DIALOG = "Intent_Action_Color_Pick_For_Color_Of_Lines";
	public static final String Intent_ACTION_SHOW_CHART_VALUES = "Intent_Action_Show_Chart_Values";
	public static final String INTENT_ACTION_PARAM_A = "Intent_Action_Param_A";
	public static final String OK = "Ok";
	public static final String CANCEL = "Cancel";

	public static final float TEXT_SCALE = 2f;
	
	public static AlertDialog.Builder alertDialogBuilder;
	public static AlertDialog alertDialog;
	public static Intent statusIntent;
	
	
	/////////Need///////////////////
	/***
	 * Method to show dialog for hint for removing saved items
	 * @param pActivity Activity that calls this method
	 */
	@SuppressWarnings("deprecation")
	public static void showRemovingItemHintProperty(Activity pActivity){
		
		final Activity activity = pActivity;
		final LinearLayout topLinearLayout;
		
		ChartHelper.dismiss(alertDialog);
		//Set Title of Dialog
		TextView title =createCustomTextView(pActivity, "HINT: Deleting saved items");
		alertDialogBuilder = new AlertDialog.Builder(activity);
		alertDialogBuilder.setCustomTitle(title);
		
		//Create top-most layout
		topLinearLayout = new LinearLayout(activity);
		
		topLinearLayout.setOrientation(LinearLayout.VERTICAL);
		//Create layout params
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		//llp.leftMargin = MARGIN_LEFT_IN_LAYOUT; 
		llp.topMargin = 20;
		TextView msg = new TextView(activity);
		msg.setText("If you want to delete save item, hold it down.");//Hold saved item to be deleted
		msg.setTextSize(15);
		msg.setGravity(Gravity.CENTER);
		topLinearLayout.addView(msg,llp);
		
		final CheckBox ch = new CheckBox(activity);
		ch.setChecked(false);
		ch.setText("Do not Show any more");
		topLinearLayout.addView(ch);
		
		alertDialogBuilder.setView(topLinearLayout);
		
		alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				Intent intent = createIntent(INTENT_ACTION_SHOW_HINT_FOR_REMOVE_ITEMS);
				intent.putExtra(INTENT_ACTION_SHOW_HINT_FOR_REMOVE_ITEMS, ch.isChecked());
				activity.sendBroadcast(intent);
				ChartHelper.dismiss(alertDialog);
			}
		});
	
		 alertDialog = alertDialogBuilder.create();
		 alertDialog.show();
		
	}

	
	protected void showNumberPicker(Context context) {

		final Context myContext = context;
		RelativeLayout relativeLayout = new RelativeLayout(myContext);
	    final NumberPicker aNumberPicker = new NumberPicker(myContext);
	    aNumberPicker.setMaxValue(50);
	    aNumberPicker.setMinValue(1);

	    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
	    RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
	    numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

	    relativeLayout.setLayoutParams(params);
	    relativeLayout.addView(aNumberPicker,numPicerParams);

	    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(myContext);
	    alertDialogBuilder.setTitle("Select the number");
	    alertDialogBuilder.setView(relativeLayout);
	    alertDialogBuilder.setCancelable(false);
	    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				 Toast.makeText(myContext, "The value set: "+aNumberPicker.getValue(), Toast.LENGTH_LONG).show();
			}
		});
	    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				 dialog.cancel();
			}
		});
	    
	    AlertDialog alertDialog = alertDialogBuilder.create();
	    alertDialog.show();
	}
	public static TextView createCustomTextView(Context context,String titleInDialog){
		
		TextView title = new TextView(context);
		// You Can Custom your Title here
		title.setText(titleInDialog);
		title.setBackgroundColor(Color.MAGENTA);
		title.setPadding(10, 15, 15, 10);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.WHITE);
		title.setTextSize(22);
		
		return title;
	}
	public static Intent createIntent(String action){
		
		return new Intent(action);
	}
	/***
	 * 
	 * @param alertDialog AlertDialog object
	 * @param status true if button has to be enabled, otherwise false
	 */
	public static void alertDialogButtonEnabled(AlertDialog alertDialog,boolean status){
		
		if(alertDialog == null)return;
		alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(status);
	}
	
}
