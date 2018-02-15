package com.takemysaree.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.takemysaree.R;
import com.takemysaree.activity.DashBoardActivity;
import com.takemysaree.activity.Login;
import com.takemysaree.activity.ProgressHUD;
import com.takemysaree.fonts.LatoNormalEditText;
import com.takemysaree.models.MyApplication;
import com.takemysaree.utils.BaseUrl;
import com.vistrav.ask.Ask;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by think360user on 20/4/17.
 */

public class FragmentLogin extends Fragment implements View.OnClickListener {
    private LinearLayout login;
    private LatoNormalEditText email, password;
    ProgressDialog pDialog;
    private ProgressHUD mProgressHUD;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initVIews(view);
        initListeners();

        return view;
    }

    private void initListeners() {
        login.setOnClickListener(this);

    }

    private void initVIews(View view) {
        login = (LinearLayout) view.findViewById(R.id.login);
        email = (LatoNormalEditText) view.findViewById(R.id.email);
        password = (LatoNormalEditText) view.findViewById(R.id.password);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                if (email.getText().toString().equals("")) {
                    email.setError("Enter email");
                    password.setError(null);
                } else if (!isValidEmail(email.getText().toString().trim())) {
                    email.setError("Enter Valid Mail ID");
                    password.setError(null);
                } else if (password.getText().toString().equals("")) {
                    password.setError("Enter Password");
                    email.setError(null);

                } else {
                    LoginAPi registerAPi = new LoginAPi();
                    registerAPi.addQueue();
                }

                break;
        }
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public class LoginAPi {

        StringRequest sr;


        public LoginAPi() {

            mProgressHUD = ProgressHUD.show(getActivity(), getResources().getString(R.string.pleasewait), true, false, null);

            sr = new StringRequest(Request.Method.POST, BaseUrl.BASE_URL + BaseUrl.login, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (mProgressHUD.isShowing() && mProgressHUD != null) {
                        mProgressHUD.dismiss();
                    }
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {

                            JSONObject object2 = jsonObject.getJSONObject("data");
                            String id = object2.getString("user_id");
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences(BaseUrl.prfs, getActivity().MODE_PRIVATE).edit();
                            editor.putString("userid", id);
                            editor.putString("name", object2.optString("name"));
                            editor.putString("email", object2.optString("email"));
                            editor.commit();

                            Intent intenta = new Intent(getActivity(), DashBoardActivity.class);
                            getActivity().startActivity(intenta);
                            getActivity().finish();


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
                    if (mProgressHUD.isShowing() && mProgressHUD != null) {
                        mProgressHUD.dismiss();
                    }
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
                    params.put("email", email.getText().toString());
                    params.put("password", password.getText().toString());
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
    public void onDestroy(){
        super.onDestroy();
        if ( pDialog!=null && pDialog.isShowing() ){
            pDialog.dismiss();
        }
    }
}
