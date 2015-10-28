package com.mappingroadconditions;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.RejectedExecutionException;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import android.os.AsyncTask;
//import android.util.Log;

public class UploadCongestionData {
	private MobileServiceClient mClient;
	private MobileServiceTable<CongestionData> mCongestionData;

	public void Upload(final ArrayList<CongestionData> cd) {
        System.out.println("Uploading congestion data");
		try {

			final ArrayList<CongestionData> cd_ = new ArrayList(cd);
			synchronized (cd){
				cd.clear();				
			}
			mClient = new MobileServiceClient(
					"https://mappingroadconditions.azure-mobile.net/",
					"PKkPbdojnJJGtUCcJikgYqasiJoOWm49", MainActivity.context_);
			mCongestionData = mClient.getTable(CongestionData.class);

			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					for (int i = 0; i < cd_.size(); i++) {
						try { 
							mCongestionData.insert(cd_.get(i));
					/*		Log.i("MappingRoadConditions",
									"congestion data sent : " + i);
					*/	}
						catch (RejectedExecutionException k) {
						//	Log.i("MappingRoadConditions", k.toString());
							try {
								Thread.sleep(50000);
								i--;
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
					}
					return null;
				}
			}.execute();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}