package com.snepos.pitchit;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
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
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.snepos.pitchit.database.Database;
import com.snepos.pitchit.database.IdeaData;
import com.snepos.pitchit.database.Response;
import com.snepos.pitchit.sqliteHelpers.DatabaseHandler;

//import android.support.v7.widget.Toolbar;


public class MyPitch extends ActionBarActivity implements ActionBar.TabListener   {
    private static Handler mHandler;
    private static boolean refreshing = false;
    private static boolean canRefresh = true;
    private final int REFRESH_DELTA_LENGTH = 3000;

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    Context context;
    Button FAB;

    private View mRefresh;
    private MenuItem mRefreshItem;
    private Animation mRotate;

    //static FeedReaderDbHelper mDbHelper ;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */


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

        // Animation test:
        canRefresh = true;
        mRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_refresh);
        mRotate.setRepeatCount(Animation.INFINITE);
        mRotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if (!refreshing) {
                    mRefreshItem.getActionView().clearAnimation();
                    mRefreshItem.setActionView(null);
                }
            }
        });

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRefresh = inflater.inflate(R.layout.iv_refresh, null);

        // End of test

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                refreshing = false;
                switch (inputMessage.what) {
                    case 0: // Failed to receive update
                        switch ((Integer) inputMessage.obj) {
                            case 0:
                                Toast.makeText(getApplicationContext(), "Error: Failed to communicate with the server", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(getApplicationContext(), "Error: Failed to receive an update", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        break;
                    case 1:  // New data from server
                        IdeaData[] data = null;
                        ListView listView = null;
                        DatabaseHandler.Table table = null;
                        switch ((Integer) inputMessage.obj) {
                            case 0:
                                data = Database.news;
                                listView = (ListView) MyPitch.this.findViewById(R.id.card_listView_new);
                                table = DatabaseHandler.Table.NEW;
                                break;
                            case 1:
                                data = Database.trending;
                                listView = (ListView) MyPitch.this.findViewById(R.id.card_listView_trending);
                                table = DatabaseHandler.Table.GROWING;
                                break;
                            case 2:
                                data = Database.hot;
                                listView = (ListView) MyPitch.this.findViewById(R.id.card_listView_hot);
                                table = DatabaseHandler.Table.POPULAR;
                                break;
                        }
                        if (data != null) {
                            CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(MyPitch.this.getApplicationContext(), R.layout.pitch_item);

                            Card[] cards = new Card[data.length];
                            for (int i = 0; i < data.length; i++) {
                                Card c = new Card(data[i].id, data[i].title, data[i].publisherName, data[i].text, false, 0, false, false, data[i].upVotes, data[i].imOnItVotes);
                                cards[i] = c;
                                cardArrayAdapter.add(c);
                            }

                            DatabaseHandler db = new DatabaseHandler(MyPitch.this);
                            db.refreshTable(table, cards);

                            if (listView != null)
                                listView.setAdapter(cardArrayAdapter);
                        } else {
                            System.err.println("Error: 'data' is null(" + inputMessage.what + ")");
                        }
                        break;
                }
            }
        };

        Database.PostRefreshUpVotes();
        Database.PostRefreshOnItVotes();
        Database.PostRefreshSpamVotes();

        Database.PostRefreshNews();
        Database.PostRefreshTrending();
        Database.PostRefreshHot();

        mTitle = getTitle();
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


        TextView tv = new TextView(context);
        tv.setText("popular");
        tv.setTextColor(getResources().getColor((R.color.greyText)));
        tv.setLayoutParams(new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, 1f));
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20);

        TextView tv1 = new TextView(context);
        tv1.setText("growing");
        tv1.setTextColor(getResources().getColor((R.color.greyText)));
        tv1.setLayoutParams(new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, 1f));
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextSize(20);

        TextView tv2 = new TextView(context);
        tv2.setText("new");
        tv2.setTextColor(getResources().getColor((R.color.greyText)));
        tv2.setLayoutParams(new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, 1f));
        tv2.setGravity(Gravity.CENTER);
        tv2.setTextSize(20);

        ImageView tv3 = new ImageView(context);
        //tv3.setText("");
        //tv3.setTextColor(getResources().getColor((R.color.greyText)));
        //tv3.setLayoutParams(new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, 1f));
        //tv3.setGravity(Gravity.CENTER);
        //tv3.setTextSize(20);
        tv3.setImageDrawable(getResources().getDrawable(R.drawable.ic_person_white));
        tv3.setLayoutParams(new TableLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, 1f));

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
        actionBar.addTab(
                actionBar.newTab()
                        .setTabListener(this)
                        .setCustomView(tv3)
        );

        actionBar.setSelectedNavigationItem(2);
    }
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if(tab.getPosition()!=3) {
            TextView tv = (TextView) tab.getCustomView();
            tv.setTextColor(getResources().getColor((R.color.greyText)));
        }
    }
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
        if(tab.getPosition()!=3) {
            TextView tv = (TextView) tab.getCustomView();
            tv.setTextColor(Color.WHITE);
        }

    }
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public static void HandleResponse(Response response)
    {
        MyPitchResponseHandler.HandleResponse(mHandler, response);
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

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_pitch, menu);

        mRefreshItem = menu.findItem(R.id.action_refresh);
        if(refreshing){
            mRefreshItem.setActionView(mRefresh);
            mRefresh.startAnimation(mRotate);
        }

        restoreActionBar();
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {

            if(canRefresh) {
                mRefreshItem.setActionView(mRefresh);
                mRefresh.startAnimation(mRotate);

                Database.PostRefreshUpVotes();
                Database.PostRefreshOnItVotes();
                Database.PostRefreshSpamVotes();

                Database.PostRefreshNews();
                Database.PostRefreshTrending();
                Database.PostRefreshHot();

                refreshing = true;
                canRefresh = false;

                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        refreshing = false;
                        canRefresh = true;
                    }
                }, REFRESH_DELTA_LENGTH);
            }

            return true;
        }
        if(id== R.id.action_tutorial)
        {
            Intent mainIntent = new Intent(MyPitch.this, TutorialSwipe.class);
            MyPitch.this.startActivity(mainIntent);
            MyPitch.this.finish();
        }
        if(id== R.id.action_add_post)
        {
            Intent mainIntent = new Intent(MyPitch.this, MyPost.class);
            MyPitch.this.startActivity(mainIntent);
        }
        if(id== R.id.action_log_out)
        {
            Login.LogOut(getApplicationContext());
            Intent mainIntent = new Intent(MyPitch.this, Login.class);
            MyPitch.this.startActivity(mainIntent);
            MyPitch.this.finish();
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
                case 3:
                    return new AcountFragment();
                default:
                    return new ErrorFragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
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
                    Card c = new Card(data[i].id, data[i].title, data[i].publisherName, data[i].text, false, 0, false, false, data[i].upVotes, data[i].imOnItVotes);
                    cardArrayAdapter.add(c);
                }
                listView.setAdapter(cardArrayAdapter);
            } else {
                System.err.println("Error: 'data' is null");

                DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());

                Card[] cardsData = db.getAllPosts(DatabaseHandler.Table.NEW);

                for (int i = cardsData.length-1; i >= 0; i--) {
                    cardArrayAdapter.add(cardsData[i]);
                }

                listView.setAdapter(cardArrayAdapter);
            }

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
                    Card c = new Card(data[i].id, data[i].title, data[i].publisherName, data[i].text, false, 0, false, false, data[i].upVotes, data[i].imOnItVotes);
                    cardArrayAdapter.add(c);
                }
                listView.setAdapter(cardArrayAdapter);
            } else {
                System.err.println("Error: 'data' is null");

                DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());

                Card[] cardsData = db.getAllPosts(DatabaseHandler.Table.GROWING);

                for (int i = cardsData.length-1; i >= 0; i--) {
                    cardArrayAdapter.add(cardsData[i]);
                }

                listView.setAdapter(cardArrayAdapter);
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
                    Card c = new Card(data[i].id, data[i].title, data[i].publisherName, data[i].text, false, 0, false, false, data[i].upVotes, data[i].imOnItVotes);
                    cardArrayAdapter.add(c);
                }
                listView.setAdapter(cardArrayAdapter);
            } else {
                System.err.println("Error: 'data' is null");

                DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());

                Card[] cardsData = db.getAllPosts(DatabaseHandler.Table.POPULAR);

                for (int i = cardsData.length-1; i >= 0; i--) {
                    cardArrayAdapter.add(cardsData[i]);
                }

                listView.setAdapter(cardArrayAdapter);
            }

            listView.setAdapter(cardArrayAdapter);
            return rootView;
        }
    }
    public static class AcountFragment extends android.support.v4.app.Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_account, container, false);
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
