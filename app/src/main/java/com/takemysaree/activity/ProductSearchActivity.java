package com.takemysaree.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.takemysaree.R;
import com.takemysaree.models.CircleImageview;
import com.takemysaree.tabfragments.BuyFragment;
import com.takemysaree.tabfragments.CategoriesFragment;
import com.takemysaree.tabfragments.HomeFragment;
import com.takemysaree.tabfragments.ProductFragment;
import com.takemysaree.tabfragments.ProductsearchFragment;
import com.takemysaree.tabfragments.RentFragment;
import com.takemysaree.tabfragments.VendorFragment;

import java.util.ArrayList;


/**
 * Created by think360user on 7/7/17.
 */

public class ProductSearchActivity extends AppCompatActivity implements View.OnClickListener {
    private HomeFragment homeFragment;
    private BuyFragment buyFragment;
    private CategoriesFragment categoriesFragment;
    private RentFragment rentFragment;
    private VendorFragment vendorFragment;
    private TabLayout mTabLayout;
    private TextView tvtitle;
    private DrawerLayout drawerLayout;
    private String[] title;
    private ImageView search;
    private Toolbar toolbar;
    private ImageView ivHamburger;
    private LayoutInflater inflater;
    private Boolean exit = false;
    private View view;
    private long mLastClickTime = 0;
    private SharedPreferences prefs;
    private ProductsearchFragment productFragment;
    private TextView tvUserName;
    ArrayList<Uri> image_uris = new ArrayList<Uri>();
    private ListView lvNavigationView;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private CircleImageview ivProfile;
    private LinearLayout sellnow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        setContentView(R.layout.dashboard_activity);

        //    prefs = getSharedPreferences(BaseUrl.prfs, Context.MODE_PRIVATE);
        initViews();
        setupTabLayout();
        initListeners();
    }

    private void initListeners() {
        ivProfile.setOnClickListener(this);
        search.setOnClickListener(this);
        sellnow.setOnClickListener(this);
    }

    private void initViews() {
        //ivHamburger = (ImageView) findViewById(R.id.ivHamburger);
        search = (ImageView) findViewById(R.id.search);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        sellnow = (LinearLayout) findViewById(R.id.sellnow);
        tvtitle = (TextView) findViewById(R.id.tvtitle);
        inflater = getLayoutInflater();
        ivProfile = (CircleImageview) findViewById(R.id.ivProfile);


    }

    private void setupTabLayout() {
        homeFragment = new HomeFragment();
        categoriesFragment = new CategoriesFragment();
        rentFragment = new RentFragment();
        vendorFragment = new VendorFragment();
        buyFragment = new BuyFragment();
        productFragment = new ProductsearchFragment();
        final ImageView tabOne = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setImageResource(R.drawable.home);


        final ImageView tabTwo = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setImageResource(R.drawable.vendor);


        final ImageView tabThree = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setImageResource(R.drawable.categories);


        final ImageView tabfour = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabfour.setImageResource(R.drawable.rent);


        final ImageView tabfive = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabfive.setImageResource(R.drawable.buy);

        mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabOne), true);
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabTwo));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabThree));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabfour));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabfive));
replaceFragment(productFragment);
        //setCurrentTabFragment(0);
        //tabOne.setImageResource(R.drawable.home_hover);


        mTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#5ec3b5"));


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (mTabLayout.getSelectedTabPosition() == 0) {
                    tabOne.setImageResource(R.drawable.home_hover);

                } else if (mTabLayout.getSelectedTabPosition() == 1) {
                    tabTwo.setImageResource(R.drawable.vendor_hover);

                } else if (mTabLayout.getSelectedTabPosition() == 2) {
                    tabThree.setImageResource(R.drawable.categories_hover);

                } else if (mTabLayout.getSelectedTabPosition() == 3) {
                    tabfour.setImageResource(R.drawable.rent_hover);

                } else if (mTabLayout.getSelectedTabPosition() == 4) {
                    tabfive.setImageResource(R.drawable.buy_hover);

                }
                setCurrentTabFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabOne.setImageResource(R.drawable.home);
                tabTwo.setImageResource(R.drawable.vendor);
                tabThree.setImageResource(R.drawable.categories);
                tabfour.setImageResource(R.drawable.rent);
                tabfive.setImageResource(R.drawable.buy);


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (mTabLayout.getSelectedTabPosition() == 0) {
                    tabOne.setImageResource(R.drawable.home_hover);
                } else if (mTabLayout.getSelectedTabPosition() == 1) {
                    tabTwo.setImageResource(R.drawable.vendor_hover);
                } else if (mTabLayout.getSelectedTabPosition() == 2) {
                    tabThree.setImageResource(R.drawable.categories_hover);
                } else if (mTabLayout.getSelectedTabPosition() == 3) {
                    tabfour.setImageResource(R.drawable.rent_hover);
                } else if (mTabLayout.getSelectedTabPosition() == 4) {
                    tabfive.setImageResource(R.drawable.buy_hover);
                }
                setCurrentTabFragment(tab.getPosition());

            }
        });


    }

    private void setCurrentTabFragment(int tabPosition) {
        switch (tabPosition) {
            case 0:
                replaceFragment(homeFragment);
                break;
            case 1:
                replaceFragment(vendorFragment);
                break;
            case 2:
                replaceFragment(categoriesFragment);
                break;
            case 3:
                replaceFragment(rentFragment);

                break;
            case 4:
                replaceFragment(buyFragment);
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
        // ft.replace(R.id.frame_container, fragment);
        //ft.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit);

        ft.commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sellnow:
                Config config = new Config();
                config.setToolbarTitleRes(R.string.app_name);
                config.setSelectionMin(1);
                config.setSelectionLimit(5);

                config.setFlashOn(true);


                getImages(config);


                break;
            case R.id.search:
                Intent intent1 = new Intent(ProductSearchActivity.this, SearchfinalActivity.class);
                startActivity(intent1);
                break;
            case R.id.ivProfile:
                Intent intent = new Intent(ProductSearchActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getImages(Config config) {


        ImagePickerActivity.setConfig(config);

        Intent intent = new Intent(this, ImagePickerActivity.class);

intent.putExtra("checkvalue","1");
        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_IMAGES) {

                image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

                if (image_uris != null) {
                    Intent intent1 = new Intent(ProductSearchActivity.this, AddProductActivity1.class);
                    intent1.putParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS, image_uris);
                    startActivity(intent1);
                }


            }
        }
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }
}
