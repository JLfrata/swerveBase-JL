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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import swervelib.parser.SwerveParser;
import swervelib.SwerveDrive;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;

public class SwerveSubsystem extends SubsystemBase {

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
    swerveDrive.setGyroOffset(new Rotation3d(0, 0, Units.radiansToDegrees(0)));
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

  // public double getCancoderRotations(CANcoder cancoder){
  //   return cancoder.getAbsolutePosition().getValueAsDouble();
  // }

  public double getCancoderDegrees(CANcoder cancoder) {
    double rotations = cancoder.getAbsolutePosition().getValueAsDouble();
    return rotations * 360.0;
}

  @Override
  public void periodic() {
    if(swerveDrive != null){
      SmartDashboard.putNumber("FR CanCoder", getCancoderDegrees(frontright));
      SmartDashboard.putNumber("FL CanCoder", getCancoderDegrees(frontleft));
      SmartDashboard.putNumber("BR CanCoder", getCancoderDegrees(backright));
      SmartDashboard.putNumber("BL CanCoder", getCancoderDegrees(backleft));
          SmartDashboard.putNumber("Swerve/Encoder Absoluto FL",

        swerveDrive.getModules()[0].getAbsolutePosition()); // Front Left
        
    SmartDashboard.putNumber("Swerve/Encoder Absoluto FR", 
        swerveDrive.getModules()[1].getAbsolutePosition()); // Front Right
        
    SmartDashboard.putNumber("Swerve/Encoder Absoluto BL", 
        swerveDrive.getModules()[2].getAbsolutePosition()); // Back Left
        
    SmartDashboard.putNumber("Swerve/Encoder Absoluto BR", 
        swerveDrive.getModules()[3].getAbsolutePosition()); // Back Right
    SmartDashboard.putNumber("X: ", swerveDrive.getPose().getX());
    SmartDashboard.putNumber("Y: ", swerveDrive.getPose().getY());
      }
  }

  @Override
  public void simulationPeriodic() {
  }

}
