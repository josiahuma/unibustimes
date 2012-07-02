package co.uk.nixr.unibustimes;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
 
public class SplashActivity extends Activity {
       
        private static final int STOPSPLASH = 0;
        //time in milliseconds
        private static final long SPLASHTIME = 3000;
       
        private ImageView splash;
       
        //handler for splash screen
        private Handler splashHandler = new Handler() {
                /* (non-Javadoc)
                 * @see android.os.Handler#handleMessage(android.os.Message)
                 */
                @Override
                public void handleMessage(Message msg) {
                        switch (msg.what) {
                        case STOPSPLASH:
                                //remove SplashScreen from view
                                splash.setVisibility(View.GONE);
                                break;
                        }
                        super.handleMessage(msg);
                        //startActivity(new Intent("co.uk.nixr.unibustimes.START"));
                        finish();
          	     	  Intent h = new Intent(SplashActivity.this, UniBusTimesActivity.class);
          	     	  startActivity(h);

                }
        };
       
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash);
        this.setRequestedOrientation(
    			ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        splash = (ImageView) findViewById(R.id.splashscreen);
                        Message msg = new Message();
                        msg.what = STOPSPLASH;
                        splashHandler.sendMessageDelayed(msg, SPLASHTIME);
    }
    
    @Override
	public void onBackPressed() {

	   return;
	}
}
