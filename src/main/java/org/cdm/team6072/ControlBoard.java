package org.cdm.team6072;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.cdm.team6072.commands.drive.DriveToggleGearCmd;
import org.cdm.team6072.commands.drive.TestDriveForward;
import org.cdm.team6072.commands.elevator.*;
import org.cdm.team6072.commands.intake.*;
import org.cdm.team6072.commands.arm.*;
import org.cdm.team6072.subsystems.*;


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

    // drive stick  --------------------------------------------------------------

    public static int DRIVE_HALFSPEED = EXTREME_BUT_THUMB;


    // test buttons on drive stick  ----------------

    public static int DRIVE_TEST = EXTREME_BUT_7;

    public static final int DRIVE_TOGGLE_GEAR = 8;








    // control stick  ----------------------------------------------------------

    // Elevator
    //      Move elevator up and down using Y-axis
    //      Move to fixed positions using buttons on base - trigger MotionProfiles based on current position

    public static int ELEVATOR_MOVE_UP = EXTREME_BUT_LEFT_TOP;
    public static int ELEVATOR_MOVE_DOWN = EXTREME_BUT_LEFT_BOT;
    public static int ELEVATOR_RESET_START = EXTREME_BUT_7;

    public static int ELEVATOR_MOVE_MM_DOWN = EXTREME_BUT_RIGHT_BOT;
    public static int ELEVATOR_MOVE_MM_UP = EXTREME_BUT_RIGHT_TOP;

    public static int ELEVATOR_MOVETO_BASE = EXTREME_BUT_9;
    public static int ELEVATOR_MOVETO_SWITCH = EXTREME_BUT_10;
    public static int ELEVATOR_MOVETO_SCALELO = EXTREME_BUT_11;
    public static int ELEVATOR_MOVETO_SWITCHHI = EXTREME_BUT_12;

    public static int ARM_MOVE_UP = EXTREME_BUT_RIGHT_TOP;
    public static int ARM_MOVE_DOWN = EXTREME_BUT_RIGHT_BOT;

    public static int ARM_RESET_START  = EXTREME_BUT_8;


    // Intake
    //      Control by buttons on top of the
    public static int INTAKE_OPEN_BTN = EXTREME_BUT_12;
    public static int INTAKE_CLOSE_LO_BTN = EXTREME_BUT_9;
    public static int INTAKE_CLOSE_HI_BTN = EXTREME_BUT_11;
    public static int INTAKE_WHEELS_IN_BTN = EXTREME_BUT_TRIGGER;  // maybe have wheels in auto select close lo?
    public static int INTAKE_WHEELS_OUT_BTN = EXTREME_BUT_THUMB;



    // drive stick is used for driving robot
    private static int DRIVE_USB_PORT = 0;
    public Joystick drive_stick;
    private JoystickButton[] drive_buttons;

    // control stick is used for elevator, intake
    private static int CONTROL_USB_PORT = 1;
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
        drive_stick = new Joystick(DRIVE_USB_PORT);
        drive_buttons = new JoystickButton[12];

        // test drive forward
//        drive_buttons[EXTREME_BUT_THUMB-1] = new JoystickButton(drive_stick, EXTREME_BUT_THUMB);
//        drive_buttons[EXTREME_BUT_THUMB-1].whenPressed(new TestDriveForward());

        // toggle hi lo gear
        drive_buttons[EXTREME_BUT_8-1] = new JoystickButton(drive_stick, EXTREME_BUT_8);
        drive_buttons[EXTREME_BUT_8-1].whenPressed(new DriveToggleGearCmd(drive_stick));

//        drive_buttons[DRIVE_TEST-1] = new JoystickButton(drive_stick, DRIVE_TEST);
//        drive_buttons[DRIVE_TEST-1].whenPressed(new TestDriveForward());

//        drive_buttons[ELEVATOR_MOVE_UP-1] = new JoystickButton(drive_stick, ELEVATOR_MOVE_UP);
//        drive_buttons[ELEVATOR_MOVE_UP-1].whenPressed(new MoveElevatorCmd(ElevatorSys.Direction.Up, 1.0));
//        drive_buttons[ELEVATOR_MOVE_UP-1].whenReleased(new StopElevatorCmd());
//
//        drive_buttons[ELEVATOR_MOVE_DOWN-1] = new JoystickButton(drive_stick, ELEVATOR_MOVE_DOWN);
//        drive_buttons[ELEVATOR_MOVE_DOWN-1].whenPressed(new MoveElevatorCmd(ElevatorSys.Direction.Down, 0.2));
//        drive_buttons[ELEVATOR_MOVE_DOWN-1].whenReleased(new StopElevatorCmd());

//        drive_buttons[ELEVATOR_MOVE_MM_UP-1] = new JoystickButton(drive_stick, ELEVATOR_MOVE_MM_UP);
//        drive_buttons[ELEVATOR_MOVE_MM_UP-1].whenPressed(new ElvMotionMagicCmd(ElevatorSys.Direction.Up, 1));
//        drive_buttons[ELEVATOR_MOVE_MM_UP-1].whenReleased(new StopElevatorCmd());
//
//        drive_buttons[ELEVATOR_MOVE_MM_DOWN-1] = new JoystickButton(drive_stick, ELEVATOR_MOVE_MM_DOWN);
//        drive_buttons[ELEVATOR_MOVE_MM_DOWN-1].whenPressed(new ElvMotionMagicCmd(ElevatorSys.Direction.Down, 1));
//        drive_buttons[ELEVATOR_MOVE_MM_DOWN-1].whenReleased(new StopElevatorCmd());

