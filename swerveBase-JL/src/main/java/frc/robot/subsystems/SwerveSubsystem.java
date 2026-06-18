package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import java.io.File;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.ctre.phoenix6.hardware.CANcoder;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import swervelib.parser.SwerveParser;
import swervelib.SwerveDrive;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;

public class SwerveSubsystem extends SubsystemBase {

  File directory = new File(Filesystem.getDeployDirectory(), "swerve");
  private static final Pose2d bluePose = new Pose2d(3.560, 4.035, new Rotation2d(0));
  private static final Pose2d redPose  = new Pose2d(12.977, 4.035, new Rotation2d(Math.toRadians(0)));
  SwerveDrive swerveDrive;
  CANcoder frontleft = new CANcoder(11);
  CANcoder frontright = new CANcoder(12);
  CANcoder backleft = new CANcoder(14);
  CANcoder backright = new CANcoder(13);
  PigeonIMU pigeonIMU = new PigeonIMU(20);

  public SwerveSubsystem() {

    try {
      swerveDrive = new SwerveParser(directory).createSwerveDrive(Constants.MAX_SPEED);
      this.swerveDrive.setModuleEncoderAutoSynchronize(true, 1);
          
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    swerveDrive.setGyro(new Rotation3d(0, 0, 0));
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
  
  public double getHeading() {
    return pigeonIMU.getYaw();
  }
  public Pose2d getPose() {
    return swerveDrive.getPose();
  }
  public void resetOdometry(Pose2d pose) {
    swerveDrive.resetOdometry(pose);
  }

  public void updatePigeonRotation() {
    swerveDrive.setGyro(new Rotation3d(pigeonIMU.getRoll(), pigeonIMU.getPitch(), pigeonIMU.getYaw()));
  }

  public Command resetGyroPigeon() {
    return new InstantCommand(() -> {
      swerveDrive.setGyro(new Rotation3d(0, 0, 0));
      pigeonIMU.setYaw(0);
    });
  }

  public Command resetGyroNavx() {
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

  public double getCancoderDegrees(CANcoder cancoder) {
    double rotations = cancoder.getAbsolutePosition().getValueAsDouble();
    return rotations * 360.0;
  }

  public Command resetPoseByAlliance(){
    return new InstantCommand(() -> {
      var alliance = DriverStation.getAlliance();
      Pose2d inicialPose = (alliance.isPresent() && alliance.get() == DriverStation.Alliance.Blue)
      ? bluePose
      : redPose;
      this.resetOdometry(inicialPose);
    });
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
