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
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
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

public class MyCart extends Activity {
    private ListView listview;
    private SharedPreferences sharedpreferences;
    private String userid;
    private ExpandableHeightListView expandable_listview;
    ArrayList<String> ids = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> price_sells = new ArrayList<>();
    ArrayList<String> price_rents = new ArrayList<>();
    ArrayList<String> pro_types = new ArrayList<>();
    ArrayList<String> images = new ArrayList<>();
    private HomeGridAdapter adapter;
    private ImageView ivHamburger, ivLargeSari1, ivLargeSari;
    private LayoutInflater myinflater;
    private LinearLayout fnllayout;
    private LatoBoldTextview itemname, wish, prdctprce1, prdctprce;
    private LatoRegularTextview prdcttitle1, prdcttitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_cart);
        sharedpreferences = getApplicationContext().getSharedPreferences(BaseUrl.prfs, Context.MODE_PRIVATE);
        userid = sharedpreferences.getString("userid", "");
        myinflater = getLayoutInflater();
        listview = (ListView) findViewById(R.id.listview);
        fnllayout = (LinearLayout) findViewById(R.id.fnllayout);
        expandable_listview = (ExpandableHeightListView) findViewById(R.id.expandable_listview);
        ViewGroup myHeader = (ViewGroup) myinflater.inflate(R.layout.list_header, listview, false);
        itemname = (LatoBoldTextview) myHeader.findViewById(R.id.itemname);
        wish = (LatoBoldTextview) myHeader.findViewById(R.id.wish);
        prdcttitle1 = (LatoRegularTextview) findViewById(R.id.prdcttitle1);
        prdcttitle = (LatoRegularTextview) findViewById(R.id.prdcttitle);
        prdctprce1 = (LatoBoldTextview) findViewById(R.id.prdctprce1);
        prdctprce = (LatoBoldTextview) findViewById(R.id.prdctprce);
        expandable_listview.addHeaderView(myHeader, null, false);
        wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCart.this, WishlistActivity.class);
                startActivity(intent);
                finish();
            }
        });
        fnllayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCart.this, CheckoutActivity.class);
                startActivity(intent);
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

    }

    public class CartList {

        StringRequest sr;


        public CartList() {

            final ProgressDialog pDialog = new ProgressDialog(MyCart.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.GET, BaseUrl.BASE_URL + BaseUrl.list_cart
                    + "&user_id=" + userid, new Response.Listener<String>() {
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
                            JSONArray jsonarray = jsonObject1.getJSONArray("cart");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobect1 = jsonarray.getJSONObject(i);
                                Individualmodel individualmodel = new Individualmodel();
                                individualmodel.setProduct_name(jsonobect1.getString("product_name"));
                                individualmodel.setProduct_price(jsonobect1.getString("product_price"));
                                individualmodel.setProduct_price_total(jsonobect1.getString("product_price_total"));
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
                                individualmodel.setIn_stock(jsonobect1.getString("in_stock"));
                                individualmodels.add(individualmodel);
                            }
                            JSONArray jsonarrays = jsonObject1.getJSONArray("suggetion");
                            for (int j = 0; j < jsonarrays.length(); j++) {
                                JSONObject jsonObject2 = jsonarrays.getJSONObject(j);
                                ids.add(jsonObject2.getString("id"));
                                names.add(jsonObject2.getString("name"));
                                images.add(jsonObject2.getString("image"));
                                price_rents.add(jsonObject2.getString("price_rent"));
                                price_sells.add(jsonObject2.getString("price_sell"));
                                pro_types.add(jsonObject2.getString("pro_type"));


                            }
                            prdcttitle.setText(names.get(0));
                            prdcttitle1.setText(names.get(1));
                            prdctprce1.setText("$" + price_sells.get(1));
                            prdctprce.setText("$" + price_sells.get(0));

                            Picasso.with(MyCart.this)
                                    .load(images.get(0))
                                    .placeholder(R.drawable.emptyimage1)
                                    .error(R.drawable.emptyimage1)

                                    .into(ivLargeSari1);
                            Picasso.with(MyCart.this)
                                    .load(images.get(1))
                                    .placeholder(R.drawable.emptyimage1)
                                    .error(R.drawable.emptyimage1)

                                    .into(ivLargeSari);
                            itemname.setText("Items(" + individualmodels.size() + ")");
                            adapter = new HomeGridAdapter(MyCart.this, individualmodels);
                            expandable_listview.setAdapter(adapter);
                            expandable_listview.setExpanded(true);
                            //listview.setScrollContainer(false);
                            //setListViewHeightBasedOnChildren(listview);

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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MyCart.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MyCart.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MyCart.this);
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
        private String product_quantity;

        public String getIn_stock() {
            return in_stock;
        }

        public void setIn_stock(String in_stock) {
            this.in_stock = in_stock;
        }

        private String in_stock;

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

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_rv_item, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
            TextView tvLOrderid = (TextView) view.findViewById(R.id.tvLOrderid);
            TextView tvLOrderids = (TextView) view.findViewById(R.id.tvLOrderids);
            TextView tvSize = (TextView) view.findViewById(R.id.tvSize);
            TextView tvQty = (TextView) view.findViewById(R.id.tvQty);
            TextView durvalue = (TextView) view.findViewById(R.id.durvalue);
            TextView tvLSize = (TextView) view.findViewById(R.id.tvLSize);
            TextView secvalue = (TextView) view.findViewById(R.id.secvalue);
            TextView tvLPaymentMode = (TextView) view.findViewById(R.id.tvLPaymentMode);
            LinearLayout dur = (LinearLayout) view.findViewById(R.id.dur);
            LinearLayout rentsec = (LinearLayout) view.findViewById(R.id.rentsec);
            if (individualmodels.get(position).getIn_stock().equals("0")) {
                tvQty.setText("Out Of Stock");
            } else {
                tvQty.setText(individualmodels.get(position).getProduct_quantity());
            }
            if (individualmodels.get(position).getCart_purchase_type().equals("1")) {
                dur.setVisibility(View.VISIBLE);
                rentsec.setVisibility(View.VISIBLE);
                tvLPaymentMode.setText("Price(" + individualmodels.get(position).getRent_days() + "):");
            } else {
                dur.setVisibility(View.GONE);
                rentsec.setVisibility(View.GONE);
                tvLPaymentMode.setText("Price:");
            }
            durvalue.setText(individualmodels.get(position).getRent_from_date() + " to " + individualmodels.get(position).getRent_to_date());
            secvalue.setText("$" + individualmodels.get(position).getSecurity());
            TextView tvOnline = (TextView) view.findViewById(R.id.tvOnline);
            tvLOrderids.setText("Sold by: " + individualmodels.get(position).getProduct_user());
            tvLOrderid.setText(individualmodels.get(position).getProduct_name());
            tvSize.setText(individualmodels.get(position).getProduct_size());
            tvOnline.setText("$" + individualmodels.get(position).getProduct_price());
           /* if(individualmodels.get(position).getProduct_size().equals("null")){
                tvLSize.setVisibility(View.GONE);
                tvSize.setVisibility(View.GONE);
            }else{
                tvLSize.setVisibility(View.VISIBLE);
                tvSize.setVisibility(View.VISIBLE);
            }*/
            Picasso.with(mContext)
                    .load(individualmodels.get(position).getProduct_image())
                    .placeholder(R.drawable.emptyimage1)
                    .error(R.drawable.emptyimage1)

                    .into(imageView);

            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteCart deleteCart = new DeleteCart(individualmodels.get(position).getCart_id());
                    deleteCart.addQueue();
                }
            });
            return view;


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MyCart.this, DashBoardActivity.class);
        startActivity(intent);
        finish();
    }

    public class DeleteCart {

        StringRequest sr;


        public DeleteCart(final String productid) {

            final ProgressDialog pDialog = new ProgressDialog(MyCart.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.POST, BaseUrl.BASE_URL +
                    BaseUrl.delete_cart, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (status.equals("true")) {
                            Intent intent = new Intent(MyCart.this, MyCart.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            finish();


                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MyCart.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MyCart.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MyCart.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MyCart.this);
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
                    params.put("cart_id", productid);


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
