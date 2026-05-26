package org.firstinspires.ftc.teamcode.framework;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.framework.hardware.Drivetrain;
import org.firstinspires.ftc.teamcode.framework.hardware.Vision;

public abstract class BaseOpMode extends LinearOpMode {
    protected Drivetrain drivetrain;
    protected Controller controller;
    protected IMU imuSensor;
    protected ElapsedTime runtime;
    protected Vision vision;
    
    protected long prevLoopNanoTime = 0;
    
    // TODO: make sure nothing moves during auto → teleop transition
    protected void initHardware(boolean auto) {
        telemetry = new JoinedTelemetry(telemetry, PanelsTelemetry.INSTANCE.getFtcTelemetry()); // TODO: test joined telemetry
        
        if (!auto) {
            // Drivetrain Motors (SAME ORDER IN HARDWARE CONFIG)
            DcMotor[] driveMotors = {
                    hardwareMap.get(DcMotor.class, "driveFL"),
                    hardwareMap.get(DcMotor.class, "driveBL"),
                    hardwareMap.get(DcMotor.class, "driveFR"),
                    hardwareMap.get(DcMotor.class, "driveBR")};
            
            drivetrain = new Drivetrain(driveMotors);
            
            drivetrain.setMotorDirections(new DcMotor.Direction[] { // _TODO: check directions
                    DcMotor.Direction.REVERSE, // motorFL
                    DcMotor.Direction.REVERSE, // motorBL
                    DcMotor.Direction.FORWARD, // motorFR
                    DcMotor.Direction.FORWARD  // motorBR
            });
            
            // Anything else that (isn't used during / might conflict) with auto
            
            controller = new Controller(gamepad1,gamepad2);
            
            imuSensor = initializeIMU();
        }
        
        runtime = new ElapsedTime();
        
        // OTHER HARDWARE
        
        try {
            vision = new Vision(hardwareMap.get(Limelight3A.class, "limelight"), this);
        } catch (Exception ignored) {}
    }
    private IMU initializeIMU() {
        IMU imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot( // _TODO: check orientation
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        imu.initialize(parameters);
        imu.resetYaw();
        return imu;
    }
    
    // CALLING THIS IN AUTO WILL CURRENTLY THROW NullPointerException FOR DRIVETRAIN
    protected void stopHardware() {
        drivetrain.setMotorSpeeds(1, new double[]{0,0,0,0});
    }
    
    protected void timingTelemetry() {
        // loops per sec
        long currentNanoTime = System.nanoTime();
        long nanoPerLoop = currentNanoTime - prevLoopNanoTime;
        
        double loopsPerSec = 0;
        if (nanoPerLoop > 0) {
            loopsPerSec = 1e9 / nanoPerLoop;
        }
        
        telemetry.addLine("\nLoop Timing:");
        telemetry.addData("\tMillis", (nanoPerLoop / 1e6));
        telemetry.addData("\tHz", loopsPerSec);
        telemetry.addData("\tRuntime", (int) runtime.seconds());
        prevLoopNanoTime = currentNanoTime;
        
        // endgame alert -- TODO: enable?
//        if (matchTimer.seconds() >= 110 && !alertedEndgame) {
//            alertedEndgame = true;
//            controller.megaRumble();
//        }
    }
    
    protected void initFinishedTelemetry() {
        telemetry.addLine("Status: Init Finished ------------------------------------------");
        for (int i=0; i<16; i++) {
            telemetry.addLine("------------------------------------------------------------------------");
        }
        telemetry.update();
    }
}