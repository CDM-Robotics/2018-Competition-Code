package org.cdm.team6072;

import edu.wpi.first.wpilibj.CounterBase;

/**
 * Created by Cole on 9/3/17.
 */
public class RobotConfig {


    // constants (units in inches)
    public static double DRIVE_WHEEL_DIAMETER = 6.0;



    // Drive Talons
    public static int DRIVE_LEFT_MASTER = 14;
    public static int DRIVE_LEFT_MASTER_PDP = 14;
    public static int DRIVE_LEFT_SLAVE0 = 15;
    public static int DRIVE_LEFT_SLAVE0_PDP = 15;

    public static int DRIVE_RIGHT_MASTER = 1;
    public static int DRIVE_RIGHT_MASTER_PDP = 1;
    public static int DRIVE_RIGHT_SLAVE0 = 30;
    public static int DRIVE_RIGHT_SLAVE0_PDP = 0;       // yes really is different from the CAN ID

    public static int DRIVE_GEAR_FWD_LO = 4;                // DIO for the gear shifting solenoid
    public static int DRIVE_GEAR_REV_HI = 5;

    // ELEVATOR
    public static int ELEVATOR_TALON = 12;
    public static int ELEVATOR_TALON_PDP = 12;

    public static int ELEVATOR_SWITCH_TOP = 1;
    public static int ELEVATOR_SWITCH_BOT = 0;

    // ARM
    public static int ARM_TALON = 13;
    public static int ARM_TALON_PDP = 13;

    public static int ARM_SWITCH_TOP = 3;
    public static int ARM_SWITCH_BOT = 2;
    


    // INTAKE
    public static int INTAKE_TALON_LEFT = 2;
    public static int INTAKE_TALON_LEFT_PDP = 2;
    public static int INTAKE_TALON_RIGHT = 4;
    public static int INTAKE_TALON_RIGHT_PDP = 4;

    // PNEUMATICS
    public static int PCM_ID = 61;

    // Power Distribution Panel - NOTE FRC says needs to be 0 for 2018 WPILib
    public static int PDP_ID = 0;

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
