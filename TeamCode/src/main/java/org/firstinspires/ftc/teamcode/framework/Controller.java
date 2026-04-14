package org.firstinspires.ftc.teamcode.framework;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;

//import org.firstinspires.ftc.teamcode.JellyTele;

@Config
public class Controller {
    
    private final Gamepad gamepad1;
    private final Gamepad gamepad2;
    
    public Controller(Gamepad gamepad1, Gamepad gamepad2) {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }
    
    // Button Mapping -- TODO: update
    
    public double moveX() { return gamepad1.left_stick_x; }
    public double moveY() { return -gamepad1.left_stick_y; }
    public double turnX() { return gamepad1.right_stick_x; }
    
    public boolean lowPrecision() { return gamepad1.left_bumper; }
    public boolean highPrecision() { return gamepad1.right_bumper; }
    public boolean driveModePressed() { return gamepad1.psWasPressed(); }
    
    public boolean intake() { return gamepad1.cross; }
    public boolean outtake() { return gamepad1.circle; }
    
    
    
//    public void rumble(double rumble1, double rumble2, int durationMs, boolean rumbleSecondary){
//        if(rumbleSecondary){
//            this.gamepad2.rumble(rumble1,rumble2,durationMs);
//        }else{
//            this.gamepad1.rumble(rumble1,rumble2,durationMs);
//        }
//
//    }
    
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
    
}