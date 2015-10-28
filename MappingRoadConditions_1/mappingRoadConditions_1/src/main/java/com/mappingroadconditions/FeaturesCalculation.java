package com.mappingroadconditions;

import java.util.ArrayList;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
//import android.util.Log;

public class FeaturesCalculation extends AsyncTask<ArrayList, Void, Void> {
	ArrayList<Float> acc_x;
	ArrayList<Float> acc_y;
	ArrayList<Float> acc_z;
	ArrayList<Float> low_acc_x;
	ArrayList<Float> low_acc_y;
	ArrayList<Float> low_acc_z;
	ArrayList<Float> high_acc_x;
	ArrayList<Float> high_acc_y;
	ArrayList<Float> high_acc_z;
	ArrayList<Long> loc_time;
	ArrayList<Long> acc_time;
	ArrayList<Double> lat;
	ArrayList<Double> lng;
	ArrayList<Double> speed;
	ArrayList<Float> acc_x_all;
	ArrayList<Float> acc_y_all;
	ArrayList<Float> acc_z_all;
	ArrayList<Float> low_acc_x_all;
	ArrayList<Float> low_acc_y_all;
	ArrayList<Float> low_acc_z_all;
	ArrayList<Float> high_acc_x_all;
	ArrayList<Float> high_acc_y_all;
	ArrayList<Float> high_acc_z_all;
	ArrayList<CongestionData> data;
	LowPassFilter lpf;
	HighPassFilter hpf;
	Feature features;

