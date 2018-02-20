package org.cdm.team6072.commands.drive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.ControlBoard;
import org.cdm.team6072.subsystems.DriveSys;


/**
 * Define a command for driving
 */
public class TankDriveCmd extends Command {


    private Joystick mStick;

    private DriveSys mDriveSys;


    /**
     * Specify the the command requires the DriveSys subsystem
     */
    public TankDriveCmd(Joystick stick) {
        requires(DriveSys.getInstance());
        mStick = stick;
        mDriveSys = DriveSys.getInstance();
    }


    /**
     * Execute is called by the scheduler until the command returns finished
     * or the OI stops requesting - for example if the whileHeld() button command is used
     */
    protected void execute() {
        //driveSys.tankDrive(mStick.getRawAxis(1), mStick.getRawAxis(0));
        //driveSys.tankDrive(mStick, 0, mStick, 1);

        mDriveSys.tankDrive(ControlBoard.getInstance().drive_stick.getY(), ControlBoard.getInstance().drive_stick.getY());
        //driveSys.tankDrive(ControlBoard.getInstance().drive_stick.getY(GenericHID.Hand.kLeft), ControlBoard.getInstance().drive_stick.getY(GenericHID.Hand.kRight));
        // 3D Pro Joystick

        //driveSys.tankDrive(ControlBoard.getInstance().drive_stick, 1, ControlBoard.getInstance().drive_stick, 0);
        // Taranis
        //driveSys.tankDrive(ControlBoard.getInstance().drive_stick, 1, ControlBoard.getInstance().drive_stick, 0);
    }


    /**
     * @return Return true when command is completed
     */
    @Override
    protected boolean isFinished() {
        return false;
    }


}
