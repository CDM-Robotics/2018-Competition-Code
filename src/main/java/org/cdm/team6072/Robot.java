package org.cdm.team6072;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.cdm.team6072.autonomous.GameChooser;
import org.cdm.team6072.commands.drive.*;
import org.cdm.team6072.subsystems.*;
import util.ControlledLogger;
import util.Logger;


/**
 * Use TimedRobot as it has better control over the loop timing
 */
public class Robot extends TimedRobot {


    private DriveSys mDriveSys;
    private ElevatorSys mElevatorSys;
    private IntakePneumaticsSys mPneuSys;
    private IntakeMotorSys mIntakeMotorSys;
    private PowerDistributionPanel mPDP;
    private ArmSys mArmSys;

    // ControlBoard holds the operator interface code such as JoyStick
    private ControlBoard mControlBoard  = ControlBoard.getInstance();
    private UsbCamera cam;

    // set up SmartDash for choosing auto
    private   SendableChooser<AutoInitSel> mChooser ;

    private enum AutoInitSel {
        SWITCHCENTER,
        SWITCHCENTER_TWOCUBE,
        SWITCHLEFT_TWOCUBE,
        SWITCHRIGHT_TWOCUBE,
        SCALELEFT,
        SCALERIGHT,
        EXCHANGELEFT,
        EXCHANGECENTER,
        EXCHANGERIGHT
    }


    // ********************************************** //
    // ENABLE/DISABLE MODE
    // ********************************************* //
    @Override
    public void robotInit() {
        mDriveSys = DriveSys.getInstance();
        mElevatorSys = ElevatorSys.getInstance();
        mIntakeMotorSys = IntakeMotorSys.getInstance();
        mArmSys = ArmSys.getInstance();

        mDriveSys.setSensorStartPosn();
        mElevatorSys.setSensorStartPosn();
        mArmSys.setSensorStartPosn();

        // must initialize nav system here for the navX-MXP
        NavXSys.getInstance();

        //CameraManager.getInstance().runCameras();
        //CameraServer.getInstance().startAutomaticCapture("cam2", 0);
        CameraManager.getInstance().runCameras();


        // set up chooser
        // Dashboard default is set in file
        //       C:\Users\Public\Documents\FRC folder\FRC DS Data Storage.ini
        // Jars are in
        //      C:\Users\David\wpilib\tools
        // Updated JARs :
        //       http://first.wpi.edu/FRC/roborio/maven/release/edu/wpi/first/wpilib/SmartDashboard/

        mChooser = new SendableChooser<AutoInitSel>();
        mChooser.addObject("Switch       Left                   Two", AutoInitSel.SWITCHLEFT_TWOCUBE);
        mChooser.addObject("Switch             Center          One", AutoInitSel.SWITCHCENTER);
        mChooser.addDefault("Switch             Center           Two", AutoInitSel.SWITCHCENTER_TWOCUBE);
        mChooser.addObject("Switch                    Right     Two", AutoInitSel.SWITCHRIGHT_TWOCUBE);
        mChooser.addObject("Exchange     Left                   Two", AutoInitSel.EXCHANGELEFT);
        mChooser.addObject("Exchange           Center           Two", AutoInitSel.EXCHANGECENTER);
        mChooser.addObject("Exchange                   Right    Two", AutoInitSel.EXCHANGERIGHT);
        mChooser.addObject("Scale        Left                   Fallback Switch Two", AutoInitSel.SCALELEFT);
        mChooser.addObject("Scale                      Right    Fallback Switch Two", AutoInitSel.SCALERIGHT);

        SmartDashboard.putData(mChooser);
    }

    @Override
    public void disabledInit() {
        Logger.getInstance().printBanner("DISABLED INIT");
        // ensure the talons are not in MotionMagic or hold modes
        mElevatorSys.resetTalon();
        mArmSys.resetTalon();
    }

    public void disabledPeriodic() {
        new ControlledLogger().print(100, new Runnable() {
            @Override
            public void run() {
                mArmSys.printPosn("Rob.dis");
            }
        });
    }



