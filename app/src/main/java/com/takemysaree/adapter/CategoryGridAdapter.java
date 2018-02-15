package com.takemysaree.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.takemysaree.R;
import com.takemysaree.fonts.LatoBoldTextview;
import com.takemysaree.fonts.LatoExtraBoldTextview;
import com.takemysaree.models.RoundedTransformation;
import com.takemysaree.tabfragments.ProductFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by think360user on 6/11/17.
 */

public class CategoryGridAdapter extends BaseAdapter {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categroies_listdetails, null);
        final int pos = position;
        ImageView imageview1 = (ImageView) view.findViewById(R.id.imageview1);
        ImageView image2 = (ImageView) view.findViewById(R.id.image2);
        LatoExtraBoldTextview name = (LatoExtraBoldTextview) view.findViewById(R.id.name);
        name.setText(categories.get(pos));


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
}
