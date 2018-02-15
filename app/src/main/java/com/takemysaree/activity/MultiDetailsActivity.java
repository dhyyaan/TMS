package com.takemysaree.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.takemysaree.R;
import com.takemysaree.adapter.RecyclerAdapterOffer;
import com.takemysaree.fonts.LatoBoldTextview;
import com.takemysaree.fonts.LatoExtraBoldTextview;
import com.takemysaree.fonts.LatoNormalEditText;
import com.takemysaree.fonts.LatoRegularTextview;
import com.takemysaree.models.GPSTracker;
import com.takemysaree.models.MyApplication;
import com.takemysaree.models.PlaceJSONParser;
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


public class MultiDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private LatoNormalEditText productname, quantity, taxes, weight, description;
    private GPSTracker gps;
    private LinearLayout clrcode;
    private LatoRegularTextview sell, both, rent, btnyes, btnno;
    List<String> ids = new ArrayList<>();
    List<String> categories = new ArrayList<>();
    List<String> images = new ArrayList<>();
    private ImageView ivHamburgers;
    private static final String API_KEY = "AIzaSyBMXzFDv4QbMuwvKuW8GC9vW2z4aKrxW98";
    private AutoCompleteTextView dialog_searchLocation;
    private LatoNormalEditText chosecate, choselocation;
    private ParserTasks parserTasks;
    private LatLng p1 = null;
    private PlacesTasks placesTasks;

    String knownName, address, city, country, postalCode, state;
    private List<Address> addresses;
    private double latitude, longitude;
    private String pickupLat, pickupLng;
    private ImageView crsq, dialogbackpress;
    private ListView listview;
    private LinearLayout selectclor;
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    Dialog dialoglctn;
    private LatoBoldTextview postproduct;
    private String user_id, color_code, product_type, nagotiable;
    private LatoNormalEditText clredit;
    private Geocoder geocoder;
    ArrayList<Uri> image_uris = new ArrayList<Uri>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiproduct_detailtitle);
        initVIew();
        initListeners();
        getCurrentLocation();


    }

    private void initListeners() {
        sell.setOnClickListener(this);
        both.setOnClickListener(this);
        rent.setOnClickListener(this);
        btnno.setOnClickListener(this);
        btnyes.setOnClickListener(this);
        choselocation.setOnClickListener(this);
        clrcode.setOnClickListener(this);
        selectclor.setOnClickListener(this);
        clredit.setOnClickListener(this);
        postproduct.setOnClickListener(this);
    }

    @SuppressLint("NewApi")
    private void initVIew() {
        selectclor = (LinearLayout) findViewById(R.id.selectclor);
        clrcode = (LinearLayout) findViewById(R.id.clrcode);
        postproduct = (LatoBoldTextview) findViewById(R.id.postproduct);
        sell = (LatoRegularTextview) findViewById(R.id.sell);
        btnyes = (LatoRegularTextview) findViewById(R.id.btnyes);
        btnno = (LatoRegularTextview) findViewById(R.id.btnno);
        rent = (LatoRegularTextview) findViewById(R.id.rent);
        both = (LatoRegularTextview) findViewById(R.id.both);
        ivHamburgers=(ImageView)findViewById(R.id.ivHamburger);
        ivHamburgers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        clredit = (LatoNormalEditText) findViewById(R.id.clredit);
        productname = (LatoNormalEditText) findViewById(R.id.productname);
        quantity = (LatoNormalEditText) findViewById(R.id.quantity);
        taxes = (LatoNormalEditText) findViewById(R.id.taxes);
        weight = (LatoNormalEditText) findViewById(R.id.weight);
        description = (LatoNormalEditText) findViewById(R.id.description);
        choselocation = (LatoNormalEditText) findViewById(R.id.choselocation);
        rent.setTextColor(Color.parseColor("#ffffff"));
        rent.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
        sell.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
        sell.setTextColor(Color.parseColor("#919191"));
        both.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
        both.setTextColor(Color.parseColor("#919191"));
        rent.setPadding(50, 0, 50, 0);
        sell.setPadding(50, 0, 50, 0);
        both.setPadding(50, 0, 50, 0);
        product_type = "1";
        btnyes.setTextColor(Color.parseColor("#ffffff"));
        btnyes.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
        btnno.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
        btnno.setTextColor(Color.parseColor("#919191"));
        btnyes.setPadding(50, 0, 50, 0);
        btnno.setPadding(50, 0, 50, 0);
        nagotiable = "1";
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

    private void getCode() {
        if (ContextCompat.checkSelfPermission(MultiDetailsActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MultiDetailsActivity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CAMERA);

        } else {
            Config config = new Config();
            config.setToolbarTitleRes(R.string.app_name);
            config.setSelectionMin(1);
            config.setSelectionLimit(5);

            config.setFlashOn(true);


            getImages(config);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Config config = new Config();
                    config.setToolbarTitleRes(R.string.app_name);
                    config.setSelectionMin(1);
                    config.setSelectionLimit(5);

                    config.setFlashOn(true);


                    getImages(config);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void getImages(Config config) {


        ImagePickerActivity.setConfig(config);

        Intent intent = new Intent(this, ImagePickerActivity.class);

        intent.putExtra("checkvalue", "1");
        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_IMAGES) {

                image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

                if (image_uris != null) {


                    Intent intent1 = new Intent(MultiDetailsActivity.this, MutliProductActivity.class);
                    intent1.putParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS, image_uris);
                    intent1.putExtra("productname", productname.getText().toString());

                    intent1.putExtra("taxes", taxes.getText().toString());
                    intent1.putExtra("weight", weight.getText().toString());
                    intent1.putExtra("color_code", color_code);
                    intent1.putExtra("description", description.getText().toString());
                    intent1.putExtra("choselocation", choselocation.getText().toString());
                    intent1.putExtra("lat", pickupLat);
                    intent1.putExtra("lng", pickupLng);
                    intent1.putExtra("product_type", product_type);
                    intent1.putExtra("nagotiable", nagotiable);
                    intent1.putExtra("quantity", quantity.getText().toString());
                    startActivity(intent1);
                    finish();


                }


            }
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.postproduct:
                if (productname.getText().toString().equals("")) {
                    productname.setError("Enter product title");
                }  else if (weight.getText().toString().equals("")) {
                    weight.setError("Enter weight");
                } else if (color_code == null) {
                    clredit.setError("Select Color");
                } else if (description.getText().toString().equals("")) {
                    description.setError("Enter description");
                } else if (choselocation.getText().toString().equals("")) {
                    choselocation.setError("Select Location");
                } else {
                    getCode();


                }

                break;
            case R.id.rent:
                product_type = "1";
                rent.setTextColor(Color.parseColor("#ffffff"));
                rent.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
                sell.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                sell.setTextColor(Color.parseColor("#919191"));
                both.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                both.setTextColor(Color.parseColor("#919191"));
                rent.setPadding(50, 0, 50, 0);
                sell.setPadding(50, 0, 50, 0);
                both.setPadding(50, 0, 50, 0);

                break;
            case R.id.sell:
                product_type = "2";
                sell.setTextColor(Color.parseColor("#ffffff"));
                sell.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
                rent.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                rent.setTextColor(Color.parseColor("#919191"));
                both.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                both.setTextColor(Color.parseColor("#919191"));
                rent.setPadding(50, 0, 50, 0);
                sell.setPadding(50, 0, 50, 0);
                both.setPadding(50, 0, 50, 0);

                break;
            case R.id.both:
                product_type = "3";
                both.setTextColor(Color.parseColor("#ffffff"));
                both.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
                sell.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                sell.setTextColor(Color.parseColor("#919191"));
                rent.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                rent.setTextColor(Color.parseColor("#919191"));
                rent.setPadding(50, 0, 50, 0);
                sell.setPadding(50, 0, 50, 0);
                both.setPadding(50, 0, 50, 0);

                break;
            case R.id.btnyes:
                nagotiable = "1";
                btnyes.setTextColor(Color.parseColor("#ffffff"));
                btnyes.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
                btnno.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                btnno.setTextColor(Color.parseColor("#919191"));
                btnyes.setPadding(50, 0, 50, 0);
                btnno.setPadding(50, 0, 50, 0);

                break;
            case R.id.btnno:
                nagotiable = "0";
                btnno.setTextColor(Color.parseColor("#ffffff"));
                btnno.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
                btnyes.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                btnyes.setTextColor(Color.parseColor("#919191"));
                btnyes.setPadding(50, 0, 50, 0);
                btnno.setPadding(50, 0, 50, 0);

                break;
            case R.id.selectclor:
                final Dialog dialogs = new Dialog(MultiDetailsActivity.this);
                dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogs.setContentView(R.layout.dialog_listviewlisting);
                final Window windows = dialogs.getWindow();
                windows.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                windows.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                windows.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                ListView listView = (ListView) dialogs.findViewById(R.id.listView);
                LatoBoldTextview titles = (LatoBoldTextview) dialogs.findViewById(R.id.titles);
                titles.setText("Select Color");
                ImageView ivHamburger = (ImageView) dialogs.findViewById(R.id.ivHamburger);
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
                productname.setError(null);
                taxes.setError(null);
                weight.setError(null);
                description.setError(null);


                dialoglctn = new Dialog(MultiDetailsActivity.this, R.style.Dialog_No_Border);
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
            case R.id.clredit:
                selectclor.performClick();
                break;
            case R.id.clrcode:
                selectclor.performClick();
                break;
        }
    }

    public class Productclor {

        StringRequest sr;


        public Productclor(final ListView rv, final Dialog dialog) {

            final ProgressDialog pDialog = new ProgressDialog(MultiDetailsActivity.this);
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
                            rv.setAdapter(new ColorListAdapter(MultiDetailsActivity.this, categories, ids
                                    , images, dialog));
                            dialog.show();


                        } else {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MultiDetailsActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MultiDetailsActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MultiDetailsActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MultiDetailsActivity.this);
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
                    color_code = images.get(pos);
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
        super.onBackPressed();
        Intent intent=new Intent(MultiDetailsActivity.this,SelectPrductCity.class);
        startActivity(intent);
        finish();
    }
}
