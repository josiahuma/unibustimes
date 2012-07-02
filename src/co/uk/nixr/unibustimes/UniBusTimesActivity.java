package co.uk.nixr.unibustimes;

import java.util.ArrayList;



import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import co.uk.nixr.unibustimes.CustomMenu.OnMenuItemSelectedListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UniBusTimesActivity extends Activity implements OnMenuItemSelectedListener, OnClickListener {
    
	/**
	 * Some global variables.
	 */
	private CustomMenu mMenu;
	public static final int MENU_ITEM_1 = 1;
	public static final int MENU_ITEM_2 = 2;
	public static final int MENU_ITEM_3 = 3;
	public static final int MENU_ITEM_4 = 4;
	private static final int DIALOG_CLOSE = 0;
	private static Toast tag;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(
    			ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.main);
        
     // Look up the AdView as a resource and load a request.
        AdView adView = (AdView)this.findViewById(R.id.adView);
        adView.loadAd(new AdRequest());
        
        View newJourneyButton = findViewById(R.id.newJourneyButton);
        newJourneyButton.setOnClickListener(this);
        
        tag = Toast.makeText(getBaseContext(), R.string.note,Toast.LENGTH_LONG);
        showToast();
              
        mMenu = new CustomMenu(this, this, getLayoutInflater());
        mMenu.setHideOnSelect(true);
        mMenu.setItemsPerLineInPortraitOrientation(4);
        mMenu.setItemsPerLineInLandscapeOrientation(8);
        //load the menu items
        loadMenuItems();
    }
    
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.newJourneyButton:
     	  Intent h = new Intent(UniBusTimesActivity.this, LocationActivity.class);
     	  startActivity(h);
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
	    if (keyCode == KeyEvent.KEYCODE_BACK) {

            showDialog(DIALOG_CLOSE);

            return true;
        }
		return super.onKeyDown(keyCode, event); 
	}
	
	public void showToast()
	{
		//tag.setGravity(Gravity.CENTER_HORIZONTAL,0, 0);
        tag.show();
	}
	
	public void cancelToast()
	{
		tag.cancel();
	}
	
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_CLOSE :
                final AlertDialog.Builder closeWindowBuilder = new AlertDialog.Builder(this);
                closeWindowBuilder.setMessage(this.getResources().getString(R.string.alertCloseWindow))
                   .setCancelable(false)
                   .setNegativeButton(this.getResources().getString(R.string.negative), new DialogInterface.OnClickListener() {
                       public void onClick(final DialogInterface dialog, final int id) {
                           //finish();
     	              }
     	          })
                   .setPositiveButton(this.getResources().getString(R.string.positive), new DialogInterface.OnClickListener() {
                       public void onClick(final DialogInterface dialog, final int id) {
                           //moveTaskToBack(true);
                    	   UniBusTimesActivity.this.finish();
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
		case 1: Intent intent1 = new Intent(UniBusTimesActivity.this, UniBusTimesActivity.class);
	       		startActivity(intent1);
	       		break;
		case 2: Uri uri = Uri.parse( "http://www.stagecoachbus.com/" );
				startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
	       		break;
		case 3: Intent intent3 = new Intent(Intent.ACTION_MAIN);
				intent3.setClassName("com.android.settings", "com.android.settings.LanguageSettings");            
				startActivity(intent3);
				break;
		case 4: Intent intent4 = new Intent(UniBusTimesActivity.this, InfoActivity.class);
	       		startActivity(intent4);
        break;
		}
	}
	  
	@Override
    protected void onResume() {
    super.onResume();
    showToast();
    }
	
	@Override
    protected void onPause() {
    super.onPause();
    cancelToast();
    }
	
	protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed. 
       android.os.Process.killProcess(android.os.Process.myPid());
    }
}