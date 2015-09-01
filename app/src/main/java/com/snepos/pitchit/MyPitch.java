package com.snepos.pitchit;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.snepos.pitchit.database.Database;
import com.snepos.pitchit.database.IdeaData;
import com.snepos.pitchit.database.Response;
import com.snepos.pitchit.sqliteHelpers.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

//import android.support.v7.widget.Toolbar;


public class MyPitch extends ActionBarActivity /*implements ActionBar.TabListener */  {
    private static Handler mHandler;
    private static boolean canRefresh = true;
    private final int REFRESH_DELTA_LENGTH = 3000;
    private boolean isTutorial=false;

    //AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    // ViewPager mViewPager;

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"popular","growing","new", "profile"};
    int Numboftabs =4;
    PitchSwipeRefreshLayout mSwipeRefreshLayout;

    Context context;
    Button FAB;


    private CharSequence mTitle;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pitch);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
         isTutorial = bundle.getBoolean("tutorial", false);


        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setLogo(R.drawable.icon);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);

        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mSwipeRefreshLayout.setEnabled(state == ViewPager.SCROLL_STATE_IDLE);
            }
        });


        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.ActionBarTitle);
            }
        });


        tabs.setViewPager(pager);
        // Animation test:

        canRefresh = true;

        mSwipeRefreshLayout = (PitchSwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        RefreshData();
                    }
                });

        // End of test
        mSwipeRefreshLayout.setFragment(pager);

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                mSwipeRefreshLayout.setRefreshing(false);
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
                            CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(MyPitch.this.getApplicationContext(), R.layout.pitch_item,MyPitch.this);

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
                    case 4: // Refresh account's on it list
                        JSONArray jsonArray = (JSONArray)inputMessage.obj;
                        ExpandableIdea[] expandableIdeas = new ExpandableIdea[jsonArray.length()];
                        for(int i = 0; i < expandableIdeas.length; i++)
                        {
                            try
                            {
                                expandableIdeas[i] = new ExpandableIdea((JSONObject) jsonArray.get(i));
                            }
                            catch(Exception e)
                            {
                                System.out.println(e.getMessage());
                            }
                        }
                        AccountFragment.mInstance.mAdapter.refreshDataset(expandableIdeas);
                        AccountFragment.mInstance.mAdapter.notifyDataSetChanged();
                        break;
                }
            }
        };

        Database.PostRefreshProfile();

        Database.PostRefreshUpVotes();
        Database.PostRefreshOnItVotes();
        Database.PostRefreshSpamVotes();

        Database.PostRefreshNews();
        Database.PostRefreshTrending();
        Database.PostRefreshHot();

        mTitle = getTitle();
        context= this;

        FAB = (Button) findViewById(R.id.button_add);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyPost.class);
                startActivity(intent);

            }
        });


    }


    public static void HandleResponse(Response response)
    {
        MyPitchResponseHandler.HandleResponse(mHandler, response);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_pitch, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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

    void RefreshData()
    {
        if(canRefresh) {
            Database.PostRefreshProfile();

            Database.PostRefreshUpVotes();
            Database.PostRefreshOnItVotes();
            Database.PostRefreshSpamVotes();

            Database.PostRefreshNews();
            Database.PostRefreshTrending();
            Database.PostRefreshHot();

            canRefresh = false;

            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                    canRefresh = true;
                }
            }, REFRESH_DELTA_LENGTH);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    /*
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
        private GeneralPitchFragment[] pitchFragments;

        public AppSectionsPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
            pitchFragments = new GeneralPitchFragment[4];
        }

        public GeneralPitchFragment getPitchFragment(int i)
        {
            return pitchFragments[i];
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            switch (i) {
                case 0:
                    pitchFragments[i] = new HotFragment();
                    return pitchFragments[i];
                case 1:
                    pitchFragments[i] = new TrendingFragment();
                    return pitchFragments[i];
                case 2:
                    pitchFragments[i] = new NewFragment();
                    return pitchFragments[i];
                case 3:
                    pitchFragments[i] = new AccountFragment();
                    return pitchFragments[i];
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
            switch (position)
            {
                case 0:
                    return " popular";
                case 1:
                    return " growing";
                case 2:
                    return " new";
                case 3:
                    return  " profile";
            }
            return "Section " + (position + 1);
        }
    }
*/
    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
        int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
        private GeneralPitchFragment[] pitchFragments;

        // Build a Constructor and assign the passed Values to appropriate values in the class
        public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
            super(fm);
            pitchFragments = new GeneralPitchFragment[4];
            this.Titles = mTitles;
            this.NumbOfTabs = mNumbOfTabsumb;

        }

        //This method return the fragment for the every position in the View Pager
        @Override
        public android.support.v4.app.Fragment getItem(int position) {

            switch (position)
            {
                case 0:
                    pitchFragments[position] = new HotFragment();
                    return pitchFragments[position];
                case 1:
                    pitchFragments[position] = new TrendingFragment();
                    return pitchFragments[position];
                case 2:
                    pitchFragments[position] = new NewFragment();
                    return pitchFragments[position];
                case 3:
                    pitchFragments[position] = new AccountFragment();
                    return pitchFragments[position];
                default:
                    return new ErrorFragment();
            }
            /*
            if(position == 0) // if the position is 0 we are returning the First tab
            {
                Tab1 tab1 = new Tab1();
                return tab1;
            }
            else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
            {
                Tab2 tab2 = new Tab2();
                return tab2;
            }
            */

        }
        public GeneralPitchFragment getPitchFragment(int i)
        {
            return pitchFragments[i];
        }

        // This method return the titles for the Tabs in the Tab Strip

        @Override
        public CharSequence getPageTitle(int position) {
            return Titles[position];
        }

        // This method return the Number of tabs for the tabs Strip

        @Override
        public int getCount() {
            return NumbOfTabs;
        }
    }
    public static class GeneralPitchFragment extends android.support.v4.app.Fragment
    {
        protected ListView listView;

        public boolean canScrollUp()
        {
            if(listView != null && listView.getChildCount() != 0) {
                return listView.getFirstVisiblePosition() != 0 || listView.getChildAt(0).getTop() < 0;
            }
            else return false;
        }

    }

    public static class NewFragment extends GeneralPitchFragment {

        CardArrayAdapter cardArrayAdapter;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_new, container, false);
            listView = (ListView) rootView.findViewById(R.id.card_listView_new);

            listView.addHeaderView(new View(rootView.getContext()));
            listView.addFooterView(new View(rootView.getContext()));

            cardArrayAdapter = new CardArrayAdapter(getActivity().getApplicationContext(), R.layout.pitch_item, getActivity());
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
    public static class TrendingFragment extends GeneralPitchFragment {

        CardArrayAdapter cardArrayAdapter;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_trending, container, false);
            listView = (ListView) rootView.findViewById(R.id.card_listView_trending);

            listView.addHeaderView(new View(rootView.getContext()));
            listView.addFooterView(new View(rootView.getContext()));

            cardArrayAdapter = new CardArrayAdapter(getActivity().getApplicationContext(), R.layout.pitch_item,getActivity());
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
    public static class HotFragment extends GeneralPitchFragment {

        CardArrayAdapter cardArrayAdapter;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_hot, container, false);
            listView = (ListView) rootView.findViewById(R.id.card_listView_hot);

            listView.addHeaderView(new View(rootView.getContext()));
            listView.addFooterView(new View(rootView.getContext()));

            cardArrayAdapter = new CardArrayAdapter(getActivity().getApplicationContext(), R.layout.pitch_item,getActivity());
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
    public static class AccountFragment extends GeneralPitchFragment {
        static AccountFragment mInstance;

        ScrollView mScrollView;

        RecyclerView mRecyclerView;
        ExpandableIdeaLayoutManager mLayoutManager;
        ExpandableIdeaAdapter mAdapter;

        public AccountFragment()
        {
            mInstance = this;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_account, container, false);
            mScrollView = (ScrollView)rootView.findViewById(R.id.account_scroll_view);

            ImageView accountColor = (ImageView) rootView.findViewById(R.id.acount_color);
            TextView accountText = (TextView) rootView.findViewById(R.id.acount_text);
            String Publisher = Login.GetUserNickname().toString();
            if(Publisher!=null)
                accountText.setText(Publisher.toString());
            int sum = 5000;
            for (int i=0; i< Publisher.length(); i++)
            {
                sum+= Publisher.charAt(i);
            }

            //cardTop
            sum *= sum;
            Integer temp = (CardArrayAdapter.matColors.get(sum % CardArrayAdapter.matColors.size()));
            accountColor.setBackgroundColor(temp);

            mRecyclerView = (RecyclerView)rootView.findViewById(R.id.account_on_it_list);

            mLayoutManager = new ExpandableIdeaLayoutManager(getActivity().getApplicationContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new ExpandableIdeaAdapter(new ExpandableIdea[]{new ExpandableIdea("Temp")});
            mRecyclerView.setAdapter(mAdapter);

            return rootView;
        }

        @Override
        public boolean canScrollUp()
        {
            return mScrollView.getScrollY() > 0;
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
