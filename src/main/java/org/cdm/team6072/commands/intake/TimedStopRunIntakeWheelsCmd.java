package org.cdm.team6072.commands.intake;


import edu.wpi.first.wpilibj.command.TimedCommand;
import org.cdm.team6072.subsystems.IntakeMotorSys;
import util.CrashTracker;


public class TimedStopRunIntakeWheelsCmd extends TimedCommand {


    private IntakeMotorSys mGrabber;
    private IntakeMotorSys.WheelDirn mRunDirn;
    private double mSpeed;


    public TimedStopRunIntakeWheelsCmd(double timeSecs) {
        super("TimedRunIntakeWheelsCmd", timeSecs);
        CrashTracker.logMessage("TimedStopRunIntakeWheelsCmd: time: " + timeSecs);
        requires(IntakeMotorSys.getInstance());
    }


    @Override
    protected void initialize() {
        mGrabber = IntakeMotorSys.getInstance();
    }


    @Override
    protected void execute() {

    }


    @Override
    protected boolean isFinished() {
        if (super.isFinished()) {
            mGrabber.runWheels(IntakeMotorSys.WheelDirn.Out, 0);
        }
        return super.isFinished();
    }
}
