package com.takemysaree.tabfragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.takemysaree.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by think360user on 20/4/17.
 */

public class HomeFragment extends Fragment {
    private TextView tvtitle;
    private TabLayout allTabs;
    private ViewPager viewPager;
    ViewPagerAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_avail, container, false);
        tvtitle=(TextView)getActivity().findViewById(R.id.tvtitle);
        tvtitle.setText("Home");
        initViews(view);

        return view;
    }

    private void initViews(View v) {
        allTabs = (TabLayout) v.findViewById(R.id.tabs);
        viewPager = (ViewPager)v. findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        allTabs.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
        allTabs.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));
        //allTabs.setSelectedTabIndicatorColor(Color.parseColor("#5ec3b5"));
        allTabs.setTabTextColors(Color.parseColor("#FF57D7CA"), Color.parseColor("#ffffff"));


    }
    private void setupViewPager(ViewPager viewPager) {

        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager(),2));
    }
    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    private class PagerAdapter extends FragmentPagerAdapter {
        int mNumOfTabs;
        private String[] titles= new String[]{"Vendors", "Exclusive"};
        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:

                    return FragmentIndividualFeed.newInstance();

                case 1:

                    return FragmentVendorsFeed.newInstance();

                default:
                    return null;
            }
        }
        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return  titles[position];
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
