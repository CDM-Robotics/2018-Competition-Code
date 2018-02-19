package org.cdm.team6072;

import edu.wpi.first.wpilibj.CounterBase;

/**
 * Created by Cole on 9/3/17.
 */
public class RobotConfig {


    // constants (units in inches)
    public static double DRIVE_WHEEL_DIAMETER = 6.0;



    // Drive Talons
    public static int DRIVE_LEFT_MASTER = 15;
    public static int DRIVE_LEFT_SLAVE0 = 14;

    public static int DRIVE_RIGHT_MASTER = 30;        // changed 2018-01-13
    public static int DRIVE_RIGHT_SLAVE0 = 1;

    // ELEVATOR
    public static int ELEVATOR_TALON = 12;

    public static int ELEVATOR_SWITCH_TOP = 1;
    public static int ELEVATOR_SWITCH_BOT = 0;

    // ARM
    public static int ARM_TALON = 13;

    public static int ARM_SWITCH_TOP = 3;
    public static int ARM_SWITCH_BOT = 2;
    


    // INTAKE
    public static int INTAKE_TALON_LEFT = 2;
    public static int INTAKE_TALON_RIGHT = 4;

    // PNEUMATICS
    public static int PCM_ID = 61;

    /**
     * There are two double solenoids controlled by the PCM
     *  solenoid 1 select open or close
     *          if open, set sol 2 off
     *
     *  solenoid 2 select close lo pressure
     *                  or close high pressure
     */
    public static int INTAKE_SOL_1_FWD_OPEN = 0;
    public static int INTAKE_SOL_1_REV_CLOSE = 1;
    public static int INTAKE_SOL_2_FWD_LO = 2;
    public static int INTAKE_SOL_2_REV_HI = 3;


}
