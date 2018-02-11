package org.cdm.team6072.profiles.drive;

import org.cdm.team6072.profiles.Constants;
import org.cdm.team6072.profiles.IMotionProfile;
import org.cdm.team6072.profiles.PIDConfig;


public class DrivetrainProfile implements IMotionProfile {



    // kF is feed forward correction, For AnyMarkCIM, has 80 units per rev.
    // Measured max for elevator motor was 820
    // So  kF = 1023 / 820 = 1.24756
    private static PIDConfig mPIDConfig = new PIDConfig(0, 1.24756, 2.0, 0, 0);


    @Override
    public PIDConfig getPIDConfig() {
        return mPIDConfig;
    }


    private int mBaseDurationMs = 10;

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

    private static DrivetrainProfile mInstance;
    public static DrivetrainProfile getInstance() {
        if (mInstance == null) {
            mInstance = new DrivetrainProfile();
        }
        return mInstance;
    }

    private DrivetrainProfile() {

    }



    // Position (rotations)	Velocity (RPM)	Duration (ms)
    public static double [][] mPoints = new double[][]{
            {0,	0	,10},
            {0.0000476190476190476,	0.5714285714	,10},
            {0.000214285714285714,	1.428571429	,10},
            {0.000547619047619048,	2.571428571	,10},
            {0.0010952380952381,	4	,10},
            {0.0019047619047619,	5.714285714	,10},
            {0.00302380952380952,	7.714285714	,10},
            {0.0045,	10	,10},
            {0.00638095238095238,	12.57142857	,10},
            {0.00871428571428571,	15.42857143	,10},
            {0.011547619047619,	18.57142857	,10},
            {0.0149285714285714,	22	,10},
            {0.0189047619047619,	25.71428571	,10},
            {0.0235238095238095,	29.71428571	,10},
            {0.0288333333333333,	34	,10},
            {0.0348809523809524,	38.57142857	,10},
            {0.0417142857142857,	43.42857143	,10},
            {0.0493809523809524,	48.57142857	,10},
            {0.0579285714285714,	54	,10},
            {0.0674047619047619,	59.71428571	,10},
            {0.0778571428571429,	65.71428571	,10},
            {0.0893095238095238,	71.71428571	,10},
            {0.101761904761905,	77.71428571	,10},
            {0.115214285714286,	83.71428571	,10},
            {0.129666666666667,	89.71428571	,10},
            {0.145119047619048,	95.71428571	,10},
            {0.161571428571429,	101.7142857	,10},
            {0.17902380952381,	107.7142857	,10},
            {0.19747619047619,	113.7142857	,10},
            {0.216928571428571,	119.7142857	,10},
            {0.237380952380952,	125.7142857	,10},
            {0.258833333333333,	131.7142857	,10},
            {0.281285714285714,	137.7142857	,10},
            {0.304738095238095,	143.7142857	,10},
            {0.329190476190476,	149.7142857	,10},
            {0.354642857142857,	155.7142857	,10},
            {0.381095238095238,	161.7142857	,10},
            {0.408547619047619,	167.7142857	,10},
            {0.437,	173.7142857	,10},
            {0.466452380952381,	179.7142857	,10},
            {0.496904761904762,	185.7142857	,10},
            {0.528309523809524,	191.1428571	,10},
            {0.560595238095238,	196.2857143	,10},
            {0.593714285714286,	201.1428571	,10},
            {0.627619047619048,	205.7142857	,10},
            {0.662261904761905,	210	,10},
            {0.697595238095238,	214	,10},
            {0.733571428571429,	217.7142857	,10},
            {0.770142857142857,	221.1428571	,10},
            {0.807261904761905,	224.2857143	,10},
            {0.844880952380952,	227.1428571	,10},
            {0.882904761904762,	229.1428571	,10},
            {0.921214285714286,	230.5714286	,10},
            {0.959714285714286,	231.4285714	,10},
            {0.998309523809524,	231.7142857	,10},
            {1.03690476190476,	231.4285714	,10},
            {1.07540476190476,	230.5714286	,10},
            {1.11371428571429,	229.1428571	,10},
            {1.1517380952381,	227.1428571	,10},
            {1.18938095238095,	224.5714286	,10},
            {1.22654761904762,	221.4285714	,10},
            {1.26316666666667,	218	,10},
            {1.29919047619048,	214.2857143	,10},
            {1.33457142857143,	210.2857143	,10},
            {1.3692619047619,	206	,10},
            {1.40321428571429,	201.4285714	,10},
            {1.43638095238095,	196.5714286	,10},
            {1.46871428571429,	191.4285714	,10},
            {1.50016666666667,	186	,10},
            {1.53069047619048,	180.2857143	,10},
            {1.5602380952381,	174.2857143	,10},
            {1.58878571428571,	168.2857143	,10},
            {1.61633333333333,	162.2857143	,10},
            {1.64288095238095,	156.2857143	,10},
            {1.66842857142857,	150.2857143	,10},
            {1.69297619047619,	144.2857143	,10},
            {1.71652380952381,	138.2857143	,10},
            {1.73907142857143,	132.2857143	,10},
            {1.76061904761905,	126.2857143	,10},
            {1.78116666666667,	120.2857143	,10},
            {1.80071428571428,	114.2857143	,10},
            {1.8192619047619,	108.2857143	,10},
            {1.83680952380952,	102.2857143	,10},
            {1.85335714285714,	96.28571429	,10},
            {1.86890476190476,	90.28571429	,10},
            {1.88345238095238,	84.28571429	,10},
            {1.897,	78.28571429	,10},
            {1.90954761904762,	72.28571429	,10},
            {1.92109523809524,	66.28571429	,10},
            {1.93164285714286,	60.28571429	,10},
            {1.94119047619047,	54.28571429	,10},
            {1.94978571428571,	48.85714286	,10},
            {1.9575,	43.71428571	,10},
            {1.96438095238095,	38.85714286	,10},
            {1.97047619047619,	34.28571429	,10},
            {1.97583333333333,	30	,10},
            {1.9805,	26	,10},
            {1.98452380952381,	22.28571429	,10},
            {1.98795238095238,	18.85714286	,10},
            {1.99083333333333,	15.71428571	,10},
            {1.99321428571428,	12.85714286	,10},
            {1.99514285714286,	10.28571429	,10},
            {1.99666666666666,	8	,10},
            {1.99783333333333,	6	,10},
            {1.99869047619047,	4.285714286	,10},
            {1.99928571428571,	2.857142857	,10},
            {1.99966666666667,	1.714285714	,10},
            {1.99988095238095,	0.8571428571	,10},
            {1.99997619047619,	0.2857142857	,10},
            {2.,	0	,10}
};


}
