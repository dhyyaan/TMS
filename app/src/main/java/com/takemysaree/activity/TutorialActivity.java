package com.takemysaree.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.takemysaree.R;
import com.takemysaree.adapter.ViewPagerAdapter;
import com.takemysaree.fonts.LatoBoldTextview;
import com.takemysaree.fonts.LatoRegularTextview;
import com.takemysaree.viewpager.AutoScrollViewPager;
import com.vistrav.ask.Ask;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by think360user on 18/4/17.
 */

public class TutorialActivity extends Activity implements View.OnClickListener{
    private CircleIndicator indicator_default;
    private AutoScrollViewPager view_pager;
private LatoRegularTextview next;
    private LinearLayout letsstart;
    int m=0;
private LatoBoldTextview skip;
    private FrameLayout introbanner;


    private int[] mImageResources = {
            R.drawable.intro,
            R.drawable.intro3,
            R.drawable.intro2,
            R.drawable.intro1
    };
    private String[] title = {
            "START SELLING NOW AND MAKE MONEY",
            "NEGOTIATE WITH BUYERS AND SELLERS",
            "FIND NEW AND USED ITEMS",
            "BECOME AN EXCLUSIVE VENDOR",

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screenbanner);

        initViews();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initListeners();

    }
    @Override
    protected void onPause() {
        super.onPause();

    }
    private void initViews() {
        introbanner=(FrameLayout) findViewById(R.id.introbanner);
        skip=(LatoBoldTextview)findViewById(R.id.skip);
        next=(LatoRegularTextview)findViewById(R.id.next);
        view_pager = (AutoScrollViewPager) findViewById(R.id.view_pager);
        indicator_default = (CircleIndicator) findViewById(R.id.indicator_default);
        view_pager.setAdapter(new ViewPagerAdapter(TutorialActivity.this, mImageResources, title,view_pager));
       // view_pager.startAutoScroll(10000);
        indicator_default.setViewPager(view_pager);

    }
    private void initListeners() {
        skip.setOnClickListener(this);
        next.setOnClickListener(this);
        view_pager.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view_pager:

               /* Intent intents=new Intent(TutorialActivity.this,ActDemo.class);
                startActivity(intents);*/
                break;
            case R.id.skip:

                Ask.on(this)
                        .forPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        //optional
                        .go();
                Intent intent=new Intent(TutorialActivity.this,LoginActivityforsocials.class);
                startActivity(intent);
                finish();
                break;
            case R.id.next:

                Ask.on(this)
                        .forPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        //optional
                        .go();
                m=view_pager.getCurrentItem();
                if(m==mImageResources.length-1){
                    Intent intenta=new Intent(TutorialActivity.this,Activity_LoginRegister.class);
                    startActivity(intenta);
                    finish();
                }else{
                    view_pager.setCurrentItem(view_pager.getCurrentItem()+1);
                }
                break;

        }
    }
}
