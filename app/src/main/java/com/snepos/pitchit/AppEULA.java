/**
 * This file provides simple End User License Agreement
 * It shows a simple dialog with the license text, and two buttons.
 * If user clicks on 'cancel' button, app closes and user will not be granted access to app.
 * If user clicks on 'accept' button, app access is allowed and this choice is saved in preferences
 * so next time this will not show, until next upgrade.
 */
package com.snepos.pitchit;

import com.snepos.pitchit.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

public class AppEULA {
    private String EULA_PREFIX = "&quot;appeula&quot;";
    private Activity mContext;
    private static  Activity _activity;

    public AppEULA(Activity context) {
        mContext = context;
    }

    private PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }

    public void show(Activity activity) {
        _activity = activity;
        PackageInfo versionInfo = getPackageInfo();

        // The eulaKey changes every time you increment the version number in
        // the AndroidManifest.xml
        final String eulaKey = EULA_PREFIX + versionInfo.versionCode;
        final SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(mContext);

        boolean bAlreadyAccepted = prefs.getBoolean(eulaKey, false);
        if (bAlreadyAccepted == false) {

            // EULA title
            String title = mContext.getString(R.string.app_name) + versionInfo.versionName
                    + " - Terms and conditions:";
            // EULA text
            String message = mContext.getString(R.string.eula_string_to_button)
                    + mContext.getString(R.string.eula_accept)
                    + mContext.getString(R.string.eula_string_from_button);

            // Disable orientation changes, to prevent parent activity
            // reinitialization
            mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.accept,
                            new Dialog.OnClickListener() {

                                @Override
                                public void onClick(
                                        DialogInterface dialogInterface, int i) {
                                    // Mark this version as read.
                                    SharedPreferences.Editor editor = prefs
                                            .edit();
                                    editor.putBoolean(eulaKey, true);
                                    editor.commit();

                                    // Close dialog
                                    dialogInterface.dismiss();

                                    // Enable orientation changes based on
                                    // device's sensor
                                    mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

                                    StartApp();
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new Dialog.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // Close the activity as they have declined
                                    // the EULA
                                    mContext.finish();
                                    mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                                }

                            });
            builder.create().show();
        }
        else
        {
            StartApp();
        }
    }

    static void StartApp()
    {
        if(Login.LoadUserEmail(_activity.getApplicationContext()))
        {
            Intent mainIntent = new Intent(_activity, MyPitch.class);
            _activity.startActivity(mainIntent);
            _activity.finish();
        }
        else
        {
            Intent mainIntent = new Intent(_activity, Login.class);
            _activity.startActivity(mainIntent);
            _activity.finish();
        }
    }
}