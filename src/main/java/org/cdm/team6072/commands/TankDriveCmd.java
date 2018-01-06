package org.cdm.team6072.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.ControlBoard;
import org.cdm.team6072.subsystems.DriveTrain2018;


/**
 * Define a command for driving
 */
public class TankDriveCmd extends Command {


    private Joystick mStick;


    /**
     * Specify the the command requires the DriveTrain2018 subsystem
     */
    public TankDriveCmd(Joystick stick) {
        mStick = stick;
        requires(DriveTrain2018.getInstance());
    }


    /**
     * Execute is called by the scheduler until the command returns finished
     * or the OI stops requesting - for example if the whileHeld() button command is used
     */
    protected void execute() {
        //System.out.println("6072: execute called");
        DriveTrain2018 driveTrain = DriveTrain2018.getInstance();
        //driveTrain.tankDrive(mStick.getRawAxis(1), mStick.getRawAxis(0));
        driveTrain.tankDrive(mStick, 0, mStick, 1);
    }


    /**
     * @return Return true when command is completed
     */
    @Override
    protected boolean isFinished() {
        return false;
    }


}
