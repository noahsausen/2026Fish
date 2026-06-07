package org.firstinspires.ftc.teamcode.framework.hardware;

import androidx.annotation.Nullable;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Configurable
public class Vision {
    private final Limelight3A limelight;
    public static int DEFAULT_PIPELINE = 0;
    public static int POLL_RATE_HZ = 100;
    public static long MAX_STALE_MILLIS = 30;
    private int currentPipeline;
    
    public Vision(Limelight3A limelight3A, LinearOpMode opMode) {
        limelight = limelight3A;
        setPipeline(DEFAULT_PIPELINE);
//        while (limelight.getStatus().getPipelineIndex() != currentPipeline && !opMode.isStopRequested()) {}
        limelight.setPollRateHz(POLL_RATE_HZ);
        limelight.start();
    }
    
    /**
     * Sends a request to set the pipeline of Limelight3A.
     * <br><br><i>Note: The pipeline may take some millis to update, but {@link #getValidResult()} checks this.</i>
     * @param index The index (0-9) of the wanted pipeline
     */
    public void setPipeline(int index) {
        limelight.pipelineSwitch(index);
        currentPipeline = index;
    }
    
    /**
     * Gets the latest result of Limelight3A, if: connected, running, valid (has target),
     * on the correct pipeline, and not too stale.
     * <br><br><i>Note: should return null when disconnected/not running, due to being invalid, but not confirmed.</i>
     * @return The latest {@link LLResult} if conditions are met; null otherwise
     */
    @Nullable
    public LLResult getValidResult() {
        if (limelight.isConnected() && limelight.isRunning()) {
            LLResult result = limelight.getLatestResult();
            if (result.isValid() && result.getPipelineIndex() == currentPipeline && result.getStaleness() < MAX_STALE_MILLIS) {
                return result;
            }
        }
        return null;
    }
    
    public LLResult getRawResult() {
        return limelight.getLatestResult();
    }
    
    /**
     * Requests the status of Limelight3A, if connected.
     * @return The response {@link LLStatus} if connected; null if not connected
     */
    @Nullable
    public LLStatus getStatus() {
        if (limelight.isConnected()) {
            return limelight.getStatus();
        }
        return null;
    }
}