    //************************************************************************************************************************ //
    // AUTONOMOUS MODE
    //******************************************* //
    @Override
    public void autonomousInit() {
        super.autonomousInit();
        Logger.getInstance().printBanner("AUTO INIT");

        DriverStation ds = DriverStation.getInstance();
        //CameraManager.getInstance().runCameras();

        NavXSys.getInstance().zeroYawHeading();

        //mDriveSys.setGearLo();
        AutoInitSel optSel = mChooser.getSelected();
        GameChooser gameChooser = new GameChooser();
        CommandGroup cmdGrp = new CommandGroup();
        switch (optSel) {
            case SWITCHCENTER:
                cmdGrp = gameChooser.chooseCmdGrp(GameChooser.CHOOSER.RUN_SWITCH, GameChooser.STARTBOX.CENTER, GameChooser.ALLOWCROSSFIELD.Yes);
                break;
            case SWITCHLEFT_TWOCUBE:
                cmdGrp = gameChooser.chooseCmdGrp(GameChooser.CHOOSER.RUN_SWITCH, GameChooser.STARTBOX.LEFT,
                        GameChooser.ALLOWCROSSFIELD.No, GameChooser.NUM_CUBES.TWO);
                break;
            case SWITCHCENTER_TWOCUBE:
                cmdGrp = gameChooser.chooseCmdGrp(GameChooser.CHOOSER.RUN_SWITCH, GameChooser.STARTBOX.CENTER,
                        GameChooser.ALLOWCROSSFIELD.No, GameChooser.NUM_CUBES.TWO);
                break;
            case SWITCHRIGHT_TWOCUBE:
                cmdGrp = gameChooser.chooseCmdGrp(GameChooser.CHOOSER.RUN_SWITCH, GameChooser.STARTBOX.RIGHT,
                            GameChooser.ALLOWCROSSFIELD.No, GameChooser.NUM_CUBES.TWO);
                break;
            case SCALELEFT:
                cmdGrp = gameChooser.chooseCmdGrp(GameChooser.CHOOSER.RUN_SCALE, GameChooser.STARTBOX.LEFT,
                            GameChooser.ALLOWCROSSFIELD.No, GameChooser.NUM_CUBES.TWO);
                break;
            case SCALERIGHT:
                cmdGrp = gameChooser.chooseCmdGrp(GameChooser.CHOOSER.RUN_SCALE, GameChooser.STARTBOX.RIGHT, GameChooser.ALLOWCROSSFIELD.No);
                break;
            case EXCHANGELEFT:
                cmdGrp = gameChooser.chooseCmdGrp(GameChooser.CHOOSER.RUN_EXCHANGE, GameChooser.STARTBOX.LEFT, GameChooser.ALLOWCROSSFIELD.No);
                break;
            case EXCHANGECENTER:
                cmdGrp = gameChooser.chooseCmdGrp(GameChooser.CHOOSER.RUN_EXCHANGE, GameChooser.STARTBOX.CENTER, GameChooser.ALLOWCROSSFIELD.No);
                break;
            case EXCHANGERIGHT:
                cmdGrp = gameChooser.chooseCmdGrp(GameChooser.CHOOSER.RUN_EXCHANGE, GameChooser.STARTBOX.RIGHT, GameChooser.ALLOWCROSSFIELD.No);
                break;
        }
        cmdGrp.start();

        System.out.println("Robot.AI: optSel:" + optSel + "  ----------------------------------------------------");
    }


    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }




    // ********************************************************************************************************************************************* //
    // TELEOP MODE
    // ******************************************* //
    @Override
    public void teleopInit() {
        Logger.getInstance().printBanner("TELEOP INIT");

        NavXSys.getInstance().zeroYawHeading();

        ArcadeDriveCmd  mArcadeDriveCmd = new ArcadeDriveCmd(mControlBoard.drive_stick);
        Scheduler.getInstance().removeAll();
        Scheduler.getInstance().add(mArcadeDriveCmd);

        //CameraManager.getInstance().runCameras();
    }

    /**
     * teleopPeriodic is called about every 20mSec
     * We call the scheduler to cause all commands that have been scheduled to run
     * A command is typically placed on the scheduler in response to operator input
     *  e.g. a button press
     */
    @Override
    public void teleopPeriodic() {
        // must call the scheduler to run
        Scheduler.getInstance().run();

        // reset the disabled loop print count to allow a few prints when disabled
        // update PDP stats every half second
//        new ControlledLogger().print(200, new Runnable() {
//            @Override
//            public void run() {
//                NavXSys.getInstance().outputAngles();
//            }
//        });
    }




    //******************************************* //
    // TEST MODE
    //****************************************** //
    @Override
    public void testInit() {
        Logger.getInstance().printBanner("TEST INIT");
    }

    @Override public void testPeriodic() {
        Scheduler.getInstance().run();
    }

}