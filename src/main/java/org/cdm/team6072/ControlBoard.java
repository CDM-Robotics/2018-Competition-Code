package org.cdm.team6072;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.cdm.team6072.autonomous.routines.subroutines.PositionIntake;
import org.cdm.team6072.autonomous.routines.subroutines.PositionScaleShooter;
import org.cdm.team6072.autonomous.routines.subroutines.PositionSwitchShooter;
import org.cdm.team6072.commands.drive.DriveDistCmd;
import org.cdm.team6072.commands.drive.DriveToggleGearCmd;
import org.cdm.team6072.commands.drive.DriveTurnYawCmd;
import org.cdm.team6072.commands.elevator.*;
import org.cdm.team6072.commands.intake.*;
import org.cdm.team6072.commands.arm.*;
import org.cdm.team6072.subsystems.*;


/**
 * ControlBoard holds the code for interacting with the
 */
public class ControlBoard {
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


    // control stick  ----------------------------------------------------------
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
        this.initSticks();
        this.setupProductionControls();
        //this.setupTestControls();

    }

    private void initSticks() {
        // USB Port 0
        drive_stick = new Joystick(DRIVE_USB_PORT);
        drive_buttons = new JoystickButton[12];

        // USB Port 1
        control_stick = new Joystick(CONTROL_USB_PORT);
        control_buttons = new JoystickButton[12];
    }


    private void setupProductionControls() {

        // *********************************************** //
        // DRIVE STICK
        // ********************************************** //

        // toggle hi lo gear
        drive_buttons[EXTREME_BUT_8-1] = new JoystickButton(drive_stick, EXTREME_BUT_8);
        drive_buttons[EXTREME_BUT_8-1].whenPressed(new DriveToggleGearCmd(drive_stick));


        // ********************************************* //
        // CONTROL STICK
        // ********************************************* //

        // elevator move up
        control_buttons[EXTREME_BUT_LEFT_TOP-1] = new JoystickButton(control_stick, EXTREME_BUT_LEFT_TOP);
        control_buttons[EXTREME_BUT_LEFT_TOP-1].whenPressed(new MoveElevatorCmd(ElevatorSys.Direction.Up, 0.8));
        control_buttons[EXTREME_BUT_LEFT_TOP-1].whenReleased(new StopElevatorCmd());

        // move elevator down
        control_buttons[EXTREME_BUT_LEFT_BOT-1] = new JoystickButton(control_stick, EXTREME_BUT_LEFT_BOT);
        control_buttons[EXTREME_BUT_LEFT_BOT-1].whenPressed(new MoveElevatorCmd(ElevatorSys.Direction.Down, 0.6));
        control_buttons[EXTREME_BUT_LEFT_BOT-1].whenReleased(new StopElevatorCmd());

        // move arm up
        control_buttons[EXTREME_BUT_RIGHT_TOP-1] = new JoystickButton(control_stick, EXTREME_BUT_RIGHT_TOP);
        control_buttons[EXTREME_BUT_RIGHT_TOP-1].whenPressed(new ArmMoveCmd(ArmSys.Direction.Up, 0.5));
        control_buttons[EXTREME_BUT_RIGHT_TOP-1].whenReleased(new ArmStopCmd());

        // move arm down
        control_buttons[EXTREME_BUT_RIGHT_BOT-1] = new JoystickButton(control_stick, EXTREME_BUT_RIGHT_BOT);
        control_buttons[EXTREME_BUT_RIGHT_BOT-1].whenPressed(new ArmMoveCmd(ArmSys.Direction.Down, 0.5));
        control_buttons[EXTREME_BUT_RIGHT_BOT-1].whenReleased(new ArmStopCmd());

        // intake wheels IN
        control_buttons[EXTREME_BUT_TRIGGER-1] = new JoystickButton(control_stick, EXTREME_BUT_TRIGGER);
        control_buttons[EXTREME_BUT_TRIGGER-1].whenPressed(new RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.In, 1.0));
        control_buttons[EXTREME_BUT_TRIGGER-1].whenReleased(new IntakeRunWheelsInLoCmd());

        // intake wheels OUT
        control_buttons[EXTREME_BUT_THUMB-1] = new JoystickButton(control_stick, EXTREME_BUT_THUMB);
        control_buttons[EXTREME_BUT_THUMB-1].whenPressed(new RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0));
        control_buttons[EXTREME_BUT_THUMB-1].whenReleased(new IntakeRunWheelsInLoCmd());

        // position SWITCH
        control_buttons[EXTREME_BUT_7 -1] = new JoystickButton(control_stick, EXTREME_BUT_7);
        control_buttons[EXTREME_BUT_7 -1].whenPressed(new PositionSwitchShooter());

        // position SCALE
        control_buttons[EXTREME_BUT_8-1] = new JoystickButton(control_stick, EXTREME_BUT_8);
        control_buttons[EXTREME_BUT_8-1].whenPressed(new PositionScaleShooter());

        // position INTAKE
        control_buttons[EXTREME_BUT_9 -1] = new JoystickButton(control_stick, EXTREME_BUT_9);
        control_buttons[EXTREME_BUT_9 -1].whenPressed(new PositionIntake());

        // intake CLOSE LO
        control_buttons[EXTREME_BUT_10 -1] = new JoystickButton(control_stick, EXTREME_BUT_10);
        control_buttons[EXTREME_BUT_10 -1].whenPressed(new CloseIntakeLoCmd());

        // intake CLOSE HI
        control_buttons[EXTREME_BUT_11 -1] = new JoystickButton(control_stick, EXTREME_BUT_11);
        control_buttons[EXTREME_BUT_11 -1].whenPressed(new CloseIntakeHiCmd());   //new PositionSwitchShooter());

        // intake OPEN
        control_buttons[EXTREME_BUT_12-1] = new JoystickButton(control_stick, EXTREME_BUT_12);
        control_buttons[EXTREME_BUT_12-1].whenPressed(new OpenIntakeCmd());

    }



    // test buttons for testing autonomous, arm function, etc
    private void setupTestControls()
    {

        // ***************************************** //
        // DRIVE STICK
        // **************************************** //
        // toggle hi lo gear
        drive_buttons[EXTREME_BUT_8-1] = new JoystickButton(drive_stick, EXTREME_BUT_8);
        drive_buttons[EXTREME_BUT_8-1].whenPressed(new DriveToggleGearCmd(drive_stick));


        drive_buttons[EXTREME_BUT_11 -1] = new JoystickButton(drive_stick, EXTREME_BUT_11);     // 9
        drive_buttons[EXTREME_BUT_11 -1].whenPressed(new ArmMoveTo45());

        drive_buttons[EXTREME_BUT_12 -1] = new JoystickButton(drive_stick, EXTREME_BUT_12);     // 9
        drive_buttons[EXTREME_BUT_12 -1].whenPressed(new ArmMoveTo135());

        // *************************************** //
        // CONTROL STICK
        // ************************************** //

        control_buttons[EXTREME_BUT_8 - 1] = new JoystickButton(control_stick, EXTREME_BUT_8);
        control_buttons[EXTREME_BUT_8 - 1].whenPressed(new ElvMoveToSwitchCmd());

        control_buttons[EXTREME_BUT_9 - 1] = new JoystickButton(control_stick, EXTREME_BUT_9);
        control_buttons[EXTREME_BUT_9 - 1].whenPressed(new PositionSwitchShooter());

        control_buttons[EXTREME_BUT_10 - 1] = new JoystickButton(control_stick, EXTREME_BUT_10);
        control_buttons[EXTREME_BUT_10 - 1].whenPressed(new DriveDistCmd(6));

        control_buttons[EXTREME_BUT_11 - 1] = new JoystickButton(control_stick, EXTREME_BUT_11);
        control_buttons[EXTREME_BUT_11 - 1].whenPressed(new DriveTurnYawCmd(45));

        control_buttons[EXTREME_BUT_12 - 1] = new JoystickButton(control_stick, EXTREME_BUT_12);
        control_buttons[EXTREME_BUT_12 - 1].whenPressed(new DriveTurnYawCmd(-45));

    }


}
