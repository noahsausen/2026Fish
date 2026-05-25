package org.firstinspires.ftc.teamcode.framework.hardware;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

public class Vision {
    private final Limelight3A limelight;
    public static int DEFAULT_PIPELINE = 0;
    private int currentPipeline = 0;
    
    public Vision(Limelight3A limelight3A) {
        limelight = limelight3A;
        limelight.setPollRateHz(100);
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
        if (limelight.isConnected() && limelight.isRunning()) {
            LLResult result = limelight.getLatestResult();
            if (result.getPipelineIndex() == currentPipeline) {
                return result;
            }
        }
        return null;
    }
}