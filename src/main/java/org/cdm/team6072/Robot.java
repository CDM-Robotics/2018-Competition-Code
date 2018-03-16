package org.cdm.team6072;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.cdm.team6072.commands.drive.*;
import org.cdm.team6072.subsystems.*;
import com.kauailabs.navx.frc.AHRS;


/**
 * Use TimedRobot as it has better control over the loop timing
 */
public class Robot extends TimedRobot {


    private DriveSys mDriveSys = DriveSys.getInstance() ;
    //private NavXSys mNavx = NavXSys.getInstance();
    private ElevatorSys mElevatorSys = ElevatorSys.getInstance();
    private IntakePneumaticsSys mPneuSys;
    private IntakeMotorSys mIntakeMotorSys = IntakeMotorSys.getInstance();

   // PowerDistributionPanel mPDP = new PowerDistributionPanel(RobotConfig.PDP_ID);

    // ControlBoard holds the operator interface code such as JoyStick
    private ControlBoard mControlBoard  = ControlBoard.getInstance();

    CommandGroup autonomousCommand;
    SendableChooser<Integer> sendableChooser = new SendableChooser<>();


    private UsbCamera cam;



    @Override
    public void robotInit() {
        System.out.println("6072: robotInit");
//        CameraManager.getInstance().runCameras();
//        CameraManager.getInstance().runFilter();

        // must initialize nav system here for the navX-MXP
        NavXSys.getInstance();
    }

    @Override
    public void disabledInit() {
        //DriveSys.getInstance().setupProfile();
    }


    //  TELEOP MODE  ---------------------------------------------------------------
    private ArcadeDriveCmd mArcadeDriveCmd;
    private TankDriveCmd mTankDriveCmd;


    private int mTelopLoopCtr = 0;

    @Override
    public void teleopInit() {
        System.out.println("6072: teleop init");
        mTelopLoopCtr = 0;

        mElevatorSys.setSensorStartPosn();
        //mPneuSys = IntakePneumaticsSys.getInstance();

        // drivesys now has ArcadeCommand set as default cmd
//        Scheduler.getInstance().removeAll();
//        mArcadeDriveCmd = new ArcadeDriveCmd(mControlBoard.drive_stick);
//        Scheduler.getInstance().add(mArcadeDriveCmd);
//        mTankDriveCmd = new TankDriveCmd(mControlBoard.drive_stick);
//        Scheduler.getInstance().add(mTankDriveCmd);

        
       //DriveSys.getInstance().getMotionProfileManager().startMotionProfile();
    }

//    public void disabledPeriodic() {
//        mDriveSys.arcadeDrive(0,0);
//    }

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

        // MOTION PROFILING
//        mElevatorSys.updateTalonRequiredMPState();
//        mElevatorSys.getMPController().control();
//        DriveSys.getInstance().updateTalonRequiredMPState();
//        DriveSys.getInstance().getMotionProfileManager().control();

//        ElevatorSys.getInstance().masterTalonTest();

        // update PDP stats every half second
        if (++mTelopLoopCtr % 50 == 0) {
            //logNavX();
//            Logging();
//            double elvCurrent = mPDP.getCurrent(RobotConfig.ELEVATOR_TALON_PDP);
//            double armCurrent = mPDP.getCurrent(RobotConfig.ARM_TALON_PDP);
//            double driveLeftCurrent = mPDP.getCurrent(RobotConfig.DRIVE_LEFT_MASTER_PDP) + mPDP.getCurrent(RobotConfig.DRIVE_LEFT_SLAVE0_PDP);
//            double driveRightCurrent = mPDP.getCurrent(RobotConfig.DRIVE_RIGHT_MASTER_PDP) + mPDP.getCurrent(RobotConfig.DRIVE_RIGHT_SLAVE0_PDP);
//
//            SmartDashboard.putNumber("PDP.ElevCurrent", elvCurrent);
//            SmartDashboard.putNumber("PDP.ArmElevCurrent", armCurrent);
//            SmartDashboard.putNumber("PDP.DriveLeftCurrent", driveLeftCurrent);
//            SmartDashboard.putNumber("PDP.DriveRightCurrent", driveRightCurrent);
        }
    }

