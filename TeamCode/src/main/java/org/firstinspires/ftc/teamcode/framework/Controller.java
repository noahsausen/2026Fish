package org.firstinspires.ftc.teamcode.framework;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.Gamepad;

@Configurable
public class Controller {
    public static double STICK_DEADBAND = 0.02;
    private final Gamepad gamepad1;
    private final Gamepad gamepad2;
    
    public Controller(Gamepad gamepad1, Gamepad gamepad2) {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }
    
    // Button Mapping
    
    public double moveX() {
        return applyDeadband(gamepad1.left_stick_x + gamepad2.left_stick_x);
    }
    public double moveY() {
        return applyDeadband(-(gamepad1.left_stick_y + gamepad2.left_stick_y));
    }
    public double turnX() {
        return applyDeadband(gamepad1.right_stick_x + gamepad2.right_stick_x);
    }
    
    public boolean lowPrecision() {
        return gamepad1.left_bumper || gamepad2.left_bumper;
    }
    public boolean highPrecision() {
        return gamepad1.right_bumper || gamepad2.right_bumper;
    }
    public boolean driveModePressed() {
        return gamepad1.psWasPressed() || gamepad2.psWasPressed();
    }
    
    public double intake() {
        return applyDeadband(gamepad1.right_trigger + gamepad2.right_trigger);
    }
    public double intakeReversed() {
        return applyDeadband(gamepad1.left_trigger + gamepad2.left_trigger);
    }
    
    public void rumble(int durationMs){
        this.gamepad1.rumble(durationMs);
        this.gamepad2.rumble(durationMs);
    }
    
    public void megaRumble() {
        Gamepad.RumbleEffect megaEffect = new Gamepad.RumbleEffect.Builder()
                .addStep(1, 0, 250)
                .addStep(0, 0, 250)
                .addStep(0, 1, 250)
                .addStep(0, 0, 250)
                .addStep(1, 0, 250)
                .addStep(0, 0, 250)
                .addStep(0, 1, 250)
                .addStep(0, 0, 250)
                .addStep(1, 1, 500)
                .build();
        gamepad1.runRumbleEffect(megaEffect);
        gamepad2.runRumbleEffect(megaEffect);
    }
    
    // linear rescaled deadband: lowers inputs to start at 0 and scales up to reach 1
    private double applyDeadband(double stick) {
        if (Math.abs(stick) > STICK_DEADBAND) {
            double loweredStick = Math.abs(stick) - STICK_DEADBAND;
            double rangeAfterDeadband = 1.0 - STICK_DEADBAND;
            // divide the lowered stick by the range remaining to stretch it back to 0 - 1
            return Math.copySign((loweredStick / rangeAfterDeadband), stick); // finish by copying the sign
        } else {
            return 0;
        }
    }
}