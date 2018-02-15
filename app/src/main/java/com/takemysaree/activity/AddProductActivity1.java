/*
 * Copyright (c) 2016. Ted Park. All Rights Reserved
 */

package com.takemysaree.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.squareup.picasso.Picasso;
import com.takemysaree.R;
import com.takemysaree.fonts.LatoBoldTextview;
import com.takemysaree.fonts.LatoExtraBoldTextview;
import com.takemysaree.fonts.LatoNormalEditText;
import com.takemysaree.fonts.LatoRegularTextview;
import com.takemysaree.models.AutoScrollViewPager;
import com.takemysaree.models.GPSTracker;
import com.takemysaree.models.MyApplication;
import com.takemysaree.models.PlaceJSONParser;
import com.takemysaree.models.RealPathUtil;
import com.takemysaree.tabfragments.CategoriesFragment;
import com.takemysaree.tabfragments.ProductFragment;
import com.takemysaree.utils.BaseUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;

public class AddProductActivity1 extends AppCompatActivity implements View.OnClickListener {
    private AutoScrollViewPager view_pager;
    private ParserTasks parserTasks;
    private LatLng p1 = null;
    private PlacesTasks placesTasks;
    private String category_id;
    private static final String API_KEY = "AIzaSyBMXzFDv4QbMuwvKuW8GC9vW2z4aKrxW98";
    private AutoCompleteTextView dialog_searchLocation;
    private LatoNormalEditText chosecate, choselocation;
    ArrayList<Uri> image_uris = new ArrayList<Uri>();

    private ImageView crsq, dialogbackpress;
    private ListView listview;
    private GPSTracker gps;
    Dialog dialoglctn;
    private LinearLayout clrcode;
    private LatoRegularTextview sell, both, rent, btnyes, btnno;
    List<String> ids = new ArrayList<>();
    List<String> categories = new ArrayList<>();
    List<String> images = new ArrayList<>();
    private ImageView doctordetail_backpress;
    private LatoNormalEditText productname;
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    private LinearLayout user_wallet;
    private double latitude, longitude;
    private String pickupLat, pickupLng;
    private LinearLayout selectclor;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private LatoNormalEditText rentprice, sellprice, quantity,
            taxes, weight, size, description,retailprice,rentpr;
    private LinearLayout bothprice;
    private LatoNormalEditText clredit;

