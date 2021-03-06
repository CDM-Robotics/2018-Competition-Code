package org.cdm.team6072.profiles.test;

import org.cdm.team6072.profiles.Constants;
import org.cdm.team6072.profiles.PIDConfig;
import org.cdm.team6072.profiles.IMotionProfile;


public class Rot5_Vel1_Dur50 implements IMotionProfile {
			
/*			
	Vprog(max speed)	1	rotations/sec
	Dist	5	rotations
	T1	1000	ms
	T2	1000	ms
	itp	50	ms
	T4	5000	ms
	FL1	20	0
	FL2	20	0
	N	100	0
*/

    // kF is feed forward correction, For AnyMarkCIM, has 80 units per rev.		
    // Measured max for elevator motor was 820		
    // So  kF = 1023 / 820 = 1.24756		
    private static PIDConfig mPIDConfig = new PIDConfig(0, 1.24756, 0.0, 0, 0);


    @Override
    public PIDConfig getPIDConfig() {
        return mPIDConfig;
    }


    private int mBaseDurationMs = 50;

    @Override
    public int getBaseTPDurationMs() {
        return mBaseDurationMs;
    }

    @Override
    public double getUnitsPerRotation() {
        return Constants.kAndyMarkUnitsPerRotation;
    }


    @Override
    public double[][] getPoints() {
        return mPoints;
    }


    // singleton constructor     -------------------------

    private static Rot5_Vel1_Dur50 mInstance;

    public static Rot5_Vel1_Dur50 getInstance() {
        if (mInstance == null) {
            mInstance = new Rot5_Vel1_Dur50();
        }
        return mInstance;
    }

    private Rot5_Vel1_Dur50() {

    }

    // Position (rotations)	Velocity (RPM)	Duration (ms)

