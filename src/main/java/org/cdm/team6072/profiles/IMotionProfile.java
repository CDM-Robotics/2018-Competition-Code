package org.cdm.team6072.profiles;


public interface IMotionProfile {


    public PIDConfig getPIDConfig();

    /**
     * @return base Trajectory Point duration of this profile in milliSeconds
     */
    public int getBaseTPDurationMs();

    /**
     * @return the Points position is in rotations. This says how many units tper rotation the encoder will use
     */
    public double getUnitsPerRotation();

    public double[][] getPoints();

}
