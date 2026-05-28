package org.firstinspires.ftc.teamcode;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import org.firstinspires.ftc.teamcode.framework.BaseOpMode;

@Configurable
@TeleOp(name = "JellyTele", group = "1-OpMode")
public class JellyTele extends BaseOpMode {
    
    // Drivetrain Constants ↓
    public static double PRECISION_MULTIPLIER_LOW = 0.35;
    public static double PRECISION_MULTIPLIER_HIGH = 0.2;
    public static double DEADBAND_VALUE = 0.02;
    public static double STRAFE_ADJUSTMENT_FACTOR = 1.08;
    private boolean alertedEndgame = false;
    
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware(false);
        while (opModeInInit()) {
            initFinishedTelemetry();
            timingTelemetry();
            telemetry.update();
        }
        runtime.reset();
        
        while (opModeIsActive()) {
            updateDrive();
            timingTelemetry();
            telemetry.update();
        }
        stopHardware();
    }
    
    
    // ↓ -------------- ↓ -------------- ↓ DRIVETRAIN ↓ -------------- ↓ -------------- ↓
    private enum DriveMode
    {
        MECANUM,
        FIELDCENTRIC
    }
    private DriveMode driveMode = DriveMode.MECANUM;
    private void updateDrive() {
        if (controller.driveModePressed()) {
            if (driveMode == DriveMode.MECANUM) {
                driveMode = DriveMode.FIELDCENTRIC;
            } else {
                driveMode = DriveMode.MECANUM;
            }
        }
        
        double[] motorSpeeds = null;
        switch (driveMode)
        {
            case MECANUM:
                motorSpeeds = calcMecanumDrive();
                break;
            case FIELDCENTRIC:
                motorSpeeds = calcFieldCentricDrive();
                break;
        }
        drivetrain.setMotorSpeeds(getPrecisionMultiplier(), motorSpeeds);
    }
    
    private double[] calcMecanumDrive() {
        double r = applyDeadband(controller.turnX());
        double x = applyDeadband(controller.moveX()) * STRAFE_ADJUSTMENT_FACTOR;
        double y = applyDeadband(controller.moveY());
        
        telemetry.addLine("Drivetrain:");
        telemetry.addData("\tDriveX", x);
        telemetry.addData("\tDriveY", y);
        telemetry.addData("\tDriveR", r);
        
        double sum = ((Math.abs(y))+(Math.abs(x))+(Math.abs(r)));
        double denominator = Math.max(sum, 1);
        
        return new double[] {
                (y + x + r)/denominator,
                (y - x + r)/denominator,
                (y - x - r)/denominator,
                (y + x - r)/denominator
        };
    }
    
    private double[] calcFieldCentricDrive() {
        double botHeading = imuSensor.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        
        double r = applyDeadband(controller.turnX());
        double x = applyDeadband(controller.moveX()) * STRAFE_ADJUSTMENT_FACTOR;
        double y = applyDeadband(controller.moveY());
        
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);
        
        telemetry.addLine("Drivetrain (Field Centric):");
        telemetry.addData("\tDriveX", x);
        telemetry.addData("\tDriveY", y);
        telemetry.addData("\tDriveR", r);
        telemetry.addData("\tBotHeading", (botHeading/Math.PI*180));
        telemetry.addData("\tDriveRotX", rotX);
        telemetry.addData("\tDriveRotY", rotY);
        
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(r), 1);
        
        return new double[] {
                (rotY + rotX + r) / denominator,
                (rotY - rotX + r) / denominator,
                (rotY - rotX - r) / denominator,
                (rotY + rotX - r) / denominator
        };
    }
    
    // linear rescaled deadband: lowers inputs to start at 0 and scales up to reach 1
    private double applyDeadband(double stick) {
        if (Math.abs(stick) > DEADBAND_VALUE) {
            double loweredStick = Math.abs(stick) - DEADBAND_VALUE;
            double rangeAfterDeadband = 1.0 - DEADBAND_VALUE;
            // divide the lowered stick by the range remaining to stretch it back to 0 - 1
            return Math.copySign((loweredStick / rangeAfterDeadband), stick); // finish by copying the sign
        } else {
            return 0;
        }
    }
    
    private double getPrecisionMultiplier() {
        if (controller.highPrecision()) {
            return PRECISION_MULTIPLIER_HIGH;
        } else if (controller.lowPrecision()) {
            return PRECISION_MULTIPLIER_LOW;
        }
        return 1;
    }
    
}