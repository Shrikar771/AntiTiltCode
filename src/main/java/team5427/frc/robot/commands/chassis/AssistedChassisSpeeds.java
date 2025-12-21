package team5427.frc.robot.commands.chassis;

import java.util.function.Supplier;

import com.ctre.phoenix6.sim.ChassisReference;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import team5427.frc.robot.subsystems.Swerve.SwerveConstants;
import team5427.frc.robot.subsystems.Swerve.SwerveSubsystem;
import team5427.frc.robot.subsystems.Swerve.gyro.GyroIOPigeon;

public class AssistedChassisSpeeds extends Command{
    private SwerveSubsystem swerveSubsystem;
    private Supplier<Double> pitch;
    private Supplier<Double> roll;
    private GyroIOPigeon gyro;
    private Rotation3d yprRot3D;
    private boolean isFinished = false;
    private AntiTipping antiTipping;
    public AssistedChassisSpeeds(){
        yprRot3D = swerveSubsystem.get3DPos();
        pitch =() -> yprRot3D.getY();
        roll = () -> yprRot3D.getZ();
        antiTipping = new AntiTipping(pitch, roll, SwerveConstants.kpAntiTilt, SwerveConstants.tippingThresholdDegrees, SwerveConstants.maxCorrectionSpeedAssist);


    }
    public void initialize(){
        antiTipping.calculate();
    }
    public void execute(){
        if(antiTipping.isTipping()){
            ChassisSpeeds assistanceSpeeds = antiTipping.getVelocityAntiTipping();
            swerveSubsystem.setInputSpeeds(assistanceSpeeds);
        }
        else{
            swerveSubsystem.setInputSpeeds(new ChassisSpeeds(0, 0, 0));;
        }
        end();
    }

    public void end(){
        isFinished = true;
    }
    public boolean isFinished(){
        return isFinished;
    }

    


}
