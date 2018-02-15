package com.takemysaree.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.takemysaree.R;
import com.takemysaree.adapter.HomeGridAdapter;
import com.takemysaree.adapter.RecyclerAdapterCollection;
import com.takemysaree.adapter.RecyclerAdapterCollection1;
import com.takemysaree.adapter.SlideAdapter;
import com.takemysaree.models.CircleImageview;
import com.takemysaree.tabfragments.BuyFragment;
import com.takemysaree.tabfragments.CategoriesFragment;
import com.takemysaree.tabfragments.HomeFragment;
import com.takemysaree.tabfragments.RentFragment;
import com.takemysaree.tabfragments.VendorFragment;
import com.takemysaree.utils.BaseUrl;


/**
 * Created by think360user on 7/7/17.
 */

public class ProfileActivity extends FragmentActivity implements View.OnClickListener {
    private HomeFragment homeFragment;
    private BuyFragment buyFragment;
    private CategoriesFragment categoriesFragment;
    private RentFragment rentFragment;
    private VendorFragment vendorFragment;
    private TabLayout mTabLayout;

    private DrawerLayout drawerLayout;
    private String[] title;

    private Toolbar toolbar;
    private ImageView ivHamburger;
    private LayoutInflater inflater;
    private View view;
    private long mLastClickTime = 0;
    private SharedPreferences prefs;
    private TextView tvUserName;
    private LinearLayout ll5;
    private ListView lvNavigationView;
    private CircleImageview ivProfile;
    private RecyclerView mRecyclerView;
    private RecyclerAdapterCollection1 homeGridAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        prefs = getApplicationContext().getSharedPreferences(BaseUrl.prfs, Context.MODE_PRIVATE);
        initViews();
        setupTabLayout();
        initListeners();
    }

    private void initListeners() {
        ivHamburger.setOnClickListener(this);
        ll5.setOnClickListener(this);
    }

    private void initViews() {
        ivHamburger = (ImageView) findViewById(R.id.ivHamburger);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        ll5=(LinearLayout)findViewById(R.id.ll5);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mLayoutManager = new GridLayoutManager(ProfileActivity.this,3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Initialize a new instance of RecyclerView Adapter instance
        homeGridAdapter = new RecyclerAdapterCollection1(ProfileActivity.this);

        // Set the adapter for RecyclerView
        mRecyclerView.setAdapter(homeGridAdapter);
        inflater = getLayoutInflater();
        lvNavigationView = (ListView) findViewById(R.id.lvNavigationView);
        view = inflater.inflate(R.layout.navigation_headerview, lvNavigationView, false);
        lvNavigationView.addHeaderView(view);
        ivProfile = (CircleImageview) view.findViewById(R.id.ivProfile);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = getResources().getStringArray(R.array.titles);
        lvNavigationView.setAdapter(new SlideAdapter(this, drawerLayout, title));
        initNavigationDrawer();

    }

    public void initNavigationDrawer() {


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        lvNavigationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }

                        mLastClickTime = SystemClock.elapsedRealtime();


                        drawerLayout.closeDrawers();
                        break;
                    case 1:
                        Intent intent = new Intent(ProfileActivity.this, MyOrderActivity.class);
                        startActivity(intent);

                        drawerLayout.closeDrawers();
                        break;
                    case 2:
                        Intent intents = new Intent(ProfileActivity.this, SettingsActivity.class);
                        startActivity(intents);

                        drawerLayout.closeDrawers();
                        break;
                    case 3:
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }

                        mLastClickTime = SystemClock.elapsedRealtime();

                        drawerLayout.closeDrawers();
                        break;
                    case 4:
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {


                            return;
                        }

                        mLastClickTime = SystemClock.elapsedRealtime();
                        Intent intent4 = new Intent(ProfileActivity.this, ContactsActivity.class);
                        startActivity(intent4);

                        drawerLayout.closeDrawers();
                        break;
                    case 5:
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {


                            return;
                        }

                        mLastClickTime = SystemClock.elapsedRealtime();
                        Intent intent5 = new Intent(ProfileActivity.this, MyOffersActivity.class);
                        startActivity(intent5);

                        drawerLayout.closeDrawers();
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }

                        mLastClickTime = SystemClock.elapsedRealtime();
                        prefs.edit().clear().apply();
                        Intent intent1=new Intent(ProfileActivity.this,LoginActivityforsocials.class);
                        startActivity(intent1);
                        finish();
                        drawerLayout.closeDrawers();
                        break;
                }


            }
        });
    }

    private void setupTabLayout() {
        homeFragment = new HomeFragment();
        vendorFragment = new VendorFragment();
        categoriesFragment = new CategoriesFragment();
        rentFragment = new RentFragment();
        buyFragment = new BuyFragment();

        final TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("HOME");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home, 0, 0);
        final TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("VENDOR");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.vendor, 0, 0);
        final TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("CATEGORIES");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.categories, 0, 0);
        final TextView tabfour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabfour.setText("RENT");
        tabfour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.rent, 0, 0);
        final TextView tabfive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabfive.setText("BUY");
        tabfive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.buy, 0, 0);

        mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabOne), true);
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabTwo));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabThree));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabfour));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabfive));


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (mTabLayout.getSelectedTabPosition() == 0) {

                } else if (mTabLayout.getSelectedTabPosition() == 1) {

                } else if (mTabLayout.getSelectedTabPosition() == 2) {

                } else if (mTabLayout.getSelectedTabPosition() == 3) {

                }
                setCurrentTabFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (mTabLayout.getSelectedTabPosition() == 0) {

                } else if (mTabLayout.getSelectedTabPosition() == 1) {

                } else if (mTabLayout.getSelectedTabPosition() == 2) {

                } else if (mTabLayout.getSelectedTabPosition() == 3) {

                }
                setCurrentTabFragment(tab.getPosition());

            }
        });


    }

    private void setCurrentTabFragment(int tabPosition) {
        switch (tabPosition) {
            case 0:
                //replaceFragment(homeFragment);
                break;
            case 1:
                // replaceFragment(scheduleFragment);
                break;
            case 2:
                // replaceFragment(sosFragment);
                break;
            case 3:
                //replaceFragment(profileFragment);

                //replaceFragment(walletfragment);
                break;

        }
    }

    public void replaceFragment(Fragment fragment) {
        boolean removeStack = true;

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (removeStack) {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.replace(R.id.frame_container, fragment);
        } else {
            ft.replace(R.id.frame_container, fragment);
            ft.addToBackStack(null);
        }


        ft.commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll5:
                Intent intent=new Intent(ProfileActivity.this,WishlistActivity.class);
                startActivity(intent);
                break;
            case R.id.ivHamburger:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
        }
    }
}
