package com.snepos.pitchit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.snepos.pitchit.database.Database;
import com.snepos.pitchit.sqliteHelpers.MyPrefs;

/**
 * Created by user1 on 12/06/2015.
 */
public class SplashScreen extends Activity {
    /** Duration of wait **/
    public final int SPLASH_DISPLAY_LENGTH = 3000;

    boolean isFirstTime;
    boolean isLogin;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);


        Database.Init();

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/

         new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {



                new AppEULA(SplashScreen.this).show(SplashScreen.this);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
