package co.uk.nixr.unibustimes;

import java.util.ArrayList;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import co.uk.nixr.unibustimes.CustomMenu.OnMenuItemSelectedListener;

public class InfoActivity extends Activity implements OnMenuItemSelectedListener, OnClickListener{
	
	/**
	 * Some global variables.
	 */
	private CustomMenu mMenu;
	public static final int MENU_ITEM_1 = 1;
	public static final int MENU_ITEM_2 = 2;
	public static final int MENU_ITEM_3 = 3;
	public static final int MENU_ITEM_4 = 4;
	private static final int DIALOG_CLOSE = 0;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(
    			ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.info);
        
        View introductionButton = findViewById(R.id.introductionButton);
        introductionButton.setOnClickListener(this);
        
        View dataCostButton = findViewById(R.id.dataCostButton);
        dataCostButton.setOnClickListener(this);
        
        View aboutSoftwreButton = findViewById(R.id.aboutSoftwareButton);
        aboutSoftwreButton.setOnClickListener(this);
        
        View termsandconditionsButton = findViewById(R.id.termsandconditionsButton);
        termsandconditionsButton.setOnClickListener(this);
        
        mMenu = new CustomMenu(this, this, getLayoutInflater());
        mMenu.setHideOnSelect(true);
        mMenu.setItemsPerLineInPortraitOrientation(4);
        mMenu.setItemsPerLineInLandscapeOrientation(8);
        //load the menu items
        loadMenuItems();
    }

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
        case R.id.introductionButton:
     	  Intent intent1 = new Intent(InfoActivity.this, IntroActivity.class);
     	  startActivity(intent1);
     	  break;
     	  
        case R.id.dataCostButton:
       	  Intent intent2 = new Intent(InfoActivity.this, DataCostActivity.class);
       	  startActivity(intent2);
       	  break;
       	  
        case R.id.aboutSoftwareButton:
         	  Intent intent3 = new Intent(InfoActivity.this, AboutSoftwareActivity.class);
         	  startActivity(intent3);
         	  break;
         	  
        case R.id.termsandconditionsButton:
         	  Intent intent4 = new Intent(InfoActivity.this, TermsAndConditionsActivity.class);
         	  startActivity(intent4);
         	  break;
     	
		}
	}

	/**
     * Snarf the menu key.
     */
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    if (keyCode == KeyEvent.KEYCODE_MENU) {
	    	doMenu();
	    	return true; //always eat it!
	    }
	    /*if (keyCode == KeyEvent.KEYCODE_BACK) {

            //showDialog(DIALOG_CLOSE);
	    	finish();
	        return true;
        }*/
		return super.onKeyDown(keyCode, event); 
	}
	
	@Override
    public void onBackPressed() {
		
            super.onBackPressed();
            finish();
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
		case 1: Intent intent1 = new Intent(InfoActivity.this, UniBusTimesActivity.class);
	       		startActivity(intent1);
	       		break;
		case 2: Uri uri = Uri.parse( "http://www.stagecoachbus.com/" );
				startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
	       		break;
		case 3: Intent intent3 = new Intent(Settings.ACTION_LOCALE_SETTINGS);
				startActivity(intent3);
				break;
		case 4: Intent intent4 = new Intent(InfoActivity.this, InfoActivity.class);
	       		startActivity(intent4);
        break;
		}
	}

}
