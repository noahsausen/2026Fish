package org.firstinspires.ftc.teamcode.tuners;

import com.bylazar.gamepad.PanelsGamepad;
import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp (name = "Panels Gamepad Test", group = "2-Tuner")
public class PanelsGamepadTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new JoinedTelemetry(telemetry, PanelsTelemetry.INSTANCE.getFtcTelemetry());
        
        Gamepad panels1 = PanelsGamepad.INSTANCE.getFirstManager().getAsFTCGamepad();
        
        waitForStart();
        
        while (opModeIsActive()) {
            telemetry.addData("Cross", panels1.cross);
            telemetry.addData("Cross Pressed", panels1.crossWasPressed());
            telemetry.addData("Right X", panels1.right_stick_x);
            telemetry.update();
        }
    }
}