package com.mappingroadconditions;

import java.util.ArrayList;
//import android.util.Log;

public class CalculateSpeed {
	public double prev_lat, prev_lng, curr_lat, curr_lng;
	public long time;

	public CalculateSpeed() {
		prev_lat = prev_lng = curr_lat = curr_lng = 0;
		time = 0;
	}

	public double getSpeed(double prev_lat, double prev_lng, double curr_lat,
			double curr_lng, long time) {
		double R = 6371.0;
		double diffLat = toRadian((curr_lat - prev_lat));
		double diffLon = toRadian(curr_lng - prev_lng);
		double lat1 = toRadian(prev_lat);
		double lat2 = toRadian(curr_lat);
		double a = Math.sin(diffLat / 2) * Math.sin(diffLat / 2)
				+ Math.sin(diffLon / 2) * Math.sin(diffLon / 2)
				* Math.cos(lat1) * Math.cos(lat2);
		double b = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = b * R;
	//	Log.i("MappingRoadConditions", "Speed: " + dist / time);
        System.out.println("Speed: " + dist / time);
		if (new Double(dist / time).isInfinite()
				|| new Double(dist / time).isNaN())
			return 0;
		return  Math.abs((dist / time)); // to NOT convert to km/hr and take absolute value ;3600000*
	}

	public ArrayList<Double> getSpeed(ArrayList<Double> lat,
			ArrayList<Double> lng, ArrayList<Long> time) {

		ArrayList<Double> speed = new ArrayList<Double>();
		if (lat.size() <= 1) {
			speed.add(0.0);
			return speed;
		}
		for (int i = 0; i < lat.size() - 1; i++)
			speed.add(getSpeed(lat.get(i), lng.get(i), lat.get(i + 1),
					lng.get(i + 1), time.get(i + 1) - time.get(i)));
		speed.add(speed.get(speed.size() - 1));// to maintain size
		return speed;
	}

	public double toRadian(double a) {
		return a * (Math.PI / 180);
	}
}
