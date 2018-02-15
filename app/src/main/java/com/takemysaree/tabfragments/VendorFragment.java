package com.takemysaree.tabfragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.takemysaree.activity.MyOrderActivity;
import com.takemysaree.activity.VendorDetailActivity;
import com.takemysaree.adapter.CategoryGridAdapter;
import com.takemysaree.adapter.RecyclerAdapter;
import com.takemysaree.adapter.VendorAdapter;
import com.takemysaree.adapter.VendorListAdapter;
import com.takemysaree.fonts.LatoBoldTextview;
import com.takemysaree.fonts.LatoRegularTextview;
import com.takemysaree.models.CircleImageview;
import com.takemysaree.models.MyApplication;
import com.takemysaree.models.RoundedTransformation;
import com.takemysaree.utils.BaseUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by think360user on 20/4/17.
 */

public class VendorFragment extends Fragment {
    private TextView tvtitle;
    private ListView rv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vendor_listing, container, false);
        tvtitle = (TextView) getActivity().findViewById(R.id.tvtitle);
        rv = (ListView) view.findViewById(R.id.rv);

        Category category = new Category();
        category.addQueue();
        tvtitle.setText("Vendor's");
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

            sr = new StringRequest(Request.Method.GET, BaseUrl.BASE_URL + BaseUrl.vandor_list + "&page=1", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.hide();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {
                            ArrayList<Vendormodel> vendormodelArrayList = new ArrayList<>();
                            vendormodelArrayList.clear();
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            JSONArray jsonarray = jsonObject1.getJSONArray("vendor");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobect1 = jsonarray.getJSONObject(i);
                                Vendormodel vendormodel = new Vendormodel();
                                vendormodel.setId(jsonobect1.getString("id"));
                                vendormodel.setName(jsonobect1.getString("name"));
                                vendormodel.setImage(jsonobect1.getString("image"));
                                vendormodel.setFollowed_count(jsonobect1.getString("followed_count"));

                                JSONArray jsonArray = jsonobect1.getJSONArray("product");
                                vendormodel.setSize(jsonArray.length());
                                if(jsonArray.length()==0){

                                }else{
                                    ArrayList<Vendorproduct> vendorproductArrayList = new ArrayList<>();
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject jsonobect2 = jsonArray.getJSONObject(j);
                                        Vendorproduct vendorproduct=new Vendorproduct();
                                        vendorproduct.setId(jsonobect2.optString("id"));
                                        vendorproduct.setName(jsonobect2.optString("name"));
                                        vendorproduct.setImage(jsonobect2.optString("image"));
                                        vendorproductArrayList.add(vendorproduct);

                                    }
                                    vendormodel.setVendorproducts(vendorproductArrayList);
                                }

                                vendormodelArrayList.add(vendormodel);
                            }
                            rv.setAdapter(new VendorListAdapter(getActivity(), vendormodelArrayList));


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
    public class Vendorproduct implements Serializable{
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        private String image;
    }

    public class Vendormodel implements Serializable {
        private String id;
        private String name;

        public String getFollowed_count() {
            return followed_count;
        }

        public void setFollowed_count(String followed_count) {
            this.followed_count = followed_count;
        }

        private String image;
        private String followed_count;
        private ArrayList<Vendorproduct> vendorproducts=new ArrayList<>();



        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public ArrayList<Vendorproduct> getVendorproducts() {
            return vendorproducts;
        }

        public void setVendorproducts(ArrayList<Vendorproduct> vendorproducts) {
            this.vendorproducts = vendorproducts;
        }

        private int size;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }







    }

    public class VendorListAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<Vendormodel> vendormodelArrayList;

        public VendorListAdapter(Context mContext, ArrayList<Vendormodel> vendormodelArrayList) {
            this.mContext = mContext;
            this.vendormodelArrayList = vendormodelArrayList;
        }


        @Override


        public int getCount() {
            return vendormodelArrayList.size();
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_listdetails, null);
            final int pos = position;
            CircleImageview grid_image=(CircleImageview)view.findViewById(R.id.grid_image);
            LatoBoldTextview shop_name=(LatoBoldTextview)view.findViewById(R.id.shop_name);
            shop_name.setText(vendormodelArrayList.get(pos).getName());
            ImageView imageview1 = (ImageView) view.findViewById(R.id.imageview1);
            ImageView imageview2 = (ImageView) view.findViewById(R.id.imageview2);
            ImageView imageview3 = (ImageView) view.findViewById(R.id.imageview3);
            ImageView imageview4 = (ImageView) view.findViewById(R.id.imageview4);
            LinearLayout linclick=(LinearLayout)view.findViewById(R.id.linclick);
            linclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), VendorDetailActivity.class);
                    intent.putExtra("id",vendormodelArrayList.get(pos).getId());
                    getActivity().startActivity(intent);
                }
            });
            LatoRegularTextview nocollection=(LatoRegularTextview)view.findViewById(R.id.nocollection);
            LatoRegularTextview license=(LatoRegularTextview)view.findViewById(R.id.license);
            license.setText("Followed by "+vendormodelArrayList.get(pos).getFollowed_count()+" people");
            LinearLayout collection_present=(LinearLayout)view.findViewById(R.id.collection_present);
            if(vendormodelArrayList.get(pos).getSize()==0){
                collection_present.setVisibility(View.GONE);
                nocollection.setVisibility(View.VISIBLE);
            }else{
                collection_present.setVisibility(View.VISIBLE);
                nocollection.setVisibility(View.GONE);
                if(vendormodelArrayList.get(pos).getSize()==1){
                    Picasso.with(mContext)
                            .load(vendormodelArrayList.get(pos).getVendorproducts().get(0).getImage())
                            .placeholder(R.drawable.emptyimage1)
                            .error(R.drawable.emptyimage1)
                            .into(imageview1);
                }else if(vendormodelArrayList.get(pos).getSize()==2){

                    Picasso.with(mContext)
                            .load(vendormodelArrayList.get(pos).getVendorproducts().get(0).getImage())
                            .placeholder(R.drawable.emptyimage1)
                            .error(R.drawable.emptyimage1)
                            .into(imageview1);
                    Picasso.with(mContext)
                            .load(vendormodelArrayList.get(pos).getVendorproducts().get(1).getImage())
                            .placeholder(R.drawable.emptyimage1)
                            .error(R.drawable.emptyimage1)
                            .into(imageview2);
                }
                else if(vendormodelArrayList.get(pos).getSize()==3){

                    Picasso.with(mContext)
                            .load(vendormodelArrayList.get(pos).getVendorproducts().get(0).getImage())
                            .placeholder(R.drawable.emptyimage1)
                            .error(R.drawable.emptyimage1)
                            .into(imageview1);
                    Picasso.with(mContext)
                            .load(vendormodelArrayList.get(pos).getVendorproducts().get(1).getImage())
                            .placeholder(R.drawable.emptyimage1)
                            .error(R.drawable.emptyimage1)
                            .into(imageview2);
                    Picasso.with(mContext)
                            .load(vendormodelArrayList.get(pos).getVendorproducts().get(2).getImage())
                            .placeholder(R.drawable.emptyimage1)
                            .error(R.drawable.emptyimage1)
                            .into(imageview3);
                }
                else if(vendormodelArrayList.get(pos).getSize()==4){

                    Picasso.with(mContext)
                            .load(vendormodelArrayList.get(pos).getVendorproducts().get(0).getImage())
                            .placeholder(R.drawable.emptyimage1)
                            .error(R.drawable.emptyimage1)
                            .into(imageview1);
                    Picasso.with(mContext)
                            .load(vendormodelArrayList.get(pos).getVendorproducts().get(1).getImage())
                            .placeholder(R.drawable.emptyimage1)
                            .error(R.drawable.emptyimage1)
                            .into(imageview2);
                    Picasso.with(mContext)
                            .load(vendormodelArrayList.get(pos).getVendorproducts().get(2).getImage())
                            .placeholder(R.drawable.emptyimage1)
                            .error(R.drawable.emptyimage1)
                            .into(imageview3);
                    Picasso.with(mContext)
                            .load(vendormodelArrayList.get(pos).getVendorproducts().get(3).getImage())
                            .placeholder(R.drawable.emptyimage1)
                            .error(R.drawable.emptyimage1)
                            .into(imageview4);
                }

            }
            if(vendormodelArrayList.get(pos).getImage().equals("")){
                Picasso.with(mContext)
                        .load(R.drawable.emptyimage1)
                        .placeholder(R.drawable.emptyimage1)
                        .error(R.drawable.emptyimage1)
                        .into(grid_image);

            }else{
                Picasso.with(mContext)
                        .load(vendormodelArrayList.get(pos).getImage())
                        .placeholder(R.drawable.emptyimage1)
                        .error(R.drawable.emptyimage1)
                        .into(grid_image);

            }


            return view;

        }
    }
}
