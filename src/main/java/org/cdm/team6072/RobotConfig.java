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

    // front, mid, and back motors on chasis
    public static int LEFT_MASTER = 13;
    public static int LEFT_SLAVE0 = 14;
    public static int LEFT_SLAVE1 = 12;

    // front, mid, and back motors on chasis
    public static int RIGHT_MASTER = 30;        // changed 2018-01-13
    public static int RIGHT_SLAVE0 = 1;
    public static int RIGHT_SLAVE1 = 15;

    // shifter (gearbox)
    public static int GEARBOX_MODULE_NUMBER = 1;
    public static int GEARBOX_FORWARD_CHANNEL = 0;
    public static int GEARBOX_REVERSE_CHANNEL = 0;
    public static int LOW_GEAR = 0;
    public static int HIGH_GEAR = 1;

    // ELEVATOR
    public static int ELEVATOR_TALON = 2;


    // GRABBER
    public static int GRABBER_TALON_LEFT = 7;
    public static int GRABBER_TALON_RIGHT = 8;

    // CLIMBER
    public static int CLIMBER_MOTOR = 6;


    // constants (units in inches)
    public static double DRIVE_WHEEL_DIAMETER = 6.0;


    // PNEUMATICS
    public static int GRABBER_OPEN_SOLENOID_ON = 0;
    public static int GRABBER_OPEN_SOLENOID_OFF = 1;
    public static int GRABBER_CLOSE_LO_SOLENOID_ON = 2;
    public static int GRABBER_CLOSE_LO_SOLENOID_OFF = 3;
    public static int GRABBER_CLOSE_HI_SOLENOID_ON = 4;
    public static int GRABBER_CLOSE_HI_SOLENOID_OFF = 5;
    public static int COMPRESSOR = 0;

}
