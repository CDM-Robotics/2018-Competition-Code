package org.cdm.team6072.commands.drive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.CmdWatchdog;
import org.cdm.team6072.subsystems.DriveSys;

public class DriveDistCmd extends Command {


    private Joystick mStick;
    private DriveSys mDriveSys;
    private float mDistInFeet;
    private boolean reverse = false;
    private int timeout = -1;
    private CmdWatchdog mWatchDog;

    public enum DIR {
        FORWARD, REVERSE
    }

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
        this.timeout = milliSecs;
    }

    public DriveDistCmd(float distInFeet, DIR direction) {
        requires(DriveSys.getInstance());
        mDistInFeet = distInFeet;
        if (direction == DIR.REVERSE) {
            reverse = true;
        } else {
            reverse = false;
        }
    }


    @Override
    protected void initialize() {
        mDriveSys = DriveSys.getInstance();
        if (reverse == false) {
            mDriveSys.startMoveDistance(mDistInFeet);
        } else {
            mDriveSys.startMoveDistanceReverse(mDistInFeet);
        }
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
    // command group timeout should call this method
    protected void interrupted() {
        mDriveSys.arcadeDrive(0, 0); // stop the wheels from continuing to spin
        super.interrupted();
    }

    @Override
    protected synchronized boolean isTimedOut() {
        if (super.isTimedOut()) {
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
}
