package com.takemysaree.tabfragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.squareup.picasso.Picasso;
import com.takemysaree.R;
import com.takemysaree.activity.ProductDetailActivity;
import com.takemysaree.activity.ProductMultiDetailActivity;
import com.takemysaree.fonts.LatoExtraBoldTextview;
import com.takemysaree.fonts.LatoRegularTextview;
import com.takemysaree.models.MyApplication;
import com.takemysaree.models.RoundCornersTransform;
import com.takemysaree.models.RoundCornersTransform1;
import com.takemysaree.models.RoundCornersTransform2;
import com.takemysaree.models.RoundCornersTransform3;
import com.takemysaree.models.RoundCornersTransform4;
import com.takemysaree.models.RoundedTransformation;
import com.takemysaree.utils.BaseUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by think360user on 23/10/17.
 */

public class FragmentVendorsFeedbuy extends Fragment {
    private GridView rv;
    HomeGridAdapter adapter;
    private LatoRegularTextview nodata;
    public FragmentVendorsFeedbuy(){

    }
    public static FragmentVendorsFeedbuy newInstance() {


        return new FragmentVendorsFeedbuy();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_listing, container, false);


       rv = (GridView)view.findViewById(R.id.grid_view);

        nodata = (LatoRegularTextview) view.findViewById(R.id.nodata);
        Category category=new Category();
        category.addQueue();

