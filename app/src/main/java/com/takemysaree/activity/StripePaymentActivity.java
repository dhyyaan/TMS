package com.takemysaree.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardNumberEditText;
import com.stripe.android.view.ExpiryDateEditText;
import com.stripe.android.view.StripeEditText;
import com.takemysaree.R;
import com.takemysaree.models.MyApplication;
import com.takemysaree.utils.BaseUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class StripePaymentActivity extends AppCompatActivity implements View.OnClickListener {
    private AppCompatButton btnPayDollar;
    private ImageView ivBackArrow;
    private StringRequest sr;
    private String exdate;
    private ProgressDialog progress_creating_token;
    private SharedPreferences sharedpreferences;

    private String PUBLISHABLE_KEY = "pk_test_22zFpNK6ZpBs4qh8Pm9RAmh0";
    private CardNumberEditText et_card_number;
    private ExpiryDateEditText et_expiry_date;
    private StripeEditText et_cvc_number;
    private String order_id,total_price;
    private TextView buytext,amnt;
    private LinearLayout buylyt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_payment);
        total_price=getIntent().getStringExtra("total_price");
        order_id=getIntent().getStringExtra("order_id");
        initViews();
        initListeners();
    }

    private void initListeners() {
        buylyt.setOnClickListener(this);
        ivBackArrow.setOnClickListener(this);
    }

    private void initViews() {
        et_card_number = (CardNumberEditText) findViewById(R.id.et_card_number);
        et_expiry_date = (ExpiryDateEditText) findViewById(R.id.et_expiry_date);
        et_cvc_number = (StripeEditText) findViewById(R.id.et_cvc_number);
        buytext = (TextView) findViewById(R.id.buytext);
        amnt = (TextView) findViewById(R.id.amnt);
        ivBackArrow = (ImageView) findViewById(R.id.ivBackArrow);
        buylyt=(LinearLayout)findViewById(R.id.buylyt);
        progress_creating_token = new ProgressDialog(this);
        progress_creating_token.setMessage("Loading");
        buytext.setText("Pay  $" + total_price);
        amnt.setText("$" + total_price);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buylyt:
                if (isDataComplete()) {

                    exdate = et_expiry_date.getText().toString();
                    if (validateCardExpiryDate(exdate)) {
                        String cardNumber = et_card_number.getText().toString();

                        StringTokenizer stringTokenizer = new StringTokenizer(exdate, "/");
                        int cardExpMonth = Integer.parseInt(stringTokenizer.nextToken());
                        int cardExpYear = Integer.parseInt(stringTokenizer.nextToken());

                        String cardCVC = et_cvc_number.getText().toString();

                        Stripe stripe = new Stripe(StripePaymentActivity.this, PUBLISHABLE_KEY);

                        // Create Card instance containing customer's payment
                        // information obtained
                        Card card = new Card(cardNumber, cardExpMonth, cardExpYear,
                                cardCVC);

                        // Check if card is valid. If valid, create token
                        if (card.validateCard()) {

                            startProgress();
                            stripe.createToken(card, new TokenCallback() {

                                @Override
                                public void onSuccess(Token token) {
                                    stopProgress();

                                    //  Toast.makeText(StripePaymentActivity.this, "Token created successfully!", Toast.LENGTH_SHORT).show();
                                    Log.d("token", "" + token);
                                    Log.d("token", "" + token);
                                    OrderCreate orderCreate=new OrderCreate(token.getId());
                                    orderCreate.addQueue();



                                }

                                @Override
                                public void onError(Exception error) {
                                    stopProgress();
                                    showAlert("Validation Error",
                                            error.getLocalizedMessage());

                        /*Log.e("Error in creating token.",
                                error.toString());*/
                                }
                            });
                        } else {
                            showAlert("Invalid Details",
                                    "Card details are invalid. Enter valid data");
                            et_card_number.setText(null);
                            et_cvc_number.setText(null);
                            et_expiry_date.setText(null);
                        }

                    } else {
                        et_expiry_date.setError("Enter valid card number");

                    }


                }

                break;
            case R.id.ivBackArrow:
                onBackPressed();
                break;
            default:
                break;
        }
    }
    public class OrderCreate {

        StringRequest sr;


        public OrderCreate(final String token) {

            final ProgressDialog pDialog = new ProgressDialog(StripePaymentActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            sr = new StringRequest(Request.Method.POST, BaseUrl.BASE_URL + BaseUrl.order_payment, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {

                            Intent intent=new Intent(StripePaymentActivity.this,OrderActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(StripePaymentActivity.this);
                            builder1.setMessage(message);
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent=new Intent(StripePaymentActivity.this,MyCart.class);
                                            startActivity(intent);
                                            finish();
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(StripePaymentActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(StripePaymentActivity.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(StripePaymentActivity.this);
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
                    params.put("order_id",order_id);
                    params.put("token",token);
                    params.put("amount",total_price);


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


    private void showAlert(String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // dialog.dismiss();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    boolean validateCardExpiryDate(String expiryDate) {
        return expiryDate.matches("(?:0[1-9]|1[0-2])/[0-9]{2}");
    }

    public void startProgress() {
        progress_creating_token.show();
    }

    public void stopProgress() {
        if (progress_creating_token.isShowing()) {
            progress_creating_token.dismiss();
        }
    }

    private boolean isDataComplete() {
        boolean isDataComplete = true;

        if (et_card_number.getText().length() == 0) {
            isDataComplete = false;
            et_card_number.setError("Enter Card Number");
        }

        if (et_cvc_number.getText().length() == 0) {
            isDataComplete = false;
            et_cvc_number.setError("Enter CVC Number");
        }
        if (et_expiry_date.getText().length() == 0) {
            isDataComplete = false;
            et_expiry_date.setError("Enter Expiry Date");
        }


        return isDataComplete;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }


}