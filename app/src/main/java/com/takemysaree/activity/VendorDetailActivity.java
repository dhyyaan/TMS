package com.takemysaree.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.squareup.picasso.Picasso;
import com.takemysaree.R;
import com.takemysaree.adapter.RecyclerAdapterCollection;
import com.takemysaree.adapter.SlideAdapter;
import com.takemysaree.fonts.LatoBoldTextview;
import com.takemysaree.fonts.LatoRegularTextview;
import com.takemysaree.models.CircleImageview;
import com.takemysaree.models.MyApplication;
import com.takemysaree.tabfragments.BuyFragment;
import com.takemysaree.tabfragments.CategoriesFragment;
import com.takemysaree.tabfragments.HomeFragment;
import com.takemysaree.tabfragments.RentFragment;
import com.takemysaree.tabfragments.VendorFragment;
import com.takemysaree.utils.BaseUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by think360user on 7/7/17.
 */

public class VendorDetailActivity extends FragmentActivity {
    private HomeFragment homeFragment;
    private BuyFragment buyFragment;
    private CategoriesFragment categoriesFragment;
    private RentFragment rentFragment;
    private VendorFragment vendorFragment;
    private TabLayout mTabLayout;

    private DrawerLayout drawerLayout;
    private String[] title;

    private Toolbar toolbar;
    private AppCompatRatingBar ratingBar;
    private ImageView ivHamburger;
    private LayoutInflater inflater;
    List<String> images = new ArrayList<>();
    private View view;
    private long mLastClickTime = 0;
    private SharedPreferences prefs;
    private TextView tvUserName;
    private ListView lvNavigationView;
    private LatoBoldTextview tvtitle;
    private CircleImageview ivProfile;
    private RecyclerView mRecyclerView;
    private RecyclerAdapterCollection homeGridAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LatoRegularTextview tvProfileName;
    private LatoRegularTextview tvFollowings, tvFollowers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activitydetail);
        initViews();
        initListeners();
    }

    private void initListeners() {

    }

    private void initViews() {
        ratingBar = (AppCompatRatingBar) findViewById(R.id.ratingBar);
        ivHamburger = (ImageView) findViewById(R.id.ivHamburger);
        tvtitle = (LatoBoldTextview) findViewById(R.id.tvtitle);
        tvProfileName = (LatoRegularTextview) findViewById(R.id.tvProfileName);
        tvFollowings = (LatoRegularTextview) findViewById(R.id.tvFollowings);
        tvFollowers = (LatoRegularTextview) findViewById(R.id.tvFollowers);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mLayoutManager = new GridLayoutManager(VendorDetailActivity.this, 3);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ivHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivProfile = (CircleImageview) findViewById(R.id.ivProfile);
        VendorDetail vendorDetail=new VendorDetail();
        vendorDetail.addQueue();


    }

    public class VendorDetail {
        StringRequest sr;
        public VendorDetail() {

            final ProgressDialog pDialog = new ProgressDialog(VendorDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.GET, BaseUrl.BASE_URL +
                    BaseUrl.vandor_detail + "&user_id="+getIntent().getStringExtra("id"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {

                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            JSONObject jsonarray = jsonObject1.getJSONObject("vendor");
                            tvProfileName.setText(jsonarray.getString("name"));
                            tvtitle.setText(jsonarray.getString("name"));
                            tvFollowings.setText(jsonarray.getString("followed_count"));
                            tvFollowers.setText(jsonarray.getString("followeer_count"));
                            ratingBar.setRating(Float.parseFloat(jsonarray.getString("rating")));
                            if (jsonarray.getString("image").equals("")) {
                                Picasso.with(VendorDetailActivity.this)
                                        .load(R.drawable.emptyimage1)
                                        .placeholder(R.drawable.emptyimage1)
                                        .error(R.drawable.emptyimage1)
                                        .into(ivProfile);
                            } else {
                                Picasso.with(VendorDetailActivity.this)
                                        .load(jsonarray.getString("image"))
                                        .placeholder(R.drawable.emptyimage1)
                                        .error(R.drawable.emptyimage1)
                                        .into(ivProfile);
                            }
                            JSONArray jsonArray = jsonarray.getJSONArray("product");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject2=jsonArray.getJSONObject(i);
                                images.add(jsonObject2.getString("image"));
                            }
                            homeGridAdapter = new RecyclerAdapterCollection(VendorDetailActivity.this,images);

                            // Set the adapter for RecyclerView
                            mRecyclerView.setAdapter(homeGridAdapter);

                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(VendorDetailActivity.this);
                            builder1.setMessage(message);
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


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    if (error instanceof TimeoutError) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(VendorDetailActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(VendorDetailActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(VendorDetailActivity.this);
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


}

