package com.takemysaree.tabfragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.takemysaree.adapter.CategoryGridAdapter;
import com.takemysaree.fonts.LatoExtraBoldTextview;
import com.takemysaree.models.MyApplication;
import com.takemysaree.utils.BaseUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by think360user on 20/4/17.
 */

public class CategoriesFragment extends Fragment {
    private TextView tvtitle;
    private GridView rv;
    List<String> categories = new ArrayList<>();
    List<String> ids = new ArrayList<>();
    List<String> images = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categories_listing, container, false);
        tvtitle=(TextView)getActivity().findViewById(R.id.tvtitle);
        rv = (GridView)view.findViewById(R.id.grid_view);
        Category category=new Category();
        category.addQueue();
        tvtitle.setText("Categories");
        return view;
    }
    public class Category {

        StringRequest sr;


        public Category() {

            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.GET, BaseUrl.BASE_URL + BaseUrl.product_category+"&page=1", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {

                            categories.clear();
                            ids.clear();
                            images.clear();
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            JSONArray jsonarray = jsonObject1.getJSONArray("category");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobect1 = jsonarray.getJSONObject(i);
                                String id = jsonobect1.getString("id");
                                String name = jsonobect1.getString("name");
                                String image = jsonobect1.getString("image");
                                categories.add(name);
                                ids.add(id);
                                images.add(image);
                            }
                            rv.setAdapter(new CategoryGridAdapter(getActivity(),categories,ids
                            ,images));


                        } else {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
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

    public class CategoryGridAdapter extends BaseAdapter {
        private Bitmap mBitmap, mBitmap1, mBitmap2, mBitmap3;
        float cornerRadius = 10.0f;
        private Resources mResources;
        private Context mContext;
        private RoundedBitmapDrawable roundedBitmapDrawable, roundedBitmapDrawable1, roundedBitmapDrawable2, roundedBitmapDrawable3;
        List<String> categories = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        List<String> images = new ArrayList<>();

        public CategoryGridAdapter(Context mContext, List<String> categories, List<String> ids,
                                   List<String> images) {
            this.mContext = mContext;
            this.categories = categories;
            this.ids = ids;
            this.images = images;
        }


        @Override


        public int getCount() {
            return ids.size();
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categroies_listdetails, null);
            final int pos = position;
            ImageView imageview1 = (ImageView) view.findViewById(R.id.imageview1);
            ImageView image2 = (ImageView) view.findViewById(R.id.image2);
            LatoExtraBoldTextview name = (LatoExtraBoldTextview) view.findViewById(R.id.name);
            name.setText(categories.get(pos));
            RelativeLayout relclick=(RelativeLayout)view.findViewById(R.id.relclick);
            relclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean removeStack = false;
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    ProductFragment fragment = new ProductFragment();
                    Bundle args = new Bundle();
                    args.putString("catid",ids.get(pos));
                    fragment.setArguments(args);
                    if (removeStack) {
                        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.replace(R.id.frame_container, fragment);
                    } else {
                        fragmentTransaction.replace(R.id.frame_container, fragment);
                        fragmentTransaction.addToBackStack(null);
                    }
                    fragmentTransaction.commit();
                }
            });

            if(images.get(pos).equals("")){
                Picasso.with(mContext)
                        .load(R.drawable.emptyimage1)
                        .placeholder(R.drawable.emptyimage1)
                        .error(R.drawable.emptyimage1)
                        .resizeDimen(R.dimen.list_detail_image_sizes, R.dimen.list_detail_image_sizes)
                        .centerCrop()
                        .into(imageview1);

            }else{
                Picasso.with(mContext)
                        .load(images.get(pos))
                        .placeholder(R.drawable.emptyimage1)
                        .error(R.drawable.emptyimage1)
                        .resizeDimen(R.dimen.list_detail_image_sizes, R.dimen.list_detail_image_sizes)
                        .centerCrop()
                        .into(imageview1);

            }


            return view;

        }
    }

}