        return view;

    }
    public class Category {

        StringRequest sr;


        public Category() {

            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.GET, BaseUrl.BASE_URL + BaseUrl.product_list +
                    "&page=1"+"&is_vendor=1"+"&pro_type=2", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {
                            nodata.setVisibility(View.GONE);
                            rv.setVisibility(View.VISIBLE);
                            ArrayList<Individualmodel> individualmodels = new ArrayList<>();

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
                                if(jsonobect1.getString("is_multi").equals("0")){
                                    individualmodel.setImage(jsonobect1.getString("image"));
                                }else{
                                    JSONArray jsonArray=jsonobect1.getJSONArray("image");
                                    ArrayList<String> image=new ArrayList<>();
                                    for(int j=0;j<jsonArray.length();j++){

                                        image.add(jsonArray.get(j).toString());
                                    }
                                    individualmodel.setImagearray(image);
                                }
                                individualmodels.add(individualmodel);
                            }

                            adapter = new HomeGridAdapter(getActivity(),individualmodels);
                            rv.setAdapter(adapter);


                        } else {

                            nodata.setVisibility(View.VISIBLE);
                            rv.setVisibility(View.GONE);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
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
        private String id;
        private String name;
        private String ads;
        private String in_stock;
        private String on_rent;
        public ArrayList<String> getImagearray() {
            return imagearray;
        }

        public void setImagearray(ArrayList<String> imagearray) {
            this.imagearray = imagearray;
        }
        private ArrayList<String> imagearray=new ArrayList<>();
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

            if (individualmodels.get(pos).getIs_multi().equals("0")) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_listdetails, null);

                ImageView imageview1 = (ImageView) view.findViewById(R.id.imageview1);
                FrameLayout fl1 = (FrameLayout) view.findViewById(R.id.fl1);
                ImageView sold = (ImageView) view.findViewById(R.id.sold);
                ImageView rented = (ImageView) view.findViewById(R.id.rented);

                LatoExtraBoldTextview ad = (LatoExtraBoldTextview) view.findViewById(R.id.ad);
                fl1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ProductDetailActivity.class);
                        intent.putExtra("productid",individualmodels.get(pos).getId());
                        mContext.startActivity(intent);

                    }
                });
                if (individualmodels.get(pos).getImage().equals("")) {
                    Picasso.with(mContext)
                            .load(R.drawable.emptyimage1)
                            .placeholder(R.drawable.emptyimage1)
                            .error(R.drawable.emptyimage1)
                            .resizeDimen(R.dimen.list_detail_image_sizes, R.dimen.list_detail_image_sizes)


                            .into(imageview1);
                } else {
                    Picasso.with(mContext)
                            .load(individualmodels.get(pos).getImage())
                            .placeholder(R.drawable.emptyimage1)
                            .error(R.drawable.emptyimage1)
                            .resizeDimen(R.dimen.list_detail_image_sizes, R.dimen.list_detail_image_sizes)


                            .into(imageview1);
                }
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
                if (individualmodels.get(pos).getAds().equals("0")) {
                    ad.setVisibility(View.GONE);
                } else {
                    ad.setVisibility(View.VISIBLE);
                }


                return view;
            } else {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_listdetailss, null);
                FrameLayout fl2 = (FrameLayout) view.findViewById(R.id.fl2);
                ImageView ig1 = (ImageView) view.findViewById(R.id.ig1);
                LinearLayout ll1=(LinearLayout)view.findViewById(R.id.ll1) ;
                LinearLayout ll3=(LinearLayout)view.findViewById(R.id.ll3) ;
                LinearLayout ll2=(LinearLayout)view.findViewById(R.id.ll2) ;
                ImageView ig2 = (ImageView) view.findViewById(R.id.ig2);
                ImageView ig31 = (ImageView) view.findViewById(R.id.ig31);
                ImageView ig3 = (ImageView) view.findViewById(R.id.ig3);
                ImageView ig4 = (ImageView) view.findViewById(R.id.ig4);
                View view1=(View)view.findViewById(R.id.vse);
                fl2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ProductMultiDetailActivity.class);
                        intent.putExtra("productid",individualmodels.get(pos).getId());
                        mContext.startActivity(intent);
                    }
                });
                if(individualmodels.get(position).getImagearray().size()==2){
                    ll2.setVisibility(View.GONE);
                    view1.setVisibility(View.VISIBLE);
                    ll1.setVisibility(View.VISIBLE);
                    ll3.setVisibility(View.VISIBLE);
                    ig31.setVisibility(View.GONE);
                    Picasso.with(mContext)
                            .load(individualmodels.get(position).getImagearray().get(0))
                            .placeholder(R.drawable.emptyimage1)
                            .error(R.drawable.emptyimage1)
                            .transform(new RoundCornersTransform3(10, 0, true, false))
                            .centerCrop()
                            .resizeDimen(R.dimen.list_detail_image_sizes2, R.dimen.list_detail_image_sizes221)
                            .into(ig4);
                    Picasso.with(mContext)
                            .load(individualmodels.get(position).getImagearray().get(1))
                            .placeholder(R.drawable.emptyimage1)
                            .error(R.drawable.emptyimage1)
                            .transform(new RoundCornersTransform2(10, 0, false, true))

                            .resizeDimen(R.dimen.list_detail_image_sizes2, R.dimen.list_detail_image_sizes221)
                            .centerCrop()

                            .into(ig3);
                }else if(individualmodels.get(position).getImagearray().size()==1){
                    ll2.setVisibility(View.GONE);
                    ll1.setVisibility(View.GONE);
                    view1.setVisibility(View.GONE);
                    ll3.setVisibility(View.GONE);
                    ig31.setVisibility(View.VISIBLE);
                    Picasso.with(mContext)
                            .load(individualmodels.get(position).getImagearray().get(0))
                            .placeholder(R.drawable.emptyimage1)
                            .error(R.drawable.emptyimage1)
                            .transform(new RoundCornersTransform4(10, 0, true, true))

                            .resizeDimen(R.dimen.list_detail_image_sizes2, R.dimen.list_detail_image_sizes)
                            .into(ig31);
                }


                else{
                    view1.setVisibility(View.VISIBLE);
                    ll1.setVisibility(View.GONE);
                    ll2.setVisibility(View.VISIBLE);
                    ig31.setVisibility(View.GONE);
                    ll3.setVisibility(View.VISIBLE);
                    Picasso.with(mContext)
                            .load(individualmodels.get(position).getImagearray().get(0))
                            .placeholder(R.drawable.emptyimage1)
                            .error(R.drawable.emptyimage1)
                            .transform(new RoundCornersTransform1(10, 1, true, false))
                            .resizeDimen(R.dimen.list_detail_image_sizes12, R.dimen.list_detail_image_sizes2212)
                            .centerCrop()
                            .into(ig1);
                    Picasso.with(mContext)
                            .load(individualmodels.get(position).getImagearray().get(1))
                            .placeholder(R.drawable.emptyimage1)
                            .error(R.drawable.emptyimage1)
                            .transform(new RoundCornersTransform(10, 2, true, false))

                            .resizeDimen(R.dimen.list_detail_image_sizes1, R.dimen.list_detail_image_sizes2212)
                            .centerCrop()
                            .into(ig2);
                    Picasso.with(mContext)
                            .load(individualmodels.get(position).getImagearray().get(2))
                            .placeholder(R.drawable.emptyimage1)
                            .error(R.drawable.emptyimage1)
                            .transform(new RoundCornersTransform2(10, 0, false, true))

                            .resizeDimen(R.dimen.list_detail_image_sizes2, R.dimen.list_detail_image_sizes221)
                            .centerCrop()
                            .into(ig3);
                }

                return view;
            }


        }
    }
}
