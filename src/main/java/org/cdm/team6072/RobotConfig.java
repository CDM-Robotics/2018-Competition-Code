package org.cdm.team6072;

import edu.wpi.first.wpilibj.CounterBase;

/**
 * Created by Cole on 9/3/17.
 */
public class RobotConfig {


    /**
     *
     * DRIVE TRAIN CONFIGURATION
     *
     */

    // Drive Talons
    public static int DRIVE_LEFT_MASTER = 15;
    public static int DRIVE_LEFT_SLAVE0 = 14;

    public static int DRIVE_RIGHT_MASTER = 30;        // changed 2018-01-13
    public static int DRIVE_RIGHT_SLAVE0 = 1;

    // ELEVATOR
    public static int ELEVATOR_TALON = 12;

    public static int ELEVATOR_SWITCH_TOP = 1;
    public static int ELEVATOR_SWITCH_BOT = 2;


    // GRABBER
    public static int INTAKE_TALON_LEFT = 2;
    public static int INTAKE_TALON_RIGHT = 4;



    // constants (units in inches)
    public static double DRIVE_WHEEL_DIAMETER = 6.0;


    // PNEUMATICS

    public static int PMC_ID = 61;

    public static int INTAKE_OPEN_SOLENOID_ON = 0;
    public static int INTAKE_OPEN_SOLENOID_OFF = 1;
    public static int GRABBER_CLOSE_LO_SOLENOID_ON = 2;
    public static int GRABBER_CLOSE_LO_SOLENOID_OFF = 3;
    public static int GRABBER_CLOSE_HI_SOLENOID_ON = 4;
    public static int GRABBER_CLOSE_HI_SOLENOID_OFF = 5;
    public static int COMPRESSOR = 0;



}
