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
    public static int scorePauseMs = 2000; // TODO: optimize
    public static Pose startPose = new Pose(35.500, 133.700, Math.toRadians(0));
    public static Pose startRotateCtrl1 = new Pose(37.462, 126.566);
    public static Pose startRotatePose = new Pose(46.325, 125.841, Math.toRadians(57));
    public static Pose score1Pose = new Pose(60.288, 126.045, Math.toRadians(57));
    public static Pose score2Ctrl1 = new Pose(47.575, 110.872);
    public static Pose score2Pose = new Pose(61.442, 115.677, Math.toRadians(52));
    public static Pose score3Ctrl1 = new Pose(46.177, 99.077);
    public static Pose score3Pose = new Pose(61.906, 103.591, Math.toRadians(48));
    public static Pose score4Ctrl1 = new Pose(43.312, 92.903);
    public static Pose score4Pose = new Pose(62.072, 94.129, Math.toRadians(40));
    public static Pose score5Ctrl1 = new Pose(55.906, 90.373);
    public static Pose score5Pose = new Pose(63.258, 80.366, Math.toRadians(15));
    public static Pose parkStep1Pose = new Pose(33.775, 70.865, Math.toRadians(90));
    public static Pose parkStep2Pose = new Pose(33.076, 34.972, Math.toRadians(90));
    public static Pose parkCtrl1 = new Pose(7.318, 35.229);
    public static Pose parkPose = new Pose(9.371, 10.612, Math.toRadians(90));
    private PathChain score1Path, score2Path, score3Path, score4Path, score5Path, parkPath;
    
    @Override
    protected Pose getStartPose() {
        return startPose;
    }
    
    @Override
    protected void buildPaths() {
        score1Path = follower.pathBuilder()
                .addPath(new BezierCurve(startPose, startRotateCtrl1, startRotatePose))
                .setLinearHeadingInterpolation(startPose.getHeading(), startRotatePose.getHeading())
                .addPath(new BezierLine(startRotatePose, score1Pose))
                .setLinearHeadingInterpolation(startRotatePose.getHeading(), score1Pose.getHeading())
                .build();
                
        score2Path = follower.pathBuilder()
                .addPath(new BezierCurve(score1Pose, score2Ctrl1, score2Pose))
                .setLinearHeadingInterpolation(score1Pose.getHeading(), score2Pose.getHeading())
                .build();
                
        score3Path = follower.pathBuilder()
                .addPath(new BezierCurve(score2Pose, score3Ctrl1, score3Pose))
                .setLinearHeadingInterpolation(score2Pose.getHeading(), score3Pose.getHeading())
                .build();
                
        score4Path = follower.pathBuilder()
                .addPath(new BezierCurve(score3Pose, score4Ctrl1, score4Pose))
                .setLinearHeadingInterpolation(score3Pose.getHeading(), score4Pose.getHeading())
                .build();
                
        score5Path = follower.pathBuilder()
                .addPath(new BezierCurve(score4Pose, score5Ctrl1, score5Pose))
                .setLinearHeadingInterpolation(score4Pose.getHeading(), score5Pose.getHeading())
                .build();
        
        parkPath = follower.pathBuilder()
                .addPath(new BezierLine(score5Pose, parkStep1Pose))
                .setLinearHeadingInterpolation(score5Pose.getHeading(), parkStep1Pose.getHeading())
                .addPath(new BezierLine(parkStep1Pose, parkStep2Pose))
                .setLinearHeadingInterpolation(parkStep1Pose.getHeading(), parkStep2Pose.getHeading())
                .addPath(new BezierCurve(parkStep2Pose, parkCtrl1, parkPose))
                .setLinearHeadingInterpolation(parkStep2Pose.getHeading(), parkPose.getHeading())
                .build();
    }
    
    @Override
    protected Command getRoutine() {
        return sequential(
                instant(() -> intake.on()),
                follow(follower, score1Path),
                waitMs(scorePauseMs),
                follow(follower, score2Path),
                waitMs(scorePauseMs),
                follow(follower, score3Path),
                waitMs(scorePauseMs),
                follow(follower, score4Path),
                waitMs(scorePauseMs),
                follow(follower, score5Path),
                waitMs(scorePauseMs),
                instant(() -> intake.off()),
                follow(follower, parkPath),
                Groups.loop(hold(follower)) // TODO: keep?
        );
    }
}