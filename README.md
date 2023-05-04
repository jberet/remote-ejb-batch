# remote-ejb-batch
Start batch job execution from WildFly remote ejb client

## Add application user to WildFly
```
$JBOSS_HOME/bin/add-user.sh -a -u user1 -p user1
```

## Build the application
```
mvn clean package
```

## Deploy the webapp to a running WildFly server
```text
mvn wildfly:deploy
```

## Run client program
```
# to run client program as a guest without providing username:
mvn exec:exec

# to run client program as user1:
mvn exec:exec -Duser=user1 -Dpassword=user1
```

The client program calls a remote EJB, which in turn starts running the batch job named `job1`.
After some duration, the job execution should complete successfully.

In additional, you may want to experiment with server suspend and resume, and see how the
running batch job execution is stopped during suspending, and automatically restarted during
server resume.

## Server suspend / resume, and batch job execution stopping / restarting
### suspend from WildFly CLI

Right after running the above client program, suspend the server with the following CLI command:
```
$JBOSS_HOME/jboss-cli.sh --connect

[standalone@localhost:9990 /] :suspend
{"outcome" => "success"}
```

Notice in the server log are some log entries indicating the server is being suspending,
and the running batch job execution is being stopped.

## resume the server and automatically restarting the stopped job execution
```text
[standalone@localhost:9990 /] :resume
{"outcome" => "success"}
```

Verify in the server log that the stopped job execution is automatically restarted,
from where it left off.

## Undeploy the webapp from WildFly server
```text
mvn wildfly:undeploy
```