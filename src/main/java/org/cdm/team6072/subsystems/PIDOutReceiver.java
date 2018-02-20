package org.cdm.team6072.subsystems;

import edu.wpi.first.wpilibj.PIDOutput;


/**
 * Class used to pass to PIDController to receive the controller output
 */
public class PIDOutReceiver implements PIDOutput {


    private double mPIDOutput = 0.0;


    public void reset() {
        mPIDOutput = 0.0;
    }



    // this method is called by the PIDController to pass the current value
    @Override
    public void pidWrite(double output) {
        mPIDOutput = output;
    }



    public double getVal() {
        return mPIDOutput;
    }


}
