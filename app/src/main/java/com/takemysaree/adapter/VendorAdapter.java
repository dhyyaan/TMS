package com.takemysaree.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.takemysaree.R;

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.ViewHolder> {
    private Context mContext;
    private Bitmap mBitmap,mBitmap1,mBitmap2,mBitmap3;
    float cornerRadius = 10.0f;
    private Resources mResources;
   private RoundedBitmapDrawable roundedBitmapDrawable,roundedBitmapDrawable1,roundedBitmapDrawable2,roundedBitmapDrawable3;
    public VendorAdapter(Context mContext) {
        this.mContext = mContext;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_listdetails, parent, false);
        ImageView imageview1 = (ImageView) view.findViewById(R.id.imageview1);
        ImageView imageview2 = (ImageView) view.findViewById(R.id.imageview2);
        ImageView imageview3 = (ImageView) view.findViewById(R.id.imageview3);
        ImageView imageview4 = (ImageView) view.findViewById(R.id.imageview4);

        mResources = mContext.getResources();

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

                /*
                    setAntiAlias(boolean aa)
                        Enables or disables anti-aliasing for this drawable.
                */
        roundedBitmapDrawable.setAntiAlias(true);

        roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
                mResources,
                mBitmap
        );

        // Set the RoundedBitmapDrawable corners radius
        roundedBitmapDrawable.setCornerRadius(cornerRadius);

                /*
                    setAntiAlias(boolean aa)
                        Enables or disables anti-aliasing for this drawable.
                */
        roundedBitmapDrawable.setAntiAlias(true);

        roundedBitmapDrawable1 = RoundedBitmapDrawableFactory.create(
                mResources,
                mBitmap1
        );

        // Set the RoundedBitmapDrawable corners radius
        roundedBitmapDrawable1.setCornerRadius(cornerRadius);

                /*
                    setAntiAlias(boolean aa)
                        Enables or disables anti-aliasing for this drawable.
                */
        roundedBitmapDrawable1.setAntiAlias(true);

        roundedBitmapDrawable2 = RoundedBitmapDrawableFactory.create(
                mResources,
                mBitmap2
        );

        // Set the RoundedBitmapDrawable corners radius
        roundedBitmapDrawable2.setCornerRadius(cornerRadius);

                /*
                    setAntiAlias(boolean aa)
                        Enables or disables anti-aliasing for this drawable.
                */
        roundedBitmapDrawable2.setAntiAlias(true);

        roundedBitmapDrawable3 = RoundedBitmapDrawableFactory.create(
                mResources,
                mBitmap3
        );

        // Set the RoundedBitmapDrawable corners radius
        roundedBitmapDrawable3.setCornerRadius(cornerRadius);

                /*
                    setAntiAlias(boolean aa)
                        Enables or disables anti-aliasing for this drawable.
                */
        roundedBitmapDrawable3.setAntiAlias(true);

        // Set the ImageView image as drawable object
        imageview1.setImageDrawable(roundedBitmapDrawable);
        imageview1.setScaleType(ImageView.ScaleType.FIT_XY);
        imageview2.setImageDrawable(roundedBitmapDrawable1);
        imageview2.setScaleType(ImageView.ScaleType.FIT_XY);
        imageview3.setImageDrawable(roundedBitmapDrawable2);
        imageview3.setScaleType(ImageView.ScaleType.FIT_XY);
        imageview4.setImageDrawable(roundedBitmapDrawable3);
        imageview4.setScaleType(ImageView.ScaleType.FIT_XY);
    /*    Picasso.with(mContext)
                .load(com.app.utility.Constants.BASE_URL+b.image)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .transform(new RoundedTransformation(50, 4))
                .resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
                .centerCrop()
                .into(v.im_user);*/
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final int pos = position;


    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // public ImageView imageview1;
        public CheckBox chkSelected;


        public ViewHolder(View itemView) {
            super(itemView);

        }
    }


}