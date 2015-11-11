package com.snepos.pitchit;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.identitytoolkit.GitkitClient;
import com.google.identitytoolkit.GitkitUser;
import com.google.identitytoolkit.IdToken;
import com.snepos.pitchit.database.HttpHandler;
import com.snepos.pitchit.database.Request;
import com.snepos.pitchit.database.Response;
import com.snepos.pitchit.sqliteHelpers.MyPrefs;

public class WelcomeSwipe extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 4;
    Context context;
    Button[] radios;
    FrameLayout frameLayout;
    boolean isLastFrame = false;
    Login myFragment;
    boolean isFromStart=false;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private CustomViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    private static GitkitClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        context = this;
        radios = new Button[4];

        // Instantiate a ViewPager and a PagerAdapter.
        frameLayout = (FrameLayout) findViewById(R.id.frameLayoutWelcome);
        radios[0] = (Button) findViewById(R.id.radioButton1);
        radios[1] = (Button) findViewById(R.id.radioButton2);
        radios[2] = (Button) findViewById(R.id.radioButton3);
        radios[3] = (Button) findViewById(R.id.radioButton4);



        mPager = (CustomViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        changeRadios(mPager.getCurrentItem());

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
            isFromStart = bundle.getBoolean("start", false);
        if (mPager.getCurrentItem() == 0&&!isFromStart) {
            isLastFrame = true;
            changeSwipeable();
            fadeOutButtons(0);//get this out
        }

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                changeRadios(mPager.getCurrentItem());

            }

            @Override
            public void onPageSelected(int i) {

                isLastFrame = false;
                if (mPager.getCurrentItem() == 3) {
                    isLastFrame = true;
                    changeSwipeable();
                    fadeOutButtons(250);
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }

    public static void setClient(GitkitClient c)
    {
        client=c;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public Fragment getItem(int position) {

            if (position <= 2 && isFromStart) {
                FragmentWelcome mytFragment = FragmentWelcome.newInstance(position);
                return mytFragment;
            } else {
                myFragment = new Login();
                return myFragment;
            }


        }

        @Override
        public int getCount() {
                return NUM_PAGES;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (!client.handleActivityResult(requestCode, resultCode, intent)) {
            super.onActivityResult(requestCode, resultCode, intent);
        }

    }


    @Override
    protected void onNewIntent(Intent intent) {
        if (!client.handleIntent(intent)) {
            super.onNewIntent(intent);
        }
    }

    void fadeOutButtons(int time)
    {

        for (int i=0;i<4;i++) {
            final int temp =i;
            Animation fadeOut = new AlphaAnimation(1, 0);
            fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
            AnimationSet animation = new AnimationSet(false); //change to false
            fadeOut.setStartOffset(time*(i));
            fadeOut.setDuration(time*2);
            animation.addAnimation(fadeOut);
            animation.setAnimationListener(new Animation.AnimationListener(){
                @Override
                public void onAnimationStart(Animation arg0) {
                }
                @Override
                public void onAnimationRepeat(Animation arg0) {
                }
                @Override
                public void onAnimationEnd(Animation arg0) {
                    radios[temp].setVisibility(View.INVISIBLE);
                }
            });
            radios[i].setAnimation(animation);
        }



    }

    public void changeSwipeable() {
            mPager.setPagingEnabled(false);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void changeRadios(int position) {
        int currentapiVersion = Build.VERSION.SDK_INT;
        for (int i = 0; i < 4; i++) {
            if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN)
                radios[i].setBackground((getResources().getDrawable(R.drawable.emepty_dots)));
            radios[i].setWidth(20);
            radios[i].setHeight(20);
        }
        if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN)
            radios[position].setBackground((getResources().getDrawable(R.drawable.white_dots)));
        radios[position].setWidth(25);
        radios[position].setHeight(25);

    }
}

