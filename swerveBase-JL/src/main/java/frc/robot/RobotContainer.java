// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.subsystems.SwerveSubsystem;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class RobotContainer {

  private final SwerveSubsystem swerveBase = new SwerveSubsystem();
  private final PS4Controller controller = new PS4Controller(Constants.OperatorConstants.kDriverControllerPort);

  public RobotContainer() {
    configureBindings();
  }
      
  private void configureBindings() {
    swerveBase.setDefaultCommand(swerveBase.driveTeleOp(
      controller::getLeftY,
      controller::getLeftX,
      controller::getRightX));
    new JoystickButton(controller, PS4Controller.Button.kSquare.value).onTrue(swerveBase.resetGyroPigeon()); 
    new JoystickButton(controller, PS4Controller.Button.kCircle.value).onTrue(swerveBase.resetPoseByAlliance());
    
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
