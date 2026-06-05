package org.firstinspires.ftc.teamcode.auto;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.groups.Groups;
import com.pedropathing.paths.PathChain;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.*;
import static com.pedropathing.ivy.pedro.PedroCommands.*;

@Configurable
@Autonomous(name = "BlueAuto")
public class BlueAuto extends BaseAuto {
    public static int pauseMs = 1000; // TODO: optimize
    public static Pose startPose = new Pose(35.500, 133.700, Math.toRadians(0));
    public static Pose depositPose = new Pose(40.744, 60.303, Math.toRadians(180));
    public static Pose startRotateCtrl1 = new Pose(37.462, 126.566);
    public static Pose startRotatePose = new Pose(46.325, 125.841, Math.toRadians(15));
    public static Pose collect1Pose = new Pose(62.288, 126.045, Math.toRadians(15));
//    public static Pose score2Ctrl1 = new Pose(47.575, 110.872);
    public static Pose collect2Pose = new Pose(61.442, 115.677, Math.toRadians(10));
//    public static Pose score3Ctrl1 = new Pose(46.177, 99.077);
    public static Pose collect3Pose = new Pose(61.906, 103.591, Math.toRadians(5));
//    public static Pose score4Ctrl1 = new Pose(43.312, 92.903);
    public static Pose collect4Pose = new Pose(62.072, 94.129, Math.toRadians(0));
//    public static Pose score5Ctrl1 = new Pose(55.906, 90.373);
    public static Pose collect5Pose = new Pose(63.258, 75.366, Math.toRadians(0));
    public static Pose parkStep1Pose = new Pose(40.775, 70.865, Math.toRadians(90));
    public static Pose parkStep2Pose = new Pose(40.076, 30.972, Math.toRadians(90));
    public static Pose parkCtrl1 = new Pose(10.318, 35.229);
    public static Pose parkPose = new Pose(6.371, 10.612, Math.toRadians(90));
    private PathChain collect1Path, deposit1Path,
            collect2Path, deposit2Path,
            collect3Path, deposit3Path,
            collect4Path, deposit4Path,
            collect5Path, deposit5Path,
            parkPath;
    
    @Override
    protected Pose getStartPose() {
        return startPose;
    }
    
    @Override
    protected void buildPaths() {
        collect1Path = follower.pathBuilder()
                .addPath(new BezierCurve(startPose, startRotateCtrl1, startRotatePose))
                .setLinearHeadingInterpolation(startPose.getHeading(), startRotatePose.getHeading())
                .addPath(new BezierLine(startRotatePose, collect1Pose))
                .setLinearHeadingInterpolation(startRotatePose.getHeading(), collect1Pose.getHeading())
                .build();
        
        deposit1Path = follower.pathBuilder()
                .addPath(new BezierLine(collect1Pose, depositPose))
                .setLinearHeadingInterpolation(collect1Pose.getHeading(), depositPose.getHeading())
                .build();
                
        collect2Path = follower.pathBuilder()
                .addPath(new BezierLine(depositPose, collect2Pose))
                .setConstantHeadingInterpolation(collect2Pose.getHeading())
                .build();
        
        deposit2Path = follower.pathBuilder()
                .addPath(new BezierLine(collect2Pose, depositPose))
                .setLinearHeadingInterpolation(collect2Pose.getHeading(), depositPose.getHeading())
                .build();
                
        collect3Path = follower.pathBuilder()
                .addPath(new BezierLine(depositPose, collect3Pose))
                .setConstantHeadingInterpolation(collect3Pose.getHeading())
                .build();
        
        deposit3Path = follower.pathBuilder()
                .addPath(new BezierLine(collect3Pose, depositPose))
                .setLinearHeadingInterpolation(collect3Pose.getHeading(), depositPose.getHeading())
                .build();
                
        collect4Path = follower.pathBuilder()
                .addPath(new BezierLine(depositPose, collect4Pose))
                .setConstantHeadingInterpolation(collect4Pose.getHeading())
                .build();
        
        deposit4Path = follower.pathBuilder()
                .addPath(new BezierLine(collect4Pose, depositPose))
                .setLinearHeadingInterpolation(collect4Pose.getHeading(), depositPose.getHeading())
                .build();
                
        collect5Path = follower.pathBuilder()
                .addPath(new BezierLine(depositPose, collect5Pose))
                .setConstantHeadingInterpolation(collect5Pose.getHeading())
                .build();
        
        deposit5Path = follower.pathBuilder()
                .addPath(new BezierLine(collect5Pose, depositPose))
                .setLinearHeadingInterpolation(collect5Pose.getHeading(), depositPose.getHeading())
                .build();
        
        parkPath = follower.pathBuilder()
                .addPath(new BezierLine(depositPose, parkStep1Pose))
                .setLinearHeadingInterpolation(depositPose.getHeading(), parkStep1Pose.getHeading())
                .addPath(new BezierLine(parkStep1Pose, parkStep2Pose))
                .setLinearHeadingInterpolation(parkStep1Pose.getHeading(), parkStep2Pose.getHeading())
                .addPath(new BezierCurve(parkStep2Pose, parkCtrl1, parkPose))
                .setLinearHeadingInterpolation(parkStep2Pose.getHeading(), parkPose.getHeading())
                .build();
    }
    
    @Override
    protected Command getRoutine() {
        return sequential(
                sequential(
                        instant(intake::on),
                        follow(follower, collect1Path),
                        waitMs(pauseMs),
                        instant(intake::off),
                        follow(follower, deposit1Path),
                        instant(intake::reverse),
                        waitMs(pauseMs)
                ),
                sequential(
                        instant(intake::on),
                        follow(follower, collect2Path),
                        waitMs(pauseMs),
                        instant(intake::off),
                        follow(follower, deposit2Path),
                        instant(intake::reverse),
                        waitMs(pauseMs)
                ),
                sequential(
                        instant(intake::on),
                        follow(follower, collect3Path),
                        waitMs(pauseMs),
                        instant(intake::off),
                        follow(follower, deposit3Path),
                        instant(intake::reverse),
                        waitMs(pauseMs)
                ),
                sequential(
                        instant(intake::on),
                        follow(follower, collect4Path),
                        waitMs(pauseMs),
                        instant(intake::off),
                        follow(follower, deposit4Path),
                        instant(intake::reverse),
                        waitMs(pauseMs)
                ),
                sequential(
                        instant(intake::on),
                        follow(follower, collect5Path),
                        waitMs(pauseMs),
                        instant(intake::off),
                        follow(follower, deposit5Path),
                        instant(intake::reverse),
                        waitMs(pauseMs)
                ),
                instant(intake::off),
                follow(follower, parkPath),
                Groups.loop(hold(follower)) // TODO: keep?
        );
    }
}