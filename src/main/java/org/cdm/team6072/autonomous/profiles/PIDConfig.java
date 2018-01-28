package org.cdm.team6072.autonomous.profiles;


/**
 * Specify the setting for a Talon PID slot
 */
public class PIDConfig {

    // Talon slot to configure
    public int SlotId = -1;


    // Feed forward gain
    public double kF = -1;


    // Proportional gain
    public double kP = -1;


    // Integral gain
    public double kI = -1;


    // Differntial gain
    public double  kD = 1;




    public PIDConfig(int slotId, double kF, double kP, double kI, double kD) {
        this.SlotId = slotId;
        this.kP = kP;
        this.kI = kI;
        this.kF = kF;
        this.kD = kD;
    }


}
