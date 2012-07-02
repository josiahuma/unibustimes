package co.uk.nixr.unibustimes;

import java.io.IOException;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import co.uk.nixr.unibustimes.CustomMenu.OnMenuItemSelectedListener;
import co.uk.nixr.unibustimes.db.DataBaseHelper;
import co.uk.nixr.unibustimes.handlers.DestinationHandler;
import co.uk.nixr.unibustimes.handlers.LocationHandler;
import co.uk.nixr.unibustimes.handlers.TimeHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TableRow.LayoutParams; 
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TimeTableActivity extends Activity implements OnMenuItemSelectedListener{
	
	private static TextView mylocation;
	private DestinationHandler dh;
	private TimeHandler th;
	private LocationHandler lh;
	String hour,day;
	Cursor cursor;
	private CustomMenu mMenu;
	public static final int MENU_ITEM_1 = 1;
	public static final int MENU_ITEM_2 = 2;
	public static final int MENU_ITEM_3 = 3;
	public static final int MENU_ITEM_4 = 4;
	private static final int DIALOG_CLOSE = 0;
	private DataBaseHelper myDbHelper = new DataBaseHelper(this);
	private SQLiteDatabase sqliteDatabase;
	private SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	private TableLayout tl;
	List<String> busTimeList;
	List<String> busDestinationList;
	HashMap<String,String> busTimeMapping;

	private String time,location, destination;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(
    			ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.time_table);
        
        Calendar c = Calendar.getInstance(); 
        int seconds = c.get(Calendar.SECOND);
        int minutes = c.get(Calendar.MINUTE);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int days = c.get(Calendar.DAY_OF_WEEK);

        if(days==1)
        {
        	day = "Sunday";
        }
        if(days==2)
        {
        	day = "Monday";
        }
        if(days==3)
        {
        	day = "Tuesday";
        }
        if(days==4)
        {
        	day = "Wednesday";
        }
        if(days==5)
        {
        	day = "Thursday";
        }
        if(days==6)
        {
        	day = "Friday";
        }
        if(days==7)
        {
        	day = "Saturday";
        }
        
        
        th = new TimeHandler();
        time = th.getTime();
        
        if(time.equalsIgnoreCase("now"))
        {
        	if(hours==0||hours==1||hours==2||hours==3||hours==4||hours==5||hours==6||hours==7||hours==8||hours==9)
            {
            	hour = "0"+hours;
            }
            else
            {
            	hour = hours+"";
            }
        }
        else if(time.equalsIgnoreCase("+1Hours"))
        {
        	hours = hours+1;
        	if(hours==0||hours==1||hours==2||hours==3||hours==4||hours==5||hours==6||hours==7||hours==8||hours==9)
            {
            	hour = "0"+hours;
            }
            else
            {
            	hour = hours+"";
            }
        }
        else if(time.equalsIgnoreCase("+2Hours"))
        {
        	hours = hours+2;
        	if(hours==0||hours==1||hours==2||hours==3||hours==4||hours==5||hours==6||hours==7||hours==8||hours==9)
            {
            	hour = "0"+hours;
            }
            else
            {
            	hour = hours+"";
            }
        }
        else
        {
        	if(hours==0||hours==1||hours==2||hours==3||hours==4||hours==5||hours==6||hours==7||hours==8||hours==9)
            {
            	hour = "0"+hours;
            }
            else
            {
            	hour = hours+"";
            }
        	
        }
        
        
        
        
        lh = new LocationHandler();
        location=lh.getLocation();
        mylocation = (TextView) findViewById(R.id.setLocation);
        mylocation.setText(location);
        
        dh = new DestinationHandler();
        destination = dh.getDestination();
        
        tl = (TableLayout) this.findViewById(R.id.time_table);
        
        mMenu = new CustomMenu(this, this, getLayoutInflater());
        mMenu.setHideOnSelect(true);
        mMenu.setItemsPerLineInPortraitOrientation(4);
        mMenu.setItemsPerLineInLandscapeOrientation(8);
        //load the menu items
        loadMenuItems();
        
        myDbHelper = new DataBaseHelper(this);
        
        try {
 
        	myDbHelper.createDataBase();
 
        } catch (IOException ioe) {
 
 		throw new Error("Unable to create database");
 
        }
 
        try {
 
        	myDbHelper.openDataBase();
        	
        }catch(SQLException sqle){
 
 		throw sqle;
 
        }
        populateTimeList();
        showTimes();
	}
	
	public List<String> populateTimeList(){

		// We have to return a List which contains only String values. Lets create a List first
		busTimeList = new ArrayList<String>();
		busDestinationList = new ArrayList<String>();
		busTimeMapping = new HashMap<String,String>();

		// First we need to make contact with the database we have created using the DbHelper class
		//myDbHelper = new DataBaseHelper(this);

		// Then we need to get a readable database
		sqliteDatabase = myDbHelper.getReadableDatabase();

		// We need a a guy to read the database query. Cursor interface will do it for us
		//(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
		//String[] columns = new String[]{"_id", "BUS_STOPS_NAME"};
		//Cursor cursor = sqliteDatabase.rawQuery("Select * from BUS_STOPS_TIMES WHERE BUS_STOPS_ID ='"+location+"'",null);
		if(destination.equalsIgnoreCase("city centre bus station"))
		{
			if(day.equalsIgnoreCase("sunday"))
			{
				cursor = sqliteDatabase.rawQuery("Select * from BUS_STOPS_TIMES WHERE BUS_STOPS_NAME ='"+location+"' AND DESTINATION ='City Centre' AND DAY ='sun'",null);
			}
			else if(day.equalsIgnoreCase("saturday"))
			{
				cursor = sqliteDatabase.rawQuery("Select * from BUS_STOPS_TIMES WHERE BUS_STOPS_NAME ='"+location+"' AND DESTINATION ='City Centre' AND DAY ='sat'",null);
			}
			else
			{
				cursor = sqliteDatabase.rawQuery("Select * from BUS_STOPS_TIMES WHERE BUS_STOPS_NAME ='"+location+"' AND DESTINATION ='City Centre' AND DAY ='mon-fri'",null);
			}
		// Above given query, read all the columns and fields of the table
		}
		else if(destination.equalsIgnoreCase("university parkwood, darwin"))
		{
			if(day.equalsIgnoreCase("sunday"))
			{
				cursor = sqliteDatabase.rawQuery("Select * from BUS_STOPS_TIMES WHERE BUS_STOPS_NAME ='"+location+"' AND DESTINATION ='University Parkwood, Darwin' AND DAY ='sun'",null);
			}
			else if(day.equalsIgnoreCase("saturday"))
			{
				cursor = sqliteDatabase.rawQuery("Select * from BUS_STOPS_TIMES WHERE BUS_STOPS_NAME ='"+location+"' AND DESTINATION ='University Parkwood, Darwin' AND DAY ='sat'",null);
			}
			else
			{
				cursor = sqliteDatabase.rawQuery("Select * from BUS_STOPS_TIMES WHERE BUS_STOPS_NAME ='"+location+"' AND DESTINATION ='University Parkwood, Darwin' AND DAY ='mon-fri'",null);
			}
		// Above given query, read all the columns and fields of the table
			
		}
		startManagingCursor(cursor);

		// Cursor object read all the fields. So we make sure to check it will not miss any by looping through a while loop
		while (cursor.moveToNext()) {
			// In one loop, cursor read one bus top all details
			//and create a ArrayList of undergraduates
			String busTimeId = cursor.getString(cursor.getColumnIndex("_id"));
			String busTimes = cursor.getString(cursor.getColumnIndex("BUS_TIMES"));
			String busTimeName = cursor.getString(cursor.getColumnIndex("BUS_STOPS_NAME"));
			String busTimeDestination = cursor.getString(cursor.getColumnIndex("DESTINATION"));
			
			Log.i ("info", busTimeId);
			Log.i ("info", busTimes);
			Log.i ("info", busTimeName);
			Log.i ("info", busTimeDestination);
			Log.i ("info", hour);
			Log.i ("info", day);
		
			// add it to the bus stop list
			busTimeList.add(busTimes);
			busDestinationList.add(busTimeDestination);
			// add it to the bus mapping
			busTimeMapping.put(busTimeId, busTimeDestination);
			
			//add to row
			
		}

		// If you don't close the database, you will get an error
		myDbHelper.close();
		sqliteDatabase.close();

		return busTimeList;
	}
	
	public void showTimes(){
		
		List<String>tempBusTimes = new ArrayList<String>();
		List<String>tempBusDestination = new ArrayList<String>();
		
		for(int i=0; i<busTimeList.size(); i++)
		{
			String tempTime = busTimeList.get(i).toString();
			if(tempTime.charAt(0)==hour.charAt(0) && tempTime.charAt(1)==hour.charAt(1))
			{
				tempBusTimes.add(busTimeList.get(i).toString());
				tempBusDestination.add(busDestinationList.get(i).toString());
			}
			else
			{
				
			}
			
		}
		
		for(int i=0; i<tempBusTimes.size(); i++)
		{
			TableRow tr = new TableRow(this);
	        tr.setLayoutParams(new LayoutParams(
	                       LayoutParams.FILL_PARENT,
	                       LayoutParams.WRAP_CONTENT));	               
	             /* Create a TextView for times to be the row-content. */
	             TextView times = new TextView(this);
	             times.setText(tempBusTimes.get(i).toString());
	             times.setTextColor(getResources().getColor(R.color.black));
	             times.setLayoutParams(new LayoutParams(
	                       LayoutParams.FILL_PARENT,
	                       LayoutParams.WRAP_CONTENT));
	             /* Create a TextView for times to be the row-content. */
	             TextView destination = new TextView(this);
	             destination.setText(tempBusDestination.get(i).toString());
	             destination.setTextColor(getResources().getColor(R.color.black));
	             destination.setLayoutParams(new LayoutParams(
	                       LayoutParams.FILL_PARENT,
	                       LayoutParams.WRAP_CONTENT));
	             /* Add times to row. */
	             tr.addView(times);
	             tr.addView(destination);
	   /* Add row to TableLayout. */
	   tl.addView(tr,new TableLayout.LayoutParams(
	             LayoutParams.FILL_PARENT,
	             LayoutParams.WRAP_CONTENT));
			
		}
		
		/* Find Tablelayout defined in main.xml */
        
        /* Create a new row to be added. */
	
		
	}
	
	/**
     * Snarf the menu key.
     */
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    if (keyCode == KeyEvent.KEYCODE_MENU) {
	    	doMenu();
	    	return true; //always eat it!
	    }
	    if (keyCode == KeyEvent.KEYCODE_BACK) {

            showDialog(DIALOG_CLOSE);

            return true;
        }
		return super.onKeyDown(keyCode, event); 
	}
	
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_CLOSE :
                final AlertDialog.Builder closeWindowBuilder = new AlertDialog.Builder(this);
                closeWindowBuilder.setMessage(this.getResources().getString(R.string.alertCloseWindow3))
                   .setCancelable(false)
                   .setNegativeButton(this.getResources().getString(R.string.negative), new DialogInterface.OnClickListener() {
                       public void onClick(final DialogInterface dialog, final int id) {
                           //finish();
     	              }
     	          })
                   .setPositiveButton(this.getResources().getString(R.string.positive), new DialogInterface.OnClickListener() {
                       public void onClick(final DialogInterface dialog, final int id) {
                    	   finish();
                    	   Intent intent = new Intent(TimeTableActivity.this, LocationActivity.class);
                    	   startActivity(intent);
    	          }
 	          });
            return closeWindowBuilder.create();
        }
        return null;
        }
	
	private void loadMenuItems() {
		//This is kind of a tedious way to load up the menu items.
		//Am sure there is room for improvement.
		ArrayList<CustomMenuItem> menuItems = new ArrayList<CustomMenuItem>();
		CustomMenuItem cmi = new CustomMenuItem();
		cmi.setCaption(this.getResources().getString(R.string.iconHome));
		cmi.setImageResourceId(R.drawable.ic_menu_home);
		cmi.setId(MENU_ITEM_1);
		menuItems.add(cmi);
		cmi = new CustomMenuItem();
		cmi.setCaption(this.getResources().getString(R.string.iconWeb));
		cmi.setImageResourceId(R.drawable.ic_globe);
		cmi.setId(MENU_ITEM_2);
		menuItems.add(cmi);
		cmi = new CustomMenuItem();
		cmi.setCaption(this.getResources().getString(R.string.iconSettings));
		cmi.setImageResourceId(R.drawable.ic_menu_settings);
		cmi.setId(MENU_ITEM_3);
		menuItems.add(cmi);
		cmi = new CustomMenuItem();
		cmi.setCaption(this.getResources().getString(R.string.iconInfo));
		cmi.setImageResourceId(R.drawable.ic_dialog_info);
		cmi.setId(MENU_ITEM_4);
		menuItems.add(cmi);
		if (!mMenu.isShowing())
		try {
			mMenu.setMenuItems(menuItems);
		} catch (Exception e) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Egads!");
			alert.setMessage(e.getMessage());
			alert.show();
		}
	}
	
	/**
     * Toggle our menu on user pressing the menu key.
     */
	private void doMenu() {
		if (mMenu.isShowing()) {
			mMenu.hide();
		} else {
			//Note it doesn't matter what widget you send the menu as long as it gets view.
			mMenu.show(findViewById(R.id.textView1));
		}
	}

	/**
     * For the demo just toast the item selected.
     */
	public void MenuItemSelectedEvent(CustomMenuItem selection) {
		switch(selection.getId())
		{
			case 1: finish();
		       		break;
			case 2: Uri uri = Uri.parse( "http://www.stagecoachbus.com/" );
   					startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
		       		break;
			case 3: Intent intent3 = new Intent(Intent.ACTION_MAIN);
					intent3.setClassName("com.android.settings", "com.android.settings.LanguageSettings");            
					startActivity(intent3);
					break;
			case 4: Intent intent4 = new Intent(TimeTableActivity.this, InfoActivity.class);
		       		startActivity(intent4);
	        break;
		}
	}


}
