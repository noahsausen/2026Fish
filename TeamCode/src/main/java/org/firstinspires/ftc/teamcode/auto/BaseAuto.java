package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.teamcode.framework.BaseOpMode;
import org.firstinspires.ftc.teamcode.framework.pedroPathing.Constants;

public abstract class BaseAuto extends BaseOpMode {
    protected Follower follower;
    protected abstract Pose getStartPose();
    protected abstract void buildPaths();
    protected abstract Command getRoutine();
    
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware(true);
        Scheduler.reset(); // Scheduler is static, reset to prevent carry over from previous op modes
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(getStartPose());
        buildPaths();
        Command routine = getRoutine();
        
        while (opModeInInit()) {
            initFinishedTelemetry();
            timingTelemetry();
            telemetry.update();
        }
        runtime.reset();
        
        Scheduler.schedule(routine);
        
        while (opModeIsActive()) {
            /* Ivy Scheduler and PP Follower don't run by themselves; these methods run one
             * cycles of each. This is great because we can update other things like telemetry
             * in the background, separate from Ivy. Conversely, RoadRunner uses one method that
             * continues running during the entire routine, forcing any other updates to be Actions.
             */
            Scheduler.execute();
            follower.update();
            
            // ↓ -------------- ↓ -------------- ↓ TELEMETRY ↓ -------------- ↓ -------------- ↓
            telemetry.addLine("PP Follower:");
            telemetry.addData("\tPose X", follower.getPose().getX());
            telemetry.addData("\tPose Y", follower.getPose().getY());
            telemetry.addData("\tPose H", follower.getPose().getHeading());
            telemetry.addData("\tBusy", follower.isBusy());
            telemetry.addData("\tT-Value", follower.getCurrentTValue());
            
            telemetry.addLine("\nIvy Scheduler:");
            telemetry.addData("Routine Scheduled", Scheduler.isScheduled(routine));
            telemetry.addData("Routine Running", Scheduler.isRunning(routine));
            
            timingTelemetry();
            telemetry.update();
        }
    }
}