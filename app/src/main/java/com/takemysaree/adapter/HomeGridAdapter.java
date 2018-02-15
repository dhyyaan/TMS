package com.takemysaree.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.takemysaree.R;
import com.takemysaree.activity.ProductDetailActivity;
import com.takemysaree.fonts.LatoBoldTextview;
import com.takemysaree.fonts.LatoExtraBoldTextview;
import com.takemysaree.fonts.LatoRegularTextview;
import com.takemysaree.models.RoundCornersTransform;
import com.takemysaree.models.RoundCornersTransform1;
import com.takemysaree.models.RoundCornersTransform2;
import com.takemysaree.models.RoundedTransformation;

/**
 * Created by think360user on 6/11/17.
 */

public class HomeGridAdapter extends BaseAdapter {
    private Bitmap mBitmap, mBitmap1, mBitmap2, mBitmap3;
    float cornerRadius = 10.0f;
    private Resources mResources;
    private Context mContext;
    private RoundedBitmapDrawable roundedBitmapDrawable, roundedBitmapDrawable1, roundedBitmapDrawable2, roundedBitmapDrawable3;

    public HomeGridAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override


    public int getCount() {
        return 10;
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
        if(pos==3){
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

        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_listdetails, null);

            ImageView imageview1 = (ImageView) view.findViewById(R.id.imageview1);
            FrameLayout fl1=(FrameLayout) view.findViewById(R.id.fl1);
            ImageView sold = (ImageView) view.findViewById(R.id.sold);
            ImageView rented = (ImageView) view.findViewById(R.id.rented);

            LatoExtraBoldTextview ad=(LatoExtraBoldTextview)view.findViewById(R.id.ad);
            fl1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext, ProductDetailActivity.class);
                    mContext.startActivity(intent);
                }
            });
            if(pos==0){
                Picasso.with(mContext)
                        .load(R.drawable.n4)
                        .placeholder(R.drawable.s1)
                        .error(R.drawable.s1)
                        .transform(new RoundedTransformation(10, 0))
                        .resizeDimen(R.dimen.list_detail_image_sizes, R.dimen.list_detail_image_sizes)
                        .centerCrop()
                        .into(imageview1);
            }
          else{
                Picasso.with(mContext)
                        .load(R.drawable.n5)
                        .placeholder(R.drawable.s1)
                        .error(R.drawable.s1)
                        .transform(new RoundedTransformation(10, 0))
                        .resizeDimen(R.dimen.list_detail_image_sizes, R.dimen.list_detail_image_sizes)
                        .centerCrop()
                        .into(imageview1);
            }


            if(pos==0){

                sold.setVisibility(View.GONE);
                rented.setVisibility(View.GONE);
                ad.setVisibility(View.VISIBLE);
            }
            else if(pos==1){

                sold.setVisibility(View.GONE);
                rented.setVisibility(View.GONE);
                ad.setVisibility(View.VISIBLE);
            }
            else if(pos==2){

                sold.setVisibility(View.VISIBLE);
                rented.setVisibility(View.GONE);
                ad.setVisibility(View.GONE);
            }

            else if(pos==4){

                sold.setVisibility(View.GONE);
                rented.setVisibility(View.VISIBLE);
                ad.setVisibility(View.GONE);

            }else{

                sold.setVisibility(View.GONE);
                ad.setVisibility(View.GONE);
                rented.setVisibility(View.GONE);
            }
            return view;
        }







    }
}
