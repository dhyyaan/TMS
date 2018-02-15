package com.takemysaree.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.takemysaree.R;
import com.takemysaree.activity.DashBoardActivity;
import com.takemysaree.fonts.LatoNormalEditText;
import com.takemysaree.models.MyApplication;
import com.takemysaree.utils.BaseUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by think360user on 21/6/17.
 */

public class FragmentRegister extends Fragment implements View.OnClickListener {
    private LatoNormalEditText email,password,input_name;
    private LinearLayout btnsignup;

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initVIews(view);
        initListeners();
        return view;
    }
    private void initListeners() {
        btnsignup.setOnClickListener(this);

    }

    private void initVIews(View view) {
        input_name=(LatoNormalEditText)view.findViewById(R.id.input_name);
        email=(LatoNormalEditText)view.findViewById(R.id.email);
        password=(LatoNormalEditText)view.findViewById(R.id.password);
        btnsignup=(LinearLayout)view.findViewById(R.id.btnsignup);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnsignup:
               if (email.getText().toString().equals("")){
                    email.setError("Enter email");
                    input_name.setError(null);
                    password.setError(null);
                } else if (!isValidEmail(email.getText().toString().trim())) {
                    email.setError("Enter Valid Mail ID");
                    input_name.setError(null);
                    password.setError(null);
                }   else if(password.getText().toString().equals("")){
                    password.setError("Enter Password");
                    email.setError(null);
                    input_name.setError(null);

                } else if(input_name.getText().toString().equals("")){
                email.setError(null);
                password.setError(null);
                input_name.setError("Enter name");
            }


                else{
                    RegisterAPi registerAPi=new RegisterAPi();
                    registerAPi.addQueue();
                }

                break;
        }

    }
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public class RegisterAPi {

        StringRequest sr;



        public RegisterAPi()
        {

            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.POST, BaseUrl.BASE_URL + BaseUrl.register, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.hide();
                    try{
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if(status.equals("true")){

                            JSONObject object2 = jsonObject.getJSONObject("data");
                            String id = object2.getString("user_id");
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences(BaseUrl.prfs, getActivity().MODE_PRIVATE).edit();
                            editor.putString("userid", id);

                            editor.commit();

                            Intent intent=new Intent(getActivity(),DashBoardActivity.class);
                            getActivity().startActivity(intent);
                            getActivity().finish();





                        }else{
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
                    } else if(error instanceof NoConnectionError){
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
                    }

                    else {
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


                    params.put("name",input_name.getText().toString().trim());
                    params.put("email",email.getText().toString());
                    params.put("password",password.getText().toString());



                    return params;
                }

            };
        }
        public void addQueue()
        {
            MyApplication.getInstance().addToRequestQueue(sr);

            sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }
    }
}
