package com.takemysaree.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gun0912.tedpicker.ImagePickerActivity;
import com.takemysaree.R;
import com.takemysaree.fonts.LatoBoldTextview;
import com.takemysaree.fragment.FragmentMutli;
import com.takemysaree.fragment.FragmentMutli1;
import com.takemysaree.fragment.FragmentMutli2;
import com.takemysaree.fragment.FragmentMutli3;
import com.takemysaree.fragment.FragmentMutli4;
import com.takemysaree.models.RealPathUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by think360user on 3/11/17.
 */

public class MutliProductActivitytest extends AppCompatActivity  {

    ArrayList<Uri> image_uris = new ArrayList<Uri>();
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
private LinearLayout l1,l2,l3,l4,l5;
    public ViewPager viewPager;
    private LatoBoldTextview title,postproduct;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiproducttest);
        image_uris = getIntent().getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
        for (int i = 0; i < image_uris.size(); i++) {

            RealPathUtil.getPath(getApplicationContext(), Uri.parse(("file://") + image_uris.get(i)));
            try {
                Bitmap mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(("file://") + image_uris.get(i)));
                bitmaps.add(mBitmap);
                // ivProfile.setImageBitmap(mBitmap);
                // new MultipartUpload(this, mBitmap, "image").addQueue();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if(image_uris.size()==1){
            setupViewPager(viewPager);
        }else if(image_uris.size()==2){
            setupViewPager1(viewPager);
        }else if(image_uris.size()==3){
            setupViewPager2(viewPager);
        }else if(image_uris.size()==4){
            setupViewPager3(viewPager);
        }else {
            setupViewPager4(viewPager);
        }

        title=(LatoBoldTextview)findViewById(R.id.title);
        postproduct=(LatoBoldTextview)findViewById(R.id.postproduct);
        title.setText(getIntent().getStringExtra("productname"));
       l1=(LinearLayout)findViewById(R.id.l1) ;
       l2=(LinearLayout)findViewById(R.id.l2) ;
       l3=(LinearLayout)findViewById(R.id.l3) ;
       l4=(LinearLayout)findViewById(R.id.l4) ;
       l5=(LinearLayout)findViewById(R.id.l5) ;


        final FrameLayout linearLayoutOne = (FrameLayout) findViewById(R.id.ll);
        linearLayoutOne.setPadding(0,0,0,0);
        ImageView image1=(ImageView)findViewById(R.id.image1);
        ImageView image2=(ImageView)findViewById(R.id.image12);
        ImageView image3=(ImageView)findViewById(R.id.image13);
        ImageView image4=(ImageView)findViewById(R.id.image14);
        ImageView image5=(ImageView)findViewById(R.id.image15);
        final FrameLayout linearLayout2 = (FrameLayout)findViewById(R.id.ll2);

        final FrameLayout linearLayout3 = (FrameLayout) findViewById(R.id.ll3);
        final FrameLayout linearLayout4 = (FrameLayout) findViewById(R.id.ll4);
        final FrameLayout linearLayout5 = (FrameLayout) findViewById(R.id.ll5);
        //LinearLayout linearLayout2 = (LinearLayout) headerView.findViewById(R.id.ll2);
      //  LinearLayout linearLayout3 = (LinearLayout) headerView.findViewById(R.id.ll3);
if(image_uris.size()==1){
 l1.setVisibility(View.VISIBLE);
    image1.setImageBitmap(bitmaps.get(0));
}else if(image_uris.size()==2){
   l1.setVisibility(View.VISIBLE);
   l2.setVisibility(View.VISIBLE);
    image1.setImageBitmap(bitmaps.get(0));
    image2.setImageBitmap(bitmaps.get(1));
}else if(image_uris.size()==3){
  l1.setVisibility(View.VISIBLE);
  l2.setVisibility(View.VISIBLE);
  l3.setVisibility(View.VISIBLE);
    image1.setImageBitmap(bitmaps.get(0));
    image2.setImageBitmap(bitmaps.get(1));
    image3.setImageBitmap(bitmaps.get(2));
}else if(image_uris.size()==4){
    l1.setVisibility(View.VISIBLE);
    l2.setVisibility(View.VISIBLE);
    l3.setVisibility(View.VISIBLE);
    l4.setVisibility(View.VISIBLE);
    image1.setImageBitmap(bitmaps.get(0));
    image2.setImageBitmap(bitmaps.get(1));
    image3.setImageBitmap(bitmaps.get(2));
    image4.setImageBitmap(bitmaps.get(3));
}else{
    l1.setVisibility(View.VISIBLE);
    l2.setVisibility(View.VISIBLE);
    l3.setVisibility(View.VISIBLE);
    l4.setVisibility(View.VISIBLE);
    l5.setVisibility(View.VISIBLE);
    image1.setImageBitmap(bitmaps.get(0));
    image2.setImageBitmap(bitmaps.get(1));
    image3.setImageBitmap(bitmaps.get(2));
    image4.setImageBitmap(bitmaps.get(3));
    image5.setImageBitmap(bitmaps.get(4));

}

    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentMutli(), "ONE");

        viewPager.setAdapter(adapter);
    }
    private void setupViewPager1(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentMutli(), "ONE");
        adapter.addFragment(new FragmentMutli1(), "TWO");

        viewPager.setAdapter(adapter);
    }
    private void setupViewPager2(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentMutli(), "ONE");
        adapter.addFragment(new FragmentMutli1(), "TWO");
        adapter.addFragment(new FragmentMutli2(), "TWO");

        viewPager.setAdapter(adapter);
    }
    private void setupViewPager3(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentMutli(), "ONE");
        adapter.addFragment(new FragmentMutli1(), "TWO");
        adapter.addFragment(new FragmentMutli2(), "TWO");
        adapter.addFragment(new FragmentMutli3(), "TWO");

        viewPager.setAdapter(adapter);
    }
    private void setupViewPager4(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentMutli(), "ONE");
        adapter.addFragment(new FragmentMutli1(), "TWO");
        adapter.addFragment(new FragmentMutli2(), "TWO");
        adapter.addFragment(new FragmentMutli3(), "TWO");
        adapter.addFragment(new FragmentMutli4(), "TWO");

        viewPager.setAdapter(adapter);
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
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
}

