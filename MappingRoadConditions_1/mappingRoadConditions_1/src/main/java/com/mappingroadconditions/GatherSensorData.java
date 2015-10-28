package com.mappingroadconditions;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import com.google.android.gms.common.ConnectionResult;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
//import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class GatherSensorData extends IntentService implements
		SensorEventListener, ConnectionCallbacks, OnConnectionFailedListener,
		LocationListener {

	private SensorManager senSensorManager;
	private Sensor senAccelerometer;
	private Sensor senGyroscope;
	private Sensor senRotationVector;
	private Sensor senMagneticField;
	private Sensor senGravity;
	Calendar calendar;
	java.util.Date now;
	public StatFs stfs;
	float[] m_lastAccels = new float[3];
	float[] m_lastGravity = new float[3];
	float[] m_lastMField = new float[3];
	float[] m_lastRot = new float[9];
	public static int Count;
	private LocationRequest mLocationRequest;
	// Stores the current instantiation of the location client in this object
	private GoogleApiClient mLocationClient;
	private Location mCurrentLocation = new Location("dummy");
	ArrayList<Float> acc_x;
	ArrayList<Float> acc_y;
	ArrayList<Float> acc_z;
	ArrayList<Long> acc_time;
	ArrayList<Long> loc_time;
	ArrayList<Double> lat;
	ArrayList<Double> lng;
	public boolean ToRecordOrNotRecord;

	public GatherSensorData() {
		super("GatherSensorData");
		// handler = new Handler();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Count = 0;
		senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	//	Log.i("Service", "In the service");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		this.unregisterSensors();
		super.onDestroy();
		Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onHandleIntent(Intent intent) {

//		Log.i("MappingRoadConditions", "Inside handle intent");

		mLocationClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();

		mLocationClient.connect();
		acc_x = new ArrayList<Float>();
		acc_y = new ArrayList<Float>();
		acc_z = new ArrayList<Float>();
		lat = new ArrayList<Double>();
		lng = new ArrayList<Double>();
		acc_time = new ArrayList<Long>();
		loc_time = new ArrayList<Long>();
		calendar = Calendar.getInstance();
		now = calendar.getTime();

		stfs = new StatFs(Environment.getExternalStorageDirectory().getPath());
		boolean temp = isExternalStorageWritable();
		// either their is no external memory or there is and we still have 500
		// MBs left
		if ((temp && stfs.getAvailableBytes() >= 70000000)) // 70 MB
			ToRecordOrNotRecord = true;
		else if (!temp) {
			MemoryInfo mi = new MemoryInfo();
			ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			activityManager.getMemoryInfo(mi);
			if (mi.availMem >= 10000000) // greater than 10 MB // so if we have 10 mb, we can write to file
				ToRecordOrNotRecord = true;
			else
				ToRecordOrNotRecord = false;
		}

		/* FOR DEBUGGING PURPOSES ONLY */
		MemoryInfo mi = new MemoryInfo();
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
/*		Log.i("MappingRoadConditions", "Value of ToRecordOrNotRecord: "
				+ ToRecordOrNotRecord);
		Log.i("MappingRoadConditions", "Size of memory on external card:  "
				+ stfs.getAvailableBytes());
		Log.i("MappingRoadConditions", "Size of internal memory" + mi.availMem);
*/		// handler.post(new DisplayToast(this, "Hello World!"));

		/*
		 * Context context = getApplicationContext(); AlarmManager alarmManager
		 * = (AlarmManager) context .getSystemService(Context.ALARM_SERVICE);
		 * Intent intent_2 = new Intent(context, AlarmReceiver.class);
		 * PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
		 * intent_2, 0); alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
		 * System.currentTimeMillis(), 30000 , pendingIntent); // every half
		 * hour 3600000/2
		 */
		this.registerSensors();
	/*	for (;;) {
			// /// BAD PROGRAMMING
		}*/

       boolean run = true;
        while (run) {
            System.out.println("Gathering data");
                LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    ;
                //    Log.i("MappingRoadConditions", "IN GATHER SENSOR DATA geo sensors!"); // good to go
                } else {
                   run = false;
                 //   Log.i("MappingRoadConditions", "geo sensors closed");
                }

               synchronized (MainActivity.Activity) {
                if (!MainActivity.Activity.equals("IN_VEHICLE"))
                    run = false;
            }

            if ( !new ConnectionDetector().isConnectingToInternet()) {
              run = false;
            }
        }

    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	public void unregisterSensors() {
	//	Log.i("MainActivity", senSensorManager.toString());
		senSensorManager.unregisterListener(this);
	//	Log.i("MainActivity", senSensorManager.toString());
		mLocationClient.disconnect();
	}

	public void registerSensors() {

		senAccelerometer = senSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
      //  Log.i("Mapping Road Conditions", "Accel" + senAccelerometer);
		senMagneticField = senSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		senGyroscope = senSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
	//	Log.i("Mapping Road Conditions", "Gyro" + senGyroscope);
		senGravity = senSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
	//	Log.i("Mapping Road Conditions", "Gravity" + senGravity);
		senRotationVector = senSensorManager
				.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
	//	Log.i("Mapping Road Conditions", "Rot" + senRotationVector);
		senSensorManager.registerListener(this, senAccelerometer,
				SensorManager.SENSOR_DELAY_FASTEST);
		senSensorManager.registerListener(this, senGravity,
				SensorManager.SENSOR_DELAY_FASTEST);
		senSensorManager.registerListener(this, senGyroscope,
				SensorManager.SENSOR_DELAY_FASTEST);
		senSensorManager.registerListener(this, senRotationVector,
				SensorManager.SENSOR_DELAY_FASTEST);
		senSensorManager.registerListener(this, senMagneticField,
				SensorManager.SENSOR_DELAY_FASTEST);
	}

	public void onSensorChanged(SensorEvent sensorEvent) {
           // Log.i("MappingRoadConditions","ToRecordOrNotRecord: " + ToRecordOrNotRecord);
		if (ToRecordOrNotRecord) {
			Sensor mySensor = sensorEvent.sensor;

			// Calendar calendar = Calendar.getInstance();
			// java.util.Date now = calendar .getTime();
			// Long ts = new Timestamp(now .getTime()).getTime();

			if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {

				System.arraycopy(sensorEvent.values, 0, m_lastAccels, 0, 3);
                boolean val = SensorManager.getRotationMatrix(m_lastRot, null,
                        m_lastGravity, m_lastMField);
         //       Log.i("MappingRoadConditions", "value of val : " + val );
				if (SensorManager.getRotationMatrix(m_lastRot, null,
						m_lastGravity, m_lastMField)) {

					float[] Result = new float[3];

					Result[0] = m_lastRot[0] * m_lastAccels[0] + m_lastRot[1]
							* m_lastAccels[1] + m_lastRot[2] * m_lastAccels[2];
					Result[1] = m_lastRot[3] * m_lastAccels[0] + m_lastRot[4]
							* m_lastAccels[1] + m_lastRot[5] * m_lastAccels[2];
					Result[2] = m_lastRot[6] * m_lastAccels[0] + m_lastRot[7]
							* m_lastAccels[1] + m_lastRot[8] * m_lastAccels[2];
					// float[] lpfAcceleration = lpf.addSamples(Result); // OLD
					// FILTER
/* Log.i("MappingRoadConditions",
 "Acceleration result matrix ="
				 + Result[0] + " " + Result[1] + " " + Result[2]);
*/
					acc_x.add(Result[0]);// lpfAcceleration[0]);
					acc_y.add(Result[1]);// lpfAcceleration[1]);
					acc_z.add(Result[2]);// lpfAcceleration[2]);
					acc_time.add(new Timestamp(now.getTime()).getTime());
					if (Count == 3999) // so we have 10 sets of features :D
					{
                     //   System.out.println("Now calculating features");
						FeaturesCalculation Task = new FeaturesCalculation();
						Task.execute(acc_x, acc_y, acc_z, loc_time, acc_time,
								lat, lng);
						// acc_x.clear();
						// acc_y.clear();
						// acc_z.clear();
					}
					Count = (Count + 1) % 4000;
				}
			}
			/*
			 * if (mySensor.getType() == Sensor.TYPE_GYROSCOPE) {
			 * 
			 * //Log.i("MappingRoadConditions", "gy_x =" +
			 * sensorEvent.values[0]); //Log.i("MappingRoadConditions", "gy_y ="
			 * + sensorEvent.values[1]); //Log.i("MappingRoadConditions",
			 * "gy_z=" + sensorEvent.values[2]);
			 * 
			 * }
			 */
			if (mySensor.getType() == Sensor.TYPE_GRAVITY) {
				System.arraycopy(sensorEvent.values, 0, m_lastGravity, 0, 3);
				// Log.i("MappingRoadConditions", "gravity_x =" +
				// sensorEvent.values[0]);
				// Log.i("MappingRoadConditions", "gravity_y =" +
				// sensorEvent.values[1]);
				// Log.i("MappingRoadConditions", "gravity_z=" +
				// sensorEvent.values[2]);

			}

			if (mySensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {

				System.arraycopy(sensorEvent.values, 0, m_lastRot, 0, 5);
				SensorManager.getRotationMatrixFromVector(m_lastRot, m_lastRot);
				// Log.i("MappingRoadConditions", "rot_x =" +
				// sensorEvent.values[0]);
				// Log.i("MappingRoadConditions", "rot_y =" +
				// sensorEvent.values[1]);
				// Log.i("MappingRoadConditions", "rot_z=" +
				// sensorEvent.values[2]);

			}
			if (mySensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				System.arraycopy(sensorEvent.values, 0, m_lastMField, 0, 3);
				// Log.i("MappingRoadConditions", "mag_x =" +
				// sensorEvent.values[0]);
				// Log.i("MappingRoadConditions", "mag_y =" +
				// sensorEvent.values[1]);
				// Log.i("MappingRoadConditions", "mag_z=" +
				// sensorEvent.values[2]);

			}
			/*
			 * JSONObject addElement = new JSONObject(); try { date = ts;
			 * addElement.put("date", date); addElement.put("acc_x", acc_x);
			 * addElement.put("acc_y", acc_y); addElement.put("acc_z", acc_z);
			 * addElement.put("gy_x", gy_x); addElement.put("gy_y", gy_y);
			 * addElement.put("gy_z", gy_z); addElement.put("lat", lat);
			 * addElement.put("lng", lng); addElement.put("mag_x", mag_x);
			 * addElement.put("mag_y", mag_y); addElement.put("mag_z", mag_z);
			 * addElement.put("speed", speed); // addElement.put("deviceID",
			 * MainActivity.IMEI); } catch (JSONException e) {
			 * e.printStackTrace(); }
			 */

			// if (obj.hasAccuracy() && obj.getAccuracy() < 10){
			// synchronized(list){
			// list.put(addElement);
			// }

			// MainActivity.dataToBeSent = list.length();
		}

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
	//	Log.i("Google Location API", "Connection Failed");
		/* Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show(); */
;
	}

	/*
	 * //@Override public void onDisconnected() { /* Toast.makeText(this,
	 * "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
	 */
	/*
	 * Log.i("Google Location API" , "Disconnected. Please re-connect");
	 * 
	 * 
	 * }
	 */
	@Override
	public void onConnected(Bundle arg0) {
	//	Log.i("Google Location API", "Connected");
		// //

		mCurrentLocation = LocationServices.FusedLocationApi
				.getLastLocation(mLocationClient);
		if (mCurrentLocation != null) {
			// mCurrentLocation.setText(String.valueOf(mLastLocation.getLatitude()));
			// mLongitudeTe.setText(String.valueOf(mLastLocation.getLongitude()));

/*			Log.i("MappingRoadConditions",
					"Latitude: " + mCurrentLocation.getLatitude());
			Log.i("MappingRoadConditions",
					"Longitude: " + mCurrentLocation.getLongitude());
*/		}

		createLocationRequest();

		// if (mRequestingLocationUpdates) {
		startLocationUpdates();
		// }

		// ///

		/*
		 * mLocationRequest = LocationRequest.create(); // Use high accuracy
		 * mLocationRequest.setPriority(
		 * LocationRequest.PRIORITY_HIGH_ACCURACY); // Set the update interval
		 * to 0 seconds mLocationRequest.setInterval(0); // Set the fastest
		 * update interval to 1 second mLocationRequest.setFastestInterval(0);
		 * 
		 * //mCurrentLocation = new Location("dummyprovider"); mCurrentLocation
		 * = mLocationClient.getLastLocation();
		 * mLocationClient.requestLocationUpdates(mLocationRequest, this); lat=
		 * mCurrentLocation.getLatitude(); lng =
		 * mCurrentLocation.getLongitude(); speed = mCurrentLocation.getSpeed();
		 * date = mCurrentLocation.getTime(); Log.i("Initial Longitude", "" +
		 * mCurrentLocation.getLongitude()); Log.i("Initial Latitude", "" +
		 * mCurrentLocation.getLatitude()); Log.i("Initial Speed", "" +
		 * mCurrentLocation.getSpeed()); Log.i("Initial Time", "" +
		 * mCurrentLocation.getTime());
		 */
	}

	protected void createLocationRequest() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(10000);
		mLocationRequest.setFastestInterval(5000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	}

	/*
	 * @Override public void onLocationChanged(Location location) {
	 * 
	 * Calendar calendar = Calendar .getInstance(); java.util.Date now =
	 * calendar .getTime(); Long ts = new Timestamp(now .getTime()).getTime();
	 * 
	 * //CalculateSpeed obj = new CalculateSpeed(); //speed = (float)
	 * obj.getSpeed(lat, lng, location.getLatitude() , location.getLongitude(),
	 * date - location.getTime()); lat = location.getLatitude(); lng =
	 * location.getLongitude(); date = ts;//location.getTime(); JSONObject
	 * addElement = new JSONObject(); try { addElement.put("date", date);
	 * addElement.put("acc_x", acc_x); addElement.put("acc_y", acc_y);
	 * addElement.put("acc_z", acc_z); addElement.put("gy_x", gy_x);
	 * addElement.put("gy_y", gy_y); addElement.put("gy_z", gy_z);
	 * addElement.put("lat", lat); addElement.put("lng", lng);
	 * addElement.put("mag_x", mag_x); addElement.put("mag_y", mag_y);
	 * addElement.put("mag_z", mag_z); addElement.put("speed", speed); //
	 * addElement.put("deviceID", MainActivity.IMEI); } catch (JSONException e)
	 * { e.printStackTrace(); } synchronized(list){ list.put(addElement); }
	 * 
	 * // MainActivity.dataToBeSent = list.length();
	 * 
	 * 
	 * }
	 */

	@Override
	public void onLocationChanged(Location location) {
		mCurrentLocation = location;
		// String mLastUpdateTime = DateFormat.getTimeInstance().format(new
		// Date());

	/*	Log.i("MappingRoadConditions", "Latitude changed to: "
				+ mCurrentLocation.getLatitude());
		Log.i("MappingRoadConditions", "Longitude changed to: "
				+ mCurrentLocation.getLongitude());
	*/	lat.add(mCurrentLocation.getLatitude());
		lng.add(mCurrentLocation.getLongitude());
		loc_time.add(new Timestamp(now.getTime()).getTime());
	}

	protected void startLocationUpdates() {
		LocationServices.FusedLocationApi.requestLocationUpdates(
				mLocationClient, mLocationRequest, this);
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
	}

}