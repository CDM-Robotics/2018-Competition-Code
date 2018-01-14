package org.cdm.team6072.autonomous;

public class MotionProfile {
    protected float dist; // distance in rotations
    protected float vMax; // max speed in rotations per second
    protected int t1; // value in milliseconds
    protected int t2; // values in milliseconds
    protected int itp; // value in milliseconds
    protected int t4; // values in milliseconds
    protected int fl1; // values in millliseconds
    protected int fl2; // values in milliseconds
    protected int n; // values in milliseconds

    private static MotionProfile mInstance;

    public MotionProfile getInstance() {
        if (mInstance == null) {
            mInstance = new MotionProfile();
        }
        return mInstance;
    }

    protected MotionProfile() {
    }
}
