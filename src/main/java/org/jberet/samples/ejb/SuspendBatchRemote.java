package org.jberet.samples.ejb;

import java.util.logging.Logger;

import jakarta.batch.runtime.BatchStatus;
import jakarta.ejb.Remote;

@Remote
public interface SuspendBatchRemote {
    static final Logger logger = Logger.getLogger(SuspendBatchRemote.class.getPackageName());

    BatchStatus getStatus();

    void startJob(String jobXmlName, int maxSeconds);
}
