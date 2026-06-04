package org.firstinspires.ftc.teamcode.framework.hardware;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotor;

@Configurable
public class Intake {
    private final DcMotor intakeMotor;
    public static double POWER = 1;
    
    public Intake(DcMotor motor) {
        intakeMotor = motor;
        intakeMotor.setDirection(DcMotor.Direction.FORWARD); // TODO: check direction
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    
    public void on() {
        intakeMotor.setPower(POWER);
    }
    
    public void off() {
        intakeMotor.setPower(0);
    }
    
    public void reverse() {
        intakeMotor.setPower(-POWER);
    }
    
    public double getPower() {
        return intakeMotor.getPower();
    }
}