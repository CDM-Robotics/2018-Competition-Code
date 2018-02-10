package org.cdm.team6072;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.cdm.team6072.commands.elevator.MoveElevatorCmd;
import org.cdm.team6072.commands.elevator.StopElevatorCmd;
import org.cdm.team6072.commands.elevator.RunMPCmd;
import org.cdm.team6072.commands.grabber.RunGrabberWheelsCmd;
import org.cdm.team6072.commands.grabber.StopGrabberWheelsCmd;
import org.cdm.team6072.subsystems.ElevatorSys;
import org.cdm.team6072.subsystems.IntakeMotorSys;
//import frc.subsystems.Climber;
//import org.cdm.team6072.subsystems.GearSlider;


/**
 * ControlBoard holds the code for interacting with the
 */
public class ControlBoard {


    // logitech gamepad buttons
    public static int LOGITECH_BUT_A = 1;
    public static int LOGITECH_BUT_B = 2;
    public static int LOGITECH_BUT_X = 3;
    public static int LOGITECH_BUT_Y = 4;
    public static int LOGITECH_BUT_LEFT = 5;
    public static int LOGITECH_BUT_RIGHT = 6;
    
    // extreme buttons
    // X-axis  -  forward and back
    // Y-axis  -  left and right
    // Z-axis  -  twist
    public static int EXTREME_BUT_THUMB = 1;
    public static int EXTREME_BUT_LEFT_TOP = 1;
    public static int EXTREME_BUT_LEFT_BOT = 2;
    public static int EXTREME_BUT_RIGHT_TOP = 3;
    public static int EXTREME_BUT_RIGHT_BOT = 4;
    public static int EXTREME_BUT_7 = 2;
    public static int EXTREME_BUT_8 = 3;
    public static int EXTREME_BUT_9 = 4;
    public static int EXTREME_BUT_10 = 2;
    public static int EXTREME_BUT_11 = 3;
    public static int EXTREME_BUT_12 = 4;


    // map commands to buttons

    public static int DRIVE_HALFSPEED = EXTREME_BUT_THUMB;

    // Elevator
    //      Move elevator up and down using X-axis
    //      Move to fixed positions using buttons on base - trigger MotionProfiles based on current position

    public static int ELEVATOR_MOVETO_BASE = EXTREME_BUT_7;
    public static int ELEVATOR_MOVETO_SCALE = EXTREME_BUT_8;
    public static int ELEVATOR_MOVETO_SWITCH_LO = EXTREME_BUT_9;
    public static int ELEVATOR_MOVETO_SWITCH_HI = EXTREME_BUT_10;



    // Intake
    //      Control by buttons on top of the
    public static int INTAKE_WHEELS_IN_BTN = EXTREME_BUT_RIGHT_TOP;
    public static int INTAKE_WHEELS_OUT_BTN = EXTREME_BUT_RIGHT_BOT;
    public static int INTAKE_OPEN_BTN = EXTREME_BUT_LEFT_TOP;
    public static int INTAKE_CLOSE_BTN = EXTREME_BUT_LEFT_BOT;
    
    


    public Joystick drive_stick;
    private JoystickButton[] drive_buttons;
    

    // put Logitech gamepad on 1
    public Joystick control_stick;                 // logitech  Extreme
    private JoystickButton[] control_buttons;      // logitech  Gamepad


    private static ControlBoard mInstance;
    public static ControlBoard getInstance() {
        if (mInstance == null) {
            mInstance = new ControlBoard();
        }
        return mInstance;
    }

    private ControlBoard () {

        // drive using usb0
        drive_stick = new Joystick(0); // joystick at usb port 0
        drive_buttons = new JoystickButton[12];
        drive_buttons[INTAKE_WHEELS_IN_BTN] = new JoystickButton(drive_stick, INTAKE_WHEELS_IN_BTN);
        drive_buttons[INTAKE_WHEELS_IN_BTN].whenPressed(new RunGrabberWheelsCmd(IntakeMotorSys.WheelDirn.In));
        drive_buttons[INTAKE_WHEELS_IN_BTN].whenReleased(new StopGrabberWheelsCmd());

        drive_buttons[INTAKE_WHEELS_OUT_BTN] = new JoystickButton(drive_stick, INTAKE_WHEELS_OUT_BTN);
        drive_buttons[INTAKE_WHEELS_OUT_BTN].whenPressed(new RunGrabberWheelsCmd(IntakeMotorSys.WheelDirn.Out));
        drive_buttons[INTAKE_WHEELS_OUT_BTN].whenReleased(new StopGrabberWheelsCmd());


        // control systems using usb1
        control_stick = new Joystick(1); // gamepad at usb port 1
        control_buttons = new JoystickButton[12];

        // map buttons and actions
        control_buttons[ELEVATOR_MOVETO_BASE] = new JoystickButton(control_stick, ELEVATOR_MOVETO_BASE);
        control_buttons[ELEVATOR_MOVETO_BASE].whenPressed(new MoveElevatorCmd(ElevatorSys.Direction.Up, 0.5));
        control_buttons[ELEVATOR_MOVETO_BASE].whenReleased(new StopElevatorCmd());

        control_buttons[ELEVATOR_MOVETO_SCALE] = new JoystickButton(control_stick, ELEVATOR_MOVETO_SCALE);
        control_buttons[ELEVATOR_MOVETO_SCALE].whenPressed(new MoveElevatorCmd(ElevatorSys.Direction.Down, 0.5));
        control_buttons[ELEVATOR_MOVETO_SCALE].whenReleased(new StopElevatorCmd());
        

//        control_buttons[ControlMappings.INTAKE_WHEELS_IN_BTN] = new JoystickButton(control_stick, ControlMappings.INTAKE_WHEELS_IN_BTN);
//        control_buttons[ControlMappings.INTAKE_WHEELS_IN_BTN].whenPressed(new RunGrabberWheelsCmd(IntakeMotorSys.WheelDirn.In));
//        control_buttons[ControlMappings.INTAKE_WHEELS_IN_BTN].whenReleased(new StopGrabberWheelsCmd());
//
//        control_buttons[ControlMappings.INTAKE_WHEELS_OUT_BTN] = new JoystickButton(control_stick, ControlMappings.INTAKE_WHEELS_OUT_BTN);
//        control_buttons[ControlMappings.INTAKE_WHEELS_OUT_BTN].whenPressed(new RunGrabberWheelsCmd(IntakeMotorSys.WheelDirn.Out));
//        control_buttons[ControlMappings.INTAKE_WHEELS_OUT_BTN].whenReleased(new StopGrabberWheelsCmd());
//
//        control_buttons[ControlMappings.INTAKE_OPEN_BTN] = new JoystickButton(control_stick, ControlMappings.INTAKE_OPEN_BTN);
//        control_buttons[ControlMappings.INTAKE_OPEN_BTN].whenPressed(new OpenGrabberCmd());
//
//        control_buttons[ControlMappings.INTAKE_CLOSE_BTN] = new JoystickButton(control_stick, ControlMappings.INTAKE_CLOSE_BTN);
//        control_buttons[ControlMappings.INTAKE_CLOSE_BTN].whenPressed(new CloseGrabberCmd());


    }


}
