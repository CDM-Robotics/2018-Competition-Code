package org.cdm.team6072.subsystems;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI;

public class Navigator implements PIDOutput {
    private static Navigator ourInstance = new Navigator();

    public static Navigator getInstance() {
        return ourInstance;
    }
    private AHRS ahrs;

    private Navigator() {
        try {
            this.ahrs = new AHRS(SPI.Port.kMXP); // initialize unit
        } catch (RuntimeException e) {
            System.out.println("navx error:");
        }
    }


    @Override
    // invoked periodically by pid controller/navX unit
    public void pidWrite(double output) {
        System.out.println("pid write: " + new Double(output).toString());
    }
}
