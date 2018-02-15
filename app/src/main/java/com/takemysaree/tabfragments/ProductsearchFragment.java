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
import com.takemysaree.activity.ProductDetailActivity;
import com.takemysaree.fonts.LatoExtraBoldTextview;
import com.takemysaree.fonts.LatoRegularTextview;
import com.takemysaree.models.Individualmodels;
import com.takemysaree.models.MyApplication;
import com.takemysaree.models.RoundCornersTransform;
import com.takemysaree.models.RoundCornersTransform1;
import com.takemysaree.models.RoundCornersTransform2;
import com.takemysaree.utils.BaseUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by think360user on 20/4/17.
 */

public class ProductsearchFragment extends Fragment {
    private TextView tvtitle;
    private LatoRegularTextview nodata;
    HomeGridAdapter adapter;
    private GridView rv;
    List<String> categories = new ArrayList<>();
    List<String> ids = new ArrayList<>();
    Bundle b;
    List<String> images = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.products_listing, container, false);
        tvtitle=(TextView)getActivity().findViewById(R.id.tvtitle);
        rv = (GridView)view.findViewById(R.id.grid_view);
        nodata = (LatoRegularTextview) view.findViewById(R.id.nodata);
        b = getArguments();
        ArrayList<Individualmodels> myList = (ArrayList<Individualmodels>) getActivity().getIntent().getSerializableExtra("mylist");
        adapter = new HomeGridAdapter(getActivity(),myList);
        rv.setAdapter(adapter);
        tvtitle.setText("Product Listing");
        return view;
    }

    public class HomeGridAdapter extends BaseAdapter {
        private Bitmap mBitmap, mBitmap1, mBitmap2, mBitmap3;
        float cornerRadius = 10.0f;
        private Resources mResources;
        private Context mContext;
        private RoundedBitmapDrawable roundedBitmapDrawable, roundedBitmapDrawable1, roundedBitmapDrawable2, roundedBitmapDrawable3;
        private ArrayList<Individualmodels> individualmodels;
        public HomeGridAdapter(Context mContext, ArrayList<Individualmodels> individualmodels) {
            this.mContext = mContext;
            this.individualmodels=individualmodels;
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
            ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width=dm.widthPixels;
            int height=dm.heightPixels;
            int dens=dm.densityDpi;
            double wi=(double)width/(double)dens;
            double hi=(double)height/(double)dens;
            double x = Math.pow(wi,2);
            double y = Math.pow(hi,2);
            double screenInches = Math.sqrt(x+y);
            final int pos = position;

            if(individualmodels.get(pos).getIs_multi().equals("0")){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_listdetails, null);

                ImageView imageview1 = (ImageView) view.findViewById(R.id.imageview1);
                FrameLayout fl1=(FrameLayout) view.findViewById(R.id.fl1);
                ImageView sold = (ImageView) view.findViewById(R.id.sold);
                ImageView rented = (ImageView) view.findViewById(R.id.rented);

                LatoExtraBoldTextview ad=(LatoExtraBoldTextview)view.findViewById(R.id.ad);
                fl1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ProductDetailActivity.class);
                        intent.putExtra("productid",individualmodels.get(pos).getId());
                        mContext.startActivity(intent);
                    }
                });
                if(individualmodels.get(pos).getImage().equals("")){
                    Picasso.with(mContext)
                            .load(R.drawable.emptyimage1)
                            .placeholder(R.drawable.emptyimage1)
                            .error(R.drawable.emptyimage1)

                            .resizeDimen(R.dimen.list_detail_image_sizes, R.dimen.list_detail_image_sizes)
                            .into(imageview1);
                }else{
                    Picasso.with(mContext)
                            .load(individualmodels.get(pos).getImage())
                            .placeholder(R.drawable.emptyimage1)
                            .error(R.drawable.emptyimage1)

                            .resizeDimen(R.dimen.list_detail_image_sizes, R.dimen.list_detail_image_sizes)
                            .into(imageview1);
                }
                if(individualmodels.get(pos).getIn_stock().equals("0")){
                    sold.setVisibility(View.VISIBLE);
                }else{
                    sold.setVisibility(View.GONE);
                }
                if(individualmodels.get(pos).getOn_rent().equals("0")){
                    rented.setVisibility(View.GONE);
                }else{
                    rented.setVisibility(View.VISIBLE);
                }
                if(individualmodels.get(pos).getAds().equals("0")){
                    ad.setVisibility(View.GONE);
                }else{
                    ad.setVisibility(View.VISIBLE);
                }


                return view;
            }else{
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_listdetailss, null);

                ImageView ig1 = (ImageView) view.findViewById(R.id.ig1);
                ImageView ig2 = (ImageView) view.findViewById(R.id.ig2);
                ImageView ig3 = (ImageView) view.findViewById(R.id.ig3);
                Picasso.with(mContext)
                        .load(R.drawable.n1)
                        .placeholder(R.drawable.s1)
                        .error(R.drawable.s1)
                        .transform(new RoundCornersTransform1(10, 1,true,false))
                        .resizeDimen(R.dimen.list_detail_image_sizes12, R.dimen.list_detail_image_sizes4)
                        .centerCrop()
                        .into(ig1);
                Picasso.with(mContext)
                        .load(R.drawable.n2)
                        .placeholder(R.drawable.s1)
                        .error(R.drawable.s1)
                        .transform(new RoundCornersTransform(10, 2,true,false))

                        .resizeDimen(R.dimen.list_detail_image_sizes1, R.dimen.list_detail_image_sizes4)
                        .centerCrop()
                        .into(ig2);
                Picasso.with(mContext)
                        .load(R.drawable.n3)
                        .placeholder(R.drawable.s1)
                        .error(R.drawable.s1)
                        .transform(new RoundCornersTransform2(10, 0,false,true))

                        .resizeDimen(R.dimen.list_detail_image_sizes2, R.dimen.list_detail_image_sizes22)
                        .centerCrop()
                        .into(ig3);
                return view;
            }








        }
    }
  /*  public class CategoryGridAdapter extends BaseAdapter {
        private Bitmap mBitmap, mBitmap1, mBitmap2, mBitmap3;
        float cornerRadius = 10.0f;
        private Resources mResources;
        private Context mContext;
        private RoundedBitmapDrawable roundedBitmapDrawable, roundedBitmapDrawable1, roundedBitmapDrawable2, roundedBitmapDrawable3;
        List<String> categories = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        List<String> images = new ArrayList<>();

        public CategoryGridAdapter(Context mContext, List<String> categories, List<String> ids,
                                   List<String> images) {
            this.mContext = mContext;
            this.categories = categories;
            this.ids = ids;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_listdetails, null);
            final int pos = position;
            ImageView imageview1 = (ImageView) view.findViewById(R.id.imageview1);
            RelativeLayout relclick=(RelativeLayout)view.findViewById(R.id.relclick);
            relclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProductDetailActivity.class);
                    intent.putExtra("productid",ids.get(pos));
                    mContext.startActivity(intent);
                }
            });




            if(images.get(pos).equals("")){
                Picasso.with(mContext)
                        .load(R.drawable.emptyimage1)
                        .placeholder(R.drawable.emptyimage1)
                        .error(R.drawable.emptyimage1)
                        .resizeDimen(R.dimen.list_detail_image_sizes, R.dimen.list_detail_image_sizes)
                        .centerCrop()
                        .into(imageview1);

            }else{
                Picasso.with(mContext)
                        .load(images.get(pos))
                        .placeholder(R.drawable.emptyimage1)
                        .error(R.drawable.emptyimage1)
                        .resizeDimen(R.dimen.list_detail_image_sizes, R.dimen.list_detail_image_sizes)
                        .centerCrop()
                        .into(imageview1);

            }


            return view;

        }
    }*/

}
