package frc;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import frc.subsystems.GearSlider;

import javax.naming.ldap.Control;

public class ControlBoard {

    public static ControlBoard instance = new ControlBoard();
    public static ControlBoard getInstance() {
        return instance;
    }

    public Joystick stick;
    private JoystickButton[] stick_buttons = new JoystickButton[12];

    public Joystick gamepad;
    private JoystickButton[] gamepadButtons = new JoystickButton[7];

    private ControlBoard () {
        stick = new Joystick(0); // joystick at usb port 0
        gamepad = new Joystick(1); // gamepad at usb port 1

        // map buttons and actions
        stick_buttons[ControlMappings.SHIFT_DRIVE_LOW_BTN] = new JoystickButton(stick, ControlMappings.SHIFT_DRIVE_LOW_BTN);

        stick_buttons[ControlMappings.GEAR_SLIDER_STICK] = new JoystickButton(stick, ControlMappings.GEAR_SLIDER_STICK);
        stick_buttons[ControlMappings.GEAR_SLIDER_STICK].whenActive(GearSlider.getInstance().slide());
    }
}
