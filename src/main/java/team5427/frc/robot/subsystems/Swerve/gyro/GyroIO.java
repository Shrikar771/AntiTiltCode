package team5427.frc.robot.subsystems.Swerve.gyro;

import edu.wpi.first.math.geometry.Rotation2d;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.littletonrobotics.junction.AutoLog;

public interface GyroIO {
  @AutoLog
  public static class GyroIOInputs {
    public boolean connected = false;
    public Rotation2d yawPosition = new Rotation2d();
    public Rotation2d rollPosition = new Rotation2d();
    public Rotation2d pitchPosition = new Rotation2d();

    public double[] odometryYawTimestamps = new double[0];
    public Rotation2d[] odometryYawPositions = new Rotation2d[] {};
    public double yawVelocityRadPerSec = 0.0;

    public double[] odometryRollTimestamps = new double[0];
    public Rotation2d[] odometryRollPositions = new Rotation2d[]{};
    public double rollVelocityRadPerSec = 0.0;

    public double[] odometryPitchTimestamps = new double[0];
    public Rotation2d[] odometryPitchPositions = new Rotation2d[]{};
    public double pitchVelocityRadPerSec = 0.0;

  }

  public default void updateInputs(GyroIOInputs inputs) {}

  public default void resetGyroYawAngle(Rotation2d angle) {}

  
}
