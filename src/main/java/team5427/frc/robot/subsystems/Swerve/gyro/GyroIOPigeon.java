package team5427.frc.robot.subsystems.Swerve.gyro;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.MountPoseConfigs;
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;

import java.lang.ref.PhantomReference;
import java.util.Queue;
import java.util.concurrent.Phaser;

import team5427.frc.robot.Constants;
import team5427.frc.robot.subsystems.Swerve.SwerveConstants;
import team5427.frc.robot.subsystems.Swerve.io.talon.PhoenixOdometryThread;

public class GyroIOPigeon implements GyroIO {
  private Pigeon2 gyro;
  private final StatusSignal<Angle> yaw;
  private final StatusSignal<Angle> roll;
  private final StatusSignal<Angle> pitch;

  

  private final StatusSignal<AngularVelocity> yawVelocity;
  private final StatusSignal<AngularVelocity> rollVelocity;
  private final StatusSignal<AngularVelocity> pitchVelocity;
  private final Queue<Double> yawPositionQueue;
  private final Queue<Double> rollPositionQueue;
  private final Queue<Double> pitchPositionQueue;

  private final Queue<Double> yawTimestampQueue;
  private final Queue<Double> pitchTimestampQueue;
  private final Queue<Double> rollTimestampQueue;


  public GyroIOPigeon() {
    gyro =
        new Pigeon2(
            SwerveConstants.kPigeonCANId.getDeviceNumber(), SwerveConstants.kPigeonCANId.getBus());
    gyro.reset();
    
    Pigeon2Configuration config = new Pigeon2Configuration();
    config.MountPose = new MountPoseConfigs();
    config.FutureProofConfigs = true;
    gyro.getConfigurator().apply(config);
    gyro.getConfigurator().setYaw(0.0);
    
    yaw = gyro.getYaw();
    yawVelocity = gyro.getAngularVelocityZWorld();
    pitch = gyro.getPitch();
    pitchVelocity = gyro.getAngularVelocityYWorld();
    roll = gyro.getRoll();
    rollVelocity = gyro.getAngularVelocityXWorld();

    BaseStatusSignal.setUpdateFrequencyForAll(Constants.kOdometryFrequency, yawVelocity, yaw, pitchVelocity, pitch, rollVelocity, roll);
    gyro.optimizeBusUtilization();
    yawTimestampQueue = PhoenixOdometryThread.getInstance().makeTimestampQueue();
    yawPositionQueue = PhoenixOdometryThread.getInstance().registerSignal(gyro.getYaw());
    pitchTimestampQueue = PhoenixOdometryThread.getInstance().makeTimestampQueue();
    pitchPositionQueue = PhoenixOdometryThread.getInstance().registerSignal(gyro.getPitch());
    rollTimestampQueue = PhoenixOdometryThread.getInstance().makeTimestampQueue();
    rollPositionQueue = PhoenixOdometryThread.getInstance().registerSignal(gyro.getRoll());


  }

  @Override
  public void updateInputs(GyroIOInputs inputs) {
    BaseStatusSignal.refreshAll(yaw, yawVelocity, pitch, pitchVelocity, roll, rollVelocity).equals(StatusCode.OK);

    inputs.connected = gyro.isConnected();
    inputs.yawPosition =
        Rotation2d.fromDegrees(
            BaseStatusSignal.getLatencyCompensatedValueAsDouble(yaw, yawVelocity));
    inputs.rollPosition = Rotation2d.fromDegrees(BaseStatusSignal.getLatencyCompensatedValueAsDouble(roll, rollVelocity));
    inputs.pitchPosition = Rotation2d.fromDegrees(BaseStatusSignal.getLatencyCompensatedValueAsDouble(pitch, pitchVelocity));

    inputs.yawVelocityRadPerSec = Units.degreesToRadians(yawVelocity.getValueAsDouble());
    inputs.pitchVelocityRadPerSec = Units.degreesToRadians(pitchVelocity.getValueAsDouble());
    inputs.rollVelocityRadPerSec = Units.degreesToRadians(rollVelocity.getValueAsDouble());



    inputs.odometryYawTimestamps =
        yawTimestampQueue.stream().mapToDouble((Double value) -> value).toArray();
    inputs.odometryYawPositions =
        yawPositionQueue.stream()
            .map((Double value) -> Rotation2d.fromDegrees(value))
            .toArray(Rotation2d[]::new);
    inputs.odometryPitchTimestamps = 
        pitchTimestampQueue.stream().mapToDouble((Double value) -> value).toArray();
    inputs.odometryPitchPositions = 
        pitchPositionQueue.stream().map((Double value)->Rotation2d.fromDegrees(value)).toArray(Rotation2d[]::new);
    inputs.odometryRollTimestamps = rollTimestampQueue.stream().mapToDouble((Double value)-> value).toArray();
    inputs.odometryRollPositions = rollPositionQueue.stream().map((Double value)->Rotation2d.fromDegrees(value)).toArray(Rotation2d[]::new);
    yawTimestampQueue.clear();
    yawPositionQueue.clear();
    rollTimestampQueue.clear();
    rollPositionQueue.clear();
    pitchTimestampQueue.clear();
    pitchPositionQueue.clear();
  }

  @Override
  public void resetGyroYawAngle(Rotation2d angle) {
    gyro.setYaw(angle.getDegrees());
  }
  pub

  
}
