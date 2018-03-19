package org.cdm.team6072.commands.arm;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ArmSys;

public class ArmMoveShoot45   extends Command {


    private ArmSys mArmSys;


    public ArmMoveShoot45() {
        System.out.println("ArmMoveShoot45: ");
        requires(ArmSys.getInstance());
        mArmSys = ArmSys.getInstance();
    }

    @Override
    protected void initialize() {
        mArmSys.moveToShoot45();
    }

    @Override
    protected void execute() {
        // move is Shoot45ed in init
        mArmSys.moveStatus();
    }

    @Override
    protected boolean isFinished() {
        return mArmSys.moveToShoot45Complete();
    }

}
