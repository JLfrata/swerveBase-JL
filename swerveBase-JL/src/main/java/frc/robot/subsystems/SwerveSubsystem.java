package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import static edu.wpi.first.units.Units.Meter;

import java.io.File;

import com.ctre.phoenix6.hardware.CANcoder;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.Filesystem;
import swervelib.parser.SwerveParser;
import swervelib.SwerveDrive;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;

public class SwerveSubsystem extends SubsystemBase {

  double maximumSpeed = Units.feetToMeters(4.5);
  File directory = new File(Filesystem.getDeployDirectory(), "swerve");
  SwerveDrive swerveDrive;
  CANcoder frontleft = new CANcoder(11);
  CANcoder frontright = new CANcoder(12);
  CANcoder backleft = new CANcoder(14);
  CANcoder backright = new CANcoder(13);

  public SwerveSubsystem() {

    try {
      swerveDrive = new SwerveParser(directory).createSwerveDrive(Constants.MAX_SPEED, new Pose2d(
          new Translation2d(Meter.of(1),
              Meter.of(4)),
          Rotation2d.fromDegrees(0)));

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  public Command driveTeleOp(DoubleSupplier translationX, DoubleSupplier translationY,
      DoubleSupplier angularRotationX) {
    return run(() -> {
      swerveDrive.drive(new Translation2d(translationX.getAsDouble() * swerveDrive.getMaximumChassisVelocity(),
          translationY.getAsDouble() * swerveDrive.getMaximumChassisVelocity()),
          angularRotationX.getAsDouble() * swerveDrive.getMaximumChassisAngularVelocity(),
          true,
          false);
    });
  }

  public Command resetGyro() {
    return new InstantCommand(() -> {
      swerveDrive.setGyro(new Rotation3d(0, 0, 0));
    });
  }

  public double getYawDegrees(){
    return swerveDrive.getYaw().getDegrees();
  }

  public SwerveDrive getSwerveDrive() {
    return swerveDrive;
  }

  public boolean exampleCondition() {
    return false;
  }

  @Override
  public void periodic() {
    if(swerveDrive != null){
      
    }
  }

  @Override
  public void simulationPeriodic() {
  }

}
