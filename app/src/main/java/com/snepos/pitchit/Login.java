package com.snepos.pitchit;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.snepos.pitchit.database.Database;
import com.snepos.pitchit.database.HttpHandler;
import com.snepos.pitchit.database.Request;
import com.snepos.pitchit.database.Response;
import com.snepos.pitchit.sqliteHelpers.MyPrefs;

import com.google.identitytoolkit.GitkitClient;
import com.google.identitytoolkit.GitkitUser;
import com.google.identitytoolkit.IdToken;

/**
 * Created by user1 on 26/06/2015.
 */
public class Login extends Fragment {
    private static Handler mHandler;
    private static String userEmail = MyPrefs.NOT_CONNECTED;
    private static String userNickname = "";
    Toolbar toolbar;
    private final int LOGIN_TIMEOUT_LENGTH = 6000;

    Button signIn;

    private GitkitClient client;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_login, container, false);


        signIn = (Button) rootView.findViewById(R.id.signIn_btn);

        signIn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    //Intent mainIntent = new Intent(Login.this, Signup.class);
                    //Login.this.startActivity(mainIntent);
                    if (isNetworkAvailable())
                        client.startSignIn();
                    else
                        Toast.makeText(getActivity(), "please connect to network", Toast.LENGTH_SHORT).show();

                }
                return false;
            }
        });

        if(getActivity().getIntent().getBooleanExtra("JustRegistered", false))
        {
            LoginRequest();
        }

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                               switch (inputMessage.what) {
                    case 0: // Failed
                        Toast.makeText(getActivity(), "Error: Login failed", Toast.LENGTH_SHORT).show();
                        signIn.setEnabled(true);
                        break;
                    case 1: // Succeeded
                        signIn.setEnabled(true);
                        userNickname = (String)inputMessage.obj;
                        String loginKey = KeyGenerator.GenerateKey(userEmail);
                        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(MyPrefs.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean(MyPrefs.LOGIN, true);
                        editor.putString(MyPrefs.EMAIL, userEmail);
                        editor.putString(MyPrefs.LOGINKEY, loginKey);
                        editor.putString(MyPrefs.NICKNAME, userNickname);
                        editor.apply();

                        if(getActivity().getIntent().getBooleanExtra("JustRegistered", false))
                        {
                            Intent mainIntent = new Intent(getActivity(),TutorialSwipe.class);
                            startActivity(mainIntent);
                            getActivity().finish();
                        }
                        else {
                            Intent mainIntent = new Intent(getActivity(), MyPitch.class);
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("tutorial", false);
                            mainIntent.putExtras(bundle);
                            startActivity(mainIntent);
                            getActivity().finish();
                        }
                        break;
                    case 2: // Not registered
                        Intent registerIntent = new Intent(getActivity(), Signup.class);
                        startActivity(registerIntent);
                        getActivity().finish();
                        break;
                }
            }
        };
        setClient();
        return rootView;
    }

    private void LoginRequest()
    {
        Request req = new Request("login", Request.App.Login);
        req.put("email", userEmail);
        req.put("key", KeyGenerator.GenerateKey(userEmail));
        HttpHandler.addRequest(req);

        signIn.setEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                signIn.setEnabled(true);
            }
        }, LOGIN_TIMEOUT_LENGTH);
    }

    public void Connected(){
        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(MyPrefs.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(MyPrefs.FIRST_TIME, false);
        editor.apply();
    }



    public static void LogOut(Context context)
    {
        userEmail = MyPrefs.NOT_CONNECTED;
        SharedPreferences settings = context.getSharedPreferences(MyPrefs.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(MyPrefs.EMAIL, userEmail);
        editor.apply();
    }

    public static boolean LoadUserEmail(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(MyPrefs.PREFS_NAME, 0);
        userEmail = settings.getString(MyPrefs.EMAIL, MyPrefs.NOT_CONNECTED);
        userNickname = settings.getString(MyPrefs.NICKNAME, "");
        String loginKey = settings.getString(MyPrefs.LOGINKEY, "");
        if(!KeyGenerator.CheckKey(userEmail, loginKey))
        {
            userEmail = MyPrefs.NOT_CONNECTED;
        }
        return userEmail != MyPrefs.NOT_CONNECTED;
    }
    public void setClient() {
         client = GitkitClient.newBuilder(this.getActivity(), new GitkitClient.SignInCallbacks() {

            @Override
            public void onSignIn(IdToken idToken, GitkitUser user) {
                if(user.getIdProvider() == null) {
                    Toast.makeText(getActivity(), "Please use Google or Facebook to sign in", Toast.LENGTH_LONG).show();
                }
                else
                {
                    userEmail = user.getEmail();

                    LoginRequest();
                }

            }


            @Override
            public void onSignInFailed() {
                Toast.makeText(getActivity(), "Sign in failed", Toast.LENGTH_LONG).show();
            }
        }).build();
        WelcomeSwipe.setClient(client);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static String GetUserEmail()
    {
        return userEmail;
    }

    public static String GetUserNickname()
    {
        return userNickname;
    }

    public static void HandleResponse(Response response)
    {
        LoginResponseHandler.HandleResponse(mHandler, response);
    }
}
