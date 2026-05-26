package org.firstinspires.ftc.teamcode.framework.hardware;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;

public class Vision {
    private final Limelight3A limelight;
    public static int DEFAULT_PIPELINE = 0;
    public static int POLL_RATE_HZ = 100; // TODO: consider setting this automatically based on fps
    public static long MAX_STALENESS_MULTIPLIER = 3;
    private int currentPipeline = 0;
    
    public Vision(Limelight3A limelight3A) {
        limelight = limelight3A;
        limelight.setPollRateHz(POLL_RATE_HZ);
        limelight.start();
        setDefaultPipeline();
    }
    
    public void setPipeline(int index) {
        limelight.pipelineSwitch(index);
        currentPipeline = index;
    }
    
    public void setDefaultPipeline() {
        setPipeline(DEFAULT_PIPELINE);
    }
    
    public LLResult getResult() {
        long maxStaleness = (1 / POLL_RATE_HZ) * 1000 * MAX_STALENESS_MULTIPLIER;
        if (limelight.isConnected() && limelight.isRunning()) {
            LLResult result = limelight.getLatestResult();
            if (result != null) {
                if (result.isValid()) {
                    if (result.getPipelineIndex() == currentPipeline && result.getStaleness() < maxStaleness) {
                        return result;
                    }
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