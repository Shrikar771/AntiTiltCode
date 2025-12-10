package team5427.frc.robot.commands.assistance;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.wpilibj2.command.Command;
import team5427.frc.robot.subsystems.Swerve.SwerveConstants;
import team5427.frc.robot.subsystems.Swerve.SwerveSubsystem;

public class AntiTipping extends Command {
    private SwerveSubsystem swerveSubsystem;
    private Rotation3d rpyTrackRot3d;
    private double rollPos;
    private double yawPos;
    private double pitchPos;
    

    
    public void initialize(){
        rpyTrackRot3d = SwerveSubsystem.get3DPos();
        rollPos = rpyTrackRot3d.getX();
        pitchPos = rpyTrackRot3d.getY();
        yawPos = rpyTrackRot3d.getZ();
        
        
    }
    public void execute(){
        if(rollPos - SwerveConstants.rollPos = )

    }
    public void end(){

    }
    
}
