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
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.squareup.picasso.Picasso;
import com.takemysaree.R;
import com.takemysaree.adapter.ProductDetailadapter;
import com.takemysaree.adapter.RecyclerAdapter;
import com.takemysaree.fonts.LatoBoldTextview;
import com.takemysaree.fonts.LatoExtraBoldTextview;
import com.takemysaree.fonts.LatoRegularTextview;
import com.takemysaree.models.MyApplication;
import com.takemysaree.models.RoundCornersTransform;
import com.takemysaree.models.RoundCornersTransform1;
import com.takemysaree.models.RoundCornersTransform2;
import com.takemysaree.tabfragments.FragmentIndividualFeed;
import com.takemysaree.utils.BaseUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class WishlistActivity extends AppCompatActivity {
    private GridView grid_view;
    private SharedPreferences sharedpreferences;
    private LatoRegularTextview nodata;
    private String userid;
private ImageView ivHamburger;
    private HomeGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wish_listing);
        sharedpreferences = getApplicationContext().getSharedPreferences(BaseUrl.prfs, Context.MODE_PRIVATE);
        userid = sharedpreferences.getString("userid", "");
        grid_view = (GridView) findViewById(R.id.grid_view);
        ivHamburger=(ImageView)findViewById(R.id.ivHamburger);
        ivHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        nodata=(LatoRegularTextview)findViewById(R.id.nodata);
        WishList wishList = new WishList();
        wishList.addQueue();


    }

    public class RemoveWishlist {

        StringRequest sr;


        public RemoveWishlist(final String productid) {

            final ProgressDialog pDialog = new ProgressDialog(WishlistActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.POST, BaseUrl.BASE_URL +
                    BaseUrl.remove_wishlist, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {


                            WishList wishList = new WishList();
                            wishList.addQueue();

                        } else {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(WishlistActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(WishlistActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(WishlistActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(WishlistActivity.this);
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
                    params.put("product_id", productid);
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

    public class WishList {

        StringRequest sr;


        public WishList() {

            final ProgressDialog pDialog = new ProgressDialog(WishlistActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.GET, BaseUrl.BASE_URL + BaseUrl.list_wishlist
                    + "&user_id=" + userid, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {
                           nodata.setVisibility(View.GONE);
                            grid_view.setVisibility(View.VISIBLE);
                            ArrayList<Individualmodel> individualmodels = new ArrayList<>();
                            individualmodels.clear();
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            JSONArray jsonarray = jsonObject1.getJSONArray("product");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobect1 = jsonarray.getJSONObject(i);
                                Individualmodel individualmodel = new Individualmodel();
                                individualmodel.setId(jsonobect1.getString("id"));
                                individualmodel.setName(jsonobect1.getString("name"));
                                individualmodel.setAds(jsonobect1.getString("ads"));
                                individualmodel.setIn_stock(jsonobect1.getString("in_stock"));
                                individualmodel.setOn_rent(jsonobect1.getString("on_rent"));
                                individualmodel.setIs_multi(jsonobect1.getString("is_multi"));
                                individualmodel.setImage(jsonobect1.getString("image"));
                                individualmodel.setRetail_price(jsonobect1.getString("retail_price"));
                                individualmodels.add(individualmodel);
                            }

                            adapter = new HomeGridAdapter(WishlistActivity.this, individualmodels);
                            grid_view.setAdapter(adapter);

                        } else {

                            nodata.setVisibility(View.VISIBLE);
                            grid_view.setVisibility(View.GONE);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(WishlistActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(WishlistActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(WishlistActivity.this);
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
        public View getView(int position, View convertView, ViewGroup parent) {
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            int dens = dm.densityDpi;
            double wi = (double) width / (double) dens;
            double hi = (double) height / (double) dens;
            double x = Math.pow(wi, 2);
            double y = Math.pow(hi, 2);
            double screenInches = Math.sqrt(x + y);
            final int pos = position;


            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_listdetails, null);

            ImageView imageview1 = (ImageView) view.findViewById(R.id.imageview1);
            FrameLayout fl1 = (FrameLayout) view.findViewById(R.id.fl1);
            ImageView sold = (ImageView) view.findViewById(R.id.sold);
            ImageView rented = (ImageView) view.findViewById(R.id.rented);
            final ImageView remove = (ImageView) view.findViewById(R.id.remove);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RemoveWishlist removeWishlist = new RemoveWishlist(individualmodels.get(pos).getId());
                    removeWishlist.addQueue();

                }
            });
            LatoRegularTextview name = (LatoRegularTextview) view.findViewById(R.id.name);
            LatoBoldTextview price = (LatoBoldTextview) view.findViewById(R.id.price);
            name.setText(individualmodels.get(pos).getName());
            price.setText("$" + individualmodels.get(pos).getRetail_price());
            if (individualmodels.get(pos).getImage().equals("")) {
                Picasso.with(mContext)
                        .load(R.drawable.emptyimage1)
                        .placeholder(R.drawable.emptyimage1)
                        .error(R.drawable.emptyimage1)
                        .into(imageview1);
            } else {
                Picasso.with(mContext)
                        .load(individualmodels.get(pos).getImage())
                        .placeholder(R.drawable.emptyimage1)
                        .error(R.drawable.emptyimage1)
                        .into(imageview1);
            }

            fl1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProductDetailActivity.class);
                    intent.putExtra("productid", individualmodels.get(pos).getId());
                    mContext.startActivity(intent);
                }
            });

            if (individualmodels.get(pos).getIn_stock().equals("0")) {
                sold.setVisibility(View.VISIBLE);
            } else {
                sold.setVisibility(View.GONE);
            }
            if (individualmodels.get(pos).getOn_rent().equals("0")) {
                rented.setVisibility(View.GONE);
            } else {
                rented.setVisibility(View.VISIBLE);
            }


            return view;


        }
    }

    public class Individualmodel implements Serializable {
        private String id;
        private String name;
        private String ads;
        private String in_stock;

        public String getRetail_price() {
            return retail_price;
        }

        public void setRetail_price(String retail_price) {
            this.retail_price = retail_price;
        }

        private String on_rent;
        private String retail_price;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAds() {
            return ads;
        }

        public void setAds(String ads) {
            this.ads = ads;
        }

        public String getIn_stock() {
            return in_stock;
        }

        public void setIn_stock(String in_stock) {
            this.in_stock = in_stock;
        }

        public String getOn_rent() {
            return on_rent;
        }

        public void setOn_rent(String on_rent) {
            this.on_rent = on_rent;
        }

        public String getIs_multi() {
            return is_multi;
        }

        public void setIs_multi(String is_multi) {
            this.is_multi = is_multi;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        private String is_multi;
        private String image;


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
