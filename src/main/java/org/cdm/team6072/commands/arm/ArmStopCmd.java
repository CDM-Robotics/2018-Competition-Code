package org.cdm.team6072.commands.arm;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ArmSys;

public class ArmStopCmd extends Command {


    /**
     * If StopElevatorCmd is called when MoveElevatorCmd is running, it will cause an interrupt
     * on move which will cause it to stop anyway
     */
    public ArmStopCmd() {
        requires(ArmSys.getInstance());
    }

    protected void execute() {
//        System.out.println("ArmStopCmd: ---------------------------------------");
        ArmSys.getInstance().stop();
//        System.out.println("ArmStopCmd end: ---------------------------------------");
    }

    @Override
    protected boolean isFinished() {
        return ArmSys.getInstance().stopComplete();
    }


}
