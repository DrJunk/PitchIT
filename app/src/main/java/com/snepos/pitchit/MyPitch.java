package com.snepos.pitchit;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.snepos.pitchit.database.Database;
import com.snepos.pitchit.database.IdeaData;
import com.snepos.pitchit.sqliteHelpers.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

//import android.support.v7.widget.Toolbar;


public class MyPitch extends ActionBarActivity implements  NavigationDrawerFragment.NavigationDrawerCallbacks , ActionBar.TabListener   {
    public static Handler mHandler;

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    Context context;
    Button FAB;
    //static FeedReaderDbHelper mDbHelper ;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pitch);
        final ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFFBE3A27));
        actionBar.setIcon(R.drawable.icon);
       // actionBar.setStripEnabled(false);

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {

                switch (inputMessage.what) {
                    case 0:
                        IdeaData[] data = null;
                        ListView listView = null;
                        switch((Integer)inputMessage.obj)
                        {
                            case 0:
                                data = Database.news;
                                listView = (ListView) MyPitch.this.findViewById(R.id.card_listView_new);
                                break;
                            case 1:
                                data = Database.trending;
                                listView = (ListView) MyPitch.this.findViewById(R.id.card_listView_trending);
                                break;
                            case 2:
                                data = Database.hot;
                                listView = (ListView) MyPitch.this.findViewById(R.id.card_listView_hot);
                                break;
                        }
                        if (listView != null) {
                            if (data != null) {
                                CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(MyPitch.this.getApplicationContext(), R.layout.pitch_item);

                                for (int i = 0; i < data.length; i++) {
                                    Card c = new Card(data[i].id, data[i].title, data[i].text, false, 0, false, false, data[i].upVotes, data[i].imOnItVotes);
                                    cardArrayAdapter.add(c);
                                }

                                listView.setAdapter(cardArrayAdapter);
                            } else {
                                System.err.println("Error: 'data' is null(" + inputMessage.what + ")");
                            }
                        }
                }
            }
        };

        Database.PostRefreshNews();
        Database.PostRefreshTrending();
        Database.PostRefreshHot();


        context= this;
       // mDbHelper = new FeedReaderDbHelper(context);
        FAB = (Button) findViewById(R.id.button_add);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MyPost.class);
                startActivity(intent);

                //Toast.makeText(MyPitch.this, "Refresh", Toast.LENGTH_SHORT).show();


            }
        });

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.

       mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());



        int currentapiVersion = android.os.Build.VERSION.SDK_INT;

        if (currentapiVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        actionBar.setHomeButtonEnabled(false);
        } else {
        // do something for phones running an SDK before froyo
        }




        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //actionBar.setBackgroundDrawable(new ColorDrawable(0xFFFF7F27));

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });





        /*
        //TextView Head = (TextView) view.findViewById(R.id.HeadName);

        //View tabView = inflater.inflate(R.layout.my_tab_layout, null);
        LayoutInflater inflater ;
        inflater = getLayoutInflater();
        View tabView = inflater.inflate(R.layout.my_tab_layout, null);
        tabView.setBackgroundColor(Color.RED); // set custom color
        tab.setCustomView(tabView);*/

        //actionBar.addTab(
         //       actionBar.newTab()
           //             .setText("wow")
           //             .setTabListener(this));
        TextView tv = new TextView(context);
        tv.setText("hot");
        tv.setTextColor(Color.GRAY);
        //tv.setHeight(500);
        //tv.setWidth(200);
        tv.setLayoutParams(new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, 1f));
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20);

        TextView tv1 = new TextView(context);
        tv1.setText("brewing");
        tv1.setTextColor(Color.GRAY);
        //tv1.setHeight(500);
        //tv1.setWidth(200);
        tv1.setLayoutParams(new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, 1f));
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextSize(20);

        TextView tv2 = new TextView(context);
        tv2.setText("new");
        tv2.setTextColor(Color.GRAY);
        //tv2.setHeight(500);
        //tv2.setWidth(200);
        tv2.setLayoutParams(new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, 1f));
        tv2.setGravity(Gravity.CENTER);
        tv2.setTextSize(20);
        //if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        actionBar.addTab(
                actionBar.newTab()
                        .setCustomView(tv)
                        .setTabListener(this));
        actionBar.addTab(
                actionBar.newTab()
                        .setCustomView(tv1)
                        .setTabListener(this));
        actionBar.addTab(
                actionBar.newTab()
                        .setCustomView(tv2)
                        .setTabListener(this));


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        TextView tv=(TextView)tab.getCustomView();
        tv.setTextColor(Color.GRAY);
    }
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
        TextView tv=(TextView)tab.getCustomView();
        tv.setTextColor(Color.WHITE);

    }
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        if(position==1)
        {

            Intent intent=new Intent(context, TutorialSwipe.class);
            startActivity(intent);
            this.finish();

           // Bundle args = new Bundle();
            //args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
           // fragment.setArguments(args);
            //FragmentTutorial fragment = new FragmentTutorial();
            //FragmentTutorial.newInstance(1);

            //FragmentManager fragmentManager = getFragmentManager();
            //fragmentManager.invalidateOptionsMenu();

            //fragmentManager.beginTransaction()
              //      .replace(R.id.container, FragmentTutorial.newInstance(1))
              //      .commit();

        }
        else {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                    .commit();
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.my_pitch, menu);
            //getMenuInflater().inflate(R.menu.my_pitch, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {

            Database.PostRefreshNews();
            Database.PostRefreshTrending();
            Database.PostRefreshHot();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new HotFragment();
                case 1:
                    return new TrendingFragment();
                case 2:
                    return new NewFragment();
                default:
                    return new ErrorFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Section " + (position + 1);
        }
    }
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my_pitch, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MyPitch) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public static class NewFragment extends android.support.v4.app.Fragment {

        CardArrayAdapter cardArrayAdapter;
        ListView listView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_new, container, false);
            listView = (ListView) rootView.findViewById(R.id.card_listView_new);

            listView.addHeaderView(new View(rootView.getContext()));
            listView.addFooterView(new View(rootView.getContext()));

            cardArrayAdapter = new CardArrayAdapter(getActivity().getApplicationContext(), R.layout.pitch_item);
            IdeaData[] data = Database.news;
            if (data != null) {
                for (int i = 0; i < data.length; i++) {
                    Card c = new Card(data[i].id, data[i].title, data[i].text, false, 0, false, false, data[i].upVotes, data[i].imOnItVotes);
                    cardArrayAdapter.add(c);
                }
                listView.setAdapter(cardArrayAdapter);
            } else {
                System.err.println("Error: 'data' is null");
            }
            /*
            List<Card> newPosts = new ArrayList<Card>();

            DatabaseHandler db = new DatabaseHandler(this.getActivity());
            newPosts = db.getAllPosts(DatabaseHandler.Table.NEW);
            if(!newPosts.isEmpty())
            {
                for (int i = 0; i < db.getPostsCount(DatabaseHandler.Table.NEW); i++) {
                    if(newPosts.get(i).getId() != 0)
                    cardArrayAdapter.add(new Card(newPosts.get(i).getId(), newPosts.get(i).getHead(), newPosts.get(i).getBody(), false, 0, false, false, 0, 0));
                }
            }
            */
            listView.setAdapter(cardArrayAdapter);
            return rootView;
        }
    }
    public static class TrendingFragment extends android.support.v4.app.Fragment {

        CardArrayAdapter cardArrayAdapter;
        ListView listView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_trending, container, false);
            listView = (ListView) rootView.findViewById(R.id.card_listView_trending);

            listView.addHeaderView(new View(rootView.getContext()));
            listView.addFooterView(new View(rootView.getContext()));

            cardArrayAdapter = new CardArrayAdapter(getActivity().getApplicationContext(), R.layout.pitch_item);
            IdeaData[] data = Database.trending;
            if (data != null) {
                for (int i = 0; i < data.length; i++) {
                    Card c = new Card(data[i].id, data[i].title, data[i].text, false, 0, false, false, data[i].upVotes, data[i].imOnItVotes);
                    cardArrayAdapter.add(c);
                }
                listView.setAdapter(cardArrayAdapter);
            } else {
                System.err.println("Error: 'data' is null");
            }

            listView.setAdapter(cardArrayAdapter);
            return rootView;
        }
    }
    public static class HotFragment extends android.support.v4.app.Fragment {

        CardArrayAdapter cardArrayAdapter;
        ListView listView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_hot, container, false);
            listView = (ListView) rootView.findViewById(R.id.card_listView_hot);

            listView.addHeaderView(new View(rootView.getContext()));
            listView.addFooterView(new View(rootView.getContext()));

            cardArrayAdapter = new CardArrayAdapter(getActivity().getApplicationContext(), R.layout.pitch_item);
            IdeaData[] data = Database.hot;
            if (data != null) {
                for (int i = 0; i < data.length; i++) {
                    Card c = new Card(data[i].id, data[i].title, data[i].text, false, 0, false, false, data[i].upVotes, data[i].imOnItVotes);
                    cardArrayAdapter.add(c);
                }
                listView.setAdapter(cardArrayAdapter);
            } else {
                System.err.println("Error: 'data' is null");
            }

            listView.setAdapter(cardArrayAdapter);
            return rootView;
        }
    }
    public static class ErrorFragment extends android.support.v4.app.Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_error, container, false);
            return rootView;

        }
    }


}
