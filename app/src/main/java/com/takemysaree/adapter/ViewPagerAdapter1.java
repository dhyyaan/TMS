package com.takemysaree.adapter;

/**
 * Created by think360user on 18/4/17.
 */

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.takemysaree.R;
import com.takemysaree.viewpager.AutoScrollViewPager;

import java.util.ArrayList;


/**
 * Created by Wasim on 11-06-2015.
 */
public class ViewPagerAdapter1 extends PagerAdapter {

    private Activity mContext;
   private ArrayList<String> arrayList=new
           ArrayList<>();
    public ViewPagerAdapter1(Activity mContext, ArrayList<String> imagearray) {
        this.mContext = mContext;
       arrayList=imagearray;
    }
   @Override
    public float getPageWidth(int position) {
       return(0.5f);
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item, container, false);

        ImageView img_pager_item = (ImageView) itemView.findViewById(R.id.imageView);
        Picasso.with(mContext).
                load(arrayList.get(position))
                .error(R.drawable.intro)
                .placeholder(R.drawable.intro)
                .into(img_pager_item);
        container.addView(itemView);



        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}