    public static double[][] mPoints = new double[][]{
            {0, 0, 50},
            {0.000119047619047619, 0.285714286, 50},
            {0.000535714285714286, 0.714285714, 50},
            {0.00136904761904762, 1.285714286, 50},
            {0.00273809523809524, 2, 50},
            {0.00476190476190476, 2.857142857, 50},
            {0.00755952380952381, 3.857142857, 50},
            {0.01125, 5, 50},
            {0.015952380952381, 6.285714286, 50},
            {0.0217857142857143, 7.714285714, 50},
            {0.0288690476190476, 9.285714286, 50},
            {0.0373214285714286, 11, 50},
            {0.0472619047619048, 12.85714286, 50},
            {0.0588095238095238, 14.85714286, 50},
            {0.0720833333333333, 17, 50},
            {0.087202380952381, 19.28571429, 50},
            {0.104285714285714, 21.71428571, 50},
            {0.123452380952381, 24.28571429, 50},
            {0.144821428571429, 27, 50},
            {0.168511904761905, 29.85714286, 50},
            {0.194642857142857, 32.85714286, 50},
            {0.223154761904762, 35.57142857, 50},
            {0.253869047619048, 38.14285714, 50},
            {0.286666666666667, 40.57142857, 50},
            {0.321428571428572, 42.85714286, 50},
            {0.358035714285714, 45, 50},
            {0.396369047619048, 47, 50},
            {0.436309523809524, 48.85714286, 50},
            {0.477738095238095, 50.57142857, 50},
            {0.520535714285714, 52.14285714, 50},
            {0.564583333333333, 53.57142857, 50},
            {0.609761904761905, 54.85714286, 50},
            {0.655952380952381, 56, 50},
            {0.703035714285714, 57, 50},
            {0.750892857142857, 57.85714286, 50},
            {0.799404761904762, 58.57142857, 50},
            {0.848452380952381, 59.14285714, 50},
            {0.897916666666667, 59.57142857, 50},
            {0.947678571428571, 59.85714286, 50},
            {0.997619047619048, 60, 50},
            {1.04761904761905, 60, 50},
            {1.09761904761905, 60, 50},
            {1.14761904761905, 60, 50},
            {1.19761904761905, 60, 50},
            {1.24761904761905, 60, 50},
            {1.29761904761905, 60, 50},
            {1.34761904761905, 60, 50},
            {1.39761904761905, 60, 50},
            {1.44761904761905, 60, 50},
            {1.49761904761905, 60, 50},
            {1.54761904761905, 60, 50},
            {1.59761904761905, 60, 50},
            {1.64761904761905, 60, 50},
            {1.69761904761905, 60, 50},
            {1.74761904761905, 60, 50},
            {1.79761904761905, 60, 50},
            {1.84761904761905, 60, 50},
            {1.89761904761905, 60, 50},
            {1.94761904761905, 60, 50},
            {1.99761904761905, 60, 50},
            {2.04761904761905, 60, 50},
            {2.09761904761905, 60, 50},
            {2.14761904761905, 60, 50},
            {2.19761904761905, 60, 50},
            {2.24761904761905, 60, 50},
            {2.29761904761905, 60, 50},
            {2.34761904761905, 60, 50},
            {2.39761904761905, 60, 50},
            {2.44761904761905, 60, 50},
            {2.49761904761905, 60, 50},
            {2.54761904761905, 60, 50},
            {2.59761904761905, 60, 50},
            {2.64761904761905, 60, 50},
            {2.69761904761905, 60, 50},
            {2.74761904761905, 60, 50},
            {2.79761904761905, 60, 50},
            {2.84761904761905, 60, 50},
            {2.89761904761905, 60, 50},
            {2.94761904761905, 60, 50},
            {2.99761904761904, 60, 50},
            {3.04761904761904, 60, 50},
            {3.09761904761904, 60, 50},
            {3.14761904761904, 60, 50},
            {3.19761904761904, 60, 50},
            {3.24761904761904, 60, 50},
            {3.29761904761904, 60, 50},
            {3.34761904761904, 60, 50},
            {3.39761904761904, 60, 50},
            {3.44761904761904, 60, 50},
            {3.49761904761904, 60, 50},
            {3.54761904761904, 60, 50},
            {3.59761904761904, 60, 50},
            {3.64761904761904, 60, 50},
            {3.69761904761904, 60, 50},
            {3.74761904761904, 60, 50},
            {3.79761904761904, 60, 50},
            {3.84761904761904, 60, 50},
            {3.89761904761904, 60, 50},
            {3.94761904761904, 60, 50},
            {3.99761904761904, 60, 50},
            {4.04761904761904, 60, 50},
            {4.09749999999999, 59.71428571, 50},
            {4.14708333333333, 59.28571429, 50},
            {4.19624999999999, 58.71428571, 50},
            {4.24488095238095, 58, 50},
            {4.29285714285714, 57.14285714, 50},
            {4.34005952380952, 56.14285714, 50},
            {4.38636904761904, 55, 50},
            {4.43166666666666, 53.71428571, 50},
            {4.47583333333333, 52.28571429, 50},
            {4.51874999999999, 50.71428571, 50},
            {4.56029761904761, 49, 50},
            {4.60035714285714, 47.14285714, 50},
            {4.63880952380952, 45.14285714, 50},
            {4.67553571428571, 43, 50},
            {4.71041666666666, 40.71428571, 50},
            {4.74333333333333, 38.28571429, 50},
            {4.77416666666666, 35.71428571, 50},
            {4.80279761904761, 33, 50},
            {4.82910714285714, 30.14285714, 50},
            {4.85297619047619, 27.14285714, 50},
            {4.87446428571428, 24.42857143, 50},
            {4.89374999999999, 21.85714286, 50},
            {4.91095238095238, 19.42857143, 50},
            {4.92619047619047, 17.14285714, 50},
            {4.93958333333333, 15, 50},
            {4.95124999999999, 13, 50},
            {4.96130952380952, 11.14285714, 50},
            {4.96988095238095, 9.428571429, 50},
            {4.97708333333333, 7.857142857, 50},
            {4.98303571428571, 6.428571429, 50},
            {4.98785714285714, 5.142857143, 50},
            {4.99166666666666, 4, 50},
            {4.99458333333333, 3, 50},
            {4.99672619047619, 2.142857143, 50},
            {4.99821428571428, 1.428571429, 50},
            {4.99916666666666, 0.857142857, 50},
            {4.99970238095238, 0.428571429, 50},
            {4.99994047619047, 0.142857143, 50},
            {4.99999999999999, 0, 50}
    };

}

