package org.jberet.samples.ejb;

import java.util.logging.Level;

import jakarta.batch.api.BatchProperty;
import jakarta.batch.api.Batchlet;
import jakarta.batch.runtime.BatchStatus;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@Dependent
public class Batchlet1 implements Batchlet {
    @Inject
    private SuspendBatchLocal suspendBean;

    private volatile boolean stopRequested;

    @Inject
    @BatchProperty(name = "max.seconds")
    private Integer maxSeconds;

    @Override
    public String process() throws Exception {
        final BatchStatus status = suspendBean.getStatus();
        if (status == null || status == BatchStatus.STOPPING || status == BatchStatus.STOPPED) {
            // this batchlet is being restarted, so just complete it
            suspendBean.setStatus(BatchStatus.COMPLETED);
            SuspendBatchRemote.logger.log(Level.INFO, "Directly complete job execution with status: {0}", BatchStatus.COMPLETED);
            return BatchStatus.COMPLETED.name();
        }

        long endAt = maxSeconds * 1000 + System.currentTimeMillis();
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (stopRequested) {
                suspendBean.setStatus(BatchStatus.STOPPED);
                SuspendBatchRemote.logger.info("About to bail out after receiving stop request");
                return BatchStatus.STOPPED.name();
            }
            SuspendBatchRemote.logger.log(Level.INFO, "Waiting for stop request, or till max.seconds: {0}", maxSeconds);
        } while (System.currentTimeMillis() <= endAt);

        suspendBean.setStatus(BatchStatus.COMPLETED);
        return BatchStatus.COMPLETED.name();
    }

    @Override
    public void stop() {
        stopRequested = true;
        suspendBean.setStatus(BatchStatus.STOPPING);
    }
}
