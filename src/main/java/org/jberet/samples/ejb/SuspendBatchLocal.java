package org.jberet.samples.ejb;

import jakarta.batch.runtime.BatchStatus;
import jakarta.ejb.Local;

@Local
public interface SuspendBatchLocal {
    BatchStatus getStatus();

    void setStatus(BatchStatus status);
}
