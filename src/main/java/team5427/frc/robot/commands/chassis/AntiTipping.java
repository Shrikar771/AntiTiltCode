package team5427.frc.robot.commands.chassis;


import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import java.util.function.Supplier;


public class AntiTipping {
  private final Supplier<Double> pitchSupplier;
  private final Supplier<Double> rollSupplier;

  private double tippingThresholdDegrees;
  private double maxCorrectionSpeed; 
  private double kP; 

  private double pitch = 0.0;
  private double roll = 0.0;
  private double correctionSpeed = 0.0;
  private double inclinationMagnitude = 0.0;
  private double yawDirectionDeg = 0.0;
  private boolean isTipping = false;
  private Rotation2d tiltDirection = new Rotation2d();
  private ChassisSpeeds speeds = new ChassisSpeeds();

  
  public AntiTipping(
      Supplier<Double> pitchSupplier,
      Supplier<Double> rollSupplier,
      double kP,
      double tippingThresholdDegrees,
      double maxCorrectionSpeed) {

    this.pitchSupplier = pitchSupplier;
    this.rollSupplier = rollSupplier;
    this.kP = kP;
    this.tippingThresholdDegrees = tippingThresholdDegrees;
    this.maxCorrectionSpeed = maxCorrectionSpeed;
  }

  public void setTippingThreshold(double degrees) {
    this.tippingThresholdDegrees = degrees;
  }

  public void setMaxCorrectionSpeed(double speedMetersPerSecond) {
    this.maxCorrectionSpeed = speedMetersPerSecond;
  }

 
  public void calculate() {
    pitch = pitchSupplier.get();
    roll = rollSupplier.get();

    isTipping = Math.abs(pitch) > tippingThresholdDegrees || Math.abs(roll) > tippingThresholdDegrees;

    tiltDirection = new Rotation2d(Math.atan2(-roll, -pitch));
    yawDirectionDeg = tiltDirection.getDegrees();

    inclinationMagnitude = Math.hypot(pitch, roll);

    correctionSpeed = kP * -inclinationMagnitude;
    correctionSpeed = MathUtil.clamp(correctionSpeed, -maxCorrectionSpeed, maxCorrectionSpeed);

    Translation2d correctionVector =
        new Translation2d(0, 1).rotateBy(tiltDirection).times(correctionSpeed);

    speeds = new ChassisSpeeds(correctionVector.getX(), -correctionVector.getY(), 0);
  }

  public double getPitch() {
    return pitch;
  }

  public double getRoll() {
    return roll;
  }

  public double getLastInclinationMagnitude() {
    return inclinationMagnitude;
  }

  public double getLastYawDirectionDeg() {
    return yawDirectionDeg;
  }

  public boolean isTipping() {
    return isTipping;
  }

  public ChassisSpeeds getVelocityAntiTipping() {
    return speeds;
  }

  public Rotation2d getLastTiltDirection() {
    return tiltDirection;
  }
}