//
//    private void Logging() {
//
//        try {
//            Path logFile = FileSystems.getDefault().getPath("/home/lvuser/logs", "PDP_Log.csv");
//            System.out.println("-------  Logging: path: " + logFile.toString() + " : " + logFile.toAbsolutePath().toString());
//            if (Files.notExists(logFile)) {
//                logFile = Files.createFile(logFile);
//                List<String> line = new ArrayList<String>();
//                line.add("time, DriveLeft, DriveRight, Elev, Arm");
//                Files.write(logFile, line, StandardCharsets.UTF_8);
//            }
//
//            double elvCurrent = mPDP.getCurrent(RobotConfig.ELEVATOR_TALON_PDP);
//            double armCurrent = mPDP.getCurrent(RobotConfig.ARM_TALON_PDP);
//            double driveLeftCurrent = mPDP.getCurrent(RobotConfig.DRIVE_LEFT_MASTER_PDP) + mPDP.getCurrent(RobotConfig.DRIVE_LEFT_SLAVE0_PDP);
//            double driveRightCurrent = mPDP.getCurrent(RobotConfig.DRIVE_RIGHT_MASTER_PDP) + mPDP.getCurrent(RobotConfig.DRIVE_RIGHT_SLAVE0_PDP);
//            //SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
//            Date now = new Date();
//            List<String> line = new ArrayList<String>();
//            String data = String.format("%tT, %f, %f, %f, %f", now, driveLeftCurrent, driveRightCurrent, elvCurrent, armCurrent);
//            line.add("time, DriveLeft, DriveRight, Elev, Arm");
//            Files.write(logFile, line, StandardCharsets.UTF_8);
//
//            SmartDashboard.putNumber("PDP.ElevCurrent", elvCurrent);
//            SmartDashboard.putNumber("PDP.ArmElevCurrent", armCurrent);
//            SmartDashboard.putNumber("PDP.DriveLeftCurrent", driveLeftCurrent);
//            SmartDashboard.putNumber("PDP.DriveRightCurrent", driveRightCurrent);
//        }
//        catch (Exception ex) {
//            System.out.println( "*******  Logging ex: "+ ex.getClass().getName() + "   msg: " + ex.getMessage() + " ");
//        }
//    }



    
    //  AUTONOMOUS MODE  ---------------------------------------------------------------


    private AutoDriveSys mAutoDriveSys;

    @Override
    public void autonomousInit() {
        super.autonomousInit();
        System.out.println("auto init (6072)  ------------------------------------------------------------");
        mAutoDriveSys = AutoDriveSys.getInstance();
        mAutoDriveSys.prepareSystem();

        //AutoDriveSys driveSys = AutoDriveSys.getInstance();
        /*TestDriveForward cmd = new TestDriveForward();
        Scheduler.getInstance().removeAll();
        Scheduler.getInstance().add(cmd);*/
    }

    @Override
    public void autonomousPeriodic() {
        mAutoDriveSys.advanceTrajectory();
    }



    //  TEST MODE  ---------------------------------------------------------------

    private int mCounter;

    private TestDriveForward mTestFwdCmd;
    private TestDriveGyro mTestDriveGyro;


    @Override
    public void testInit() {
        System.out.println("testInit: --------------------");
        mCounter = 0;
//        mArcadeDriveCmd = new ArcadeDriveCmd(mControlBoard.drive_stick);
//        Scheduler.getInstance().removeAll();
//        Scheduler.getInstance().add(mArcadeDriveCmd);
    }

    @Override public void testPeriodic() {
        Scheduler.getInstance().run();
        /*if (mCounter < 500) {
            mCounter++;
            mDriveSys.arcadeDrive(-0.4,0);

        }
        if (mCounter%5==0 && mCounter<500) {
            System.out.println(ControlBoard.getInstance().drive_stick.getY() + "       " + ControlBoard.getInstance().drive_stick.getZ());
            SmartDashboard.putNumber("Counter: ", mCounter);
        }*/
        /*if (mCounter==500) {

            disabledPeriodic();
        }*/

    }




}