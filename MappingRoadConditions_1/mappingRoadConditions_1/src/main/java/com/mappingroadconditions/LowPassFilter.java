package com.mappingroadconditions;

import java.util.ArrayList;
import inf155.Convolution;

public class LowPassFilter {
	/*
	 * private float timeConstant = 1f; private float alpha = 0.9f; private
	 * float dt = 0; private float timestamp = System.nanoTime(); private float
	 * startTime = 0; private int count = 0; private float[] output = new
	 * float[] { 0, 0, 0 }; private float[] gravity = new float[] { 0, 0, 0 };
	 * private float[] input = new float[] { 0, 0, 0 }; public float[]
	 * addSamples(float[] acceleration) { if (startTime == 0) { startTime =
	 * System.nanoTime(); } timestamp = System.nanoTime();
	 * System.arraycopy(acceleration, 0, this.input, 0, acceleration.length); dt
	 * = 1 / (count++ / ((timestamp - startTime) / 1000000000.0f)); alpha =
	 * timeConstant / (timeConstant + dt); if (count > 5) { gravity[0] = alpha *
	 * gravity[0] + (1 - alpha) * input[0]; gravity[1] = alpha * gravity[1] + (1
	 * - alpha) * input[1]; gravity[2] = alpha * gravity[2] + (1 - alpha) *
	 * input[2]; output[0] = input[0] - gravity[0]; output[1] = input[1] -
	 * gravity[1]; output[2] = input[2] - gravity[2]; } return output; } public
	 * void setTimeConstant(float timeConstant) { this.timeConstant =
	 * timeConstant; } public void reset() { startTime = 0; timestamp = 0; count
	 * = 0; dt = 0; alpha = 0; }
	 */

