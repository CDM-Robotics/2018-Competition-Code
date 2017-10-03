package frc;

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
    public static int LEFT_MOTOR_1 = 1;
    public static int LEFT_MOTOR_2 = 2;
    public static int LEFT_MOTOR_3 = 3;

    // front, mid, and back motors on chasis
    public static int RIGHT_MOTOR_1 = 3;
    public static int RIGHT_MOTOR_2 = 4;
    public static int RIGHT_MOTOR_3 = 5;

    // shifter (gearbox)
    public static int GEARBOX_MODULE_NUMBER = 1;
    public static int GEARBOX_FORWARD_CHANNEL = 0;
    public static int GEARBOX_REVERSE_CHANNEL = 0;
    public static int LOW_GEAR = 0;
    public static int HIGH_GEAR = 1;

    // GEAR Slider
    public static int SLIDER_TALON = 5;

    // encoders
    public static int ENCODER_LEFT_1 = 0;
    public static int ENCODER_LEFT_2 = 0;
    public static int ENCODER_RIGHT_1 = 0;
    public static int ENCODER_RIGHT_2 = 0;

    public static int CLIMBER_MOTOR = 0;

}