//        drive_buttons[ELEVATOR_MOVETO_BASE-1] = new JoystickButton(drive_stick, ELEVATOR_MOVETO_BASE);
//        drive_buttons[ELEVATOR_MOVETO_BASE-1].whenPressed(new ElvMoveToBaseCmd(ElevatorSys.Direction.Down, 1));

        drive_buttons[EXTREME_BUT_7 -1] = new JoystickButton(drive_stick, EXTREME_BUT_7);     // 9
        drive_buttons[EXTREME_BUT_7 -1].whenPressed(new ElvFindBaseCmd());
//
        drive_buttons[EXTREME_BUT_10 -1] = new JoystickButton(drive_stick, EXTREME_BUT_10);  // 10
        drive_buttons[EXTREME_BUT_10 -1].whenPressed(new ElvMoveToSwitchCmd());

        drive_buttons[EXTREME_BUT_11 -1] = new JoystickButton(drive_stick, EXTREME_BUT_11);  // 11
        drive_buttons[EXTREME_BUT_11 -1].whenPressed(new ElvMoveToScaleLoCmd());

        drive_buttons[EXTREME_BUT_12-1] = new JoystickButton(drive_stick, EXTREME_BUT_12);  // 12
        drive_buttons[EXTREME_BUT_12-1].whenPressed(new ElvMoveToScaleHiCmd());

//        drive_buttons[ELEVATOR_MOVE_DELTA-1] = new JoystickButton(drive_stick, ELEVATOR_MOVE_DELTA);
//        drive_buttons[ELEVATOR_MOVE_DELTA-1].whenPressed(new ElvMoveDeltaCmd(ElevatorSys.Direction.Up, 10, 0.5));
//        drive_buttons[ELEVATOR_MOVE_DELTA-1].whenReleased(new StopElevatorCmd());

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

        // elevator move up
        control_buttons[EXTREME_BUT_LEFT_TOP-1] = new JoystickButton(control_stick, EXTREME_BUT_LEFT_TOP);
        control_buttons[EXTREME_BUT_LEFT_TOP-1].whenPressed(new MoveElevatorCmd(ElevatorSys.Direction.Up, 0.8));
        control_buttons[EXTREME_BUT_LEFT_TOP-1].whenReleased(new StopElevatorCmd());

        // move elevator down
        control_buttons[EXTREME_BUT_LEFT_BOT-1] = new JoystickButton(control_stick, EXTREME_BUT_LEFT_BOT);
        control_buttons[EXTREME_BUT_LEFT_BOT-1].whenPressed(new MoveElevatorCmd(ElevatorSys.Direction.Down, 0.6));
        control_buttons[EXTREME_BUT_LEFT_BOT-1].whenReleased(new StopElevatorCmd());

//        control_buttons[ELEVATOR_RESET_START-1] = new JoystickButton(control_stick, ELEVATOR_RESET_START);
//        control_buttons[ELEVATOR_RESET_START-1].whenPressed(new ElvResetCmd());

        // move arm up
        control_buttons[EXTREME_BUT_RIGHT_TOP-1] = new JoystickButton(control_stick, EXTREME_BUT_RIGHT_TOP);
        control_buttons[EXTREME_BUT_RIGHT_TOP-1].whenPressed(new ArmMoveCmd(ArmSys.Direction.Up, 0.5));
        control_buttons[EXTREME_BUT_RIGHT_TOP-1].whenReleased(new ArmStopCmd());

        // move arm down
        control_buttons[EXTREME_BUT_RIGHT_BOT-1] = new JoystickButton(control_stick, EXTREME_BUT_RIGHT_BOT);
        control_buttons[EXTREME_BUT_RIGHT_BOT-1].whenPressed(new ArmMoveCmd(ArmSys.Direction.Down, 0.5));
        control_buttons[EXTREME_BUT_RIGHT_BOT-1].whenReleased(new ArmStopCmd());

//        control_buttons[ARM_RESET_START-1] = new JoystickButton(control_stick, ARM_RESET_START);
//        control_buttons[ARM_RESET_START-1].whenPressed(new ArmResetCmd());


//        control_buttons[ELEVATOR_MOVETO_SWITCH-1] = new JoystickButton(control_stick, ELEVATOR_MOVETO_SWITCH);
//        control_buttons[ELEVATOR_MOVETO_SWITCH-1].whenPressed(new ElvMoveToSwitchCmd());

        // intake wheels IN
        control_buttons[EXTREME_BUT_TRIGGER-1] = new JoystickButton(control_stick, EXTREME_BUT_TRIGGER);
        control_buttons[EXTREME_BUT_TRIGGER-1].whenPressed(new RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.In, 0.5));
        control_buttons[EXTREME_BUT_TRIGGER-1].whenReleased(new StopIntakeWheelsCmd());

        // intake wheels OUT
        control_buttons[EXTREME_BUT_THUMB-1] = new JoystickButton(control_stick, EXTREME_BUT_THUMB);
        control_buttons[EXTREME_BUT_THUMB-1].whenPressed(new RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 0.8));
        control_buttons[EXTREME_BUT_THUMB-1].whenReleased(new StopIntakeWheelsCmd());

        // intake OPEN
        control_buttons[EXTREME_BUT_12-1] = new JoystickButton(control_stick, EXTREME_BUT_12);
        control_buttons[EXTREME_BUT_12-1].whenPressed(new OpenIntakeCmd());

        // intake CLOSE LO
        control_buttons[EXTREME_BUT_9 -1] = new JoystickButton(control_stick, EXTREME_BUT_9);
        control_buttons[EXTREME_BUT_9 -1].whenPressed(new CloseIntakeLoCmd());

        // intake CLOSE HI
        control_buttons[EXTREME_BUT_11 -1] = new JoystickButton(control_stick, EXTREME_BUT_11);
        control_buttons[EXTREME_BUT_11 -1].whenPressed(new CloseIntakeHiCmd());

    }


}
