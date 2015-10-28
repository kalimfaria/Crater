package com.mappingroadconditions;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//import android.util.Log;

public class ConnectionDetector {
	private Context _context = MainActivity.context_;

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity;
		try {
			connectivity = (ConnectivityManager) _context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				if (info != null)
					for (int i = 0; i < info.length; i++){
                        if (info[i].getType() == ConnectivityManager.TYPE_MOBILE && info[i].getState() == NetworkInfo.State.CONNECTED && MainActivity.isGPRSAllowed ) {
                            return true;
                        }
                        else if (info[i].getType() == ConnectivityManager.TYPE_MOBILE && info[i].getState() == NetworkInfo.State.CONNECTED && !MainActivity.isGPRSAllowed);

						if (info[i].getState() == NetworkInfo.State.CONNECTED && (info[i].getType() != ConnectivityManager.TYPE_MOBILE)) {
							return true;
						}
			}}
		} catch (NullPointerException e) {
		//	Log.i("Null pointer exception", e.toString());
           System.out.println(e.toString()) ;
		}
		return false;
	}
}