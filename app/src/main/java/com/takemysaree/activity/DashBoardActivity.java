package com.takemysaree.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.squareup.picasso.Picasso;
import com.takemysaree.R;
import com.takemysaree.models.CircleImageview;
import com.takemysaree.models.MyApplication;
import com.takemysaree.tabfragments.BuyFragment;
import com.takemysaree.tabfragments.HomeFragment;
import com.takemysaree.tabfragments.VendorFragment;
import com.takemysaree.tabfragments.RentFragment;
import com.takemysaree.tabfragments.CategoriesFragment;
import com.takemysaree.utils.BaseUrl;
import com.vistrav.ask.Ask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by think360user on 7/7/17.
 */

public class DashBoardActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener {
    private HomeFragment homeFragment;
    private BuyFragment buyFragment;
    private CategoriesFragment categoriesFragment;
    private RentFragment rentFragment;
    private VendorFragment vendorFragment;
    private TabLayout mTabLayout;
    private static final int PERMISSION_REQUEST_CAMERA = 0;
private TextView cartvalue;
    private DrawerLayout RootLayout;

    private TextView tvtitle;
    //private DrawerLayout drawerLayout;
    private String[] title;
    private ImageView search;
    private Toolbar toolbar;
    private ImageView ivHamburger;
    private LayoutInflater inflater;
    private Boolean exit = false;
    private View view;
    private int Position_X;
    private int Position_Y;

    private long mLastClickTime = 0;
    private SharedPreferences prefs;
    private TextView tvUserName;
    ArrayList<Uri> image_uris = new ArrayList<Uri>();
    private ListView lvNavigationView;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private CircleImageview ivProfile;
    private LinearLayout sellnow,lvtch;
    private SharedPreferences sharedpreferences;
    private String userid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        setContentView(R.layout.dashboard_activity);
        sharedpreferences = getApplicationContext().getSharedPreferences(BaseUrl.prfs, Context.MODE_PRIVATE);
        userid = sharedpreferences.getString("userid", "");

