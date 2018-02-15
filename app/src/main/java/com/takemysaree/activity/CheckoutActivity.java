package com.takemysaree.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.squareup.picasso.Picasso;
import com.takemysaree.R;
import com.takemysaree.adapter.SlideAdapter;
import com.takemysaree.fonts.LatoBoldTextview;
import com.takemysaree.fonts.LatoRegularTextview;
import com.takemysaree.models.MyApplication;
import com.takemysaree.utils.BaseUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by think360user on 7/12/17.
 */

public class CheckoutActivity extends Activity {
    private ListView listview;
    private SharedPreferences sharedpreferences;
    private String userid, address_id, shipping_code;
    private ExpandableHeightListView expandable_listview;
    private ArrayList<String> codes = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    ArrayList<String> price_sells = new ArrayList<>();
    ArrayList<String> price_rents = new ArrayList<>();
    ArrayList<String> pro_types = new ArrayList<>();
    ArrayList<String> images = new ArrayList<>();
    private HomeGridAdapter adapter;
    int pos = 0;
    private String post, kk;
    ArrayAdapter<String> adapter1;
    SlideAdapter adapters;
    private TextView addrs, ctys, zipcode;
    private ImageView ivHamburger, ivLargeSari1, ivLargeSari;
    private LayoutInflater myinflater;
    private LinearLayout addaddress, addaddress1;
    private LatoBoldTextview itemname, wish, prdctprce1, prdctprce;
    private LatoRegularTextview prdcttitle1, prdcttitle;
    private TextView addnew, grandtotal, total_amount;
    LinearLayout fnllayout, dscntview, applyview;
    private Spinner spinner1;
    private TextView applycpn, dscntvalue, addadds;
    private EditText promocode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_screen);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        address_id = getIntent().getStringExtra("address_id");
        shipping_code = getIntent().getStringExtra("shipping_code");
        if (shipping_code == null) {
            shipping_code = "";
        } else {
            shipping_code = getIntent().getStringExtra("shipping_code");
        }
        if (address_id == null) {
            address_id = "";
        } else {
            address_id = getIntent().getStringExtra("address_id");
        }
        sharedpreferences = getApplicationContext().getSharedPreferences(BaseUrl.prfs, Context.MODE_PRIVATE);
        userid = sharedpreferences.getString("userid", "");
        myinflater = getLayoutInflater();
        listview = (ListView) findViewById(R.id.listview);
        promocode = (EditText) findViewById(R.id.promocode);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        addaddress = (LinearLayout) findViewById(R.id.addaddress);
        dscntview = (LinearLayout) findViewById(R.id.dscntview);
        applyview = (LinearLayout) findViewById(R.id.applyview);
        addaddress1 = (LinearLayout) findViewById(R.id.addaddress1);
        fnllayout = (LinearLayout) findViewById(R.id.fnllayout);
        total_amount = (TextView) findViewById(R.id.total_amount);
        applycpn = (TextView) findViewById(R.id.applycpn);
        addrs = (TextView) findViewById(R.id.addrs);
        addadds = (TextView) findViewById(R.id.addadds);
        dscntvalue = (TextView) findViewById(R.id.dscntvalue);
        grandtotal = (TextView) findViewById(R.id.grandtotal);
        ctys = (TextView) findViewById(R.id.ctys);
        zipcode = (TextView) findViewById(R.id.zipcode);
        addnew = (TextView) findViewById(R.id.addnew);
        fnllayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderCreate orderCreate = new OrderCreate();
                orderCreate.addQueue();


            }
        });
        dscntvalue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CopuonDel copuonDel = new CopuonDel();
                copuonDel.addQueue();
            }
        });
        addadds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckoutActivity.this, Add_Address.class);
                startActivity(intent);
                finish();
            }
        });
        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, Add_Address.class);
                startActivity(intent);
                finish();
            }
        });
        expandable_listview = (ExpandableHeightListView) findViewById(R.id.expandable_listview);
        ViewGroup myHeader = (ViewGroup) myinflater.inflate(R.layout.list_header, listview, false);
        itemname = (LatoBoldTextview) myHeader.findViewById(R.id.itemname);
        wish = (LatoBoldTextview) myHeader.findViewById(R.id.wish);
        wish.setVisibility(View.GONE);
        expandable_listview.addHeaderView(myHeader, null, false);
        wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, WishlistActivity.class);
                startActivity(intent);
                finish();
            }
        });
        CartList cartList = new CartList();
        cartList.addQueue();
        ivHamburger = (ImageView) findViewById(R.id.ivHamburger);
        ivLargeSari1 = (ImageView) findViewById(R.id.ivLargeSari1);
        ivLargeSari = (ImageView) findViewById(R.id.ivLargeSari);
        ivHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        applycpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (promocode.getText().toString().equals("")) {
                    promocode.setError("Please Enter promocode");
                } else {
                    promocode.setError(null);
                    CopuonApply copuonApply = new CopuonApply();
                    copuonApply.addQueue();

                }
            }
        });

    }

    public class OrderCreate {

        StringRequest sr;


        public OrderCreate() {

            final ProgressDialog pDialog = new ProgressDialog(CheckoutActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.GET, BaseUrl.BASE_URL + BaseUrl.create_order
                    + "&user_id=" + userid, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {
                            JSONObject jsonObject2 = jsonObject.getJSONObject("data");
                            Intent intent = new Intent(CheckoutActivity.this, StripePaymentActivity.class);
                            intent.putExtra("total_price", jsonObject2.getString("total_price"));
                            intent.putExtra("order_id", jsonObject2.getString("order_id"));
                            startActivity(intent);


                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(CheckoutActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CheckoutActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CheckoutActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CheckoutActivity.this);
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

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public class CopuonApply {

        StringRequest sr;


        public CopuonApply() {

            final ProgressDialog pDialog = new ProgressDialog(CheckoutActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.POST, BaseUrl.BASE_URL + BaseUrl.add_discount_coupon
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {
                            promocode.setText(null);
                            hideSoftKeyboard();
                            CartList cartList = new CartList();
                            cartList.addQueue();

                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(CheckoutActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CheckoutActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CheckoutActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CheckoutActivity.this);
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
                    params.put("user_id", userid);
                    params.put("coupon_code", promocode.getText().toString());


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

    public class CopuonDel {

        StringRequest sr;


        public CopuonDel() {

            final ProgressDialog pDialog = new ProgressDialog(CheckoutActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.POST, BaseUrl.BASE_URL + BaseUrl.delete_discount_coupon
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {
                            CartList cartList = new CartList();
                            cartList.addQueue();

                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(CheckoutActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CheckoutActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CheckoutActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CheckoutActivity.this);
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
                    params.put("user_id", userid);


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

    public class CartList {

        StringRequest sr;


        public CartList() {

            final ProgressDialog pDialog = new ProgressDialog(CheckoutActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.GET, BaseUrl.BASE_URL + BaseUrl.cart_checkout
                    + "&user_id=" + userid + "&address_id=" + address_id + "&shipping_code=" + shipping_code, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {
                            // nodata.setVisibility(View.GONE);
                            listview.setVisibility(View.VISIBLE);
                            ArrayList<Individualmodel> individualmodels = new ArrayList<>();
                            individualmodels.clear();
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            if (jsonObject1.getString("discount").equals("0")) {
                                applyview.setVisibility(View.VISIBLE);
                                dscntview.setVisibility(View.GONE);
                            } else {
                                applyview.setVisibility(View.GONE);
                                dscntview.setVisibility(View.VISIBLE);
                            }
                            JSONArray jsonarray = jsonObject1.getJSONArray("product");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobect1 = jsonarray.getJSONObject(i);

                                Individualmodel individualmodel = new Individualmodel();
                                individualmodel.setProduct_name(jsonobect1.getString("product_name"));
                                individualmodel.setProduct_price(jsonobect1.getString("product_price"));
                                individualmodel.setProduct_price_total(jsonobect1.getString("product_price_total"));
                                individualmodel.setProduct_service_charge(jsonobect1.getString("product_service_charge"));
                                individualmodel.setProduct_shipping_charge(jsonobect1.getString("product_shipping_charge"));
                                individualmodel.setProduct_tax_charge(jsonobect1.getString("product_tax_charge"));
                                individualmodel.setProduct_quantity(jsonobect1.getString("product_quantity"));
                                individualmodel.setCart_id(jsonobect1.getString("cart_id"));
                                individualmodel.setCart_purchase_type(jsonobect1.getString("cart_purchase_type"));
                                individualmodel.setRent_from_date(jsonobect1.getString("rent_from_date"));
                                individualmodel.setRent_to_date(jsonobect1.getString("rent_to_date"));
                                individualmodel.setProduct_user(jsonobect1.getString("product_user"));
                                individualmodel.setProduct_size(jsonobect1.getString("product_size"));
                                individualmodel.setProduct_color(jsonobect1.getString("product_color"));
                                individualmodel.setSecurity(jsonobect1.getString("security"));
                                individualmodel.setProduct_image(jsonobect1.getString("product_image"));
                                individualmodel.setRent_days(jsonobect1.getString("rent_days"));
                                individualmodels.add(individualmodel);
                            }
                            JSONArray jsonArray = jsonObject1.getJSONArray("shipping");
                            names.clear();
                            codes.clear();
                            for (int k = 0; k < jsonArray.length(); k++) {

                                JSONObject jsonObject2 = jsonArray.getJSONObject(k);
                                names.add(jsonObject2.getString("name"));
                                codes.add(jsonObject2.getString("code"));

                            }
                            adapter1 = new ArrayAdapter<String>(CheckoutActivity.this, R.layout.spinner_item, names);
                            adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);

                            spinner1.setAdapter(adapter1);
                            spinner1.setSelection(pos);
                            shipping_code = "";

                            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Object item = parent.getItemAtPosition(position);
                                    // spinners = parent.getItemAtPosition(position).toString();

                                    if (position == -1) {
                                        //post=String.valueOf(position);

                                    } else {

                                        pos = position;
                                        kk = codes.get(position);
                                        if (shipping_code.equals("")) {
                                            shipping_code = kk;
                                        } else {
                                            CartList cartList = new CartList();
                                            cartList.addQueue();
                                        }


                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            grandtotal.setText("$" + jsonObject1.getString("total_price"));
                            total_amount.setText("$" + jsonObject1.getString("total_amount"));

                            String have_address = jsonObject1.getString("have_address");
                            if (have_address.equals("0")) {
                                addaddress.setVisibility(View.VISIBLE);
                                addaddress1.setVisibility(View.GONE);

                            } else {
                                addaddress.setVisibility(View.GONE);
                                addaddress1.setVisibility(View.VISIBLE);
                                JSONObject jsonObject2 = jsonObject1.getJSONObject("address");
                                addrs.setText(jsonObject2.getString("address"));
                                ctys.setText(jsonObject2.getString("city") + ", " +
                                        jsonObject2.getString("state_name") + ", " +
                                        jsonObject2.getString("country_name"));
                                zipcode.setText(jsonObject2.getString("zip_postal_code"));
                            }


                            itemname.setText("Items(" + individualmodels.size() + ")");
                            adapter = new HomeGridAdapter(CheckoutActivity.this, individualmodels);
                            expandable_listview.setAdapter(adapter);
                            expandable_listview.setExpanded(true);


                        } else {

                            // nodata.setVisibility(View.VISIBLE);
                            listview.setVisibility(View.GONE);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CheckoutActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CheckoutActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CheckoutActivity.this);
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


    public class Individualmodel implements Serializable {
        private String product_name;
        private String product_price;
        private String product_price_total;
        private String product_shipping_charge;

        public String getProduct_tax_charge() {
            return product_tax_charge;
        }

        public void setProduct_tax_charge(String product_tax_charge) {
            this.product_tax_charge = product_tax_charge;
        }

        private String product_tax_charge;

        public String getProduct_shipping_charge() {
            return product_shipping_charge;
        }

        public void setProduct_shipping_charge(String product_shipping_charge) {
            this.product_shipping_charge = product_shipping_charge;
        }

        public String getProduct_service_charge() {
            return product_service_charge;
        }

        public void setProduct_service_charge(String product_service_charge) {
            this.product_service_charge = product_service_charge;
        }

        private String product_quantity;
        private String product_service_charge;

        public String getSecurity() {
            return security;
        }

        public void setSecurity(String security) {
            this.security = security;
        }

        private String cart_id;

        public String getRent_days() {
            return rent_days;
        }

        public void setRent_days(String rent_days) {
            this.rent_days = rent_days;
        }

        private String cart_purchase_type;
        private String rent_from_date;
        private String rent_to_date;
        private String security;
        private String rent_days;

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getProduct_price() {
            return product_price;
        }

        public void setProduct_price(String product_price) {
            this.product_price = product_price;
        }

        public String getProduct_price_total() {
            return product_price_total;
        }

        public void setProduct_price_total(String product_price_total) {
            this.product_price_total = product_price_total;
        }

        public String getProduct_quantity() {
            return product_quantity;
        }

        public void setProduct_quantity(String product_quantity) {
            this.product_quantity = product_quantity;
        }

        public String getCart_id() {
            return cart_id;
        }

        public void setCart_id(String cart_id) {
            this.cart_id = cart_id;
        }

        public String getCart_purchase_type() {
            return cart_purchase_type;
        }

        public void setCart_purchase_type(String cart_purchase_type) {
            this.cart_purchase_type = cart_purchase_type;
        }

        public String getRent_from_date() {
            return rent_from_date;
        }

        public void setRent_from_date(String rent_from_date) {
            this.rent_from_date = rent_from_date;
        }

        public String getRent_to_date() {
            return rent_to_date;
        }

        public void setRent_to_date(String rent_to_date) {
            this.rent_to_date = rent_to_date;
        }

        public String getProduct_user() {
            return product_user;
        }

        public void setProduct_user(String product_user) {
            this.product_user = product_user;
        }

        public String getProduct_size() {
            return product_size;
        }

        public void setProduct_size(String product_size) {
            this.product_size = product_size;
        }

        public String getProduct_color() {
            return product_color;
        }

        public void setProduct_color(String product_color) {
            this.product_color = product_color;
        }

        public String getProduct_image() {
            return product_image;
        }

        public void setProduct_image(String product_image) {
            this.product_image = product_image;
        }

        private String product_user;
        private String product_size;
        private String product_color;
        private String product_image;


    }

    public class HomeGridAdapter extends BaseAdapter {
        private Bitmap mBitmap, mBitmap1, mBitmap2, mBitmap3;
        float cornerRadius = 10.0f;
        private Resources mResources;
        private Context mContext;
        private RoundedBitmapDrawable roundedBitmapDrawable, roundedBitmapDrawable1, roundedBitmapDrawable2, roundedBitmapDrawable3;
        private ArrayList<Individualmodel> individualmodels;

        public HomeGridAdapter(Context mContext, ArrayList<Individualmodel> individualmodels) {
            this.mContext = mContext;
            this.individualmodels = individualmodels;
        }


        @Override


        public int getCount() {
            return individualmodels.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_check_rv_item, null);


            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            TextView price = (TextView) view.findViewById(R.id.price);
            TextView tax = (TextView) view.findViewById(R.id.tax);
            TextView prctext = (TextView) view.findViewById(R.id.prctext);
            TextView shpngchrge = (TextView) view.findViewById(R.id.shpngchrge);
            TextView tvQty = (TextView) view.findViewById(R.id.tvQty);
            LinearLayout rentsecure = (LinearLayout) view.findViewById(R.id.rentsecure);
            TextView subtotl = (TextView) view.findViewById(R.id.subtotl);
            TextView rentscre = (TextView) view.findViewById(R.id.rentscre);
            tvQty.setText(individualmodels.get(position).getProduct_quantity());
            price.setText("$" + individualmodels.get(position).getProduct_price());
            tax.setText("$" + individualmodels.get(position).getProduct_tax_charge());
            shpngchrge.setText("$" + individualmodels.get(position).getProduct_shipping_charge());
            subtotl.setText("$" + individualmodels.get(position).getProduct_price_total());
            if (individualmodels.get(position).getCart_purchase_type().equals("1")) {
                rentsecure.setVisibility(View.VISIBLE);
                prctext.setText("Price(" + individualmodels.get(position).getRent_days() + " Days)");
                rentscre.setText("$" + individualmodels.get(position).getProduct_service_charge());
            } else {
                rentsecure.setVisibility(View.GONE);
            }
            Picasso.with(mContext)
                    .load(individualmodels.get(position).getProduct_image())
                    .placeholder(R.drawable.emptyimage1)
                    .error(R.drawable.emptyimage1)

                    .into(imageView);
            return view;


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CheckoutActivity.this, DashBoardActivity.class);
        startActivity(intent);
        finish();
    }

}
