package com.mappingroadconditions;

import java.io.IOException;

import android.app.ActionBar;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.net.MalformedURLException;
import org.apache.http.client.ClientProtocolException;
import android.content.Context;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.ActivityRecognition;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.UserAuthenticationCallback;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ExecutionException;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.app.AlarmManager;


public class MainActivity extends Activity implements ConnectionCallbacks,
		OnConnectionFailedListener {
    public PendingIntent pendingIntentAlarm;
    public AlarmManager managerAlarm;
    public static boolean isGPRSAllowed = true;
	private MobileServiceClient mClient = null;
	public static String IMEI = " ";
	PendingIntent mActivityRecognitionPendingIntent;
	PendingIntent mLocationStuff;
	TelephonyManager telephonyManager;
	PhoneStateListener listener;
	public String phonestate = "Idle";
	Context context;
	public static final String SHAREDPREFFILE = "temp";
	public static final String USERIDPREF = "uid";
	public static final String TOKENPREF = "tkn";
	ImageButton button1;
	GoogleApiClient cl;
	ImageButton button2;
    public static AssetManager assetManager;
    public static Context context_;
	private static final int REQUEST_RESOLVE_ERROR = 1001;
	private static final String DIALOG_ERROR = "dialog_error";
	private boolean mResolvingError = false;
	public boolean bAuthenticating = false;
	public Handler handler_upload = new Handler();
	public Handler handler_gather = new Handler();
	public final Object mAuthenticationLock = new Object();
	public static boolean hasExternalStorage = false;
	public static String Activity = " ";
	public TextView tv;
    public Button button,button3,button4;
    //public WebView wv;
/*	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			if (new ConnectionDetector().isConnectingToInternet()) {
				new UploadClassificationData().Upload();
			    handler_upload.postDelayed(this, 3600000);// do this once every
				
			} else {
            //    Log.i("MappingRoadConditions", "Not Connected to Internet.");
                handler_upload.postDelayed(this, 3600000);// do this once every hour
            }
		}
	};

	private Runnable runnable1 = new Runnable() {

		@Override
		public void run() {
			boolean gotGPS = false;
			int resultCode = GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(context);
			if (resultCode == ConnectionResult.SUCCESS) {
			//	Log.i("MappingRoadConditions", "Got google play services");
				

				LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					gotGPS = true;
				//	Log.i("MappingRoadConditions", " geo sensors!"); // good to go
                    System.out.println("geo sensors");
				} else {
					gotGPS = false;
				//	Log.i("MappingRoadConditions", "No geo sensors");
				}

			} else {
			//	Log.i("MappingRoadConditions", "No google play services");
                ;
			}

			telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			listener = new PhoneStateListener() {

				@Override
				public void onCallStateChanged(int state, String incomingNumber) {

					switch (state) {
					case TelephonyManager.CALL_STATE_IDLE:
						phonestate = "Idle";
						break;
					case TelephonyManager.CALL_STATE_OFFHOOK:
						phonestate = "Off Hook";
						break;
					case TelephonyManager.CALL_STATE_RINGING:
						phonestate = "Ringing";
						break;
					}
				//	Log.i("MappingRoadConditions", "Phone State:" + phonestate);
				}
			};

			// Register the listener with the telephony manager
			telephonyManager.listen(listener,
					PhoneStateListener.LISTEN_CALL_STATE);
			boolean act = false;
			synchronized (Activity) {
				act = Activity.equals("IN_VEHICLE");
			}
		//	Log.i("MappingRoadConditions","Phonestate: "+phonestate);
			if (resultCode == ConnectionResult.SUCCESS  && act // FOR FINAL APP ONLY :O
					&& phonestate.equals("Idle")
					&& new ConnectionDetector().isConnectingToInternet() && gotGPS) {
				Intent service = new Intent(context_, GatherSensorData.class);
				startService(service);
				
			} 
		/*	if (!phonestate.equals("Idle"))
				Log.i("MappingRoadConditions", "Phone state is not idle");
			 if (!act)
				Log.i("MappingRoadConditions", "Not in a moving vehicle");
			if ( !new ConnectionDetector().isConnectingToInternet())
				Log.i("MappingRoadConditions", "Net is not available");
			if ( !gotGPS)
				Log.i("MappingRoadConditions", "GPS is not available");
		*/
/*			handler_gather.postDelayed(this, 900000);

		}
	};
*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = (Context) this;
        context_ = (Context) this;
        button = (Button)  findViewById(R.id.congestion);
        button3 = (Button)  findViewById(R.id.pothole);
        button4 = (Button)  findViewById(R.id.speedbreaker);
        button3.setBackgroundColor(Color.parseColor("#4686A0"));
        button4.setBackgroundColor(Color.parseColor("#4686A0"));
        button.setBackgroundColor(Color.parseColor("#4686A0"));
        button1 = (ImageButton) findViewById(R.id.Google);
        button2 = (ImageButton) findViewById(R.id.Facebook);
        tv = (TextView) findViewById(R.id.text_id);
        tv.setVisibility(tv.INVISIBLE);
        button.setVisibility(button.INVISIBLE);
        button3.setVisibility(button3.INVISIBLE);
        button4.setVisibility(button4.INVISIBLE);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4686A0")));
        bar.setIcon(R.drawable.craterviwhite);


        // if items are available, then move on, else don't
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        Sensor senAccelerometer, senGravity, senRotationVector;
        SensorManager senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senGravity = senSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        senRotationVector = senSensorManager
                .getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if (resultCode == ConnectionResult.SUCCESS && senAccelerometer != null && senGravity != null && senRotationVector != null ) {
            // all good
            //
            Initialize();
            cl = new GoogleApiClient.Builder(context)
                    .addApi(ActivityRecognition.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            cl.connect();
      /*      wv.getSettings().setLoadsImagesAutomatically(true);
            wv.getSettings().setJavaScriptEnabled(true);
            wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            wv.setWebViewClient(new MyAppWebViewClient());
            wv.loadUrl("http://mappingroadconditions.azurewebsites.net/"); */

            try {
                mClient = new MobileServiceClient(
                        "https://mappingroadconditions.azure-mobile.net/congestion.html",
                        "PKkPbdojnJJGtUCcJikgYqasiJoOWm49", this)
                        .withFilter(new RefreshTokenCacheFilter());
            } catch (MalformedURLException e) {
              //  Log.i("MappingRoadConditions", "Error in creating mClient");

                e.printStackTrace();
            }

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
             //   Log.i("MappingRoadConditions", "In click function to open new activity");
                Intent intent = new Intent(context_,CongestionMap.class);
                startActivity(intent);
                }
            });

            button3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                 //   Log.i("MappingRoadConditions", "In click function to open potholes activity");
                    Intent intent = new Intent(context_, PotholesMap.class);
                    startActivity(intent);
                }
            });
            button4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                  //  Log.i("MappingRoadConditions", "In click function to open speedbreakers activity");
                    Intent intent = new Intent(context_, SpeedBreakersMap.class);
                    startActivity(intent);
                }
            });

            button1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                    //    Log.i("MappingRoadConditions", "Pressed google button");
                        authenticate_with_Google(false);
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                      //  Log.i("MappingRoadConditions", "Pressed facebook button");
                        authenticate(false);
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            tv.setVisibility(tv.VISIBLE);
            tv.setText("Welcome to Crater! Thank you for installing our app and contributing to your community. Login to begin.");
            }
        else
        {
           button1.setVisibility(button1.INVISIBLE);
            button2.setVisibility(button2.INVISIBLE);
            tv.setVisibility(tv.VISIBLE);
            button.setVisibility(button.INVISIBLE);
            button3.setVisibility(button3.INVISIBLE);
            button4.setVisibility(button4.INVISIBLE);
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	void Proceed() {

        tv.setText("Go through the maps linked below to discover potholes, speedbreakers and congestion in your area!");
        try {
         //   Log.i("MappingRoadConditions", "printing version name");
         //  Log.i("MappingRoadConditions",getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
       ; }
        catch(Exception e)
        {
            e.printStackTrace();
          //  versionno = "0";
        }

       /* SharedPreferences settings = getSharedPreferences("potholes", 0);
        int potholes = settings.getInt("potholes", 0);
        settings = getSharedPreferences("speedbreakers", 0);
        int speedbreakers = settings.getInt("speedbreakers", 0);

        if (potholes > 0 || speedbreakers > 0 )
            tv.setText("You have cited "+potholes+" potholes.\n\n" +
                "You have cited "+speedbreakers+" speed-breakers.");
        else
            tv.setText("Go through the maps linked below to discover potholes, speedbreakers and congestion in your area!");*/
         tv.setPadding(0,30,0,20);
      //  handler_upload.postDelayed(runnable, 5000);
		//handler_gather.postDelayed(runnable1, 8000);

        Intent alarmIntent = new Intent(this, Alarm.class);
        pendingIntentAlarm = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        startAlarm();
	}

    public void startAlarm() {
        managerAlarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 6 * 10000 * 15; // 6*10 seconds = 1 minute ==> *15 so we have 15 minutes

        managerAlarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntentAlarm);
     //   Toast.makeText(this, "Alarm Set", Toast.LENGTH_LONG).show();
    }


	private void authenticate(boolean bRefreshCache)
			throws ClientProtocolException, IOException {
		// Login using the Facebook provider.
		bAuthenticating = true;
	//	Log.i("MappingRoadConditions", "In facebook auth");
		if (bRefreshCache || !loadUserTokenCache(mClient)) {
		//	Log.i("MappingRoadConditions", "Need to auth");
			mClient.login(MobileServiceAuthenticationProvider.Facebook,
					new UserAuthenticationCallback() {
						@Override
						public void onCompleted(MobileServiceUser user,
								Exception exception,
								ServiceFilterResponse response) {

							synchronized (mAuthenticationLock) {
								if (exception == null) {
									cacheUserToken(mClient.getCurrentUser());
								//	Log.i("MappingRoadConditions",
								//			"authenticating");
									createAndShowDialog(String.format(
											"You are now logged in - %1$2s",
											user.getUserId()), "Success");

									button1.setVisibility(button1.INVISIBLE);
									button2.setVisibility(button2.INVISIBLE);
									button.setVisibility(button.VISIBLE);
                                    button3.setVisibility(button3.VISIBLE);
                                    button4.setVisibility(button4.VISIBLE);
                                    Proceed();

								} else {
							/*		createAndShowDialog(exception.getMessage(),
											"Login Error"); */ // SUPPRESSING ERROR
								/*	button1.setVisibility(button1.VISIBLE);
									button2.setVisibility(button2.VISIBLE);
						            button.setVisibility(button.INVISIBLE); */ //LOGGING IN NO MATTER WHAT

                                    //REMOVE LATER//
                                    Proceed();
                                    button1.setVisibility(button1.INVISIBLE);
                                    button2.setVisibility(button2.INVISIBLE);
                                    button.setVisibility(button.VISIBLE);
                                    button3.setVisibility(button3.VISIBLE);
                                    button4.setVisibility(button4.VISIBLE);
								}
								bAuthenticating = false;
								mAuthenticationLock.notifyAll();
							}
						}
					});
		} else {
			// Other threads may be blocked waiting to be notified when
			// authentication is complete.
		//	Log.i("MappingRoadConditions", "Don't need to auth");
			button1.setVisibility(button1.INVISIBLE);
			button2.setVisibility(button1.INVISIBLE);
            button.setVisibility(button.VISIBLE);
            button3.setVisibility(button3.VISIBLE);
            button4.setVisibility(button4.VISIBLE);

            Proceed();
			synchronized (mAuthenticationLock) {
				bAuthenticating = false;
				mAuthenticationLock.notifyAll();
			}
		}
	}

	private void authenticate_with_Google(boolean bRefreshCache)
			throws ClientProtocolException, IOException {
		bAuthenticating = true;
	//	Log.i("MappingRoadConditions", "In google auth");
		if (bRefreshCache || !loadUserTokenCache(mClient)) {
		//	Log.i("MappingRoadConditions", "Need to auth");
			mClient.login(MobileServiceAuthenticationProvider.Google,
					new UserAuthenticationCallback() {
						@Override
						public void onCompleted(MobileServiceUser user,
								Exception exception,
								ServiceFilterResponse response) {

							synchronized (mAuthenticationLock) {
								if (exception == null) {
									cacheUserToken(mClient.getCurrentUser());
								//	Log.i("MappingRoadConditions",
								//			"authenticating");
									createAndShowDialog(String.format(
											"You are now logged in - %1$2s",
											user.getUserId()), "Success");

									button1.setVisibility(button1.INVISIBLE);
									button2.setVisibility(button1.INVISIBLE);
								    button.setVisibility(button.VISIBLE);
                                    button3.setVisibility(button3.VISIBLE);
                                    button4.setVisibility(button4.VISIBLE);
									Proceed();

								} else {
									/*createAndShowDialog(exception.getMessage(),
											"Login Error"); */

                                   /*		createAndShowDialog(exception.getMessage(),
											"Login Error"); */ // SUPPRESSING ERROR
								/*	button1.setVisibility(button1.VISIBLE);
									button2.setVisibility(button2.VISIBLE);
						            button.setVisibility(button.INVISIBLE); */ //LOGGING IN NO MATTER WHAT

                                    //REMOVE LATER//
                                    Proceed();
                                    button1.setVisibility(button1.INVISIBLE);
                                    button2.setVisibility(button2.INVISIBLE);
                                    button.setVisibility(button.VISIBLE);
                                    button3.setVisibility(button3.VISIBLE);
                                    button4.setVisibility(button4.VISIBLE);

								}
								bAuthenticating = false;
								mAuthenticationLock.notifyAll();

							}
						}
					});
		} else {
		//	Log.i("MappingRoadConditions", "Don't need to auth");
			button1.setVisibility(button1.INVISIBLE);			
			button2.setVisibility(button2.INVISIBLE);
			button.setVisibility(button.VISIBLE);
            button3.setVisibility(button3.VISIBLE);
            button4.setVisibility(button4.VISIBLE);
			Proceed();
			synchronized (mAuthenticationLock) {
				bAuthenticating = false;
				mAuthenticationLock.notifyAll();
			}

		}
	}

	private void createAndShowDialog(Exception exception, String title) {
		createAndShowDialog(exception.toString(), title);
	}

	private void createAndShowDialog(final String message, final String title) {
		final Context o = this;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(o);
				builder.setMessage(message);
				builder.setTitle(title);
				builder.create().show();
			}
		});
	}

	private boolean loadUserTokenCache(MobileServiceClient client) {
	//	Log.i("MappingRoadConditions", "Load User Token Cache");
		SharedPreferences prefs = getSharedPreferences(SHAREDPREFFILE,
				Context.MODE_PRIVATE);
		String userId = prefs.getString(USERIDPREF, "undefined");
		if (userId == "undefined")
			return false;
		String token = prefs.getString(TOKENPREF, "undefined");
		if (token == "undefined")
			return false;

		MobileServiceUser user = new MobileServiceUser(userId);
		user.setAuthenticationToken(token);
		client.setCurrentUser(user);

		return true;
	}

	private void cacheUserToken(MobileServiceUser user) {
	//	Log.i("MappingRoadConditions", "Cache user token");
		SharedPreferences prefs = getSharedPreferences(SHAREDPREFFILE,
				Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString(USERIDPREF, user.getUserId());
		editor.putString(TOKENPREF, user.getAuthenticationToken());
		editor.commit();
	}

	public boolean detectAndWaitForAuthentication() {
	//	Log.i("MappingRoadConditions", "Detect and wait for auth");
		boolean detected = false;
		synchronized (mAuthenticationLock) {
			do {
				if (bAuthenticating == true)
					detected = true;
				try {
					mAuthenticationLock.wait(1000);
				} catch (InterruptedException e) {
				}
			} while (bAuthenticating == true);
		}
		if (bAuthenticating == true)
			return true;

		return detected;
	}

	private void waitAndUpdateRequestToken(ServiceFilterRequest request) {
	//	Log.i("MappingRoadConditions", "wait And Update Request Token");
		MobileServiceUser user = null;
		if (detectAndWaitForAuthentication()) {
			user = mClient.getCurrentUser();
			if (user != null) {
				request.removeHeader("X-ZUMO-AUTH");
				request.addHeader("X-ZUMO-AUTH", user.getAuthenticationToken());
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.gprs:
                if (item.isChecked()) {
                    isGPRSAllowed = true;
                    item.setChecked(false);
      //              Log.i("MappingRoadConditions","GPRS is turned off");

                }
                else {
                    isGPRSAllowed = false;
                    item.setChecked(true);
       //             Log.i("MappingRoadConditions","GPRS is turned on");
                }
                return true;
            case R.id.about:
            {
                Intent intent = new Intent(context_,About.class);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
	}

	private class RefreshTokenCacheFilter implements ServiceFilter {

		AtomicBoolean mAtomicAuthenticatingFlag = new AtomicBoolean();

		public ListenableFuture<ServiceFilterResponse> handleRequest(

		final ServiceFilterRequest request,
				final NextServiceFilterCallback nextServiceFilterCallback) {
		//	Log.i("MappingRoadConditions",
		//			" Refresh Token Cache Filter handle request");

			// In this example, if authentication is already in progress we
			// block the request
			// until authentication is complete to avoid unnecessary
			// authentications as
			// a result of HTTP status code 401.
			// If authentication was detected, add the token to the request.
			waitAndUpdateRequestToken(request);

			// Send the request down the filter chain
			// retrying up to 5 times on 401 response codes.
			ListenableFuture<ServiceFilterResponse> future = null;
			ServiceFilterResponse response = null;
			int responseCode = 401;
			for (int i = 0; (i < 5) && (responseCode == 401); i++) {
				future = nextServiceFilterCallback.onNext(request);
				try {
					response = future.get();
					responseCode = response.getStatus().getStatusCode();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					if (e.getCause().getClass() == MobileServiceException.class) {
						MobileServiceException mEx = (MobileServiceException) e
								.getCause();
						responseCode = mEx.getResponse().getStatus()
								.getStatusCode();
						if (responseCode == 401) {
							// Two simultaneous requests from independent
							// threads could get HTTP status 401.
							// Protecting against that right here so multiple
							// authentication requests are
							// not setup to run on the UI thread.
							// We only want to authenticate once. Requests
							// should just wait and retry
							// with the new token.
							if (mAtomicAuthenticatingFlag.compareAndSet(false,
									true)) {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										try {
											authenticate(true);
										} catch (ClientProtocolException e) {
											e.printStackTrace();
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								});
							}
							waitAndUpdateRequestToken(request);
							mAtomicAuthenticatingFlag.set(false);
						}
					}
				}
			}
			return future;
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
	//	Log.e("MappingRoadConditions", "Connection failed");
	;
	}

	public void showErrorDialog(int errorCode) {
		//ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
		//Bundle args = new Bundle();
		createAndShowDialog("Error code: " + errorCode, "Error");
	}

	/* Called from ErrorDialogFragment when the dialog is dismissed. */
	public void onDialogDismissed() {
		mResolvingError = false;
	}

	/* A fragment to display an error dialog */
	public static class ErrorDialogFragment extends DialogFragment {
		public ErrorDialogFragment() {
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Get the error code and retrieve the appropriate dialog
			int errorCode = this.getArguments().getInt(DIALOG_ERROR);
			return GooglePlayServicesUtil.getErrorDialog(errorCode,
					this.getActivity(), REQUEST_RESOLVE_ERROR);
		}

		@Override
		public void onDismiss(DialogInterface dialog) {
			((MainActivity) getActivity()).onDialogDismissed();
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
	//	Log.e("MappingRoadConditions", "Connected to ActRec");
		Intent i = new Intent(this, ActivityRecognitionIntentService.class);
		mActivityRecognitionPendingIntent = PendingIntent.getService(this, 200,
				i, PendingIntent.FLAG_UPDATE_CURRENT);
		ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(cl,
				200, mActivityRecognitionPendingIntent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_RESOLVE_ERROR) {
			mResolvingError = false;
			if (resultCode == RESULT_OK) {
				// Make sure the app is not already connected or attempting to
				// connect
				if (!cl.isConnecting() && !cl.isConnected()) {
					cl.connect();
				}
			}
		}
	}

	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
	//	Log.e("MappingRoadConditions", "Connection to ActRec suspended");
;
	}

    public void Initialize()
    {
        assetManager = getAssets();
        boolean isFirstTime = MyPreferences.isFirst(MainActivity.this);
           if (isFirstTime ||  (LoadClassifier.c == null))
           {

           //show start activity
            LoadClassifier.loadModel();
           //    Log.i("MappingRoadConditions","Loading classifier");
        }

     //   Log.i("MappingRoadConditions","Classifier Loaded :D");
        //  initializing variables
        if (isExternalStorageWritable())
            hasExternalStorage = true; // writing to external memory
        else ;
          //  Log.i("MappingRoadConditions", "No external storage");

        TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = tel.getDeviceId();
       // Log.i("MappingRoadConditions","IMEI: "+IMEI);


    }

  /*  @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first


        SharedPreferences settings = getSharedPreferences("potholes", 0);
        int potholes = settings.getInt("potholes", 0);
        settings = getSharedPreferences("speedbreakers", 0);
        int speedbreakers = settings.getInt("speedbreakers", 0);

        if (potholes > 0 || speedbreakers > 0)
        tv.setText("You have cited "+potholes+" potholes.\n\n" +
                "You have cited "+speedbreakers+" speed-breakers.");
        if (LoadClassifier.c == null)
        LoadClassifier.loadModel();

    }*/
}