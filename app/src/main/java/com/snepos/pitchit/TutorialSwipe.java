package com.snepos.pitchit;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class TutorialSwipe extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 4;
    Context context;
    Button [] radios;
    Button start;
    boolean isSignUp;
    FrameLayout frameLayout;
    boolean isLastFrame=false;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        context=this;
        isSignUp=false;
        radios = new Button[4];

        // Instantiate a ViewPager and a PagerAdapter.
        frameLayout = (FrameLayout) findViewById(R.id.frameLayoutTutorial);
        start = (Button)findViewById(R.id.startbtn);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLastFrame) {
                    if (Login.LoadUserEmail(TutorialSwipe.this.getApplicationContext())) {
                        Intent intent = new Intent(context, MyPitch.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("tutorial", true);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        TutorialSwipe.this.finish();
                    } else {
                        Intent intent = new Intent(context, Login.class);
                        startActivity(intent);
                        TutorialSwipe.this.finish();
                    }
                }
            }
        });
        radios[0] = (Button)findViewById(R.id.radioButton1);
        radios[1] = (Button)findViewById(R.id.radioButton2);
        radios[2] = (Button)findViewById(R.id.radioButton3);
        radios[3] = (Button)findViewById(R.id.radioButton4);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        changeRadios(mPager.getCurrentItem());
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                changeRadios(mPager.getCurrentItem());
            }

            @Override
            public void onPageSelected(int i) {

                isLastFrame=false;
                if(mPager.getCurrentItem()==3) {
                    isLastFrame=true;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });



    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }



    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public Fragment getItem(int position) {

            FragmentTutorial myFragment = FragmentTutorial.newInstance(position);
            return myFragment;

            //Intent intent=new Intent(context, MyPitch.class);

        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public void changeSwipeable (boolean isSwipe)
    {
        isSignUp=isSwipe;
        if(isSignUp)
        {
            mPager.setOnTouchListener(null);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void changeRadios(int position){
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        for(int i=0;i<4;i++)
        {
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