package org.cdm.team6072.commands.intake;


import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.TimedCommand;
import org.cdm.team6072.subsystems.IntakeMotorSys;
import util.CrashTracker;


public class TimedRunIntakeWheelsCmd extends TimedCommand {


    private IntakeMotorSys mGrabber;
    private IntakeMotorSys.WheelDirn mRunDirn;
    private double mSpeed;


    public TimedRunIntakeWheelsCmd(IntakeMotorSys.WheelDirn runDirn, double speed, double timeSecs) {
        super("TimedRunIntakeWheelsCmd", timeSecs);
        CrashTracker.logMessage("RunIntakeWheelsCmd: direction: " + runDirn);
        requires(IntakeMotorSys.getInstance());
        mRunDirn = runDirn;
        mSpeed = speed;
    }


    @Override
    protected void initialize() {
        mGrabber = IntakeMotorSys.getInstance();
    }


    @Override
    protected void execute() {
        mGrabber.runWheels(mRunDirn, mSpeed);
    }


    // rely on TimedCommand to return isFinished and isTimedOut


    @Override
    protected boolean isFinished() {
        if (super.isFinished()) {
            mGrabber.runWheels(IntakeMotorSys.WheelDirn.Out, 0);
        }
        return super.isFinished();
    }
}
