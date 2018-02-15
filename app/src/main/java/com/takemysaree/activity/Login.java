package com.takemysaree.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.takemysaree.models.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Login extends Activity {
	private LinearLayout horizontalOuterLayout;
	private HorizontalScrollView horizontalScrollview;
	private TextView horizontalTextView;
	private int scrollMax;
	private int scrollPos =	0;
	private TimerTask clickSchedule;
	private TimerTask scrollerSchedule;
	private TimerTask faceAnimationSchedule;
	private ImageView clickedButton	=	null;
	private Timer scrollTimer		=	null;
	private ArrayList<String> imagearray=new ArrayList<>();
	private Timer clickTimer		=	null;
	private Timer faceTimer         =   null;
	private Button imageButton;
	private Boolean isFaceDown      =   true;
	private String[] nameArray = {"Apple", "Banana", "Grapes", "Orange", "Strawberry","Apple", "Banana"};
	private String[] imageNameArray = {"apple", "banana", "grapes", "orange", "strawberry","apple", "banana"};
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        horizontalScrollview  = (HorizontalScrollView) findViewById(R.id.horiztonal_scrollview_id);
        horizontalOuterLayout =	(LinearLayout)findViewById(R.id.horiztonal_outer_layout_id);
        horizontalTextView    = (TextView)findViewById(R.id.horizontal_textview_id);
        
        horizontalScrollview.setHorizontalScrollBarEnabled(false);
        addImagesToView();

        
		ViewTreeObserver vto 		=	horizontalOuterLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
            	horizontalOuterLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            	getScrollMaxAmount();
            	startAutoScrolling();            	          	
            }
        });
    }
    
    public void getScrollMaxAmount(){
    	int actualWidth = (horizontalOuterLayout.getMeasuredWidth()-512);
    	scrollMax   = actualWidth;
    }
    
    public void startAutoScrolling(){
		if (scrollTimer == null) {
			scrollTimer					=	new Timer();
			final Runnable Timer_Tick 	= 	new Runnable() {
			    public void run() {
			    	moveScrollView();
			    }
			};
			
			if(scrollerSchedule != null){
				scrollerSchedule.cancel();
				scrollerSchedule = null;
			}
			scrollerSchedule = new TimerTask(){
				@Override
				public void run(){
					runOnUiThread(Timer_Tick);
				}
			};
			
			scrollTimer.schedule(scrollerSchedule, 30, 30);
		}
	}
    
    public void moveScrollView(){
    	scrollPos							= 	(int) (horizontalScrollview.getScrollX() + 1.0);
		if(scrollPos >= scrollMax){
			scrollPos						=	0;
		}
		horizontalScrollview.scrollTo(scrollPos, 0);
				
	}
    
    /** Adds the images to view. */
    public void addImagesToView(){
		for (int i=0;i<imageNameArray.length;i++){
			 imageButton =	new Button(this);
			imageButton.setPadding(5,0,5,0);

			int imageResourceId		 =	getResources().getIdentifier(imageNameArray[i], "drawable",getPackageName());
		   Drawable image 			 =	this.getResources().getDrawable(R.drawable.intro
		   );

		    imageButton.setBackgroundDrawable(image);
		    imageButton.setTag(i);
		    imageButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					if(isFaceDown){
						if(clickTimer!= null){
							clickTimer.cancel();
							clickTimer			=	null;
						}
						clickedButton			=	(ImageView)arg0;
						stopAutoScrolling();
						clickedButton.startAnimation(scaleFaceUpAnimation());
						clickedButton.setSelected(true);
						clickTimer				=	new Timer();

						if(clickSchedule != null) {
							clickSchedule.cancel();
							clickSchedule = null;
						}

						clickSchedule = new TimerTask(){
				        	public void run() {
				        		startAutoScrolling();
				            }
				        };

						clickTimer.schedule( clickSchedule, 1500);
					}
				}
			});

			LinearLayout.LayoutParams params 	=	new LinearLayout.LayoutParams(256,256);
			params.setMargins(0, 25, 0, 25);
			imageButton.setLayoutParams(params);
			horizontalOuterLayout.addView(imageButton);
		}
	}
    
    public Animation scaleFaceUpAnimation(){
		Animation scaleFace = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		scaleFace.setDuration(500);
		scaleFace.setFillAfter(true);
		scaleFace.setInterpolator(new AccelerateInterpolator());
		Animation.AnimationListener	scaleFaceAnimationListener = new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
				horizontalTextView.setText(nameArray[(Integer) clickedButton.getTag()]);
				isFaceDown = false;
			}			
			@Override
			public void onAnimationRepeat(Animation arg0) {}
			@Override
			public void onAnimationEnd(Animation arg0) {
				if(faceTimer != null){
					faceTimer.cancel();
					faceTimer = null;
				}
				
				faceTimer = new Timer();
				if(faceAnimationSchedule != null){
					faceAnimationSchedule.cancel();
					faceAnimationSchedule = null;
				}
				faceAnimationSchedule = new TimerTask() {
					@Override
					public void run() {
						faceScaleHandler.sendEmptyMessage(0); 										
					}
				};
				
				faceTimer.schedule(faceAnimationSchedule, 750);				
			}
		}; 
		scaleFace.setAnimationListener(scaleFaceAnimationListener);
		return scaleFace;
	}
    
    private Handler faceScaleHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	if(clickedButton.isSelected() == true)
        		clickedButton.startAnimation(scaleFaceDownAnimation(500));		
        }
	};
	
	public Animation scaleFaceDownAnimation(int duration){
		Animation scaleFace = new ScaleAnimation(1.2f, 1.0f, 1.2f, 1.0f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		scaleFace.setDuration(duration);
		scaleFace.setFillAfter(true);
		scaleFace.setInterpolator(new AccelerateInterpolator());
		Animation.AnimationListener	scaleFaceAnimationListener = new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {}
			@Override
			public void onAnimationRepeat(Animation arg0) {}
			@Override
			public void onAnimationEnd(Animation arg0) {
				horizontalTextView.setText("");
				isFaceDown = true;				
			}
		}; 
		scaleFace.setAnimationListener(scaleFaceAnimationListener);
		return scaleFace;
	}
    
    public void stopAutoScrolling(){
		if (scrollTimer != null) {
			scrollTimer.cancel();
			scrollTimer	=	null;
		}
	}
    
    public void onBackPressed(){
		super.onBackPressed();
		finish();
	}
	
	public void onPause() {
		super.onPause();
		finish();
	}
	
	public void onDestroy(){
		clearTimerTaks(clickSchedule);
		clearTimerTaks(scrollerSchedule);
		clearTimerTaks(faceAnimationSchedule);		
		clearTimers(scrollTimer);
		clearTimers(clickTimer);
		clearTimers(faceTimer);
		
		clickSchedule         = null;
		scrollerSchedule      = null;
		faceAnimationSchedule = null;
		scrollTimer           = null;
		clickTimer            = null;
		faceTimer             = null;
		super.onDestroy();
	}
	
	private void clearTimers(Timer timer){
	    if(timer != null) {
		    timer.cancel();
	        timer = null;
	    }
	}
	
	private void clearTimerTaks(TimerTask timerTask){
		if(timerTask != null) {
			timerTask.cancel();
			timerTask = null;
		}
	}
	public class GetImages {

		StringRequest sr;

		public GetImages() {

			final ProgressDialog pDialog = new ProgressDialog(Login.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.show();

			sr = new StringRequest(Request.Method.GET, "http://think360.in/blood_donation/image.php"
					, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					pDialog.dismiss();
					try {
						final JSONObject jsonObject = new JSONObject(response);
						String status = jsonObject.getString("status");

						if (status.equals("true")) {
JSONArray jsonArray=jsonObject.getJSONArray("data");
							for(int i=0;i<jsonArray.length();i++){
								imagearray.add(jsonArray.getString(i));
							}

							//addImagesToView(imagearray);

						} else if (status.equals("false")) {


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
						AlertDialog.Builder builder1 = new AlertDialog.Builder(Login.this);
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
						AlertDialog.Builder builder1 = new AlertDialog.Builder(Login.this);
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
						AlertDialog.Builder builder1 = new AlertDialog.Builder(Login.this);
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
}
