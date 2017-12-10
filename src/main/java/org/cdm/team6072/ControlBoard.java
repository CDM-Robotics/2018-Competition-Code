package org.cdm.team6072;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.Climber;
//import frc.subsystems.Climber;
//import frc.subsystems.GearSlider;


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

    public Joystick stick;
    private JoystickButton[] stick_buttons;

    public Joystick gamepad;
    private JoystickButton[] gamepadButtons;



    private ControlBoard () {

        stick = new Joystick(0); // joystick at usb port 0
        stick_buttons = new JoystickButton[12];

        gamepad = new Joystick(1); // gamepad at usb port 1
        gamepadButtons = new JoystickButton[7];

        // map buttons and actions
        //stick_buttons[ControlMappings.SHIFT_DRIVE_LOW_BTN] = new JoystickButton(stick, ControlMappings.SHIFT_DRIVE_LOW_BTN);

        //stick_buttons[ControlMappings.GEAR_SLIDER_STICK] = new JoystickButton(stick, ControlMappings.GEAR_SLIDER_STICK);
        //stick_buttons[ControlMappings.GEAR_SLIDER_STICK].whenActive(GearSlider.getInstance().slide());

        gamepadButtons[ControlMappings.CLIMB_BTN] = new JoystickButton(gamepad, ControlMappings.CLIMB_BTN);
        //gamepadButtons[ControlMappings.CLIMB_BTN].whenActive(Climber.getInstance().initiateClimb());
        Command climb = Climber.getInstance().climbUp();
        gamepadButtons[ControlMappings.CLIMB_BTN].whileHeld(climb);
    }


}