        //    prefs = getSharedPreferences(BaseUrl.prfs, Context.MODE_PRIVATE);
        initViews();
        setupTabLayout();
        initListeners();
        CartList cartList=new CartList();
        cartList.addQueue();
        Ask.on(this)
                .forPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withRationales("Location permission need for map to work properly",
                        "In order to save file you will need to grant storage permission") //optional
                .go();

    }

    private void initListeners() {
        ivProfile.setOnClickListener(this);
        search.setOnClickListener(this);
        sellnow.setOnClickListener(this);
        lvtch.setOnClickListener(this);
    }
    public class CartList {

        StringRequest sr;


        public CartList() {



            sr = new StringRequest(Request.Method.GET, BaseUrl.BASE_URL + BaseUrl.list_cart
                    + "&user_id=" + userid, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            JSONArray jsonarray = jsonObject1.getJSONArray("cart");
                            cartvalue.setText(String.valueOf(jsonarray.length()));

                        } else {
                            cartvalue.setText(String.valueOf(0));

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (error instanceof TimeoutError) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(DashBoardActivity.this);
                        builder1.setMessage("TimeOut Error. Please try again.");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else if (error instanceof NoConnectionError) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(DashBoardActivity.this);
                        builder1.setMessage("No Internet Connection.Please try again");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(DashBoardActivity.this);
                        builder1.setMessage("Server Error.Please try again");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();


                    return params;
                }

            };
        }

        public void addQueue() {
            MyApplication.getInstance().addToRequestQueue(sr);

            sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }


    }
    private void initViews() {
        //ivHamburger = (ImageView) findViewById(R.id.ivHamburger);
      //  RootLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        search = (ImageView) findViewById(R.id.search);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        sellnow = (LinearLayout) findViewById(R.id.sellnow);
        lvtch = (LinearLayout) findViewById(R.id.lvtch);
        tvtitle = (TextView) findViewById(R.id.tvtitle);
        cartvalue = (TextView) findViewById(R.id.cartvalue);
        inflater = getLayoutInflater();
        ivProfile = (CircleImageview) findViewById(R.id.ivProfile);
    /*   CartList cartList=new CartList();
        cartList.addQueue(); */

    }

    private void setupTabLayout() {
        homeFragment = new HomeFragment();
        categoriesFragment = new CategoriesFragment();
        rentFragment = new RentFragment();
        vendorFragment = new VendorFragment();
        buyFragment = new BuyFragment();
        final TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Home");
        tabOne.setTextSize(8);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home, 0, 0);
        final TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Vendor");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.vendor, 0, 0);
        final TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Categories");
        tabThree.setTextSize(8);
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.categories, 0, 0);
        final TextView tabfour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabfour.setText("Rent");
        tabfour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.rent, 0, 0);
        final TextView tabfive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabfive.setText("Buy");
        tabfive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.buy, 0, 0);


        mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabOne), true);
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabTwo));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabThree));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabfour));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabfive));

        setCurrentTabFragment(0);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home_hover, 0, 0);
        tabOne.setTextColor(Color.parseColor("#33b7a9"));


        mTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#5ec3b5"));


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (mTabLayout.getSelectedTabPosition() == 0) {
                    tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home_hover, 0, 0);
                    tabOne.setTextColor(Color.parseColor("#33b7a9"));

                } else if (mTabLayout.getSelectedTabPosition() == 1) {
                    tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.vendor_hover, 0, 0);
                    tabTwo.setTextColor(Color.parseColor("#33b7a9"));


                } else if (mTabLayout.getSelectedTabPosition() == 2) {
                    tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.categories_hover, 0, 0);
                    tabThree.setTextColor(Color.parseColor("#33b7a9"));


                } else if (mTabLayout.getSelectedTabPosition() == 3) {
                    tabfour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.rent_hover, 0, 0);
                    tabfour.setTextColor(Color.parseColor("#33b7a9"));


                } else if (mTabLayout.getSelectedTabPosition() == 4) {
                    tabfive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.buy_hover, 0, 0);
                    tabfive.setTextColor(Color.parseColor("#33b7a9"));


                }
                setCurrentTabFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home, 0, 0);
                tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.vendor, 0, 0);
                tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.categories, 0, 0);
                tabfour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.rent, 0, 0);
                tabfive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.buy, 0, 0);
                tabOne.setTextColor(Color.parseColor("#A8A8A8"));
                tabTwo.setTextColor(Color.parseColor("#A8A8A8"));
                tabThree.setTextColor(Color.parseColor("#A8A8A8"));
                tabfour.setTextColor(Color.parseColor("#A8A8A8"));
                tabfive.setTextColor(Color.parseColor("#A8A8A8"));



            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (mTabLayout.getSelectedTabPosition() == 0) {
                    tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home_hover, 0, 0);
                    tabOne.setTextColor(Color.parseColor("#33b7a9"));
                } else if (mTabLayout.getSelectedTabPosition() == 1) {
                    tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.vendor_hover, 0, 0);
                    tabTwo.setTextColor(Color.parseColor("#33b7a9"));
                } else if (mTabLayout.getSelectedTabPosition() == 2) {
                    tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.categories_hover, 0, 0);
                    tabThree.setTextColor(Color.parseColor("#33b7a9"));
                } else if (mTabLayout.getSelectedTabPosition() == 3) {
                    tabfour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.rent_hover, 0, 0);
                    tabfour.setTextColor(Color.parseColor("#33b7a9"));
                } else if (mTabLayout.getSelectedTabPosition() == 4) {
                    tabfive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.buy_hover, 0, 0);
                    tabfive.setTextColor(Color.parseColor("#33b7a9"));
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
               Intent intents=new Intent(DashBoardActivity.this,SelectPrduct.class);
               startActivity(intents);
               finish();


                break;
            case R.id.search:
                Intent intent1 = new Intent(DashBoardActivity.this, SearchfinalActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.ivProfile:
                Intent intent = new Intent(DashBoardActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.lvtch:
                Intent intentsd = new Intent(DashBoardActivity.this, MyCart.class);
                startActivity(intentsd);
                finish();
                break;
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        final int X = (int) motionEvent.getRawX();
        final int Y = (int) motionEvent.getRawY();

        int pointerCount = motionEvent.getPointerCount();
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE:

                if (pointerCount == 1) {
                    RelativeLayout.LayoutParams Params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    Params.leftMargin = X - Position_X;
                    Params.topMargin = Y - Position_Y;
                    Params.rightMargin = -500;
                    Params.bottomMargin = -500;
                    view.setLayoutParams(Params);
                }


                break;
        }
        RootLayout.invalidate();
        return true;
    }
}
