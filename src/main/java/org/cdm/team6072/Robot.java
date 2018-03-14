package org.cdm.team6072;

import java.nio.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.cdm.team6072.commands.drive.*;
import org.cdm.team6072.subsystems.*;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.PowerDistributionPanel;


public class Robot extends IterativeRobot {


    private DriveSys mDriveSys = DriveSys.getInstance() ;
    //private Navigator mNavx = Navigator.getInstance();
    private ElevatorSys mElevatorSys = ElevatorSys.getInstance();
    private IntakePneumaticsSys mPneuSys;
    private IntakeMotorSys mIntakeMotorSys = IntakeMotorSys.getInstance();

   // PowerDistributionPanel mPDP = new PowerDistributionPanel(RobotConfig.PDP_ID);

    // ControlBoard holds the operator interface code such as JoyStick
    private ControlBoard mControlBoard  = ControlBoard.getInstance();


    //private MotionProfileManager profile = new MotionProfileManager(DriveSys.getInstance().getmLeftMaster());

    //private MotionProfileManager profile = new MotionProfileManager(DriveTrain.getInstance().getmLeftMaster());
    private UsbCamera cam;


    private static AHRS mAhrs;

    /**
     * AHRS must be initialised in RobotInit()
     * @return
     */
    public static AHRS getAHRS() {
        return mAhrs;
    }


