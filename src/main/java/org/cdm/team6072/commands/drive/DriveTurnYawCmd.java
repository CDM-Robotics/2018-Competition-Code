package org.cdm.team6072.commands.drive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.DriveSys;

public class DriveTurnYawCmd extends Command {


    private Joystick mStick;

    private DriveSys mDriveSys;

    private int mAngle;


    /**
     * Specify the the command requires the DriveSys subsystem
     */
    public DriveTurnYawCmd(int angle) {
        requires(DriveSys.getInstance());
        mAngle  = angle;
    }


    @Override
    protected void initialize() {
        mDriveSys = DriveSys.getInstance();
        mDriveSys.initTurnYaw(mAngle);
    }


    /**
     * Execute is called by the scheduler until the command returns finished
     * or the OI stops requesting - for example if the whileHeld() button command is used
     */
    protected void execute() {
        //mDriveSys.moveDistanceExec();
        mDriveSys.turnYawExec();
    }

    @Override
    protected void interrupted() {
        mDriveSys.arcadeDrive(0, 0);
        super.interrupted();
    }

    /**
     * @return Return true when command is completed
     */
    @Override
    protected boolean isFinished() {
        return mDriveSys.turnYawComplete();
    }


}
