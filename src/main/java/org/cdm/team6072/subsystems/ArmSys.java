package org.cdm.team6072.subsystems;


import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * ArmSys has a single talon used to move the arm through an arc of +- 80 degrees from horizontal
 *
 * ArmSys is attached to the ElevatorSys, and moves up and dwon with it.
 *
 * The IntakeMotorSys is attached to teh end of the ArmSys, and is used to hold the cubes.
 */
public class ArmSys extends Subsystem {




    private static ArmSys mInstance = null;
    public static ArmSys getInstance() {
        if (mInstance == null) {
            mInstance = new ArmSys();
        }
        return mInstance;
    }


    private ArmSys() {

    }


    @Override
    public void initDefaultCommand() {

    }


}
