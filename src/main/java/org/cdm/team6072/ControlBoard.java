package org.cdm.team6072;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.commands.elevator.MoveElevatorCmd;
import org.cdm.team6072.commands.elevator.StopElevatorCmd;
import org.cdm.team6072.subsystems.Climber;
import org.cdm.team6072.subsystems.Elevator;
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
    private JoystickButton[] stick_buttons;

    public Joystick gamepad;
    private JoystickButton[] gamepadButtons;



    private ControlBoard () {

        usb0_stick = new Joystick(0); // joystick at usb port 0
        stick_buttons = new JoystickButton[12];

        gamepad = new Joystick(1); // gamepad at usb port 1
        gamepadButtons = new JoystickButton[7];

        // map buttons and actions
        gamepadButtons[ControlMappings.ELEVATOR_UP_BTN] = new JoystickButton(usb0_stick, ControlMappings.ELEVATOR_UP_BTN);
        gamepadButtons[ControlMappings.ELEVATOR_UP_BTN].whenPressed(new MoveElevatorCmd(false));
        gamepadButtons[ControlMappings.ELEVATOR_UP_BTN].whenReleased(new StopElevatorCmd());

        gamepadButtons[ControlMappings.ELEVATOR_DOWN_BTN] = new JoystickButton(usb0_stick, ControlMappings.ELEVATOR_DOWN_BTN);
        gamepadButtons[ControlMappings.ELEVATOR_DOWN_BTN].whenPressed(new MoveElevatorCmd(true));
        gamepadButtons[ControlMappings.ELEVATOR_DOWN_BTN].whenReleased(new StopElevatorCmd());

        //stick_buttons[ControlMappings.SHIFT_DRIVE_LOW_BTN] = new JoystickButton(usb0_stick, ControlMappings.SHIFT_DRIVE_LOW_BTN);

        //gamepadButtons[ControlMappings.GEAR_SLIDER_STICK] = new JoystickButton(usb0_stick, ControlMappings.GEAR_SLIDER_STICK);
        //gamepadButtons[ControlMappings.GEAR_SLIDER_STICK].whenActive(GearSlider.getInstance().manualSlide());

        //gamepadButtons[ControlMappings.CLIMB_BTN] = new JoystickButton(gamepad, ControlMappings.CLIMB_BTN);
        //Command climb = Climber.getInstance().climbUp();
        //gamepadButtons[ControlMappings.CLIMB_BTN].whileHeld(climb);
    }


}
