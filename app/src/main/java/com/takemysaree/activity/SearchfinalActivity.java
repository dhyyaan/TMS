package com.takemysaree.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.takemysaree.R;
import com.takemysaree.fonts.LatoBoldTextview;
import com.takemysaree.fonts.LatoExtraBoldTextview;
import com.takemysaree.fonts.LatoNormalEditText;
import com.takemysaree.fonts.LatoRegularTextview;
import com.takemysaree.models.Individualmodels;
import com.takemysaree.models.MyApplication;
import com.takemysaree.tabfragments.FragmentVendorsFeedbuy;
import com.takemysaree.utils.BaseUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by think360user on 3/11/17.
 */

public class SearchfinalActivity extends Activity implements View.OnClickListener {
    LatoRegularTextview bothcounty,usa,canada,individualuser,vendoruser,rent,sell,both;
    private LinearLayout cateclick;

    private LatoBoldTextview reset;
    private LatoBoldTextview searchproduct;
    private LatoRegularTextview chosecate,price;
    private ImageView crsq;
    List<String> ids = new ArrayList<>();
    List<String> categories = new ArrayList<>();
    List<String> images = new ArrayList<>();
    private LatoNormalEditText dialog_searchLocation;
    int selectedpos=0;
    private String is_vendor,pro_type;
    private String category_id="";
    private ImageView dialogbackpress;
    private String pricevalue="";

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_fragment1);
        initViews();
        initListeners();


    }

    private void initListeners() {
        bothcounty.setOnClickListener(this);
        usa.setOnClickListener(this);
        canada.setOnClickListener(this);
        individualuser.setOnClickListener(this);
        vendoruser.setOnClickListener(this);
        rent.setOnClickListener(this);
        both.setOnClickListener(this);
        sell.setOnClickListener(this);
        cateclick.setOnClickListener(this);
        price.setOnClickListener(this);
        searchproduct.setOnClickListener(this);
        crsq.setOnClickListener(this);
        reset.setOnClickListener(this);
        dialogbackpress.setOnClickListener(this);
    }

    @SuppressLint("NewApi")
    private void initViews() {
        dialogbackpress=(ImageView)findViewById(R.id.dialogbackpress);
        crsq=(ImageView)findViewById(R.id.crsq);
        searchproduct=(LatoBoldTextview)findViewById(R.id.searchproduct);
        dialog_searchLocation=(LatoNormalEditText)findViewById(R.id.dialog_searchLocation);
        bothcounty = (LatoRegularTextview) findViewById(R.id.bothcounty);
        cateclick = (LinearLayout) findViewById(R.id.cateclick);
        reset=(LatoBoldTextview)findViewById(R.id.reset);
        usa = (LatoRegularTextview) findViewById(R.id.usa);
        chosecate = (LatoRegularTextview) findViewById(R.id.chosecate);
        canada = (LatoRegularTextview) findViewById(R.id.canada);
        price = (LatoRegularTextview) findViewById(R.id.price);
        bothcounty.setTextColor(Color.parseColor("#ffffff"));
        bothcounty.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
        usa.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
        usa.setTextColor(Color.parseColor("#919191"));
        canada.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
        canada.setTextColor(Color.parseColor("#919191"));
        bothcounty.setPadding(50, 0, 50, 0);
        usa.setPadding(50, 0, 50, 0);
        canada.setPadding(50, 0, 50, 0);
        individualuser = (LatoRegularTextview) findViewById(R.id.individualuser);
        vendoruser = (LatoRegularTextview) findViewById(R.id.vendoruser);
        individualuser.setTextColor(Color.parseColor("#ffffff"));
        individualuser.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
        vendoruser.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
        vendoruser.setTextColor(Color.parseColor("#919191"));
        individualuser.setPadding(50, 0, 50, 0);
        vendoruser.setPadding(50, 0, 50, 0);
        rent = (LatoRegularTextview) findViewById(R.id.rent);
        both = (LatoRegularTextview) findViewById(R.id.both);
        sell = (LatoRegularTextview) findViewById(R.id.sell);
        rent.setTextColor(Color.parseColor("#ffffff"));
        rent.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
        sell.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
        sell.setTextColor(Color.parseColor("#919191"));
        both.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
        both.setTextColor(Color.parseColor("#919191"));
        rent.setPadding(50, 0, 50, 0);
        sell.setPadding(50, 0, 50, 0);
        both.setPadding(50, 0, 50, 0);
        pro_type="1";
        is_vendor="0";
    }
    public class Productser {

        StringRequest sr;


        public Productser() {

            final ProgressDialog pDialog = new ProgressDialog(SearchfinalActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.GET, BaseUrl.BASE_URL + BaseUrl.product_list +
                    "&page=1"+"&is_vendor="+is_vendor+"&pro_type="+pro_type+
                    "&category_id="+category_id+"&price="+pricevalue+
                    "&text="+dialog_searchLocation.getText().toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {

                            ArrayList<Individualmodels> individualmodels = new ArrayList<>();

                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            JSONArray jsonarray = jsonObject1.getJSONArray("product");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobect1 = jsonarray.getJSONObject(i);
                                Individualmodels individualmodel = new Individualmodels();
                                individualmodel.setId(jsonobect1.getString("id"));
                                individualmodel.setName(jsonobect1.getString("name"));
                                individualmodel.setAds(jsonobect1.getString("ads"));
                                individualmodel.setIn_stock(jsonobect1.getString("in_stock"));
                                individualmodel.setOn_rent(jsonobect1.getString("on_rent"));
                                individualmodel.setIs_multi(jsonobect1.getString("is_multi"));
                                individualmodel.setImage(jsonobect1.getString("image"));
                                individualmodels.add(individualmodel);
                            }
                            Intent intent=new Intent(SearchfinalActivity.this,ProductSearchActivity.class);
                            intent.putExtra("mylist", individualmodels);
                            startActivity(intent);
                            finish();


                        } else {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(SearchfinalActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(SearchfinalActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(SearchfinalActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(SearchfinalActivity.this);
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
    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialogbackpress:
                onBackPressed();
                break;
            case R.id.reset:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(SearchfinalActivity.this);
                builder1.setMessage("Are you sure you want to reset all search?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent=new Intent(SearchfinalActivity.this,SearchfinalActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
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
                break;
            case R.id.searchproduct:
                Productser productser=new Productser();
                productser.addQueue();
                break;
            case R.id.price:
                final Dialog dialogpr = new Dialog(SearchfinalActivity.this);
                dialogpr.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogpr.setContentView(R.layout.dialog_listviewlisting);
                final Window windowpr = dialogpr.getWindow();
                windowpr.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                windowpr.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                windowpr.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                LatoBoldTextview titles=(LatoBoldTextview)dialogpr.findViewById(R.id.titles);
                titles.setText("Select Price");
                ImageView ivHamburger=(ImageView)dialogpr.findViewById(R.id.ivHamburger);
                ivHamburger.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogpr.dismiss();
                    }
                });
                ListView listView = (ListView) dialogpr.findViewById(R.id.listView);
                listView.setAdapter(new ColorListAdapter(dialogpr));
                dialogpr.show();
                break;
            case R.id.cateclick:
                final Dialog dialogpsr = new Dialog(SearchfinalActivity.this);
                dialogpsr.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogpsr.setContentView(R.layout.dialog_listviewlisting);
                final Window windowpsr = dialogpsr.getWindow();
                windowpsr.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                windowpsr.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                windowpsr.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                LatoBoldTextview titlesd=(LatoBoldTextview)dialogpsr.findViewById(R.id.titles);
                titlesd.setText("Select Category");
                ImageView ivHamburgerd=(ImageView)dialogpsr.findViewById(R.id.ivHamburger);
                ivHamburgerd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogpsr.dismiss();
                    }
                });
                ListView listViewd = (ListView) dialogpsr.findViewById(R.id.listView);
                Category category = new Category(listViewd, dialogpsr);
                category.addQueue();

                break;
            case R.id.rent:
                pro_type="1";
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
                pro_type="2";
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
                pro_type="3";
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
            case R.id.bothcounty:
                //product_type="1";
                bothcounty.setTextColor(Color.parseColor("#ffffff"));
                bothcounty.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
                canada.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                canada.setTextColor(Color.parseColor("#919191"));
                usa.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                usa.setTextColor(Color.parseColor("#919191"));
                bothcounty.setPadding(50, 0, 50, 0);
                canada.setPadding(50, 0, 50, 0);
                usa.setPadding(50, 0, 50, 0);

                break;
            case R.id.canada:
                //product_type="2";
                canada.setTextColor(Color.parseColor("#ffffff"));
                canada.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
                bothcounty.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                bothcounty.setTextColor(Color.parseColor("#919191"));
                usa.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                usa.setTextColor(Color.parseColor("#919191"));
                bothcounty.setPadding(50, 0, 50, 0);
                usa.setPadding(50, 0, 50, 0);
                canada.setPadding(50, 0, 50, 0);

                break;
            case R.id.usa:
                // product_type="3";
                usa.setTextColor(Color.parseColor("#ffffff"));
                usa.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
                bothcounty.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                bothcounty.setTextColor(Color.parseColor("#919191"));
                canada.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                canada.setTextColor(Color.parseColor("#919191"));
                canada.setPadding(50, 0, 50, 0);
                usa.setPadding(50, 0, 50, 0);
                bothcounty.setPadding(50, 0, 50, 0);

                break;
            case R.id.individualuser:
                is_vendor="0";
                //product_type="1";
                individualuser.setTextColor(Color.parseColor("#ffffff"));
                individualuser.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
                vendoruser.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                vendoruser.setTextColor(Color.parseColor("#919191"));
                individualuser.setPadding(50, 0, 50, 0);
                vendoruser.setPadding(50, 0, 50, 0);


                break;
            case R.id.crsq:
                dialog_searchLocation.setText("");
                break;
            case R.id.vendoruser:
                is_vendor="1";
                //product_type="2";
                vendoruser.setTextColor(Color.parseColor("#ffffff"));
                vendoruser.setBackground(getResources().getDrawable(R.drawable.round_rectangle));
                individualuser.setBackground(getResources().getDrawable(R.drawable.round_rectanglebordertaborange));
                individualuser.setTextColor(Color.parseColor("#919191"));
                vendoruser.setPadding(50, 0, 50, 0);
                individualuser.setPadding(50, 0, 50, 0);

                break;
        }

    }
    public class Category {

        StringRequest sr;


        public Category(final ListView rv, final Dialog dialog) {

            final ProgressDialog pDialog = new ProgressDialog(SearchfinalActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.GET, BaseUrl.BASE_URL +
                    BaseUrl.product_category + "&page=1", new Response.Listener<String>() {
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
                            rv.setAdapter(new CatAdapter
                                    (SearchfinalActivity.this,
                                            categories, ids
                                    , images, dialog));
                            dialog.show();


                        } else {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(SearchfinalActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(SearchfinalActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(SearchfinalActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(SearchfinalActivity.this);
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

private Dialog dialog;
        public ColorListAdapter(Dialog dialog) {
            this.dialog=dialog;
        }


        @Override


        public int getCount() {
            return 3;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialogprice_listdetails, null);
            final int pos = position;

            final LinearLayout relclk = (LinearLayout) view.findViewById(R.id.relclk);

            final RadioButton rb_Choice = (RadioButton) view.findViewById(R.id.rb_Choice);

            final LatoExtraBoldTextview name = (LatoExtraBoldTextview) view.findViewById(R.id.name);
         if(selectedpos==pos){
             rb_Choice.setChecked(true);
         }else{
             rb_Choice.setChecked(false);
         }

           if(pos==0){


               name.setText("Low Price");

           }else if(pos==1){
               name.setText("High Price");

           }else{
               name.setText("Recent");

           }

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    relclk.performClick();
                }
            });
            relclk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedpos=pos;
                    if(pos==0){
                        pricevalue="2";
                    }else if(pos==1){
                        pricevalue="1";
                    }else{
                        pricevalue="0";
                    }
                    rb_Choice.setChecked(true);

                    price.setText(name.getText().toString());

                    dialog.cancel();
                }
            });


            return view;

        }
    }
    public class CatAdapter extends BaseAdapter {

        private Dialog dialog;
        private List<String> categories=new ArrayList<>();
        private List<String> images=new ArrayList<>();
        private List<String> ids=new ArrayList<>();
        public CatAdapter(Activity activity,List<String> categories ,
        List<String> ids ,
        List<String> images,Dialog dialog ) {
            this.dialog=dialog;
            this.categories=categories;
            this.ids=ids;
            this.images=images;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialogprice_listdetails, null);
            final int pos = position;

            final LinearLayout relclk = (LinearLayout) view.findViewById(R.id.relclk);

            final RadioButton rb_Choice = (RadioButton) view.findViewById(R.id.rb_Choice);

            final TextView name = (TextView) view.findViewById(R.id.name);
            if(selectedpos==pos){
                rb_Choice.setChecked(true);
            }else{
                rb_Choice.setChecked(false);
            }

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
                    selectedpos=pos;
                   category_id=ids.get(pos);
                    rb_Choice.setChecked(true);

                    chosecate.setText(categories.get(pos));

                    dialog.cancel();
                }
            });


            return view;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(SearchfinalActivity.this,DashBoardActivity.class);
        startActivity(intent);
        finish();
    }
}
