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
import android.widget.Button;
import android.widget.FrameLayout;

public class TutorialSwipe extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 7;
    Context context;
    Button [] radios;
    boolean isSignUp;
    FrameLayout frameLayout;

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
        radios = new Button[6];
        // Instantiate a ViewPager and a PagerAdapter.
        frameLayout = (FrameLayout) findViewById(R.id.frameLayoutTutorial);
        radios[0] = (Button)findViewById(R.id.radioButton0);
        radios[1] = (Button)findViewById(R.id.radioButton1);
        radios[2] = (Button)findViewById(R.id.radioButton2);
        radios[3] = (Button)findViewById(R.id.radioButton3);
        radios[4] = (Button)findViewById(R.id.radioButton4);
        radios[5] = (Button)findViewById(R.id.radioButton5);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        changeRadios(mPager.getCurrentItem());
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                changeRadios(mPager.getCurrentItem());
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

            if(position==6) {
                Intent intent = new Intent(context, MyPitch.class);
                startActivity(intent);
                TutorialSwipe.this.finish();
            }
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
    public void changeRadios(int position){
        for(int i=0;i<6;i++)
        {
            radios[i].setBackground((getResources().getDrawable(R.drawable.emepty_dots)));
            radios[i].setWidth(20);
            radios[i].setHeight(20);
        }
        radios[position].setBackground((getResources().getDrawable(R.drawable.white_dots)));
        radios[position].setWidth(25);
        radios[position].setHeight(25);

    }
}