    private LatoBoldTextview postproduct;
    private CircleIndicator indicator_default;
    private Geocoder geocoder;
    String knownName, address, city, country, postalCode, state;
    private List<Address> addresses;
    private  String user_id,color_code,product_type,nagotiable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addproduct_detail);
        SharedPreferences prefs = getSharedPreferences(BaseUrl.prfs, Context.MODE_PRIVATE);
        user_id = prefs.getString("userid", "");
        initViews();
        initListeners();
        getCurrentLocation();
       // Toast.makeText(AddProductActivity1.this, "" + ImagePickerActivity.paths.size(), Toast.LENGTH_SHORT).show();
    }

    private void getCurrentLocation() {
        gps = new GPSTracker(this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            pickupLat = String.valueOf(latitude);
            pickupLng = String.valueOf(longitude);
            geocoder = new Geocoder(this, Locale.getDefault());
            try {

                addresses = geocoder.getFromLocation(latitude, longitude, 1);

                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                postalCode = addresses.get(0).getPostalCode();
                knownName = addresses.get(0).getFeatureName();
                choselocation.setText(address + ", " + city);


            } catch (IOException e) {
                e.printStackTrace();
            }

            // \n is for new line
            //    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


    }

    @SuppressLint("NewApi")
    private void initViews() {
        view_pager = (AutoScrollViewPager) findViewById(R.id.view_pager);
        user_wallet = (LinearLayout) findViewById(R.id.user_wallet);
        chosecate = (LatoNormalEditText) findViewById(R.id.chosecate);
        postproduct = (LatoBoldTextview) findViewById(R.id.postproduct);
        choselocation = (LatoNormalEditText) findViewById(R.id.choselocation);
        quantity = (LatoNormalEditText) findViewById(R.id.quantity);
        size = (LatoNormalEditText) findViewById(R.id.size);
        taxes = (LatoNormalEditText) findViewById(R.id.taxes);
        retailprice = (LatoNormalEditText) findViewById(R.id.retailprice);
        rentpr = (LatoNormalEditText) findViewById(R.id.rentpr);
        weight = (LatoNormalEditText) findViewById(R.id.weight);
        description = (LatoNormalEditText) findViewById(R.id.description);
        sellprice = (LatoNormalEditText) findViewById(R.id.sellprice);
        clredit = (LatoNormalEditText) findViewById(R.id.clredit);
        rentprice = (LatoNormalEditText) findViewById(R.id.rentprice);
        productname = (LatoNormalEditText) findViewById(R.id.productname);
        bothprice = (LinearLayout) findViewById(R.id.bothprice);
        selectclor = (LinearLayout) findViewById(R.id.selectclor);
        clrcode = (LinearLayout) findViewById(R.id.clrcode);
        sell = (LatoRegularTextview) findViewById(R.id.sell);
        btnyes = (LatoRegularTextview) findViewById(R.id.btnyes);
        btnno = (LatoRegularTextview) findViewById(R.id.btnno);
        rent = (LatoRegularTextview) findViewById(R.id.rent);
        both = (LatoRegularTextview) findViewById(R.id.both);
        rent.setTextColor(Color.parseColor("#ffffff"));
        rent.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
        sell.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
        sell.setTextColor(Color.parseColor("#919191"));
        both.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
        both.setTextColor(Color.parseColor("#919191"));
        rent.setPadding(50, 0, 50, 0);
        sell.setPadding(50, 0, 50, 0);
        both.setPadding(50, 0, 50, 0);
        product_type="1";
        btnyes.setTextColor(Color.parseColor("#ffffff"));
        btnyes.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
        btnno.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
        btnno.setTextColor(Color.parseColor("#919191"));
        btnyes.setPadding(50, 0, 50, 0);
        btnno.setPadding(50, 0, 50, 0);
        nagotiable="1";
        rentprice.setVisibility(View.VISIBLE);
        sellprice.setVisibility(View.GONE);
        bothprice.setVisibility(View.GONE);
        indicator_default = (CircleIndicator) findViewById(R.id.indicator_default);
        doctordetail_backpress = (ImageView) findViewById(R.id.doctordetail_backpress);
        image_uris = getIntent().getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
        user_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config config = new Config();
                config.setToolbarTitleRes(R.string.app_name);
                config.setSelectionMin(1);
                config.setSelectionLimit(5);

                config.setFlashOn(true);


                getImages(config);
            }
        });
        chosecate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AddProductActivity1.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialogcategories_listing);
                final Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                GridView rv = (GridView) dialog.findViewById(R.id.grid_view);
                ImageView ivclose = (ImageView) dialog.findViewById(R.id.ivclose);
                ivclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                Category category = new Category(rv, dialog);
                category.addQueue();

            }
        });
        doctordetail_backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(AddProductActivity1.this);
                builder1.setMessage("You will loose your all uploaded data. Are you sure you want to exit?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(AddProductActivity1.this, SelectPrductCity.class);
                                startActivity(intent);
                                finish();
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        for (int i = 0; i < image_uris.size(); i++) {

            RealPathUtil.getPath(getApplicationContext(), Uri.parse(("file://") + image_uris.get(i)));
            try {
                Bitmap mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(("file://") + image_uris.get(i)));
                bitmaps.add(mBitmap);
                // ivProfile.setImageBitmap(mBitmap);
                // new MultipartUpload(this, mBitmap, "image").addQueue();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        view_pager.setAdapter(new ViewPagerAdapter1(AddProductActivity1.this, bitmaps));
        view_pager.startAutoScroll(10000);
        indicator_default.setViewPager(view_pager);

    }

    private void getImages(Config config) {


        ImagePickerActivity.setConfig(config);

        Intent intent = new Intent(this, ImagePickerActivity.class);

        if (image_uris != null) {
            intent.putParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS, image_uris);
        }


        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);

    }

    private void initListeners() {
        sell.setOnClickListener(this);
        both.setOnClickListener(this);
        rent.setOnClickListener(this);
        btnno.setOnClickListener(this);
        btnyes.setOnClickListener(this);
        selectclor.setOnClickListener(this);
        clredit.setOnClickListener(this);
        choselocation.setOnClickListener(this);
        clrcode.setOnClickListener(this);
        postproduct.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_IMAGES) {

                image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

                if (image_uris != null) {
                    Intent intent1 = new Intent(AddProductActivity1.this, AddProductActivity1.class);
                    intent1.putParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS, image_uris);
                    startActivity(intent1);
                }


            }
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.postproduct:
                if(productname.getText().toString().equals("")){
                    productname.setError("Enter product name");
                }else if(category_id==null){
                    chosecate.setError("Please select category");
                }else if(quantity.getText().toString().equals("")){
                    quantity.setError("Enter quantity");
                }else if(weight.getText().toString().equals("")){
                    weight.setError("Enter weight");
                }else if(size.getText().toString().equals("")){
                    size.setError("Enter size");
                }else if(color_code==null){
                    clredit.setError("Select Color");
                }else if(description.getText().toString().equals("")){
                    description.setError("Enter description");
                }else if(product_type=="1"&&rentprice.getText().toString().equals("")){
                    rentprice.setError("Enter Price/Day");
                }else if(product_type=="2"&&sellprice.getText().toString().equals("")){
                    sellprice.setError("Enter Price");
                }else if(product_type=="3"&&retailprice.getText().toString().equals("")){
                    retailprice.setError("Enter Price");
                }else if(product_type=="3"&&rentpr.getText().toString().equals("")){
                    rentpr.setError("Enter Price/Day");
                }else if(choselocation.getText().toString().equals("")){
                    choselocation.setError("Select Location");
                }else{
                    AddProduct addProduct = new AddProduct();
                    addProduct.addQueue();
                }

                break;
            case R.id.clredit:
                selectclor.performClick();
                break;
            case R.id.selectclor:
                final Dialog dialogs = new Dialog(AddProductActivity1.this);
                dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogs.setContentView(R.layout.dialog_listviewlisting);
                final Window windows = dialogs.getWindow();
                windows.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                windows.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                windows.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                ListView listView = (ListView) dialogs.findViewById(R.id.listView);
                LatoBoldTextview titles=(LatoBoldTextview)dialogs.findViewById(R.id.titles);
                titles.setText("Select Color");
                ImageView ivHamburger=(ImageView)dialogs.findViewById(R.id.ivHamburger);
                ivHamburger.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogs.dismiss();
                    }
                });
                Productclor productclor = new Productclor(listView, dialogs);
                productclor.addQueue();
                break;
            case R.id.choselocation:


                dialoglctn = new Dialog(AddProductActivity1.this, R.style.Dialog_No_Border);
                dialoglctn.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialoglctn.setContentView(R.layout.dialog_search_layout);
                Window window = dialoglctn.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.CENTER;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                window.setAttributes(wlp);
                dialoglctn.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                dialog_searchLocation = (AutoCompleteTextView) dialoglctn.findViewById(R.id.dialog_searchLocation);
                listview = (ListView) dialoglctn.findViewById(R.id.listview);
                crsq = (ImageView) dialoglctn.findViewById(R.id.crsq);
                dialogbackpress = (ImageView) dialoglctn.findViewById(R.id.dialogbackpress);
                dialog_searchLocation.setText(choselocation.getText().toString());
                int textLength = dialog_searchLocation.getText().length();
                dialog_searchLocation.setSelection(textLength, textLength);
                crsq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_searchLocation.setText("");
                    }
                });
                dialogbackpress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialoglctn.dismiss();
                    }
                });
                dialog_searchLocation.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        int textLength = dialog_searchLocation.getText().length();
                        dialog_searchLocation.setSelection(textLength, textLength);
                        placesTasks = new PlacesTasks();
                        placesTasks.execute(s.toString());

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub
                    }
                });
                String locationaddress = dialog_searchLocation.getText().toString();
                if (!locationaddress.equals("")) {
                    placesTasks = new PlacesTasks();
                    placesTasks.execute(locationaddress);


                }


                dialoglctn.show();

                break;
            case R.id.rent:
                product_type="1";
                rent.setTextColor(Color.parseColor("#ffffff"));
                rent.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
                sell.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                sell.setTextColor(Color.parseColor("#919191"));
                both.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                both.setTextColor(Color.parseColor("#919191"));
                rent.setPadding(50, 0, 50, 0);
                sell.setPadding(50, 0, 50, 0);
                both.setPadding(50, 0, 50, 0);
                rentprice.setVisibility(View.VISIBLE);
                sellprice.setVisibility(View.GONE);
                bothprice.setVisibility(View.GONE);
                break;
            case R.id.sell:
                product_type="2";
                sell.setTextColor(Color.parseColor("#ffffff"));
                sell.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
                rent.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                rent.setTextColor(Color.parseColor("#919191"));
                both.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                both.setTextColor(Color.parseColor("#919191"));
                rent.setPadding(50, 0, 50, 0);
                sell.setPadding(50, 0, 50, 0);
                both.setPadding(50, 0, 50, 0);
                rentprice.setVisibility(View.GONE);
                sellprice.setVisibility(View.VISIBLE);
                bothprice.setVisibility(View.GONE);
                break;
            case R.id.both:
                product_type="3";
                both.setTextColor(Color.parseColor("#ffffff"));
                both.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
                sell.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                sell.setTextColor(Color.parseColor("#919191"));
                rent.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                rent.setTextColor(Color.parseColor("#919191"));
                rent.setPadding(50, 0, 50, 0);
                sell.setPadding(50, 0, 50, 0);
                both.setPadding(50, 0, 50, 0);
                rentprice.setVisibility(View.GONE);
                sellprice.setVisibility(View.GONE);
                bothprice.setVisibility(View.VISIBLE);
                break;
            case R.id.btnyes:
                nagotiable="1";
                btnyes.setTextColor(Color.parseColor("#ffffff"));
                btnyes.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
                btnno.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                btnno.setTextColor(Color.parseColor("#919191"));
                btnyes.setPadding(50, 0, 50, 0);
                btnno.setPadding(50, 0, 50, 0);

                break;
            case R.id.btnno:
                nagotiable="0";
                btnno.setTextColor(Color.parseColor("#ffffff"));
                btnno.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
                btnyes.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                btnyes.setTextColor(Color.parseColor("#919191"));
                btnyes.setPadding(50, 0, 50, 0);
                btnno.setPadding(50, 0, 50, 0);

                break;
        }

    }

    public class ViewPagerAdapter1 extends PagerAdapter {

        private Activity mContext;
        ArrayList<Bitmap> image_uris = new ArrayList<>();

        public ViewPagerAdapter1(Activity mContext, ArrayList<Bitmap> image_uris) {
            this.mContext = mContext;
            this.image_uris = image_uris;
        }

        public int getCount() {
            return image_uris.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item, container, false);

            ImageView img_pager_item = (ImageView) itemView.findViewById(R.id.imageView);
            img_pager_item.setImageBitmap(image_uris.get(position));

            container.addView(itemView);


            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    public class Category {

        StringRequest sr;


        public Category(final GridView rv, final Dialog dialog) {

            final ProgressDialog pDialog = new ProgressDialog(AddProductActivity1.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.GET, BaseUrl.BASE_URL + BaseUrl.product_category + "&page=1", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {

                            categories.clear();
                            ids.clear();
                            images.clear();
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            JSONArray jsonarray = jsonObject1.getJSONArray("category");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobect1 = jsonarray.getJSONObject(i);
                                String id = jsonobect1.getString("id");
                                String name = jsonobect1.getString("name");
                                String image = jsonobect1.getString("image");
                                categories.add(name);
                                ids.add(id);
                                images.add(image);
                            }
                            rv.setAdapter(new CategoryGridAdapter(AddProductActivity1.this, categories, ids
                                    , images, dialog));
                            dialog.show();


                        } else {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(AddProductActivity1.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddProductActivity1.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddProductActivity1.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddProductActivity1.this);
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

    public class Productclor {

        StringRequest sr;


        public Productclor(final ListView rv, final Dialog dialog) {

            final ProgressDialog pDialog = new ProgressDialog(AddProductActivity1.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.GET, BaseUrl.BASE_URL + BaseUrl.product_color, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {

                            categories.clear();
                            ids.clear();
                            images.clear();
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            JSONArray jsonarray = jsonObject1.getJSONArray("color");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobect1 = jsonarray.getJSONObject(i);
                                String id = jsonobect1.getString("id");
                                String name = jsonobect1.getString("name");
                                String image = jsonobect1.getString("code");
                                categories.add(name);
                                ids.add(id);
                                images.add(image);
                            }
                            rv.setAdapter(new ColorListAdapter(AddProductActivity1.this, categories, ids
                                    , images, dialog));
                            dialog.show();


                        } else {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(AddProductActivity1.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddProductActivity1.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddProductActivity1.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddProductActivity1.this);
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

    public class AddProduct {

        StringRequest sr;


        public AddProduct() {

            final ProgressDialog pDialog = new ProgressDialog(AddProductActivity1.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.POST, BaseUrl.BASE_URL + BaseUrl.product_add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(AddProductActivity1.this);
                            builder1.setMessage(message);
                            builder1.setCancelable(true);
                            builder1.setCancelable(false);

                            builder1.setPositiveButton(
                                    "Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent=new Intent(AddProductActivity1.this,DashBoardActivity.class);
                                            startActivity(intent);
                                            finish();
                                            dialog.cancel();
                                        }
                                    });


                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                           // Toast.makeText(AddProductActivity1.this, message, Toast.LENGTH_SHORT).show();



                        } else {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(AddProductActivity1.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddProductActivity1.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddProductActivity1.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddProductActivity1.this);
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
                    JSONArray array = new JSONArray(ImagePickerActivity.paths);
                    params.put("category_id", category_id);
                    params.put("user_id", user_id);
                    params.put("product_name", productname.getText().toString());
                    params.put("quantity", quantity.getText().toString());
                    params.put("taxes", taxes.getText().toString());
                    params.put("weight", weight.getText().toString());
                    params.put("size", size.getText().toString());
                    params.put("color_code", color_code);
                    params.put("description", description.getText().toString());
                    params.put("product_type", product_type);
                    if(product_type.equals("1")){
                        params.put("retail_price", "");
                        params.put("rent_price",rentprice.getText().toString() );
                    }else if(product_type.equals("2")){
                        params.put("retail_price", sellprice.getText().toString());
                        params.put("rent_price","");
                    }else{
                        params.put("retail_price", retailprice.getText().toString());
                        params.put("rent_price",rentpr.getText().toString());
                    }


                    params.put("nagotiable",nagotiable );
                    params.put("location", choselocation.getText().toString());
                    params.put("latitude", pickupLat);
                    params.put("longitude",pickupLng );
                    params.put("country", "1");
                    params.put("image", array.toString());

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

    public class CategoryGridAdapter extends BaseAdapter {
        List<String> categories = new ArrayList<>();
        private Context mContext;
        List<String> ids = new ArrayList<>();
        private Dialog dialog;
        List<String> images = new ArrayList<>();

        public CategoryGridAdapter(Context mContext, List<String> categories, List<String> ids,
                                   List<String> images, Dialog dialog) {
            this.mContext = mContext;
            this.categories = categories;
            this.ids = ids;
            this.dialog = dialog;
            this.images = images;
        }


        @Override


        public int getCount() {
            return ids.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialogcategroies_listdetails, null);
            final int pos = position;
            RelativeLayout relclick = (RelativeLayout) view.findViewById(R.id.relclick);
            LatoExtraBoldTextview name = (LatoExtraBoldTextview) view.findViewById(R.id.name);
            name.setText(categories.get(pos));

            relclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    category_id=ids.get(pos);
                    chosecate.setText(categories.get(pos));
                    dialog.cancel();
                }
            });


            return view;

        }
    }

    public class ColorListAdapter extends BaseAdapter {
        List<String> categories = new ArrayList<>();
        private Context mContext;
        List<String> ids = new ArrayList<>();
        private Dialog dialog;
        List<String> images = new ArrayList<>();

        public ColorListAdapter(Context mContext, List<String> categories, List<String> ids,
                                List<String> images, Dialog dialog) {
            this.mContext = mContext;
            this.categories = categories;
            this.ids = ids;
            this.dialog = dialog;
            this.images = images;
        }


        @Override


        public int getCount() {
            return ids.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialogcolor_listdetails, null);
            final int pos = position;
            LinearLayout cll = (LinearLayout) view.findViewById(R.id.cll);
            final LinearLayout relclk = (LinearLayout) view.findViewById(R.id.relclk);
            cll.setBackgroundColor(Color.parseColor(images.get(pos)));

            LatoExtraBoldTextview name = (LatoExtraBoldTextview) view.findViewById(R.id.name);
            name.setText(categories.get(pos));
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    relclk.performClick();
                }
            });
            relclk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clredit.setText(categories.get(pos));
                    color_code=images.get(pos);
                    clrcode.setBackgroundColor(Color.parseColor(images.get(pos)));
                    //chosecate.setText(categories.get(pos));
                    dialog.cancel();
                }
            });


            return view;

        }
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTasks extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            // String key = "key=AIzaSyAdWDAfT_vljW2hBr_drpgLiqTeJR0wT90";
            String key = "key=" + API_KEY;
            String location = "location=" + String.valueOf(pickupLat) + "," + String.valueOf(pickupLng);
            String input = "";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }


            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "radius=500";

            // Building the parameters to the web service
            String parameters = input + "&" + sensor + "&" + key + "&" + location;


            // Output format
            String output = "json";
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?" + input + "&" + location + "&" + sensor + "&" + key ;
// Building// the url to the web service
            //  String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?"+parameters;

            try {
                // Fetching the data from web service in background
                data = downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTasks = new ParserTasks();

            // Starting Parsing the JSON string returned by Web Service
            parserTasks.execute(result);
        }
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            //Log.d("Exception while downloading url", e.toString());
        } finally {
            if (iStream != null)
                iStream.close();


            urlConnection.disconnect();
        }
        return data;
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTasks extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(final List<HashMap<String, String>> result) {

            final String[] from = new String[]{"description"};
            int[] to = new int[]{android.R.id.text1};

            // Creating a SimpleAdapter for the AutoCompleteTextView
            final SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);


            // Setting the adapter
            dialog_searchLocation.setAdapter(adapter);
            dialog_searchLocation.setDropDownHeight(0);
            dialog_searchLocation.setDropDownWidth(0);
            listview.setAdapter(adapter);
            listview.setTextFilterEnabled(true);
            listview.setVisibility(View.VISIBLE);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //catid = "0";
                    Object item = adapterView.getItemAtPosition(i);
                    HashMap<String, String> hm = (HashMap<String, String>) item;
                    choselocation.setText(hm.get("description"));
                    String seladdress = dialog_searchLocation.getText().toString();
                    dialog_searchLocation.setText(hm.get("description"));
                    getLocationFromAddress(choselocation.getText().toString());

                    listview.setVisibility(View.GONE);

                    hideSoftKeyboard();
                    dialoglctn.dismiss();

                }
            });
        }
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;


        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            pickupLat = String.valueOf(location.getLatitude());
            pickupLng = String.valueOf(location.getLongitude());
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddProductActivity1.this);
        builder1.setMessage("You will loose your all uploaded data. Are you sure you want to exit?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(AddProductActivity1.this, SelectPrductCity.class);
                        startActivity(intent);
                        finish();
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
