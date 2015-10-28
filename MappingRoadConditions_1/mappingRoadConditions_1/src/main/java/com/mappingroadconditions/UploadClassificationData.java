package com.mappingroadconditions;
import java.net.MalformedURLException;
import java.util.concurrent.RejectedExecutionException;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import android.os.AsyncTask;
//import android.util.Log;

public class UploadClassificationData {

    private MobileServiceClient mClient;
    private MobileServiceTable<ClassifiedData> mToDoTable;

    public void Upload() {
        try {

            mClient = new MobileServiceClient(
                    "https://mappingroadconditions.azure-mobile.net/",
                    "PKkPbdojnJJGtUCcJikgYqasiJoOWm49", MainActivity.context_);
            mToDoTable = mClient.getTable(ClassifiedData.class);

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {

                    String Data = new FileHandler().ReadFile();
                    if (Data.length() > 2) {
                        String[] data_arr = Data.split("\\r?\\n");

                        for (int i = 0; i < data_arr.length; i++) {
                            try {
                                ClassifiedData f = new ClassifiedData(data_arr[i]);
                                mToDoTable.insert(f);
                           /*     Log.i("MappingRoadConditions",
                                        "number of records sent: " + i);
                           */ } catch (RejectedExecutionException k) {
                          //      Log.i("MappingRoadConditions", k.toString());
                                try {
                                    Thread.sleep(50000);
                                    i--;
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
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
