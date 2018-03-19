package org.cdm.team6072.commands.arm;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ArmSys;

public class ArmMoveIntake   extends Command {


    private ArmSys mArmSys;


    public ArmMoveIntake() {
        System.out.println("ArmMoveIntake: ");
        requires(ArmSys.getInstance());
        mArmSys = ArmSys.getInstance();
    }

    @Override
    protected void initialize() {
        mArmSys.moveToIntake();
    }

    @Override
    protected void execute() {
        // move is Intakeed in init
        mArmSys.moveStatus();
    }

    @Override
    protected boolean isFinished() {
        return mArmSys.moveToIntakeComplete();
    }
    
}
