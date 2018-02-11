package org.cdm.team6072.profiles.elevator;

import org.cdm.team6072.profiles.Constants;
import org.cdm.team6072.profiles.IMotionProfile;
import org.cdm.team6072.profiles.PIDConfig;


public class ElevTest1Prof implements IMotionProfile {


    // singleton constructor     -------------------------

    private static ElevTest1Prof mInstance;
    public static ElevTest1Prof getInstance() {
        if (mInstance == null) {
            mInstance = new ElevTest1Prof();
        }
        return mInstance;
    }

    private ElevTest1Prof() {

    }


    // kF is feed forward correction, For AndyMarkCIM, has 80 units per rev.
    // Measured max for elevator motor was 820
    // So  kF = 1023 / 820 = 1.24756
    private static PIDConfig mPIDConfig = new PIDConfig(0, 1.24756, 2.0, 0, 0);


    @Override
    public PIDConfig getPIDConfig() {
        return mPIDConfig;
    }


    @Override
    public double getUnitsPerRotation() {
        return Constants.kAndyMarkUnitsPerRotation;
    }


    // define the normal duration of each trajectory point
    // sued to control flow of points to talon
    private int mBaseDurationMs = 10;

    @Override
    public int getBaseTPDurationMs() {
        return mBaseDurationMs;
    }


    @Override
    public double[][] getPoints() {
        return Points;
    }


