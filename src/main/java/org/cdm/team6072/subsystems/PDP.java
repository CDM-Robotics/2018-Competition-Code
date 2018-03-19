package org.cdm.team6072.subsystems;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Subsystem;


/**
 * Implement the PDP subsystem so we can report to shuffleboard
 */
public class PDP extends Subsystem {


    PowerDistributionPanel mPDP = new PowerDistributionPanel();



    private static PDP mInstance;
    public static PDP getInstance() {
        if (mInstance == null) {
            mInstance = new PDP();
        }
        return mInstance;
    }


    @Override
    protected void initDefaultCommand() {

    }


//    public double getTotalCurrent() {
//        return mPDP.getTotalCurrent();
//    }


}
