package org.jberet.samples.ejb;

import java.util.Properties;
import java.util.logging.Level;

import jakarta.batch.operations.JobOperator;
import jakarta.batch.runtime.BatchStatus;
import jakarta.ejb.ConcurrencyManagement;
import jakarta.ejb.ConcurrencyManagementType;
import jakarta.ejb.Singleton;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.inject.Inject;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class SuspendBatchSingleton implements SuspendBatchLocal, SuspendBatchRemote {
    @Inject
    private JobOperator jobOperator;

    private BatchStatus status;

    @Override
    public synchronized void setStatus(final BatchStatus status) {
        this.status = status;
    }

    @Override
    public synchronized BatchStatus getStatus() {
        return status;
    }

    @Override
    public void startJob(final String jobXmlName, final int maxSeconds) {
        setStatus(BatchStatus.STARTING);
        final Properties properties = new Properties();
        properties.setProperty("max.seconds", String.valueOf(maxSeconds));
        jobOperator.start(jobXmlName, properties);
        SuspendBatchRemote.logger.log(Level.INFO, "Starting job {0} with maxSeconds {1}", new Object[]{jobXmlName, maxSeconds});
    }
}
