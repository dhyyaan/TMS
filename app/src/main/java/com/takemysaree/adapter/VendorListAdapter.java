package com.takemysaree.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.takemysaree.R;
import com.takemysaree.models.RoundedTransformation;
import com.takemysaree.tabfragments.VendorFragment;

import java.util.ArrayList;

/**
 * Created by think360user on 6/11/17.
 */

public class VendorListAdapter extends BaseAdapter {
    private Bitmap mBitmap,mBitmap1,mBitmap2,mBitmap3;
    float cornerRadius = 10.0f;
    private Resources mResources;
    private Context mContext;
    private ArrayList<VendorFragment.Vendormodel> vendormodelArrayList;
    private RoundedBitmapDrawable roundedBitmapDrawable,roundedBitmapDrawable1,roundedBitmapDrawable2,roundedBitmapDrawable3;
    public  VendorListAdapter(Context mContext, ArrayList<VendorFragment.Vendormodel> vendormodelArrayList){
        this.mContext = mContext;
        this.vendormodelArrayList=vendormodelArrayList;
    }


    @Override


    public int getCount() {
        return vendormodelArrayList.size();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_listdetails, null);
        final int pos = position;
        ImageView imageview1 = (ImageView) view.findViewById(R.id.imageview1);
        ImageView imageview2 = (ImageView) view.findViewById(R.id.imageview2);
        ImageView imageview3 = (ImageView) view.findViewById(R.id.imageview3);
        ImageView imageview4 = (ImageView) view.findViewById(R.id.imageview4);
    Picasso.with(mContext)
                .load(R.drawable.s1)
                .placeholder(R.drawable.s1)
                .error(R.drawable.s1)
                .transform(new RoundedTransformation(10, 4))
            .resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
                .centerCrop()
                .into(imageview1);
        Picasso.with(mContext)
                .load(R.drawable.s1)
                .placeholder(R.drawable.s1)
                .error(R.drawable.s1)
                .transform(new RoundedTransformation(10, 4))
                .resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
                .centerCrop()
                .into(imageview2);
        Picasso.with(mContext)
                .load(R.drawable.s1)
                .placeholder(R.drawable.s1)
                .error(R.drawable.s1)
                .transform(new RoundedTransformation(10, 4))
                .resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
                .centerCrop()
                .into(imageview3);
        Picasso.with(mContext)
                .load(R.drawable.s1)
                .placeholder(R.drawable.s1)
                .error(R.drawable.s1)
                .transform(new RoundedTransformation(10, 4))
                .resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
                .centerCrop()
                .into(imageview4);
       /* mResources = mContext.getResources();

        mBitmap = BitmapFactory.decodeResource(mResources, R.drawable.s1);
        mBitmap1 = BitmapFactory.decodeResource(mResources, R.drawable.s2);
        mBitmap2 = BitmapFactory.decodeResource(mResources, R.drawable.s3);
        mBitmap3 = BitmapFactory.decodeResource(mResources, R.drawable.s1);

        roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
                mResources,
                mBitmap
        );

        // Set the RoundedBitmapDrawable corners radius
        roundedBitmapDrawable.setCornerRadius(cornerRadius);

                *//*
                    setAntiAlias(boolean aa)
                        Enables or disables anti-aliasing for this drawable.
                *//*
        roundedBitmapDrawable.setAntiAlias(true);

        roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
                mResources,
                mBitmap
        );

        // Set the RoundedBitmapDrawable corners radius
        roundedBitmapDrawable.setCornerRadius(cornerRadius);

                *//*
                    setAntiAlias(boolean aa)
                        Enables or disables anti-aliasing for this drawable.
                *//*
        roundedBitmapDrawable.setAntiAlias(true);

        roundedBitmapDrawable1 = RoundedBitmapDrawableFactory.create(
                mResources,
                mBitmap1
        );

        // Set the RoundedBitmapDrawable corners radius
        roundedBitmapDrawable1.setCornerRadius(cornerRadius);

                *//*
                    setAntiAlias(boolean aa)
                        Enables or disables anti-aliasing for this drawable.
                *//*
        roundedBitmapDrawable1.setAntiAlias(true);

        roundedBitmapDrawable2 = RoundedBitmapDrawableFactory.create(
                mResources,
                mBitmap2
        );

        // Set the RoundedBitmapDrawable corners radius
        roundedBitmapDrawable2.setCornerRadius(cornerRadius);

                *//*
                    setAntiAlias(boolean aa)
                        Enables or disables anti-aliasing for this drawable.
                *//*
        roundedBitmapDrawable2.setAntiAlias(true);

        roundedBitmapDrawable3 = RoundedBitmapDrawableFactory.create(
                mResources,
                mBitmap3
        );

        // Set the RoundedBitmapDrawable corners radius
        roundedBitmapDrawable3.setCornerRadius(cornerRadius);

                *//*
                    setAntiAlias(boolean aa)
                        Enables or disables anti-aliasing for this drawable.
                *//*
        roundedBitmapDrawable3.setAntiAlias(true);

        // Set the ImageView image as drawable object
        imageview1.setImageDrawable(roundedBitmapDrawable);
        imageview1.setScaleType(ImageView.ScaleType.FIT_XY);
        imageview2.setImageDrawable(roundedBitmapDrawable1);
        imageview2.setScaleType(ImageView.ScaleType.FIT_XY);
        imageview3.setImageDrawable(roundedBitmapDrawable2);
        imageview3.setScaleType(ImageView.ScaleType.FIT_XY);
        imageview4.setImageDrawable(roundedBitmapDrawable3);
        imageview4.setScaleType(ImageView.ScaleType.FIT_XY);*/
        return view;

    }
}
