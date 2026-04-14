package org.firstinspires.ftc.teamcode.framework;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import org.firstinspires.ftc.teamcode.framework.hardware.Drivetrain;

public abstract class BaseOpMode extends LinearOpMode {
    protected Drivetrain drivetrain;
    protected Controller controller;
    protected IMU imuSensor;
    protected ElapsedTime matchTimer;
    protected double imuOffset = 0;
    
    // TODO: make sure nothing moves during auto → teleop transition
    public void initHardware(boolean auto) { // TODO: need auto boolean? (to prevent movement)
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        
        // Drivetrain Motors (SAME ORDER IN HARDWARE CONFIG)
        
        DcMotor[] driveMotors = {
                hardwareMap.get(DcMotor.class, "motorFL"),
                hardwareMap.get(DcMotor.class, "motorBL"),
                hardwareMap.get(DcMotor.class, "motorFR"),
                hardwareMap.get(DcMotor.class, "motorBR")};
        
        drivetrain = new Drivetrain(driveMotors);
        
        drivetrain.setMotorDirections(new DcMotor.Direction[]{ // TODO: check directions
                DcMotor.Direction.REVERSE, // motorFL
                DcMotor.Direction.REVERSE, // motorBL
                DcMotor.Direction.FORWARD, // motorFR
                DcMotor.Direction.FORWARD  // motorBR
        });
        
        
        // OTHER HARDWARE
        
        // TODO: aux hardware classes & init here
        
        controller = new Controller(gamepad1,gamepad2);
        
        imuSensor = initializeIMUSensor();
        imuOffset = imuSensor.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        
        matchTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    }
    private IMU initializeIMUSensor()
    {
        IMU imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot( // TODO: check orientation
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        imu.initialize(parameters);
        return imu;
    }
    
    public void stopHardware() {
        drivetrain.setMotorSpeeds(1, new double[]{0,0,0,0});
    }
}