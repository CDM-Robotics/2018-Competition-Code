package org.cdm.team6072.commands.drive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.ControlBoard;
import org.cdm.team6072.subsystems.DriveTrain;


/**
 * Define a command for driving
 */
public class ArcadeDriveCmd extends Command {


    private Joystick mStick;


    /**
     * Specify the the command requires the DriveTrain subsystem
     */
    public ArcadeDriveCmd(Joystick stick) {
        mStick = stick;
        requires(DriveTrain.getInstance());
    }


    /**
     * Execute is called by the scheduler until the command returns finished
     * or the OI stops requesting - for example if the whileHeld() button command is used
     */
    protected void execute() {
        //System.out.println("6072: execute called");
        DriveTrain driveTrain = DriveTrain.getInstance();
        //driveTrain.tankDrive(mStick.getRawAxis(1), mStick.getRawAxis(0));
        //driveTrain.tankDrive(mStick, 0, mStick, 1);

        // gamepad
        //ControlBoard.getInstance().usb0_stick.
        driveTrain.arcadeDrive(ControlBoard.getInstance().usb0_stick.getY(), ControlBoard.getInstance().usb0_stick.getZ());
        //driveTrain.tankDrive(ControlBoard.getInstance().usb0_stick.getY(GenericHID.Hand.kLeft), ControlBoard.getInstance().usb0_stick.getY(GenericHID.Hand.kRight));
        // 3D Pro Joystick

        //driveTrain.tankDrive(ControlBoard.getInstance().usb0_stick, 1, ControlBoard.getInstance().usb0_stick, 0);
        // Taranis
        //driveTrain.tankDrive(ControlBoard.getInstance().usb0_stick, 1, ControlBoard.getInstance().usb0_stick, 0);
    }


    /**
     * @return Return true when command is completed
     */
    @Override
    protected boolean isFinished() {
        return false;
    }


}
