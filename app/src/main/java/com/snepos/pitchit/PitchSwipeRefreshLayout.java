package com.snepos.pitchit;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Created by Omer on 26/08/2015.
 */
public class PitchSwipeRefreshLayout extends SwipeRefreshLayout
{
    private ViewPager viewPager;

    public PitchSwipeRefreshLayout(Context context) {super(context);}
    public PitchSwipeRefreshLayout(Context context, AttributeSet attrs) { super(context, attrs);}

    public void setFragment(ViewPager viewPager)
    {
        this.viewPager = viewPager;
    }

    @Override
    public boolean canChildScrollUp()
    {
        return ((MyPitch.AppSectionsPagerAdapter)viewPager.getAdapter()).getPitchFragment(viewPager.getCurrentItem()).canScrollUp();
    }
}
