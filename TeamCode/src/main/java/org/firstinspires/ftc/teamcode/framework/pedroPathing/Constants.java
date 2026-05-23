package org.firstinspires.ftc.teamcode.framework.pedroPathing;

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

public class Constants { // TODO: 5. other tuning after following todos
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(2); // kilograms -- TODO: 1. measure mass
    
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
            .forwardPodY(0) // TODO: 4. offsets tuner
            .strafePodX(0)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinpoint") // TODO: 2. check: i2c port not 0, hardware map matches
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD) // TODO: 3. forward increases x
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD); // left increases y
    
    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);
    
    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .build();
    }
}