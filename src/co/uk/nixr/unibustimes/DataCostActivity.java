package co.uk.nixr.unibustimes;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class DataCostActivity extends Activity{
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(
    			ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.data_cost_details);
        
	}

}
