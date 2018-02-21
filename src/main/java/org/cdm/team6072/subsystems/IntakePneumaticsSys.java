package org.cdm.team6072.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.cdm.team6072.RobotConfig;



public class IntakePneumaticsSys extends Subsystem {


    // IMPORTANT NOTE
    // DoubleSolenoid class abstracts away the PCM. It's being detected by WPILib

    /**
     * There are two double solenoids controlled by the PCM
     *  solenoid 1 select open or close
     *          if open, set sol 2 off
     *
     *  solenoid 2 select close lo pressure
     *                  or close high pressure
     */
    private DoubleSolenoid mSol_1;
    private DoubleSolenoid mSol_2;



    private Compressor mCompressor;


    private static IntakePneumaticsSys mInstance;
    public static IntakePneumaticsSys getInstance() {
        if (mInstance == null) {
            mInstance = new IntakePneumaticsSys();
        }
        return mInstance;
    }


    private IntakePneumaticsSys() {
        // the compressor will automatically stop when the pressure gets too high
        System.out.println("IntakePneumaticsSys.ctor  --------------------------------------------------------");
        mCompressor = new Compressor(RobotConfig.PCM_ID);
        mCompressor.start();
        mSol_1 = new DoubleSolenoid(RobotConfig.PCM_ID, RobotConfig.INTAKE_SOL_1_FWD_OPEN, RobotConfig.INTAKE_SOL_1_REV_CLOSE);
        mSol_2 = new DoubleSolenoid(RobotConfig.PCM_ID, RobotConfig.INTAKE_SOL_2_FWD_LO, RobotConfig.INTAKE_SOL_2_REV_HI);
    }


    @Override
    protected void initDefaultCommand() {

    }


    public void OpenIntake() {
        System.out.println("IntakePneumaticsSys.OpenIntake: exec");
        mSol_1.set(DoubleSolenoid.Value.kForward);
        mSol_2.set(DoubleSolenoid.Value.kForward);
    }



    public void CloseIntakeLo() {
        System.out.println("IntakePneumaticsSys.CloseIntakeLo: exec");
        mSol_1.set(DoubleSolenoid.Value.kForward);
        mSol_2.set(DoubleSolenoid.Value.kReverse);
    }



    public void CloseIntakeHi() {
        System.out.println("IntakePneumaticsSys.CloseIntakeHi: exec");
        mSol_1.set(DoubleSolenoid.Value.kReverse);
        mSol_2.set(DoubleSolenoid.Value.kReverse);
    }



}
