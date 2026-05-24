package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.groups.Groups;
import com.pedropathing.paths.PathChain;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.*;
import static com.pedropathing.ivy.pedro.PedroCommands.*;

@Autonomous(name = "BlueAuto")
public class BlueAuto extends BaseAuto {
    public static Pose startPose = new Pose(36, 130, Math.toRadians(270));
    public static Pose scorePose = new Pose(36, 72, Math.toRadians(180));
    private PathChain scorePath;
    
    @Override
    protected Pose getStartPose() {
        return startPose;
    }
    
    @Override
    protected void buildPaths() {
        scorePath = follower.pathBuilder()
                .addPath(new BezierLine(startPose, scorePose))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .build();
    }
    
    @Override
    protected Command getRoutine() {
        return sequential(
                follow(follower, scorePath),
                race(
                        Groups.loop(hold(follower)),
                        waitMs(1000)
                )
        );
    }
}