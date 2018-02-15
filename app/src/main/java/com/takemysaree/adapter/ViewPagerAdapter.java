package com.takemysaree.adapter;

/**
 * Created by think360user on 18/4/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.takemysaree.R;
import com.takemysaree.activity.ActDemo;
import com.takemysaree.activity.TutorialActivity;
import com.takemysaree.viewpager.AutoScrollViewPager;


/**
 * Created by Wasim on 11-06-2015.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private Activity mContext;
    private int[] mResources;
    private AutoScrollViewPager viewPager;
    private String[] title;
    public ViewPagerAdapter(Activity mContext, int[] mResources, String[] title, AutoScrollViewPager viewPager) {
        this.mContext = mContext;
        this.mResources = mResources;
        this.viewPager=viewPager;
        this.title=title;
    }
  /*  @Override
    public float getPageWidth(int position) {
        return position;
    }*/
    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.tutorialadapter, container, false);

        ImageView img_pager_item = (ImageView) itemView.findViewById(R.id.img_pager_item);
        TextView text_pager_item = (TextView) itemView.findViewById(R.id.text_pager_item);
        img_pager_item.setImageResource(mResources[position]);
        text_pager_item.setText(title[position]);
       /* img_pager_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intents=new Intent(mContext,ActDemo.class);
                mContext.startActivity(intents);
            }
        });*/
      /*  viewPager.setAutoScrollDurationFactor(30);
        viewPager.setInterval(100);
        viewPager.setStopScrollWhenTouch(false);
        viewPager.setDirection(1);
        viewPager.setCycle(true);
        viewPager.setSlideBorderMode(2);*/
        container.addView(itemView);



        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}