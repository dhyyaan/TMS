package com.gun0912.tedpicker;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.gun0912.tedpicker.custom.adapter.BaseRecyclerViewAdapter;
import com.gun0912.tedpicker.plus.AppHelper;
import com.gun0912.tedpicker.plus.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by TedPark on 16. 2. 20..
 */
public class Adapter_SelectedPhoto extends BaseRecyclerViewAdapter<Uri, Adapter_SelectedPhoto.SelectedPhotoHolder> {

    ArrayList<String> patharray=new ArrayList<>();
    int closeImageRes;
private String path;

    ImagePickerActivity imagePickerActivity;

    public Adapter_SelectedPhoto(ImagePickerActivity imagePickerActivity, int closeImageRes) {
        super(imagePickerActivity);
        this.imagePickerActivity = imagePickerActivity;
        this.closeImageRes = closeImageRes;


    }

    @Override
    public void onBindView(SelectedPhotoHolder holder, int position) {

        Uri uri = getItem(position);

        RealPathUtil.getPath(getContext(), Uri.parse(("file://")+uri));
        try {
            Bitmap mBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.parse(("file://")+uri));
            Glide.with(imagePickerActivity)
                    .load(uri.toString())
                    //   .override(selected_bottom_size, selected_bottom_size)
                    .dontAnimate()
                    .centerCrop()
                    .error(R.drawable.no_image)
                    .into(holder.selected_photo);
           /* MultipartUpload multipartUpload=new MultipartUpload(getContext(),mBitmap);
            multipartUpload.addQueue();*/

        } catch (IOException e) {
            e.printStackTrace();
        }






        holder.iv_close.setTag(uri);



    }

    @Override
    public SelectedPhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(getContext());
        View view = mInflater.inflate(R.layout.picker_list_item_selected_thumbnail, parent, false);
        return new SelectedPhotoHolder(view);
    }

    public class MultipartUpload {

        VolleyMultipartRequest multipartRequest;
        RequestQueue queue;

        public MultipartUpload(final Context activity, final Bitmap bitmap) {
            queue = Volley.newRequestQueue(activity);
            final ProgressDialog pDialog = new ProgressDialog(getContext());
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
                            path=jsonObject1.getString("path");
                            patharray.add(path);

                         Toast.makeText(activity, ""+patharray.size(), Toast.LENGTH_SHORT).show();
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
    public class MultipartUploaddelete {

        VolleyMultipartRequest multipartRequest;
        RequestQueue queue;

        public MultipartUploaddelete(final Context activity, final Bitmap bitmap) {

            final ProgressDialog pDialog = new ProgressDialog(getContext());
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
                            path=jsonObject1.getString("path");


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
                    params.put("file_path",path);
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



    class SelectedPhotoHolder extends RecyclerView.ViewHolder {


        ImageView selected_photo;
        ImageView iv_close;


        public SelectedPhotoHolder(View itemView) {
            super(itemView);
            selected_photo = (ImageView) itemView.findViewById(R.id.selected_photo);
            iv_close = (ImageView) itemView.findViewById(R.id.iv_close);
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = (Uri) view.getTag();
                    RealPathUtil.getPath(getContext(), Uri.parse(("file://")+uri));
                    try {
                        Bitmap mBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.parse(("file://")+uri));

                        imagePickerActivity.removeImage(uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });

        }





    }
}
