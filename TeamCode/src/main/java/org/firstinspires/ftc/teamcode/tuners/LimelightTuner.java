package org.firstinspires.ftc.teamcode.tuners;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.framework.hardware.Vision;

@Configurable
@TeleOp (name = "Limelight Tuner", group = "2-Tuner")
public class LimelightTuner extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new JoinedTelemetry(telemetry, PanelsTelemetry.INSTANCE.getFtcTelemetry());
        Vision vision = new Vision(hardwareMap.get(Limelight3A.class, "limelight"), this);
        
        waitForStart();
        while (opModeIsActive()) {
            LLStatus status = vision.getStatus();
            if (status != null) {
                telemetry.addData("Actual FPS", status.getFps());
            }
            LLResult result = vision.getValidResult();
            if (result != null) {
                telemetry.addData("Target X", result.getTx());
                telemetry.addData("Target Y", result.getTy());
                telemetry.addData("Target Area", result.getTa());
            }
            telemetry.update();
        }
    }
}