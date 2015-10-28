package com.mappingroadconditions;

import weka.gui.Main;

public class ClassifiedData {
	@com.google.gson.annotations.SerializedName("id")
	private String id;
	@com.google.gson.annotations.SerializedName("latitude")
	public Double mLatitude;
	@com.google.gson.annotations.SerializedName("longitude")
	public Double mLongitude;
    @com.google.gson.annotations.SerializedName("classification")
    public Double classification;
	@com.google.gson.annotations.SerializedName("timestamp")
	public Long timestamp;
	@com.google.gson.annotations.SerializedName("deviceID")
	public String deviceID;
    @com.google.gson.annotations.SerializedName("versionno")
    public String versionno;

	public ClassifiedData() {
	}

	public ClassifiedData(Double Latitude, Double Longitude, Double cls,
                          Long timestamp) {
		this.mLatitude = Latitude;
		this.mLongitude = Longitude;
		this.classification = cls;
		this.timestamp = timestamp;
		deviceID = MainActivity.IMEI;
        try {
            versionno = MainActivity.context_.getPackageManager().getPackageInfo(MainActivity.context_.getPackageName(), 0).versionName;
            ;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            versionno = "0";
        }
        }

    public ClassifiedData(String data)
    {
        String[] data_arr = data.split(",");
        this.mLatitude = Double.parseDouble(data_arr[0]);
        this.mLongitude = Double.parseDouble(data_arr[1]);
        this.classification = Double.parseDouble(data_arr[2]);
        this.timestamp = Long.parseLong(data_arr[3]);
        this.deviceID = MainActivity.IMEI;
        try {
            versionno = MainActivity.context_.getPackageManager().getPackageInfo(MainActivity.context_.getPackageName(), 0).versionName;
            ;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            versionno = "0";
        }
    }

    public String toString()
    {
        return this.mLatitude + "," + this.mLongitude + "," + this.classification + "," +  this.timestamp +"," + deviceID +"," + versionno;
    }

}
