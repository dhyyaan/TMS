package com.takemysaree.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.takemysaree.models.CustomViewPager;
import com.takemysaree.models.RealPathUtil;
import com.takemysaree.tabfragments.HomeFragment;
import com.takemysaree.utils.BaseUrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by think360user on 3/11/17.
 */

public class MutliProductActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    ArrayList<Uri> image_uris = new ArrayList<Uri>();
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    private ImageView ivHamburger;
   public static ArrayList<String> productname = new ArrayList<>();
    public static ArrayList<String> categoryid = new ArrayList<>();
    public static ArrayList<String> sizes = new ArrayList<>();
    public static ArrayList<String> retailprice = new ArrayList<>();
    public static ArrayList<String> rentprice = new ArrayList<>();
    public CustomViewPager viewPager;
    public  static  String user_id;
    private LatoBoldTextview title, postproduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiproduct);
        image_uris = getIntent().getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
        SharedPreferences prefs = getSharedPreferences(BaseUrl.prfs, Context.MODE_PRIVATE);
        user_id = prefs.getString("userid", "");
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
        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        viewPager.setPagingEnabled(false);
        if (image_uris.size() == 1) {
            setupViewPager(viewPager);
        } else if (image_uris.size() == 2) {
            setupViewPager1(viewPager);
        } else if (image_uris.size() == 3) {
            setupViewPager2(viewPager);
        } else if (image_uris.size() == 4) {
            setupViewPager3(viewPager);
        } else {
            setupViewPager4(viewPager);
        }
        ivHamburger = (ImageView) findViewById(R.id.ivHamburger);
        ivHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MutliProductActivity.this);
                builder1.setMessage("Are you sure you do not want  to add this product?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(MutliProductActivity.this, DashBoardActivity.class);
                                startActivity(intent);
                                finish();
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        title = (LatoBoldTextview) findViewById(R.id.title);
        postproduct = (LatoBoldTextview) findViewById(R.id.postproduct);
        title.setText(getIntent().getStringExtra("productname"));
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        View headerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.activity_custom, null, false);

        final FrameLayout linearLayoutOne = (FrameLayout) headerView.findViewById(R.id.ll);
        linearLayoutOne.setPadding(0, 0, 0, 0);
        ImageView image1 = (ImageView) headerView.findViewById(R.id.image1);
        ImageView image2 = (ImageView) headerView.findViewById(R.id.image2);
        ImageView image3 = (ImageView) headerView.findViewById(R.id.image3);
        ImageView image4 = (ImageView) headerView.findViewById(R.id.image4);
        ImageView image5 = (ImageView) headerView.findViewById(R.id.image5);
        final FrameLayout linearLayout2 = (FrameLayout) headerView.findViewById(R.id.ll1);
        linearLayout2.setPadding(0, 0, 0, 0);
        final FrameLayout linearLayout3 = (FrameLayout) headerView.findViewById(R.id.ll2);
        final FrameLayout linearLayout4 = (FrameLayout) headerView.findViewById(R.id.ll3);
        final FrameLayout linearLayout5 = (FrameLayout) headerView.findViewById(R.id.ll4);
        //LinearLayout linearLayout2 = (LinearLayout) headerView.findViewById(R.id.ll2);
        //  LinearLayout linearLayout3 = (LinearLayout) headerView.findViewById(R.id.ll3);
        if (image_uris.size() == 1) {
            tabLayout.getTabAt(0).setCustomView(linearLayoutOne);
            image1.setImageBitmap(bitmaps.get(0));
        } else if (image_uris.size() == 2) {
            tabLayout.getTabAt(0).setCustomView(linearLayoutOne);
            tabLayout.getTabAt(1).setCustomView(linearLayout2);
            image1.setImageBitmap(bitmaps.get(0));
            image2.setImageBitmap(bitmaps.get(1));
        } else if (image_uris.size() == 3) {
            tabLayout.getTabAt(0).setCustomView(linearLayoutOne);
            tabLayout.getTabAt(1).setCustomView(linearLayout2);
            tabLayout.getTabAt(2).setCustomView(linearLayout3);
            image1.setImageBitmap(bitmaps.get(0));
            image2.setImageBitmap(bitmaps.get(1));
            image3.setImageBitmap(bitmaps.get(2));
        } else if (image_uris.size() == 4) {
            tabLayout.getTabAt(0).setCustomView(linearLayoutOne);
            tabLayout.getTabAt(1).setCustomView(linearLayout2);
            tabLayout.getTabAt(2).setCustomView(linearLayout3);
            tabLayout.getTabAt(3).setCustomView(linearLayout4);
            image1.setImageBitmap(bitmaps.get(0));
            image2.setImageBitmap(bitmaps.get(1));
            image3.setImageBitmap(bitmaps.get(2));
            image4.setImageBitmap(bitmaps.get(3));
        } else {
            tabLayout.getTabAt(0).setCustomView(linearLayoutOne);
            tabLayout.getTabAt(1).setCustomView(linearLayout2);
            tabLayout.getTabAt(2).setCustomView(linearLayout3);
            tabLayout.getTabAt(3).setCustomView(linearLayout4);
            tabLayout.getTabAt(4).setCustomView(linearLayout5);
            image1.setImageBitmap(bitmaps.get(0));
            image2.setImageBitmap(bitmaps.get(1));
            image3.setImageBitmap(bitmaps.get(2));
            image4.setImageBitmap(bitmaps.get(3));
            image5.setImageBitmap(bitmaps.get(4));

        }
       /* LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(2));
        tabStrip.setEnabled(false);
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setClickable(false);
        }

        postproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
        // tabLayout.clearOnTabSelectedListeners();
       /* tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

             *//*   if (tabLayout.getSelectedTabPosition() == 0) {
                    linearLayoutOne.setAlpha(1f);
                    linearLayout2.setAlpha(.4f);
                    linearLayout3.setAlpha(.4f);
                    linearLayout4.setAlpha(.4f);
                    linearLayout5.setAlpha(.4f);

                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    linearLayout2.setAlpha(1f);
                    linearLayoutOne.setAlpha(.4f);
                    linearLayout3.setAlpha(.4f);
                    linearLayout4.setAlpha(.4f);
                    linearLayout5.setAlpha(.4f);

                } else if (tabLayout.getSelectedTabPosition() == 2) {
                    linearLayout3.setAlpha(1f);
                    linearLayout2.setAlpha(.4f);
                    linearLayoutOne.setAlpha(.4f);
                    linearLayout4.setAlpha(.4f);
                    linearLayout5.setAlpha(.4f);

                } else if (tabLayout.getSelectedTabPosition() == 3) {
                    linearLayout4.setAlpha(1f);
                    linearLayout2.setAlpha(.4f);
                    linearLayout3.setAlpha(.4f);
                    linearLayoutOne.setAlpha(.4f);
                    linearLayout5.setAlpha(.4f);

                } else if (tabLayout.getSelectedTabPosition() == 4) {
                    linearLayout5.setAlpha(1f);
                    linearLayout2.setAlpha(.4f);
                    linearLayout2.setAlpha(.4f);
                    linearLayout3.setAlpha(.4f);
                    linearLayout4.setAlpha(.4f);
                    linearLayoutOne.setAlpha(.4f);

                }
*//*
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });*/

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MutliProductActivity.this);
        builder1.setMessage("Are you sure you do not want  to add this product?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(MutliProductActivity.this, DashBoardActivity.class);
                        startActivity(intent);
                        finish();
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}

