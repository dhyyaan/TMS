package com.takemysaree.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.takemysaree.R;
import com.takemysaree.models.MyApplication;
import com.takemysaree.utils.BaseUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivityforsocials extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private LoginButton loginButton;
    private TextView btnContinueWithFb,btnContinueWithGoogle,btnContinueWithEmail;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient = null;
    private static final int RC_SIGN_IN = 007;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(LoginActivityforsocials.this);
        setContentView(R.layout.activity_login);
        LoginManager.getInstance().logOut();
        TextView tv = (TextView)findViewById(R.id.tvTAndC);
        btnContinueWithFb = (TextView)findViewById(R.id.btnContinueWithFb);
        btnContinueWithGoogle = (TextView)findViewById(R.id.btnContinueWithGoogle);
        btnContinueWithEmail = (TextView)findViewById(R.id.btnContinueWithEmail);
        WebView v = (WebView)findViewById(R.id.webview);
        v.setWebViewClient(new MyBrowser());
        v.getSettings().setLoadsImagesAutomatically(true);
        v.getSettings().setJavaScriptEnabled(true);

        v.loadUrl("http://tms.himview.com/api/index.php?/12345/image_slider");
        SpannableString s_text_login = new SpannableString("Terms &amp; Conditions and ");
        loginButton = (LoginButton) findViewById(R.id.loginButton);
        loginButton.setReadPermissions("public_profile email");
        callbackManager = CallbackManager.Factory.create();
        if (com.facebook.AccessToken.getCurrentAccessToken() != null) {
            RequestData();
        }
        btnContinueWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivityforsocials.this,Activity_LoginRegister.class);
                startActivity(intent);
                finish();
            }
        });
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                RequestData();
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getApplicationContext(), "User has Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getApplicationContext(), "You have no Internet Connected", Toast.LENGTH_SHORT).show();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        btnContinueWithFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
            }
        });
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {


            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        btnContinueWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        s_text_login.setSpan(clickableSpan, 0, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(98,209,193));
        s_text_login.setSpan(fcs, 0, 22, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //For UnderLine
        s_text_login.setSpan(new UnderlineSpan(),0,22,0);
        tv.setText(s_text_login);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setHighlightColor(Color.TRANSPARENT);
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {


            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        SpannableString s_text_login2 = new SpannableString("Privacy Policy");
        s_text_login2.setSpan(clickableSpan, 0, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(98,209,193));
        s_text_login2.setSpan(fcs2, 0, 14, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tv.append(s_text_login2);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 64206) //facebook
        {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        else if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            updateUI(true);
        }
        super.onActivityResult(requestCode, resultCode, data);
    } private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String mailId = acct.getEmail();
            String name = acct.getDisplayName();
            LoginAPi loginAPi=new LoginAPi(mailId,name);
            loginAPi.addQueue();

            /*RegisterAPI();
            MyUtils.getInstance().addToRequestQueue(sr);*/
            Log.i("", "");
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void RequestData() {
        GraphRequest request = GraphRequest.newMeRequest(com.facebook.AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                JSONObject json = response.getJSONObject();

                if (json != null) {
                    String id = json.optString("id");
                    String name = json.optString("name");
                    String email = json.optString("email");
                    LoginAPi loginAPi=new LoginAPi(email,name);
                    loginAPi.addQueue();
                    //String imageLink = "http://graph.facebook.com/" + id + "/picture?type=large";


                }

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
            mGoogleApiClient.clearDefaultAccountAndReconnect();
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            signOut();
            revokeAccess();
        } else {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
            mGoogleApiClient.clearDefaultAccountAndReconnect();
        }
    }

    public class LoginAPi {

        StringRequest sr;


        public LoginAPi(final String email,final String name) {


            final ProgressDialog pDialog = new ProgressDialog(LoginActivityforsocials.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
            sr = new StringRequest(Request.Method.POST, BaseUrl.BASE_URL + BaseUrl.social_login, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        final JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("true")) {

                            JSONObject object2 = jsonObject.getJSONObject("data");
                            String id = object2.getString("user_id");
                            SharedPreferences.Editor editor = getSharedPreferences(BaseUrl.prfs, LoginActivityforsocials.this.MODE_PRIVATE).edit();
                            editor.putString("userid", id);
                            editor.putString("name", object2.optString("name"));
                            editor.putString("email", object2.optString("email"));
                            editor.commit();

                            Intent intenta = new Intent(LoginActivityforsocials.this, DashBoardActivity.class);
                           startActivity(intenta);
                           finish();


                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivityforsocials.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivityforsocials.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivityforsocials.this);
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivityforsocials.this);
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
                    params.put("email", email);
                    params.put("name", name);
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
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(LoginActivityforsocials.this,TutorialActivity.class);
        startActivity(intent);
        finish();
    }
}
