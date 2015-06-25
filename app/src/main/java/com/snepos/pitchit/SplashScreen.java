package com.snepos.pitchit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.snepos.pitchit.database.Database;

/**
 * Created by user1 on 12/06/2015.
 */
public class SplashScreen extends Activity {
    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private final String PREFS_NAME = "myPrefs";
    private final String FIRST_TIME = "firstTime";

    boolean isFirstTime;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);



        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        isFirstTime = settings.getBoolean(FIRST_TIME, true);



        if(isFirstTime)
        {

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(FIRST_TIME, false);
            editor.apply();
            Intent mainIntent = new Intent(SplashScreen.this,TutorialSwipe.class);
            SplashScreen.this.startActivity(mainIntent);
            SplashScreen.this.finish();
        }

        Database.Init();

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/

         new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if(!isFirstTime) {
                    Intent mainIntent = new Intent(SplashScreen.this, MyPitch.class);
                    SplashScreen.this.startActivity(mainIntent);
                    SplashScreen.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //if (id == R.id.action_settings) {
        //    return true;
        //}
        return super.onOptionsItemSelected(item);
    }
}
