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
    private boolean fieldCentric = false;
    private void updateDrive() {
        if (controller.driveModePressed()) {
            fieldCentric = !fieldCentric;
        }
        
        if (fieldCentric) {
            drivetrain.setMotorSpeeds(getPrecisionMultiplier(), calcFieldCentricDrive());
        } else {
            drivetrain.setMotorSpeeds(getPrecisionMultiplier(), calcMecanumDrive());
        }
    }
    
    private double[] calcMecanumDrive() {
        double r = controller.turnX();
        double x = controller.moveX() * STRAFE_ADJUSTMENT_FACTOR;
        double y = controller.moveY();
        
        telemetry.addLine("Drivetrain:");
        telemetry.addData("\tDrive X", x);
        telemetry.addData("\tDrive Y", y);
        telemetry.addData("\tDrive R", r);
        telemetry.addData("\tPrecision", getPrecisionMultiplier());
        
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(r), 1);
        
        return new double[] {
                (y + x + r)/denominator,
                (y - x + r)/denominator,
                (y - x - r)/denominator,
                (y + x - r)/denominator
        };
    }
    
    private double[] calcFieldCentricDrive() {
        double botHeading = imuSensor.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        
        double r = controller.turnX();
        double x = controller.moveX() * STRAFE_ADJUSTMENT_FACTOR;
        double y = controller.moveY();
        
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);
        
        telemetry.addLine("Drivetrain (Field Centric):");
        telemetry.addData("\tDrive X", x);
        telemetry.addData("\tDrive Y", y);
        telemetry.addData("\tDrive R", r);
        telemetry.addData("\tHeading", Math.toDegrees(botHeading));
        telemetry.addData("\tRotated X", rotX);
        telemetry.addData("\tRotated Y", rotY);
        telemetry.addData("\tPrecision", getPrecisionMultiplier());
        
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(r), 1);
        
        return new double[] {
                (rotY + rotX + r) / denominator,
                (rotY - rotX + r) / denominator,
                (rotY - rotX - r) / denominator,
                (rotY + rotX - r) / denominator
        };
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