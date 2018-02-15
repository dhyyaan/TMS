package com.takemysaree.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.takemysaree.R;
import com.takemysaree.adapter.CustomerListAdapter;
import com.takemysaree.fonts.LatoBoldTextview;
import com.takemysaree.listeners.OnCustomerListChangedListener;
import com.takemysaree.listeners.OnStartDragListener;
import com.takemysaree.models.Customer;
import com.takemysaree.models.F1models;
import com.takemysaree.models.MyApplication;
import com.takemysaree.tabfragments.CategoriesFragment;
import com.takemysaree.utils.BaseUrl;
import com.takemysaree.utils.SimpleItemTouchHelperCallback;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListOfMultiProduct extends AppCompatActivity
        implements OnCustomerListChangedListener,
        OnStartDragListener {

    private Toolbar mToolbar;

    private RecyclerView mRecyclerView;
    private CustomerListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ItemTouchHelper mItemTouchHelper;
    private List<F1models> mCustomers;
private LatoBoldTextview title;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    public static final String LIST_OF_SORTED_DATA_ID = "json_list_sorted_data_id";
    public final static String PREFERENCE_FILE = "preference_file";
    ArrayList<F1models> arrayList;
    ArrayList<String> ids=new ArrayList<>();
    private LatoBoldTextview postproduct;
    private ImageView ivHamburger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        ivHamburger=(ImageView)findViewById(R.id.ivHamburger);
        ivHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ListOfMultiProduct.this);
                builder1.setMessage("Are you sure you do not want to rearrange order of this product?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(ListOfMultiProduct.this, DashBoardActivity.class);
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
        title=(LatoBoldTextview)findViewById(R.id.title);
        postproduct=(LatoBoldTextview)findViewById(R.id.postproduct);
        setSupportActionBar(mToolbar);
        arrayList = new ArrayList<>();
        mSharedPreferences = this.getApplicationContext()
                .getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();


        setupRecyclerView();
    }

    public class Category {

        StringRequest sr;


        public Category() {

            final ProgressDialog pDialog = new ProgressDialog(ListOfMultiProduct.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.GET, BaseUrl.BASE_URL +
                    BaseUrl.get_chid_product + "&product_id=" + getIntent().getStringExtra("productid"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {

                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            JSONArray jsonarray = jsonObject1.getJSONArray("child");
                            arrayList.clear();
                            for (int i = 0; i < jsonarray.length(); i++) {

                                JSONObject jsonobect1 = jsonarray.getJSONObject(i);

                                F1models f1models = new F1models();

                                String id = jsonobect1.getString("product_id");
                                String name = jsonobect1.getString("name");
                                String price_sell = jsonobect1.getString("price_sell");
                                String price_rent = jsonobect1.getString("price_rent");
                                String image = jsonobect1.getString("image");
                                f1models.setCatid(id);
                                f1models.setRentprice(price_rent);
                                f1models.setSellprice(price_sell);
                                f1models.setProductname(name);
                                f1models.setSize(image);
                                arrayList.add(f1models);

                            }
                            title.setText(jsonObject1.getString("main_title"));
                            //setup the adapter with empty list
                            mAdapter = new CustomerListAdapter(arrayList, ListOfMultiProduct.this, ListOfMultiProduct.this, ListOfMultiProduct.this);
                            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
                            mItemTouchHelper = new ItemTouchHelper(callback);
                            mItemTouchHelper.attachToRecyclerView(mRecyclerView);


                            mRecyclerView.setAdapter(mAdapter);
                        } else {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ListOfMultiProduct.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ListOfMultiProduct.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ListOfMultiProduct.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ListOfMultiProduct.this);
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

    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.note_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Category category = new Category();
        category.addQueue();
        postproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomers = getSampleData();
                ids.clear();
                for(int j=0;j<mCustomers.size();j++){
                    ids.add(mCustomers.get(j).getCatid());
                }
                UpdateChildorder updateChildorder=new UpdateChildorder(ids);
                updateChildorder.addQueue();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNoteListChanged(ArrayList<F1models> arrayList) {
        //after drag and drop operation, the new list of Customers is passed in here

        //create a List of Long to hold the Ids of the
        //Customers in the List
        List<String> listOfSortedCustomerId = new ArrayList<String>();

        for (F1models f1models : arrayList) {
            listOfSortedCustomerId.add(f1models.getCatid());
        }

        //convert the List of Longs to a JSON string
        Gson gson = new Gson();
        String jsonListOfSortedCustomerIds = gson.toJson(listOfSortedCustomerId);


        //save to SharedPreference
        mEditor.putString(LIST_OF_SORTED_DATA_ID, jsonListOfSortedCustomerIds).commit();
        mEditor.commit();


    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);

    }

    private List<F1models> getSampleData() {

        //Get the sample data
        List<F1models> customerList = arrayList;

        //create an empty array to hold the list of sorted Customers
        List<F1models> sortedCustomers = new ArrayList<F1models>();

        //get the JSON array of the ordered of sorted customers
        String jsonListOfSortedCustomerId = mSharedPreferences.getString(LIST_OF_SORTED_DATA_ID, "");


        //check for null
        if (!jsonListOfSortedCustomerId.isEmpty()) {

            //convert JSON array into a List<Long>
            Gson gson = new Gson();
            List<Long> listOfSortedCustomersId = gson.fromJson(jsonListOfSortedCustomerId, new TypeToken<List<Long>>() {
            }.getType());

            //build sorted list
            if (listOfSortedCustomersId != null && listOfSortedCustomersId.size() > 0) {
                for (Long id : listOfSortedCustomersId) {
                    for (F1models customer : customerList) {
                        if (customer.getCatid().equals(id)) {
                            sortedCustomers.add(customer);
                            customerList.remove(customer);
                            break;
                        }
                    }
                }
            }

            //if there are still customers that were not in the sorted list
            //maybe they were added after the last drag and drop
            //add them to the sorted list
            if (customerList.size() > 0) {
                sortedCustomers.addAll(customerList);
            }

            return sortedCustomers;
        } else {
            return customerList;
        }
    }
    public class UpdateChildorder {

        StringRequest sr;


        public UpdateChildorder(final ArrayList<String> id) {

            final ProgressDialog pDialog = new ProgressDialog(ListOfMultiProduct.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.POST, BaseUrl.BASE_URL +
                    BaseUrl.update_chid_order , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ListOfMultiProduct.this);
                            builder1.setMessage(message);
                            builder1.setCancelable(false);

                            builder1.setPositiveButton(
                                    "Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent=new Intent(ListOfMultiProduct.this,DashBoardActivity.class);
                                            startActivity(intent);
                                            dialog.cancel();
                                            finish();
                                        }
                                    });


                            AlertDialog alert11 = builder1.create();
                            alert11.show();

                        } else {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ListOfMultiProduct.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ListOfMultiProduct.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ListOfMultiProduct.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ListOfMultiProduct.this);
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
                    JSONArray jsonArray=new JSONArray(id);
                    params.put("product_order",jsonArray.toString());


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ListOfMultiProduct.this);
        builder1.setMessage("Are you sure you do not want to rearrange order of this product?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(ListOfMultiProduct.this, DashBoardActivity.class);
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
