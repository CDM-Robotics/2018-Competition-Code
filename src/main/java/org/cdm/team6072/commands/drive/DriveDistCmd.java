package org.cdm.team6072.commands.drive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.CmdWatchdog;
import org.cdm.team6072.subsystems.DriveSys;

public class DriveDistCmd extends Command {


    private Joystick mStick;

    private DriveSys mDriveSys;

    private float mDistInFeet;

    private int timeout = -1;

    private CmdWatchdog mWatchDog;


    /**
     * Specify the the command requires the DriveSys subsystem
     */
    public DriveDistCmd(float distInFeeet) {
        requires(DriveSys.getInstance());
        mDistInFeet = distInFeeet;
    }

    public DriveDistCmd(float distInFeeet, int milliSecs, String cmdName) {
        requires(DriveSys.getInstance());
        this.setName(cmdName);
        mDistInFeet = distInFeeet;
//        mWatchDog = CmdWatchdog.SetWatchdog(this, milliSecs);
        this.timeout = milliSecs;
    }


    @Override
    protected void initialize() {
        mDriveSys = DriveSys.getInstance();
        mDriveSys.startMoveDistance(mDistInFeet);

        if (timeout != -1) {
            this.setTimeout(timeout/1000);
        }
    }


    /**
     * Execute is called by the scheduler until the command returns finished
     * or the OI stops requesting - for example if the whileHeld() button command is used
     */
    protected void execute() {
        //mDriveSys.moveDistanceExec();
        mDriveSys.moveDistancePIDExec();
    }


    @Override
    protected void interrupted() {
        System.out.println("DriveDistCmd.interrrupted   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        mDriveSys.arcadeDrive(0, 0);
        super.interrupted();
    }

    @Override
    protected synchronized boolean isTimedOut() {
        if (super.isTimedOut()) {
            System.out.println("DriveDistCmd.isTimedOut ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            mDriveSys.arcadeDrive(0, 0);
        }
        return super.isTimedOut();
    }

    /**
     * @return Return true when command is completed
     */
    @Override
    protected boolean isFinished() {
        if (timeout != -1) {
            if (isTimedOut()) {
                System.out.println("DriveDistCmd.isFinished -- isTimedOut is true  ----------------------------");
            }
            return mDriveSys.moveDistComplete() || isTimedOut();
        }
        return mDriveSys.moveDistComplete();
    }

//    @Override
//    protected boolean isCanceled() {
//        return false;is
//    }


}
