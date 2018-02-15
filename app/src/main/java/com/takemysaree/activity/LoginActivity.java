package com.takemysaree.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.takemysaree.R;
import com.takemysaree.adapter.HeaderAdapter;
import com.takemysaree.models.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by think360user on 12/10/17.
 */

public class LoginActivity extends Activity {
    HeaderAdapter madapter;
     LinearLayoutManager layoutManager;
    private ArrayList<String> imagearray=new ArrayList<>();
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    final int duration = 10;
    final int pixelsToMove = 30;
    private final Runnable SCROLLING_RUNNABLE = new Runnable() {

        @Override
        public void run() {
            rv_autoScroll.smoothScrollBy(pixelsToMove, 0);
            mHandler.postDelayed(this, duration);
        }
    };
    private RecyclerView rv_autoScroll;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        rv_autoScroll = (RecyclerView) findViewById(R.id.marqueList);
      layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);


        rv_autoScroll.setLayoutManager(layoutManager);

        GetImages getImages=new GetImages();
        getImages.addQueue();

    }
    public class GetImages {

        StringRequest sr;

        public GetImages() {

            final ProgressDialog pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.GET, "http://think360.in/blood_donation/image.php"
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");

                        if (status.equals("true")) {
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            for(int i=0;i<jsonArray.length();i++){
                                imagearray.add(jsonArray.getString(i));
                            }
                            rv_autoScroll.setHasFixedSize(true);
                            rv_autoScroll.setItemViewCacheSize(20);
                            rv_autoScroll.setDrawingCacheEnabled(true);
                            rv_autoScroll.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                            madapter = new HeaderAdapter(LoginActivity.this,imagearray);
                            rv_autoScroll.setAdapter(madapter);

                            rv_autoScroll.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);
                                    int lastItem = layoutManager.findLastCompletelyVisibleItemPosition();
                                    if(lastItem == layoutManager.getItemCount()-1){
                                        mHandler.removeCallbacks(SCROLLING_RUNNABLE);
                                        Handler postHandler = new Handler();
                                        postHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                rv_autoScroll.setAdapter(null);
                                                rv_autoScroll.setAdapter(madapter);
                                                mHandler.postDelayed(SCROLLING_RUNNABLE, 10000);
                                            }
                                        }, 100);
                                    }
                                }
                            });
                            mHandler.postDelayed(SCROLLING_RUNNABLE, 100);
                        } else if (status.equals("false")) {


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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
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
}
