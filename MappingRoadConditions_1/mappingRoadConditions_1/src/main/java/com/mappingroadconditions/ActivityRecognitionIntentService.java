package com.mappingroadconditions;

import android.app.IntentService;
import android.content.Intent;
//import android.util.Log;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class ActivityRecognitionIntentService extends IntentService {

	public ActivityRecognitionIntentService() {
		super("MappingRoadConditions");
	//	Log.i("MappingRoadConditions", "In Activity Recognition Constructor");
        System.out.println("In Activity Recognition Constructor");

	}

	protected void onHandleIntent(Intent intent) {

	//	Log.i("MappingRoadConditions", "In Activity Recognition");
        System.out.println("In Activity Recognition ");
		// If the incoming intent contains an update
		if (ActivityRecognitionResult.hasResult(intent)) {
			// Get the update
			ActivityRecognitionResult result = ActivityRecognitionResult
					.extractResult(intent);
			// Get the most probable activity
			DetectedActivity mostProbableActivity = result
					.getMostProbableActivity();
			/*
			 * Get the probability that this activity is the the user's actual
			 * activity
			 */
			int confidence = mostProbableActivity.getConfidence();
			/*
			 * Get an integer describing the type of activity
			 */
			int activityType = mostProbableActivity.getType();
			String activityName = getNameFromType(activityType);
			synchronized (MainActivity.Activity) {
				MainActivity.Activity = activityName;
			}
		//	Log.i("Activity Recognition", "Activity: " + activityName
	//				+ " Confidence: " + confidence);

            System.out.println("Activity: " + activityName 	+ " Confidence: " + confidence);

		}
	}

	private String getNameFromType(int activityType) {
		switch (activityType) {
		case DetectedActivity.IN_VEHICLE:
			return "in vehicle";
		case DetectedActivity.ON_BICYCLE:
			return "on bicycle";
		case DetectedActivity.ON_FOOT:
			return "on foot";
		case DetectedActivity.STILL:
			return "still";
		case DetectedActivity.UNKNOWN:
			return "unknown";
		case DetectedActivity.TILTING:
			return "tilting";
		case DetectedActivity.RUNNING:
			return "running";
		case DetectedActivity.WALKING:
			return "walking";
		}
		return null;
	}
}