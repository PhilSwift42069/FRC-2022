// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.


//////////////////////////
/*imports*/

//default
package frc.robot;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.math.util.*;

//motors
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

//controllers
import edu.wpi.first.wpilibj.Joystick;

//encoders
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXSensorCollection;

//camera
import edu.wpi.first.cameraserver.CameraServer;

//other
import java.io.IOException;
import java.lang.Math;
import java.nio.file.Path;
import java.util.ResourceBundle.Control;
import java.util.concurrent.TimeUnit;

////////////////////////////

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  //Controllers
  private Joystick joystick;

  /*drive motors*/
  private WPI_TalonFX[] leftDrive = new WPI_TalonFX[2];
  private WPI_TalonFX[] rightDrive = new WPI_TalonFX[2];

  /////////////////////////

  //motor groups
  private MotorControllerGroup lsideDrive;
  private MotorControllerGroup rsideDrive;

  //drivetrain
  private DifferentialDrive chassis;

  //Motor IDs
  private final int RIGHT_DRIVE_BACK = 12;
  private final int RIGHT_DRIVE_FRONT = 13;
  private final int LEFT_DRIVE_BACK = 2;
  private final int LEFT_DRIVE_FRONT = 3;

  //JoyStickAxis
  private final int FOREWARD_BACKWARD_AXIS = 1;
  private final int LEFT_RIGHT_AXIS = 2;
  private final int DRIVING_SPEED = 3;
  

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    //camera
    CameraServer.startAutomaticCapture(); //open USB camera server

    /////////////
    //initiate variables
    /*controllers*/
    joystick = new Joystick(0);

    /*drivetrain*/
    leftDrive[0] = new WPI_TalonFX(LEFT_DRIVE_BACK);
    leftDrive[1] = new WPI_TalonFX(LEFT_DRIVE_FRONT);
    rightDrive[0] = new WPI_TalonFX(RIGHT_DRIVE_BACK);
    rightDrive[1] = new WPI_TalonFX(RIGHT_DRIVE_FRONT);
    leftDrive[0].configFactoryDefault();
    leftDrive[1].configFactoryDefault();
    rightDrive[0].configFactoryDefault();
    rightDrive[1].configFactoryDefault();
    leftDrive[0].setInverted(true);
    leftDrive[1].setInverted(true);
    lsideDrive = new MotorControllerGroup(leftDrive[0], leftDrive[1]);
    rsideDrive = new MotorControllerGroup(rightDrive[0], rightDrive[1]);
    chassis = new DifferentialDrive(lsideDrive, rsideDrive);
    ///////////////////////////////////////
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {

  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
   
  }

  //ROBOT CONTROL METHODS (USER DEFINED)
  //DEFINE DRIVE CODE
  private void drive(){//drives the robot
    double topSpeed = 1;
    System.out.println(joystick.getRawAxis(DRIVING_SPEED));
    chassis.arcadeDrive(-joystick.getRawAxis(FOREWARD_BACKWARD_AXIS) * topSpeed, joystick.getRawAxis(LEFT_RIGHT_AXIS) * 0.5); 
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    leftDrive[0].stopMotor();
    leftDrive[1].stopMotor();
    rightDrive[0].stopMotor();
    rightDrive[1].stopMotor();
    leftDrive[0].setNeutralMode(NeutralMode.Brake);
    leftDrive[1].setNeutralMode(NeutralMode.Brake);
    rightDrive[0].setNeutralMode(NeutralMode.Brake);
    rightDrive[1].setNeutralMode(NeutralMode.Brake);
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    drive();
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    leftDrive[0].stopMotor();
    leftDrive[1].stopMotor();
    rightDrive[0].stopMotor();
    rightDrive[1].stopMotor();
    leftDrive[0].setNeutralMode(NeutralMode.Coast);
    leftDrive[1].setNeutralMode(NeutralMode.Coast);
    rightDrive[0].setNeutralMode(NeutralMode.Coast);
    rightDrive[1].setNeutralMode(NeutralMode.Coast);
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}