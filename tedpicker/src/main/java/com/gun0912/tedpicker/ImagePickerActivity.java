/*
 * Copyright (c) 2016. Ted Park. All Rights Reserved
 */

package com.gun0912.tedpicker;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.commonsware.cwac.camera.CameraHost;
import com.commonsware.cwac.camera.CameraHostProvider;
import com.gun0912.tedpicker.custom.adapter.SpacesItemDecoration;
import com.gun0912.tedpicker.plus.AppHelper;
import com.gun0912.tedpicker.plus.VolleyMultipartRequest;
import com.gun0912.tedpicker.util.Util;
import com.vistrav.ask.Ask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ImagePickerActivity extends AppCompatActivity implements CameraHostProvider {

    /**
     * Returns the parcelled image uris in the intent with this extra.
     */
    public static final String EXTRA_IMAGE_URIS = "image_uris";
    //public static final String ExtraPath = "pathsuri";
    public static CwacCameraFragment1.MyCameraHost mMyCameraHost;
    // initialize with default config.
    private static Config mConfig = new Config();
    /**
     * Key to persist the list when saving the state of the activity.
     */

    public ArrayList<Uri> mSelectedImages;
    public static ArrayList<String> paths = new ArrayList<>();
    protected Toolbar toolbar;
    View view_root;
    TextView mSelectedImageEmptyMessage;
    View view_selected_photos_container;
    CwacCameraFragment1 cameraFragment;
    RecyclerView rc_selected_photos;
    TextView tv_selected_title;
    ViewPager mViewPager;
    TabLayout tabLayout;
    int valueofindex;
    FrameLayout framecntr;
    PagerAdapter_Picker adapter;
    private ImageView action_done;
    Adapter_SelectedPhoto adapter_selectedPhoto;

    public static Config getConfig() {
        return mConfig;
    }

    public static void setConfig(Config config) {

        if (config == null) {
            throw new NullPointerException("Config cannot be passed null. Not setting config will use default values.");
        }

        mConfig = config;
    }

    @Override
    public CameraHost getCameraHost() {
        return mMyCameraHost;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFromSavedInstanceState(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.picker_activity_main_pp);
        initView();
        String kk = getIntent().getStringExtra("checkvalue");
        if (kk != null) {
            paths.clear();
        } else {

        }


        setTitle(mConfig.getToolbarTitleRes());


        setupTabs();
        setSelectedPhotoRecyclerView();


    }

    private void initView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);


        view_root = findViewById(R.id.view_root);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        framecntr = (FrameLayout) findViewById(R.id.framecntr);

        tv_selected_title = (TextView) findViewById(R.id.tv_selected_title);
        action_done=(ImageView)findViewById(R.id.action_done);
        rc_selected_photos = (RecyclerView) findViewById(R.id.rc_selected_photos);
        mSelectedImageEmptyMessage = (TextView) findViewById(R.id.selected_photos_empty);

        view_selected_photos_container = findViewById(R.id.view_selected_photos_container);
        view_selected_photos_container.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view_selected_photos_container.getViewTreeObserver().removeOnPreDrawListener(this);

                int selected_bottom_size = (int) getResources().getDimension(mConfig.getSelectedBottomHeight());

                ViewGroup.LayoutParams params = view_selected_photos_container.getLayoutParams();
                params.height = selected_bottom_size;
                view_selected_photos_container.setLayoutParams(params);


                return true;
            }
        });
        cameraFragment = new CwacCameraFragment1();
        cameraFragment.setConfig(ImagePickerActivity.getConfig());
        replaceFragment(cameraFragment);

        if (mConfig.getSelectedBottomColor() > 0) {
            tv_selected_title.setBackgroundColor(ContextCompat.getColor(this, mConfig.getSelectedBottomColor()));
            mSelectedImageEmptyMessage.setTextColor(ContextCompat.getColor(this, mConfig.getSelectedBottomColor()));
        }
        action_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePicture();
            }
        });

    }

    private void setupFromSavedInstanceState(Bundle savedInstanceState) {


        if (savedInstanceState != null) {
            mSelectedImages = savedInstanceState.getParcelableArrayList(EXTRA_IMAGE_URIS);
        } else {
            mSelectedImages = getIntent().getParcelableArrayListExtra(EXTRA_IMAGE_URIS);
        }


        if (mSelectedImages == null) {
            mSelectedImages = new ArrayList<>();
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mSelectedImages != null) {
            outState.putParcelableArrayList(EXTRA_IMAGE_URIS, mSelectedImages);
        }

    }

    private void setupTabs() {
        adapter = new PagerAdapter_Picker(this, getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);


        if (mConfig.getTabBackgroundColor() > 0)
            tabLayout.setBackgroundColor(ContextCompat.getColor(this, mConfig.getTabBackgroundColor()));

        if (mConfig.getTabSelectionIndicatorColor() > 0)
            tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, mConfig.getTabSelectionIndicatorColor()));

    }

    public void replaceFragment(Fragment fragment) {
        boolean removeStack = true;

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (removeStack) {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.replace(R.id.framecntr, fragment);
        } else {
            ft.replace(R.id.framecntr, fragment);
            ft.addToBackStack(null);
        }
        // ft.replace(R.id.frame_container, fragment);
        //ft.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit);

        ft.commit();
    }

    private void setSelectedPhotoRecyclerView() {


        LinearLayoutManager mLayoutManager_Linear = new LinearLayoutManager(this);
        mLayoutManager_Linear.setOrientation(LinearLayoutManager.HORIZONTAL);

        rc_selected_photos.setLayoutManager(mLayoutManager_Linear);
        rc_selected_photos.addItemDecoration(new SpacesItemDecoration(Util.dpToPx(this, 5), SpacesItemDecoration.TYPE_VERTICAL));
        rc_selected_photos.setHasFixedSize(true);

        int closeImageRes = mConfig.getSelectedCloseImage();

        adapter_selectedPhoto = new Adapter_SelectedPhoto(this, closeImageRes);
        adapter_selectedPhoto.updateItems(mSelectedImages);
        rc_selected_photos.setAdapter(adapter_selectedPhoto);


        if (mSelectedImages.size() >= 1) {
            mSelectedImageEmptyMessage.setVisibility(View.GONE);
        }


    }


    public GalleryFragment getGalleryFragment() {

        if (adapter == null || adapter.getCount() < 2)
            return null;

        return (GalleryFragment) adapter.getItem(1);

    }

    public void addImage(final Uri uri) {


        if (mSelectedImages.size() == mConfig.getSelectionLimit()) {
            String text = String.format(getResources().getString(R.string.max_count_msg), mConfig.getSelectionLimit());
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            return;
        }
        RealPathUtil.getPath(ImagePickerActivity.this, Uri.parse(("file://") + uri));
        try {
            Bitmap mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(("file://") + uri));
            MultipartUpload multipartUpload = new MultipartUpload(getApplicationContext(), mBitmap);
            multipartUpload.addQueue();

        } catch (IOException e) {
            e.printStackTrace();
        }

        mSelectedImages.add(uri);
        adapter_selectedPhoto.updateItems(mSelectedImages);


        if (mSelectedImages.size() >= 1) {
            mSelectedImageEmptyMessage.setVisibility(View.GONE);
        }


        rc_selected_photos.smoothScrollToPosition(adapter_selectedPhoto.getItemCount() - 1);


    }

    public void removeImage(Uri uri) {

        valueofindex = mSelectedImages.indexOf(uri);
        mSelectedImages.remove(uri);
        MultipartUploaddelete multipartUpload = new MultipartUploaddelete(ImagePickerActivity.this, valueofindex);
        multipartUpload.addQueue();
        adapter_selectedPhoto.updateItems(mSelectedImages);

        if (mSelectedImages.size() == 0) {
            mSelectedImageEmptyMessage.setVisibility(View.VISIBLE);
        }
        GalleryFragment.mGalleryAdapter.notifyDataSetChanged();


    }

    public boolean containsImage(Uri uri) {
        return mSelectedImages.contains(uri);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_confirm, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_done) {
            updatePicture();
            return true;
        }

        return super.onOptionsItemSelected(item);


    }

    private void updatePicture() {

        if (mSelectedImages.size() < mConfig.getSelectionMin()) {
            String text = String.format(getResources().getString(R.string.min_count_msg), mConfig.getSelectionMin());
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(EXTRA_IMAGE_URIS, mSelectedImages);
        setResult(Activity.RESULT_OK, intent);
        finish();

    }

    public class MultipartUpload {

        VolleyMultipartRequest multipartRequest;
        RequestQueue queue;

        public MultipartUpload(final Context activity, final Bitmap bitmap) {
            queue = Volley.newRequestQueue(activity);
            final ProgressDialog pDialog = new ProgressDialog(ImagePickerActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
            multipartRequest = new VolleyMultipartRequest(Request.Method.POST, "http://tms.himview.com/api/index.php?/12345/add_image", new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    pDialog.dismiss();
                    String resultResponse = new String(response.data);
                    try {


                        JSONObject result = new JSONObject(resultResponse);
                        String status = result.getString("status");
                        if (status.equalsIgnoreCase("true")) {
                            JSONObject jsonObject = result.getJSONObject("data");
                            JSONObject jsonObject1 = jsonObject.getJSONObject("orignal_file");
                            String url = jsonObject1.getString("url");
                            String path = jsonObject1.getString("path");
                            paths.add(path);

                            // Toast.makeText(activity, "" + paths.size(), Toast.LENGTH_SHORT).show();
                            JSONObject jsonObject2 = jsonObject.getJSONObject("thumb_file");
                            String urls = jsonObject2.getString("url");


                        } else {
                            String message = result.getString("message");
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    NetworkResponse networkResponse = error.networkResponse;
                    String errorMessage = "Unknown error";
                    if (networkResponse == null) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            errorMessage = "Request timeout";
                        } else if (error.getClass().equals(NoConnectionError.class)) {
                            errorMessage = "Failed to connect server";
                        }
                    } else {

                            /*String result = new String(networkResponse.data);
                            JSONObject response = new JSONObject(result);
                            String status = response.getString("status");
                            String message = response.getString("message");

                            Log.e("Error Status", status);
                            Log.e("Error Message", message);*/

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = " Something is getting wrong";
                        }
                    }


                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("type", "product");
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();

                    params.put("file", new DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(activity, bitmap), "image/jpeg"));


                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Accept", "json");
                    return params;
                }
            };
        }

        public void addQueue() {
            queue.add(multipartRequest);
            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }
    }

    public class MultipartUploaddelete {

        VolleyMultipartRequest multipartRequest;
        RequestQueue queue;

        public MultipartUploaddelete(final Context activity, final int index) {
            queue = Volley.newRequestQueue(activity);
            final ProgressDialog pDialog = new ProgressDialog(ImagePickerActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
            multipartRequest = new VolleyMultipartRequest(Request.Method.POST, "http://tms.himview.com/api/index.php?/12345/delete_image", new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    pDialog.dismiss();
                    String resultResponse = new String(response.data);
                    try {
                        JSONObject result = new JSONObject(resultResponse);
                        String status = result.getString("status");
                        if (status.equalsIgnoreCase("true")) {
                            paths.remove(paths.get(index));


                        } else {
                            String message = result.getString("message");
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    NetworkResponse networkResponse = error.networkResponse;
                    String errorMessage = "Unknown error";
                    if (networkResponse == null) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            errorMessage = "Request timeout";
                        } else if (error.getClass().equals(NoConnectionError.class)) {
                            errorMessage = "Failed to connect server";
                        }
                    } else {

                            /*String result = new String(networkResponse.data);
                            JSONObject response = new JSONObject(result);
                            String status = response.getString("status");
                            String message = response.getString("message");

                            Log.e("Error Status", status);
                            Log.e("Error Message", message);*/

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = " Something is getting wrong";
                        }
                    }


                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("type", "product");
                    params.put("file_path", paths.get(index));
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();


                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Accept", "json");
                    return params;
                }
            };
        }

        public void addQueue() {
            queue.add(multipartRequest);
            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }
    }
}
