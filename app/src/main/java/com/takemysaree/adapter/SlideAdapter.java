package com.takemysaree.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.takemysaree.R;
import com.takemysaree.fonts.LatoRegularTextview;


public class SlideAdapter extends BaseAdapter {
    int Images[];
    String[] title;
    String id;
    LayoutInflater inflater;
    Activity ctx;
    DrawerLayout v;


    int c;
    SlideAdapterHolder slideAdapterHolder;

    public SlideAdapter(Activity context, DrawerLayout view, String[] titles) {
        ctx = context;

        v = view;
        title = titles;
        slideAdapterHolder = new SlideAdapterHolder();
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return title.length;
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_slide_panel, null);
            slideAdapterHolder.tvTitle = (LatoRegularTextview) convertView.findViewById(R.id.tvTitle);



                slideAdapterHolder.tvTitle.setText(title[position]);


        }
        return convertView;
    }
    public class SlideAdapterHolder
    {
        LatoRegularTextview tvTitle;
        TextView tvNumberOfItems;
        ImageView ivProfile;
    }
}