	// / The above is the implementation by android
	Convolution conv;
	double[] low_pass_filtercoefficients = { 0.0000033476783404103552,
			0.0000038336239196914068, 0.0000042533086352576578,
			0.0000045732753031623142, 0.0000047594530512495943,
			0.0000047785299202441701, 0.000004599448037420784,
			0.0000041949745570082734, 0.0000035432943700619898,
			0.0000026295648312196295, 0.0000014473687564762166,
			-0.000000000000000000014746405619216851, -0.000001698483749769973,
			-0.0000036225035387550168, -0.0000057345543565642376,
			-0.0000079852024182301459, -0.000010313523656427986,
			-0.000012648009999989936, -0.000014907955458216339,
			-0.000017005317845338151, -0.000018847034614564166,
			-0.000020337753249376585, -0.000021382918564629416,
			-0.00002189214172942501, -0.000021782759491179229,
			-0.000020983477613922745, -0.000019437980584313137,
			-0.000017108380785827226, -0.000013978375130919328,
			-0.000010055976021740032, -0.0000053756868238965931,
			0.00000000000000000017765765377972923, 0.000005979891266244084,
			0.000012444259799195, 0.000019245959632070832,
			0.000026212534077442071, 0.000033149331916061168,
			0.000039843615092407927, 0.000046069607590926331,
			0.000051594400574329425, 0.000056184594465101503,
			0.000059613525579628545, 0.000061668894350077713,
			0.000062160585257597974, 0.000060928446475453353,
			0.000057849780925516365, 0.000052846290909895662,
			0.000045890216458386056, 0.000037009413607849607,
			0.000026291133356064394, 0.000013884285118000173,
			-0.00000000000000000011333750112955502, -0.000015089651336369506,
			-0.000031054884736436305, -0.000047513418073549904,
			-0.0000640376637549137, -0.000080163824186442508,
			-0.000095402752759256898, -0.00010925237949430151,
			-0.00012121143900117421, -0.00013079418089254673,
			-0.00013754569126684545, -0.0001410574102873592,
			-0.00014098239707727769, -0.00013704987075860466,
			-0.00012907854687778999, -0.00011698829276247318,
			-0.00010080964426229261, -0.00008069076016024542,
			-0.000056901439182828691, -0.000029833887414624576,
			0.00000000000000000021764182454385067, 0.000031974991203057731,
			0.000065362554796706435, 0.000099344527632313725,
			0.00013303024926555079, 0.0001654768876318544,
			0.00019571257044638488, 0.00022276183157022181,
			0.00024567278449419378, 0.00026354534950258612,
			0.00027555979016157618, 0.0002810047614419097,
			0.00027930403858129394, 0.00027004108481313438,
			0.00025298062888739337, 0.00022808646082435425,
			0.0001955347168361824, 0.00015572201137101552,
			0.00010926788458631988, 0.000057011165306054733,
			-0.0000000000000000003679234391515846, -0.000060524535798659569,
			-0.00012315114991087882, -0.00018632709178954845,
			-0.00024839249242207708, -0.00030761963858296176,
			-0.00036225635974836801, -0.00041057254562679906,
			-0.00045090867125334688, -0.00048172508994555265,
			-0.00050165076668189806, -0.00050953006959348235,
			-0.00050446621846506935, -0.00048586000876163087,
			-0.00045344248908976378, -0.00040730036946461741,
			-0.00034789307645496128, -0.0002760605472437037,
			-0.00019302106474312165, -0.00010035867590632156,
			0.00000000000000000056476525089023279, 0.00010581948337048451,
			0.00021459928229008635, 0.00032362719431446539,
			0.00043004108601116129, 0.00053089789338070569,
			0.00062324832658480203, 0.00070421553210612707,
			0.00077107577229222483, 0.00082133903325145175,
			0.000852827373415676, 0.00086374878178402121,
			0.00085276433062412269, 0.00081904648447156099,
			0.00076232656627835311, 0.00068292958148086951,
			0.00058179485882645608, 0.00046048127853817597,
			0.00032115621766293966, 0.00016656774152862651,
			-0.00000000000000000080149674656317145, -0.00017478776268422307,
			-0.00035363771663766009, -0.00053208414977444073,
			-0.00070545660172582794, -0.00086899325171594798,
			-0.0010179620121229345, -0.0011477864599249209,
			-0.0012541734817499873, -0.0013332393243615411,
			-0.0013816306390855657, -0.0013966370920436284,
			-0.0013762921861259921, -0.0013194591071744499,
			-0.0012258986651838802, -0.0010963167483268126,
			-0.00093238913761113141, -0.00073676203491311295,
			-0.00051302722658751459, -0.00026567142632317347,
			0.0000000000000000010637157116811485, 0.00027796404392414276,
			0.00056160422962993914, 0.00084385571387271801,
			0.0011173684292692708, 0.001374684798672628, 0.0016084280244955272,
			0.0018114965162929278, 0.0019772596774096131,
			0.0020997500397298336, 0.0021738466247826697,
			0.0021954444271195229, 0.0021616050663647844,
			0.0020706839387193265, 0.0019224296145965872,
			0.0017180517706201139, 0.0014602546020740224,
			0.0011532334234010666, 0.00080263301369637571,
			0.00041546718274746385, -0.000000000000000001330307460227559,
			-0.00043440987909412813, -0.00087749936196864875,
			-0.0013183221471795561, -0.0017454967174440441,
			-0.0021474759392032246, -0.0025128323655630859,
			-0.0028305527157007535, -0.0030903345210355399,
			-0.0032828776036102976, -0.0034001628987134816,
			-0.0034357111614090213, -0.003384814310689144,
			-0.0032447325662718126, -0.0030148511178103984,
			-0.0026967908259463158, -0.0022944683760706959,
			-0.0018141023712991389, -0.0012641630393304818,
			-0.00065526451320457348, 0.0000000000000000015759465204726169,
			0.00068727845679135294, 0.0013907325189366747,
			0.0020933579293916206, 0.0027773469483116119,
			0.0034244858672080555, 0.0040165795402186061,
			0.0045358939508034759, 0.00496560709665924, 0.0052902579481625593,
			0.0054961829346688281, 0.0055719293519384729,
			0.0055086352708349695, 0.005300365964444294, 0.0049443975541600292,
			0.0044414394953097595, 0.0037957886639220883,
			0.0030154091469566462, 0.0021119333522028653,
			0.0011005817098374683, -0.0000000000000000017747149507808387,
			-0.0011679848289098554, -0.0023786876292254504,
			-0.0036049622466102823, -0.0048176665894850675,
			-0.0059861875465354892, -0.0070790161836479142,
			-0.0080643622921944928, -0.0089107962165412662,
			-0.0095879049904859116, -0.010066949185353202,
			-0.010321506536121626, -0.010328088379226563,
			-0.010066715212758457, -0.0095214382757418444,
			-0.0086807949299773073, -0.0075381868004229177,
			-0.0060921710663424554, -0.0043466569671370987,
			-0.0023110014597893689, 0.0000000000000000019041810219142253,
			0.0025662294269995813, 0.005362468673958347, 0.0083587255961265847,
			0.011520659188757729, 0.014810096727259489, 0.018185633918346617,
			0.021603307220411528, 0.025017325855095675, 0.028380849647941377,
			0.031646797736848108, 0.034768672399454338, 0.037701381794253148,
			0.040402045297596834, 0.042830765354432608, 0.044951350341468181,
			0.046731973856522946, 0.048145757078519438, 0.049171262363165234,
			0.049792888017501195, 0.050001156193899866, 0.049792888017501195,
			0.049171262363165234, 0.048145757078519438, 0.046731973856522946,
			0.044951350341468181, 0.042830765354432608, 0.040402045297596834,
			0.037701381794253148, 0.034768672399454338, 0.031646797736848108,
			0.028380849647941377, 0.025017325855095675, 0.021603307220411528,
			0.018185633918346617, 0.014810096727259489, 0.011520659188757729,
			0.0083587255961265847, 0.005362468673958347, 0.0025662294269995813,
			0.0000000000000000019041810219142253, -0.0023110014597893689,
			-0.0043466569671370987, -0.0060921710663424554,
			-0.0075381868004229177, -0.0086807949299773073,
			-0.0095214382757418444, -0.010066715212758457,
			-0.010328088379226563, -0.010321506536121626,
			-0.010066949185353202, -0.0095879049904859116,
			-0.0089107962165412662, -0.0080643622921944928,
			-0.0070790161836479142, -0.0059861875465354892,
			-0.0048176665894850675, -0.0036049622466102823,
			-0.0023786876292254504, -0.0011679848289098554,
			-0.0000000000000000017747149507808387, 0.0011005817098374683,
			0.0021119333522028653, 0.0030154091469566462,
			0.0037957886639220883, 0.0044414394953097595,
			0.0049443975541600292, 0.005300365964444294, 0.0055086352708349695,
			0.0055719293519384729, 0.0054961829346688281,
			0.0052902579481625593, 0.00496560709665924, 0.0045358939508034759,
			0.0040165795402186061, 0.0034244858672080555,
			0.0027773469483116119, 0.0020933579293916206,
			0.0013907325189366747, 0.00068727845679135294,
			0.0000000000000000015759465204726169, -0.00065526451320457348,
			-0.0012641630393304818, -0.0018141023712991389,
			-0.0022944683760706959, -0.0026967908259463158,
			-0.0030148511178103984, -0.0032447325662718126,
			-0.003384814310689144, -0.0034357111614090213,
			-0.0034001628987134816, -0.0032828776036102976,
			-0.0030903345210355399, -0.0028305527157007535,
			-0.0025128323655630859, -0.0021474759392032246,
			-0.0017454967174440441, -0.0013183221471795561,
			-0.00087749936196864875, -0.00043440987909412813,
			-0.000000000000000001330307460227559, 0.00041546718274746385,
			0.00080263301369637571, 0.0011532334234010666,
			0.0014602546020740224, 0.0017180517706201139,
			0.0019224296145965872, 0.0020706839387193265,
			0.0021616050663647844, 0.0021954444271195229,
			0.0021738466247826697, 0.0020997500397298336,
			0.0019772596774096131, 0.0018114965162929278,
			0.0016084280244955272, 0.001374684798672628, 0.0011173684292692708,
			0.00084385571387271801, 0.00056160422962993914,
			0.00027796404392414276, 0.0000000000000000010637157116811485,
			-0.00026567142632317347, -0.00051302722658751459,
			-0.00073676203491311295, -0.00093238913761113141,
			-0.0010963167483268126, -0.0012258986651838802,
			-0.0013194591071744499, -0.0013762921861259921,
			-0.0013966370920436284, -0.0013816306390855657,
			-0.0013332393243615411, -0.0012541734817499873,
			-0.0011477864599249209, -0.0010179620121229345,
			-0.00086899325171594798, -0.00070545660172582794,
			-0.00053208414977444073, -0.00035363771663766009,
			-0.00017478776268422307, -0.00000000000000000080149674656317145,
			0.00016656774152862651, 0.00032115621766293966,
			0.00046048127853817597, 0.00058179485882645608,
			0.00068292958148086951, 0.00076232656627835311,
			0.00081904648447156099, 0.00085276433062412269,
			0.00086374878178402121, 0.000852827373415676,
			0.00082133903325145175, 0.00077107577229222483,
			0.00070421553210612707, 0.00062324832658480203,
			0.00053089789338070569, 0.00043004108601116129,
			0.00032362719431446539, 0.00021459928229008635,
			0.00010581948337048451, 0.00000000000000000056476525089023279,
			-0.00010035867590632156, -0.00019302106474312165,
			-0.0002760605472437037, -0.00034789307645496128,
			-0.00040730036946461741, -0.00045344248908976378,
			-0.00048586000876163087, -0.00050446621846506935,
			-0.00050953006959348235, -0.00050165076668189806,
			-0.00048172508994555265, -0.00045090867125334688,
			-0.00041057254562679906, -0.00036225635974836801,
			-0.00030761963858296176, -0.00024839249242207708,
			-0.00018632709178954845, -0.00012315114991087882,
			-0.000060524535798659569, -0.0000000000000000003679234391515846,
			0.000057011165306054733, 0.00010926788458631988,
			0.00015572201137101552, 0.0001955347168361824,
			0.00022808646082435425, 0.00025298062888739337,
			0.00027004108481313438, 0.00027930403858129394,
			0.0002810047614419097, 0.00027555979016157618,
			0.00026354534950258612, 0.00024567278449419378,
			0.00022276183157022181, 0.00019571257044638488,
			0.0001654768876318544, 0.00013303024926555079,
			0.000099344527632313725, 0.000065362554796706435,
			0.000031974991203057731, 0.00000000000000000021764182454385067,
			-0.000029833887414624576, -0.000056901439182828691,
			-0.00008069076016024542, -0.00010080964426229261,
			-0.00011698829276247318, -0.00012907854687778999,
			-0.00013704987075860466, -0.00014098239707727769,
			-0.0001410574102873592, -0.00013754569126684545,
			-0.00013079418089254673, -0.00012121143900117421,
			-0.00010925237949430151, -0.000095402752759256898,
			-0.000080163824186442508, -0.0000640376637549137,
			-0.000047513418073549904, -0.000031054884736436305,
			-0.000015089651336369506, -0.00000000000000000011333750112955502,
			0.000013884285118000173, 0.000026291133356064394,
			0.000037009413607849607, 0.000045890216458386056,
			0.000052846290909895662, 0.000057849780925516365,
			0.000060928446475453353, 0.000062160585257597974,
			0.000061668894350077713, 0.000059613525579628545,
			0.000056184594465101503, 0.000051594400574329425,
			0.000046069607590926331, 0.000039843615092407927,
			0.000033149331916061168, 0.000026212534077442071,
			0.000019245959632070832, 0.000012444259799195,
			0.000005979891266244084, 0.00000000000000000017765765377972923,
			-0.0000053756868238965931, -0.000010055976021740032,
			-0.000013978375130919328, -0.000017108380785827226,
			-0.000019437980584313137, -0.000020983477613922745,
			-0.000021782759491179229, -0.00002189214172942501,
			-0.000021382918564629416, -0.000020337753249376585,
			-0.000018847034614564166, -0.000017005317845338151,
			-0.000014907955458216339, -0.000012648009999989936,
			-0.000010313523656427986, -0.0000079852024182301459,
			-0.0000057345543565642376, -0.0000036225035387550168,
			-0.000001698483749769973, -0.000000000000000000014746405619216851,
			0.0000014473687564762166, 0.0000026295648312196295,
			0.0000035432943700619898, 0.0000041949745570082734,
			0.000004599448037420784, 0.0000047785299202441701,
			0.0000047594530512495943, 0.0000045732753031623142,
			0.0000042533086352576578, 0.0000038336239196914068,
			0.0000033476783404103552 };

	ArrayList<Float> execute(double[] input) {
		ArrayList<Float> temp = new ArrayList<Float>();
		conv = new Convolution();
		conv.setFilter(low_pass_filtercoefficients);
		conv.setInput(input);
		conv.convolve();
		double[] output = conv.getOutput();
		for (int i = 0; i < input.length; i++)
			// output.length; i++) // testing with a 100
			temp.add((float) output[i]);
		return temp;
	}
}