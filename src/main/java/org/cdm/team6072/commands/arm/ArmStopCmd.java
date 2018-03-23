package org.cdm.team6072.commands.arm;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ArmSys;

public class ArmStopCmd extends Command {


    private ArmSys mArmSys;

    /**
     * If StopElevatorCmd is called when MoveElevatorCmd is running, it will cause an interrupt
     * on move which will cause it to stop anyway
     */
    public ArmStopCmd() {
        requires(ArmSys.getInstance());
        mArmSys = ArmSys.getInstance();
    }


    @Override
    protected void initialize() {
        System.out.println("StopArmCmd:  ------------");
        mArmSys.initStop();
    }


    @Override
    protected void execute() {
        mArmSys.getInstance().stopping();
    }


    @Override
    protected boolean isFinished() {
        return mArmSys.stopComplete();
    }


}
