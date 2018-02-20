package org.cdm.team6072.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.Robot;
import org.cdm.team6072.RobotConfig;
import org.cdm.team6072.subsystems.DriveSys;


import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;


public class TestDriveGyro extends Command {

    // 5 seconds - assumes execute is called every 20 mSec
    private static int KTIMETORUN = 50 * 8;


    public TestDriveGyro() {
        requires(DriveSys.getInstance());
    }


    private int mCounter = 0;

    private DriveSys mDriveSys;
    private AHRS mAhrs = new AHRS(SPI.Port.kMXP);

    private double mStartAngle;

    @Override
    protected void initialize() {
        try {
            mDriveSys = DriveSys.getInstance();
//            byte updateHz = 64;
//            mAhrs = new AHRS(SPI.Port.kMXP, 100000, updateHz);
            //mAhrs.reset();
            mStartAngle = mAhrs.getAngle();
            mAhrs.enableLogging(false);
            mAhrs.getBoardYawAxis();
            System.out.println("TestDriveGyro.init  navX yaw axis:" + mAhrs.getBoardYawAxis().board_axis.toString() + "  isCalibrating: " + mAhrs.isCalibrating());
        }
        catch (Exception ex) {
            System.out.println("*********************  TestDriveGyro: Ex initializing: " + ex.getMessage());
        }
    }


    @Override
    protected void execute() {
        mCounter++;
        double angle = mAhrs.getAngle();
        double yaw = mAhrs.getYaw();
        boolean isMoving = mAhrs.isMoving();
        boolean isConnected = mAhrs.isConnected();
        double correct = mStartAngle - angle;
        if (mCounter % 5 == 0) {
            System.out.println("TestDriveFwd.execute: startAngle: " + mStartAngle + "  curAngle = " + angle + "  correct: " + correct + "  isMoving: " + isMoving + "  isConn: " + isConnected);
        }
        if (mCounter == 20) {
            mAhrs.enableLogging(false);
        }
        mDriveSys.arcadeDrive(-0.4,-correct / 180);
    }

    @Override
    protected boolean isFinished() {
        if (mCounter == KTIMETORUN) {
            mDriveSys.arcadeDrive(0,0);
            return true;
        }
        return false;
    }

}
