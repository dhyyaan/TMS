package com.takemysaree.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.takemysaree.R;
import com.takemysaree.fonts.LatoExtraBoldTextview;
import com.takemysaree.fonts.LatoRegularTextview;
import com.takemysaree.models.RoundedTransformation;

/**
 * Created by think360user on 6/11/17.
 */

public class HomeGridAdapterrent extends BaseAdapter {
    private Bitmap mBitmap, mBitmap1, mBitmap2, mBitmap3;
    float cornerRadius = 10.0f;
    private Resources mResources;
    private Context mContext;
    private RoundedBitmapDrawable roundedBitmapDrawable, roundedBitmapDrawable1, roundedBitmapDrawable2, roundedBitmapDrawable3;

    public HomeGridAdapterrent(Context mContext) {
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_listdetails, null);

            ImageView imageview1 = (ImageView) view.findViewById(R.id.imageview1);

            ImageView sold = (ImageView) view.findViewById(R.id.sold);
            ImageView rented = (ImageView) view.findViewById(R.id.rented);
        LatoExtraBoldTextview ad=(LatoExtraBoldTextview)view.findViewById(R.id.ad);
            Picasso.with(mContext)
                    .load(R.drawable.s1)
                    .placeholder(R.drawable.s1)
                    .error(R.drawable.s1)
                    .transform(new RoundedTransformation(10, 0))
                    .resizeDimen(R.dimen.list_detail_image_sizes, R.dimen.list_detail_image_sizes)
                    .centerCrop()
                    .into(imageview1);


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
