package com.takemysaree.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.squareup.picasso.Picasso;
import com.takemysaree.R;
import com.takemysaree.adapter.ProductDetailadapter;
import com.takemysaree.fonts.LatoBoldTextview;
import com.takemysaree.fonts.LatoExtraBoldTextview;
import com.takemysaree.fonts.LatoNormalEditText;
import com.takemysaree.fonts.LatoRegularTextview;
import com.takemysaree.models.AutoScrollViewPager;
import com.takemysaree.models.GPSTracker;
import com.takemysaree.models.MyApplication;
import com.takemysaree.utils.BaseUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;

public class ProductMultiDetailActivity extends AppCompatActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;
    private ProductDetailadapter productDetailadapter;
    private int[] mImageResources = {
            R.drawable.n1,
            R.drawable.n2,
            R.drawable.n4,

    };
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    private GPSTracker gps;
    private double latitude, longitude;
    private String pickupLat, pickupLng;
    private AutoScrollViewPager mPagers;
    private String fromdate;
    LatoBoldTextview totalprice, totalprices;
    LinearLayout rentl;
    LinearLayout buyl;
    int valueforpos = 0;
    int valueforrent = 0;
    LatoRegularTextview slectfromdate, selecttodate;
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();
    private ArrayList<String> price_retails = new ArrayList<>();
    private ArrayList<String> price_rents = new ArrayList<>();
    private ArrayList<String> cmnts = new ArrayList<>();
    private ArrayList<String> imageuser = new ArrayList<>();
    private ArrayList<String> user_name = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    private LatoBoldTextview pdname;

    private LatoRegularTextview tvSariTitle, tvSariPrice, tvNoOfComments;
    private ImageView ivHamburger, cmntsend;
    private RecyclerView mRecyclerView;
    private String page = "2";
    private LatoNormalEditText tvcmnt;
    private AppCompatRatingBar ratingBar;
    private SharedPreferences sharedpreferences;
    private String userid;
    private Calendar calendar;
    private int year, month, day;
    private CircleIndicator indicator_default;
    private LinearLayout morecmnts, addtowshlist;
    private LinearLayout buylyt, rentlyt;
    private ImageView ivSave, ivunsave;
    private LatoBoldTextview buytext, renttext;
    private RelativeLayout rell;
    private String priceall;
    private TextView moreinfo;
    private LatoRegularTextview tvSariView, tvSariColor;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    // private PagerAdapter mPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_multidetail);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        rell = (RelativeLayout) findViewById(R.id.rell);
        rell.setBackgroundColor(Color.parseColor("#ffffff"));
        rell.setAlpha(1f);
        indicator_default = (CircleIndicator) findViewById(R.id.indicator_default);
        sharedpreferences = getApplicationContext().getSharedPreferences(BaseUrl.prfs, Context.MODE_PRIVATE);
        userid = sharedpreferences.getString("userid", "");
        ratingBar = (AppCompatRatingBar) findViewById(R.id.ratingBar);
        ivHamburger = (ImageView) findViewById(R.id.ivHamburger);
        moreinfo=(TextView)findViewById(R.id.moreinfo);
        ivSave = (ImageView) findViewById(R.id.ivSave);
        ivunsave = (ImageView) findViewById(R.id.ivunsave);
        morecmnts = (LinearLayout) findViewById(R.id.morecmnts);
        rentlyt = (LinearLayout) findViewById(R.id.rentlyt);
        buylyt = (LinearLayout) findViewById(R.id.buylyt);
        addtowshlist = (LinearLayout) findViewById(R.id.addtowshlist);
        cmntsend = (ImageView) findViewById(R.id.cmntsend);
        ivHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        pdname = (LatoBoldTextview) findViewById(R.id.pdname);
        buytext = (LatoBoldTextview) findViewById(R.id.buytext);
        renttext = (LatoBoldTextview) findViewById(R.id.renttext);
        tvcmnt = (LatoNormalEditText) findViewById(R.id.tvcmnt);
        tvSariTitle = (LatoRegularTextview) findViewById(R.id.tvSariTitle);
        tvNoOfComments = (LatoRegularTextview) findViewById(R.id.tvNoOfComments);
        tvSariPrice = (LatoRegularTextview) findViewById(R.id.tvSariPrice);
        tvSariView = (LatoRegularTextview) findViewById(R.id.tvSariView);
       // moreinfo = (LatoExtraBoldTextview) findViewById(R.id.moreinfo);
        tvSariColor = (LatoRegularTextview) findViewById(R.id.tvSariColor);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        getCurrentLocation();

        cmntsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvcmnt.getText().toString().equals("")) {
                    tvcmnt.setError("Enter Comment");

                } else {
                    AddComment addComment = new AddComment();
                    addComment.addQueue();
                }
            }
        });
        morecmnts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentList commentList = new CommentList();
                commentList.addQueue();

            }
        });
        ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddWishList addWishList = new AddWishList();
                addWishList.addQueue();

            }
        });
        ivunsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveWishlist removeWishlist = new RemoveWishlist();
                removeWishlist.addQueue();

            }
        });


    }


    @Override
    public void onBackPressed() {

        finish();
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    public class ViewPagerAdapter extends PagerAdapter {

        private Activity mContext;
        private ArrayList<String> mResources = new ArrayList<>();

        public ViewPagerAdapter(Activity mContext, ArrayList<String> mResources) {
            this.mContext = mContext;
            this.mResources = mResources;
            Log.d("lsk", "k" + this.mResources);

        }

        @Override
        public float getPageWidth(int position) {
            if(mResources.size()==1){
                return 1;
            }else{
                return 0.7f;
            }


        }


        @Override
        public int getCount() {
            return mResources.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.fragment_screen_slide_multi, container, false);

            ImageView ivLargeSari = (ImageView) itemView.findViewById(R.id.ivLargeSari);

            LatoBoldTextview titleofproduct = (LatoBoldTextview) itemView.findViewById(R.id.titleofproduct);
            LatoBoldTextview retailprice = (LatoBoldTextview) itemView.findViewById(R.id.retailprice);
            LatoBoldTextview rentprice = (LatoBoldTextview) itemView.findViewById(R.id.rentprice);
            rentl = (LinearLayout) itemView.findViewById(R.id.rentl);
            buyl = (LinearLayout) itemView.findViewById(R.id.buyl);
            mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    valueforpos = mPager.getCurrentItem();
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });


            ivLargeSari.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rell.setBackgroundColor(Color.parseColor("#000000"));
                    rell.setAlpha(.01f);
                    final Dialog dialog = new Dialog(ProductMultiDetailActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_multidetail);
                    dialog.setCancelable(false);
                    // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    final Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                    window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    mPagers = (AutoScrollViewPager) dialog.findViewById(R.id.pager);
                    ImageView imageclose = (ImageView) dialog.findViewById(R.id.imageclose);
                    imageclose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rell.setBackgroundColor(Color.parseColor("#ffffff"));
                            rell.setAlpha(1f);
                            dialog.dismiss();
                        }
                    });
                    CircleIndicator indicator_defaults = (CircleIndicator) dialog.findViewById(R.id.indicator_default);
                    ViewPagerAdapters mPagerAdapters = new ViewPagerAdapters(ProductMultiDetailActivity.this, images, dialog);
                    mPagers.setAdapter(mPagerAdapters);
                    mPagers.setCurrentItem(valueforpos);
                    indicator_defaults.setViewPager(mPagers);
                   /* mPagers.startAutoScroll();
                    mPagers.setInterval(3000);
                    mPagers.setCycle(true);
                    mPagers.setStopScrollWhenTouch(true);*/
                    dialog.show();
                }
            });

            titleofproduct.setText(names.get(position));
            retailprice.setText("$" + price_retails.get(position));
            rentprice.setText("$" + price_rents.get(position));
            Picasso.with(mContext)
                    .load(mResources.get(position))
                    .placeholder(R.drawable.emptyimage1)
                    .error(R.drawable.emptyimage1)
                    .into(ivLargeSari);


            container.addView(itemView);


            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    public class ViewPagerAdapters extends PagerAdapter {

        private Activity mContext;
        private ArrayList<String> mResources = new ArrayList<>();
        private Dialog dialog;

        public ViewPagerAdapters(Activity mContext, ArrayList<String> mResources, Dialog dialog) {
            this.mContext = mContext;
            this.dialog = dialog;
            this.mResources = mResources;
            Log.d("lsk", "k" + this.mResources);

        }

        /*@Override
        public float getPageWidth(int position) {
            return position;
        }*/

        @Override
        public int getCount() {
            return mResources.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.dialog_screen_slide_multi, container, false);

            ImageView ivLargeSari = (ImageView) itemView.findViewById(R.id.ivLargeSari);
            LatoBoldTextview titleofproduct = (LatoBoldTextview) itemView.findViewById(R.id.titleofproduct);
            LatoBoldTextview retailprice = (LatoBoldTextview) itemView.findViewById(R.id.retailprice);
            LatoBoldTextview rentprice = (LatoBoldTextview) itemView.findViewById(R.id.rentprice);
            LinearLayout rentl = (LinearLayout) itemView.findViewById(R.id.rentl);
            LinearLayout buyl = (LinearLayout) itemView.findViewById(R.id.buyl);
            LinearLayout buyitem = (LinearLayout) itemView.findViewById(R.id.buyitem);
            LinearLayout rentitem = (LinearLayout) itemView.findViewById(R.id.rentitem);
            buyitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddCartSell addCartRent = new AddCartSell(ids.get(valueforrent));
                    addCartRent.addQueue();
                    dialog.dismiss();
                }
            });
            if (price_retails.get(position).equals("0")) {
                buyl.setVisibility(View.GONE);
            } else {
                buyl.setVisibility(View.VISIBLE);
            }
            if (price_rents.get(position).equals("0")) {
                rentl.setVisibility(View.GONE);
            } else {
                rentl.setVisibility(View.VISIBLE);
            }
            rentitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    valueforrent = mPagers.getCurrentItem();
                    final Dialog dialogs = new Dialog(ProductMultiDetailActivity.this);
                    dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogs.setContentView(R.layout.dialogcolor_rent);
                    dialogs.setCancelable(false);

                    final Window window = dialogs.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    LatoBoldTextview rentitem = (LatoBoldTextview) dialogs.findViewById(R.id.rentitem);
                    totalprice = (LatoBoldTextview) dialogs.findViewById(R.id.totalprice);
                    LatoRegularTextview bookforrent = (LatoRegularTextview) dialogs.findViewById(R.id.bookforrent);

                    ImageView imageclose = (ImageView) dialogs.findViewById(R.id.imageclose);
                    imageclose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.show();
                            dialogs.dismiss();
                        }
                    });
                    LatoBoldTextview rentprice = (LatoBoldTextview) dialogs.findViewById(R.id.rentprice);
                    slectfromdate = (LatoRegularTextview) dialogs.findViewById(R.id.slectfromdate);
                    selecttodate = (LatoRegularTextview) dialogs.findViewById(R.id.selecttodate);
                    rentitem.setText("Rent " + names.get(valueforrent));
                    rentprice.setText("$" + price_rents.get(valueforrent)+"/Day");
                    slectfromdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            slectfromdate.setError(null);
                            showDialog(999);

                        }
                    });
                    bookforrent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (slectfromdate.getText().toString().equals("Select Date")) {
                                slectfromdate.setError("Please Select From Date");
                            } else if (selecttodate.getText().toString().equals("Select Date")) {
                                selecttodate.setError("Please Select To Date");
                            } else {
                                AddCartRent addCartRent = new AddCartRent(ids.get(valueforrent));
                                addCartRent.addQueue();
                                dialog.dismiss();
                                dialogs.dismiss();

                            }

                        }
                    });
                    selecttodate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selecttodate.setError(null);
                            if (slectfromdate.getText().toString().equals(" Select Date")) {

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                                builder1.setMessage("Please Select From date");
                                builder1.setCancelable(false);

                                builder1.setPositiveButton(
                                        "Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });


                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            } else {

                                showDialog(9991);
                            }


                        }
                    });
                    dialogs.show();
                    dialog.dismiss();

                }
            });
            titleofproduct.setText(names.get(position));
            retailprice.setText("$" + price_retails.get(position));
            rentprice.setText("$" + price_rents.get(position)+"/Day");
            Picasso.with(mContext)
                    .load(mResources.get(position))
                    .placeholder(R.drawable.emptyimage1)
                    .error(R.drawable.emptyimage1)
                    .into(ivLargeSari);

            container.addView(itemView);


            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            DatePickerDialog dialog = new DatePickerDialog(ProductMultiDetailActivity.this, myDateListener, year, month, day);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, 10);
            dialog.getDatePicker().setMinDate(cal.getTimeInMillis());
            return dialog;
        } else if (id == 9991) {
            DatePickerDialog dialog = new DatePickerDialog(ProductMultiDetailActivity.this, myDateListeners, year, month, day);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, 10);
            dialog.getDatePicker().setMinDate(cal.getTimeInMillis());
            return dialog;
        } else if (id == 100) {
            DatePickerDialog dialog = new DatePickerDialog(ProductMultiDetailActivity.this, myDateListener1, year, month, day);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, 10);
            dialog.getDatePicker().setMinDate(cal.getTimeInMillis());
            return dialog;
        } else if (id == 1001) {
            DatePickerDialog dialog = new DatePickerDialog(ProductMultiDetailActivity.this, myDateListeners2, year, month, day);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, 10);
            dialog.getDatePicker().setMinDate(cal.getTimeInMillis());
            return dialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener1 = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day

                    showDaten(arg1, arg2 + 1, arg3);
                }
            };
    private DatePickerDialog.OnDateSetListener myDateListeners2 = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day

                    showDate1n(arg1, arg2 + 1, arg3);
                }
            };

    private void showDaten(int year, int month, int day) {
        slectfromdate.setText(String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day));
        ;
        selecttodate.setText("Select Date");
        totalprices.setText("");

    }

    private void showDate1n(int year, int month, int day) {
        String kk = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
        String mm = slectfromdate.getText().toString();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date parsed = sdf.parse(kk);
            Date now = sdf.parse(mm);

            if (now.after(parsed)) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                builder1.setMessage("To date is not smaller than from date");
                builder1.setCancelable(false);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


                AlertDialog alert11 = builder1.create();
                alert11.show();
            } else {
                int days = (int) ((parsed.getTime() - now.getTime()) / (1000 * 60 * 60 * 24));
                int days1 = days + 1;
                int value = Integer.parseInt(priceall) * days1;
                totalprices.setText("$" + String.valueOf(value));

                selecttodate.setText(String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day));
                ;
            }

            System.out.println(parsed.compareTo(now));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day

                    showDate(arg1, arg2 + 1, arg3);
                }
            };
    private DatePickerDialog.OnDateSetListener myDateListeners = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day

                    showDate1(arg1, arg2 + 1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        slectfromdate.setText(String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day));
        ;
        selecttodate.setText("Select Date");
        totalprice.setText("");

    }

    private void showDate1(int year, int month, int day) {
        String kk = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
        String mm = slectfromdate.getText().toString();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date parsed = sdf.parse(kk);
            Date now = sdf.parse(mm);

            if (now.after(parsed)) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                builder1.setMessage("To date is not smaller than from date");
                builder1.setCancelable(false);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


                AlertDialog alert11 = builder1.create();
                alert11.show();
            } else {
                int days = (int) ((parsed.getTime() - now.getTime()) / (1000 * 60 * 60 * 24));
                int days1 = days + 1;
                int value = Integer.parseInt(price_rents.get(valueforpos)) * days1;
                totalprice.setText("$" + String.valueOf(value));

                selecttodate.setText(String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day));
                ;
            }

            System.out.println(parsed.compareTo(now));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public class AddWishList {

        StringRequest sr;


        public AddWishList() {

            final ProgressDialog pDialog = new ProgressDialog(ProductMultiDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.POST, BaseUrl.BASE_URL +
                    BaseUrl.add_wishlist, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {
                            ivunsave.setVisibility(View.VISIBLE);
                            ivSave.setVisibility(View.GONE);

                        } else {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                            builder1.setMessage(message);
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });


                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    if (error instanceof TimeoutError) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                        builder1.setMessage("TimeOut Error. Please try again.");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else if (error instanceof NoConnectionError) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                        builder1.setMessage("No Internet Connection.Please try again");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                        builder1.setMessage("Server Error.Please try again");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("product_id", getIntent().getStringExtra("productid"));
                    params.put("user_id", userid);


                    return params;
                }

            };
        }

        public void addQueue() {
            MyApplication.getInstance().addToRequestQueue(sr);

            sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }


    }

    public class RemoveWishlist {

        StringRequest sr;


        public RemoveWishlist() {

            final ProgressDialog pDialog = new ProgressDialog(ProductMultiDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.POST, BaseUrl.BASE_URL +
                    BaseUrl.remove_wishlist, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {
                            ivSave.setVisibility(View.VISIBLE);
                            ivunsave.setVisibility(View.GONE);

                        } else {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                            builder1.setMessage(message);
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });


                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    if (error instanceof TimeoutError) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                        builder1.setMessage("TimeOut Error. Please try again.");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else if (error instanceof NoConnectionError) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                        builder1.setMessage("No Internet Connection.Please try again");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                        builder1.setMessage("Server Error.Please try again");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("product_id", getIntent().getStringExtra("productid"));
                    params.put("user_id", userid);


                    return params;
                }

            };
        }

        public void addQueue() {
            MyApplication.getInstance().addToRequestQueue(sr);

            sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }


    }

    public class CommentList {

        StringRequest sr;


        public CommentList() {

            final ProgressDialog pDialog = new ProgressDialog(ProductMultiDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.GET, BaseUrl.BASE_URL +
                    BaseUrl.product_comment_list + "&product_id=" + getIntent().getStringExtra("productid") +
                    "&page=" + page, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            String nextPage = jsonObject1.getString("nextPage");

                            page = nextPage;

                            final JSONObject jsonarray = jsonObject1.getJSONObject("comment");
                            tvNoOfComments.setText(jsonarray.getString("comment_count") + " Comments");
                            JSONArray jsonArray = jsonarray.getJSONArray("comment");

                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(j);
                                cmnts.add(jsonObject2.getString("comment"));
                                user_name.add(jsonObject2.getString("user_name"));
                                imageuser.add(jsonObject2.getString("user_image"));
                                dates.add(jsonObject2.getString("date"));

                            }
                            productDetailadapter = new ProductDetailadapter(ProductMultiDetailActivity.this, cmnts, imageuser, user_name, dates);
                            mRecyclerView.setAdapter(productDetailadapter);
                            productDetailadapter.notifyDataSetChanged();

                        } else {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                            builder1.setMessage("No more comments");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });


                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    if (error instanceof TimeoutError) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                        builder1.setMessage("TimeOut Error. Please try again.");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else if (error instanceof NoConnectionError) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                        builder1.setMessage("No Internet Connection.Please try again");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                        builder1.setMessage("Server Error.Please try again");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();


                    return params;
                }

            };
        }

        public void addQueue() {
            MyApplication.getInstance().addToRequestQueue(sr);

            sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }


    }

    public class AddComment {

        StringRequest sr;


        public AddComment() {

            final ProgressDialog pDialog = new ProgressDialog(ProductMultiDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.POST, BaseUrl.BASE_URL + BaseUrl.product_add_comment, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        cmnts.clear();
                        user_name.clear();
                        imageuser.clear();
                        dates.clear();
                        tvcmnt.setText("");
                        hideSoftKeyboard();

                        if (status.equals("true")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            final JSONObject jsonarray = jsonObject1.getJSONObject("comment");
                            tvNoOfComments.setText(jsonarray.getString("comment_count") + " Comments");
                            JSONArray jsonArray = jsonarray.getJSONArray("comment");

                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(j);
                                cmnts.add(jsonObject2.getString("comment"));
                                user_name.add(jsonObject2.getString("user_name"));
                                imageuser.add(jsonObject2.getString("user_image"));
                                dates.add(jsonObject2.getString("date"));

                            }
                            productDetailadapter = new ProductDetailadapter(ProductMultiDetailActivity.this, cmnts, imageuser, user_name, dates);
                            mRecyclerView.setAdapter(productDetailadapter);
                            productDetailadapter.notifyDataSetChanged();

                        } else {


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    if (error instanceof TimeoutError) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                        builder1.setMessage("TimeOut Error. Please try again.");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else if (error instanceof NoConnectionError) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                        builder1.setMessage("No Internet Connection.Please try again");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                        builder1.setMessage("Server Error.Please try again");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("product_id", getIntent().getStringExtra("productid"));
                    params.put("user_id", userid);
                    params.put("comment", tvcmnt.getText().toString());


                    return params;
                }

            };
        }

        public void addQueue() {
            MyApplication.getInstance().addToRequestQueue(sr);

            sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }


    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public class ProductDetail {

        StringRequest sr;


        public ProductDetail() {

            final ProgressDialog pDialog = new ProgressDialog(ProductMultiDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.GET, BaseUrl.BASE_URL + BaseUrl.product_detail_multiple +
                    "&product_id=" + getIntent().getStringExtra("productid") + "&user_id=" + userid
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        cmnts.clear();
                        user_name.clear();
                        imageuser.clear();
                        dates.clear();
                        if (status.equals("true")) {


                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            final JSONObject jsonarray = jsonObject1.getJSONObject("product");
                            pdname.setText(jsonarray.optString("user_name"));
                            tvSariTitle.setText(jsonarray.optString("name"));
                            tvSariPrice.setText("$" + jsonarray.optString("rent_price"));
                            tvSariView.setText(jsonarray.optString("views") + " Views");
                            if (jsonarray.optString("color_code").equals("")) {
                                tvSariColor.setText("N/A");
                            } else {
                                tvSariColor.setBackgroundColor(Color.parseColor(jsonarray.optString("color_code")));
                            }
                            String is_wishlist = jsonarray.getString("is_wishlist");
                            if (is_wishlist.equals("0")) {
                                ivSave.setVisibility(View.VISIBLE);
                                ivunsave.setVisibility(View.GONE);
                            } else {
                                ivunsave.setVisibility(View.VISIBLE);
                                ivSave.setVisibility(View.GONE);
                            }
                            if (jsonarray.getString("retail_price").equals("0")) {
                                buylyt.setVisibility(View.GONE);
                            } else {
                                buylyt.setVisibility(View.VISIBLE);
                                buytext.setText("Buy All Items $" + jsonarray.getString("retail_price"));
                            }
                            if (jsonarray.getString("rent_price").equals("0")) {
                                rentlyt.setVisibility(View.GONE);
                            } else {
                                rentlyt.setVisibility(View.VISIBLE);
                                priceall = jsonarray.getString("rent_price");
                                renttext.setText("Rent All Items $" + jsonarray.getString("rent_price"));
                            }
                            buylyt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AddCartSell addCartSell = new AddCartSell(getIntent().getStringExtra("productid"));
                                    addCartSell.addQueue();
                                }
                            });
                            rentlyt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    rell.setBackgroundColor(Color.parseColor("#000000"));
                                    rell.setAlpha(.01f);
                                    final Dialog dialogs = new Dialog(ProductMultiDetailActivity.this);
                                    dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialogs.setContentView(R.layout.dialogcolor_rent);
                                    dialogs.setCancelable(false);

                                    final Window window = dialogs.getWindow();
                                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                                    window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                    window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                    LatoBoldTextview rentitem = (LatoBoldTextview) dialogs.findViewById(R.id.rentitem);
                                    totalprices = (LatoBoldTextview) dialogs.findViewById(R.id.totalprice);
                                    LatoRegularTextview bookforrent = (LatoRegularTextview) dialogs.findViewById(R.id.bookforrent);
                                    ImageView imageclose = (ImageView) dialogs.findViewById(R.id.imageclose);

                                    imageclose.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            rell.setBackgroundColor(Color.parseColor("#ffffff"));
                                            rell.setAlpha(1f);
                                            dialogs.dismiss();
                                        }
                                    });
                                    LatoBoldTextview rentprice = (LatoBoldTextview) dialogs.findViewById(R.id.rentprice);
                                    slectfromdate = (LatoRegularTextview) dialogs.findViewById(R.id.slectfromdate);
                                    selecttodate = (LatoRegularTextview) dialogs.findViewById(R.id.selecttodate);
                                    rentitem.setText("Rent " + jsonarray.optString("name"));
                                    rentprice.setText("$" + jsonarray.optString("rent_price"));
                                    slectfromdate.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            slectfromdate.setError(null);
                                            showDialog(100);

                                        }
                                    });
                                    bookforrent.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (slectfromdate.getText().toString().equals("Select Date")) {
                                                slectfromdate.setError("Please Select From Date");
                                            } else if (selecttodate.getText().toString().equals("Select Date")) {
                                                selecttodate.setError("Please Select To Date");
                                            } else {
                                                AddCartRent addCartRent = new AddCartRent(getIntent().getStringExtra("productid"));
                                                addCartRent.addQueue();
                                                dialogs.dismiss();
                                            }
                                        }
                                    });
                                    selecttodate.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            selecttodate.setError(null);
                                            if (slectfromdate.getText().toString().equals(" Select Date")) {

                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                                                builder1.setMessage("Please Select From date");
                                                builder1.setCancelable(false);

                                                builder1.setPositiveButton(
                                                        "Ok",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
                                                            }
                                                        });


                                                AlertDialog alert11 = builder1.create();
                                                alert11.show();
                                            } else {
                                                showDialog(1001);
                                            }


                                        }
                                    });
                                    dialogs.show();

                                }
                            });
                            JSONArray jsonArray = jsonarray.getJSONArray("child");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                images.add(jsonObject2.getString("image"));
                                names.add(jsonObject2.getString("name"));
                                ids.add(jsonObject2.getString("product_id"));
                                price_retails.add(jsonObject2.getString("price_retail"));
                                price_rents.add(jsonObject2.getString("price_rent"));
                            }
                            final PagerAdapter mPagerAdapter = new ViewPagerAdapter(ProductMultiDetailActivity.this, images);
                            mPager.setAdapter(mPagerAdapter);
                            indicator_default.setViewPager(mPager);

                            tvNoOfComments.setText(jsonarray.getString("comment_count") + " Comments");
                            ratingBar.setRating(Float.parseFloat(jsonarray.getString("rating")));
                            JSONArray jsonArray1 = jsonarray.getJSONArray("comment");
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                                cmnts.add(jsonObject2.getString("comment"));
                                user_name.add(jsonObject2.getString("user_name"));
                                imageuser.add(jsonObject2.getString("user_image"));
                                dates.add(jsonObject2.getString("date"));
                            }
                            productDetailadapter = new ProductDetailadapter(ProductMultiDetailActivity.this, cmnts, imageuser, user_name, dates);
                            mRecyclerView.setAdapter(productDetailadapter);
                            moreinfo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Dialog dialog = new Dialog(ProductMultiDetailActivity.this);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.dialog_product);
                                    final Window window = dialog.getWindow();
                                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                                    window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    ImageView clse = (ImageView) dialog.findViewById(R.id.clse);
                                    TextView dstnce = (TextView) dialog.findViewById(R.id.dstnce);
                                    dstnce.setText(jsonarray.optString("distance")+" miles away from you");
                                    clse.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    LatoBoldTextview productnme = (LatoBoldTextview) dialog.findViewById(R.id.productnme);
                                    WebView webview = (WebView) dialog.findViewById(R.id.webview);
                                    String message = "<font color='white'>" +
                                            jsonarray.optString("description") + "</font>";
                                    webview.loadData(jsonarray.optString("description"), "text/html", "UTF-8");
                                    LatoBoldTextview pdpreice = (LatoBoldTextview) dialog.findViewById(R.id.pdpreice);
                                    productnme.setText(jsonarray.optString("name"));
                                    if(jsonarray.optString("retail_price").equals("0")){
                                        pdpreice.setText("$" + jsonarray.optString("rent_price"));
                                    }else{
                                        pdpreice.setText("$" + jsonarray.optString("retail_price"));
                                    }
                                    dialog.show();
                                }
                            });

                        } else {


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    if (error instanceof TimeoutError) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                        builder1.setMessage("TimeOut Error. Please try again.");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else if (error instanceof NoConnectionError) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                        builder1.setMessage("No Internet Connection.Please try again");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                        builder1.setMessage("Server Error.Please try again");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();


                    return params;
                }

            };
        }

        public void addQueue() {
            MyApplication.getInstance().addToRequestQueue(sr);

            sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }


    }
    private void getCurrentLocation() {
        gps = new GPSTracker(this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            pickupLat = String.valueOf(latitude);
            pickupLng = String.valueOf(longitude);

            ProductDetail productDetail = new ProductDetail();
            productDetail.addQueue();



            // \n is for new line
            //    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


    }
    public class AddCartRent {

        StringRequest sr;


        public AddCartRent(final String productid) {

            final ProgressDialog pDialog = new ProgressDialog(ProductMultiDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.POST, BaseUrl.BASE_URL +
                    BaseUrl.add_cart_rent, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (status.equals("true")) {
                            Intent intent = new Intent(ProductMultiDetailActivity.this, MyCart.class);
                            startActivity(intent);
                            finish();


                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                            builder1.setMessage(message);
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });


                            AlertDialog alert11 = builder1.create();
                            alert11.show();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    if (error instanceof TimeoutError) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                        builder1.setMessage("TimeOut Error. Please try again.");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else if (error instanceof NoConnectionError) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                        builder1.setMessage("No Internet Connection.Please try again");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                        builder1.setMessage("Server Error.Please try again");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("product_id", productid);
                    params.put("user_id", userid);
                    params.put("quantity", "1");
                    params.put("rent_from", slectfromdate.getText().toString());
                    params.put("rent_to", selecttodate.getText().toString());


                    return params;
                }

            };
        }

        public void addQueue() {
            MyApplication.getInstance().addToRequestQueue(sr);

            sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }


    }

    public class AddCartSell {

        StringRequest sr;


        public AddCartSell(final String productid) {

            final ProgressDialog pDialog = new ProgressDialog(ProductMultiDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.POST, BaseUrl.BASE_URL +
                    BaseUrl.add_cart_sell, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (status.equals("true")) {
                            Intent intent = new Intent(ProductMultiDetailActivity.this, MyCart.class);
                            startActivity(intent);
                            finish();


                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                            builder1.setMessage(message);
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });


                            AlertDialog alert11 = builder1.create();
                            alert11.show();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    if (error instanceof TimeoutError) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                        builder1.setMessage("TimeOut Error. Please try again.");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else if (error instanceof NoConnectionError) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                        builder1.setMessage("No Internet Connection.Please try again");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductMultiDetailActivity.this);
                        builder1.setMessage("Server Error.Please try again");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("product_id", productid);
                    params.put("user_id", userid);
                    params.put("quantity", "1");


                    return params;
                }

            };
        }

        public void addQueue() {
            MyApplication.getInstance().addToRequestQueue(sr);

            sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }


    }

}
