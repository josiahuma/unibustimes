package co.uk.nixr.unibustimes;

import java.io.IOException;


import java.util.ArrayList;
import java.util.List;

import com.google.ads.AdRequest;
import com.google.ads.AdView;



//import com.google.android.maps.GeoPoint;
//import com.google.android.maps.MapView;

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
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
//import android.media.audiofx.BassBoost.Settings;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class LocationActivity extends Activity implements OnMenuItemSelectedListener, OnItemSelectedListener, android.view.View.OnClickListener{
	
	/**
	 * Some global variables.
	 */
	private CustomMenu mMenu;
	private TimeHandler th;
	private LocationHandler lh;
	private DestinationHandler dh;
	public static final int MENU_ITEM_1 = 1;
	public static final int MENU_ITEM_2 = 2;
	public static final int MENU_ITEM_3 = 3;
	public static final int MENU_ITEM_4 = 4;
	private static final int DIALOG_CLOSE = 0;
	private DataBaseHelper myDbHelper = new DataBaseHelper(this);
	private SQLiteDatabase sqliteDatabase;
	Spinner spin,spin1,spin2;
	String[]timeList,busList,destinationList;
	List<String> busStops = new ArrayList<String>();
	List<String> destinations = new ArrayList<String>();
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(
    			ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.location);
        
     // Look up the AdView as a resource and load a request.
        AdView adView = (AdView)this.findViewById(R.id.adView);
        adView.loadAd(new AdRequest());
        
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
        
        //get the list of bus stops
        busStops = populateStopList();
        busList = new String[busStops.size()];
        
        for(int i=0; i<busStops.size(); i++)
        {
        	busList[i]=busStops.get(i);
        }
        
        //get the list of destinations
        destinations = populateDestinationList();
        destinationList = new String[destinations.size()];
        
        for(int i=0; i<destinations.size(); i++)
        {
        	destinationList[i]=destinations.get(i);
        }
        
        timeList = new String[3];
        
        timeList[0] = "Now";
        timeList[1] = "+1Hours";
        timeList[2] = "+2Hours";
		
        /*final Button button = (Button) findViewById(R.id.mylocation);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	//findLocaiton();
            	//turnGPSOn();
            	//getCurrentLocation();
            }
        });*/
               
        View viewTimesButton = findViewById(R.id.viewTimesButton);
        viewTimesButton.setOnClickListener(this);
                
        mMenu = new CustomMenu(this, this, getLayoutInflater());
        mMenu.setHideOnSelect(true);
        mMenu.setItemsPerLineInPortraitOrientation(4);
        mMenu.setItemsPerLineInLandscapeOrientation(8);
        //load the menu items
        loadMenuItems();
        
        th = new TimeHandler();
        spin = (Spinner) findViewById(R.id.timeSpinner);
		spin.setOnItemSelectedListener(this);

		@SuppressWarnings({ "unchecked", "rawtypes" })
		ArrayAdapter aa = new ArrayAdapter(
				this,
				android.R.layout.simple_spinner_item, 
				timeList);

		aa.setDropDownViewResource(
		   android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(aa);
        
        lh = new LocationHandler();
        spin1 = (Spinner) findViewById(R.id.locationSpinner);
		spin1.setOnItemSelectedListener(this);

		@SuppressWarnings({ "unchecked", "rawtypes" })
		ArrayAdapter aa1 = new ArrayAdapter(
				this,
				android.R.layout.simple_spinner_item, 
				busList);

		aa1.setDropDownViewResource(
		   android.R.layout.simple_spinner_dropdown_item);
		spin1.setAdapter(aa1);
		
		dh = new DestinationHandler();
        spin2 = (Spinner) findViewById(R.id.destinationSpinner);
		spin2.setOnItemSelectedListener(this);

		@SuppressWarnings({ "unchecked", "rawtypes" })
		ArrayAdapter aa2 = new ArrayAdapter(
				this,
				android.R.layout.simple_spinner_item, 
				destinationList);

		aa2.setDropDownViewResource(
		   android.R.layout.simple_spinner_dropdown_item);
		spin2.setAdapter(aa2);
    }
    
    public List<String> populateStopList(){

		// We have to return a List which contains only String values. Lets create a List first
		List<String> busStopList = new ArrayList<String>();

		// First we need to make contact with the database we have created using the DbHelper class
		//myDbHelper = new DataBaseHelper(this);

		// Then we need to get a readable database
		sqliteDatabase = myDbHelper.getReadableDatabase();

		// We need a a guy to read the database query. Cursor interface will do it for us
		//(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
		String[] columns = new String[]{"_id", "BUS_STOPS_NAME"};
		Cursor cursor = sqliteDatabase.query("BUS_STOPS", columns, null, null, null, null, null);
		// Above given query, read all the columns and fields of the table

		startManagingCursor(cursor);

		// Cursor object read all the fields. So we make sure to check it will not miss any by looping through a while loop
		while (cursor.moveToNext()) {
			// In one loop, cursor read one bus top all details
			//and create a ArrayList of undergraduates
			String busStopId = cursor.getString(cursor.getColumnIndex("_id"));
			String busStopName = cursor.getString(cursor.getColumnIndex("BUS_STOPS_NAME"));
		
			// add it to the bus top list
			busStopList.add(busStopName);
		}

		// If you don't close the database, you will get an error
		myDbHelper.close();
		sqliteDatabase.close();

		return busStopList;
	}
    
    public List<String> populateDestinationList(){

		// We have to return a List which contains only String values. Lets create a List first
		List<String> destinationList = new ArrayList<String>();

		// First we need to make contact with the database we have created using the DbHelper class
		//myDbHelper = new DataBaseHelper(this);

		// Then we need to get a readable database
		sqliteDatabase = myDbHelper.getReadableDatabase();

		// We need a a guy to read the database query. Cursor interface will do it for us
		//(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
		String[] columns = new String[]{"_id", "BUS_DESTINATION"};
		Cursor cursor = sqliteDatabase.query("BUS_DESTINATION", columns, null, null, null, null, null);
		// Above given query, read all the columns and fields of the table

		startManagingCursor(cursor);

		// Cursor object read all the fields. So we make sure to check it will not miss any by looping through a while loop
		while (cursor.moveToNext()) {
			// In one loop, cursor read one bus top all details
			//and create a ArrayList of undergraduates
			String destinationId = cursor.getString(cursor.getColumnIndex("_id"));
			String destinationName = cursor.getString(cursor.getColumnIndex("BUS_DESTINATION"));
		
			// add it to the bus top list
			destinationList.add(destinationName);
		}

		// If you don't close the database, you will get an error
		myDbHelper.close();
		sqliteDatabase.close();

		return destinationList;
	}
    
    public void onClick(View v) 
    {
        switch (v.getId()) 
        {
	        case R.id.viewTimesButton:
	        	finish();
	     	  Intent h = new Intent(LocationActivity.this, TimeTableActivity.class);
	     	  startActivity(h);
	     	  break;
        }
    }
    
    /*private void turnGPSOn(){
        String provider = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3")); 
            sendBroadcast(poke);
        }
    }*/
    
    /*public void findLocaiton()
    {
            String strProvider = "", strContext = "";
            strContext = Context.LOCATION_SERVICE;
            LocationManager locationManager = (LocationManager)getSystemService(strContext);
           
            Log.i("HomePage : ","Defining Criteria.");
           
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
            criteria.setSpeedRequired(false);
            criteria.setCostAllowed(false);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
     
            Log.i("HomePage : ","Defining Provider.");
            strProvider = locationManager.getBestProvider(criteria, true);
            if(strProvider == null)
            {
                    Log.e("HomePage : ", "Provider is not available.");
            }
            else if(strProvider.equals(LocationManager.GPS_PROVIDER))
            {
                    location = locationManager.getLastKnownLocation(strProvider);
                    updateWithNewLocation(location);
                    locationManager.requestLocationUpdates(strProvider, 30000, 0, locationListener);
            }
            else if(strProvider.equals(LocationManager.NETWORK_PROVIDER))
            {
                    location = locationManager.getLastKnownLocation(strProvider);
                    updateWithNewLocation(location);
                    locationManager.requestLocationUpdates(strProvider, 30000, 0, locationListener);
            }
    }*/
    
    /*public void getCurrentLocation() 
    {
        LocationManager locationManager;
        String context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(context);
        Criteria crta = new Criteria();
        crta.setAccuracy(Criteria.ACCURACY_FINE);
        crta.setAltitudeRequired(false);
        crta.setBearingRequired(false);
        crta.setCostAllowed(true);
        crta.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(crta, true);

        locationManager.requestLocationUpdates(provider, 1000, 0,
                new LocationListener() {
                    //@Override
                    public void onStatusChanged(String provider, int status,
                            Bundle extras) {
                    }

                    //@Override
                    public void onProviderEnabled(String provider) {
                    }

                    //@Override
                    public void onProviderDisabled(String provider) {
                    }

                    //@Override
                    public void onLocationChanged(Location location) {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            if (latitude != 0.0 && longitude != 0.0) 
                            {
                                System.out.println("WE GOT THE LOCATION");
                                System.out.println(latitude);
                                System.out.println(longitude);                            
                        	}
                        }

                    }

                });
        Toast msg = Toast.makeText(LocationActivity.this,"Latitude: "+ latitude +"\n"+ "Longitude: "+longitude, Toast.LENGTH_LONG);
		msg.show();
    }*/
    
    /*public void updateWithNewLocation(Location location)
    {
                    if(location != null)
                    {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                    }
                    else
                    {
                            Log.d("HomePage", "No Location Found!");
                    }
    
	    Toast msg = Toast.makeText(LocationActivity.this,"Latitude: "+ latitude +"\n"+ "Longitude: "+longitude, Toast.LENGTH_LONG);
		msg.show();
    }*/
    
    /**
     * Snarf the menu key.
     */
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{ 
	    if (keyCode == KeyEvent.KEYCODE_MENU) 
	    {
	    	doMenu();
	    	return true; //always eat it!
	    }
	    if (keyCode == KeyEvent.KEYCODE_BACK) 
	    {

            showDialog(DIALOG_CLOSE);

            return true;
        }
		return super.onKeyDown(keyCode, event); 
	}
	
	@Override
    protected Dialog onCreateDialog(int id) 
	{
        switch (id) 
        {
        case DIALOG_CLOSE :
                final AlertDialog.Builder closeWindowBuilder = new AlertDialog.Builder(this);
                closeWindowBuilder.setMessage(this.getResources().getString(R.string.alertCloseWindow2))
                   .setCancelable(false)
                   .setNegativeButton(this.getResources().getString(R.string.negative), new DialogInterface.OnClickListener() {
                       public void onClick(final DialogInterface dialog, final int id) {
                           //finish();
     	              }
     	          })
                   .setPositiveButton(this.getResources().getString(R.string.positive), new DialogInterface.OnClickListener() {
                       public void onClick(final DialogInterface dialog, final int id) {
                           //moveTaskToBack(true);
                    	   finish();
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
	private void doMenu() 
	{
		if (mMenu.isShowing()) 
		{
			mMenu.hide();
		} 
		else 
		{
			mMenu.show(findViewById(R.id.textView5));
		}
	}

	/**
     * 
     */
	public void MenuItemSelectedEvent(CustomMenuItem selection) 
	{
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
		case 4: Intent intent4 = new Intent(LocationActivity.this, InfoActivity.class);
   		startActivity(intent4);

        break;
		}
	}

	public void onItemSelected(AdapterView<?> parent, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		if(parent == spin) 
		{
			th.setTime(timeList[arg2].toString());	    
			
		} 
		else if(parent == spin1) 
		{
			lh.setLocation(busList[arg2].toString());	    
			
		} 
		else if(parent == spin2) 
		{
			dh.setDestination(destinationList[arg2].toString());
	    }


	}

	public void onNothingSelected(AdapterView<?> arg0) 
	{
		// TODO Auto-generated method stub
		lh.setLocation("Nothing Selected");
	}
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    if (myDbHelper != null) {
	    	myDbHelper.close();
	    }
	    if (sqliteDatabase != null) {
	    	sqliteDatabase.close();
	    }
	    if (mMenu.isShowing()) 
		{
			mMenu.hide();
		} 
	}
	/*private class MyLocationListener implements LocationListener
	{
	        public void onLocationChanged(Location location)
	        {
	                updateWithNewLocation(location);
	        }
	 
	        public void onProviderDisabled(String provider) {}
	 
	        public void onProviderEnabled(String provider) {}
	 
	        public void onStatusChanged(String provider, int status, Bundle extras) {}
	}*/
		
}
