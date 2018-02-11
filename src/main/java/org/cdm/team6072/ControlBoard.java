package org.cdm.team6072;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.cdm.team6072.commands.drive.TestDriveForward;
import org.cdm.team6072.commands.drive.TestDriveGyro;
import org.cdm.team6072.commands.elevator.ElvMoveToBaseCmd;
import org.cdm.team6072.commands.elevator.ElvMoveToScaleCmd;
import org.cdm.team6072.commands.elevator.MoveElevatorCmd;
import org.cdm.team6072.commands.elevator.StopElevatorCmd;
import org.cdm.team6072.commands.intake.CloseIntakeCmd;
import org.cdm.team6072.commands.intake.RunIntakeWheelsCmd;
import org.cdm.team6072.commands.intake.StopIntakeWheelsCmd;
import org.cdm.team6072.commands.intake.OpenIntakeCmd;
import org.cdm.team6072.subsystems.ElevatorSys;
import org.cdm.team6072.subsystems.IntakeMotorSys;


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
    // Y-axis  -  forward and back
    // X-axis  -  left and right
    // Z-axis  -  twist
    // hub is POV?
    public static int EXTREME_BUT_TRIGGER = 1;
    public static int EXTREME_BUT_THUMB = 2;
    public static int EXTREME_BUT_LEFT_TOP = 5;
    public static int EXTREME_BUT_LEFT_BOT = 3;
    public static int EXTREME_BUT_RIGHT_TOP = 6;
    public static int EXTREME_BUT_RIGHT_BOT = 4;
    public static int EXTREME_BUT_7 = 7;
    public static int EXTREME_BUT_8 = 8;
    public static int EXTREME_BUT_9 = 9;
    public static int EXTREME_BUT_10 = 10;      // not working?
    public static int EXTREME_BUT_11 = 11;      // not working?
    public static int EXTREME_BUT_12 = 12;


    // map commands to buttons  ---------------------------------------------------------------------

    public static int DRIVE_HALFSPEED = EXTREME_BUT_THUMB;
    public static int DRIVE_TEST = EXTREME_BUT_LEFT_BOT;
    public static int DRIVE_TESTGYRO = EXTREME_BUT_RIGHT_BOT;

    // Elevator
    //      Move elevator up and down using X-axis
    //      Move to fixed positions using buttons on base - trigger MotionProfiles based on current position
    public static int ELEVATOR_MOVE_UP = EXTREME_BUT_LEFT_TOP; //EXTREME_BUT_12;
    public static int ELEVATOR_MOVE_DOWN = EXTREME_BUT_LEFT_BOT; //EXTREME_BUT_11;
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


    // drive stick is used for driving robot
    private static int DRIVE_USB_PORT = 0;
    public Joystick drive_stick;
    private JoystickButton[] drive_buttons;

    // control stick is used for elevator, intake
    private static int CONTROL_USB_PORT = 0;
    public Joystick control_stick;
    private JoystickButton[] control_buttons;


    private static ControlBoard mInstance;
    public static ControlBoard getInstance() {
        if (mInstance == null) {
            mInstance = new ControlBoard();
        }
        return mInstance;
    }

    private ControlBoard () {
//        drive_stick = new Joystick(DRIVE_USB_PORT);
//        drive_buttons = new JoystickButton[12];
//
//        drive_buttons[DRIVE_TEST-1] = new JoystickButton(drive_stick, DRIVE_TEST);
//        drive_buttons[DRIVE_TEST-1].whenPressed(new TestDriveForward());
//        drive_buttons[DRIVE_TESTGYRO-1] = new JoystickButton(drive_stick, DRIVE_TESTGYRO);
//        drive_buttons[DRIVE_TESTGYRO-1].whenPressed(new TestDriveGyro());


//        drive_buttons[INTAKE_WHEELS_IN_BTN-1] = new JoystickButton(drive_stick, INTAKE_WHEELS_IN_BTN);
//        drive_buttons[INTAKE_WHEELS_IN_BTN-1].whenPressed(new RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.In));
//        drive_buttons[INTAKE_WHEELS_IN_BTN-1].whenReleased(new StopIntakeWheelsCmd());
//
//        drive_buttons[INTAKE_WHEELS_OUT_BTN-1] = new JoystickButton(drive_stick, INTAKE_WHEELS_OUT_BTN);
//        drive_buttons[INTAKE_WHEELS_OUT_BTN-1].whenPressed(new RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out));
//        drive_buttons[INTAKE_WHEELS_OUT_BTN-1].whenReleased(new StopIntakeWheelsCmd());

        // control systems using usb1
        control_stick = new Joystick(CONTROL_USB_PORT);
        control_buttons = new JoystickButton[12];

        // map buttons and actions
        control_buttons[ELEVATOR_MOVE_UP-1] = new JoystickButton(control_stick, ELEVATOR_MOVE_UP);
        control_buttons[ELEVATOR_MOVE_UP-1].whenPressed(new MoveElevatorCmd(ElevatorSys.Direction.Up, 0.5));
        control_buttons[ELEVATOR_MOVE_UP-1].whenReleased(new StopElevatorCmd());

        control_buttons[ELEVATOR_MOVE_DOWN-1] = new JoystickButton(control_stick, ELEVATOR_MOVE_DOWN);
        control_buttons[ELEVATOR_MOVE_DOWN-1].whenPressed(new MoveElevatorCmd(ElevatorSys.Direction.Down, 0.5));
        control_buttons[ELEVATOR_MOVE_DOWN-1].whenReleased(new StopElevatorCmd());
//
//        control_buttons[ELEVATOR_MOVETO_BASE-1] = new JoystickButton(control_stick, ELEVATOR_MOVETO_BASE);
//        control_buttons[ELEVATOR_MOVETO_BASE-1].whenPressed(new ElvMoveToBaseCmd(ElevatorSys.Direction.Up, 0.5));
//
//        control_buttons[ELEVATOR_MOVETO_SCALE-1] = new JoystickButton(control_stick, ELEVATOR_MOVETO_SCALE);
//        control_buttons[ELEVATOR_MOVETO_SCALE-1].whenPressed(new ElvMoveToScaleCmd(ElevatorSys.Direction.Down, 0.5));
//
//        control_buttons[INTAKE_WHEELS_IN_BTN-1] = new JoystickButton(control_stick, INTAKE_WHEELS_IN_BTN);
//        control_buttons[INTAKE_WHEELS_IN_BTN-1].whenPressed(new RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.In));
//        control_buttons[INTAKE_WHEELS_IN_BTN-1].whenReleased(new StopIntakeWheelsCmd());
//
//        control_buttons[INTAKE_WHEELS_OUT_BTN-1] = new JoystickButton(control_stick, INTAKE_WHEELS_OUT_BTN);
//        control_buttons[INTAKE_WHEELS_OUT_BTN-1].whenPressed(new RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out));
//        control_buttons[INTAKE_WHEELS_OUT_BTN-1].whenReleased(new StopIntakeWheelsCmd());
//
//        control_buttons[INTAKE_OPEN_BTN-1] = new JoystickButton(control_stick, INTAKE_OPEN_BTN);
//        control_buttons[INTAKE_OPEN_BTN-1].whenPressed(new OpenIntakeCmd());
//
//        control_buttons[INTAKE_CLOSE_BTN-1] = new JoystickButton(control_stick, INTAKE_CLOSE_BTN);
//        control_buttons[INTAKE_CLOSE_BTN-1].whenPressed(new CloseIntakeCmd());

    }


}
