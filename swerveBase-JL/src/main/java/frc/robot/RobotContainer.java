// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.subsystems.SwerveSubsystem;
import swervelib.SwerveInputStream;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class RobotContainer {

  private final SwerveSubsystem swerveBase = new SwerveSubsystem();
  private final PS4Controller controller = new PS4Controller(Constants.OperatorConstants.kDriverControllerPort);

  public RobotContainer() {
    configureBindings();
  }

  // SwerveInputStream driveAngularVelocity = SwerveInputStream.of(swerveBase.getSwerveDrive(),
  //     () -> controller.getLeftY() * -0.8,
  //     () -> controller.getLeftX() * -0.8)
  //     .withControllerRotationAxis(controller::getRightX)
  //     .deadband(0.05)
  //     .scaleTranslation(0.8)
  //     .allianceRelativeControl(true);

  // SwerveInputStream driveDirectAngle = driveAngularVelocity.copy().withControllerHeadingAxis(controller::getRightX,
  //     controller::getRightY)
  //     .headingWhile(true);
      
  //     Command driveFieldOrientedDirectAngle = swerveBase.driveFieldOriented(driveDirectAngle);
  //     Command driveFieldOrientedDirectAngle = swerveBase.driveFieldOriented(driveDirectAngle);
      
  private void configureBindings() {
    swerveBase.setDefaultCommand(swerveBase.driveTeleOp(
      controller::getLeftY,
      controller::getLeftX,
      controller::getRightX));
    new JoystickButton(controller, PS4Controller.Button.kSquare.value).onTrue(swerveBase.resetGyroPigeon()); 
    
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
