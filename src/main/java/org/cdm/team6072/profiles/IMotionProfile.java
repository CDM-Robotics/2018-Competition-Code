package org.cdm.team6072.profiles;


public interface IMotionProfile {


    public PIDConfig getPIDConfig();

    /**
     * @return base Trajectory Point duration of this profile in milliSeconds
     */
    public int getBaseTPDurationMs();

    public double[][] getPoints();

}
