package com.takemysaree.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.takemysaree.R;
import com.takemysaree.activity.EventListener;
import com.takemysaree.activity.ListOfMultiProduct;
import com.takemysaree.activity.MutliProductActivity;
import com.takemysaree.fonts.LatoBoldTextview;
import com.takemysaree.fonts.LatoExtraBoldTextview;
import com.takemysaree.fonts.LatoNormalEditText;
import com.takemysaree.models.F1models;
import com.takemysaree.models.F2models;
import com.takemysaree.models.F3models;
import com.takemysaree.models.F4models;
import com.takemysaree.models.MyApplication;
import com.takemysaree.models.RealPathUtil;
import com.takemysaree.utils.BaseUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by think360user on 20/4/17.
 */

public class FragmentMutli4 extends Fragment {
    private LinearLayout login;
    ArrayList<Uri> image_uris = new ArrayList<Uri>();
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    private ImageView view_pager;
    List<String> ids = new ArrayList<>();
    List<String> categories = new ArrayList<>();
    List<String> images = new ArrayList<>();
    private TabLayout tabLayout;
    LatoBoldTextview next, previous, post;

    private String category_id;
    private EventListener listener;
    private LatoNormalEditText retailprice, chosecate, choselocation, rentpr, productname, size;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof EventListener) {
            listener = (EventListener) activity;
        } else {
            // Throw an error!
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.addmultiproduct_detailwithtwo, container, false);
        image_uris = getActivity().getIntent().getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
        for (int i = 0; i < image_uris.size(); i++) {

            RealPathUtil.getPath(getActivity().getApplicationContext(), Uri.parse(("file://") + image_uris.get(i)));
            try {
                Bitmap mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(("file://") + image_uris.get(i)));
                bitmaps.add(mBitmap);
                if (mBitmap != null)
                {
                    mBitmap = null;

                }
                // ivProfile.setImageBitmap(mBitmap);
                // new MultipartUpload(this, mBitmap, "image").addQueue();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        view_pager = (ImageView) view.findViewById(R.id.view_pager);
        view_pager.setImageBitmap(bitmaps.get(4));
        choselocation = (LatoNormalEditText) view.findViewById(R.id.choselocation);
        productname = (LatoNormalEditText) view.findViewById(R.id.productname);
        size = (LatoNormalEditText) view.findViewById(R.id.size);
        retailprice = (LatoNormalEditText) view.findViewById(R.id.retailprice);
        rentpr = (LatoNormalEditText) view.findViewById(R.id.rentpr);
        choselocation.setText(getActivity().getIntent().getStringExtra("choselocation"));
        chosecate = (LatoNormalEditText) view.findViewById(R.id.chosecate);
        chosecate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialogcategories_listing);
                final Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                GridView rv = (GridView) dialog.findViewById(R.id.grid_view);
                ImageView ivclose = (ImageView) dialog.findViewById(R.id.ivclose);
                ivclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                Category category = new Category(rv, dialog);
                category.addQueue();
            }
        });
        if (getActivity().getIntent().getStringExtra("product_type").equals("1")) {
            rentpr.setVisibility(View.VISIBLE);
            retailprice.setVisibility(View.GONE);

        } else if (getActivity().getIntent().getStringExtra("product_type").equals("2")) {
            rentpr.setVisibility(View.GONE);
            retailprice.setVisibility(View.VISIBLE);
        } else {
            rentpr.setVisibility(View.VISIBLE);
            retailprice.setVisibility(View.VISIBLE);

        }
        next = (LatoBoldTextview) view.findViewById(R.id.next);
        post = (LatoBoldTextview) view.findViewById(R.id.post);
        previous = (LatoBoldTextview) view.findViewById(R.id.previous);
        if (image_uris.size() == 5) {
            next.setVisibility(View.GONE);
            post.setVisibility(View.VISIBLE);
        } else {
            next.setVisibility(View.VISIBLE);
            post.setVisibility(View.GONE);
        }
        View headerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
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

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayout.Tab tab = tabLayout.getTabAt(3);
                linearLayoutOne.setAlpha(1f);
                linearLayout2.setAlpha(.4f);
                linearLayout3.setAlpha(.4f);
                linearLayout4.setAlpha(.4f);
                linearLayout5.setAlpha(.4f);
                tab.select();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productname.getText().toString().equals("")) {
                    productname.setError("Please Enter name");
                } else if (category_id == null) {
                    chosecate.setError("Please select category");
                } else if (size.getText().toString().equals("")) {
                    size.setError("Please enter size");
                } else if (rentpr.getText().toString().equals("") && rentpr.getVisibility() == View.VISIBLE) {
                    rentpr.setError("Please enter price/day");
                } else if (retailprice.getText().toString().equals("") && retailprice.getVisibility() == View.VISIBLE) {
                    retailprice.setError("Please enter price");
                } else {
                    MutliProductActivity.productname.clear();
                    MutliProductActivity.sizes.clear();
                    MutliProductActivity.retailprice.clear();
                    MutliProductActivity.rentprice.clear();
                    MutliProductActivity.categoryid.clear();
                    MutliProductActivity.productname.add(FragmentMutli.product1);
                    MutliProductActivity.productname.add(FragmentMutli1.product12);
                    MutliProductActivity.productname.add(FragmentMutli2.product13);
                    MutliProductActivity.productname.add(FragmentMutli3.product14);
                    MutliProductActivity.productname.add(productname.getText().toString());
                    MutliProductActivity.sizes.add(FragmentMutli.size1);
                    MutliProductActivity.sizes.add(FragmentMutli1.size12);
                    MutliProductActivity.sizes.add(FragmentMutli2.size13);
                    MutliProductActivity.sizes.add(FragmentMutli3.size14);
                    MutliProductActivity.sizes.add(size.getText().toString());
                    MutliProductActivity.retailprice.add(FragmentMutli.retailpr1);
                    MutliProductActivity.retailprice.add(FragmentMutli1.retailpr12);
                    MutliProductActivity.retailprice.add(FragmentMutli2.retailpr13);
                    MutliProductActivity.retailprice.add(FragmentMutli3.retailpr14);
                    MutliProductActivity.retailprice.add(retailprice.getText().toString());
                    MutliProductActivity.rentprice.add(FragmentMutli.rentpr1);
                    MutliProductActivity.rentprice.add(FragmentMutli1.rentpr12);
                    MutliProductActivity.rentprice.add(FragmentMutli2.rentpr13);
                    MutliProductActivity.rentprice.add(FragmentMutli3.rentpr14);
                    MutliProductActivity.rentprice.add(rentpr.getText().toString());
                    MutliProductActivity.categoryid.add(FragmentMutli.cats1);
                    MutliProductActivity.categoryid.add(FragmentMutli1.cats12);
                    MutliProductActivity.categoryid.add(FragmentMutli2.cats13);
                    MutliProductActivity.categoryid.add(FragmentMutli3.cats14);
                    MutliProductActivity.categoryid.add(category_id);
                    PostMulti postMulti = new PostMulti();
                    postMulti.addQueue();

                }

            }
        });
        return view;
    }

    public class PostMulti {

        StringRequest sr;


        public PostMulti() {

            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.POST, BaseUrl.BASE_URL +
                    BaseUrl.product_add_multiple, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.hide();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            final String productid=jsonObject1.getString("product_id");

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                            builder1.setMessage(message);
                            builder1.setCancelable(false);

                            builder1.setPositiveButton(
                                    "Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent=new Intent(getActivity(), ListOfMultiProduct.class);
                                            intent.putExtra("productid",productid);
                                            getActivity().startActivity(intent);
                                            getActivity().finish();
                                            dialog.cancel();
                                        }
                                    });


                            AlertDialog alert11 = builder1.create();
                            alert11.show();


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
                    pDialog.hide();
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
                    JSONArray array = new JSONArray(ImagePickerActivity.paths);
                    params.put("main_title", getActivity().getIntent().getStringExtra("productname"));
                    params.put("user_id", MutliProductActivity.user_id);
                    params.put("quantity", getActivity().getIntent().getStringExtra("quantity"));
                    params.put("taxes", getActivity().getIntent().getStringExtra("taxes"));
                    params.put("weight", getActivity().getIntent().getStringExtra("weight"));
                    params.put("color_code", getActivity().getIntent().getStringExtra("color_code"));
                    params.put("description", getActivity().getIntent().getStringExtra("description"));
                    params.put("product_type", getActivity().getIntent().getStringExtra("product_type"));
                    params.put("nagotiable", getActivity().getIntent().getStringExtra("nagotiable"));
                    params.put("location", getActivity().getIntent().getStringExtra("choselocation"));
                    params.put("latitude", getActivity().getIntent().getStringExtra("lat"));
                    params.put("longitude", getActivity().getIntent().getStringExtra("lng"));
                    params.put("country", "1");
                    JSONArray jsonArray = new JSONArray(MutliProductActivity.productname);
                    JSONArray jsonArray1 = new JSONArray(MutliProductActivity.sizes);
                    JSONArray jsonArray2 = new JSONArray(MutliProductActivity.categoryid);
                    JSONArray jsonArray3 = new JSONArray(MutliProductActivity.rentprice);
                    JSONArray jsonArray4 = new JSONArray(MutliProductActivity.retailprice);
                    params.put("product_name", jsonArray.toString());
                    params.put("category_id", jsonArray2.toString());
                    params.put("size", jsonArray1.toString());
                    params.put("retail_price", jsonArray4.toString());
                    params.put("rent_price", jsonArray3.toString());
                    params.put("image", array.toString());


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

    public class Category {

        StringRequest sr;


        public Category(final GridView rv, final Dialog dialog) {

            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.GET, BaseUrl.BASE_URL + BaseUrl.product_category + "&page=1", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.hide();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {

                            categories.clear();
                            ids.clear();
                            images.clear();
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
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
                            rv.setAdapter(new CategoryGridAdapter(getActivity(), categories, ids
                                    , images, dialog));
                            dialog.show();


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
                    pDialog.hide();
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
        List<String> categories = new ArrayList<>();
        private Context mContext;
        List<String> ids = new ArrayList<>();
        private Dialog dialog;
        List<String> images = new ArrayList<>();

        public CategoryGridAdapter(Context mContext, List<String> categories, List<String> ids,
                                   List<String> images, Dialog dialog) {
            this.mContext = mContext;
            this.categories = categories;
            this.ids = ids;
            this.dialog = dialog;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialogcategroies_listdetails, null);
            final int pos = position;
            RelativeLayout relclick = (RelativeLayout) view.findViewById(R.id.relclick);
            LatoExtraBoldTextview name = (LatoExtraBoldTextview) view.findViewById(R.id.name);
            name.setText(categories.get(pos));

            relclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    category_id = ids.get(pos);
                    chosecate.setText(categories.get(pos));
                    dialog.cancel();
                }
            });


            return view;

        }
    }
}
