package org.firstinspires.ftc.teamcode.framework.hardware;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Vision {
    private final Limelight3A limelight;
    public static int DEFAULT_PIPELINE = 0;
    public static int POLL_RATE_HZ = 100;
    public static long MAX_STALE_MILLIS = 30;
    private int currentPipeline = DEFAULT_PIPELINE;
    
    public Vision(Limelight3A limelight3A, LinearOpMode opMode) {
        limelight = limelight3A;
        setPipeline(DEFAULT_PIPELINE);
        while (limelight.getStatus().getPipelineIndex() != currentPipeline && !opMode.isStopRequested()) {}
        limelight.setPollRateHz(POLL_RATE_HZ);
        limelight.start();
    }
    
    public void setPipeline(int index) {
        limelight.pipelineSwitch(index);
        currentPipeline = index;
    }
    
    public LLResult getResult() {
        if (limelight.isConnected() && limelight.isRunning()) {
            LLResult result = limelight.getLatestResult();
            if (result != null) {
                if (result.isValid() && result.getPipelineIndex() == currentPipeline && result.getStaleness() < MAX_STALE_MILLIS) {
                    return result;
                }
            }
        }
        return null;
    }
    
    public LLStatus getStatus() {
        if (limelight.isConnected()) {
            return limelight.getStatus();
        }
        return null;
    }
}