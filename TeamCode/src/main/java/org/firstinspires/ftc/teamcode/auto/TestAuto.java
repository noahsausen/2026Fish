package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.*;
import static com.pedropathing.ivy.pedro.PedroCommands.*;

import org.firstinspires.ftc.teamcode.framework.BaseOpMode;
import org.firstinspires.ftc.teamcode.framework.pedroPathing.Constants;


@Autonomous(name = "TestAuto")
public class TestAuto extends BaseOpMode {
    private Follower follower;
    
    public static Pose startPose = new Pose(28.5, 128, Math.toRadians(180));
    public static Pose scorePose = new Pose(60, 85, Math.toRadians(135));
    private PathChain scorePath;
    private void buildPaths() { // add double endTime to LinearHeadingInterpolations to finish turning earlier
        scorePath = follower.pathBuilder()
                .addPath(new BezierLine(startPose, scorePose))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .build();
    }
    
    private Command buildRoutine() {
        return sequential(
                follow(follower, scorePath)
        );
    }
    
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware(true);
        Scheduler.reset(); // Scheduler is static, reset to prevent carry over from previous op modes
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        buildPaths();
        
        waitForStart();
        
        Command routine = buildRoutine();
        Scheduler.schedule(routine);
        
        while (opModeIsActive()) {
            Scheduler.execute();
            follower.update();
            
            // ↓ -------------- ↓ -------------- ↓ TELEMETRY ↓ -------------- ↓ -------------- ↓
            telemetry.addLine("PedroPathing Follower:");
            telemetry.addData("\tPose X", follower.getPose().getX());
            telemetry.addData("\tPose Y", follower.getPose().getY());
            telemetry.addData("\tHeading", follower.getPose().getHeading());
            telemetry.addData("\tBusy", follower.isBusy());
            
            telemetry.addLine("\nIvy Scheduler:");
            telemetry.addData("Routine Scheduled", Scheduler.isScheduled(routine));
            telemetry.addData("Routine Running", Scheduler.isRunning(routine));
            telemetry.update();
        }
    }
}