    @Override
    public void robotInit() {
        System.out.println("6072: robotInit");
        // AHRS MUST BE INIT in RobotInit or you get bad results
        //initAHRS();
//        CameraManager.getInstance().runCameras();
//        CameraManager.getInstance().runFilter();

        // must initialize nav system here for the navX-MXP
        NavSys.getInstance();
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



    private void sleep(int milliSecs) {
        try {
            Thread.sleep(milliSecs);
        } catch (Exception ex) {}
    }



    private void initAHRS() {
        try {
          /* Communicate w/navX-MXP via the MXP SPI Bus.                                     */
          /* Alternatively:  I2C.Port.kMXP, SerialPort.Port.kMXP or SerialPort.Port.kUSB     */
          /* See http://navx-mxp.kauailabs.com/guidance/selecting-an-interface/ for details. */
            mAhrs = new AHRS(SPI.Port.kMXP);
            mAhrs.reset();
            mAhrs.zeroYaw();
        } catch (RuntimeException ex ) {
            DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
        }
        while (mAhrs.isCalibrating()) {
            sleep(10);
        }
        System.out.println("Robot.initAHRS: YawAxis: " + mAhrs.getBoardYawAxis().board_axis.getValue()
                + "  firmware: " + mAhrs.getFirmwareVersion()
                + "  isConnected: " + mAhrs.isConnected()
        );
    }

    private void logNavX() {
        AHRS ahrs = mAhrs;
          /* Display 6-axis Processed Angle Data                                      */
        SmartDashboard.putBoolean(  "NavX/IMU_Connected",        ahrs.isConnected());
        SmartDashboard.putBoolean(  "NavX/IMU_IsCalibrating",    ahrs.isCalibrating());
        SmartDashboard.putNumber(   "NavX/IMU_Yaw",              ahrs.getYaw());
        SmartDashboard.putNumber(   "NavX/IMU_Pitch",            ahrs.getPitch());
        SmartDashboard.putNumber(   "NavX/IMU_Roll",             ahrs.getRoll());

          /* Display tilt-corrected, Magnetometer-based heading (requires             */
          /* magnetometer calibration to be useful)                                   */

        SmartDashboard.putNumber(   "NavX/IMU_CompassHeading",   ahrs.getCompassHeading());

          /* Display 9-axis Heading (requires magnetometer calibration to be useful)  */
        SmartDashboard.putNumber(   "NavX/IMU_FusedHeading",     ahrs.getFusedHeading());

          /* These functions are compatible w/the WPI Gyro Class, providing a simple  */
          /* path for upgrading from the Kit-of-Parts gyro to the navx-MXP            */

        SmartDashboard.putNumber(   "NavX/IMU_TotalYaw",         ahrs.getAngle());
        SmartDashboard.putNumber(   "NavX/IMU_YawRateDPS",       ahrs.getRate());

          /* Display Processed Acceleration Data (Linear Acceleration, Motion Detect) */

        SmartDashboard.putNumber(   "NavX/IMU_Accel_X",          ahrs.getWorldLinearAccelX());
        SmartDashboard.putNumber(   "NavX/IMU_Accel_Y",          ahrs.getWorldLinearAccelY());
        SmartDashboard.putBoolean(  "NavX/IMU_IsMoving",         ahrs.isMoving());
        SmartDashboard.putBoolean(  "NavX/IMU_IsRotating",       ahrs.isRotating());

          /* Display estimates of velocity/displacement.  Note that these values are  */
          /* not expected to be accurate enough for estimating robot position on a    */
          /* FIRST FRC Robotics Field, due to accelerometer noise and the compounding */
          /* of these errors due to single (velocity) integration and especially      */
          /* double (displacement) integration.                                       */

        SmartDashboard.putNumber(   "NavX/Velocity_X",           ahrs.getVelocityX());
        SmartDashboard.putNumber(   "NavX/Velocity_Y",           ahrs.getVelocityY());
        SmartDashboard.putNumber(   "NavX/Displacement_X",       ahrs.getDisplacementX());
        SmartDashboard.putNumber(   "NavX/Displacement_Y",       ahrs.getDisplacementY());

          /* Display Raw Gyro/Accelerometer/Magnetometer Values                       */
          /* NOTE:  These values are not normally necessary, but are made available   */
          /* for advanced users.  Before using this data, please consider whether     */
          /* the processed data (see above) will suit your needs.                     */

        SmartDashboard.putNumber(   "NavX/RawGyro_X",            ahrs.getRawGyroX());
        SmartDashboard.putNumber(   "NavX/RawGyro_Y",            ahrs.getRawGyroY());
        SmartDashboard.putNumber(   "NavX/RawGyro_Z",            ahrs.getRawGyroZ());
        SmartDashboard.putNumber(   "NavX/RawAccel_X",           ahrs.getRawAccelX());
        SmartDashboard.putNumber(   "NavX/RawAccel_Y",           ahrs.getRawAccelY());
        SmartDashboard.putNumber(   "NavX/RawAccel_Z",           ahrs.getRawAccelZ());
        SmartDashboard.putNumber(   "NavX/RawMag_X",             ahrs.getRawMagX());
        SmartDashboard.putNumber(   "NavX/RawMag_Y",             ahrs.getRawMagY());
        SmartDashboard.putNumber(   "NavX/RawMag_Z",             ahrs.getRawMagZ());
        SmartDashboard.putNumber(   "NavX/IMU_Temp_C",           ahrs.getTempC());

          /* Omnimount Yaw Axis Information                                           */
          /* For more info, see http://navx-mxp.kauailabs.com/installation/omnimount  */
        AHRS.BoardYawAxis yaw_axis = ahrs.getBoardYawAxis();
        SmartDashboard.putString(   "NavX/YawAxisDirection",     yaw_axis.up ? "Up" : "Down" );
        SmartDashboard.putNumber(   "NavX/YawAxis",              yaw_axis.board_axis.getValue() );

          /* Sensor Board Information                                                 */
        SmartDashboard.putString(   "NavX/FirmwareVersion",      ahrs.getFirmwareVersion());

          /* Quaternion Data                                                          */
          /* Quaternions are fascinating, and are the most compact representation of  */
          /* orientation data.  All of the Yaw, Pitch and Roll Values can be derived  */
          /* from the Quaternions.  If interested in motion processing, knowledge of  */
          /* Quaternions is highly recommended.                                       */
        SmartDashboard.putNumber(   "NavX/QuaternionW",          ahrs.getQuaternionW());
        SmartDashboard.putNumber(   "NavX/QuaternionX",          ahrs.getQuaternionX());
        SmartDashboard.putNumber(   "NavX/QuaternionY",          ahrs.getQuaternionY());
        SmartDashboard.putNumber(   "NavX/QuaternionZ",          ahrs.getQuaternionZ());

          /* Connectivity Debugging Support                                           */
        SmartDashboard.putNumber(   "NavX/IMU_Byte_Count",       ahrs.getByteCount());
        SmartDashboard.putNumber(   "NavX/IMU_Update_Count",     ahrs.getUpdateCount());
    }

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