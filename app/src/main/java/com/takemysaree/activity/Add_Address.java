package com.takemysaree.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.takemysaree.R;
import com.takemysaree.models.MyApplication;
import com.takemysaree.utils.BaseUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by think360user on 11/12/17.
 */

public class Add_Address extends Activity {
    Spinner spinner2,spinner3;
    ArrayList<String> arrayList=new ArrayList<>();
    ArrayList<String> arrayListid=new ArrayList<>();
    ArrayList<String> arrayList1=new ArrayList<>();
    ArrayList<String> arrayListid1=new ArrayList<>();
    ArrayAdapter<String> adapter;
    private
    ArrayAdapter<String> adapter1;
    private String spinners,post,kk,ll="";
    private SharedPreferences sharedpreferences;
    private String userid;
    private LinearLayout postprdct;
    private ImageView ivHamburger;
    private EditText city,zipcode,address,name,emailid,phoneno;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addaddress);
        sharedpreferences = getApplicationContext().getSharedPreferences(BaseUrl.prfs, Context.MODE_PRIVATE);
        userid = sharedpreferences.getString("userid", "");
        spinner2=(Spinner)findViewById(R.id.spinner1) ;
        ivHamburger=(ImageView)findViewById(R.id.ivHamburger);
        ivHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        spinner3=(Spinner)findViewById(R.id.spinner2) ;
        city=(EditText) findViewById(R.id.city) ;
        zipcode=(EditText) findViewById(R.id.zipcode) ;
        address=(EditText) findViewById(R.id.address) ;
        name=(EditText) findViewById(R.id.name) ;
        emailid=(EditText) findViewById(R.id.emailid) ;
        phoneno=(EditText) findViewById(R.id.phoneno) ;
        postprdct=(LinearLayout)findViewById(R.id.postprdct);
        CartList cartList=new CartList();
        cartList.addQueue();
        postprdct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kk.equals("")){

                    Toast.makeText(Add_Address.this,"Please Select Country" ,Toast.LENGTH_LONG).show();
                }else if(ll.equals("")){
                    Toast.makeText(Add_Address.this,"Please Select State" ,Toast.LENGTH_LONG).show();

                }else if(city.getText().toString().equals("")){
                    city.setError("Please Enter City");
                }else if(zipcode.getText().toString().equals("")){
                    zipcode.setError("Please Enter Zipcode");
                }else if(address.getText().toString().equals("")){
                    address.setError("Please Enter Address");
                }else if(name.getText().toString().equals("")){
                    name.setError("Please Enter name");
                }else if(emailid.equals("")){
                    emailid.setError("Please Enter emailid");
                }
                else if (!isValidEmail(emailid.getText().toString().trim())) {
                    emailid.setError("Enter Valid Mail ID");

                }else if(phoneno.getText().toString().equals("")){
                    phoneno.setError("Please Enter Phone no");
                }else{
PostAdd postAdd=new PostAdd();
postAdd.addQueue();
                }
            }
        });

    }
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public class PostAdd {

        StringRequest sr;


        public PostAdd() {

            final ProgressDialog pDialog = new ProgressDialog(Add_Address.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.POST, BaseUrl.BASE_URL + BaseUrl.add_address
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");

                            Intent intent=new Intent(Add_Address.this,CheckoutActivity.class);
                            intent.putExtra("address_id",jsonObject1.getString("address_id"));
                            startActivity(intent);
                            finish();

                        }



                        else {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(Add_Address.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(Add_Address.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(Add_Address.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(Add_Address.this);
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
                    params.put("user_id",userid);
                    params.put("address",address.getText().toString());
                    params.put("city",city.getText().toString());
                    params.put("state",ll);
                    params.put("country",kk);
                    params.put("zip_postal_code",zipcode.getText().toString());
                    params.put("phone",phoneno.getText().toString());
                    params.put("name",name.getText().toString());
                    params.put("email",emailid.getText().toString());


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
    public class CartList {

        StringRequest sr;


        public CartList() {

            final ProgressDialog pDialog = new ProgressDialog(Add_Address.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.GET, BaseUrl.BASE_URL + BaseUrl.list_country
                   , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {
                            JSONObject jsonObject2=jsonObject.getJSONObject("data");
                            JSONArray jsonArray=jsonObject2.getJSONArray("country");
                            arrayList.clear();
                            arrayListid.clear();
                            for(int j=0;j<jsonArray.length();j++){
                                JSONObject jsonObject21=jsonArray.getJSONObject(j);
                                arrayList.add(jsonObject21.getString("name"));
                                arrayListid.add(jsonObject21.getString("id"));
                            }
                            adapter = new ArrayAdapter<String>(Add_Address.this, R.layout.spinner_item, arrayList);
                            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            spinner2.setAdapter(adapter);
                            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Object item = parent.getItemAtPosition(position);
                                    spinners = parent.getItemAtPosition(position).toString();

                                    if (position==-1) {
                                        //post=String.valueOf(position);

                                    }else
                                    {

                                        post=String.valueOf(position);
                                        kk=arrayListid.get(position);
                                        CarSubtype carsubtype=new CarSubtype();
                                        carsubtype.addQueue();
                                        Log.e("TAG",kk);


                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        }



                        else {





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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(Add_Address.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(Add_Address.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(Add_Address.this);
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
    public class CarSubtype {

        StringRequest sr;


        public CarSubtype() {

            final ProgressDialog pDialog = new ProgressDialog(Add_Address.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.GET, BaseUrl.BASE_URL + BaseUrl.list_state+"&country_id="+
                    kk
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {
                            JSONObject jsonObject2=jsonObject.getJSONObject("data");
                            JSONArray jsonArray=jsonObject2.getJSONArray("state");
                            arrayList1.clear();
                            arrayListid1.clear();
                            for(int j=0;j<jsonArray.length();j++){

                                JSONObject jsonObject21=jsonArray.getJSONObject(j);
                                arrayList1.add(jsonObject21.getString("name"));
                                arrayListid1.add(jsonObject21.getString("id"));
                            }
                            adapter1 = new ArrayAdapter<String>(Add_Address.this, R.layout.spinner_item, arrayList1);
                            adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            spinner3.setAdapter(adapter1);
                            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Object item = parent.getItemAtPosition(position);
                                    spinners = parent.getItemAtPosition(position).toString();

                                    if (position==-1) {
                                        //post=String.valueOf(position);

                                    }else
                                    {
                                        ll=arrayListid1.get(position).toString();
                                       // post=String.valueOf(position);



                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        }



                        else {





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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(Add_Address.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(Add_Address.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(Add_Address.this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(Add_Address.this,CheckoutActivity.class);
        startActivity(intent);
        finish();
    }
}
