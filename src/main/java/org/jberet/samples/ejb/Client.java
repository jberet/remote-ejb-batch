package org.jberet.samples.ejb;

import java.util.Hashtable;
import java.util.logging.Level;
import javax.naming.Context;
import javax.naming.InitialContext;

public final class Client {
    private static final String ARCHIVE_NAME = "remote-ejb-batch";
    private static final String JOB_XML_NAME = "job1.xml";
    private static final int MAX_SECONDS = 10;
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        final String lookupName = "ejb:/" + ARCHIVE_NAME + "/"
                + SuspendBatchSingleton.class.getSimpleName() + "!"
                + SuspendBatchRemote.class.getName();
        Context jndiContext = getRemoteContext();
        final SuspendBatchRemote bean = (SuspendBatchRemote) jndiContext.lookup(lookupName);
        bean.startJob(JOB_XML_NAME, MAX_SECONDS);
        jndiContext.close();
    }

    private static Context getRemoteContext() throws Exception {
        final String user = System.getProperty("user");
        final String password = System.getProperty("password");
        SuspendBatchRemote.logger.log(Level.INFO, "look up with user \"{0}\", password \"{1}\"", new Object[]{user, password});

        final Hashtable<String, String> props = new Hashtable<>();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        props.put(Context.PROVIDER_URL, String.format("%s://%s:%d", "remote+http", HOST, PORT));

        if (user != null && password != null) {
            props.put(Context.SECURITY_PRINCIPAL, user);
            props.put(Context.SECURITY_CREDENTIALS, password);
        }
        return new InitialContext(props);
    }
}