	@Override
	protected Void doInBackground(ArrayList... arg0) {
		data = new ArrayList<CongestionData>();
		lpf = new LowPassFilter();
		hpf = new HighPassFilter();
		acc_x_all = new ArrayList<Float>(arg0[0]);
		acc_y_all = new ArrayList<Float>(arg0[1]);
		acc_z_all = new ArrayList<Float>(arg0[2]);
		loc_time = new ArrayList<Long>(arg0[3]);
		acc_time = new ArrayList<Long>(arg0[4]);
		lat = new ArrayList<Double>(arg0[5]);
		lng = new ArrayList<Double>(arg0[6]);
		synchronized (arg0) {
			arg0[0].clear();
			arg0[1].clear();
			arg0[2].clear();
			arg0[3].clear();
			arg0[4].clear();
			arg0[5].clear();
			arg0[6].clear();
			GatherSensorData.Count = 0;
		}
		speed = (new CalculateSpeed()).getSpeed(lat, lng, loc_time);
		acc_x = new ArrayList<Float>();
		acc_y = new ArrayList<Float>();
		acc_z = new ArrayList<Float>();
		low_acc_x = new ArrayList<Float>();
		low_acc_y = new ArrayList<Float>();
		low_acc_z = new ArrayList<Float>();
		high_acc_x = new ArrayList<Float>();
		high_acc_y = new ArrayList<Float>();
		high_acc_z = new ArrayList<Float>();
		
		//// congestion data 
		
		for (int i = 0; i < speed.size();i++)
		{
			data.add(new CongestionData(lat.get(i), lng.get(i), speed.get(i),loc_time.get(i)));
		}

		// data is low pass filtered
		double[] arr_x_temp = new double[acc_x_all.size()];
		for (int k = 0; k < acc_x_all.size(); k++)
			arr_x_temp[k] = (double) acc_x_all.get(k);
		low_acc_x_all = lpf.execute(arr_x_temp);
		double[] arr_y_temp = new double[acc_y_all.size()];
		for (int k = 0; k < acc_y_all.size(); k++)
			arr_y_temp[k] = (double) acc_y_all.get(k);
		low_acc_y_all = lpf.execute(arr_y_temp);
		double[] arr_z_temp = new double[acc_z_all.size()];
		for (int k = 0; k < acc_z_all.size(); k++)
			arr_z_temp[k] = (double) acc_z_all.get(k);
		low_acc_z_all = lpf.execute(arr_z_temp);
		// high pass
		arr_x_temp = new double[acc_x_all.size()];
		for (int k = 0; k < acc_x_all.size(); k++)
			arr_x_temp[k] = (double) acc_x_all.get(k);
		high_acc_x_all = hpf.execute(arr_x_temp);
		arr_y_temp = new double[acc_y_all.size()];
		for (int k = 0; k < acc_y_all.size(); k++)
			arr_y_temp[k] = (double) acc_y_all.get(k);
		high_acc_y_all = hpf.execute(arr_y_temp);
		arr_z_temp = new double[acc_z_all.size()];
		for (int k = 0; k < acc_z_all.size(); k++)
			arr_z_temp[k] = (double) acc_z_all.get(k);
		high_acc_z_all = hpf.execute(arr_z_temp);

		DescriptiveStatistics sp = new DescriptiveStatistics();
		for (int i = 0; i < 40; i++) {
			features = new Feature();
			
	//		Log.i("", "Size of total list: " + acc_z_all.size());
			if (i == 39) {
				acc_x = new ArrayList<Float>(acc_x_all.subList(i * 4000 / 40,
						(i + 1) * 4000 / 40 - 1));
				acc_y = new ArrayList<Float>(acc_y_all.subList(i * 4000 / 40,
						(i + 1) * 4000 / 40 - 1));
				acc_z = new ArrayList<Float>(acc_z_all.subList(i * 4000 / 40,
						(i + 1) * 4000 / 40 - 1));
				acc_x.add(acc_x_all.get(3999));
				acc_y.add(acc_y_all.get(3999));
				acc_z.add(acc_z_all.get(3999));
				low_acc_x = new ArrayList<Float>(low_acc_x_all.subList(
						i * 4000 / 40, (i + 1) * 4000 / 40 - 1));
				low_acc_y = new ArrayList<Float>(low_acc_y_all.subList(
						i * 4000 / 40, (i + 1) * 4000 / 40 - 1));
				low_acc_z = new ArrayList<Float>(low_acc_z_all.subList(
						i * 4000 / 40, (i + 1) * 4000 / 40 - 1));
				low_acc_x.add(low_acc_x_all.get(3999));
				low_acc_y.add(low_acc_y_all.get(3999));
				low_acc_z.add(low_acc_z_all.get(3999));
				high_acc_x = new ArrayList<Float>(high_acc_x_all.subList(
						i * 4000 / 40, (i + 1) * 4000 / 40 - 1));
				high_acc_y = new ArrayList<Float>(high_acc_y_all.subList(
						i * 4000 / 40, (i + 1) * 4000 / 40 - 1));
				high_acc_z = new ArrayList<Float>(high_acc_z_all.subList(
						i * 4000 / 40, (i + 1) * 4000 / 40 - 1));
				high_acc_x.add(high_acc_x_all.get(3999));
				high_acc_y.add(high_acc_y_all.get(3999));
				high_acc_z.add(high_acc_z_all.get(3999));
			} else {
				acc_x = new ArrayList<Float>(acc_x_all.subList(i * 4000 / 40,
						(i + 1) * 4000 / 40));
				acc_y = new ArrayList<Float>(acc_y_all.subList(i * 4000 / 40,
						(i + 1) * 4000 / 40));
				acc_z = new ArrayList<Float>(acc_z_all.subList(i * 4000 / 40,
						(i + 1) * 4000 / 40));
				low_acc_x = new ArrayList<Float>(low_acc_x_all.subList(
						i * 4000 / 40, (i + 1) * 4000 / 40));
				low_acc_y = new ArrayList<Float>(low_acc_y_all.subList(
						i * 4000 / 40, (i + 1) * 4000 / 40));
				low_acc_z = new ArrayList<Float>(low_acc_z_all.subList(
						i * 4000 / 40, (i + 1) * 4000 / 40));
				high_acc_x = new ArrayList<Float>(high_acc_x_all.subList(
						i * 4000 / 40, (i + 1) * 4000 / 40));
				high_acc_y = new ArrayList<Float>(high_acc_y_all.subList(
						i * 4000 / 40, (i + 1) * 4000 / 40));
				high_acc_z = new ArrayList<Float>(high_acc_z_all.subList(
						i * 4000 / 40, (i + 1) * 4000 / 40));
			}

			long start_time = acc_time.get(i * 4000 / 40);
			long end_time = acc_time.get((i + 1) * 4000 / 40 - 1); // -1 to get
																	// to 3999
			for (int k = 0; k < loc_time.size() - 1; k++) {
				if (start_time >= loc_time.get(k)
						&& end_time <= loc_time.get(k + 1))
					sp.addValue(speed.get(k));
			}

			DescriptiveStatistics stats_y = new DescriptiveStatistics();
			DescriptiveStatistics stats_z = new DescriptiveStatistics();
			DescriptiveStatistics stats_x = new DescriptiveStatistics();
			DescriptiveStatistics low_stats_y = new DescriptiveStatistics();
			DescriptiveStatistics low_stats_z = new DescriptiveStatistics();
			DescriptiveStatistics low_stats_x = new DescriptiveStatistics();
			DescriptiveStatistics high_stats_y = new DescriptiveStatistics();
			DescriptiveStatistics high_stats_z = new DescriptiveStatistics();
			DescriptiveStatistics high_stats_x = new DescriptiveStatistics();
			for (int j = 0; j < acc_x.size(); j++) {
				stats_x.addValue(acc_x.get(j));
				stats_y.addValue(acc_y.get(j));
				stats_z.addValue(acc_z.get(j));
				low_stats_x.addValue(low_acc_x.get(j));
				low_stats_y.addValue(low_acc_y.get(j));
				low_stats_z.addValue(low_acc_z.get(j));
				high_stats_x.addValue(high_acc_x.get(j));
				high_stats_y.addValue(high_acc_y.get(j));
				high_stats_z.addValue(high_acc_z.get(j));

			}

			features.Std_X = (float) stats_x.getStandardDeviation();
			features.Std_Y = (float) stats_y.getStandardDeviation();
			features.Std_Z = (float) stats_z.getStandardDeviation();
			features.AvSpeed = (float) sp.getMean();
			features.MaxSpeed = (float) sp.getMax();
			features.MinSpeed = (float) sp.getMin();
			features.Maxima_X = (float) stats_x.getMax();
			features.Maxima_Y = (float) stats_y.getMax();
			features.Maxima_Z = (float) stats_z.getMax();
			features.Minima_X = (float) stats_x.getMin();
			features.Minima_Y = (float) stats_y.getMin();
			features.Minima_Z = (float) stats_z.getMin();
			features.Pt1stdaway_X = 0;
			features.Pt1stdaway_Y = 0;
			features.Pt1stdaway_Z = 0;
			features.Pt2stdaway_X = 0;
			features.Pt2stdaway_Y = 0;
			features.Pt2stdaway_Z = 0;

			features.FilStd_X = (float) low_stats_x.getStandardDeviation();
			features.FilStd_Y = (float) low_stats_y.getStandardDeviation();
			features.FilStd_Z = (float) low_stats_z.getStandardDeviation();
			features.FilMaxima_X = (float) low_stats_x.getMax();
			features.FilMaxima_Y = (float) low_stats_y.getMax();
			features.FilMaxima_Z = (float) low_stats_z.getMax();
			features.FilMinima_X = (float) low_stats_x.getMin();
			features.FilMinima_Y = (float) low_stats_y.getMin();
			features.FilMinima_Z = (float) low_stats_z.getMin();
			features.FilPt1stdaway_X = 0;
			features.FilPt1stdaway_Y = 0;
			features.FilPt1stdaway_Z = 0;
			features.FilPt2stdaway_X = 0;
			features.FilPt2stdaway_Y = 0;
			features.FilPt2stdaway_Z = 0;
			float mean_x = (float) stats_x.getMean();
			float mean_y = (float) stats_y.getMean();
			float mean_z = (float) stats_z.getMean();
			float low_mean_x = (float) low_stats_x.getMean();
			float low_mean_y = (float) low_stats_y.getMean();
			float low_mean_z = (float) low_stats_z.getMean();
			float high_mean_x = (float) low_stats_x.getMean();
			float high_mean_y = (float) low_stats_y.getMean();
			float high_mean_z = (float) low_stats_z.getMean();

			features.high_pass_x = (float) high_stats_x.getStandardDeviation();
			features.high_pass_std_y = (float) high_stats_y
					.getStandardDeviation();
			features.high_pass_z = (float) high_stats_z.getStandardDeviation();
			features.high_pass_maxima_x = (float) high_stats_x.getMax();
			features.high_pass_maxima_y = (float) high_stats_y.getMax();
			features.high_pass_maxima_z = (float) high_stats_z.getMax();
			features.high_pass_minima_x = (float) high_stats_x.getMin();
			features.high_pass_minima_y = (float) high_stats_y.getMin();
			features.high_pass_minima_z = (float) high_stats_z.getMin();
			features.high_pass_pt1stdaway_x = 0;
			features.high_pass_pt1stdaway_y = 0;
			features.high_pass_pt1stdaway_z = 0;
			features.high_pass_pt2stdaway_x = 0;
			features.high_pass_pt2stdaway_y = 0;
			features.high_pass_pt2stdaway_z = 0;

			for (int j = 0; j < acc_x.size(); j++) {
				if (acc_x.get(j) < (mean_x - features.Std_X)
						&& acc_x.get(j) > (mean_x + features.high_pass_x))
					features.Pt1stdaway_X++;
				if (acc_y.get(j) < (mean_y - features.Std_Y)
						&& acc_y.get(j) > (mean_y + features.high_pass_std_y))
					features.Pt1stdaway_Y++;
				if (acc_z.get(j) < (mean_z - features.Std_Z)
						&& acc_z.get(j) > (mean_z + features.high_pass_z))
					features.Pt1stdaway_Z++;
				if (acc_x.get(j) < (mean_x - 2 * features.Std_X)
						&& acc_x.get(j) > (mean_x + 2 * features.high_pass_x))
					features.Pt2stdaway_X++;
				if (acc_y.get(j) < (mean_y - 2 * features.Std_Y)
						&& acc_y.get(j) > (mean_y + 2 * features.high_pass_std_y))
					features.Pt2stdaway_Y++;
				if (acc_z.get(j) < (mean_z - 2 * features.Std_Z)
						&& acc_z.get(j) > (mean_z + 2 * features.high_pass_z))
					features.Pt2stdaway_Z++;

				if (low_acc_x.get(j) < (low_mean_x - features.FilStd_X)
						&& low_acc_x.get(j) > (low_mean_x + features.FilStd_X))
					features.FilPt1stdaway_X++;
				if (low_acc_y.get(j) < (low_mean_y - features.FilStd_Y)
						&& low_acc_y.get(j) > (low_mean_y + features.FilStd_Y))
					features.FilPt1stdaway_Y++;
				if (low_acc_z.get(j) < (low_mean_z - features.FilStd_Z)
						&& low_acc_z.get(j) > (low_mean_z + features.FilStd_Z))
					features.FilPt1stdaway_Z++;
				if (low_acc_x.get(j) < (low_mean_x - 2 * features.FilStd_X)
						&& low_acc_x.get(j) > (low_mean_x + 2 * features.FilStd_X))
					features.FilPt2stdaway_X++;
				if (low_acc_y.get(j) < (low_mean_y - 2 * features.FilStd_Y)
						&& low_acc_y.get(j) > (low_mean_y + 2 * features.FilStd_Y))
					features.FilPt2stdaway_Y++;
				if (low_acc_z.get(j) < (low_mean_z - 2 * features.FilStd_Z)
						&& low_acc_z.get(j) > (low_mean_z + 2 * features.FilStd_Z))
					features.FilPt2stdaway_Z++;

				if (high_acc_x.get(j) < (high_mean_x - features.high_pass_x)
						&& high_acc_x.get(j) > (high_mean_x + features.high_pass_x))
					features.high_pass_pt1stdaway_x++;
				if (high_acc_y.get(j) < (high_mean_y - features.high_pass_std_y)
						&& high_acc_y.get(j) > (high_mean_y + features.high_pass_std_y))
					features.high_pass_pt1stdaway_y++;
				if (high_acc_z.get(j) < (high_mean_z - features.high_pass_z)
						&& high_acc_z.get(j) > (high_mean_z + features.high_pass_z))
					features.high_pass_pt1stdaway_z++;
				if (high_acc_x.get(j) < (high_mean_x - 2 * features.high_pass_x)
						&& high_acc_x.get(j) > (high_mean_x + 2 * features.high_pass_x))
					features.high_pass_pt2stdaway_x++;
				if (high_acc_y.get(j) < (high_mean_y - 2 * features.high_pass_std_y)
						&& high_acc_y.get(j) > (high_mean_y + 2 * features.high_pass_std_y))
					features.high_pass_pt2stdaway_y++;
				if (high_acc_z.get(j) < (high_mean_z - 2 * features.high_pass_z)
						&& high_acc_z.get(j) > (high_mean_z + 2 * features.high_pass_z))
					features.high_pass_pt2stdaway_z++;

			}

			FastFourierTransformer transformer = new FastFourierTransformer(
					DftNormalization.STANDARD);
			int size = acc_x.size();
			double[] arr_x = new double[size + 28];
			for (int k = 0; k < size; k++)
				arr_x[k] = (double) acc_x.get(k);
			for (int k = 0; k < 28; k++)
				arr_x[k] = 0.0;
	//		Log.i("MappingRoadConditions", "Arr size : " + arr_x.length);

			Complex[] cmplx_x = transformer.transform(arr_x,
					TransformType.FORWARD);
			double[] arr_y = new double[size + 28];
			for (int k = 0; k < size; k++)
				arr_y[k] = (double) acc_y.get(k);
			for (int k = 0; k < 28; k++)
				arr_y[k] = 0.0;
			Complex[] cmplx_y = transformer.transform(arr_y,
					TransformType.FORWARD);
			double[] arr_z = new double[size + 28];
			for (int k = 0; k < size; k++)
				arr_z[k] = (double) acc_z.get(k);
			for (int k = 0; k < 28; k++)
				arr_z[k] = 0.0;
			Complex[] cmplx_z = transformer.transform(arr_z,
					TransformType.FORWARD);
	//		Log.d("MappingRoadConditions", "Calculating Features !");
	//		Log.e("MappingRoadConditions", "Calculating Features !");
//			Log.d("MappingRoadConditions", "Size of acc_x" + acc_x.size());
            System.out.println("Calculating Features !");

			DescriptiveStatistics a = new DescriptiveStatistics();
			DescriptiveStatistics b = new DescriptiveStatistics();
			DescriptiveStatistics c = new DescriptiveStatistics();
			for (int p = 10; p < cmplx_z.length; p++) {
				a.addValue(Math.sqrt(Math.pow(cmplx_x[p].getReal(), 2)
						+ Math.pow(cmplx_x[p].getImaginary(), 2)));
				b.addValue(Math.sqrt(Math.pow(cmplx_y[p].getReal(), 2)
						+ Math.pow(cmplx_y[p].getImaginary(), 2)));
				c.addValue(Math.sqrt(Math.pow(cmplx_x[p].getReal(), 2)
						+ Math.pow(cmplx_z[p].getImaginary(), 2)));
			}
			features.MaxFFT_X = (float) a.getMax();
			features.MaxFFT_Y = (float) b.getMax();
			features.MaxFFT_Z = (float) c.getMax();
			features.FFT_X_1 = (float) Math.sqrt(Math.pow(cmplx_x[1].getReal(),
					2) + Math.pow(cmplx_x[1].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_X_2 = (float) Math.sqrt(Math.pow(cmplx_x[2].getReal(),
					2) + Math.pow(cmplx_x[2].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_X_3 = (float) Math.sqrt(Math.pow(cmplx_x[3].getReal(),
					2) + Math.pow(cmplx_x[3].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_X_4 = (float) Math.sqrt(Math.pow(cmplx_x[4].getReal(),
					2) + Math.pow(cmplx_x[4].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_X_5 = (float) Math.sqrt(Math.pow(cmplx_x[5].getReal(),
					2) + Math.pow(cmplx_x[5].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_X_6 = (float) Math.sqrt(Math.pow(cmplx_x[6].getReal(),
					2) + Math.pow(cmplx_x[6].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_X_7 = (float) Math.sqrt(Math.pow(cmplx_x[7].getReal(),
					2) + Math.pow(cmplx_x[7].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_X_8 = (float) Math.sqrt(Math.pow(cmplx_x[8].getReal(),
					2) + Math.pow(cmplx_x[8].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_X_9 = (float) Math.sqrt(Math.pow(cmplx_x[9].getReal(),
					2) + Math.pow(cmplx_x[9].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_X_10 = (float) Math.sqrt(Math.pow(
					cmplx_x[10].getReal(), 2)
					+ Math.pow(cmplx_x[10].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_11 = (float) Math.sqrt(Math.pow(
					cmplx_x[11].getReal(), 2)
					+ Math.pow(cmplx_x[11].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_12 = (float) Math.sqrt(Math.pow(
					cmplx_x[12].getReal(), 2)
					+ Math.pow(cmplx_x[12].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_13 = (float) Math.sqrt(Math.pow(
					cmplx_x[13].getReal(), 2)
					+ Math.pow(cmplx_x[13].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_14 = (float) Math.sqrt(Math.pow(
					cmplx_x[14].getReal(), 2)
					+ Math.pow(cmplx_x[14].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_15 = (float) Math.sqrt(Math.pow(
					cmplx_x[15].getReal(), 2)
					+ Math.pow(cmplx_x[15].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_16 = (float) Math.sqrt(Math.pow(
					cmplx_x[16].getReal(), 2)
					+ Math.pow(cmplx_x[16].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_17 = (float) Math.sqrt(Math.pow(
					cmplx_x[17].getReal(), 2)
					+ Math.pow(cmplx_x[17].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_18 = (float) Math.sqrt(Math.pow(
					cmplx_x[18].getReal(), 2)
					+ Math.pow(cmplx_x[18].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_19 = (float) Math.sqrt(Math.pow(
					cmplx_x[19].getReal(), 2)
					+ Math.pow(cmplx_x[19].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_20 = (float) Math.sqrt(Math.pow(
					cmplx_x[20].getReal(), 2)
					+ Math.pow(cmplx_x[20].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_21 = (float) Math.sqrt(Math.pow(
					cmplx_x[21].getReal(), 2)
					+ Math.pow(cmplx_x[21].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_22 = (float) Math.sqrt(Math.pow(
					cmplx_x[22].getReal(), 2)
					+ Math.pow(cmplx_x[22].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_23 = (float) Math.sqrt(Math.pow(
					cmplx_x[23].getReal(), 2)
					+ Math.pow(cmplx_x[23].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_24 = (float) Math.sqrt(Math.pow(
					cmplx_x[24].getReal(), 2)
					+ Math.pow(cmplx_x[24].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_25 = (float) Math.sqrt(Math.pow(
					cmplx_x[25].getReal(), 2)
					+ Math.pow(cmplx_x[25].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_26 = (float) Math.sqrt(Math.pow(
					cmplx_x[26].getReal(), 2)
					+ Math.pow(cmplx_x[26].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_27 = (float) Math.sqrt(Math.pow(
					cmplx_x[27].getReal(), 2)
					+ Math.pow(cmplx_x[27].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_28 = (float) Math.sqrt(Math.pow(
					cmplx_x[28].getReal(), 2)
					+ Math.pow(cmplx_x[28].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_29 = (float) Math.sqrt(Math.pow(
					cmplx_x[29].getReal(), 2)
					+ Math.pow(cmplx_x[29].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_30 = (float) Math.sqrt(Math.pow(
					cmplx_x[30].getReal(), 2)
					+ Math.pow(cmplx_x[30].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_31 = (float) Math.sqrt(Math.pow(
					cmplx_x[31].getReal(), 2)
					+ Math.pow(cmplx_x[31].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_32 = (float) Math.sqrt(Math.pow(
					cmplx_x[32].getReal(), 2)
					+ Math.pow(cmplx_x[32].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_33 = (float) Math.sqrt(Math.pow(
					cmplx_x[33].getReal(), 2)
					+ Math.pow(cmplx_x[33].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_34 = (float) Math.sqrt(Math.pow(
					cmplx_x[34].getReal(), 2)
					+ Math.pow(cmplx_x[34].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_35 = (float) Math.sqrt(Math.pow(
					cmplx_x[35].getReal(), 2)
					+ Math.pow(cmplx_x[35].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_36 = (float) Math.sqrt(Math.pow(
					cmplx_x[36].getReal(), 2)
					+ Math.pow(cmplx_x[36].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_37 = (float) Math.sqrt(Math.pow(
					cmplx_x[37].getReal(), 2)
					+ Math.pow(cmplx_x[37].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_38 = (float) Math.sqrt(Math.pow(
					cmplx_x[38].getReal(), 2)
					+ Math.pow(cmplx_x[38].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_39 = (float) Math.sqrt(Math.pow(
					cmplx_x[39].getReal(), 2)
					+ Math.pow(cmplx_x[39].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_40 = (float) Math.sqrt(Math.pow(
					cmplx_x[40].getReal(), 2)
					+ Math.pow(cmplx_x[40].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_41 = (float) Math.sqrt(Math.pow(
					cmplx_x[41].getReal(), 2)
					+ Math.pow(cmplx_x[41].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_42 = (float) Math.sqrt(Math.pow(
					cmplx_x[42].getReal(), 2)
					+ Math.pow(cmplx_x[42].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_43 = (float) Math.sqrt(Math.pow(
					cmplx_x[43].getReal(), 2)
					+ Math.pow(cmplx_x[43].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_44 = (float) Math.sqrt(Math.pow(
					cmplx_x[44].getReal(), 2)
					+ Math.pow(cmplx_x[44].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_45 = (float) Math.sqrt(Math.pow(
					cmplx_x[45].getReal(), 2)
					+ Math.pow(cmplx_x[45].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_46 = (float) Math.sqrt(Math.pow(
					cmplx_x[46].getReal(), 2)
					+ Math.pow(cmplx_x[46].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_47 = (float) Math.sqrt(Math.pow(
					cmplx_x[47].getReal(), 2)
					+ Math.pow(cmplx_x[47].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_48 = (float) Math.sqrt(Math.pow(
					cmplx_x[48].getReal(), 2)
					+ Math.pow(cmplx_x[48].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_X_49 = (float) Math.sqrt(Math.pow(
					cmplx_x[49].getReal(), 2)
					+ Math.pow(cmplx_x[49].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants

			features.FFT_Y_1 = (float) Math.sqrt(Math.pow(cmplx_y[1].getReal(),
					2) + Math.pow(cmplx_y[1].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_Y_2 = (float) Math.sqrt(Math.pow(cmplx_y[2].getReal(),
					2) + Math.pow(cmplx_y[2].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_Y_3 = (float) Math.sqrt(Math.pow(cmplx_y[3].getReal(),
					2) + Math.pow(cmplx_y[3].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_Y_4 = (float) Math.sqrt(Math.pow(cmplx_y[4].getReal(),
					2) + Math.pow(cmplx_y[4].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_Y_5 = (float) Math.sqrt(Math.pow(cmplx_y[5].getReal(),
					2) + Math.pow(cmplx_y[5].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_Y_6 = (float) Math.sqrt(Math.pow(cmplx_y[6].getReal(),
					2) + Math.pow(cmplx_y[6].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_Y_7 = (float) Math.sqrt(Math.pow(cmplx_y[7].getReal(),
					2) + Math.pow(cmplx_y[7].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_Y_8 = (float) Math.sqrt(Math.pow(cmplx_y[8].getReal(),
					2) + Math.pow(cmplx_y[8].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_Y_9 = (float) Math.sqrt(Math.pow(cmplx_y[9].getReal(),
					2) + Math.pow(cmplx_y[9].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_Y_10 = (float) Math.sqrt(Math.pow(
					cmplx_y[10].getReal(), 2)
					+ Math.pow(cmplx_y[10].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_11 = (float) Math.sqrt(Math.pow(
					cmplx_y[11].getReal(), 2)
					+ Math.pow(cmplx_y[11].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_12 = (float) Math.sqrt(Math.pow(
					cmplx_y[12].getReal(), 2)
					+ Math.pow(cmplx_y[12].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_13 = (float) Math.sqrt(Math.pow(
					cmplx_y[13].getReal(), 2)
					+ Math.pow(cmplx_y[13].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_14 = (float) Math.sqrt(Math.pow(
					cmplx_y[14].getReal(), 2)
					+ Math.pow(cmplx_y[14].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_15 = (float) Math.sqrt(Math.pow(
					cmplx_y[15].getReal(), 2)
					+ Math.pow(cmplx_y[15].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_16 = (float) Math.sqrt(Math.pow(
					cmplx_y[16].getReal(), 2)
					+ Math.pow(cmplx_y[16].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_17 = (float) Math.sqrt(Math.pow(
					cmplx_y[17].getReal(), 2)
					+ Math.pow(cmplx_y[17].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_18 = (float) Math.sqrt(Math.pow(
					cmplx_y[18].getReal(), 2)
					+ Math.pow(cmplx_y[18].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_19 = (float) Math.sqrt(Math.pow(
					cmplx_y[19].getReal(), 2)
					+ Math.pow(cmplx_y[19].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_20 = (float) Math.sqrt(Math.pow(
					cmplx_y[20].getReal(), 2)
					+ Math.pow(cmplx_y[20].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_21 = (float) Math.sqrt(Math.pow(
					cmplx_y[21].getReal(), 2)
					+ Math.pow(cmplx_y[21].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_22 = (float) Math.sqrt(Math.pow(
					cmplx_y[22].getReal(), 2)
					+ Math.pow(cmplx_y[22].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_23 = (float) Math.sqrt(Math.pow(
					cmplx_y[23].getReal(), 2)
					+ Math.pow(cmplx_y[23].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_24 = (float) Math.sqrt(Math.pow(
					cmplx_y[24].getReal(), 2)
					+ Math.pow(cmplx_y[24].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_25 = (float) Math.sqrt(Math.pow(
					cmplx_y[25].getReal(), 2)
					+ Math.pow(cmplx_y[25].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_26 = (float) Math.sqrt(Math.pow(
					cmplx_y[26].getReal(), 2)
					+ Math.pow(cmplx_y[26].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_27 = (float) Math.sqrt(Math.pow(
					cmplx_y[27].getReal(), 2)
					+ Math.pow(cmplx_y[27].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_28 = (float) Math.sqrt(Math.pow(
					cmplx_y[28].getReal(), 2)
					+ Math.pow(cmplx_y[28].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_29 = (float) Math.sqrt(Math.pow(
					cmplx_y[29].getReal(), 2)
					+ Math.pow(cmplx_y[29].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_30 = (float) Math.sqrt(Math.pow(
					cmplx_y[30].getReal(), 2)
					+ Math.pow(cmplx_y[30].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_31 = (float) Math.sqrt(Math.pow(
					cmplx_y[31].getReal(), 2)
					+ Math.pow(cmplx_y[31].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_32 = (float) Math.sqrt(Math.pow(
					cmplx_y[32].getReal(), 2)
					+ Math.pow(cmplx_y[32].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_33 = (float) Math.sqrt(Math.pow(
					cmplx_y[33].getReal(), 2)
					+ Math.pow(cmplx_y[33].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_34 = (float) Math.sqrt(Math.pow(
					cmplx_y[34].getReal(), 2)
					+ Math.pow(cmplx_y[34].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_35 = (float) Math.sqrt(Math.pow(
					cmplx_y[35].getReal(), 2)
					+ Math.pow(cmplx_y[35].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_36 = (float) Math.sqrt(Math.pow(
					cmplx_y[36].getReal(), 2)
					+ Math.pow(cmplx_y[36].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_37 = (float) Math.sqrt(Math.pow(
					cmplx_y[37].getReal(), 2)
					+ Math.pow(cmplx_y[37].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_38 = (float) Math.sqrt(Math.pow(
					cmplx_y[38].getReal(), 2)
					+ Math.pow(cmplx_y[38].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_39 = (float) Math.sqrt(Math.pow(
					cmplx_y[39].getReal(), 2)
					+ Math.pow(cmplx_y[39].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_40 = (float) Math.sqrt(Math.pow(
					cmplx_y[40].getReal(), 2)
					+ Math.pow(cmplx_y[40].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_41 = (float) Math.sqrt(Math.pow(
					cmplx_y[41].getReal(), 2)
					+ Math.pow(cmplx_y[41].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_42 = (float) Math.sqrt(Math.pow(
					cmplx_y[42].getReal(), 2)
					+ Math.pow(cmplx_y[42].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_43 = (float) Math.sqrt(Math.pow(
					cmplx_y[43].getReal(), 2)
					+ Math.pow(cmplx_y[43].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_44 = (float) Math.sqrt(Math.pow(
					cmplx_y[44].getReal(), 2)
					+ Math.pow(cmplx_y[44].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_45 = (float) Math.sqrt(Math.pow(
					cmplx_y[45].getReal(), 2)
					+ Math.pow(cmplx_y[45].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_46 = (float) Math.sqrt(Math.pow(
					cmplx_y[46].getReal(), 2)
					+ Math.pow(cmplx_y[46].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_47 = (float) Math.sqrt(Math.pow(
					cmplx_y[47].getReal(), 2)
					+ Math.pow(cmplx_y[47].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_48 = (float) Math.sqrt(Math.pow(
					cmplx_y[48].getReal(), 2)
					+ Math.pow(cmplx_y[48].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Y_49 = (float) Math.sqrt(Math.pow(
					cmplx_y[49].getReal(), 2)
					+ Math.pow(cmplx_y[49].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants

			features.FFT_Z_1 = (float) Math.sqrt(Math.pow(cmplx_z[1].getReal(),
					2) + Math.pow(cmplx_z[1].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_Z_2 = (float) Math.sqrt(Math.pow(cmplx_z[2].getReal(),
					2) + Math.pow(cmplx_z[2].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_Z_3 = (float) Math.sqrt(Math.pow(cmplx_z[3].getReal(),
					2) + Math.pow(cmplx_z[3].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_Z_4 = (float) Math.sqrt(Math.pow(cmplx_z[4].getReal(),
					2) + Math.pow(cmplx_z[4].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_Z_5 = (float) Math.sqrt(Math.pow(cmplx_z[5].getReal(),
					2) + Math.pow(cmplx_z[5].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_Z_6 = (float) Math.sqrt(Math.pow(cmplx_z[6].getReal(),
					2) + Math.pow(cmplx_z[6].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_Z_7 = (float) Math.sqrt(Math.pow(cmplx_z[7].getReal(),
					2) + Math.pow(cmplx_z[7].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_Z_8 = (float) Math.sqrt(Math.pow(cmplx_z[8].getReal(),
					2) + Math.pow(cmplx_z[8].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_Z_9 = (float) Math.sqrt(Math.pow(cmplx_z[9].getReal(),
					2) + Math.pow(cmplx_z[9].getImaginary(), 2));// skipping 0
																	// to get
																	// rid of
																	// constants
			features.FFT_Z_10 = (float) Math.sqrt(Math.pow(
					cmplx_z[10].getReal(), 2)
					+ Math.pow(cmplx_z[10].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_11 = (float) Math.sqrt(Math.pow(
					cmplx_z[11].getReal(), 2)
					+ Math.pow(cmplx_z[11].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_12 = (float) Math.sqrt(Math.pow(
					cmplx_z[12].getReal(), 2)
					+ Math.pow(cmplx_z[12].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_13 = (float) Math.sqrt(Math.pow(
					cmplx_z[13].getReal(), 2)
					+ Math.pow(cmplx_z[13].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_14 = (float) Math.sqrt(Math.pow(
					cmplx_z[14].getReal(), 2)
					+ Math.pow(cmplx_z[14].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_15 = (float) Math.sqrt(Math.pow(
					cmplx_z[15].getReal(), 2)
					+ Math.pow(cmplx_z[15].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_16 = (float) Math.sqrt(Math.pow(
					cmplx_z[16].getReal(), 2)
					+ Math.pow(cmplx_z[16].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_17 = (float) Math.sqrt(Math.pow(
					cmplx_z[17].getReal(), 2)
					+ Math.pow(cmplx_z[17].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_18 = (float) Math.sqrt(Math.pow(
					cmplx_z[18].getReal(), 2)
					+ Math.pow(cmplx_z[18].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_19 = (float) Math.sqrt(Math.pow(
					cmplx_z[19].getReal(), 2)
					+ Math.pow(cmplx_z[19].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_20 = (float) Math.sqrt(Math.pow(
					cmplx_z[20].getReal(), 2)
					+ Math.pow(cmplx_z[20].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_21 = (float) Math.sqrt(Math.pow(
					cmplx_z[21].getReal(), 2)
					+ Math.pow(cmplx_z[21].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_22 = (float) Math.sqrt(Math.pow(
					cmplx_z[22].getReal(), 2)
					+ Math.pow(cmplx_z[22].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_23 = (float) Math.sqrt(Math.pow(
					cmplx_z[23].getReal(), 2)
					+ Math.pow(cmplx_z[23].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_24 = (float) Math.sqrt(Math.pow(
					cmplx_z[24].getReal(), 2)
					+ Math.pow(cmplx_z[24].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_25 = (float) Math.sqrt(Math.pow(
					cmplx_z[25].getReal(), 2)
					+ Math.pow(cmplx_z[25].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_26 = (float) Math.sqrt(Math.pow(
					cmplx_z[26].getReal(), 2)
					+ Math.pow(cmplx_z[26].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_27 = (float) Math.sqrt(Math.pow(
					cmplx_z[27].getReal(), 2)
					+ Math.pow(cmplx_z[27].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_28 = (float) Math.sqrt(Math.pow(
					cmplx_z[28].getReal(), 2)
					+ Math.pow(cmplx_z[28].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_29 = (float) Math.sqrt(Math.pow(
					cmplx_z[29].getReal(), 2)
					+ Math.pow(cmplx_z[29].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_30 = (float) Math.sqrt(Math.pow(
					cmplx_z[30].getReal(), 2)
					+ Math.pow(cmplx_z[30].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_31 = (float) Math.sqrt(Math.pow(
					cmplx_z[31].getReal(), 2)
					+ Math.pow(cmplx_z[31].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_32 = (float) Math.sqrt(Math.pow(
					cmplx_z[32].getReal(), 2)
					+ Math.pow(cmplx_z[32].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_33 = (float) Math.sqrt(Math.pow(
					cmplx_z[33].getReal(), 2)
					+ Math.pow(cmplx_z[33].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_34 = (float) Math.sqrt(Math.pow(
					cmplx_z[34].getReal(), 2)
					+ Math.pow(cmplx_z[34].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_35 = (float) Math.sqrt(Math.pow(
					cmplx_z[35].getReal(), 2)
					+ Math.pow(cmplx_z[35].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_36 = (float) Math.sqrt(Math.pow(
					cmplx_z[36].getReal(), 2)
					+ Math.pow(cmplx_z[36].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_37 = (float) Math.sqrt(Math.pow(
					cmplx_z[37].getReal(), 2)
					+ Math.pow(cmplx_z[37].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_38 = (float) Math.sqrt(Math.pow(
					cmplx_z[38].getReal(), 2)
					+ Math.pow(cmplx_z[38].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_39 = (float) Math.sqrt(Math.pow(
					cmplx_z[39].getReal(), 2)
					+ Math.pow(cmplx_z[39].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_40 = (float) Math.sqrt(Math.pow(
					cmplx_z[40].getReal(), 2)
					+ Math.pow(cmplx_z[40].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_41 = (float) Math.sqrt(Math.pow(
					cmplx_z[41].getReal(), 2)
					+ Math.pow(cmplx_z[41].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_42 = (float) Math.sqrt(Math.pow(
					cmplx_z[42].getReal(), 2)
					+ Math.pow(cmplx_z[42].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_43 = (float) Math.sqrt(Math.pow(
					cmplx_z[43].getReal(), 2)
					+ Math.pow(cmplx_z[43].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_44 = (float) Math.sqrt(Math.pow(
					cmplx_z[44].getReal(), 2)
					+ Math.pow(cmplx_z[44].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_45 = (float) Math.sqrt(Math.pow(
					cmplx_z[45].getReal(), 2)
					+ Math.pow(cmplx_z[45].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_46 = (float) Math.sqrt(Math.pow(
					cmplx_z[46].getReal(), 2)
					+ Math.pow(cmplx_z[46].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_47 = (float) Math.sqrt(Math.pow(
					cmplx_z[47].getReal(), 2)
					+ Math.pow(cmplx_z[47].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_48 = (float) Math.sqrt(Math.pow(
					cmplx_z[48].getReal(), 2)
					+ Math.pow(cmplx_z[48].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FFT_Z_49 = (float) Math.sqrt(Math.pow(
					cmplx_z[49].getReal(), 2)
					+ Math.pow(cmplx_z[49].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			// ////////////
			// FOR LOW PASS FILTERED DATA

			transformer = new FastFourierTransformer(DftNormalization.STANDARD);
			size = low_acc_x.size();
			arr_x = new double[size + 28];
			for (int k = 0; k < size; k++)
				arr_x[k] = (double) low_acc_x.get(k);
			for (int k = 0; k < 28; k++)
				arr_x[k] = 0.0;
			//Log.i("MappingRoadConditions", "Arr size : " + arr_x.length);

			cmplx_x = transformer.transform(arr_x, TransformType.FORWARD);

			arr_y = new double[size + 28];
			for (int k = 0; k < size; k++)
				arr_y[k] = (double) low_acc_y.get(k);
			for (int k = 0; k < 28; k++)
				arr_y[k] = 0.0;
			cmplx_y = transformer.transform(arr_y, TransformType.FORWARD);

			arr_z = new double[size + 28];
			for (int k = 0; k < size; k++)
				arr_z[k] = (double) low_acc_z.get(k);
			for (int k = 0; k < 28; k++)
				arr_z[k] = 0.0;
			cmplx_z = transformer.transform(arr_z, TransformType.FORWARD);

			a = new DescriptiveStatistics();
			b = new DescriptiveStatistics();
			c = new DescriptiveStatistics();
			for (int p = 10; p < cmplx_z.length; p++) {
				a.addValue(Math.sqrt(Math.pow(cmplx_x[p].getReal(), 2)
						+ Math.pow(cmplx_x[p].getImaginary(), 2)));
				b.addValue(Math.sqrt(Math.pow(cmplx_y[p].getReal(), 2)
						+ Math.pow(cmplx_y[p].getImaginary(), 2)));
				c.addValue(Math.sqrt(Math.pow(cmplx_x[p].getReal(), 2)
						+ Math.pow(cmplx_z[p].getImaginary(), 2)));
			}
			features.FilMaxFFT_X = (float) a.getMax();
			features.FilMaxFFT_Y = (float) b.getMax();
			features.FilMaxFFT_Z = (float) c.getMax();
			features.FilFFT_X_1 = (float) Math.sqrt(Math.pow(
					cmplx_x[1].getReal(), 2)
					+ Math.pow(cmplx_x[1].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_2 = (float) Math.sqrt(Math.pow(
					cmplx_x[2].getReal(), 2)
					+ Math.pow(cmplx_x[2].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_3 = (float) Math.sqrt(Math.pow(
					cmplx_x[3].getReal(), 2)
					+ Math.pow(cmplx_x[3].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_4 = (float) Math.sqrt(Math.pow(
					cmplx_x[4].getReal(), 2)
					+ Math.pow(cmplx_x[4].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_5 = (float) Math.sqrt(Math.pow(
					cmplx_x[5].getReal(), 2)
					+ Math.pow(cmplx_x[5].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_6 = (float) Math.sqrt(Math.pow(
					cmplx_x[6].getReal(), 2)
					+ Math.pow(cmplx_x[6].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_7 = (float) Math.sqrt(Math.pow(
					cmplx_x[7].getReal(), 2)
					+ Math.pow(cmplx_x[7].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_8 = (float) Math.sqrt(Math.pow(
					cmplx_x[8].getReal(), 2)
					+ Math.pow(cmplx_x[8].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_9 = (float) Math.sqrt(Math.pow(
					cmplx_x[9].getReal(), 2)
					+ Math.pow(cmplx_x[9].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_10 = (float) Math.sqrt(Math.pow(
					cmplx_x[10].getReal(), 2)
					+ Math.pow(cmplx_x[10].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_11 = (float) Math.sqrt(Math.pow(
					cmplx_x[11].getReal(), 2)
					+ Math.pow(cmplx_x[11].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_12 = (float) Math.sqrt(Math.pow(
					cmplx_x[12].getReal(), 2)
					+ Math.pow(cmplx_x[12].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_13 = (float) Math.sqrt(Math.pow(
					cmplx_x[13].getReal(), 2)
					+ Math.pow(cmplx_x[13].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_14 = (float) Math.sqrt(Math.pow(
					cmplx_x[14].getReal(), 2)
					+ Math.pow(cmplx_x[14].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_15 = (float) Math.sqrt(Math.pow(
					cmplx_x[15].getReal(), 2)
					+ Math.pow(cmplx_x[15].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_16 = (float) Math.sqrt(Math.pow(
					cmplx_x[16].getReal(), 2)
					+ Math.pow(cmplx_x[16].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_17 = (float) Math.sqrt(Math.pow(
					cmplx_x[17].getReal(), 2)
					+ Math.pow(cmplx_x[17].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_18 = (float) Math.sqrt(Math.pow(
					cmplx_x[18].getReal(), 2)
					+ Math.pow(cmplx_x[18].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_19 = (float) Math.sqrt(Math.pow(
					cmplx_x[19].getReal(), 2)
					+ Math.pow(cmplx_x[19].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_20 = (float) Math.sqrt(Math.pow(
					cmplx_x[20].getReal(), 2)
					+ Math.pow(cmplx_x[20].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_21 = (float) Math.sqrt(Math.pow(
					cmplx_x[21].getReal(), 2)
					+ Math.pow(cmplx_x[21].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_22 = (float) Math.sqrt(Math.pow(
					cmplx_x[22].getReal(), 2)
					+ Math.pow(cmplx_x[22].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_23 = (float) Math.sqrt(Math.pow(
					cmplx_x[23].getReal(), 2)
					+ Math.pow(cmplx_x[23].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_24 = (float) Math.sqrt(Math.pow(
					cmplx_x[24].getReal(), 2)
					+ Math.pow(cmplx_x[24].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_25 = (float) Math.sqrt(Math.pow(
					cmplx_x[25].getReal(), 2)
					+ Math.pow(cmplx_x[25].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_26 = (float) Math.sqrt(Math.pow(
					cmplx_x[26].getReal(), 2)
					+ Math.pow(cmplx_x[26].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_27 = (float) Math.sqrt(Math.pow(
					cmplx_x[27].getReal(), 2)
					+ Math.pow(cmplx_x[27].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_28 = (float) Math.sqrt(Math.pow(
					cmplx_x[28].getReal(), 2)
					+ Math.pow(cmplx_x[28].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_29 = (float) Math.sqrt(Math.pow(
					cmplx_x[29].getReal(), 2)
					+ Math.pow(cmplx_x[29].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_30 = (float) Math.sqrt(Math.pow(
					cmplx_x[30].getReal(), 2)
					+ Math.pow(cmplx_x[30].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_31 = (float) Math.sqrt(Math.pow(
					cmplx_x[31].getReal(), 2)
					+ Math.pow(cmplx_x[31].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_32 = (float) Math.sqrt(Math.pow(
					cmplx_x[32].getReal(), 2)
					+ Math.pow(cmplx_x[32].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_33 = (float) Math.sqrt(Math.pow(
					cmplx_x[33].getReal(), 2)
					+ Math.pow(cmplx_x[33].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_34 = (float) Math.sqrt(Math.pow(
					cmplx_x[34].getReal(), 2)
					+ Math.pow(cmplx_x[34].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_35 = (float) Math.sqrt(Math.pow(
					cmplx_x[35].getReal(), 2)
					+ Math.pow(cmplx_x[35].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_36 = (float) Math.sqrt(Math.pow(
					cmplx_x[36].getReal(), 2)
					+ Math.pow(cmplx_x[36].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_37 = (float) Math.sqrt(Math.pow(
					cmplx_x[37].getReal(), 2)
					+ Math.pow(cmplx_x[37].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_38 = (float) Math.sqrt(Math.pow(
					cmplx_x[38].getReal(), 2)
					+ Math.pow(cmplx_x[38].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_39 = (float) Math.sqrt(Math.pow(
					cmplx_x[39].getReal(), 2)
					+ Math.pow(cmplx_x[39].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_40 = (float) Math.sqrt(Math.pow(
					cmplx_x[40].getReal(), 2)
					+ Math.pow(cmplx_x[40].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_41 = (float) Math.sqrt(Math.pow(
					cmplx_x[41].getReal(), 2)
					+ Math.pow(cmplx_x[41].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_42 = (float) Math.sqrt(Math.pow(
					cmplx_x[42].getReal(), 2)
					+ Math.pow(cmplx_x[42].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_43 = (float) Math.sqrt(Math.pow(
					cmplx_x[43].getReal(), 2)
					+ Math.pow(cmplx_x[43].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_44 = (float) Math.sqrt(Math.pow(
					cmplx_x[44].getReal(), 2)
					+ Math.pow(cmplx_x[44].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_45 = (float) Math.sqrt(Math.pow(
					cmplx_x[45].getReal(), 2)
					+ Math.pow(cmplx_x[45].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_46 = (float) Math.sqrt(Math.pow(
					cmplx_x[46].getReal(), 2)
					+ Math.pow(cmplx_x[46].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_47 = (float) Math.sqrt(Math.pow(
					cmplx_x[47].getReal(), 2)
					+ Math.pow(cmplx_x[47].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_X_48 = (float) Math.sqrt(Math.pow(
					cmplx_x[48].getReal(), 2)
					+ Math.pow(cmplx_x[48].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			// features.FilFFT_X_49 = (float)
			// Math.sqrt(Math.pow(cmplx_x[49].getReal(), 2) +
			// Math.pow(cmplx_x[49].getImaginary(), 2));// skipping 0 to get rid
			// of constants

			features.FilFFT_Y_1 = (float) Math.sqrt(Math.pow(
					cmplx_y[1].getReal(), 2)
					+ Math.pow(cmplx_y[1].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_2 = (float) Math.sqrt(Math.pow(
					cmplx_y[2].getReal(), 2)
					+ Math.pow(cmplx_y[2].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_3 = (float) Math.sqrt(Math.pow(
					cmplx_y[3].getReal(), 2)
					+ Math.pow(cmplx_y[3].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_4 = (float) Math.sqrt(Math.pow(
					cmplx_y[4].getReal(), 2)
					+ Math.pow(cmplx_y[4].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_5 = (float) Math.sqrt(Math.pow(
					cmplx_y[5].getReal(), 2)
					+ Math.pow(cmplx_y[5].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_6 = (float) Math.sqrt(Math.pow(
					cmplx_y[6].getReal(), 2)
					+ Math.pow(cmplx_y[6].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_7 = (float) Math.sqrt(Math.pow(
					cmplx_y[7].getReal(), 2)
					+ Math.pow(cmplx_y[7].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_8 = (float) Math.sqrt(Math.pow(
					cmplx_y[8].getReal(), 2)
					+ Math.pow(cmplx_y[8].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_9 = (float) Math.sqrt(Math.pow(
					cmplx_y[9].getReal(), 2)
					+ Math.pow(cmplx_y[9].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_10 = (float) Math.sqrt(Math.pow(
					cmplx_y[10].getReal(), 2)
					+ Math.pow(cmplx_y[10].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_11 = (float) Math.sqrt(Math.pow(
					cmplx_y[11].getReal(), 2)
					+ Math.pow(cmplx_y[11].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_12 = (float) Math.sqrt(Math.pow(
					cmplx_y[12].getReal(), 2)
					+ Math.pow(cmplx_y[12].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_13 = (float) Math.sqrt(Math.pow(
					cmplx_y[13].getReal(), 2)
					+ Math.pow(cmplx_y[13].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_14 = (float) Math.sqrt(Math.pow(
					cmplx_y[14].getReal(), 2)
					+ Math.pow(cmplx_y[14].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_15 = (float) Math.sqrt(Math.pow(
					cmplx_y[15].getReal(), 2)
					+ Math.pow(cmplx_y[15].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_16 = (float) Math.sqrt(Math.pow(
					cmplx_y[16].getReal(), 2)
					+ Math.pow(cmplx_y[16].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_17 = (float) Math.sqrt(Math.pow(
					cmplx_y[17].getReal(), 2)
					+ Math.pow(cmplx_y[17].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_18 = (float) Math.sqrt(Math.pow(
					cmplx_y[18].getReal(), 2)
					+ Math.pow(cmplx_y[18].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_19 = (float) Math.sqrt(Math.pow(
					cmplx_y[19].getReal(), 2)
					+ Math.pow(cmplx_y[19].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_20 = (float) Math.sqrt(Math.pow(
					cmplx_y[20].getReal(), 2)
					+ Math.pow(cmplx_y[20].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_21 = (float) Math.sqrt(Math.pow(
					cmplx_y[21].getReal(), 2)
					+ Math.pow(cmplx_y[21].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_22 = (float) Math.sqrt(Math.pow(
					cmplx_y[22].getReal(), 2)
					+ Math.pow(cmplx_y[22].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_23 = (float) Math.sqrt(Math.pow(
					cmplx_y[23].getReal(), 2)
					+ Math.pow(cmplx_y[23].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_24 = (float) Math.sqrt(Math.pow(
					cmplx_y[24].getReal(), 2)
					+ Math.pow(cmplx_y[24].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_25 = (float) Math.sqrt(Math.pow(
					cmplx_y[25].getReal(), 2)
					+ Math.pow(cmplx_y[25].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_26 = (float) Math.sqrt(Math.pow(
					cmplx_y[26].getReal(), 2)
					+ Math.pow(cmplx_y[26].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_27 = (float) Math.sqrt(Math.pow(
					cmplx_y[27].getReal(), 2)
					+ Math.pow(cmplx_y[27].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_28 = (float) Math.sqrt(Math.pow(
					cmplx_y[28].getReal(), 2)
					+ Math.pow(cmplx_y[28].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_29 = (float) Math.sqrt(Math.pow(
					cmplx_y[29].getReal(), 2)
					+ Math.pow(cmplx_y[29].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_30 = (float) Math.sqrt(Math.pow(
					cmplx_y[30].getReal(), 2)
					+ Math.pow(cmplx_y[30].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_31 = (float) Math.sqrt(Math.pow(
					cmplx_y[31].getReal(), 2)
					+ Math.pow(cmplx_y[31].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_32 = (float) Math.sqrt(Math.pow(
					cmplx_y[32].getReal(), 2)
					+ Math.pow(cmplx_y[32].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_33 = (float) Math.sqrt(Math.pow(
					cmplx_y[33].getReal(), 2)
					+ Math.pow(cmplx_y[33].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_34 = (float) Math.sqrt(Math.pow(
					cmplx_y[34].getReal(), 2)
					+ Math.pow(cmplx_y[34].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_35 = (float) Math.sqrt(Math.pow(
					cmplx_y[35].getReal(), 2)
					+ Math.pow(cmplx_y[35].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_36 = (float) Math.sqrt(Math.pow(
					cmplx_y[36].getReal(), 2)
					+ Math.pow(cmplx_y[36].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_37 = (float) Math.sqrt(Math.pow(
					cmplx_y[37].getReal(), 2)
					+ Math.pow(cmplx_y[37].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_38 = (float) Math.sqrt(Math.pow(
					cmplx_y[38].getReal(), 2)
					+ Math.pow(cmplx_y[38].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_39 = (float) Math.sqrt(Math.pow(
					cmplx_y[39].getReal(), 2)
					+ Math.pow(cmplx_y[39].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_40 = (float) Math.sqrt(Math.pow(
					cmplx_y[40].getReal(), 2)
					+ Math.pow(cmplx_y[40].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_41 = (float) Math.sqrt(Math.pow(
					cmplx_y[41].getReal(), 2)
					+ Math.pow(cmplx_y[41].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_42 = (float) Math.sqrt(Math.pow(
					cmplx_y[42].getReal(), 2)
					+ Math.pow(cmplx_y[42].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_43 = (float) Math.sqrt(Math.pow(
					cmplx_y[43].getReal(), 2)
					+ Math.pow(cmplx_y[43].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_44 = (float) Math.sqrt(Math.pow(
					cmplx_y[44].getReal(), 2)
					+ Math.pow(cmplx_y[44].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_45 = (float) Math.sqrt(Math.pow(
					cmplx_y[45].getReal(), 2)
					+ Math.pow(cmplx_y[45].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_46 = (float) Math.sqrt(Math.pow(
					cmplx_y[46].getReal(), 2)
					+ Math.pow(cmplx_y[46].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_47 = (float) Math.sqrt(Math.pow(
					cmplx_y[47].getReal(), 2)
					+ Math.pow(cmplx_y[47].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_48 = (float) Math.sqrt(Math.pow(
					cmplx_y[48].getReal(), 2)
					+ Math.pow(cmplx_y[48].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Y_49 = (float) Math.sqrt(Math.pow(
					cmplx_y[49].getReal(), 2)
					+ Math.pow(cmplx_y[49].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants

			features.FilFFT_Z_1 = (float) Math.sqrt(Math.pow(
					cmplx_z[1].getReal(), 2)
					+ Math.pow(cmplx_z[1].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_2 = (float) Math.sqrt(Math.pow(
					cmplx_z[2].getReal(), 2)
					+ Math.pow(cmplx_z[2].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_3 = (float) Math.sqrt(Math.pow(
					cmplx_z[3].getReal(), 2)
					+ Math.pow(cmplx_z[3].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_4 = (float) Math.sqrt(Math.pow(
					cmplx_z[4].getReal(), 2)
					+ Math.pow(cmplx_z[4].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_5 = (float) Math.sqrt(Math.pow(
					cmplx_z[5].getReal(), 2)
					+ Math.pow(cmplx_z[5].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_6 = (float) Math.sqrt(Math.pow(
					cmplx_z[6].getReal(), 2)
					+ Math.pow(cmplx_z[6].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_7 = (float) Math.sqrt(Math.pow(
					cmplx_z[7].getReal(), 2)
					+ Math.pow(cmplx_z[7].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_8 = (float) Math.sqrt(Math.pow(
					cmplx_z[8].getReal(), 2)
					+ Math.pow(cmplx_z[8].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_9 = (float) Math.sqrt(Math.pow(
					cmplx_z[9].getReal(), 2)
					+ Math.pow(cmplx_z[9].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_10 = (float) Math.sqrt(Math.pow(
					cmplx_z[10].getReal(), 2)
					+ Math.pow(cmplx_z[10].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_11 = (float) Math.sqrt(Math.pow(
					cmplx_z[11].getReal(), 2)
					+ Math.pow(cmplx_z[11].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_12 = (float) Math.sqrt(Math.pow(
					cmplx_z[12].getReal(), 2)
					+ Math.pow(cmplx_z[12].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_13 = (float) Math.sqrt(Math.pow(
					cmplx_z[13].getReal(), 2)
					+ Math.pow(cmplx_z[13].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_14 = (float) Math.sqrt(Math.pow(
					cmplx_z[14].getReal(), 2)
					+ Math.pow(cmplx_z[14].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_15 = (float) Math.sqrt(Math.pow(
					cmplx_z[15].getReal(), 2)
					+ Math.pow(cmplx_z[15].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_16 = (float) Math.sqrt(Math.pow(
					cmplx_z[16].getReal(), 2)
					+ Math.pow(cmplx_z[16].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_17 = (float) Math.sqrt(Math.pow(
					cmplx_z[17].getReal(), 2)
					+ Math.pow(cmplx_z[17].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_18 = (float) Math.sqrt(Math.pow(
					cmplx_z[18].getReal(), 2)
					+ Math.pow(cmplx_z[18].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_19 = (float) Math.sqrt(Math.pow(
					cmplx_z[19].getReal(), 2)
					+ Math.pow(cmplx_z[19].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_20 = (float) Math.sqrt(Math.pow(
					cmplx_z[20].getReal(), 2)
					+ Math.pow(cmplx_z[20].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_21 = (float) Math.sqrt(Math.pow(
					cmplx_z[21].getReal(), 2)
					+ Math.pow(cmplx_z[21].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_22 = (float) Math.sqrt(Math.pow(
					cmplx_z[22].getReal(), 2)
					+ Math.pow(cmplx_z[22].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_23 = (float) Math.sqrt(Math.pow(
					cmplx_z[23].getReal(), 2)
					+ Math.pow(cmplx_z[23].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_24 = (float) Math.sqrt(Math.pow(
					cmplx_z[24].getReal(), 2)
					+ Math.pow(cmplx_z[24].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_25 = (float) Math.sqrt(Math.pow(
					cmplx_z[25].getReal(), 2)
					+ Math.pow(cmplx_z[25].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_26 = (float) Math.sqrt(Math.pow(
					cmplx_z[26].getReal(), 2)
					+ Math.pow(cmplx_z[26].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_27 = (float) Math.sqrt(Math.pow(
					cmplx_z[27].getReal(), 2)
					+ Math.pow(cmplx_z[27].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_28 = (float) Math.sqrt(Math.pow(
					cmplx_z[28].getReal(), 2)
					+ Math.pow(cmplx_z[28].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_29 = (float) Math.sqrt(Math.pow(
					cmplx_z[29].getReal(), 2)
					+ Math.pow(cmplx_z[29].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_30 = (float) Math.sqrt(Math.pow(
					cmplx_z[30].getReal(), 2)
					+ Math.pow(cmplx_z[30].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_31 = (float) Math.sqrt(Math.pow(
					cmplx_z[31].getReal(), 2)
					+ Math.pow(cmplx_z[31].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_32 = (float) Math.sqrt(Math.pow(
					cmplx_z[32].getReal(), 2)
					+ Math.pow(cmplx_z[32].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_33 = (float) Math.sqrt(Math.pow(
					cmplx_z[33].getReal(), 2)
					+ Math.pow(cmplx_z[33].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_34 = (float) Math.sqrt(Math.pow(
					cmplx_z[34].getReal(), 2)
					+ Math.pow(cmplx_z[34].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_35 = (float) Math.sqrt(Math.pow(
					cmplx_z[35].getReal(), 2)
					+ Math.pow(cmplx_z[35].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_36 = (float) Math.sqrt(Math.pow(
					cmplx_z[36].getReal(), 2)
					+ Math.pow(cmplx_z[36].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_37 = (float) Math.sqrt(Math.pow(
					cmplx_z[37].getReal(), 2)
					+ Math.pow(cmplx_z[37].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_38 = (float) Math.sqrt(Math.pow(
					cmplx_z[38].getReal(), 2)
					+ Math.pow(cmplx_z[38].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_39 = (float) Math.sqrt(Math.pow(
					cmplx_z[39].getReal(), 2)
					+ Math.pow(cmplx_z[39].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_40 = (float) Math.sqrt(Math.pow(
					cmplx_z[40].getReal(), 2)
					+ Math.pow(cmplx_z[40].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_41 = (float) Math.sqrt(Math.pow(
					cmplx_z[41].getReal(), 2)
					+ Math.pow(cmplx_z[41].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_42 = (float) Math.sqrt(Math.pow(
					cmplx_z[42].getReal(), 2)
					+ Math.pow(cmplx_z[42].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_43 = (float) Math.sqrt(Math.pow(
					cmplx_z[43].getReal(), 2)
					+ Math.pow(cmplx_z[43].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_44 = (float) Math.sqrt(Math.pow(
					cmplx_z[44].getReal(), 2)
					+ Math.pow(cmplx_z[44].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_45 = (float) Math.sqrt(Math.pow(
					cmplx_z[45].getReal(), 2)
					+ Math.pow(cmplx_z[45].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_46 = (float) Math.sqrt(Math.pow(
					cmplx_z[46].getReal(), 2)
					+ Math.pow(cmplx_z[46].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			features.FilFFT_Z_47 = (float) Math.sqrt(Math.pow(
					cmplx_z[47].getReal(), 2)
					+ Math.pow(cmplx_z[47].getImaginary(), 2));// skipping 0 to
																// get rid of
																// constants
			// features.FilFFT_Z_48 = (float)
			// Math.sqrt(Math.pow(cmplx_z[48].getReal(), 2) +
			// Math.pow(cmplx_z[48].getImaginary(), 2));// skipping 0 to get rid
			// of constants
			// features.FilFFT_Z_49 = (float)
			// Math.sqrt(Math.pow(cmplx_z[49].getReal(), 2) +
			// Math.pow(cmplx_z[49].getImaginary(), 2));// skipping 0 to get rid
			// of constants

			// ////////////////////////////
			// high pass

			a = new DescriptiveStatistics();
			for (int p = 0; p < high_acc_x.size(); p++) {
				a.addValue(high_acc_x.get(i) / high_acc_z.get(i));
			}

			features.high_pass_mean_xz = (float) a.getMean();
			features.high_pass_min_xz = (float) a.getMin();
			features.high_pass_max_xz = (float) a.getMax();
			features.high_pass_median_xz = (float) a.getPercentile(50);

			transformer = new FastFourierTransformer(DftNormalization.STANDARD);
			size = high_acc_x.size();
			arr_x = new double[size + 28];
			for (int k = 0; k < size; k++)
				arr_x[k] = (double) high_acc_x.get(k);
			for (int k = 0; k < 28; k++)
				arr_x[k] = 0.0;
	//		Log.i("MappingRoadConditions", "Arr size : " + arr_x.length);
            System.out.println("Arr size : " + arr_x.length);

			cmplx_x = transformer.transform(arr_x, TransformType.FORWARD);

			arr_y = new double[size + 28];
			for (int k = 0; k < size; k++)
				arr_y[k] = (double) high_acc_y.get(k);
			for (int k = 0; k < 28; k++)
				arr_y[k] = 0.0;
			cmplx_y = transformer.transform(arr_y, TransformType.FORWARD);

			arr_z = new double[size + 28];
			for (int k = 0; k < size; k++)
				arr_z[k] = (double) high_acc_z.get(k);
			for (int k = 0; k < 28; k++)
				arr_z[k] = 0.0;
			cmplx_z = transformer.transform(arr_z, TransformType.FORWARD);

			a = new DescriptiveStatistics();
			b = new DescriptiveStatistics();
			c = new DescriptiveStatistics();
			for (int p = 10; p < cmplx_z.length; p++) {
				a.addValue(Math.sqrt(Math.pow(cmplx_x[p].getReal(), 2)
						+ Math.pow(cmplx_x[p].getImaginary(), 2)));
				b.addValue(Math.sqrt(Math.pow(cmplx_y[p].getReal(), 2)
						+ Math.pow(cmplx_y[p].getImaginary(), 2)));
				c.addValue(Math.sqrt(Math.pow(cmplx_x[p].getReal(), 2)
						+ Math.pow(cmplx_z[p].getImaginary(), 2)));
			}
			features.high_pass_maxfft_x = (float) a.getMax();
			features.high_pass_maxfft_y = (float) b.getMax();
			features.high_pass_maxfft_z = (float) c.getMax();
			if (lat.size() > 0) {
				features.mLatitude = lat.get(lat.size() / 2);
				features.mLongitude = lng.get(lng.size() / 2);
				features.timestamp = loc_time.get(loc_time.size() / 2);
			}

            double k = new LoadClassifier().classify(features);
            ClassifiedData cd = new ClassifiedData(features.mLatitude,features.mLongitude, k,features.timestamp);
          /*  if ( k == 1.0 )
            {
                final SharedPreferences reader = MainActivity.context_.getSharedPreferences("potholes", Context.MODE_PRIVATE);
                 int p  = reader.getInt("potholes", 0);

                final SharedPreferences.Editor editor = reader.edit();
                editor.putInt("potholes", ++p);
                editor.commit();

            }
            else if ( k == -1.0)
            {
                final SharedPreferences reader = MainActivity.context_.getSharedPreferences("speedbreakers", Context.MODE_PRIVATE);
                int p  = reader.getInt("speedbreakers", 0);
                final SharedPreferences.Editor editor = reader.edit();
                editor.putInt("speedbreakers", ++p);
                editor.commit();
            }*/
			new FileHandler().WriteFile(cd.toString() + "\n");

			acc_x.clear();
			acc_y.clear();
			acc_z.clear();
		}
		new UploadCongestionData().Upload(data);
		return null;
	}
}
