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

public class TutorialSwipe extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 7;
    Context context;
    Button [] radios;
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
        radios = new Button[6];
        // Instantiate a ViewPager and a PagerAdapter.
        radios[0] = (Button)findViewById(R.id.radioButton0);
        radios[1] = (Button)findViewById(R.id.radioButton1);
        radios[2] = (Button)findViewById(R.id.radioButton2);
        radios[3] = (Button)findViewById(R.id.radioButton3);
        radios[4] = (Button)findViewById(R.id.radioButton4);
        radios[5] = (Button)findViewById(R.id.radioButton5);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
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
            for(int i=0;i<6;i++)
            {
                radios[i].setBackground((getResources().getDrawable(R.drawable.emepty_dots)));
                radios[i].setWidth(20);
                radios[i].setHeight(20);
            }
            if(position>0) {
                radios[position - 1].setBackground((getResources().getDrawable(R.drawable.white_dots)));
                radios[position - 1].setWidth(25);
                radios[position - 1].setHeight(25);
            }
            else {
                radios[position].setBackground((getResources().getDrawable(R.drawable.white_dots)));
                radios[position].setWidth(25);
                radios[position].setHeight(25);
            }
            if(position==6) {
                Intent intent = new Intent(context, MyPitch.class);
                startActivity(intent);
                TutorialSwipe.this.finish();
            }
            return new FragmentTutorial().newInstance(position);
            //Intent intent=new Intent(context, MyPitch.class);

        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}