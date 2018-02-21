package org.cdm.team6072.commands.drive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.ControlBoard;
import org.cdm.team6072.subsystems.DriveSys;


/**
 * Define a command for driving
 */
public class ArcadeDriveCmd extends Command {


    private Joystick mStick;

    private DriveSys mDriveSys;


    /**
     * Specify the the command requires the DriveSys subsystem
     */
    public ArcadeDriveCmd(Joystick stick) {
        requires(DriveSys.getInstance());
        mStick = stick;
        mDriveSys = DriveSys.getInstance();
    }


    /**
     * Execute is called by the scheduler until the command returns finished
     * or the OI stops requesting - for example if the whileHeld() button command is used
     */
    protected void execute() {
        mDriveSys.arcadeDrive(mStick.getY(), mStick.getX());
    }


    /**
     * @return Return true when command is completed
     */
    @Override
    protected boolean isFinished() {
        return false;
    }


}
