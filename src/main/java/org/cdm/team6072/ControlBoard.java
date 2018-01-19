package org.cdm.team6072;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.cdm.team6072.commands.elevator.MoveElevatorCmd;
import org.cdm.team6072.commands.elevator.StopElevatorCmd;
import org.cdm.team6072.commands.grabber.OpenGrabberCmd;
import org.cdm.team6072.commands.grabber.RunGrabberWheelsCmd;
import org.cdm.team6072.commands.grabber.StopGrabberWheelsCmd;
import org.cdm.team6072.commands.grabber.CloseGrabberCmd;
import org.cdm.team6072.subsystems.Elevator;
import org.cdm.team6072.subsystems.Grabber;
//import frc.subsystems.Climber;
//import org.cdm.team6072.subsystems.GearSlider;


/**
 * ControlBoard holds the code for interacting with the
 */
public class ControlBoard {

    private static ControlBoard mInstance;

    public static ControlBoard getInstance() {
        if (mInstance == null) {
            mInstance = new ControlBoard();
        }
        return mInstance;
    }

    public Joystick usb0_stick;
    private JoystickButton[] usb0_buttons;




    // put Logitech gamepad on 1
    public Joystick usb1_stick;
    private JoystickButton[] usb1_Buttons;



    private ControlBoard () {

        // drive using usb0
        usb0_stick = new Joystick(0); // joystick at usb port 0
        usb0_buttons = new JoystickButton[12];
        usb0_buttons[3] = new JoystickButton(usb0_stick, 3);
        usb0_buttons[3].whenPressed(new RunGrabberWheelsCmd(Grabber.WheelDirn.In));
        usb0_buttons[3].whenReleased(new StopGrabberWheelsCmd());

        usb0_buttons[4] = new JoystickButton(usb0_stick, 4);
        usb0_buttons[4].whenPressed(new RunGrabberWheelsCmd(Grabber.WheelDirn.Out));
        usb0_buttons[4].whenReleased(new StopGrabberWheelsCmd());


        // control systems using usb1

        usb1_stick = new Joystick(1); // gamepad at usb port 1
        usb1_Buttons = new JoystickButton[12];

        // map buttons and actions
        usb1_Buttons[ControlMappings.ELEVATOR_UP_BTN] = new JoystickButton(usb1_stick, ControlMappings.ELEVATOR_UP_BTN);
        usb1_Buttons[ControlMappings.ELEVATOR_UP_BTN].whenPressed(new MoveElevatorCmd(Elevator.Direction.Up, 0.5));
        usb1_Buttons[ControlMappings.ELEVATOR_UP_BTN].whenReleased(new StopElevatorCmd());

        usb1_Buttons[ControlMappings.ELEVATOR_DOWN_BTN] = new JoystickButton(usb1_stick, ControlMappings.ELEVATOR_DOWN_BTN);
        usb1_Buttons[ControlMappings.ELEVATOR_DOWN_BTN].whenPressed(new MoveElevatorCmd(Elevator.Direction.Down, 0.5));
        usb1_Buttons[ControlMappings.ELEVATOR_DOWN_BTN].whenReleased(new StopElevatorCmd());

        usb1_Buttons[ControlMappings.GRABBER_WHEELS_IN_BTN] = new JoystickButton(usb1_stick, ControlMappings.GRABBER_WHEELS_IN_BTN);
        usb1_Buttons[ControlMappings.GRABBER_WHEELS_IN_BTN].whenPressed(new RunGrabberWheelsCmd(Grabber.WheelDirn.In));
        usb1_Buttons[ControlMappings.GRABBER_WHEELS_IN_BTN].whenReleased(new StopGrabberWheelsCmd());

        usb1_Buttons[ControlMappings.GRABBER_WHEELS_OUT_BTN] = new JoystickButton(usb1_stick, ControlMappings.GRABBER_WHEELS_OUT_BTN);
        usb1_Buttons[ControlMappings.GRABBER_WHEELS_OUT_BTN].whenPressed(new RunGrabberWheelsCmd(Grabber.WheelDirn.Out));
        usb1_Buttons[ControlMappings.GRABBER_WHEELS_OUT_BTN].whenReleased(new StopGrabberWheelsCmd());

        usb1_Buttons[ControlMappings.GRABBER_OPEN_BTN] = new JoystickButton(usb1_stick, ControlMappings.GRABBER_OPEN_BTN);
        usb1_Buttons[ControlMappings.GRABBER_OPEN_BTN].whenPressed(new OpenGrabberCmd());

        usb1_Buttons[ControlMappings.GRABBER_CLOSE_BTN] = new JoystickButton(usb1_stick, ControlMappings.GRABBER_CLOSE_BTN);
        usb1_Buttons[ControlMappings.GRABBER_CLOSE_BTN].whenPressed(new CloseGrabberCmd());


    }


}
