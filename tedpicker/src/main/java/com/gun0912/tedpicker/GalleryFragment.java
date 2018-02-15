/*
 * Copyright (c) 2016. Ted Park. All Rights Reserved
 */

package com.gun0912.tedpicker;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.gun0912.tedpicker.plus.AppHelper;
import com.gun0912.tedpicker.plus.VolleyMultipartRequest;
import com.gun0912.tedpicker.view.CustomSquareFrameLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Gil on 04/03/2014.
 */
public class GalleryFragment extends Fragment {

    public static ImageGalleryAdapter mGalleryAdapter;
    public static ImagePickerActivity mActivity;
    private ImageView ivHamburger;
    public static int index;
    private CwacCameraFragment1 cameraFragment;
    private FrameLayout frameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.picker_fragment_gallery, container, false);
        GridView galleryGridView = (GridView) rootView.findViewById(R.id.gallery_grid);
        mActivity = ((ImagePickerActivity) getActivity());
        frameLayout = (FrameLayout) getActivity().findViewById(R.id.btn_takegallery);
        ivHamburger=(ImageView)getActivity().findViewById(R.id.ivHamburger);
        ivHamburger.setImageResource(R.drawable.back_arrow);
        ivHamburger.setVisibility(View.VISIBLE);
        ivHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraFragment=new CwacCameraFragment1();
                cameraFragment.setConfig(ImagePickerActivity.getConfig());
                replaceFragment(cameraFragment);
            }
        });

        List<Uri> images = getImagesFromGallary(getActivity());
        mGalleryAdapter = new ImageGalleryAdapter(getActivity(), images);

        galleryGridView.setAdapter(mGalleryAdapter);
        galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Uri mUri = mGalleryAdapter.getItem(i);


                if (!mActivity.containsImage(mUri)) {
                    mActivity.addImage(mUri);
                } else {
                    mActivity.removeImage(mUri);


                }

                mGalleryAdapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }

    public void replaceFragment(Fragment fragment) {
        boolean removeStack = true;

        FragmentManager fm =getActivity(). getSupportFragmentManager();
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
    public void refreshGallery(Context context) {

        List<Uri> images = getImagesFromGallary(context);

        if (mGalleryAdapter == null) {

            mGalleryAdapter = new ImageGalleryAdapter(context, images);
        } else {

            mGalleryAdapter.clear();
            mGalleryAdapter.addAll(images);
            mGalleryAdapter.notifyDataSetChanged();

        }


    }

    public class MultipartUpload {

        VolleyMultipartRequest multipartRequest;
        RequestQueue queue;

        public MultipartUpload(final Context activity, final Bitmap bitmap) {
            queue = Volley.newRequestQueue(activity);
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
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
                            JSONObject jsonObject =result.getJSONObject("data");
                            JSONObject jsonObject1=jsonObject.getJSONObject("orignal_file");
                            String url = jsonObject1.getString("url");
                            String path=jsonObject1.getString("path");
                            //paths.add(path);

                           // Toast.makeText(activity, ""+paths.size(), Toast.LENGTH_SHORT).show();
                            JSONObject jsonObject2=jsonObject.getJSONObject("thumb_file");
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
    public List<Uri> getImagesFromGallary(Context context) {

        List<Uri> images = new ArrayList<Uri>();


        Cursor imageCursor = null;
        try {
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.ORIENTATION};
            final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";


            imageCursor = context.getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
            while (imageCursor.moveToNext()) {
                Uri uri = Uri.parse(imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                images.add(uri);


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (imageCursor != null && !imageCursor.isClosed()) {
                imageCursor.close();
            }
        }


        return images;

    }


    class ViewHolder {
        CustomSquareFrameLayout root;

        ImageView mThumbnail;

        // This is like storing too much data in memory.
        // find a better way to handle this
        Uri uri;

        public ViewHolder(View view) {
            root = (CustomSquareFrameLayout) view.findViewById(R.id.root);
            mThumbnail = (ImageView) view.findViewById(R.id.thumbnail_image);
        }

    }

    public class ImageGalleryAdapter extends ArrayAdapter<Uri> {

        Context context;


        public ImageGalleryAdapter(Context context, List<Uri> images) {
            super(context, 0, images);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.picker_grid_item_gallery_thumbnail, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            final Uri mUri = getItem(position);
            boolean isSelected = mActivity.containsImage(mUri);


            if(holder.root instanceof FrameLayout){
                ((FrameLayout)holder.root).setForeground(isSelected ? ResourcesCompat.getDrawable(getResources(),R.drawable.gallery_photo_selected,null) : null);
            }



            if (holder.uri == null || !holder.uri.equals(mUri)) {


                Glide.with(context)
                        .load(mUri.toString())
                        .thumbnail(0.1f)
                                //.fit()
                        .dontAnimate()
                                //   .override(holder.mThumbnail.getWidth(), holder.mThumbnail.getWidth())
                                //  .override(holder.root.getWidth(), holder.root.getWidth())
                        .centerCrop()
                        .placeholder(R.drawable.place_holder_gallery)
                        .error(R.drawable.no_image)

                        .into(holder.mThumbnail);
                holder.uri = mUri;


            }


            return convertView;
        }
    }
}