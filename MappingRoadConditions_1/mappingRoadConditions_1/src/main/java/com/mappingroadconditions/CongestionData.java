package com.mappingroadconditions;

public class CongestionData {
	@com.google.gson.annotations.SerializedName("id")
	private String id;
	@com.google.gson.annotations.SerializedName("latitude")
	public Double mLatitude;
	@com.google.gson.annotations.SerializedName("longitude")
	public Double mLongitude;
	@com.google.gson.annotations.SerializedName("speed")
	public Double speed;
	@com.google.gson.annotations.SerializedName("timestamp")
	public Long timestamp;
	@com.google.gson.annotations.SerializedName("deviceID")
	public String deviceID;
    @com.google.gson.annotations.SerializedName("versionno")
    public String versionno;

	public CongestionData() {
	}

	public CongestionData(Double Latitude, Double Longitude, Double speed,
			Long timestamp) {
		this.mLatitude = Latitude;
		this.mLongitude = Longitude;
		this.speed = speed;
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

	public CongestionData(String data) {
		this.deviceID = MainActivity.IMEI;
	}
}