    public static final int kNumPoints = 700;
    // Position (rotations)	Velocity (RPM)	Duration (ms)
    public static double[][] Points = new double[][]{
            {0, 0, 10},
            {1.98019801980198E-06, 0.023762376, 10},
            {8.91089108910891E-06, 0.059405941, 10},
            {2.27722772277228E-05, 0.106930693, 10},
            {4.55445544554455E-05, 0.166336634, 10},
            {7.92079207920792E-05, 0.237623762, 10},
            {0.000125742574257426, 0.320792079, 10},
            {0.000187128712871287, 0.415841584, 10},
            {0.000265346534653465, 0.522772277, 10},
            {0.000362376237623762, 0.641584158, 10},
            {0.00048019801980198, 0.772277228, 10},
            {0.000620792079207921, 0.914851485, 10},
            {0.000786138613861386, 1.069306931, 10},
            {0.000978217821782178, 1.235643564, 10},
            {0.0011990099009901, 1.413861386, 10},
            {0.00145049504950495, 1.603960396, 10},
            {0.00173465346534654, 1.805940594, 10},
            {0.00205346534653465, 2.01980198, 10},
            {0.00240891089108911, 2.245544554, 10},
            {0.0028029702970297, 2.483168317, 10},
            {0.00323762376237624, 2.732673267, 10},
            {0.00371485148514852, 2.994059406, 10},
            {0.00423663366336634, 3.267326733, 10},
            {0.00480495049504951, 3.552475248, 10},
            {0.00542178217821782, 3.84950495, 10},
            {0.00608910891089109, 4.158415842, 10},
            {0.00680891089108911, 4.479207921, 10},
            {0.00758316831683169, 4.811881188, 10},
            {0.00841386138613862, 5.156435644, 10},
            {0.00930297029702971, 5.512871287, 10},
            {0.0102524752475248, 5.881188119, 10},
            {0.0112643564356436, 6.261386139, 10},
            {0.012340594059406, 6.653465347, 10},
            {0.0134831683168317, 7.057425743, 10},
            {0.0146940594059406, 7.473267327, 10},
            {0.0159752475247525, 7.900990099, 10},
            {0.0173287128712871, 8.340594059, 10},
            {0.0187564356435644, 8.792079208, 10},
            {0.020260396039604, 9.255445545, 10},
            {0.0218425742574258, 9.730693069, 10},
            {0.0235049504950495, 10.21782178, 10},
            {0.0252495049504951, 10.71683168, 10},
            {0.0270782178217822, 11.22772277, 10},
            {0.0289930693069307, 11.75049505, 10},
            {0.0309960396039604, 12.28514851, 10},
            {0.0330891089108911, 12.83168317, 10},
            {0.0352742574257426, 13.39009901, 10},
            {0.0375534653465347, 13.96039604, 10},
            {0.0399287128712872, 14.54257426, 10},
            {0.0424019801980198, 15.13663366, 10},
            {0.0449752475247525, 15.74257426, 10},
            {0.047650495049505, 16.36039604, 10},
            {0.0504297029702971, 16.99009901, 10},
            {0.0533148514851485, 17.63168317, 10},
            {0.0563079207920792, 18.28514851, 10},
            {0.0594108910891089, 18.95049505, 10},
            {0.0626257425742575, 19.62772277, 10},
            {0.0659544554455446, 20.31683168, 10},
            {0.0693990099009901, 21.01782178, 10},
            {0.0729613861386139, 21.73069307, 10},
            {0.0766435643564357, 22.45544554, 10},
            {0.0804475247524753, 23.19207921, 10},
            {0.0843752475247525, 23.94059406, 10},
            {0.0884287128712871, 24.7009901, 10},
            {0.092609900990099, 25.47326733, 10},
            {0.0969207920792079, 26.25742574, 10},
            {0.101363366336634, 27.05346535, 10},
            {0.105939603960396, 27.86138614, 10},
            {0.110651485148515, 28.68118812, 10},
            {0.11550099009901, 29.51287129, 10},
            {0.120490099009901, 30.35643564, 10},
            {0.125620792079208, 31.21188119, 10},
            {0.130895049504951, 32.07920792, 10},
            {0.136314851485149, 32.95841584, 10},
            {0.141882178217822, 33.84950495, 10},
            {0.14759900990099, 34.75247525, 10},
            {0.153467326732673, 35.66732673, 10},
            {0.159489108910891, 36.59405941, 10},
            {0.165666336633663, 37.53267327, 10},
            {0.17200099009901, 38.48316832, 10},
            {0.178495049504951, 39.44554455, 10},
            {0.185150495049505, 40.41980198, 10},
            {0.191969306930693, 41.40594059, 10},
            {0.198953465346535, 42.4039604, 10},
            {0.20610495049505, 43.41386139, 10},
            {0.213425742574257, 44.43564356, 10},
            {0.220917821782178, 45.46930693, 10},
            {0.228583168316832, 46.51485149, 10},
            {0.236423762376238, 47.57227723, 10},
            {0.244441584158416, 48.64158416, 10},
            {0.252638613861386, 49.72277228, 10},
            {0.261016831683168, 50.81584158, 10},
            {0.269578217821782, 51.92079208, 10},
            {0.278324752475248, 53.03762376, 10},
            {0.287258415841584, 54.16633663, 10},
            {0.296381188118812, 55.30693069, 10},
            {0.305695049504951, 56.45940594, 10},
            {0.31520198019802, 57.62376238, 10},
            {0.32490396039604, 58.8, 10},
            {0.33480297029703, 59.98811881, 10},
            {0.34490099009901, 61.18811881, 10},
            {0.35519900990099, 62.38811881, 10},
            {0.36569702970297, 63.58811881, 10},
            {0.376395049504951, 64.78811881, 10},
            {0.387293069306931, 65.98811881, 10},
            {0.398391089108911, 67.18811881, 10},
            {0.409689108910891, 68.38811881, 10},
            {0.421187128712871, 69.58811881, 10},
            {0.432885148514852, 70.78811881, 10},
            {0.444783168316832, 71.98811881, 10},
            {0.456881188118812, 73.18811881, 10},
            {0.469179207920792, 74.38811881, 10},
            {0.481677227722772, 75.58811881, 10},
            {0.494375247524753, 76.78811881, 10},
            {0.507273267326733, 77.98811881, 10},
            {0.520371287128713, 79.18811881, 10},
            {0.533669306930693, 80.38811881, 10},
            {0.547167326732673, 81.58811881, 10},
            {0.560865346534653, 82.78811881, 10},
            {0.574763366336634, 83.98811881, 10},
            {0.588861386138614, 85.18811881, 10},
            {0.603159405940594, 86.38811881, 10},
            {0.617657425742574, 87.58811881, 10},
            {0.632355445544554, 88.78811881, 10},
            {0.647253465346535, 89.98811881, 10},
            {0.662351485148515, 91.18811881, 10},
            {0.677649504950495, 92.38811881, 10},
            {0.693147524752475, 93.58811881, 10},
            {0.708845544554455, 94.78811881, 10},
            {0.724743564356436, 95.98811881, 10},
            {0.740841584158416, 97.18811881, 10},
            {0.757139603960396, 98.38811881, 10},
            {0.773637623762376, 99.58811881, 10},
            {0.790335643564356, 100.7881188, 10},
            {0.807233663366337, 101.9881188, 10},
            {0.824331683168317, 103.1881188, 10},
            {0.841629702970297, 104.3881188, 10},
            {0.859127722772277, 105.5881188, 10},
            {0.876825742574257, 106.7881188, 10},
            {0.894723762376238, 107.9881188, 10},
            {0.912821782178218, 109.1881188, 10},
            {0.931119801980198, 110.3881188, 10},
            {0.949617821782178, 111.5881188, 10},
            {0.968315841584159, 112.7881188, 10},
            {0.987213861386139, 113.9881188, 10},
            {1.00631188118812, 115.1881188, 10},
            {1.0256099009901, 116.3881188, 10},
            {1.04510792079208, 117.5881188, 10},
            {1.06480594059406, 118.7881188, 10},
            {1.08470396039604, 119.9881188, 10},
            {1.10480198019802, 121.1881188, 10},
            {1.1251, 122.3881188, 10},
            {1.14559801980198, 123.5881188, 10},
            {1.16629603960396, 124.7881188, 10},
            {1.18719405940594, 125.9881188, 10},
            {1.20829207920792, 127.1881188, 10},
            {1.2295900990099, 128.3881188, 10},
            {1.25108811881188, 129.5881188, 10},
            {1.27278613861386, 130.7881188, 10},
            {1.29468415841584, 131.9881188, 10},
            {1.31678217821782, 133.1881188, 10},
            {1.3390801980198, 134.3881188, 10},
            {1.36157821782178, 135.5881188, 10},
            {1.38427623762376, 136.7881188, 10},
            {1.40717425742574, 137.9881188, 10},
            {1.43027227722772, 139.1881188, 10},
            {1.4535702970297, 140.3881188, 10},
            {1.47706831683168, 141.5881188, 10},
            {1.50076633663366, 142.7881188, 10},
            {1.52466435643564, 143.9881188, 10},
            {1.54876237623762, 145.1881188, 10},
            {1.5730603960396, 146.3881188, 10},
            {1.59755841584158, 147.5881188, 10},
            {1.62225643564357, 148.7881188, 10},
            {1.64715445544555, 149.9881188, 10},
            {1.67225247524753, 151.1881188, 10},
            {1.69755049504951, 152.3881188, 10},
            {1.72304851485149, 153.5881188, 10},
            {1.74874653465347, 154.7881188, 10},
            {1.77464455445545, 155.9881188, 10},
            {1.80074257425743, 157.1881188, 10},
            {1.82704059405941, 158.3881188, 10},
            {1.85353861386139, 159.5881188, 10},
            {1.88023663366337, 160.7881188, 10},
            {1.90713465346535, 161.9881188, 10},
            {1.93423267326733, 163.1881188, 10},
            {1.96153069306931, 164.3881188, 10},
            {1.98902871287129, 165.5881188, 10},
            {2.01672673267327, 166.7881188, 10},
            {2.04462475247525, 167.9881188, 10},
            {2.07272277227723, 169.1881188, 10},
            {2.10102079207921, 170.3881188, 10},
            {2.12951881188119, 171.5881188, 10},
            {2.15821683168317, 172.7881188, 10},
            {2.18711485148515, 173.9881188, 10},
            {2.21621287128713, 175.1881188, 10},
            {2.24551089108911, 176.3881188, 10},
            {2.27500891089109, 177.5881188, 10},
            {2.30470693069307, 178.7881188, 10},
            {2.33460495049505, 179.9881188, 10},
            {2.36470297029703, 181.1881188, 10},
            {2.39500099009901, 182.3881188, 10},
            {2.42549900990099, 183.5881188, 10},
            {2.45619702970297, 184.7881188, 10},
            {2.48709504950495, 185.9881188, 10},
            {2.51819306930693, 187.1881188, 10},
            {2.54949108910891, 188.3881188, 10},
            {2.58098910891089, 189.5881188, 10},
            {2.61268712871287, 190.7881188, 10},
            {2.64458514851485, 191.9881188, 10},
            {2.67668316831683, 193.1881188, 10},
            {2.70898118811881, 194.3881188, 10},
            {2.74147920792079, 195.5881188, 10},
            {2.77417722772277, 196.7881188, 10},
            {2.80707524752475, 197.9881188, 10},
            {2.84017326732673, 199.1881188, 10},
            {2.87347128712871, 200.3881188, 10},
            {2.90696930693069, 201.5881188, 10},
            {2.94066732673268, 202.7881188, 10},
            {2.97456534653466, 203.9881188, 10},
            {3.00866336633664, 205.1881188, 10},
            {3.04296138613862, 206.3881188, 10},
            {3.0774594059406, 207.5881188, 10},
            {3.11215742574258, 208.7881188, 10},
            {3.14705544554456, 209.9881188, 10},
            {3.18215346534654, 211.1881188, 10},
            {3.21745148514852, 212.3881188, 10},
            {3.2529495049505, 213.5881188, 10},
            {3.28864752475248, 214.7881188, 10},
            {3.32454554455446, 215.9881188, 10},
            {3.36064356435644, 217.1881188, 10},
            {3.39694158415842, 218.3881188, 10},
            {3.4334396039604, 219.5881188, 10},
            {3.47013762376238, 220.7881188, 10},
            {3.50703564356436, 221.9881188, 10},
            {3.54413366336634, 223.1881188, 10},
            {3.58143168316832, 224.3881188, 10},
            {3.6189297029703, 225.5881188, 10},
            {3.65662772277228, 226.7881188, 10},
            {3.69452574257426, 227.9881188, 10},
            {3.73262376237624, 229.1881188, 10},
            {3.77092178217822, 230.3881188, 10},
            {3.8094198019802, 231.5881188, 10},
            {3.84811782178218, 232.7881188, 10},
            {3.88701584158416, 233.9881188, 10},
            {3.92611386138614, 235.1881188, 10},
            {3.96541188118812, 236.3881188, 10},
            {4.0049099009901, 237.5881188, 10},
            {4.04460792079208, 238.7881188, 10},
            {4.08450594059406, 239.9881188, 10},
            {4.12460396039604, 241.1881188, 10},
            {4.16490198019802, 242.3881188, 10},
            {4.2054, 243.5881188, 10},
            {4.24609801980198, 244.7881188, 10},
            {4.28699603960396, 245.9881188, 10},
            {4.32809405940594, 247.1881188, 10},
            {4.36939207920792, 248.3881188, 10},
            {4.4108900990099, 249.5881188, 10},
            {4.45258811881188, 250.7881188, 10},
            {4.49448613861386, 251.9881188, 10},
            {4.53658415841584, 253.1881188, 10},
            {4.57888217821782, 254.3881188, 10},
            {4.6213801980198, 255.5881188, 10},
            {4.66407821782178, 256.7881188, 10},
            {4.70697623762377, 257.9881188, 10},
            {4.75007425742575, 259.1881188, 10},
            {4.79337227722773, 260.3881188, 10},
            {4.83687029702971, 261.5881188, 10},
            {4.88056831683169, 262.7881188, 10},
            {4.92446633663367, 263.9881188, 10},
            {4.96856435643565, 265.1881188, 10},
            {5.01286237623763, 266.3881188, 10},
            {5.05736039603961, 267.5881188, 10},
            {5.10205841584159, 268.7881188, 10},
            {5.14695643564357, 269.9881188, 10},
            {5.19205445544555, 271.1881188, 10},
            {5.23735247524753, 272.3881188, 10},
            {5.28285049504951, 273.5881188, 10},
            {5.32854851485149, 274.7881188, 10},
            {5.37444653465347, 275.9881188, 10},
            {5.42054455445545, 277.1881188, 10},
            {5.46684257425743, 278.3881188, 10},
            {5.51334059405941, 279.5881188, 10},
            {5.56003861386139, 280.7881188, 10},
            {5.60693663366337, 281.9881188, 10},
            {5.65403465346535, 283.1881188, 10},
            {5.70133267326733, 284.3881188, 10},
            {5.74883069306931, 285.5881188, 10},
            {5.79652871287129, 286.7881188, 10},
            {5.84442673267327, 287.9881188, 10},
            {5.89252475247525, 289.1881188, 10},
            {5.94082277227723, 290.3881188, 10},
            {5.98932079207921, 291.5881188, 10},
            {6.03801881188119, 292.7881188, 10},
            {6.08691683168317, 293.9881188, 10},
            {6.13601485148515, 295.1881188, 10},
            {6.18531287128713, 296.3881188, 10},
            {6.23481089108911, 297.5881188, 10},
            {6.28450891089109, 298.7881188, 10},
            {6.33440693069307, 299.9881188, 10},
            {6.38450495049505, 301.1881188, 10},
            {6.43479900990099, 302.3405941, 10},
            {6.48528316831684, 303.4693069, 10},
            {6.53595346534654, 304.5742574, 10},
            {6.58680594059406, 305.6554455, 10},
            {6.63783663366337, 306.7128713, 10},
            {6.68904158415842, 307.7465347, 10},
            {6.74041683168317, 308.7564356, 10},
            {6.79195841584159, 309.7425743, 10},
            {6.84366237623763, 310.7049505, 10},
            {6.89552475247525, 311.6435644, 10},
            {6.94754158415842, 312.5584158, 10},
            {6.99970891089109, 313.449505, 10},
            {7.05202277227723, 314.3168317, 10},
            {7.1044792079208, 315.160396, 10},
            {7.15707425742575, 315.980198, 10},
            {7.20980396039604, 316.7762376, 10},
            {7.26266435643565, 317.5485149, 10},
            {7.31565148514852, 318.2970297, 10},
            {7.36876138613862, 319.0217822, 10},
            {7.42199009900991, 319.7227723, 10},
            {7.47533366336634, 320.4, 10},
            {7.52878811881189, 321.0534653, 10},
            {7.5823495049505, 321.6831683, 10},
            {7.63601386138614, 322.2891089, 10},
            {7.68977722772278, 322.8712871, 10},
            {7.74363564356436, 323.429703, 10},
            {7.79758514851486, 323.9643564, 10},
            {7.85162178217822, 324.4752475, 10},
            {7.90574158415842, 324.9623762, 10},
            {7.95994059405941, 325.4257426, 10},
            {8.01421485148515, 325.8653465, 10},
            {8.06856039603961, 326.2811881, 10},
            {8.12297326732674, 326.6732673, 10},
            {8.1774495049505, 327.0415842, 10},
            {8.23198514851486, 327.3861386, 10},
            {8.28657623762377, 327.7069307, 10},
            {8.34121881188119, 328.0039604, 10},
            {8.39590891089109, 328.2772277, 10},
            {8.45064257425743, 328.5267327, 10},
            {8.50541584158416, 328.7524752, 10},
            {8.56022475247525, 328.9544554, 10},
            {8.61506534653466, 329.1326733, 10},
            {8.66993366336634, 329.2871287, 10},
            {8.72482574257426, 329.4178218, 10},
            {8.77973762376238, 329.5247525, 10},
            {8.83466534653466, 329.6079208, 10},
            {8.88960495049505, 329.6673267, 10},
            {8.94455247524753, 329.7029703, 10},
            {8.99950396039604, 329.7148515, 10},
            {9.05445544554456, 329.7029703, 10},
            {9.10940297029703, 329.6673267, 10},
            {9.16434257425743, 329.6079208, 10},
            {9.2192702970297, 329.5247525, 10},
            {9.27418217821782, 329.4178218, 10},
            {9.32907425742574, 329.2871287, 10},
            {9.38394257425743, 329.1326733, 10},
            {9.43878316831683, 328.9544554, 10},
            {9.49359207920792, 328.7524752, 10},
            {9.54836534653465, 328.5267327, 10},
            {9.60309900990099, 328.2772277, 10},
            {9.65778910891089, 328.0039604, 10},
            {9.71243168316832, 327.7069307, 10},
            {9.76702277227723, 327.3861386, 10},
            {9.82155841584158, 327.0415842, 10},
            {9.87603465346535, 326.6732673, 10},
            {9.93044752475248, 326.2811881, 10},
            {9.98479306930693, 325.8653465, 10},
            {10.0390673267327, 325.4257426, 10},
            {10.0932663366337, 324.9623762, 10},
            {10.1473861386139, 324.4752475, 10},
            {10.2014227722772, 323.9643564, 10},
            {10.2553722772277, 323.429703, 10},
            {10.3092306930693, 322.8712871, 10},
            {10.3629940594059, 322.2891089, 10},
            {10.4166584158416, 321.6831683, 10},
            {10.4702198019802, 321.0534653, 10},
            {10.5236742574257, 320.4, 10},
            {10.5770178217822, 319.7227723, 10},
            {10.6302465346535, 319.0217822, 10},
            {10.6833564356436, 318.2970297, 10},
            {10.7363435643564, 317.5485149, 10},
            {10.789203960396, 316.7762376, 10},
            {10.8419336633663, 315.980198, 10},
            {10.8945287128713, 315.160396, 10},
            {10.9469851485149, 314.3168317, 10},
            {10.999299009901, 313.449505, 10},
            {11.0514663366337, 312.5584158, 10},
            {11.1034831683168, 311.6435644, 10},
            {11.1553455445545, 310.7049505, 10},
            {11.2070495049505, 309.7425743, 10},
            {11.2585910891089, 308.7564356, 10},
            {11.3099663366337, 307.7465347, 10},
            {11.3611712871287, 306.7128713, 10},
            {11.412201980198, 305.6554455, 10},
            {11.4630544554455, 304.5742574, 10},
            {11.5137247524753, 303.4693069, 10},
            {11.5642089108911, 302.3405941, 10},
            {11.614502970297, 301.1881188, 10},
            {11.664602970297, 300.0118812, 10},
            {11.7145049504951, 298.8118812, 10},
            {11.7642069306931, 297.6118812, 10},
            {11.8137089108911, 296.4118812, 10},
            {11.8630108910891, 295.2118812, 10},
            {11.9121128712871, 294.0118812, 10},
            {11.9610148514852, 292.8118812, 10},
            {12.0097168316832, 291.6118812, 10},
            {12.0582188118812, 290.4118812, 10},
            {12.1065207920792, 289.2118812, 10},
            {12.1546227722772, 288.0118812, 10},
            {12.2025247524753, 286.8118812, 10},
            {12.2502267326733, 285.6118812, 10},
            {12.2977287128713, 284.4118812, 10},
            {12.3450306930693, 283.2118812, 10},
            {12.3921326732673, 282.0118812, 10},
            {12.4390346534654, 280.8118812, 10},
            {12.4857366336634, 279.6118812, 10},
            {12.5322386138614, 278.4118812, 10},
            {12.5785405940594, 277.2118812, 10},
            {12.6246425742574, 276.0118812, 10},
            {12.6705445544555, 274.8118812, 10},
            {12.7162465346535, 273.6118812, 10},
            {12.7617485148515, 272.4118812, 10},
            {12.8070504950495, 271.2118812, 10},
            {12.8521524752475, 270.0118812, 10},
            {12.8970544554456, 268.8118812, 10},
            {12.9417564356436, 267.6118812, 10},
            {12.9862584158416, 266.4118812, 10},
            {13.0305603960396, 265.2118812, 10},
            {13.0746623762376, 264.0118812, 10},
            {13.1185643564356, 262.8118812, 10},
            {13.1622663366337, 261.6118812, 10},
            {13.2057683168317, 260.4118812, 10},
            {13.2490702970297, 259.2118812, 10},
            {13.2921722772277, 258.0118812, 10},
            {13.3350742574257, 256.8118812, 10},
            {13.3777762376238, 255.6118812, 10},
            {13.4202782178218, 254.4118812, 10},
            {13.4625801980198, 253.2118812, 10},
            {13.5046821782178, 252.0118812, 10},
            {13.5465841584158, 250.8118812, 10},
            {13.5882861386139, 249.6118812, 10},
            {13.6297881188119, 248.4118812, 10},
            {13.6710900990099, 247.2118812, 10},
            {13.7121920792079, 246.0118812, 10},
            {13.7530940594059, 244.8118812, 10},
            {13.793796039604, 243.6118812, 10},
            {13.834298019802, 242.4118812, 10},
            {13.8746, 241.2118812, 10},
            {13.914701980198, 240.0118812, 10},
            {13.954603960396, 238.8118812, 10},
            {13.9943059405941, 237.6118812, 10},
            {14.0338079207921, 236.4118812, 10},
            {14.0731099009901, 235.2118812, 10},
            {14.1122118811881, 234.0118812, 10},
            {14.1511138613861, 232.8118812, 10},
            {14.1898158415842, 231.6118812, 10},
            {14.2283178217822, 230.4118812, 10},
            {14.2666198019802, 229.2118812, 10},
            {14.3047217821782, 228.0118812, 10},
            {14.3426237623762, 226.8118812, 10},
            {14.3803257425743, 225.6118812, 10},
            {14.4178277227723, 224.4118812, 10},
            {14.4551297029703, 223.2118812, 10},
            {14.4922316831683, 222.0118812, 10},
            {14.5291336633663, 220.8118812, 10},
            {14.5658356435644, 219.6118812, 10},
            {14.6023376237624, 218.4118812, 10},
            {14.6386396039604, 217.2118812, 10},
            {14.6747415841584, 216.0118812, 10},
            {14.7106435643564, 214.8118812, 10},
            {14.7463455445545, 213.6118812, 10},
            {14.7818475247525, 212.4118812, 10},
            {14.8171495049505, 211.2118812, 10},
            {14.8522514851485, 210.0118812, 10},
            {14.8871534653465, 208.8118812, 10},
            {14.9218554455446, 207.6118812, 10},
            {14.9563574257426, 206.4118812, 10},
            {14.9906594059406, 205.2118812, 10},
            {15.0247613861386, 204.0118812, 10},
            {15.0586633663366, 202.8118812, 10},
            {15.0923653465347, 201.6118812, 10},
            {15.1258673267327, 200.4118812, 10},
            {15.1591693069307, 199.2118812, 10},
            {15.1922712871287, 198.0118812, 10},
            {15.2251732673267, 196.8118812, 10},
            {15.2578752475248, 195.6118812, 10},
            {15.2903772277228, 194.4118812, 10},
            {15.3226792079208, 193.2118812, 10},
            {15.3547811881188, 192.0118812, 10},
            {15.3866831683168, 190.8118812, 10},
            {15.4183851485149, 189.6118812, 10},
            {15.4498871287129, 188.4118812, 10},
            {15.4811891089109, 187.2118812, 10},
            {15.5122910891089, 186.0118812, 10},
            {15.5431930693069, 184.8118812, 10},
            {15.573895049505, 183.6118812, 10},
            {15.604397029703, 182.4118812, 10},
            {15.634699009901, 181.2118812, 10},
            {15.664800990099, 180.0118812, 10},
            {15.694702970297, 178.8118812, 10},
            {15.7244049504951, 177.6118812, 10},
            {15.7539069306931, 176.4118812, 10},
            {15.7832089108911, 175.2118812, 10},
            {15.8123108910891, 174.0118812, 10},
            {15.8412128712871, 172.8118812, 10},
            {15.8699148514852, 171.6118812, 10},
            {15.8984168316832, 170.4118812, 10},
            {15.9267188118812, 169.2118812, 10},
            {15.9548207920792, 168.0118812, 10},
            {15.9827227722772, 166.8118812, 10},
            {16.0104247524753, 165.6118812, 10},
            {16.0379267326733, 164.4118812, 10},
            {16.0652287128713, 163.2118812, 10},
            {16.0923306930693, 162.0118812, 10},
            {16.1192326732673, 160.8118812, 10},
            {16.1459346534654, 159.6118812, 10},
            {16.1724366336634, 158.4118812, 10},
            {16.1987386138614, 157.2118812, 10},
            {16.2248405940594, 156.0118812, 10},
            {16.2507425742574, 154.8118812, 10},
            {16.2764445544555, 153.6118812, 10},
            {16.3019465346535, 152.4118812, 10},
            {16.3272485148515, 151.2118812, 10},
            {16.3523504950495, 150.0118812, 10},
            {16.3772524752475, 148.8118812, 10},
            {16.4019544554456, 147.6118812, 10},
            {16.4264564356436, 146.4118812, 10},
            {16.4507584158416, 145.2118812, 10},
            {16.4748603960396, 144.0118812, 10},
            {16.4987623762376, 142.8118812, 10},
            {16.5224643564357, 141.6118812, 10},
            {16.5459663366337, 140.4118812, 10},
            {16.5692683168317, 139.2118812, 10},
            {16.5923702970297, 138.0118812, 10},
            {16.6152722772277, 136.8118812, 10},
            {16.6379742574258, 135.6118812, 10},
            {16.6604762376238, 134.4118812, 10},
            {16.6827782178218, 133.2118812, 10},
            {16.7048801980198, 132.0118812, 10},
            {16.7267821782178, 130.8118812, 10},
            {16.7484841584159, 129.6118812, 10},
            {16.7699861386139, 128.4118812, 10},
            {16.7912881188119, 127.2118812, 10},
            {16.8123900990099, 126.0118812, 10},
            {16.8332920792079, 124.8118812, 10},
            {16.8539940594059, 123.6118812, 10},
            {16.874496039604, 122.4118812, 10},
            {16.894798019802, 121.2118812, 10},
            {16.9149, 120.0118812, 10},
            {16.934801980198, 118.8118812, 10},
            {16.954503960396, 117.6118812, 10},
            {16.9740059405941, 116.4118812, 10},
            {16.9933079207921, 115.2118812, 10},
            {17.0124099009901, 114.0118812, 10},
            {17.0313118811881, 112.8118812, 10},
            {17.0500138613861, 111.6118812, 10},
            {17.0685158415842, 110.4118812, 10},
            {17.0868178217822, 109.2118812, 10},
            {17.1049198019802, 108.0118812, 10},
            {17.1228217821782, 106.8118812, 10},
            {17.1405237623762, 105.6118812, 10},
            {17.1580257425743, 104.4118812, 10},
            {17.1753277227723, 103.2118812, 10},
            {17.1924297029703, 102.0118812, 10},
            {17.2093316831683, 100.8118812, 10},
            {17.2260336633663, 99.61188119, 10},
            {17.2425356435644, 98.41188119, 10},
            {17.2588376237624, 97.21188119, 10},
            {17.2749396039604, 96.01188119, 10},
            {17.2908415841584, 94.81188119, 10},
            {17.3065435643564, 93.61188119, 10},
            {17.3220455445545, 92.41188119, 10},
            {17.3373475247525, 91.21188119, 10},
            {17.3524495049505, 90.01188119, 10},
            {17.3673514851485, 88.81188119, 10},
            {17.3820534653465, 87.61188119, 10},
            {17.3965554455446, 86.41188119, 10},
            {17.4108574257426, 85.21188119, 10},
            {17.4249594059406, 84.01188119, 10},
            {17.4388613861386, 82.81188119, 10},
            {17.4525633663366, 81.61188119, 10},
            {17.4660653465347, 80.41188119, 10},
            {17.4793673267327, 79.21188119, 10},
            {17.4924693069307, 78.01188119, 10},
            {17.5053712871287, 76.81188119, 10},
            {17.5180732673267, 75.61188119, 10},
            {17.5305752475248, 74.41188119, 10},
            {17.5428772277228, 73.21188119, 10},
            {17.5549792079208, 72.01188119, 10},
            {17.5668811881188, 70.81188119, 10},
            {17.5785831683168, 69.61188119, 10},
            {17.5900851485149, 68.41188119, 10},
            {17.6013871287129, 67.21188119, 10},
            {17.6124891089109, 66.01188119, 10},
            {17.6233910891089, 64.81188119, 10},
            {17.6340930693069, 63.61188119, 10},
            {17.644595049505, 62.41188119, 10},
            {17.654897029703, 61.21188119, 10},
            {17.664999009901, 60.01188119, 10},
            {17.674900990099, 58.81188119, 10},
            {17.6846049504951, 57.63564356, 10},
            {17.6941138613861, 56.47128713, 10},
            {17.7034297029703, 55.31881188, 10},
            {17.7125544554456, 54.17821782, 10},
            {17.7214900990099, 53.04950495, 10},
            {17.7302386138614, 51.93267327, 10},
            {17.738801980198, 50.82772277, 10},
            {17.7471821782178, 49.73465347, 10},
            {17.7553811881188, 48.65346535, 10},
            {17.763400990099, 47.58415842, 10},
            {17.7712435643564, 46.52673267, 10},
            {17.7789108910891, 45.48118812, 10},
            {17.7864049504951, 44.44752475, 10},
            {17.7937277227723, 43.42574257, 10},
            {17.8008811881188, 42.41584158, 10},
            {17.8078673267327, 41.41782178, 10},
            {17.8146881188119, 40.43168317, 10},
            {17.8213455445545, 39.45742574, 10},
            {17.8278415841584, 38.4950495, 10},
            {17.8341782178218, 37.54455446, 10},
            {17.8403574257426, 36.60594059, 10},
            {17.8463811881188, 35.67920792, 10},
            {17.8522514851485, 34.76435644, 10},
            {17.8579702970297, 33.86138614, 10},
            {17.8635396039604, 32.97029703, 10},
            {17.8689613861386, 32.09108911, 10},
            {17.8742376237624, 31.22376238, 10},
            {17.8793702970297, 30.36831683, 10},
            {17.8843613861386, 29.52475248, 10},
            {17.8892128712871, 28.69306931, 10},
            {17.8939267326733, 27.87326733, 10},
            {17.898504950495, 27.06534653, 10},
            {17.9029495049505, 26.26930693, 10},
            {17.9072623762376, 25.48514851, 10},
            {17.9114455445545, 24.71287129, 10},
            {17.915500990099, 23.95247525, 10},
            {17.9194306930693, 23.2039604, 10},
            {17.9232366336634, 22.46732673, 10},
            {17.9269207920792, 21.74257426, 10},
            {17.9304851485149, 21.02970297, 10},
            {17.9339316831683, 20.32871287, 10},
            {17.9372623762376, 19.63960396, 10},
            {17.9404792079208, 18.96237624, 10},
            {17.9435841584158, 18.2970297, 10},
            {17.9465792079208, 17.64356436, 10},
            {17.9494663366337, 17.0019802, 10},
            {17.9522475247525, 16.37227723, 10},
            {17.9549247524753, 15.75445545, 10},
            {17.9575, 15.14851485, 10},
            {17.9599752475248, 14.55445545, 10},
            {17.9623524752475, 13.97227723, 10},
            {17.9646336633663, 13.4019802, 10},
            {17.9668207920792, 12.84356436, 10},
            {17.9689158415842, 12.2970297, 10},
            {17.9709207920792, 11.76237624, 10},
            {17.9728376237624, 11.23960396, 10},
            {17.9746683168317, 10.72871287, 10},
            {17.9764148514852, 10.22970297, 10},
            {17.9780792079208, 9.742574257, 10},
            {17.9796633663366, 9.267326733, 10},
            {17.9811693069307, 8.803960396, 10},
            {17.982599009901, 8.352475248, 10},
            {17.9839544554456, 7.912871287, 10},
            {17.9852376237624, 7.485148515, 10},
            {17.9864504950495, 7.069306931, 10},
            {17.987595049505, 6.665346535, 10},
            {17.9886732673267, 6.273267327, 10},
            {17.9896871287129, 5.893069307, 10},
            {17.9906386138614, 5.524752475, 10},
            {17.9915297029703, 5.168316832, 10},
            {17.9923623762376, 4.823762376, 10},
            {17.9931386138614, 4.491089109, 10},
            {17.9938603960396, 4.17029703, 10},
            {17.9945297029703, 3.861386139, 10},
            {17.9951485148515, 3.564356436, 10},
            {17.9957188118812, 3.279207921, 10},
            {17.9962425742574, 3.005940594, 10},
            {17.9967217821782, 2.744554455, 10},
            {17.9971584158416, 2.495049505, 10},
            {17.9975544554456, 2.257425743, 10},
            {17.9979118811881, 2.031683168, 10},
            {17.9982326732673, 1.817821782, 10},
            {17.9985188118812, 1.615841584, 10},
            {17.9987722772277, 1.425742574, 10},
            {17.998995049505, 1.247524752, 10},
            {17.9991891089109, 1.081188119, 10},
            {17.9993564356436, 0.926732673, 10},
            {17.999499009901, 0.784158416, 10},
            {17.9996188118812, 0.653465347, 10},
            {17.9997178217822, 0.534653465, 10},
            {17.999798019802, 0.427722772, 10},
            {17.9998613861386, 0.332673267, 10},
            {17.9999099009901, 0.24950495, 10},
            {17.9999455445545, 0.178217822, 10},
            {17.9999702970297, 0.118811881, 10},
            {17.9999861386139, 0.071287129, 10},
            {17.999995049505, 0.035643564, 10},
            {17.999999009901, 0.011881188, 10},
            {18, 0, 10}
    };


}