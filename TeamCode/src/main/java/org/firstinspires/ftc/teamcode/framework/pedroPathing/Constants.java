package org.firstinspires.ftc.teamcode.framework.pedroPathing;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Configurable
public class Constants { // TODO: 4. other tuning after following todos
    public static FollowerConstants followerConstants = new FollowerConstants()
            .centripetalScaling(0);
    
    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .leftFrontMotorName("driveFL")
            .leftRearMotorName("driveBL")
            .rightFrontMotorName("driveFR")
            .rightRearMotorName("driveBR")
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD);
    
    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(0) // TODO: 3. offsets tuner
            .strafePodX(0)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinpoint") // TODO: 1. check: i2c port not 0, hardware map matches
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD) // TODO: 2. forward increases x,
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD); // left increases y
    
    /* DEFAULT REQUIREMENTS FOR PATHS TO BE CONSIDERED COMPLETE:
     * TValue: min fraction of path complete (0-1)
     * Velocity: max velocity (in/sec)
     * Translational: max translational error (in)
     * Heading: max heading error (radians)
     * Timeout: wait for correction at path end (millis)
     *
     * DECELERATION CONSTANTS (likely ignored with predictive braking):
     * Braking Strength: deceleration strength (> stronger)
     * Braking Start: how early deceleration starts (> earlier)
     */
    public static PathConstraints pathConstraints = new PathConstraints(
            0.97,
            0.1,
            0.1,
            0.007,
            100,
            1,
            10,
            1);
    
    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .build();
    }
}