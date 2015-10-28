package com.mappingroadconditions;

import inf155.Convolution;

import java.util.ArrayList;

public class HighPassFilter {
	Convolution conv;
	double[] high_pass_filtercoefficients = { 0.0000014016224694085282,
			0.0000017242197900498334, 0.0000019167653117133005,
			0.0000019432451138852633, 0.0000017807962254146439,
			0.0000014235511060559888, 0.00000088506514881386779,
			0.00000019893717620695541, -0.00000058260922939240523,
			-0.0000013922205152591423, -0.000002152806213098908,
			-0.000002784439057906075, -0.0000032121815014992252,
			-0.0000033741138852219411, -0.0000032287592065335464,
			-0.0000027610958509916813, -0.0000019864288884753463,
			-0.00000095155047783697745, 0.00000026714990871544992,
			0.0000015686827475180533, 0.0000028353116784407527,
			0.000003942704846754794, 0.0000047715539483287542,
			0.0000052196215073022927, 0.0000052130568737975517,
			0.0000047158085991520189, 0.0000037360638837358477,
			0.0000023288623250792423, 0.00000059434735405264014,
			-0.0000013284901684670495, -0.0000032722894705642926,
			-0.0000050552983797585085, -0.0000064977408456768881,
			-0.0000074391524864560795, -0.0000077550727437398388,
			-0.0000073714366877879042, -0.0000062751225537742053,
			-0.0000045193817002713525, -0.0000022232893546343172,
			0.0000004351240608728781, 0.0000032318139628315354,
			0.0000059148878204213347, 0.0000082265937932574404,
			0.0000099272744875827401, 0.000010819125012476771,
			0.000010767480351302856, 0.0000097174544617285326,
			0.000007704065542384316, 0.0000048544910819464534,
			0.0000013817651682336664, -0.0000024299958832712272,
			-0.0000062479433439874398, -0.0000097187537677867629,
			-0.000012500470447996567, -0.000014295131836756121,
			-0.000014879156628678344, -0.000014128496344774713,
			-0.000012035893163581476, -0.0000087181779154375707,
			-0.0000044123684755052852, 0.00000053968380423118293,
			0.0000057172874679799543, 0.000010655687108829517,
			0.000014886839991843763, 0.000017982698909855251,
			0.000019597086838686096, 0.000019502175253691138,
			0.000017615875511632583, 0.000014017112394583449,
			0.0000089469269870223316, 0.0000027945764978801414,
			-0.0000039308412074743391, -0.000010641331630623868,
			-0.000016720340298777587, -0.000021578271283256095,
			-0.000024708284820265376, -0.000025737203717426281,
			-0.000024466560680903631, -0.000020899494795871587,
			-0.000015250314504501286, -0.0000079350033994588788,
			0.00000045736585249568041, 0.0000092105720925535838,
			0.000017541109855889641, 0.000024667092905844672,
			0.000029880246326772858, 0.000032614473853373569,
			0.000032504504014560426, 0.000029428739752308885,
			0.000023531630934911513, 0.000015222572255849823,
			0.0000051503657260928537, -0.0000058454962390236786,
			-0.000016803272232817016, -0.000026721706816628942,
			-0.000034650068034674386, -0.000039777515309965565,
			-0.00004151353927515011, -0.000039551680212799892,
			-0.000033909937407218889, -0.000024943147766529002,
			-0.000013324993821104989, 0.00000000000000000090770904566209918,
			0.000013891344417875889, 0.000027108590743395596,
			0.000038420534341629403, 0.000046718140808395958,
			0.000051120984185065305, 0.000051067161854414587,
			0.000046377757728763672, 0.000037288899642298991,
			0.000024447151423574473, 0.0000088671754387817238,
			-0.0000081459770488740908, -0.000025104248867517335,
			-0.000040464984018698329, -0.000052769437421228439,
			-0.000060779107790735683, -0.000063597330875360606,
			-0.000060764429766376725, -0.000052316686485496431,
			-0.00003880233634041373, -0.00002125146085750412,
			-0.0000011007569314372219, 0.000019921671426217841,
			0.000039942497097805686, 0.000057108152071295643,
			0.000069754250821078842, 0.00007656415210829633,
			0.000076701777226125618, 0.00006990560206238652,
			0.000056533804161093173, 0.000037554639814089879,
			0.000014480912552757796, -0.000010747540157057873,
			-0.000035924524655872346, -0.000058768254532037391,
			-0.000077125901744270404, -0.000089173792337798286,
			-0.000093594850782207487, -0.000089716316033395586,
			-0.00007759377283738354, -0.000058031943747694211,
			-0.000032538116774008539, -0.0000032100800247531905,
			0.000027433530271944637, 0.000056667038633873191,
			0.000081797777085329085, 0.00010041144141437971,
			0.00011060062463702535, 0.00011115516864708465,
			0.00010169572216675352, 0.000082736430267405772,
			0.000055668654265368231, 0.000022664533936597462,
			-0.000013493542485931989, -0.000049644551471736319,
			-0.000082522531352375775, -0.00010904890555318508,
			-0.00012661750358785806, -0.00013334616011878288,
			-0.00012827095212771249, -0.00011146356392557761,
			-0.000084058618988426407, -0.000048185570315040237,
			-0.0000068082364090147584, 0.000036516441582093598,
			0.000077941137642411037, 0.00011366688789123378,
			0.00014028827843065833, 0.00015511398075905859,
			0.00015643279398310547, 0.00014369935651626002,
			0.00011762016656085162, 0.000080128988787028264,
			0.000034250418120581194, -0.00001613952186544451,
			-0.000066637254699722162, -0.00011269327769811057,
			-0.00015001875678908691, -0.0001749810365994052,
			-0.0001849519145908773, -0.00017857570693149692,
			-0.00015593040056705132, -0.00011856406490349419,
			-0.000069399460122972158, -0.000012511506872271719,
			0.00004720606181769885, 0.0001044580210336314,
			0.00015401369741591597, 0.0001911810276421506,
			0.00021224599363489324, 0.00021483667268650561,
			0.00019817676570975813, 0.00016320243987525803,
			0.00011252792699839812, 0.000050258580522056538,
			-0.000018336142379146716, -0.000087264274238768911,
			-0.00015033090722820281, -0.00020169103524527357,
			-0.0002363866731048286, -0.00025081927917641142,
			-0.00024311296205266203, -0.00021333256118446212,
			-0.0001635327936350337, -0.000097629256740520265,
			-0.00002109791790177667, 0.000059474591355145326,
			0.00013695345381898921, 0.00020428328881226076,
			0.0002551260792639255, 0.0002844521128847348,
			0.00028902930647117022, 0.00026776395380434347,
			0.00022185806431119319, 0.00015476406663993657,
			0.000071935422020669246, -0.000019609921464593696,
			-0.00011187894663270105, -0.00019659684137637076,
			-0.00026594441499198712, -0.0003132743490971416,
			-0.00033374113023671088, -0.00032478555372755726,
			-0.00028642622960650958, -0.00022132666523643008,
			-0.00013462590876007737, -0.00003354174617253968,
			0.000073223803217436573, 0.00017622833385557359,
			0.00026611914508932168, 0.00033447704602423387,
			0.00037459820954358379, 0.00038214202155775295,
			0.00035558307626338523, 0.0002964215167279329,
			0.00020912651501286389, 0.00010081108578771513,
			-0.000019339407549390356, -0.00014083992575835436,
			-0.00025281519340741859, -0.00034496752373947167,
			-0.00040851738785742441, -0.00043703136320015344,
			-0.00042706007342384046, -0.0003785238855040842,
			-0.00029480527457397806, -0.00018253214686495898,
			-0.000051063844122832006, 0.000088281382707245849,
			0.00022319439583883579, 0.00034145648074703302,
			0.00043203948964806201, 0.00048612632095258886,
			0.00049795796467246164, 0.00046542663218935882,
			0.00039035535198840733, 0.00027843115757050801,
			0.00013878935488172663, -0.000016722312023598514,
			-0.00017454127494157185, -0.00032056623910410717,
			-0.00044141038322067025, -0.00052562087996823389,
			-0.00056475429516618207, -0.00055420771001826632,
			-0.00049372495674362862, -0.00038752462716060311,
			-0.00024402923741191339, -0.000075210295717109159,
			0.00010440109549648781, 0.00027895721698698142,
			0.00043268467722500309, 0.00055130189373353447,
			0.00062333640587682515, 0.00064122130593112817,
			0.00060206706223837815, 0.000508031721721249,
			0.00036624674584081893, 0.00018829465037202838,
			-0.000010725204948083806, -0.00021346847855154301,
			-0.0004018490911446718, -0.00055864430955452824,
			-0.00066905995184496044, -0.00072211426795602048,
			-0.00071171203664628505, -0.00063730524779591661,
			-0.00050407145236892035, -0.00032258257007307303,
			-0.00010798206115808416, 0.00012126708078805519,
			0.00034496108892034935, 0.00054292681745891761,
			0.00069683127246670596, 0.00079186780515047898,
			0.00081816485390749573, 0.00077178448458811299,
			0.00065521174623486269, 0.00047727928285857412,
			0.00025252104208729807, -0.0000000000000000016943769391064677,
			-0.00025829696865530997, -0.00049936299654297102,
			-0.00070120997904153117, -0.00084486243311213854,
			-0.00091612666224662295, -0.00090697150551053904,
			-0.00081638807290204966, -0.00065063958894515084,
			-0.00042286514311890429, -0.00015205825578524943,
			0.00013850231101991144, 0.00042324121940495941,
			0.0006765259290258508, 0.00087496173994327825,
			0.00099953809263893592, 0.0010374304518362934,
			0.00098328857401692592, 0.00083988418885924991,
			0.00061804567853078333, 0.0003358696010852144,
			0.000017263467093142343, -0.00031006469225893635,
			-0.00061700228572804546, -0.00087560768867041305,
			-0.0010616386871065053, -0.0011568063448345713,
			-0.0011505463102028991, -0.0010411381510066174,
			-0.00083605790708526949, -0.00055151514655075431,
			-0.00021119773955523809, 0.00015568098756973679,
			0.00051687349226634818, 0.00083991667976221897,
			0.0010950330103201107, 0.0012578562285013523, 0.001311733841038024,
			0.0012493908202724331, 0.0010737913643974518,
			0.00079810366268953713, 0.00044475074887176619,
			0.000043611911152074227, -0.00037048344396371465,
			-0.0007607611177460102, -0.00109174897510758,
			-0.0013324803107095118, -0.0014593662232195931,
			-0.0014584726637109932, -0.0013269851409644076,
			-0.0010737118387804212, -0.00071855852567368275,
			-0.00029099906202458361, 0.00017234437239702712,
			0.00063081506826147542, 0.0010432668906292721,
			0.0013717335357514137, 0.0015848967653015341,
			0.0016610390712930012, 0.0015902051872764629,
			0.0013753613123158412, 0.0010324256705890507,
			0.0005891419807292418, 0.000082870108985869006,
			-0.00044253342941378952, -0.00094048725186316666,
			-0.0013657871842081473, -0.0016786798021003644,
			-0.0018485429510399379, -0.0018568354917631287,
			-0.0016990359925219132, -0.0013853738790299203,
			-0.0009402597848260745, -0.00040043576056204893,
			0.00018801930844538514, 0.00077358846338457298,
			0.0013037927565476789, 0.0017298587020414065,
			0.0020111650612964281, 0.0021190669687242525,
			0.0020397414899038921, 0.001775777573119746, 0.0013463387448166865,
			0.00078585004782198839, 0.00014129108401149927,
			-0.0005316968285523139, -0.0011735707594596346,
			-0.001726110027307269, -0.0021376493247420598,
			-0.0023678548446007643, -0.0023916065722005496,
			-0.0022016188963411708, -0.0018095350523924549,
			-0.0012453602854636142, -0.00055524314471137003,
			0.00020223847579674631, 0.00096099305041150076,
			0.0016531252245067741, 0.0022149512752178396,
			0.0025927924112306381, 0.002748024588925653, 0.0026609164547772191,
			0.0023328828470048687, 0.0017869125750026354,
			0.0010660851955212563, 0.00023025902239298554,
			-0.0006488228997720872, -0.0014936152924047766,
			-0.0022275323064383126, -0.002781805434005183,
			-0.0031018308437856671, -0.0031524285059694552,
			-0.0029215165498964715, -0.0024218314444219378,
			-0.0016904877818499672, -0.00078635722186964462,
			0.00021456128236719399, 0.0012254252993091565,
			0.0021559759121630487, 0.0029204816746585148,
			0.0034455100514212962, 0.0036768326691403769,
			0.0035848275508465091, 0.0031678560647109438,
			0.0024532561106328714, 0.0014957930683700247,
			0.00037362969108184512, -0.00081790327292727771,
			-0.0019742517256003378, -0.0029906485233401649,
			-0.0037714263938068131, -0.0042388205734762369,
			-0.0043404682205613497, -0.0040549016875860277,
			-0.003394487047092957, -0.0024054662903222894,
			-0.0011650048855149141, 0.00022459420977691142,
			0.0016440955225182667, 0.0029673151537561418,
			0.0040720673847194832, 0.0048511458813703919,
			0.0052223773473155689, 0.0051368337039435852,
			0.0045844179953833473, 0.0035962406436675612,
			0.0022434613596423758, 0.00063256751716831534,
			-0.0011026320917132801, -0.0028117245245045945,
			-0.0043402918357258293, -0.0055433697082755435,
			-0.0062986307980357793, -0.0065181388180596492,
			-0.0061575981354349881, -0.0052221969871656644,
			-0.0037683988799250478, -0.0019013574883894623,
			0.00023200943437757821, 0.0024538809197996443,
			0.0045695717522435892, 0.0063836927132323671,
			0.0077171522130911451, 0.0084235947292155104,
			0.0084038589325132473, 0.0076171429606449641,
			0.0060877814562786984, 0.0039068548080326256,
			0.0012282430731001012, -0.0017408240339678628,
			-0.0047542180082368818, -0.0075454318702831858,
			-0.0098488689150802149, -0.011422319664817661,
			-0.012068883326268204, -0.011656559140932277,
			-0.010133841379975722, -0.0075398952078406566,
			-0.0040082542301845114, 0.00023656062205895797,
			0.0048895801618765904, 0.0095826187193960299, 0.013907206888152755,
			0.017441766214378412, 0.019781196165850872, 0.020566800586825303,
			0.019514386880078945, 0.01643843698800929, 0.011270473456431089,
			0.0040701129027877611, -0.0049722117642643201,
			-0.015541304393819766, -0.027211859100482242, -0.03947092175447104,
			-0.051746796358167396, -0.063442552550376508,
			-0.073971936405660743, -0.082795286719153025,
			-0.089453029305237991, -0.0935944661063383, 0.90499890223879109,
			-0.0935944661063383, -0.089453029305237991, -0.082795286719153025,
			-0.073971936405660743, -0.063442552550376508,
			-0.051746796358167396, -0.03947092175447104, -0.027211859100482242,
			-0.015541304393819766, -0.0049722117642643201,
			0.0040701129027877611, 0.011270473456431089, 0.01643843698800929,
			0.019514386880078945, 0.020566800586825303, 0.019781196165850872,
			0.017441766214378412, 0.013907206888152755, 0.0095826187193960299,
			0.0048895801618765904, 0.00023656062205895797,
			-0.0040082542301845114, -0.0075398952078406566,
			-0.010133841379975722, -0.011656559140932277,
			-0.012068883326268204, -0.011422319664817661,
			-0.0098488689150802149, -0.0075454318702831858,
			-0.0047542180082368818, -0.0017408240339678628,
			0.0012282430731001012, 0.0039068548080326256,
			0.0060877814562786984, 0.0076171429606449641,
			0.0084038589325132473, 0.0084235947292155104,
			0.0077171522130911451, 0.0063836927132323671,
			0.0045695717522435892, 0.0024538809197996443,
			0.00023200943437757821, -0.0019013574883894623,
			-0.0037683988799250478, -0.0052221969871656644,
			-0.0061575981354349881, -0.0065181388180596492,
			-0.0062986307980357793, -0.0055433697082755435,
			-0.0043402918357258293, -0.0028117245245045945,
			-0.0011026320917132801, 0.00063256751716831534,
			0.0022434613596423758, 0.0035962406436675612,
			0.0045844179953833473, 0.0051368337039435852,
			0.0052223773473155689, 0.0048511458813703919,
			0.0040720673847194832, 0.0029673151537561418,
			0.0016440955225182667, 0.00022459420977691142,
			-0.0011650048855149141, -0.0024054662903222894,
			-0.003394487047092957, -0.0040549016875860277,
			-0.0043404682205613497, -0.0042388205734762369,
			-0.0037714263938068131, -0.0029906485233401649,
			-0.0019742517256003378, -0.00081790327292727771,
			0.00037362969108184512, 0.0014957930683700247,
			0.0024532561106328714, 0.0031678560647109438,
			0.0035848275508465091, 0.0036768326691403769,
			0.0034455100514212962, 0.0029204816746585148,
			0.0021559759121630487, 0.0012254252993091565,
			0.00021456128236719399, -0.00078635722186964462,
			-0.0016904877818499672, -0.0024218314444219378,
			-0.0029215165498964715, -0.0031524285059694552,
			-0.0031018308437856671, -0.002781805434005183,
			-0.0022275323064383126, -0.0014936152924047766,
			-0.0006488228997720872, 0.00023025902239298554,
			0.0010660851955212563, 0.0017869125750026354,
			0.0023328828470048687, 0.0026609164547772191, 0.002748024588925653,
			0.0025927924112306381, 0.0022149512752178396,
			0.0016531252245067741, 0.00096099305041150076,
			0.00020223847579674631, -0.00055524314471137003,
			-0.0012453602854636142, -0.0018095350523924549,
			-0.0022016188963411708, -0.0023916065722005496,
			-0.0023678548446007643, -0.0021376493247420598,
			-0.001726110027307269, -0.0011735707594596346,
			-0.0005316968285523139, 0.00014129108401149927,
			0.00078585004782198839, 0.0013463387448166865,
			0.001775777573119746, 0.0020397414899038921, 0.0021190669687242525,
			0.0020111650612964281, 0.0017298587020414065,
			0.0013037927565476789, 0.00077358846338457298,
			0.00018801930844538514, -0.00040043576056204893,
			-0.0009402597848260745, -0.0013853738790299203,
			-0.0016990359925219132, -0.0018568354917631287,
			-0.0018485429510399379, -0.0016786798021003644,
			-0.0013657871842081473, -0.00094048725186316666,
			-0.00044253342941378952, 0.000082870108985869006,
			0.0005891419807292418, 0.0010324256705890507,
			0.0013753613123158412, 0.0015902051872764629,
			0.0016610390712930012, 0.0015848967653015341,
			0.0013717335357514137, 0.0010432668906292721,
			0.00063081506826147542, 0.00017234437239702712,
			-0.00029099906202458361, -0.00071855852567368275,
			-0.0010737118387804212, -0.0013269851409644076,
			-0.0014584726637109932, -0.0014593662232195931,
			-0.0013324803107095118, -0.00109174897510758,
			-0.0007607611177460102, -0.00037048344396371465,
			0.000043611911152074227, 0.00044475074887176619,
			0.00079810366268953713, 0.0010737913643974518,
			0.0012493908202724331, 0.001311733841038024, 0.0012578562285013523,
			0.0010950330103201107, 0.00083991667976221897,
			0.00051687349226634818, 0.00015568098756973679,
			-0.00021119773955523809, -0.00055151514655075431,
			-0.00083605790708526949, -0.0010411381510066174,
			-0.0011505463102028991, -0.0011568063448345713,
			-0.0010616386871065053, -0.00087560768867041305,
			-0.00061700228572804546, -0.00031006469225893635,
			0.000017263467093142343, 0.0003358696010852144,
			0.00061804567853078333, 0.00083988418885924991,
			0.00098328857401692592, 0.0010374304518362934,
			0.00099953809263893592, 0.00087496173994327825,
			0.0006765259290258508, 0.00042324121940495941,
			0.00013850231101991144, -0.00015205825578524943,
			-0.00042286514311890429, -0.00065063958894515084,
			-0.00081638807290204966, -0.00090697150551053904,
			-0.00091612666224662295, -0.00084486243311213854,
			-0.00070120997904153117, -0.00049936299654297102,
			-0.00025829696865530997, -0.0000000000000000016943769391064677,
			0.00025252104208729807, 0.00047727928285857412,
			0.00065521174623486269, 0.00077178448458811299,
			0.00081816485390749573, 0.00079186780515047898,
			0.00069683127246670596, 0.00054292681745891761,
			0.00034496108892034935, 0.00012126708078805519,
			-0.00010798206115808416, -0.00032258257007307303,
			-0.00050407145236892035, -0.00063730524779591661,
			-0.00071171203664628505, -0.00072211426795602048,
			-0.00066905995184496044, -0.00055864430955452824,
			-0.0004018490911446718, -0.00021346847855154301,
			-0.000010725204948083806, 0.00018829465037202838,
			0.00036624674584081893, 0.000508031721721249,
			0.00060206706223837815, 0.00064122130593112817,
			0.00062333640587682515, 0.00055130189373353447,
			0.00043268467722500309, 0.00027895721698698142,
			0.00010440109549648781, -0.000075210295717109159,
			-0.00024402923741191339, -0.00038752462716060311,
			-0.00049372495674362862, -0.00055420771001826632,
			-0.00056475429516618207, -0.00052562087996823389,
			-0.00044141038322067025, -0.00032056623910410717,
			-0.00017454127494157185, -0.000016722312023598514,
			0.00013878935488172663, 0.00027843115757050801,
			0.00039035535198840733, 0.00046542663218935882,
			0.00049795796467246164, 0.00048612632095258886,
			0.00043203948964806201, 0.00034145648074703302,
			0.00022319439583883579, 0.000088281382707245849,
			-0.000051063844122832006, -0.00018253214686495898,
			-0.00029480527457397806, -0.0003785238855040842,
			-0.00042706007342384046, -0.00043703136320015344,
			-0.00040851738785742441, -0.00034496752373947167,
			-0.00025281519340741859, -0.00014083992575835436,
			-0.000019339407549390356, 0.00010081108578771513,
			0.00020912651501286389, 0.0002964215167279329,
			0.00035558307626338523, 0.00038214202155775295,
			0.00037459820954358379, 0.00033447704602423387,
			0.00026611914508932168, 0.00017622833385557359,
			0.000073223803217436573, -0.00003354174617253968,
			-0.00013462590876007737, -0.00022132666523643008,
			-0.00028642622960650958, -0.00032478555372755726,
			-0.00033374113023671088, -0.0003132743490971416,
			-0.00026594441499198712, -0.00019659684137637076,
			-0.00011187894663270105, -0.000019609921464593696,
			0.000071935422020669246, 0.00015476406663993657,
			0.00022185806431119319, 0.00026776395380434347,
			0.00028902930647117022, 0.0002844521128847348,
			0.0002551260792639255, 0.00020428328881226076,
			0.00013695345381898921, 0.000059474591355145326,
			-0.00002109791790177667, -0.000097629256740520265,
			-0.0001635327936350337, -0.00021333256118446212,
			-0.00024311296205266203, -0.00025081927917641142,
			-0.0002363866731048286, -0.00020169103524527357,
			-0.00015033090722820281, -0.000087264274238768911,
			-0.000018336142379146716, 0.000050258580522056538,
			0.00011252792699839812, 0.00016320243987525803,
			0.00019817676570975813, 0.00021483667268650561,
			0.00021224599363489324, 0.0001911810276421506,
			0.00015401369741591597, 0.0001044580210336314,
			0.00004720606181769885, -0.000012511506872271719,
			-0.000069399460122972158, -0.00011856406490349419,
			-0.00015593040056705132, -0.00017857570693149692,
			-0.0001849519145908773, -0.0001749810365994052,
			-0.00015001875678908691, -0.00011269327769811057,
			-0.000066637254699722162, -0.00001613952186544451,
			0.000034250418120581194, 0.000080128988787028264,
			0.00011762016656085162, 0.00014369935651626002,
			0.00015643279398310547, 0.00015511398075905859,
			0.00014028827843065833, 0.00011366688789123378,
			0.000077941137642411037, 0.000036516441582093598,
			-0.0000068082364090147584, -0.000048185570315040237,
			-0.000084058618988426407, -0.00011146356392557761,
			-0.00012827095212771249, -0.00013334616011878288,
			-0.00012661750358785806, -0.00010904890555318508,
			-0.000082522531352375775, -0.000049644551471736319,
			-0.000013493542485931989, 0.000022664533936597462,
			0.000055668654265368231, 0.000082736430267405772,
			0.00010169572216675352, 0.00011115516864708465,
			0.00011060062463702535, 0.00010041144141437971,
			0.000081797777085329085, 0.000056667038633873191,
			0.000027433530271944637, -0.0000032100800247531905,
			-0.000032538116774008539, -0.000058031943747694211,
			-0.00007759377283738354, -0.000089716316033395586,
			-0.000093594850782207487, -0.000089173792337798286,
			-0.000077125901744270404, -0.000058768254532037391,
			-0.000035924524655872346, -0.000010747540157057873,
			0.000014480912552757796, 0.000037554639814089879,
			0.000056533804161093173, 0.00006990560206238652,
			0.000076701777226125618, 0.00007656415210829633,
			0.000069754250821078842, 0.000057108152071295643,
			0.000039942497097805686, 0.000019921671426217841,
			-0.0000011007569314372219, -0.00002125146085750412,
			-0.00003880233634041373, -0.000052316686485496431,
			-0.000060764429766376725, -0.000063597330875360606,
			-0.000060779107790735683, -0.000052769437421228439,
			-0.000040464984018698329, -0.000025104248867517335,
			-0.0000081459770488740908, 0.0000088671754387817238,
			0.000024447151423574473, 0.000037288899642298991,
			0.000046377757728763672, 0.000051067161854414587,
			0.000051120984185065305, 0.000046718140808395958,
			0.000038420534341629403, 0.000027108590743395596,
			0.000013891344417875889, 0.00000000000000000090770904566209918,
			-0.000013324993821104989, -0.000024943147766529002,
			-0.000033909937407218889, -0.000039551680212799892,
			-0.00004151353927515011, -0.000039777515309965565,
			-0.000034650068034674386, -0.000026721706816628942,
			-0.000016803272232817016, -0.0000058454962390236786,
			0.0000051503657260928537, 0.000015222572255849823,
			0.000023531630934911513, 0.000029428739752308885,
			0.000032504504014560426, 0.000032614473853373569,
			0.000029880246326772858, 0.000024667092905844672,
			0.000017541109855889641, 0.0000092105720925535838,
			0.00000045736585249568041, -0.0000079350033994588788,
			-0.000015250314504501286, -0.000020899494795871587,
			-0.000024466560680903631, -0.000025737203717426281,
			-0.000024708284820265376, -0.000021578271283256095,
			-0.000016720340298777587, -0.000010641331630623868,
			-0.0000039308412074743391, 0.0000027945764978801414,
			0.0000089469269870223316, 0.000014017112394583449,
			0.000017615875511632583, 0.000019502175253691138,
			0.000019597086838686096, 0.000017982698909855251,
			0.000014886839991843763, 0.000010655687108829517,
			0.0000057172874679799543, 0.00000053968380423118293,
			-0.0000044123684755052852, -0.0000087181779154375707,
			-0.000012035893163581476, -0.000014128496344774713,
			-0.000014879156628678344, -0.000014295131836756121,
			-0.000012500470447996567, -0.0000097187537677867629,
			-0.0000062479433439874398, -0.0000024299958832712272,
			0.0000013817651682336664, 0.0000048544910819464534,
			0.000007704065542384316, 0.0000097174544617285326,
			0.000010767480351302856, 0.000010819125012476771,
			0.0000099272744875827401, 0.0000082265937932574404,
			0.0000059148878204213347, 0.0000032318139628315354,
			0.0000004351240608728781, -0.0000022232893546343172,
			-0.0000045193817002713525, -0.0000062751225537742053,
			-0.0000073714366877879042, -0.0000077550727437398388,
			-0.0000074391524864560795, -0.0000064977408456768881,
			-0.0000050552983797585085, -0.0000032722894705642926,
			-0.0000013284901684670495, 0.00000059434735405264014,
			0.0000023288623250792423, 0.0000037360638837358477,
			0.0000047158085991520189, 0.0000052130568737975517,
			0.0000052196215073022927, 0.0000047715539483287542,
			0.000003942704846754794, 0.0000028353116784407527,
			0.0000015686827475180533, 0.00000026714990871544992,
			-0.00000095155047783697745, -0.0000019864288884753463,
			-0.0000027610958509916813, -0.0000032287592065335464,
			-0.0000033741138852219411, -0.0000032121815014992252,
			-0.000002784439057906075, -0.000002152806213098908,
			-0.0000013922205152591423, -0.00000058260922939240523,
			0.00000019893717620695541, 0.00000088506514881386779,
			0.0000014235511060559888, 0.0000017807962254146439,
			0.0000019432451138852633, 0.0000019167653117133005,
			0.0000017242197900498334, 0.0000014016224694085282 };

	ArrayList<Float> execute(double[] input) {
		ArrayList<Float> temp = new ArrayList<Float>();
		conv = new Convolution();
		conv.setFilter(high_pass_filtercoefficients);
		conv.setInput(input);
		conv.convolve();
		double[] output = conv.getOutput();
		for (int i = 0; i < input.length; i++)
			// output.length; i++) // testing with a 100
			temp.add((float) output[i]);
		return temp;
	}
}
