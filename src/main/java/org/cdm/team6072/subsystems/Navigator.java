package org.cdm.team6072.subsystems;

import util.CrashTracker;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI;

public class Navigator implements PIDOutput {


    private static Navigator mInstance;
    public static Navigator getInstance() {
        if (mInstance == null) {
            mInstance = new Navigator();
        }
        return mInstance;
    }


    private AHRS mAhrs;

    private Navigator() {
        try {
            mAhrs = new AHRS(SPI.Port.kMXP); // initialize unit
        } catch (RuntimeException e) {
            System.out.println("navx error: " + e.getMessage());
        }
    }


    @Override
    // invoked periodically by pid controller/navX unit
    public void pidWrite(double output) {

        System.out.println("pid write: " + new Double(output).toString());
    }


}
