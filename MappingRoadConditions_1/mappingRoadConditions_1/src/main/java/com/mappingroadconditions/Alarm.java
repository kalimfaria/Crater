package com.mappingroadconditions; /**
 * Created by faria on 5/31/15.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

public class Alarm extends BroadcastReceiver{
    private MobileServiceClient mClient = null;
    TelephonyManager telephonyManager;
    PhoneStateListener listener;
    public String phonestate = "Idle";
    public static String Activity = " ";
    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // For our recurring task, we'll just display a message
      //  Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();
     //   Toast.makeText(MainActivity.context_, "Alarm Triggered", Toast.LENGTH_LONG).show();
        if (new ConnectionDetector().isConnectingToInternet()) {
            new UploadClassificationData().Upload();
         //   handler_upload.postDelayed(this, 3600000);// do this once every

        } else {
          //  Toast.makeText(MainActivity.context_, "No Internet to Connect To For Uploading", Toast.LENGTH_LONG).show();

        }

        boolean gotGPS = false;
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(MainActivity.context_);
        if (resultCode == ConnectionResult.SUCCESS) {
            //	Log.i("MappingRoadConditions", "Got google play services");


            LocationManager manager = (LocationManager) MainActivity.context_.getSystemService(Context.LOCATION_SERVICE);
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

        telephonyManager = (TelephonyManager) MainActivity.context_.getSystemService(Context.TELEPHONY_SERVICE);
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
            Intent service = new Intent(MainActivity.context_, GatherSensorData.class);
            MainActivity.context_.startService(service);

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


    }

}
